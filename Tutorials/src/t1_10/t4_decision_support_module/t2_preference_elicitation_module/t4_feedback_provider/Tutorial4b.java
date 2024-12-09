package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t4_feedback_provider;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import exeption.ReferenceSetsConstructorException;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSets;
import interaction.reference.ReferenceSetsContainer;
import interaction.reference.Result;
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
 * This tutorial focuses on the {@link interaction.feedbackprovider.FeedbackProvider} class.
 *
 * @author MTomczyk
 */
public class Tutorial4b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the DMs' string identifiers.
        String[] DMs = new String[]{"DM1", "DM2"};
        DM[] DM_IDs = new DM[]{new DM(0, DMs[0]), new DM(1, DMs[1])};

        // Create the feedback provides (two artificial value-model-based DMs):
        IDMFeedbackProvider[] dmFeedbackProviders = new IDMFeedbackProvider[2];
        double[][] weights = new double[][]{{0.7d, 0.3d}, {0.3d, 0.7d}};
        for (int i = 0; i < 2; i++)
        {
            LNorm internalModel = new LNorm(weights[i], Double.POSITIVE_INFINITY, new INormalization[]{new Linear(0.0d, 2.0d), new Linear(0.0d, 2.0d)});
            IPreferenceModel<LNorm> lnorm = new model.definitions.LNorm(internalModel);
            dmFeedbackProviders[i] = new ArtificialValueDM<>(lnorm);
        }

        // Create the main object:
        FeedbackProvider feedbackProvider = FeedbackProvider.getForTwoDMs(DMs[0], dmFeedbackProviders[0], DMs[1], dmFeedbackProviders[1]);

        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 2.0d), new boolean[2]);

        // Create the context:
        DMContext context = Common.getContext(0, new Alternatives(new ArrayList<>()), startingTimestamp, criteria, os);

        // Create the reference sets manually:
        Result referenceSetsResult = new Result(context, null);
        HashMap<DM, ReferenceSets> dmReferenceSets = new HashMap<>();
        try
        {
            {
                HashMap<Integer, LinkedList<ReferenceSet>> referenceSetsMap = new HashMap<>(); // raw data
                LinkedList<ReferenceSet> referenceSetsList = new LinkedList<>(); // reference sets
                referenceSetsList.add(new ReferenceSet(new Alternative("A0", new double[]{2.0d, 0.0d}),
                        new Alternative("A1", new double[]{0.0d, 2.0d})));
                referenceSetsMap.put(2, referenceSetsList); // the reference sets in the list are of size 2
                dmReferenceSets.put(DM_IDs[0], new ReferenceSets(1, new int[]{2}, referenceSetsMap));
            }
            {
                HashMap<Integer, LinkedList<ReferenceSet>> referenceSetsMap = new HashMap<>(); // raw data
                LinkedList<ReferenceSet> referenceSetsList = new LinkedList<>(); // reference sets
                referenceSetsList.add(new ReferenceSet(new Alternative("A0", new double[]{2.0d, 0.0d}),
                        new Alternative("A1", new double[]{0.0d, 2.0d})));
                referenceSetsMap.put(2, referenceSetsList); // the reference sets in the list are of size 2
                dmReferenceSets.put(DM_IDs[1], new ReferenceSets(1, new int[]{2}, referenceSetsMap));
            }
        } catch (ReferenceSetsConstructorException e)
        {
            throw new RuntimeException(e);
        }

        referenceSetsResult._referenceSetsContainer = new ReferenceSetsContainer(1, null, dmReferenceSets);

        try
        {
            // Get the result and print the string representation:
            interaction.feedbackprovider.Result result = feedbackProvider.generateFeedback(context, DM_IDs, referenceSetsResult);
            result.printStringRepresentation();

        } catch (FeedbackProviderException e)
        {
            throw new RuntimeException(e);
        }
    }
}
