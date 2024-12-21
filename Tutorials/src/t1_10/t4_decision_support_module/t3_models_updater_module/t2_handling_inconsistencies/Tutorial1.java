package t1_10.t4_decision_support_module.t3_models_updater_module.t2_handling_inconsistencies;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ConstructorException;
import exeption.HistoryException;
import exeption.InconsistencyHandlerException;
import history.History;
import history.PreferenceInformationWrapper;
import inconsistency.IInconsistencyHandler;
import inconsistency.RemoveOldest;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases the RemoveOldest procedure ({@link inconsistency.RemoveOldest}).
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Establish two cost-type criteria.
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        // Set the known objective space: [0.0, 2.0] x [0.0, 4.0]
        ObjectiveSpace os = new ObjectiveSpace(new Range[]{Range.get0R(2.0d), Range.get0R(4.0d)}, new boolean[2]);
        // Starting time
        LocalDateTime startingTime = LocalDateTime.now();
        // Random number generator
        IRandom R = new MersenneTwister64(0);

        // Create random model generator (creates one random model instance):
        IRandomModel<LNorm> generator = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        // Create FRS (params container):
        FRS.Params<LNorm> pFRS = new FRS.Params<>(generator);
        pFRS._feasibleSamplesToGenerate = 1000; // the procedure is requested to generate this number of feasible samples
        pFRS._samplingLimit = 1000000; // the limit for the number of samples to generate and examine
        pFRS._inconsistencyThreshold = 0; // if the method constructed less/equal this number of samples, the overall result is considered inconsistent
        // pFRS._normalizationBuilder = new StandardLinearBuilder(); already supplied
        FRS<LNorm> frs = new FRS<>(pFRS); // create object instance

        // Create inconsistency handler (if the flag is true, the handler will store reports on all visited states):
        IInconsistencyHandler<LNorm> inconsistencyHandler = new RemoveOldest<>(false);

        // Create the history object:
        History history = new History("History (tutorial)");

        // Performance vectors for iterations and two alternatives:
        double[][][] aData = new double[][][]{
                {{1.0d, 1.0d}, {0.0d, 0.0d}}, // will trigger inconsistency
                {{1.0d, 2.25d}, {0.0d, 4.0d}},
                {{1.0d, 1.0d}, {0.0d, 0.0d}}, // will trigger inconsistency
                {{1.25d, 2.0d}, {2.0d, 0.0d}},
                {{1.0d, 1.0d}, {0.0d, 0.0d}}, // will trigger inconsistency
        };

        // Create two artificial feedbacks:
        IPreferenceInformation[] feedback = new IPreferenceInformation[aData.length];
        for (int i = 0; i < aData.length; i++)
        {
            feedback[i] = PairwiseComparison.getPreference(new Alternative("A" + (i * 4), aData[i][0]),
                    new Alternative("A" + (i * 4 + 1), aData[i][1]));
        }

        try
        {
            // Simulate the iterations:
            for (int i = 0; i < feedback.length; i++)
            {
                // Register subsequent pairwise comparisons: the method returns the wrapped input (most recently provided feedback):
                LinkedList<PreferenceInformationWrapper> mostRecentFeedback = history.registerPreferenceInformation(feedback[i], i);
                System.out.println("Iteration = " + i + " =======================");
                System.out.println(history.getFullStringRepresentation());

                // Create the decision-making context:
                DMContext context = Common.getContext(i, new Alternatives(new ArrayList<>()), startingTime, criteria, os, R);
                // System-related calls:
                frs.registerDecisionMakingContext(context);
                inconsistencyHandler.registerDecisionMakingContext(context);
                frs.notifyAboutMostRecentPreferenceInformation(mostRecentFeedback);
                frs.notifyModelsConstructionBegins();

                // Create feasible samples:
                Report<LNorm> report = frs.constructModels(history.getPreferenceInformationCopy());

                // Print the report:
                System.out.println(report);
                System.out.println();

                // try to reintroduce consistency
                if (report._inconsistencyDetected)
                {
                    System.out.println("Inconsistency handling (final report):");
                    // System-related calls
                    frs.notifyConsistencyReintroductionBegins();
                    frs.clearModels();

                    inconsistency.Report<LNorm> reportIH = inconsistencyHandler.reintroduceConsistency(report, frs, history.getPreferenceInformationCopy());
                    reportIH.printStringRepresentation();
                    System.out.println();

                    // Update history:
                    history.updateHistoryWithASubset(reportIH._consistentState._preferenceInformation,
                            i, LocalDateTime.now());

                    frs.notifyConsistencyReintroductionEnds();
                }

                frs.notifyModelsConstructionEnds();
                inconsistencyHandler.unregisterDecisionMakingContext();

            }
        } catch (HistoryException | ConstructorException | InconsistencyHandlerException e)
        {
            throw new RuntimeException(e);
        }
    }

}
