package emo.interactive.ktscone.dcemo;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.interactive.ktscone.AbstractKTSConeBundle;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import system.ds.DecisionSupportSystem;

/**
 * Bundle (container) of necessary fields for CDEMO algorithm.
 *
 * @author MTomczyk
 */

public class DCEMOBundle extends AbstractKTSConeBundle
{
    /**
     * Params container for the bundle's getter.
     */
    public static class Params extends AbstractKTSConeBundle.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         * @param DSS      decision support system object
         */
        protected Params(Criteria criteria, DecisionSupportSystem DSS)
        {
            super("DCEMO", criteria, DSS);
        }

        /**
         * Parameterized constructor.
         *
         * @param pA params contained of the parent class
         */
        protected Params(AbstractKTSConeBundle.Params pA)
        {
            super(pA);
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
         * @return params container
         */
        public static DCEMOBundle.Params getDefault(Criteria criteria, String DM, IRule interactionRule,
                                                    IReferenceSetConstructor referenceSetConstructor,
                                                    IDMFeedbackProvider dmFeedbackProvider)
        {
            return getDefault(criteria, DM, interactionRule, referenceSetConstructor, dmFeedbackProvider, null);
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
         * @param dssAdjuster             auxiliary DSS params adjuster (can be null, if not used); adjustment is done after the default initialization
         * @return params container
         */
        public static DCEMOBundle.Params getDefault(Criteria criteria, String DM, IRule interactionRule,
                                                    IReferenceSetConstructor referenceSetConstructor,
                                                    IDMFeedbackProvider dmFeedbackProvider,
                                                    DecisionSupportSystem.IParamsAdjuster dssAdjuster)
        {
            AbstractKTSConeBundle.Params pA = AbstractKTSConeBundle.Params.getDefault("DCEMO", criteria, DM, interactionRule,
                    referenceSetConstructor, dmFeedbackProvider, dssAdjuster);
            return new Params(pA);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param criteria considered criteria
         * @param pDSS     params container used to establish the decision support system
         * @return params container
         */
        public static DCEMOBundle.Params getDefault(Criteria criteria, DecisionSupportSystem.Params pDSS)
        {
            AbstractKTSConeBundle.Params pA = AbstractKTSConeBundle.Params.getDefault("DCEMO", criteria, pDSS);
            return new Params(pA);
        }
    }


    /**
     * Constructs the bundle of fields aiding in instantiating the CDEMO algorithm.
     * The runtime exception will be thrown if the parameterization is invalid.
     *
     * @param p params container
     */
    public DCEMOBundle(DCEMOBundle.Params p)
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
        DCEMOBundle.Params pp = (DCEMOBundle.Params) p;
        _phasesBundle._sort = new DCEMOSort(p._criteria, pp._DSS);
    }
}
