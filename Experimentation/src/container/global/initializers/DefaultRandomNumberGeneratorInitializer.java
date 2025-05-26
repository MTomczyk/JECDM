package container.global.initializers;

import exception.GlobalException;
import exception.ScenarioException;
import random.IRandom;
import random.MersenneTwister64;
import random.XoRoShiRo128PP;
import scenario.Scenario;

import java.util.Objects;

/**
 * Default initialized that construct RNGs on demand {@link IRandomNumberGeneratorInitializer#getRNG(Scenario, int, int)})
 * using a non-colliding and sequential seed.
 *
 * @author MTomczyk
 */
public class DefaultRandomNumberGeneratorInitializer implements IRandomNumberGeneratorInitializer
{
    /**
     * Auxiliary interface for constructing the instance of RNG based on a simple seed.
     */
    public interface IInstanceConstructor
    {
        /**
         * The main method for constructing the instance of RNG.
         *
         * @param seed seed
         * @return instance of RNG
         */
        IRandom getInstance(int seed);
    }

    /**
     * RNG instance constructor.
     */
    private final IInstanceConstructor _instanceConstructor;

    /**
     * Default constructor. Parameterizes the class instance to operate with {@link MersenneTwister64}.
     */
    public DefaultRandomNumberGeneratorInitializer()
    {
        this(null);
    }

    /**
     * Parameterized constructor.
     *
     * @param instanceConstructor RNG instance constructor; if null, the class instance is parameterized to operate
     *                            with {@link MersenneTwister64}.
     */
    public DefaultRandomNumberGeneratorInitializer(IInstanceConstructor instanceConstructor)
    {
        _instanceConstructor = Objects.requireNonNullElseGet(instanceConstructor, () -> MersenneTwister64::new);
    }

    /**
     * Auxiliary method. It is called by {@link executor.ExperimentPerformer} at the beginning of the processing
     * to request RNG streams creation. An implementation may then construct such streams and return them
     * when {@link IRandomNumberGeneratorInitializer#getRNG(Scenario, int, int)} is called. This implementation
     * does not use this functionality.
     *
     * @param noScenarios total number of scenarios (should include even disabled ones)
     * @param noTrials    total number of trials per scenario
     * @throws GlobalException the exception will never be thrown by this implementation
     */
    @Override
    public void requestStreamsCreationDuringGDCInit(int noScenarios, int noTrials) throws GlobalException
    {
        // not used
    }


    /**
     * Auxiliary method. It is called by {@link executor.ExperimentPerformer} at the beginning of the processing
     * to request RNG streams creation. An implementation may then construct such streams and return them
     * when {@link IRandomNumberGeneratorInitializer#getRNG(Scenario, int, int)} is called. This implementation
     * does not use this functionality.
     *
     * @param scenario scenario being processed
     * @param noTrials total number of trials per scenario
     * @throws ScenarioException the exception will never be thrown by this implementation
     */
    @Override
    public void requestStreamsCreationDuringSDCInit(Scenario scenario, int noTrials) throws ScenarioException
    {
        // not used
    }

    /**
     * Main method.
     *
     * @param scenario trial's scenario requesting the random number generator
     * @param trialID  ID of a trial requesting the random number generator
     * @param noTrials total number of trials per scenario
     * @return random number generator
     * @throws ScenarioException the exception will be thrown if the RGN cannot be instantiated
     */
    public IRandom getRNG(Scenario scenario, int trialID, int noTrials) throws ScenarioException
    {
        int seed = scenario.getID() * noTrials + trialID;
        if (_instanceConstructor == null) throw new ScenarioException("The RNG instance constructor is null",
                null, this.getClass(), scenario);
        IRandom R = _instanceConstructor.getInstance(seed);
        if (R == null) throw new ScenarioException("The instance constructor did not created an RGN (returned null)",
                null, this.getClass(), scenario);
        return _instanceConstructor.getInstance(seed);
    }
}
