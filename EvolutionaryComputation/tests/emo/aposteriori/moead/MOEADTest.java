package emo.aposteriori.moead;

import criterion.Criteria;
import emo.aposteriori.Utils;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.ISimilarity;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import emo.utils.front.Front;
import exception.EAException;
import exception.RunnerException;
import org.junit.jupiter.api.Test;
import os.IOSChangeListener;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import population.ISpecimenGetter;
import population.Specimen;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.L32_X64_MIX;
import random.MersenneTwister64;
import reproduction.DoubleReproduce;
import runner.IRunner;
import runner.Runner;
import selection.Random;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;
import space.os.ObjectiveSpace;
import space.simplex.DasDennis;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link MOEAD}.
 *
 * @author MTomczyk
 */
class MOEADTest
{
    /**
     * Tests the {@link MOEAD#adjustOptimizationGoals(IGoal[], ISimilarity)} method.
     */
    @Test
    void adjustOptimizationGoals()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int cuts = 50;
        int gens = 100;
        ISimilarity similarity = new Euclidean();
        IGoal[] goals = GoalsFactory.getLNormsDND(3, cuts, Double.POSITIVE_INFINITY, problemBundle._normalizations);

        MOEAD moead = MOEAD.getMOEAD(0, false, false, R, goals, problemBundle,
                similarity, 10);
        IRunner runner = new Runner(new Runner.Params(moead));
        runner.setSteadyStateRepeatsFor(goals.length, 0);

        String msg = null;
        try
        {
            int newCuts = 5 + R.nextInt(15);
            goals = GoalsFactory.getLNormsDND(3, newCuts, Double.POSITIVE_INFINITY, problemBundle._normalizations);
            moead.adjustOptimizationGoals(goals, similarity);
            runner.setSteadyStateRepeatsFor(goals.length, 0);

            runner.init();
            assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, moead.getPopulationSize());
            assertEquals(1, moead.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
                assertEquals(goals.length, moead.getPopulationSize());
                assertEquals(1, moead.getOffspringSize());

                newCuts = 5 + R.nextInt(15);
                goals = GoalsFactory.getLNormsDND(3, newCuts, Double.POSITIVE_INFINITY, problemBundle._normalizations);

                moead.adjustOptimizationGoals(goals, similarity);
                runner.setSteadyStateRepeatsFor(goals.length, 0);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, moead.getPopulationSize());
            assertEquals(1, moead.getOffspringSize());

        } catch (RunnerException | EAException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

    /**
     * Tests the "performed evaluations counter".
     */
    @Test
    void testPerformedEvaluations()
    {
        for (boolean update : new boolean[]{false, true})
            for (int[] l : new int[][]{{7, 50}, {8, 50}, {10, 10}, {12, 50}})
            {
                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int cuts = l[0];
                int gens = l[1];
                IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
                int expected = goals.length * gens;
                MOEAD moead = MOEAD.getMOEAD(0, update, false,
                        R, goals, problemBundle,
                        new Euclidean(), 10);
                IRunner runner = new Runner(new Runner.Params(moead));
                runner.setSteadyStateRepeatsFor(goals.length, 0);
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
                assertEquals(expected, moead.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }
        for (boolean update : new boolean[]{false, true})
            for (int[] l : new int[][]{{7, 50}, {8, 50}, {10, 10}, {12, 50}})
            {
                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int cuts = l[0];
                int gens = l[1];
                IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
                int expected = goals.length * gens;
                MOEADBuilder moeadBuilder = new MOEADBuilder(R);
                moeadBuilder.setParentsSelector(new Random(2));
                moeadBuilder.setGoals(goals);
                moeadBuilder.setProblemImplementations(problemBundle);
                moeadBuilder.setCriteria(problemBundle._criteria);
                moeadBuilder.setSimilarity(new Euclidean());
                if (update) moeadBuilder.setDynamicOSBoundsLearningPolicy(problemBundle);
                else moeadBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
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
                assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
                assertEquals(expected, moead.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }

        for (int[] l : new int[][]{
                {7, 50, 1, 10000},
                {8, 50, 1, 10000},
                {10, 10, 1, 10000},
                {12, 50, 1, 10000},
                {12, 50, 2, 10000},
                {12, 50, 2, DasDennis.getNoProblems(2, 12)},
                {12, 50, 2, 17},
                {12, 50, 2, 0},
                {12, 50, 2, -5}})
        {
            IRandom R = new MersenneTwister64(0);
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
            int cuts = l[0];
            int gens = l[1];
            int off = l[2];
            int offLimit = l[3];
            IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
            int expected = goals.length + Math.max(0, Math.min(offLimit, off * goals.length)) * (gens - 1);
            MOEADBuilder moeadBuilder = new MOEADBuilder(R);
            moeadBuilder.setParentsSelector(new Random(2));
            moeadBuilder.setGoals(goals);
            moeadBuilder.setProblemImplementations(problemBundle);
            moeadBuilder.setCriteria(problemBundle._criteria);
            moeadBuilder.setSimilarity(new Euclidean());
            moeadBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
            moeadBuilder.setEAParamsAdjuster(p -> {
                p._offspringSize = off;
                p._offspringLimitPerGeneration = offLimit;
            });
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
            assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
            assertEquals(expected, moead.getSpecimensContainer().getNoPerformedFunctionEvaluations());
        }
    }

    /**
     * Tests the objective space manager.
     */
    @Test
    void testOSManager()
    {
        for (int mode = 0; mode < 10; mode++)
        {
            MOEADBuilder moeadBuilder = new MOEADBuilder(new L32_X64_MIX(0));
            moeadBuilder.setParentsSelector(new Random(2));
            moeadBuilder.setGoals(GoalsFactory.getLNormsDND(2, 2, Double.POSITIVE_INFINITY));
            Criteria C = Criteria.constructCriteria("C", new boolean[]{true, false});
            moeadBuilder.setCriteria(C);
            moeadBuilder.setParentsSelector(new Random(2));
            moeadBuilder.setInitialPopulationConstructor(new DoubleConstruct(R -> new double[0])); // dummy constructor
            moeadBuilder.setParentsReproducer(new DoubleReproduce((p1, p2, R) -> new double[0])); /// dummy reproducer
            moeadBuilder.setSimilarity(new Euclidean());
            moeadBuilder.setNeighborhoodSize(1);

            LinkedList<double[]> evaluations = new LinkedList<>();
            evaluations.add(new double[]{0.5d, 2.0d});
            evaluations.add(new double[]{0.7d, 0.7d});
            evaluations.add(new double[]{2.0d, 0.5d});

            evaluations.add(new double[]{-1.0d, 0.0d});
            evaluations.add(new double[]{-0.5d, -0.5d});
            evaluations.add(new double[]{0.0d, -1.0d});

            moeadBuilder.setSpecimensEvaluator(new DoubleEvaluate(v -> evaluations.pop()));
            Utils.OSChangedReport osChangedReport = new Utils.OSChangedReport();
            IOSChangeListener iosChangeListener = (ea, os, prevOS) -> {
                osChangedReport._previousOS = prevOS == null ? null : prevOS.getClone();
                osChangedReport._currentOS = os == null ? null : os.getClone();
            };

            if (mode == 0)
                moeadBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
            else if (mode == 1) moeadBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, null);
            else if (mode == 2) moeadBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, new double[]{5.0d, 10.0d});
            else if (mode == 3)
            {
                moeadBuilder.setDynamicOSBoundsLearningPolicy();
                moeadBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener
                });
            }
            else if (mode == 4)
            {
                moeadBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
                moeadBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 5)
            {
                moeadBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                moeadBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 6)
            {
                moeadBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                moeadBuilder.setOSMParamsAdjuster(p -> {
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 7)
            {
                moeadBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                moeadBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 8)
            {
                moeadBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                moeadBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else //noinspection ConstantValue
                if (mode == 9)
                {
                    moeadBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                            new Linear(), new Linear()}, null, null);
                    moeadBuilder.setOSMParamsAdjuster(p -> {
                        p._specimenGetters = new ISpecimenGetter[]{
                                container -> {
                                    ArrayList<Specimen> specimen = new ArrayList<>(container.getPopulation().size());
                                    specimen.addAll(container.getPopulation());
                                    if (container.getOffspring() != null) specimen.addAll(container.getOffspring());
                                    Front f = new Front(C);
                                    LinkedList<Specimen> r = f.getNonDominatedSpecimens(specimen);
                                    return new ArrayList<>(r);
                                }
                        };
                        p._listeners = new IOSChangeListener[]{
                                iosChangeListener};
                    });
                }

            String msg = null;
            try
            {
                MOEAD moead = moeadBuilder.getInstance();
                IRunner runner = new Runner(moead, moeadBuilder.getPopulationSize());
                runner.executeEvolution(2);

                if (mode == 0)
                {
                    assertNull(moead.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 1)
                {
                    assertNull(moead.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 2)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{1.0d, 2.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{5.0d, 10.0d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 3)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-1.0d, 0.7d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 4)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-1.0d, 0.7d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 5)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-1.0d, 0.7d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 6)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-100.0d, 100.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-100.0d, 100.0d})));
                }
                else if (mode == 7)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 8)
                {
                    assertNotNull(moead.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    // no change was triggered
                    assertNull(osChangedReport._currentOS);
                    assertNull(osChangedReport._previousOS);
                }
                else //noinspection ConstantValue
                    if (mode == 9)
                    {
                        assertNotNull(moead.getObjectiveSpaceManager());
                        TestUtils.assertEquals(new double[]{2.0d, -1.0d}, moead.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                        TestUtils.assertEquals(new double[]{0.0d, 0.5d}, moead.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                        assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-0.5d, 0.5d},
                                new Range[]{new Range(-0.5d, 2.0d), new Range(-0.5d, 0.5d)}, new boolean[]{true, false})));
                        assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{0.0d, 0.5d})));
                    }
            } catch (EAException | RunnerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }
}