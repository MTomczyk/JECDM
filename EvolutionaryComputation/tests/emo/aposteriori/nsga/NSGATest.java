package emo.aposteriori.nsga;

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
 * Provides various tests for {@link NSGA}.
 *
 * @author MTomczyk
 */
class NSGATest
{
    /**
     * Tests the {@link NSGA#adjustPopulationSize(int)} method.
     */
    @Test
    void adjustPopulationSize()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int ps = 100;
        int gens = 100;
        NSGA nsga = NSGA.getNSGA(0, false, 0.01d, ps, R, problemBundle);
        IRunner runner = new Runner(new Runner.Params(nsga));

        String msg = null;
        try
        {
            int newPS = 1 + R.nextInt(100);
            nsga.adjustPopulationSize(newPS);
            runner.init();
            assertEquals(newPS, nsga.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsga.getPopulationSize());
            assertEquals(newPS, nsga.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(newPS, nsga.getSpecimensContainer().getPopulation().size());
                assertEquals(newPS, nsga.getPopulationSize());
                assertEquals(newPS, nsga.getOffspringSize());

                newPS = 1 + R.nextInt(100);
                nsga.adjustPopulationSize(newPS);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(newPS, nsga.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsga.getPopulationSize());
            assertEquals(newPS, nsga.getOffspringSize());

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
                NSGA nsga = NSGA.getNSGA(0, update, 0.01d, ps, R, problemBundle);
                nsga.adjustOffspringSize(off);
                IRunner runner = new Runner(new Runner.Params(nsga));
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(ps, nsga.getSpecimensContainer().getPopulation().size());
                int expected = ps + off * (gens - 1);
                assertEquals(expected, nsga.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            }

        for (boolean update : new boolean[]{false, true})
            for (int[] l : new int[][]{{50, 50, 50}, {20, 50, 30}, {100, 10, 40}, {30, 50, 20}})
            {
                IRandom R = new MersenneTwister64(0);
                AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
                int ps = l[0];
                int gens = l[1];
                int off = l[2];
                NSGABuilder nsgaBuilder = new NSGABuilder(R);
                nsgaBuilder.setParentsSelector(new Random(2));
                nsgaBuilder.setPopulationSize(ps);
                nsgaBuilder.setThreshold(0.01d);
                nsgaBuilder.setProblemImplementations(problemBundle);
                nsgaBuilder.setCriteria(problemBundle._criteria);
                if (update) nsgaBuilder.setDynamicOSBoundsLearningPolicy(problemBundle);
                else nsgaBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
                String msg = null;
                NSGA nsga = null;
                try
                {
                    nsga = nsgaBuilder.getInstance();
                } catch (EAException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(nsga);
                nsga.adjustOffspringSize(off);
                IRunner runner = new Runner(new Runner.Params(nsga));
                TestUtils.compare(null, () -> runner.executeEvolution(gens));
                assertEquals(ps, nsga.getSpecimensContainer().getPopulation().size());
                int expected = ps + off * (gens - 1);
                assertEquals(expected, nsga.getSpecimensContainer().getNoPerformedFunctionEvaluations());
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
            NSGABuilder nsgaBuilder = new NSGABuilder(R);
            nsgaBuilder.setParentsSelector(new Random(2));
            nsgaBuilder.setPopulationSize(ps);
            nsgaBuilder.setThreshold(0.01d);
            nsgaBuilder.setProblemImplementations(problemBundle);
            nsgaBuilder.setCriteria(problemBundle._criteria);
            nsgaBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations);
            nsgaBuilder.setEAParamsAdjuster(p -> {
                p._offspringSize = off;
                p._offspringLimitPerGeneration = offLimit;
            });
            String msg = null;
            NSGA nsga = null;
            try
            {
                nsga = nsgaBuilder.getInstance();
            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(nsga);
            IRunner runner = new Runner(new Runner.Params(nsga));
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(ps, nsga.getSpecimensContainer().getPopulation().size());
            int expected = ps + Math.max(0, Math.min(offLimit, off)) * (gens - 1);
            assertEquals(expected, nsga.getSpecimensContainer().getNoPerformedFunctionEvaluations());
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
            NSGABuilder nsgaBuilder = new NSGABuilder(new L32_X64_MIX(0));
            nsgaBuilder.setParentsSelector(new Random(2));
            nsgaBuilder.setPopulationSize(3);
            nsgaBuilder.setThreshold(0.01d);
            Criteria C = Criteria.constructCriteria("C", new boolean[]{true, false});
            nsgaBuilder.setCriteria(C);
            nsgaBuilder.setParentsSelector(new Random(2));
            nsgaBuilder.setInitialPopulationConstructor(new DoubleConstruct(R -> new double[0])); // dummy constructor
            nsgaBuilder.setParentsReproducer(new DoubleReproduce((p1, p2, R) -> new double[0])); /// dummy reproducer

            LinkedList<double[]> evaluations = new LinkedList<>();
            evaluations.add(new double[]{0.5d, 2.0d});
            evaluations.add(new double[]{0.7d, 0.7d});
            evaluations.add(new double[]{2.0d, 0.5d});

            evaluations.add(new double[]{-1.0d, 0.0d});
            evaluations.add(new double[]{-0.5d, -0.5d});
            evaluations.add(new double[]{0.0d, -1.0d});

            nsgaBuilder.setSpecimensEvaluator(new DoubleEvaluate(v -> evaluations.pop()));
            Utils.OSChangedReport osChangedReport = new Utils.OSChangedReport();
            IOSChangeListener iosChangeListener = (ea, os, prevOS) -> {
                osChangedReport._previousOS = prevOS == null ? null : prevOS.getClone();
                osChangedReport._currentOS = os == null ? null : os.getClone();
            };

            if (mode == 0) nsgaBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
            else if (mode == 1) nsgaBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, null);
            else if (mode == 2) nsgaBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, new double[]{5.0d, 10.0d});
            else if (mode == 3)
            {
                nsgaBuilder.setDynamicOSBoundsLearningPolicy();
                nsgaBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener
                });
            }
            else if (mode == 4)
            {
                nsgaBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
                nsgaBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 5)
            {
                nsgaBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 6)
            {
                nsgaBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaBuilder.setOSMParamsAdjuster(p -> {
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 7)
            {
                nsgaBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 8)
            {
                nsgaBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nsgaBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else //noinspection ConstantValue
                if (mode == 9)
                {
                    nsgaBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                            new Linear(), new Linear()}, null, null);
                    nsgaBuilder.setOSMParamsAdjuster(p -> {
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
                NSGA nsga = nsgaBuilder.getInstance();
                IRunner runner = new Runner(nsga);
                runner.executeEvolution(2);

                if (mode == 0)
                {
                    assertNull(nsga.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 1)
                {
                    assertNull(nsga.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 2)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{1.0d, 2.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{5.0d, 10.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 3)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 4)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 5)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 6)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{-100.0d, 100.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-100.0d, 100.0d})));
                }
                else if (mode == 7)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 8)
                {
                    assertNotNull(nsga.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    // no change was triggered
                    assertNull(osChangedReport._currentOS);
                    assertNull(osChangedReport._previousOS);
                }
                else //noinspection ConstantValue
                    if (mode == 9)
                    {
                        assertNotNull(nsga.getObjectiveSpaceManager());
                        TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nsga.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                        TestUtils.assertEquals(new double[]{0.0d, 0.5d}, nsga.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
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