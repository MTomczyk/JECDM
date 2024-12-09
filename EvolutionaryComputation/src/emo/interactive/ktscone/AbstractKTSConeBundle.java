package emo.interactive.ktscone;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.aposteriori.nsgaii.NSGAIIOSChangeListener;
import emo.aposteriori.nsgaii.NSGAIISort;
import emo.interactive.AbstractEMOInteractiveBundle;
import exeption.DecisionSupportSystemException;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.value.scalarizing.KTSCone;
import os.IOSChangeListener;
import space.normalization.builder.StandardLinearBuilder;
import system.ds.DSSParamsProvider;
import system.ds.DecisionSupportSystem;

/**
 * Abstract bundle (container) of necessary fields for CDEMO and DCEMO algorithms.
 *
 * @author MTomczyk
 */

public abstract class AbstractKTSConeBundle extends AbstractEMOInteractiveBundle
{
    /**
     * Params container for the bundle's getter.
     */
    public static class Params extends AbstractEMOInteractiveBundle.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name     name of the EA
         * @param criteria considered criteria
         * @param DSS      decision support system object
         */
        protected Params(String name, Criteria criteria, DecisionSupportSystem DSS)
        {
            super(name, criteria, DSS);
        }


        /**
         * Parameterized constructor.
         *
         * @param pA already instantiated params container
         */
        protected Params(AbstractKTSConeBundle.Params pA)
        {
            super(pA._name, pA._criteria, pA._DSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param name                    name of the EA
         * @param criteria                considered criteria
         * @param DM                      decision maker's identifier
         * @param interactionRule         interaction rule
         * @param referenceSetConstructor reference set constructor
         * @param dmFeedbackProvider      DM-based feedback provider
         * @return params container
         */
        protected static AbstractKTSConeBundle.Params getDefault(String name, Criteria criteria, String DM,
                                                                 IRule interactionRule,
                                                                 IReferenceSetConstructor referenceSetConstructor,
                                                                 IDMFeedbackProvider dmFeedbackProvider)
        {
            IPreferenceModel<KTSCone> preferenceModel = new model.definitions.KTSCone();
            IConstructor<KTSCone> constructor = new model.constructor.value.KTSCone();

            DecisionSupportSystem.Params pDSS = DSSParamsProvider.getForSingleDecisionMakerSingleModelArtificialProvider(criteria,
                    DM, interactionRule, referenceSetConstructor, dmFeedbackProvider, preferenceModel, constructor);
            return getDefault(name, criteria, pDSS);
        }

        /**
         * Constructs a default params container that involves one decision maker with one preference model, one DM-based
         * feedback provider, and one interaction rule.
         *
         * @param name     name of the EA
         * @param criteria considered criteria
         * @param pDSS     params container used to establish the decision support system
         * @return params container
         */
        protected static AbstractKTSConeBundle.Params getDefault(String name, Criteria criteria, DecisionSupportSystem.Params pDSS)
        {
            try
            {
                DecisionSupportSystem DSS = new DecisionSupportSystem(pDSS);
                return new AbstractKTSConeBundle.Params(name, criteria, DSS);
            } catch (DecisionSupportSystemException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Constructs the bundle of fields aiding in instantiating the CDEMO or DCEMO algorithm.
     * The runtime exception will be thrown if the parameterization is invalid.
     *
     * @param p params container
     */
    public AbstractKTSConeBundle(AbstractKTSConeBundle.Params p)
    {
        super(p);
    }

    /**
     * Auxiliary method instantiating the "prepare step" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiatePrepareStepPhase(AbstractEABundle.Params p)
    {
        AbstractKTSConeBundle.Params pp = (Params) p;
        _phasesBundle._prepareStep = new KTSConeInteractionAndPrepareStep(pp._DSS, pp._dmContextParamsConstructor);
    }

    /**
     * Auxiliary method for retrieving OS changed listeners (facilitates customization by the class extensions).
     * @param p params container
     * @return OS changed listeners.
     */
    @Override
    protected IOSChangeListener[] getOSChangedListeners(AbstractEABundle.Params p)
    {
        KTSConeInteractionAndPrepareStep l1 = (KTSConeInteractionAndPrepareStep) _phasesBundle._prepareStep;
        NSGAIIOSChangeListener l2 = new NSGAIIOSChangeListener((NSGAIISort) _phasesBundle._sort, new StandardLinearBuilder());
        return new IOSChangeListener[]{l1, l2};
    }

    /**
     * Provides initial normalization data to selected objects.
     *
     * @param p params container
     */
    @Override
    protected void registerInitialNormalizations(AbstractEABundle.Params p)
    {
        if (p._initialNormalizations != null) ((NSGAIISort) _phasesBundle._sort).updateNormalizations(p._initialNormalizations);
    }
}
