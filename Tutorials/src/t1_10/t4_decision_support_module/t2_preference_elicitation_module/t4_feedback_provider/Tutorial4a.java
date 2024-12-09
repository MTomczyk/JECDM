package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t4_feedback_provider;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import exeption.ReferenceSetsConstructorException;
import interaction.feedbackprovider.dm.DMResult;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSets;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;
import space.os.ObjectiveSpace;
import system.dm.DM;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link interaction.feedbackprovider.dm.IDMFeedbackProvider}
 * interface ({@link interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM}).
 *
 * @author MTomczyk
 */
@SuppressWarnings("ExtractMethodRecommender")
public class Tutorial4a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the artificial DM (L-norm based):
        // Create the internal model (Chebyshev function):
        LNorm internalModel = new LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY,
                new INormalization[]{new Linear(0.0d, 2.0d), new Linear(0.0d, 2.0d)});
        // Create the preference model (the L-norm definition wraps the internal model):
        IPreferenceModel<LNorm> lnorm = new model.definitions.LNorm(internalModel);

        // The artificial DM can be supplied with form constructors (transform reference sets -> preference information
        // of a specific form). The used constructor employs the pairwise comparison constructor that could be specified
        // using the below lines:
        //LinkedList<IFormConstructor<LNorm>> formConstructors = new LinkedList<>();
        //formConstructors.add(new PairwiseComparisons<>());

        // Create the artificial DM:
        ArtificialValueDM<LNorm> artificialValueDM = new ArtificialValueDM<>(lnorm);

        // Test the answer:

        // Create ID:
        DM dm = new DM(0, "DM");

        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 2.0d), new boolean[2]);

        // Create the context:
        DMContext context = Common.getContext(0, new Alternatives(new ArrayList<>()), startingTimestamp, criteria, os);

        try
        {
            artificialValueDM.registerDecisionMakingContext(context); // called by the system
            artificialValueDM.registerDM(dm);  // called by the system

            // Create the reference sets (manually):
            HashMap<Integer, LinkedList<ReferenceSet>> referenceSetsMap = new HashMap<>(); // raw data
            LinkedList<ReferenceSet> referenceSetsList = new LinkedList<>(); // reference sets
            referenceSetsList.add(new ReferenceSet(new Alternative("A0", new double[]{2.0d, 0.0d}),
                    new Alternative("A1", new double[]{0.0d, 2.0d})));
            referenceSetsMap.put(2, referenceSetsList); // the reference sets in the list are of size 2
            ReferenceSets referenceSets = new ReferenceSets(1, new int[]{2}, referenceSetsMap);

            // Get the feedback:
            DMResult dmResult = artificialValueDM.getFeedback(referenceSets);

            // Print the feedback:
            dmResult.printStringRepresentation();

        } catch (FeedbackProviderException | ReferenceSetsConstructorException e)
        {
            throw new RuntimeException(e);
        }


    }
}
