package emo.interactive.nemo.nemo0;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.interactive.nemo.AbstractNEMOBundle;
import exeption.DecisionSupportSystemException;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.rs.representative.RepresentativeModel;
import model.internals.value.AbstractValueInternalModel;
import system.ds.DSSParamsProvider;
import system.ds.DecisionSupportSystem;

/**
 * Bundle (container) of necessary fields for the NEMO-0 algorithm
 * (see <a href="https://doi.org/10.1109/TEVC.2014.2303783">link</a>).
 *
 * @author MTomczyk
 */

public class NEMO0Bundle extends AbstractNEMOBundle
{
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
            super("NEMO-0", criteria, DSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria                       considered criteria
         * @param DM                             decision maker's identifier
         * @param interactionRule                interaction rule
         * @param referenceSetConstructor        reference set constructor
         * @param dmFeedbackProvider             DM-based feedback provider
         * @param preferenceModel                preference model used
         * @param representativeModelConstructor representative model instance constructor (founded on {@link FRS})
         * @param <T>                            internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params getDefault(Criteria criteria,
                                                                               String DM,
                                                                               IRule interactionRule,
                                                                               IReferenceSetConstructor referenceSetConstructor,
                                                                               IDMFeedbackProvider dmFeedbackProvider,
                                                                               IPreferenceModel<T> preferenceModel,
                                                                               RepresentativeModel<T> representativeModelConstructor)
        {

            return getDefault(criteria, DM, interactionRule, referenceSetConstructor, dmFeedbackProvider,
                    preferenceModel, representativeModelConstructor, null);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria                       considered criteria
         * @param DM                             decision maker's identifier
         * @param interactionRule                interaction rule
         * @param referenceSetConstructor        reference set constructor
         * @param dmFeedbackProvider             DM-based feedback provider
         * @param preferenceModel                preference model used
         * @param representativeModelConstructor representative model instance constructor (founded on {@link FRS})
         * @param dssAdjuster                    auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
         * @param <T>                            internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> Params getDefault(Criteria criteria,
                                                                               String DM,
                                                                               IRule interactionRule,
                                                                               IReferenceSetConstructor referenceSetConstructor,
                                                                               IDMFeedbackProvider dmFeedbackProvider,
                                                                               IPreferenceModel<T> preferenceModel,
                                                                               RepresentativeModel<T> representativeModelConstructor,
                                                                               DecisionSupportSystem.IParamsAdjuster dssAdjuster)
        {

            DecisionSupportSystem.Params pDSS = DSSParamsProvider.getForSingleDecisionMakerSingleModelArtificialProvider(criteria,
                    DM, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel,
                    representativeModelConstructor);
            if (dssAdjuster != null) dssAdjuster.adjust(pDSS);
            return getDefault(criteria, pDSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria considered criteria
         * @param pDSS     params container used to establish the decision support system
         * @param <T>      internal preference model definition
         * @return params container
         */
        public static <T extends AbstractValueInternalModel> NEMO0Bundle.Params getDefault(Criteria criteria, DecisionSupportSystem.Params pDSS)
        {
            try
            {
                DecisionSupportSystem DSS = new DecisionSupportSystem(pDSS);
                return new NEMO0Bundle.Params(criteria, DSS);
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
    public NEMO0Bundle(NEMO0Bundle.Params p)
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
        NEMO0Bundle.Params pp = (NEMO0Bundle.Params) p;
        _phasesBundle._sort = new NEMO0Sort(p._criteria, pp._DSS);
    }


}
