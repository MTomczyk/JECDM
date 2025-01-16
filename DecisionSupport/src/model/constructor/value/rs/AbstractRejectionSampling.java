package model.constructor.value.rs;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.AbstractConstructor;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Abstract class that provides fundamentals for rejection-sampling-based model constructors (e.g., {@link FRS}).
 *
 * @author MTomczyk
 */
public abstract class AbstractRejectionSampling<T extends AbstractValueInternalModel>
        extends AbstractConstructor<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel>
    {
        /**
         * Random model generator.
         */
        public final IRandomModel<T> _RM;

        /**
         * The number of feasible samples the method is supposed to generate. This number should be positive.
         * If it is not, it is set to 1.
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
         * Parameterized constructor.
         *
         * @param RM random model generator
         */
        public Params(IRandomModel<T> RM)
        {
            _RM = RM;
        }
    }

    /**
     * Random model generator.
     */
    protected final IRandomModel<T> _RM;

    /**
     * The number of feasible samples the method is supposed to generate. This number should be positive.
     * If it is not, it is set to 1.
     */
    protected int _feasibleSamplesToGenerate;

    /**
     * The threshold for the number of generated compatible samples. The overall processing result is considered
     * inconsistent if the number is smaller or equal to this threshold.
     */
    protected final int _inconsistencyThreshold;

    /**
     * If true, the method should first check the already existing samples and preserves those that are compatible
     * with the current preference information set. When false, the method should generate everything from scratch.
     */
    protected final boolean _validateAlreadyExistingSamplesFirst;

    /**
     * Captured RNG.
     */
    protected IRandom _R;

    /**
     * Parameterized constructor.
     *
     * @param name name of the model constructor
     * @param p    params container
     */
    public AbstractRejectionSampling(String name, Params<T> p)
    {
        super(name, p._compatibilityAnalyzer);
        _RM = p._RM;
        _inconsistencyThreshold = p._inconsistencyThreshold;
        _validateAlreadyExistingSamplesFirst = p._validateAlreadyExistingSamplesFirst;
        _feasibleSamplesToGenerate = p._feasibleSamplesToGenerate;
        if (_feasibleSamplesToGenerate < 0) _feasibleSamplesToGenerate = 1;
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws ConstructorException the exception can be thrown and propagated higher
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
            _RM.setNormalizations(dmContext.getNormalizationsCurrentOS());

            // provide for models
            supplyModelsWithNormalizations(dmContext.getNormalizationsCurrentOS());
        }
    }

    /**
     * Auxiliary method that performs simple validation of parameters that are relevant to {@link AbstractRejectionSampling#mainConstructModels(Report, LinkedList)}.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
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
     * This method is called by this class extensions at the beginning of the model construction process when identifying models
     * that remain compatible after receiving the most recent feedback.
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
     * The main-construct models phase (to be overwritten).
     * The concrete extension should provide the constructed models via the bundle object.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
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
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    protected boolean initializeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        return false;
    }

    /**
     * Execute a single sampling step (can be overwritten)
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return returns the constructed model
     * @throws ConstructorException the exception can be thrown and propagated higher
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
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    protected void finalizeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {

    }
}
