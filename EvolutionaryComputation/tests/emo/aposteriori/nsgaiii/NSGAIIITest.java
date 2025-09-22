package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import emo.aposteriori.Utils;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
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
import utils.TestUtils;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link NSGAIII}.
 *
 * @author MTomczyk
 */
class NSGAIIITest
{
    /**
     * Tests the {@link NSGAIII#adjustOptimizationGoals(IGoal[])} method.
     */
    @Test
    void adjustOptimizationGoals()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int cuts = 100;
        int gens = 100;
        IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
        NSGAIII nsgaiii = NSGAIII.getNSGAIII(0, false, false, R, goals, problemBundle);
        IRunner runner = new Runner(new Runner.Params(nsgaiii));

        String msg = null;
        try
        {
            int newCuts = 5 + R.nextInt(15);
            goals = GoalsFactory.getPointLineProjectionsDND(3, newCuts, problemBundle._normalizations);
            nsgaiii.adjustOptimizationGoals(goals);

            runner.init();
            assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, nsgaiii.getPopulationSize());
            assertEquals(goals.length, nsgaiii.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
                assertEquals(goals.length, nsgaiii.getPopulationSize());
                assertEquals(goals.length, nsgaiii.getOffspringSize());

                newCuts = 5 + R.nextInt(15);
                goals = GoalsFactory.getPointLineProjectionsDND(3, newCuts, problemBundle._normalizations);

                nsgaiii.adjustOptimizationGoals(goals);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, nsgaiii.getPopulationSize());
            assertEquals(goals.length, nsgaiii.getOffspringSize());

        } catch (RunnerException e)
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
            for (int[] l : new int[][]{{7, 50, 50}, {8, 50, 30}, {10, 10, 40}, {12, 50, 20}})
            {

                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int cuts = l[0];
                int gens = l[1];
                int off = l[2];
                IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
                int expected = goals.length + off * (gens - 1);
                NSGAIII nsgaiii = NSGAIII.getNSGAIII(0, update,
                        false, R, goals, problemBundle);
                nsgaiii.adjustOffspringSize(off);
                IRunner runner = new Runner(new Runner.Params(nsgaiii));
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
                assertEquals(expected, nsgaiii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }

        for (boolean update : new boolean[]{false, true})
            for (int[] l : new int[][]{{7, 50, 50}, {8, 50, 30}, {10, 10, 40}, {12, 50, 20}})
            {
                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int cuts = l[0];
                int gens = l[1];
                int off = l[2];
                IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
                int expected = goals.length + off * (gens - 1);
                NSGAIIIBuilder nsgaiiiBuilder = new NSGAIIIBuilder(R);
                nsgaiiiBuilder.setParentsSelector(new Random(2));
                nsgaiiiBuilder.setGoals(goals);
                nsgaiiiBuilder.setProblemImplementations(problemBundle);
                nsgaiiiBuilder.setCriteria(problemBundle._criteria);
                if (update) nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(problemBundle);
                else nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
                String msg = null;
                NSGAIII nsgaiii = null;
                try
                {
                    nsgaiii = nsgaiiiBuilder.getInstance();
                } catch (EAException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(nsgaiii);
                nsgaiii.adjustOffspringSize(off);
                IRunner runner = new Runner(new Runner.Params(nsgaiii));
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
                assertEquals(expected, nsgaiii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }

        for (int[] l : new int[][]{{50, 50, 50, 25}, {20, 50, 30, 25},
                {100, 10, 40, 25}, {30, 50, 20, 25}, {30, 50, 20, -5}})
        {
            IRandom R = new MersenneTwister64(0);
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
            int cuts = l[0];
            int gens = l[1];
            int off = l[2];
            int offLimit = l[3];
            IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);
            int expected = goals.length + Math.max(0, Math.min(offLimit, off)) * (gens - 1);
            NSGAIIIBuilder nsgaiiiBuilder = new NSGAIIIBuilder(R);
            nsgaiiiBuilder.setParentsSelector(new Random(2));
            nsgaiiiBuilder.setGoals(goals);
            nsgaiiiBuilder.setProblemImplementations(problemBundle);
            nsgaiiiBuilder.setCriteria(problemBundle._criteria);
            nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
            nsgaiiiBuilder.setEAParamsAdjuster(p -> {
                p._offspringSize = off;
                p._offspringLimitPerGeneration = offLimit;
            });
            String msg = null;
            NSGAIII nsgaiii = null;
            try
            {
                nsgaiii = nsgaiiiBuilder.getInstance();
            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(nsgaiii);
            IRunner runner = new Runner(new Runner.Params(nsgaiii));
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
            assertEquals(expected, nsgaiii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
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
            NSGAIIIBuilder nsgaiiiBuilder = new NSGAIIIBuilder(new L32_X64_MIX(0));
            nsgaiiiBuilder.setParentsSelector(new Random(2));
            nsgaiiiBuilder.setGoals(GoalsFactory.getLNormsDND(2, 2, Double.POSITIVE_INFINITY));
            Criteria C = Criteria.constructCriteria("C", new boolean[]{true, false});
            nsgaiiiBuilder.setCriteria(C);
            nsgaiiiBuilder.setParentsSelector(new Random(2));
            nsgaiiiBuilder.setInitialPopulationConstructor(new DoubleConstruct(R -> new double[0])); // dummy constructor
            nsgaiiiBuilder.setParentsReproducer(new DoubleReproduce((p1, p2, R) -> new double[0])); /// dummy reproducer

            LinkedList<double[]> evaluations = new LinkedList<>();
            evaluations.add(new double[]{0.5d, 2.0d});
            evaluations.add(new double[]{0.7d, 0.7d});
            evaluations.add(new double[]{2.0d, 0.5d});

            evaluations.add(new double[]{-1.0d, 0.0d});
            evaluations.add(new double[]{-0.5d, -0.5d});
            evaluations.add(new double[]{0.0d, -1.0d});

            nsgaiiiBuilder.setSpecimensEvaluator(new DoubleEvaluate(v -> evaluations.pop()));
            Utils.OSChangedReport osChangedReport = new Utils.OSChangedReport();
            IOSChangeListener iosChangeListener = (ea, os, prevOS) -> {
                osChangedReport._previousOS = prevOS == null ? null : prevOS.getClone();
                osChangedReport._currentOS = os == null ? null : os.getClone();
            };

            if (mode == 0)
                nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
            else if (mode == 1) nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, null);
            else if (mode == 2) nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, new double[]{5.0d, 10.0d});
            else if (mode == 3)
            {
                nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy();
                nsgaiiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener
                });
            }
            else if (mode == 4)
            {
                nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
                nsgaiiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 5)
            {
                nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 6)
            {
                nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 7)
            {
                nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 8)
            {
                nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else //noinspection ConstantValue
                if (mode == 9)
                {
                    nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                            new Linear(), new Linear()}, null, null);
                    nsgaiiiBuilder.setOSMParamsAdjuster(p -> {
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
                NSGAIII nsgaiii = nsgaiiiBuilder.getInstance();
                IRunner runner = new Runner(nsgaiii);
                runner.executeEvolution(2);

                if (mode == 0)
                {
                    assertNull(nsgaiii.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 1)
                {
                    assertNull(nsgaiii.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 2)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{1.0d, 2.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{5.0d, 10.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 3)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 4)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 5)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 6)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{-100.0d, 100.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-100.0d, 100.0d})));
                }
                else if (mode == 7)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 8)
                {
                    assertNotNull(nsgaiii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    // no change was triggered
                    assertNull(osChangedReport._currentOS);
                    assertNull(osChangedReport._previousOS);
                }
                else //noinspection ConstantValue
                    if (mode == 9)
                    {
                        assertNotNull(nsgaiii.getObjectiveSpaceManager());
                        TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaiii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                        TestUtils.assertEquals(new double[]{0.0d, 0.5d}, nsgaiii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                        assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{2.0d, 0.5d},
                                new Range[]{new Range(2.0d, 2.0d), new Range(0.5d, 0.5d)}, new boolean[]{true, false})));
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