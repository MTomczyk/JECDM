package emo.interactive.nemo.nemoii;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.interactive.nemo.AbstractNEMOBundle;
import exeption.DecisionSupportSystemException;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.AbstractValueInternalModel;
import system.ds.DSSParamsProvider;
import system.ds.DecisionSupportSystem;

/**
 * Bundle (container) of necessary fields for the NEMO-II algorithm
 * (see <a href="https://doi.org/10.1109/TEVC.2014.2303783">link</a>).
 *
 * @author MTomczyk
 */

public class NEMOIIBundle extends AbstractNEMOBundle
{
    /**
     * Auxiliary interface for classes that can be used to adjust the params container being processed.
     */
    public interface IParamsAdjuster
    {
        /**
         * The main method for adjusting the params container.
         *
         * @param p params container being instantiated
         */
        void adjust(Params p);
    }

    /**
     * Params container for the bundle's getter.
     */
    public static class Params extends AbstractNEMOBundle.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         * @param DSS      decision support system object
         */
        protected Params(Criteria criteria, DecisionSupportSystem DSS)
        {
            super("NEMO-II", criteria, DSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria                considered criteria
         * @param DM                      decision maker's identifier
         * @param interactionRule         interaction rule
         * @param referenceSetConstructor reference set constructor
         * @param dmFeedbackProvider      DM-based feedback provider
         * @param preferenceModel         preference model used
         * @param modelConstructor        model constructor that is supposed to generate a plurality of compatible preference model instances (e.g. {@link FRS})
         * @param <T>                     internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params getDefault(Criteria criteria,
                                                                               String DM,
                                                                               IRule interactionRule,
                                                                               IReferenceSetConstructor referenceSetConstructor,
                                                                               IDMFeedbackProvider dmFeedbackProvider,
                                                                               IPreferenceModel<T> preferenceModel,
                                                                               IConstructor<T> modelConstructor)
        {

            return getDefault(criteria, DM, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                    preferenceModel, modelConstructor, null);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria                considered criteria
         * @param DM                      decision maker's identifier
         * @param interactionRule         interaction rule
         * @param referenceSetConstructor reference set constructor
         * @param dmFeedbackProvider      DM-based feedback provider
         * @param preferenceModel         preference model used
         * @param modelConstructor        model constructor that is supposed to generate a plurality of compatible preference model instances (e.g. {@link FRS})
         * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
         * @param <T>                     internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params getDefault(Criteria criteria,
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
            return getDefault(criteria, pDSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria considered criteria
         * @param pDSS     params container used to establish the decision support system
         * @return params container
         */
        public static Params getDefault(Criteria criteria, DecisionSupportSystem.Params pDSS)
        {
            try
            {
                DecisionSupportSystem DSS = new DecisionSupportSystem(pDSS);
                return new Params(criteria, DSS);
            } catch (DecisionSupportSystemException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Constructs the bundle of fields aiding in instantiating the NEMO-0 algorithm.
     * The runtime exception will be thrown if the parameterization is invalid.
     *
     * @param p params container
     */
    public NEMOIIBundle(Params p)
    {
        super(p);
    }


    /**
     * Auxiliary method instantiating the "sort" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateSortPhase(AbstractEABundle.Params p)
    {
        Params pp = (Params) p;
        _phasesBundle._sort = new NEMOIISort(p._criteria, pp._DSS);
    }


}
