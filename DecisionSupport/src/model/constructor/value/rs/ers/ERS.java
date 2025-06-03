package model.constructor.value.rs.ers;

import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.constructor.value.rs.ers.evolutionary.EvolutionaryModelConstructor;
import model.similarity.ISimilarity;
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
 * This class represents an evolutionary-based rejection sampling procedure for generating uniformly distributed
 * preference model instances compatible with the decision maker's feedback. Its description can be found in
 * <a href="https://papers.ssrn.com/sol3/papers.cfm?abstract_id=5279824">this paper</a> (preprint; TODO to be updated
 * when published). In short, its central element is an efficient fixed-size queue that keeps the sampled models sorted.
 * The default sorting criterion (comparator) is implemented as {@link MostSimilarWithTieResolving} (it is recommended
 * to use it). First, the comparator assumes that compatible models are favored in the queue over incompatible ones.
 * Among the latter, the procedure favors those with lesser incompatibility scores, calculated in the spirit of the most
 * discriminative function approach (see {@link compatibility.CompatibilityAnalyzer#calculateTheMostDiscriminativeCompatibilityWithValueModel(LinkedList,
 * AbstractValueInternalModel)}). Regarding the compatible models, the queue prioritizes those that maximize their
 * distance to the closest neighbor (another model). In the case of equality, similarities to the next nearest neighbors
 * are compared, etc. Note that the similarity measure can be customized (see {@link ISimilarity}).
 *
 * @author MTomczyk
 */
public class ERS<T extends AbstractValueInternalModel> extends AbstractRejectionSampling<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel> extends AbstractRejectionSampling.Params<T>
    {
        /**
         * Represents the number of k most-similar neighbors kept per each model.
         */
        public int _kMostSimilarNeighbors = 5;

        /**
         * Comparator used to sort the models (see the default {@link MostSimilarWithTieResolving}).
         */
        public AbstractComparator<T> _comparator = new MostSimilarWithTieResolving<>();

        /**
         * Similarity measure used when comparing two models.
         */
        public ISimilarity<T> _similarity = null;

        /**
         * Optional evolutionary model constructor. If not null (used), this object will be employed when
         * constructing new models during the ``improve attempt'' phase.
         */
        public EvolutionaryModelConstructor<T> _EMC = null;

        /**
         * Parameterized constructor.
         *
         * @param initialModelsConstructor random model generator
         */
        public Params(IRandomModel<T> initialModelsConstructor)
        {
            super(initialModelsConstructor);
        }
    }

    /**
     * Reference to the report viewed as {@link model.constructor.value.rs.ers.Report}.
     */
    private model.constructor.value.rs.ers.Report<T> _ersReport;

    /**
     * Models queue object (primary object responsible for controlling the distribution of sampled models).
     */
    private final ModelsQueue<T> _modelsQueue;

    /**
     * Optional evolutionary model constructor. If not null (used), this object will be employed when
     * constructing new models during the ``improve attempt'' phase.
     */
    private final EvolutionaryModelConstructor<T> _EMC;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ERS(Params<T> p)
    {
        super("ERS", p);
        _modelsQueue = new ModelsQueue<>(_feasibleSamplesToGenerate, p._kMostSimilarNeighbors,
                p._compatibilityAnalyzer, p._comparator, p._similarity);
        _EMC = p._EMC;
    }

    /**
     * Auxiliary method for creating report instance.
     *
     * @return report instance
     */
    @Override
    protected Report<T> getReportInstance()
    {
        _ersReport = new model.constructor.value.rs.ers.Report<>(_dmContext);
        return _ersReport;
    }

    /**
     * Getter for the models queue.
     *
     * @return models queue
     */
    public ModelsQueue<T> getModelsQueue()
    {
        return _modelsQueue;
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
        if (initializeStep(bundle, preferenceInformation)) return;
        int attempts = Math.max(0, _iterationsLimit.getIterations(_dmContext, preferenceInformation,
                bundle, _feasibleSamplesToGenerate));
        for (int t = 0; t < attempts; t++) executeStep(bundle, preferenceInformation);
        finalizeStep(bundle, preferenceInformation);
    }

    /**
     * Execute the initialize step.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return indicates whether to prematurely terminate (true)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    protected boolean initializeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        validate(bundle, preferenceInformation);
        _R = _dmContext.getR();

        bundle._inconsistencyDetected = false;
        bundle._normalizationsWereUpdated = _normalizationsWereUpdated;
        bundle._compatibleModelsToSample = _feasibleSamplesToGenerate;

        bundle._modelsPreservedBetweenIterations = 0;
        bundle._modelsRejectedBetweenIterations = 0;
        bundle._successRateInPreserving = 0.0d;

        bundle._acceptedNewlyConstructedModels = 0;
        bundle._rejectedNewlyConstructedModels = 0;
        bundle._successRateInConstructing = 0.0d;

        if ((_modelsQueue.getQueue().isEmpty()) || (!_validateAlreadyExistingSamplesFirst)
                || (_models.size() < _modelsQueue.getQueue().size())) // first fill
        {
            ArrayList<T> rms;
            if (!attemptToSupplyInitialModels())
            {
                rms = new ArrayList<>(_feasibleSamplesToGenerate);
                for (int i = 0; i < _feasibleSamplesToGenerate; i++) rms.add(_RM.generateModel(_R));
            }
            else
            {
                rms = _models;
            }

            _modelsQueue.reset();
            _modelsQueue.initializeWithBatch(rms, preferenceInformation);
            _modelsQueue.updateClosestModelsAndSortQueue(false, false);

            bundle._acceptedNewlyConstructedModels = _modelsQueue.getNoCompatibleModels();
            bundle._rejectedNewlyConstructedModels = _modelsQueue.getQueue().size() - bundle._acceptedNewlyConstructedModels;
        }
        else if (_models.size() != _modelsQueue.getQueue().size()) // error case
        {
            throw new ConstructorException("The number of stored models is greater than the number of models maintained in the queue", this.getClass());
        }
        else
        {
            boolean rr = _modelsQueue.reevaluateCompatibilities(preferenceInformation);
            bundle._modelsPreservedBetweenIterations = _modelsQueue.getNoCompatibleModels();
            bundle._modelsRejectedBetweenIterations = 0;
            bundle._successRateInPreserving = (double) bundle._modelsPreservedBetweenIterations / _modelsQueue.getQueue().size();
            if (!rr) _modelsQueue.updateClosestModelsAndSortQueue(true, true);
        }
        return false;
    }


    /**
     * Execute a single sampling step.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return returns the constructed model
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    protected T executeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        T model;
        if (_EMC == null) model = _RM.generateModel(_R); // use random model generator
        else model = _EMC.getModel(_dmContext, this); // use evolutionary generator

        boolean compatible = _modelsQueue.insertModel(model, preferenceInformation);
        if (compatible) bundle._acceptedNewlyConstructedModels++;
        else bundle._rejectedNewlyConstructedModels++;
        return model;
    }

    /**
     * Execute the finalize step.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    protected void finalizeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        bundle._successRateInConstructing = (double) (bundle._acceptedNewlyConstructedModels) /
                (bundle._acceptedNewlyConstructedModels + bundle._rejectedNewlyConstructedModels);

        _models = _modelsQueue.getCompatibleModels();
        bundle._models = _models;

        if (_models.size() <= _inconsistencyThreshold) bundle._inconsistencyDetected = true;
    }

}
