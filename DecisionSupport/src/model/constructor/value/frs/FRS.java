package model.constructor.value.frs;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.AbstractConstructor;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of a Fast Rejection Sampling method (see <a href="https://doi.org/10.1016/j.ins.2020.11.030">link</a>).
 * It is a method based on the Monte Carlo simulation technique. In a nutshell, it iteratively samples random model
 * instances (using {@link IRandomModel} for this purpose) and rejects those that are not compatible with the decision
 * maker's preferences provided. The method can be parameterized given, e.g., the sampling limit or the number of feasible
 * samples required to be generated.
 *
 * @author MTomczyk
 */
public class FRS<T extends AbstractValueInternalModel> extends AbstractConstructor<T> implements IConstructor<T>
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
         * Limit for the number of samples the FRS method is allowed to generate. Should be positive and greater (or equal)
         * than {@link Params#_feasibleSamplesToGenerate}. In the case of value violation, it is set to 1 or
         * {@link Params#_feasibleSamplesToGenerate} (whatever is greater).
         */
        public int _samplingLimit = 1000000;

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
         * If true, the FRS method first checks the already existing samples and preserves those that are compatible
         * with the current preference information set. When false, FRS generates everything from scratch.
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
    private final IRandomModel<T> _RM;

    /**
     * Limit for the number of samples the FRS method is allowed to generate. Should be positive and greater (or equal)
     * than {@link Params#_feasibleSamplesToGenerate}. In the case of value violation, it is set to 1 or
     * {@link Params#_feasibleSamplesToGenerate} (whatever is greater).
     */
    private int _samplingLimit;

    /**
     * The number of feasible samples the method is supposed to generate. This number should be positive.
     * If it is not, it is set to 1.
     */
    private int _feasibleSamplesToGenerate;

    /**
     * The threshold for the number of generated compatible samples. The overall processing result is considered
     * inconsistent if the number is smaller or equal to this threshold.
     */
    private final int _inconsistencyThreshold;

    /**
     * If true, the FRS method first checks the already existing samples and preserves those that are compatible
     * with the current preference information set. When false, FRS generates everything from scratch.
     */
    private final boolean _validateAlreadyExistingSamplesFirst;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public FRS(Params<T> p)
    {
        super("FRS", p._compatibilityAnalyzer);
        _RM = p._RM;
        _samplingLimit = p._samplingLimit;
        _feasibleSamplesToGenerate = p._feasibleSamplesToGenerate;
        _inconsistencyThreshold = p._inconsistencyThreshold;
        _validateAlreadyExistingSamplesFirst = p._validateAlreadyExistingSamplesFirst;

        if (_feasibleSamplesToGenerate < 0) _feasibleSamplesToGenerate = 1;
        if (_samplingLimit < 0) _samplingLimit = 1;
        if (_samplingLimit < _feasibleSamplesToGenerate) _samplingLimit = _feasibleSamplesToGenerate;
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
        if (bundle == null)
            throw new ConstructorException("The bundle result object is not provided (the reference is null)", this.getClass());
        if (preferenceInformation == null)
            throw new ConstructorException("The preference examples are not provided (the list is null)", this.getClass());

        IRandom R = _dmContext.getR();
        if (R == null) throw new ConstructorException("The random number generator is not supplied by the decision-making context", this.getClass());

        bundle._inconsistencyDetected = false;
        bundle._normalizationsWereUpdated = _normalizationsWereUpdated;

        // preservation attempt
        bundle._modelsPreservedBetweenIterations = 0;
        bundle._modelsRejectedBetweenIterations = 0;
        bundle._successRateInPreserving = 0.0d;

        ArrayList<T> newModels = new ArrayList<>(_feasibleSamplesToGenerate);

        int toGenerate = _feasibleSamplesToGenerate;

        if ((_validateAlreadyExistingSamplesFirst) && (_models != null))
        {
            ArrayList<T> preservedModels;

            // case only when there was a successful preference elicitation and there is no inconsistency handling
            // in that case, check just the newest PI
            if ((_preferenceElicitationAttempted) && (!_preferenceElicitationFailed)
                    && (_mostRecentPreferenceInformation != null) && (!_inconsistencyHandlingMode))
            {
                LinkedList<PreferenceInformationWrapper> mr = new LinkedList<>(_mostRecentPreferenceInformation);
                preservedModels = retrieveFeasibleModels(mr);
            }
            else preservedModels = retrieveFeasibleModels(preferenceInformation);

            newModels.addAll(preservedModels);
            bundle._modelsPreservedBetweenIterations = newModels.size();
            bundle._modelsRejectedBetweenIterations = _models.size() - newModels.size();
            if (!_models.isEmpty()) bundle._successRateInPreserving = (double) newModels.size() / _models.size();
            toGenerate -= newModels.size();
        }

        // set to new models
        _models = newModels;
        bundle._models = _models;
        bundle._acceptedNewlyConstructedModels = 0;
        bundle._rejectedNewlyConstructedModels = 0;
        bundle._successRateInConstructing = 0.0d;

        if (toGenerate == 0)
        {
            if (_models.size() <= _inconsistencyThreshold) bundle._inconsistencyDetected = true;
            return;
        }

        for (int t = 0; t < _samplingLimit; t++)
        {
            T M = _RM.generateModel(R);
            Double a = _compatibilityAnalyzer.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, M);
            if ((a == null) || (Double.compare(a, 0.0d) > 0))
            {
                newModels.add(M);
                toGenerate--;
                bundle._acceptedNewlyConstructedModels++;
                if (toGenerate == 0) break;
            }
            else
            {
                bundle._rejectedNewlyConstructedModels++;
            }
        }

        bundle._successRateInConstructing = (double) bundle._acceptedNewlyConstructedModels /
                (bundle._acceptedNewlyConstructedModels + bundle._rejectedNewlyConstructedModels);
        if (_models.size() <= _inconsistencyThreshold) bundle._inconsistencyDetected = true;
    }

    /**
     * Auxiliary method that attempts to retrieve those models that remain feasible.
     *
     * @param preferenceInformation preference examples (provided via wrappers)
     * @return models that remain feasible
     */
    private ArrayList<T> retrieveFeasibleModels(LinkedList<PreferenceInformationWrapper> preferenceInformation)
    {
        ArrayList<T> R = new ArrayList<>();
        if ((_models == null) || (_models.isEmpty())) return R;
        for (T M : _models)
        {
            Double a = _compatibilityAnalyzer.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, M);
            if ((a == null) || (Double.compare(a, 0.0d) > 0)) R.add(M);
        }
        return R;
    }

}
