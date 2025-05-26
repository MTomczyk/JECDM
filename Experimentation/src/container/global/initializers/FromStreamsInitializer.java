package container.global.initializers;

import exception.GlobalException;
import exception.ScenarioException;
import random.IRandom;
import scenario.Scenario;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * This implementation used the {@link IRandomNumberGeneratorInitializer#requestStreamsCreationDuringGDCInit(int, int)}
 * or {@link IRandomNumberGeneratorInitializer#requestStreamsCreationDuringSDCInit(Scenario, int)} method to generate
 * streams first, i.e., before executing experiments. Then, when {@link IRandomNumberGeneratorInitializer#getRNG(Scenario, int, int)}
 * is called, a suitable stream is determined and returned. Note that the constructed {@link IRandom} objects must be either
 * jumpable or splittable.
 *
 * @author MTomczyk
 */
public class FromStreamsInitializer implements IRandomNumberGeneratorInitializer
{
    /**
     * Streams creation mode.
     */
    public enum Mode
    {
        /**
         * Jumpable mode: calls {@link IRandom#createInstancesViaJumps(int)} on the reference RNG.
         */
        JUMPABLE,

        /**
         * Splittable mode: calls {@link IRandom#createSplitInstances(int)} on the reference RNG.
         */
        SPLITTABLE
    }

    /**
     * Constants representing when the RNGs' streams should be instantiated.
     */
    public enum InitializationStage
    {
        /**
         * RNGs' streams will be instantiated during GDC initialization. all instances that are required will be
         * initialized at once.
         */
        GLOBAL,

        /**
         * RNGs' streams will be instantiated during SDC initialization. All instances that are required to execute
         * the scenario being processed will be initialized at once.
         */
        SCENARIO,
    }

    /**
     * Auxiliary interface for constructing an initial instance of RNG.
     */
    public interface IReferenceRNGConstructor
    {
        /**
         * The main method for constructing an initial instance of RNG
         *
         * @return instance of RNG
         */
        IRandom getInstance();
    }

    /**
     * Streams creation mode.
     */
    protected final Mode _mode;

    /**
     * Constant representing when the RNGs' streams should be instantiated.
     */
    protected final InitializationStage _initializationStage;

    /**
     * Reference, i.e., initial RNG.
     */
    protected final IRandom _referenceRNG;

    /**
     * RNG streams.
     */
    protected ArrayList<IRandom> _streams;

    /**
     * Parameterized constructor.
     *
     * @param mode                streams creation mode
     * @param initializationStage streams initialization stage
     * @param instanceConstructor initial instance constructor; note that the constructed implementation of {@link IRandom}
     *                            must be either jumpable or splittable, as imposed by the passed mode option; if not, an exception
     *                            will be thrown during streams initialization
     */
    public FromStreamsInitializer(Mode mode, InitializationStage initializationStage, IReferenceRNGConstructor instanceConstructor)
    {
        _mode = mode;
        if (instanceConstructor != null) _referenceRNG = instanceConstructor.getInstance();
        else _referenceRNG = null;
        _initializationStage = initializationStage;
        _streams = null;
    }

    /**
     * Creates streams during GDC initialization. Their number equals to no. scenarios x no. trials.
     *
     * @param noScenarios total number of scenarios (should include even disabled ones)
     * @param noTrials    total number of trials per scenario
     * @throws GlobalException the exception will be thrown if the streams cannot be instantiated
     */
    @Override
    public void requestStreamsCreationDuringGDCInit(int noScenarios, int noTrials) throws GlobalException
    {
        if (_initializationStage != InitializationStage.GLOBAL) return;
        _streams = null;
        if (_referenceRNG == null)
            throw new GlobalException("The reference RNG is not supplied", null, this.getClass());
        int total = noScenarios * noTrials;
        _streams = new ArrayList<>(total);
        Stream<IRandom> s;
        if (_mode == Mode.JUMPABLE)
        {
            if (!_referenceRNG.isJumpable()) throw new GlobalException("The reference RNG is not jumpable (" +
                    _referenceRNG + ")", null, this.getClass());
            s = _referenceRNG.createInstancesViaJumps(total);
        }
        else
        {
            if (!_referenceRNG.isSplittable()) throw new GlobalException("The reference RNG is not splittable (" +
                    _referenceRNG + ")", null, this.getClass());
            s = _referenceRNG.createSplitInstances(total);
        }
        if (s == null) throw new GlobalException("RNGs are not constructed (are null)" +
                _referenceRNG + ")", null, this.getClass());
        _streams.addAll(s.toList());
    }

    /**
     * Creates streams during SDC initialization. Their number equals to no. trials.
     *
     * @param scenario scenario being processed
     * @param noTrials total number of trials per scenario
     * @throws ScenarioException the exception will be thrown if the streams cannot be instantiated
     */
    public void requestStreamsCreationDuringSDCInit(Scenario scenario, int noTrials) throws ScenarioException
    {
        if (_initializationStage != InitializationStage.SCENARIO) return;
        if (_referenceRNG == null)
            throw new ScenarioException("The reference RNG is not supplied", null, this.getClass(), scenario);

        if ((_streams != null) && (!_streams.isEmpty())) _streams.clear();

        _streams = new ArrayList<>(noTrials);
        Stream<IRandom> s;
        if (_mode == Mode.JUMPABLE)
        {
            if (!_referenceRNG.isJumpable()) throw new ScenarioException("The reference RNG is not jumpable (" +
                    _referenceRNG + ")", null, this.getClass(), scenario);
            s = _referenceRNG.createInstancesViaJumps(noTrials);
        }
        else
        {
            if (!_referenceRNG.isSplittable()) throw new ScenarioException("The reference RNG is not splittable (" +
                    _referenceRNG + ")", null, this.getClass(), scenario);
            s = _referenceRNG.createSplitInstances(noTrials);
        }
        if (s == null) throw new ScenarioException("RNGs are not constructed (are null)" +
                _referenceRNG + ")", null, this.getClass(), scenario);
        _streams.addAll(s.toList());
    }

    /**
     * Returns a suitable RNG from the pre-constructed streams.
     *
     * @param scenario trial's scenario requesting the random number generator
     * @param trialID  ID of a trial requesting the random number generator
     * @param noTrials total number of trials per scenario
     * @return pre-constructed random number generator
     * @throws ScenarioException the exception will be thrown if the RGN cannot be retrieved
     */
    @Override
    public IRandom getRNG(Scenario scenario, int trialID, int noTrials) throws ScenarioException
    {
        if ((_streams == null) || (_streams.isEmpty()))
            throw new ScenarioException("RNGs are not available (the array is null or empty)", null, this.getClass(), scenario);
        int id;
        if (_initializationStage == InitializationStage.GLOBAL) id = scenario.getID() * noTrials + trialID;
        else id = trialID;
        if (_streams.size() <= id)
            throw new ScenarioException("There is not enough streams (available = " + _streams.size() + "; requested index = " + id + ")", null, this.getClass(), scenario);
        if (_streams.get(id) == null)
            throw new ScenarioException("The requested stream is null", null, this.getClass(), scenario);
        return _streams.get(id);
    }
}
