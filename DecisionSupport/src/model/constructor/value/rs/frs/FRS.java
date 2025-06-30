package model.constructor.value.rs.frs;

import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.value.rs.AbstractRejectionSampling;
import model.internals.value.AbstractValueInternalModel;

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
public class FRS<T extends AbstractValueInternalModel> extends AbstractRejectionSampling<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel> extends AbstractRejectionSampling.Params<T>
    {
        /**
         * Limit for the number of samples the FRS method is allowed to generate. Should be positive and greater (or equal)
         * than {@link Params#_feasibleSamplesToGenerate}. In the case of value violation, it is set to 1 or
         * {@link Params#_feasibleSamplesToGenerate} (whatever is greater). IMPORTANT NOTE: this field has no effect if
         * {@link Params#_iterationsLimit} is provided via params container. The implementation of this interface is
         * used, if not null, to dynamically adjust the number of iterations a sampler is allowed to run.
         */
        public int _samplingLimit = 1000000;

        /**
         * Parameterized constructor.
         *
         * @param RM random model generator
         */
        public Params(IRandomModel<T> RM)
        {
            super(RM);
        }
    }

    /**
     * Limit for the number of samples the FRS method is allowed to generate. Should be positive and greater (or equal)
     * than {@link Params#_feasibleSamplesToGenerate}. In the case of value violation, it is set to 1 or
     * {@link Params#_feasibleSamplesToGenerate} (whatever is greater).
     */
    protected int _samplingLimit;

    /**
     * Auxiliary counter.
     */
    protected int _toGenerate;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public FRS(Params<T> p)
    {
        super("FRS", p);
        _samplingLimit = p._samplingLimit;
        if (_samplingLimit < 0) _samplingLimit = 1;
        if (_samplingLimit < _feasibleSamplesToGenerate) _samplingLimit = _feasibleSamplesToGenerate;
        attemptToSupplyInitialModels();
    }

    /**
     * The main-construct models phase (to be overwritten).
     * The concrete extension should provide the constructed models via the bundle object.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    protected void mainConstructModels(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        if (initializeStep(bundle, preferenceInformation)) return;
        int attempts;
        if (_iterationsLimit != null)
            attempts = Math.max(0, _iterationsLimit.getIterations(_dmContext, preferenceInformation,
                    bundle, _feasibleSamplesToGenerate));
        else attempts = Math.max(0, _samplingLimit);


        for (int t = 0; t < attempts; t++)
        {
            executeStep(bundle, preferenceInformation);
            if (_toGenerate == 0) break;
        }

        finalizeStep(bundle, preferenceInformation);
    }

    /**
     * Execute the finalize step.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return indicates whether to prematurely terminate (true)
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    protected boolean initializeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        long startTime = System.nanoTime();

        super.initializeStep(bundle, preferenceInformation);
        _R = _dmContext.getR();

        bundle._inconsistencyDetected = false;
        bundle._normalizationsWereUpdated = _normalizationsWereUpdated;
        bundle._compatibleModelsToSample = _feasibleSamplesToGenerate;

        // preservation phase
        _toGenerate = _feasibleSamplesToGenerate;

        ArrayList<T> preservedModels = executePreservationAttempt(bundle, preferenceInformation);
        _toGenerate -= preservedModels.size();

        // set to new models
        _models = preservedModels;
        bundle._models = _models;
        bundle._acceptedNewlyConstructedModels = 0;
        bundle._rejectedNewlyConstructedModels = 0;
        bundle._successRateInConstructing = 0.0d;

        if (_toGenerate == 0)
        {
            if (_models.size() <= _inconsistencyThreshold) bundle._inconsistencyDetected = true;
            _passedTime += (System.nanoTime() - startTime);
            return true;
        }

        _passedTime += (System.nanoTime() - startTime);
        return false;
    }

    /**
     * Execute a single sampling step.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return returns the constructed model
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    protected T executeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        long startTime = System.nanoTime();

        T M = _RM.generateModel(_R);
        Double a = _compatibilityAnalyzer.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, M);
        if ((a == null) || (Double.compare(a, 0.0d) > 0))
        {
            if (_models.size() < _feasibleSamplesToGenerate) _models.add(M);
            _toGenerate--;
            bundle._acceptedNewlyConstructedModels++;
        }
        else bundle._rejectedNewlyConstructedModels++;

        _passedTime += (System.nanoTime() - startTime);

        if ((_toGenerate == 0) && (_compatibleFoundInIterations == null))
        {
            _compatibleFoundInIterations = (bundle._acceptedNewlyConstructedModels + bundle._rejectedNewlyConstructedModels);
            _compatibleFoundInTime = _passedTime / 1000000.0d;
        }

        return M;
    }


    /**
     * Execute the finalize step.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    protected void finalizeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        long startTime = System.nanoTime();
        bundle._successRateInConstructing = (double) bundle._acceptedNewlyConstructedModels /
                (bundle._acceptedNewlyConstructedModels + bundle._rejectedNewlyConstructedModels);
        if (_models.size() <= _inconsistencyThreshold) bundle._inconsistencyDetected = true;
        _toGenerate = 0;
        _passedTime += (System.nanoTime() - startTime);
    }

}
