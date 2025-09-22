package emo.aposteriori.nsgaii;

import criterion.Criteria;
import emo.aposteriori.Utils;
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
 * Provides various tests for {@link NSGAII}.
 *
 * @author MTomczyk
 */
class NSGAIITest
{
    /**
     * Tests the {@link NSGAII#adjustPopulationSize(int)} method.
     */
    @Test
    void adjustPopulationSize()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int ps = 100;
        int gens = 100;
        NSGAII nsgaii = NSGAII.getNSGAII(0, false, ps, R, problemBundle);
        IRunner runner = new Runner(new Runner.Params(nsgaii));

        String msg = null;
        try
        {
            int newPS = 1 + R.nextInt(100);
            nsgaii.adjustPopulationSize(newPS);
            runner.init();
            assertEquals(newPS, nsgaii.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsgaii.getPopulationSize());
            assertEquals(newPS, nsgaii.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(newPS, nsgaii.getSpecimensContainer().getPopulation().size());
                assertEquals(newPS, nsgaii.getPopulationSize());
                assertEquals(newPS, nsgaii.getOffspringSize());

                newPS = 1 + R.nextInt(100);
                nsgaii.adjustPopulationSize(newPS);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(newPS, nsgaii.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsgaii.getPopulationSize());
            assertEquals(newPS, nsgaii.getOffspringSize());

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
            for (int[] l : new int[][]{{50, 50, 50}, {20, 50, 30}, {100, 10, 40}, {30, 50, 20}})
            {
                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int ps = l[0];
                int gens = l[1];
                int off = l[2];
                NSGAII nsgaii = NSGAII.getNSGAII(0, update, ps, R, problemBundle);
                nsgaii.adjustOffspringSize(off);
                IRunner runner = new Runner(new Runner.Params(nsgaii));
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(ps, nsgaii.getSpecimensContainer().getPopulation().size());
                int expected = ps + off * (gens - 1);
                assertEquals(expected, nsgaii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }

        for (boolean update : new boolean[]{false, true})
            for (int[] l : new int[][]{{50, 50, 50}, {20, 50, 30}, {100, 10, 40}, {30, 50, 20}})
            {
                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int ps = l[0];
                int gens = l[1];
                int off = l[2];
                NSGAIIBuilder nsgaiiBuilder = new NSGAIIBuilder(R);
                nsgaiiBuilder.setParentsSelector(new Random(2));
                nsgaiiBuilder.setPopulationSize(ps);
                nsgaiiBuilder.setProblemImplementations(problemBundle);
                nsgaiiBuilder.setCriteria(problemBundle._criteria);
                if (update) nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(problemBundle);
                else nsgaiiBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
                String msg = null;
                NSGAII nsgaii = null;
                try
                {
                    nsgaii = nsgaiiBuilder.getInstance();
                } catch (EAException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(nsgaii);
                nsgaii.adjustOffspringSize(off);
                IRunner runner = new Runner(new Runner.Params(nsgaii));
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(ps, nsgaii.getSpecimensContainer().getPopulation().size());
                int expected = ps + off * (gens - 1);
                assertEquals(expected, nsgaii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }

        for (int[] l : new int[][]{{50, 50, 50, 25}, {20, 50, 30, 25},
                {100, 10, 40, 25}, {30, 50, 20, 25}, {30, 50, 20, -5}})
        {
            IRandom R = new MersenneTwister64(0);
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
            int ps = l[0];
            int gens = l[1];
            int off = l[2];
            int offLimit = l[3];
            NSGAIIBuilder nsgaiiBuilder = new NSGAIIBuilder(R);
            nsgaiiBuilder.setParentsSelector(new Random(2));
            nsgaiiBuilder.setPopulationSize(ps);
            nsgaiiBuilder.setProblemImplementations(problemBundle);
            nsgaiiBuilder.setCriteria(problemBundle._criteria);
            nsgaiiBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
            nsgaiiBuilder.setEAParamsAdjuster(p -> {
                p._offspringSize = off;
                p._offspringLimitPerGeneration = offLimit;
            });
            String msg = null;
            NSGAII nsgaii = null;
            try
            {
                nsgaii = nsgaiiBuilder.getInstance();
            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(nsgaii);
            IRunner runner = new Runner(new Runner.Params(nsgaii));
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(ps, nsgaii.getSpecimensContainer().getPopulation().size());
            int expected = ps + Math.max(0, Math.min(offLimit, off)) * (gens - 1);
            assertEquals(expected, nsgaii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
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
            NSGAIIBuilder nsgaiiBuilder = new NSGAIIBuilder(new L32_X64_MIX(0));
            nsgaiiBuilder.setParentsSelector(new Random(2));
            nsgaiiBuilder.setPopulationSize(3);
            Criteria C = Criteria.constructCriteria("C", new boolean[]{true, false});
            nsgaiiBuilder.setCriteria(C);
            nsgaiiBuilder.setParentsSelector(new Random(2));
            nsgaiiBuilder.setInitialPopulationConstructor(new DoubleConstruct(R -> new double[0])); // dummy constructor
            nsgaiiBuilder.setParentsReproducer(new DoubleReproduce((p1, p2, R) -> new double[0])); /// dummy reproducer

            LinkedList<double[]> evaluations = new LinkedList<>();
            evaluations.add(new double[]{0.5d, 2.0d});
            evaluations.add(new double[]{0.7d, 0.7d});
            evaluations.add(new double[]{2.0d, 0.5d});

            evaluations.add(new double[]{-1.0d, 0.0d});
            evaluations.add(new double[]{-0.5d, -0.5d});
            evaluations.add(new double[]{0.0d, -1.0d});

            nsgaiiBuilder.setSpecimensEvaluator(new DoubleEvaluate(v -> evaluations.pop()));
            Utils.OSChangedReport osChangedReport = new Utils.OSChangedReport();
            IOSChangeListener iosChangeListener = (ea, os, prevOS) -> {
                osChangedReport._previousOS = prevOS == null ? null : prevOS.getClone();
                osChangedReport._currentOS = os == null ? null : os.getClone();
            };

            if (mode == 0)
                nsgaiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
            else if (mode == 1) nsgaiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, null);
            else if (mode == 2) nsgaiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, new double[]{5.0d, 10.0d});
            else if (mode == 3)
            {
                nsgaiiBuilder.setDynamicOSBoundsLearningPolicy();
                nsgaiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener
                });
            }
            else if (mode == 4)
            {
                nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
                nsgaiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 5)
            {
                nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 6)
            {
                nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 7)
            {
                nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 8)
            {
                nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else //noinspection ConstantValue
                if (mode == 9)
                {
                    nsgaiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                            new Linear(), new Linear()}, null, null);
                    nsgaiiBuilder.setOSMParamsAdjuster(p -> {
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
                NSGAII nsgaii = nsgaiiBuilder.getInstance();
                IRunner runner = new Runner(nsgaii);
                runner.executeEvolution(2);

                if (mode == 0)
                {
                    assertNull(nsgaii.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 1)
                {
                    assertNull(nsgaii.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 2)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{1.0d, 2.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{5.0d, 10.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 3)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 4)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 5)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 6)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{-100.0d, 100.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-100.0d, 100.0d})));
                }
                else if (mode == 7)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 8)
                {
                    assertNotNull(nsgaii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    // no change was triggered
                    assertNull(osChangedReport._currentOS);
                    assertNull(osChangedReport._previousOS);
                }
                else //noinspection ConstantValue
                    if (mode == 9)
                    {
                        assertNotNull(nsgaii.getObjectiveSpaceManager());
                        TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsgaii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                        TestUtils.assertEquals(new double[]{0.0d, 0.5d}, nsgaii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                        System.out.println(osChangedReport._previousOS.toString());
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