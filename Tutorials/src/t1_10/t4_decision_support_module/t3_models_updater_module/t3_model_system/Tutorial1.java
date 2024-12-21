package t1_10.t4_decision_support_module.t3_models_updater_module.t3_model_system;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.HistoryException;
import exeption.ModelSystemException;
import history.History;
import history.PreferenceInformationWrapper;
import inconsistency.RemoveOldest;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import system.model.ModelSystem;
import system.model.Report;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases the ModelSystem ({@link system.model.ModelSystem}).
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
        IRandom R = new MersenneTwister64(0);

        ModelSystem.Params<LNorm> pMS = new ModelSystem.Params<>();
        pMS._preferenceModel = new model.definitions.LNorm();

        FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
        pFRS._feasibleSamplesToGenerate = 1000;
        pFRS._samplingLimit = 1000000;
        pMS._modelConstructor = new FRS<>(pFRS);

        pMS._inconsistencyHandler = new RemoveOldest<>();

        ModelSystem<LNorm> modelSystem;
        try
        {
            modelSystem = new ModelSystem<>(pMS);
        } catch (ModelSystemException e)
        {
            throw new RuntimeException(e);
        }

        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(new Range[]{Range.get0R(2.0d), Range.get0R(4.0d)}, new boolean[2]);
        LocalDateTime startingTime = LocalDateTime.now();

        History history = new History("History (tutorial)");

        double[][][] aData = new double[][][]{
                {{1.0d, 1.0d}, {0.0d, 0.0d}}, // will trigger inconsistency
                {{1.0d, 2.25d}, {0.0d, 4.0d}},
                {{1.0d, 1.0d}, {0.0d, 0.0d}}, // will trigger inconsistency
                {{1.25d, 2.0d}, {2.0d, 0.0d}},
                {{1.0d, 1.0d}, {0.0d, 0.0d}}, // will trigger inconsistency
        };

        IPreferenceInformation[] feedback = new IPreferenceInformation[aData.length];
        for (int i = 0; i < aData.length; i++)
            feedback[i] = PairwiseComparison.getPreference(new Alternative("A" + (i * 4), aData[i][0]),
                    new Alternative("A" + (i * 4 + 1), aData[i][1]));

        try
        {
            for (int i = 0; i < feedback.length; i++)
            {
                LinkedList<PreferenceInformationWrapper> mostRecentFeedback = history.registerPreferenceInformation(feedback[i], i);
                System.out.println("Iteration = " + i + " =======================");
                System.out.println(modelSystem.getHistory().getFullStringRepresentation());

                DMContext context = Common.getContext(i, new Alternatives(new ArrayList<>()), startingTime, criteria, os, R);

                modelSystem.registerDecisionMakingContext(context);
                modelSystem.notifyAboutMostRecentPreferenceInformation(mostRecentFeedback);

                // Main method:
                Report<LNorm> report = modelSystem.updateModel();
                report.printStringRepresentation();
                System.out.println();

                modelSystem.unregisterDecisionMakingContext();
            }
        } catch (ModelSystemException | HistoryException e)
        {
            throw new RuntimeException(e);
        }
    }

}
