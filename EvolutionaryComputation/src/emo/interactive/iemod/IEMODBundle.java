package emo.interactive.iemod;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.aposteriori.moead.MOEADConstructMatingPool;
import emo.aposteriori.moead.MOEADFinalizeStep;
import emo.aposteriori.moead.MOEADInitEnds;
import emo.aposteriori.moead.MOEADOSChangeListener;
import emo.interactive.AbstractEMOInteractiveBundle;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exeption.DecisionSupportSystemException;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.value.AbstractValueInternalModel;
import os.IOSChangeListener;
import space.normalization.builder.StandardLinearBuilder;
import system.ds.DSSParamsProvider;
import system.ds.DecisionSupportSystem;

/**
 * Bundle (container) of necessary fields for the IEMO/D algorithm (see <a
 * href="https://doi.org/10.1109/TEVC.2019.2915767">link</a>).
 *
 * @author MTomczyk
 */

public class IEMODBundle extends AbstractEMOInteractiveBundle
{
    /**
     * Auxiliary interface for classes that can be used to adjust the params container of the upper class being
     * instantiated.
     */
    public interface IParamsAdjuster<T extends AbstractValueInternalModel>
    {
        /**
         * The main method for adjusting the params container.
         *
         * @param p params container being instantiated
         */
        void adjust(IEMODBundle.Params<T> p);
    }

    /**
     * Params container for the bundle's getter.
     */
    public static class Params<T extends AbstractValueInternalModel> extends AbstractEMOInteractiveBundle.Params
    {
        /**
         * Goals manager steering the evolutionary process.
         */
        public final MOEADGoalsManager _goalsManager;

        /**
         * Goals updater.
         */
        protected final IEMODGoalsUpdater<T> _goalsUpdater;

        /**
         * Parameterized constructor.
         *
         * @param criteria     considered criteria
         * @param goalsManager goals manager
         * @param goalsUpdater object responsible for updating the optimization goals
         * @param DSS          decision support system object
         */
        protected Params(Criteria criteria,
                         MOEADGoalsManager goalsManager,
                         IEMODGoalsUpdater<T> goalsUpdater,
                         DecisionSupportSystem DSS)
        {
            super("IEMO/D", criteria, DSS);
            _goalsManager = goalsManager;
            _goalsUpdater = goalsUpdater;
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one
         * DM-based feedback provider, and one interaction rule.
         *
         * @param criteria                considered criteria
         * @param goalsManager            goals manager
         * @param DM                      decision maker's identifier
         * @param interactionRule         interaction rule
         * @param referenceSetConstructor reference set constructor
         * @param dmFeedbackProvider      DM-based feedback provider
         * @param preferenceModel         preference model used
         * @param modelConstructor        model instance constructor
         * @param <T>                     internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params<T> getDefault(Criteria criteria,
                                                                                  MOEADGoalsManager goalsManager,
                                                                                  String DM,
                                                                                  IRule interactionRule,
                                                                                  IReferenceSetConstructor referenceSetConstructor,
                                                                                  IDMFeedbackProvider dmFeedbackProvider,
                                                                                  IPreferenceModel<T> preferenceModel,
                                                                                  IConstructor<T> modelConstructor)
        {
            return getDefault(criteria, goalsManager, DM, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                    preferenceModel, modelConstructor, null);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one
         * DM-based feedback provider, and one interaction rule.
         *
         * @param criteria                considered criteria
         * @param goalsManager            goals manager
         * @param DM                      decision maker's identifier
         * @param interactionRule         interaction rule
         * @param referenceSetConstructor reference set constructor
         * @param dmFeedbackProvider      DM-based feedback provider
         * @param preferenceModel         preference model used
         * @param modelConstructor        model instance constructor
         * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done
         *                                after the default initialization
         * @param <T>                     internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params<T> getDefault(Criteria criteria,
                                                                                  MOEADGoalsManager goalsManager,
                                                                                  String DM,
                                                                                  IRule interactionRule,
                                                                                  IReferenceSetConstructor referenceSetConstructor,
                                                                                  IDMFeedbackProvider dmFeedbackProvider,
                                                                                  IPreferenceModel<T> preferenceModel,
                                                                                  IConstructor<T> modelConstructor,
                                                                                  DecisionSupportSystem.IParamsAdjuster dssAdjuster)
        {
            DecisionSupportSystem.Params pDSS = DSSParamsProvider.getForSingleDecisionMakerSingleModelArtificialProvider(criteria,
                    DM, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, modelConstructor);
            if (dssAdjuster != null) dssAdjuster.adjust(pDSS);
            return getDefault(criteria, goalsManager, new IEMODGoalsUpdater<>(preferenceModel, goalsManager), pDSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one
         * DM-based feedback provider, and one interaction rule.
         *
         * @param criteria     considered criteria
         * @param goalsManager goals manager
         * @param goalsUpdater object responsible for updating the optimization goals
         * @param pDSS         params container used to establish the decision support system
         * @param <T>          internal value model
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params<T> getDefault(Criteria criteria,
                                                                                  MOEADGoalsManager goalsManager,
                                                                                  IEMODGoalsUpdater<T> goalsUpdater,
                                                                                  DecisionSupportSystem.Params pDSS)
        {
            try
            {
                DecisionSupportSystem DSS = new DecisionSupportSystem(pDSS);
                return new Params<>(criteria, goalsManager, goalsUpdater, DSS);
            } catch (DecisionSupportSystemException e)
            {
                throw new RuntimeException(e);
            }
        }


    }


    /**
     * Constructs the bundle of fields aiding in instantiating the IEMO/D algorithm. The runtime exception will be
     * thrown if the parameterization is invalid.
     *
     * @param p params container
     */
    public IEMODBundle(IEMODBundle.Params<? extends AbstractValueInternalModel> p)
    {
        super(p);
    }

    /**
     * Auxiliary method instantiating the "finalize step" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateFinalizeStepPhase(AbstractEABundle.Params p)
    {
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        _phasesBundle._finalizeStep = new MOEADFinalizeStep(pp._goalsManager);
    }

    /**
     * Auxiliary method instantiating the "construct mating pool" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateConstructMatingPoolPhase(AbstractEABundle.Params p)
    {
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        _phasesBundle._constructMatingPool = new MOEADConstructMatingPool(pp._goalsManager);
    }


    /**
     * Auxiliary method instantiating the "init ends" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateInitEndsPhase(AbstractEABundle.Params p)
    {
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        _phasesBundle._initEnds = new MOEADInitEnds(pp._goalsManager);
    }

    /**
     * Auxiliary method instantiating the "prepare step" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiatePrepareStepPhase(AbstractEABundle.Params p)
    {
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        _phasesBundle._prepareStep = new IEMODInteractAndPrepareStep<>(pp._goalsManager, pp._DSS, pp._goalsUpdater, pp._dmContextParamsConstructor);
    }


    /**
     * Auxiliary method instantiating the "update objective space" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateUpdateObjectiveSpacePhase(AbstractEABundle.Params p)
    {
        super.instantiateUpdateObjectiveSpacePhase(p);
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        if (p._initialNormalizations != null) pp._goalsManager.updateNormalizations(p._initialNormalizations);
    }

    /**
     * Auxiliary method for retrieving OS changed listeners (facilitates customization by the class extensions).
     *
     * @param p params container
     * @return OS changed listeners.
     */
    @Override
    protected IOSChangeListener[] getOSChangedListeners(AbstractEABundle.Params p)
    {
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        IOSChangeListener l = new MOEADOSChangeListener(pp._goalsManager, new StandardLinearBuilder());
        return new IOSChangeListener[]{l, pp._dmContextParamsConstructor, (IOSChangeListener) _phasesBundle._prepareStep};
    }


    /**
     * Provides initial normalization data to selected objects.
     *
     * @param p params container
     */
    @Override
    protected void registerInitialNormalizations(AbstractEABundle.Params p)
    {
        IEMODBundle.Params<? extends AbstractValueInternalModel> pp = (IEMODBundle.Params<? extends AbstractValueInternalModel>) p;
        if (p._initialNormalizations != null) pp._goalsManager.updateNormalizations(p._initialNormalizations);
    }

    /**
     * Do not use the "merge" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateMergePhase(AbstractEABundle.Params p)
    {
        _phasesBundle._merge = null;
    }


}
