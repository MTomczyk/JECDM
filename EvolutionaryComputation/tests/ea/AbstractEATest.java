package ea;

import emo.aposteriori.moead.MOEAD;
import emo.aposteriori.moead.MOEADBuilder;
import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaii.NSGAIIBuilder;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.EAException;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import random.XoRoShiRo128PP;
import runner.IRunner;
import runner.Runner;
import selection.Random;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link AbstractEA}.
 *
 * @author MTomczyk
 */
class AbstractEATest
{

    /**
     * Tests {@link AbstractEA#areTimestampsConsistent(EATimestamp)}.
     */
    @SuppressWarnings("DataFlowIssue")
    @Test
    void areTimestampsConsistent1()
    {
        EATimestamp[][] eaTimestamps = new EATimestamp[][]{
                {new EATimestamp(0, 1000), null}, // 0 false
                {null, new EATimestamp(0, 1000)}, // 1 false
                {null, new EATimestamp(1, 0)},  // 2 false
                {null, new EATimestamp(0, 0)},  // 3 false

                {new EATimestamp(0, 0), new EATimestamp(0, 1)}, // 4 false
                {new EATimestamp(0, 0), new EATimestamp(1, 1)}, // 5 false
                {new EATimestamp(0, 0), new EATimestamp(1, 0)}, // 6 true
                {new EATimestamp(1, 0), new EATimestamp(1, 1)}, // 7 false

                {new EATimestamp(1, 0), new EATimestamp(2, 0)}, // 8 true
                {new EATimestamp(1, 0), new EATimestamp(2, 0)}, // 9 false
                {new EATimestamp(1, 0), new EATimestamp(1, 1)}, // 10 true
                {new EATimestamp(1, 1), new EATimestamp(2, 0)}, // 11 true

                {new EATimestamp(2, 3), new EATimestamp(3, 0)}, // 12 true
                {new EATimestamp(2, 3), new EATimestamp(3, 0)}, // 13 true
                {new EATimestamp(2, 3), new EATimestamp(2, 4)}, // 14 false

        };

        int[] ssr = new int[]{
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 2, 2, 2,
                4, 4, 4
        };

        Integer[] changeSSRto = new Integer[]
                {
                        null, null, null, null,
                        null, null, null, null,
                        null, null, null, null,
                        null, 10, 10
                };

        Integer[] changeSSRto2 = new Integer[]
                {
                        null, null, null, 1000,
                        null, null, 1000, null,
                        1000, null, 1000, 1000,
                        1000, 1000, null
                };

        boolean[] exp = new boolean[]
                {
                        false, false, false, true,
                        false, false, true, false,
                        true, false, true, true,
                        true, true, false
                };

        String[] msgs = new String[]
                {
                        null, null, null, null,
                        null, null, null, null,

                        null, null, null,
                        "The expected number of steady-state repeats cannot be set, as the current timestamp is not null (the method is not in the pre-run stage), is not in the 0-th generation and 0-th SSR, and the current steady-state repeat number is not the last possible (meaning that a whole generation has not passed): the current timestamp = [Generation = 2; Steady-state repeat = 0]; the number of SSR in the previous generation = 2; the new expected number of SSR = 1000",

                        "The expected number of steady-state repeats cannot be set, as the current timestamp is not null (the method is not in the pre-run stage), is not in the 0-th generation and 0-th SSR, and the current steady-state repeat number is not the last possible (meaning that a whole generation has not passed): the current timestamp = [Generation = 3; Steady-state repeat = 0]; the number of SSR in the previous generation = 4; the new expected number of SSR = 1000",
                        "The expected number of steady-state repeats cannot be set, as the current timestamp is not null (the method is not in the pre-run stage), is not in the 0-th generation and 0-th SSR, and the current steady-state repeat number is not the last possible (meaning that a whole generation has not passed): the current timestamp = [Generation = 3; Steady-state repeat = 0]; the number of SSR in the previous generation = 10; the new expected number of SSR = 1000",
                        null
                };

        for (int i = 0; i < 15; i++)
        {
            NSGAIIBuilder builder = new NSGAIIBuilder(new XoRoShiRo128PP(0));
            DTLZBundle problem = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            builder.setCriteria(problem._criteria);
            builder.setFixedOSBoundsLearningPolicy(problem._normalizations);
            builder._populationSize = 100;
            builder._select = new Random(2);
            builder.setSpecimensEvaluator(problem._evaluate);
            builder.setInitialPopulationConstructor(problem._construct);
            builder.setParentsReproducer(problem._reproduce);
            int finalI = i;
            builder.setEAParamsAdjuster(p -> p._expectedNumberOfSteadyStateRepeats = ssr[finalI]);
            NSGAII nsgaii = null;
            String msg = null;
            try
            {
                nsgaii = builder.getInstance();
                nsgaii.updateCurrentTimestamp(eaTimestamps[i][0]);
                if (changeSSRto[i] != null)
                    nsgaii.updateExpectedNumberOfSteadyStateRepeats(changeSSRto[i]);
                assertEquals(exp[i], nsgaii.areTimestampsConsistent(eaTimestamps[i][1]));
                nsgaii.updateCurrentTimestamp(eaTimestamps[i][1]);

                nsgaii.transitSSRData();

                if (changeSSRto2[i] != null)
                    nsgaii.updateExpectedNumberOfSteadyStateRepeats(changeSSRto2[i]);


            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertEquals(msgs[i], msg);
            assertNotNull(nsgaii);
        }
    }

    /**
     * Tests {@link AbstractEA#areTimestampsConsistent(EATimestamp)}.
     */
    @Test
    void areTimestampsConsistent2()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int cuts = 5;
        int gens = 10;
        IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
        MOEADBuilder moeadBuilder = new MOEADBuilder(R);
        moeadBuilder.setParentsSelector(new Random(2));
        moeadBuilder.setGoals(goals);
        moeadBuilder.setProblemImplementations(problemBundle);
        moeadBuilder.setCriteria(problemBundle._criteria);
        moeadBuilder.setSimilarity(new Euclidean());
        moeadBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
        String msg = null;
        MOEAD moead = null;
        try
        {
            moead = moeadBuilder.getInstance();
        } catch (EAException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(moead);
        IRunner runner = new Runner(new Runner.Params(moead));
        runner.setSteadyStateRepeatsFor(goals.length, 0);
        TestUtils.compare(null, () -> runner.executeEvolution(gens));
    }
}