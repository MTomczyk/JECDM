package system.ds;

import criterion.Criteria;
import inconsistency.RemoveOldest;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * Auxiliary class providing supportive means for constructing DSS parameterization object {@link DecisionSupportSystem.Params}.
 *
 * @author MTomczyk
 */
public abstract class DSSParamsProvider
{
    /**
     * Constructors a DSS params container that involves one decision maker with one preference model,
     * one DM-based feedback provider, and one interaction rule (inconsistency handler = remove oldest; refiner = default).
     *
     * @param criteria                consistent family of criteria
     * @param DM                      decision maker's identifier
     * @param interactionRule         interaction rule
     * @param referenceSetConstructor reference set constructor
     * @param dmFeedbackProvider      DM-based feedback provider
     * @param preferenceModel         preference model used
     * @param modelConstructor        model instance constructor
     * @param <T>                     internal preference model definition
     * @return parameterized {@link DecisionSupportSystem.Params} object
     */
    public static <T extends AbstractInternalModel> DecisionSupportSystem.Params
    getForSingleDecisionMakerSingleModelArtificialProvider(
            Criteria criteria,
            String DM,
            IRule interactionRule,
            IReferenceSetConstructor referenceSetConstructor,
            IDMFeedbackProvider dmFeedbackProvider,
            IPreferenceModel<T> preferenceModel,
            IConstructor<T> modelConstructor
    )
    {
        DecisionSupportSystem.Params p = new DecisionSupportSystem.Params();
        p._criteria = criteria;
        p._interactionTrigger = new InteractionTrigger(interactionRule);
        p._refiner = Refiner.getDefault();
        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(referenceSetConstructor);
        p._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        p._feedbackProvider = FeedbackProvider.getForSingleDM(DM, dmFeedbackProvider);
        DMBundle dmBundle = new DMBundle(DM);
        dmBundle._modelBundles = new ModelBundle<?>[1];
        ModelBundle<T> modelBundle = new ModelBundle<>();
        modelBundle._preferenceModel = preferenceModel;
        modelBundle._modelConstructor = modelConstructor;
        modelBundle._inconsistencyHandler = new RemoveOldest<>();
        dmBundle._modelBundles[0] = modelBundle;
        p._dmBundles = new DMBundle[]{dmBundle};
        return p;
    }
}
