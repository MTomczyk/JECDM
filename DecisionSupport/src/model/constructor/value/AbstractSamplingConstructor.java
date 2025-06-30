package model.constructor.value;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.AbstractConstructor;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;
import space.normalization.INormalization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Abstract class that provides fundamentals for sampling-based model constructors.
 *
 * @author MTomczyk
 */
public abstract class AbstractSamplingConstructor<T extends AbstractValueInternalModel>
        extends AbstractConstructor<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel>
    {
        /**
         * The number of feasible samples the method is supposed to generate. This number should be positive. If it is
         * not, it is set to 1.
         */
        public int _feasibleSamplesToGenerate = 1;

        /**
         * The threshold for the number of generated compatible samples. The overall processing result is considered
         * inconsistent if the number is smaller or equal to this threshold.
         */
        public int _inconsistencyThreshold = 0;

        /**
         * If true, the method should first check the already existing samples and preserves those that are compatible
         * with the current preference information set. When false, the method should generate everything from scratch.
         */
        public boolean _validateAlreadyExistingSamplesFirst = true;

        /**
         * Auxiliary object responsible for calculating the degree to which a given preference information is compatible
         * with an input preference model.
         */
        public CompatibilityAnalyzer _compatibilityAnalyzer = new CompatibilityAnalyzer();

        /**
         * Initial models that can be optionally supplied (can be null). The extensions are supposed to initially feel
         * the model sets constructed during initialization/restarts.
         */
        public T[] _initialModels;
    }

    /**
     * Interface for auxiliary objects responsible for updating normalizations in interested receivers (called during
     * DM-context registration, only when the data on the current OS indicates recent change)
     */
    protected interface INormalizationsUpdater
    {
        /**
         * The main method.
         *
         * @param normalizations new normalization
         */
        void update(INormalization[] normalizations);
    }

    /**
     * The number of feasible samples the method is supposed to generate. This number should be positive. If it is not,
     * it is set to 1.
     */
    protected int _feasibleSamplesToGenerate;

    /**
     * The threshold for the number of generated compatible samples. The overall processing result is considered
     * inconsistent if the number is smaller or equal to this threshold.
     */
    protected final int _inconsistencyThreshold;

    /**
     * If true, the method should first check the already existing samples and preserves those that are compatible with
     * the current preference information set. When false, the method should generate everything from scratch.
     */
    protected final boolean _validateAlreadyExistingSamplesFirst;

    /**
     * Captured RNG.
     */
    protected IRandom _R;

    /**
     * Initial models that can be optionally supplied (can be null). The extensions are supposed to initially feel the
     * model sets constructed during initialization/restarts.
     */
    protected final T[] _initialModels;

    /**
     * Field representing in how many iterations the required number of compatible models has been found (null, if the
     * task was not achieved). The counter is zeroed during initialization step.
     * {@link AbstractSamplingConstructor#initializeStep(Report, LinkedList)}.
     */
    protected Integer _compatibleFoundInIterations = null;

    /**
     * Field representing in what time [ms] the required number of compatible models has been found (null, if the task
     * was not achieved). The counter is zeroed during initialization step.
     * {@link AbstractSamplingConstructor#initializeStep(Report, LinkedList)}.
     */
    protected Double _compatibleFoundInTime = null;

    /**
     * Auxiliary starting processing time (passed; in ns).
     */
    protected long _passedTime;

    /**
     * Registered normalization updaters.
     */
    protected LinkedList<INormalizationsUpdater> _normalizationUpdaters;

    /**
     * Parameterized constructor.
     *
     * @param name name of the model constructor
     * @param p    params container
     */
    public AbstractSamplingConstructor(String name, Params<T> p)
    {
        super(name, p._compatibilityAnalyzer);
        _inconsistencyThreshold = p._inconsistencyThreshold;
        _validateAlreadyExistingSamplesFirst = p._validateAlreadyExistingSamplesFirst;
        _feasibleSamplesToGenerate = p._feasibleSamplesToGenerate;
        _initialModels = p._initialModels;
        if (_feasibleSamplesToGenerate < 0) _feasibleSamplesToGenerate = 1;

        _normalizationUpdaters = new LinkedList<>();
        _normalizationUpdaters.add(this::supplyModelsWithNormalizations);
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws ConstructorException
    {
        super.registerDecisionMakingContext(dmContext);
        if ((!_normalizationsSuppliedAtLeastOnce) || (dmContext.isOsChanged()))
        {
            // provide for generator
            _normalizationsSuppliedAtLeastOnce = true;
            _normalizationsWereUpdated = true;
            for (INormalizationsUpdater updater : _normalizationUpdaters)
                updater.update(dmContext.getNormalizationsCurrentOS());
        }
    }

    /**
     * Auxiliary method that performs simple validation of parameters that are relevant to
     * {@link AbstractSamplingConstructor#mainConstructModels(Report, LinkedList)}.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    protected void validate(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        if (bundle == null)
            throw new ConstructorException("The bundle result object is not provided (the reference is null)", this.getClass());
        if (preferenceInformation == null)
            throw new ConstructorException("The preference examples are not provided (the list is null)", this.getClass());
        IRandom R = _dmContext.getR();
        if (R == null)
            throw new ConstructorException("The random number generator is not supplied by the decision-making context", this.getClass());
    }

    /**
     * When called, the models are supplied with {@link AbstractSamplingConstructor#_initialModels} (if provided).
     * Method terminated if {@link AbstractConstructor#_models} is not empty.
     *
     * @return false if the method prematurely terminated; false otherwise
     */
    protected boolean attemptToSupplyInitialModels()
    {
        if ((_models != null) && (!_models.isEmpty())) return false;
        if (_initialModels == null) return false;
        if (_initialModels.length == 0) return false;
        if (_models == null) _models = new ArrayList<>(_feasibleSamplesToGenerate);
        _models.addAll(Arrays.asList(_initialModels));
        return true;
    }

    /**
     * Auxiliary method that can be called to clear all stored models (can be called, e.g., when the inconsistency was
     * detected). This extension additionally runs {@link AbstractSamplingConstructor#attemptToSupplyInitialModels()}.
     */
    @Override
    public void clearModels()
    {
        super.clearModels();
        attemptToSupplyInitialModels();
    }

    /**
     * This method is called by this class extensions at the beginning of the model construction process when
     * identifying models that remain compatible after receiving the most recent feedback.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return models that remain compatible
     */
    protected ArrayList<T> executePreservationAttempt(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation)
    {
        // preservation attempt
        bundle._modelsPreservedBetweenIterations = 0;
        bundle._modelsRejectedBetweenIterations = 0;
        bundle._successRateInPreserving = 0.0d;

        ArrayList<T> preservedModels = new ArrayList<>(_feasibleSamplesToGenerate);

        if ((_validateAlreadyExistingSamplesFirst) && (_models != null))
        {
            // case only when there was a successful preference elicitation and there is no inconsistency handling
            // in that case, check just the newest PI
            if ((_preferenceElicitationAttempted) && (!_preferenceElicitationFailed)
                    && (_mostRecentPreferenceInformation != null) && (!_inconsistencyHandlingMode))
            {
                LinkedList<PreferenceInformationWrapper> mr = new LinkedList<>(_mostRecentPreferenceInformation);
                preservedModels = retrieveFeasibleModels(mr);
            }
            else preservedModels = retrieveFeasibleModels(preferenceInformation);

            bundle._modelsPreservedBetweenIterations = preservedModels.size();
            bundle._modelsRejectedBetweenIterations = _models.size() - preservedModels.size();
            if (!_models.isEmpty()) bundle._successRateInPreserving = (double) preservedModels.size() / _models.size();
        }
        return preservedModels;
    }

    /**
     * Auxiliary method that attempts to retrieve those models that remain feasible.
     *
     * @param preferenceInformation preference examples (provided via wrappers)
     * @return models that remain feasible
     */
    protected ArrayList<T> retrieveFeasibleModels(LinkedList<PreferenceInformationWrapper> preferenceInformation)
    {
        ArrayList<T> R = new ArrayList<>();
        if ((_models == null) || (_models.isEmpty())) return R;
        for (T M : _models)
        {
            Double a = _compatibilityAnalyzer.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, M);
            if (Double.compare(a, 0.0d) > 0) R.add(M);
        }
        return R;
    }

    /**
     * The main-construct models phase (to be overwritten). The concrete extension should provide the constructed models
     * via the bundle object.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    protected void mainConstructModels(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        throw new ConstructorException("The mainConstructModels method is not implemented", this.getClass());
    }


    /**
     * Execute the initialize step (can be overwritten)
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return indicates whether to prematurely terminate (true)
     * @throws ConstructorException the exception can be thrown 
     */
    protected boolean initializeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        validate(bundle, preferenceInformation);
        _passedTime = 0;
        _compatibleFoundInIterations = null;
        _compatibleFoundInTime = null;
        return false;
    }

    /**
     * Execute a single sampling step (can be overwritten)
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return returns the constructed model
     * @throws ConstructorException the exception can be thrown 
     */
    protected T executeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        return null;
    }

    /**
     * Execute the finalize step (can be overwritten)
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    protected void finalizeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {

    }

    /**
     * Getter for the field representing in how many iterations the required number of compatible models has been found
     * (null, if the task was not achieved). The counter is zeroed during initialization step.
     * {@link AbstractSamplingConstructor#initializeStep(Report, LinkedList)}.
     *
     * @return iterations required to find a desired number of compatible models (null, if not found (yet))
     */
    public Integer getCompatibleFoundInIterations()
    {
        return _compatibleFoundInIterations;
    }

    /**
     * Getter for the field representing in what time [ms] the required number of compatible models has been found
     * (null, if the task was not achieved). The counter is zeroed during initialization step.
     * {@link AbstractSamplingConstructor#initializeStep(Report, LinkedList)}.
     *
     * @return time in ms required to find a desired number of compatible models (null, if not found (yet))
     */
    public Double getCompatibleFoundInTime()
    {
        return _compatibleFoundInTime;
    }
}
