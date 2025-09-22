package emo.interactive.iemod;

import criterion.Criteria;
import emo.aposteriori.Utils;
import emo.interactive.StandardDSSBuilder;
import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import emo.utils.front.Front;
import exception.EAException;
import exception.RunnerException;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.rs.iterationslimit.Constant;
import model.definitions.LNorm;
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
 * Provides various tests for {@link IEMOD}.
 *
 * @author MTomczyk
 */
class IEMODTest
{
    /**
     * Tests the "performed evaluations counter".
     */
    @Test
    void testPerformedEvaluations()
    {
        for (int[] l : new int[][]{{7, 50}, {8, 50}, {10, 10}, {12, 50}})
        {
            IRandom R = new MersenneTwister64(0);
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            int cuts = l[0];
            int gens = l[1];
            IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(2, cuts, problemBundle._normalizations);
            int expected = goals.length * gens;
            FRS.Params<model.internals.value.scalarizing.LNorm> p =
                    new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            p._iterationsLimit = new Constant(100000);
            p._feasibleSamplesToGenerate = goals.length;
            IEMOD iemod = IEMOD.getIEMOD(
                    false, false,
                    R, goals, problemBundle, new Euclidean(), 10,
                    new IterationInterval(5, 100, 1),
                    new RandomPairs(new RequiredSpread(1.0E-3)),
                    new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(
                            new double[]{0.5d, 0.5d},
                            Double.POSITIVE_INFINITY, INormalization.getCloned(problemBundle._normalizations)))),
                    new LNorm((model.internals.value.scalarizing.LNorm) null),
                    new FRS<>(p));
            IRunner runner = new Runner(new Runner.Params(iemod));
            runner.setSteadyStateRepeatsFor(goals.length, 0);
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(goals.length, iemod.getSpecimensContainer().getPopulation().size());
            assertEquals(expected, iemod.getSpecimensContainer().getNoPerformedFunctionEvaluations());
        }

        for (int[] l : new int[][]{{50, 50, 50, 25}, {20, 50, 30, 25},
                {100, 10, 40, 25}, {30, 50, 20, 25}, {30, 50, 20, -5}})
        {
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            int cuts = l[0];
            int gens = l[1];
            int off = l[2];
            int offLimit = l[3];

            IEMODBuilder<model.internals.value.scalarizing.LNorm> iemodBuilder = new IEMODBuilder<>(new L32_X64_MIX(0));
            iemodBuilder.setParentsSelector(new Random(2));
            IGoal[] goals = GoalsFactory.getLNormsDND(2, cuts, Double.POSITIVE_INFINITY);
            int expected = goals.length + Math.max(0, Math.min(offLimit, off * goals.length)) * (gens - 1);
            iemodBuilder.setGoals(goals);
            iemodBuilder.setCriteria(problemBundle._criteria);
            iemodBuilder.setParentsSelector(new Random(2));
            iemodBuilder.setInitialPopulationConstructor(problemBundle._construct); // dummy constructor
            iemodBuilder.setParentsReproducer(problemBundle._reproduce); /// dummy reproducer
            iemodBuilder.setSpecimensEvaluator(problemBundle._evaluate);
            iemodBuilder.setSimilarity(new Euclidean());
            iemodBuilder.setNeighborhoodSize(10);

            StandardDSSBuilder<model.internals.value.scalarizing.LNorm> DSSB = new StandardDSSBuilder<>();
            iemodBuilder.setStandardDSSBuilder(DSSB);
            iemodBuilder.setUseUtopiaIncumbent(false);
            DSSB.setInteractionRule(new IterationInterval(10, 1000, 1));
            DSSB.setReferenceSetConstructor(new RandomPairs(new RequiredSpread(1.0E-6)));
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            DSSB.setPreferenceModel(new LNorm());
            FRS.Params<model.internals.value.scalarizing.LNorm>
                    pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 100000;
            pFRS._feasibleSamplesToGenerate = goals.length;
            pFRS._initialModels = new model.internals.value.scalarizing.LNorm[iemodBuilder.getGoals().length];
            for (int i = 0; i < pFRS._initialModels.length; i++)
            {
                pFRS._initialModels[i] = new model.internals.value.scalarizing.LNorm(
                        goals[i].getParams()[0],
                        goals[i].getParams()[1][0], null);
            }
            DSSB.setModelConstructor(new FRS<>(pFRS));
            iemodBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations, problemBundle._utopia,
                    problemBundle._nadir);
            iemodBuilder.setEAParamsAdjuster(p -> {
                p._offspringSize = off;
                p._offspringLimitPerGeneration = offLimit;
            });

            String msg = null;
            IEMOD iemod = null;
            try
            {
                iemod = iemodBuilder.getInstance();
            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(iemod);
            IRunner runner = new Runner(new Runner.Params(iemod));
            runner.setSteadyStateRepeatsFor(goals.length, 0);
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(goals.length, iemod.getSpecimensContainer().getPopulation().size());
            assertEquals(expected, iemod.getSpecimensContainer().getNoPerformedFunctionEvaluations());
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
            IEMODBuilder<model.internals.value.scalarizing.LNorm> iemodBuilder = new IEMODBuilder<>(new L32_X64_MIX(0));
            iemodBuilder.setParentsSelector(new Random(2));
            IGoal[] goals = GoalsFactory.getLNormsDND(2, 2, Double.POSITIVE_INFINITY);
            iemodBuilder.setGoals(goals);
            Criteria C = Criteria.constructCriteria("C", new boolean[]{true, false});
            iemodBuilder.setCriteria(C);
            iemodBuilder.setParentsSelector(new Random(2));
            iemodBuilder.setInitialPopulationConstructor(new DoubleConstruct(R -> new double[0])); // dummy constructor
            iemodBuilder.setParentsReproducer(new DoubleReproduce((p1, p2, R) -> new double[0])); /// dummy reproducer
            iemodBuilder.setSimilarity(new Euclidean());
            iemodBuilder.setNeighborhoodSize(1);
            iemodBuilder.setReassignmentStrategy((report, ea, moeadGoalsManager) -> {
                for (int s = 0; s < ea.getSpecimensContainer().getPopulation().size(); s++)
                {
                    Family family = moeadGoalsManager.getFamilies()[0];
                    IGoal goal = family.getGoal(s);
                    Specimen specimen = ea.getSpecimensContainer().getPopulation().get(s);
                    Assignment GA = new Assignment(goal);
                    GA.setFirstSpecimen(specimen);
                    GA.setFirstSpecimenEvaluation(goal.evaluate(specimen));
                    family.setAssignment(s, GA);
                }
            });

            StandardDSSBuilder<model.internals.value.scalarizing.LNorm> DSSB = new StandardDSSBuilder<>();
            iemodBuilder.setStandardDSSBuilder(DSSB);
            iemodBuilder.setUseUtopiaIncumbent(false);
            DSSB.setInteractionRule(new IterationInterval(1000, 1000));
            DSSB.setReferenceSetConstructor(new RandomPairs());
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            DSSB.setPreferenceModel(new LNorm());
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 100000;
            pFRS._feasibleSamplesToGenerate = 3;
            pFRS._initialModels = new model.internals.value.scalarizing.LNorm[iemodBuilder.getGoals().length];
            for (int i = 0; i < pFRS._initialModels.length; i++)
            {
                pFRS._initialModels[i] = new model.internals.value.scalarizing.LNorm(
                        goals[i].getParams()[0],
                        goals[i].getParams()[1][0], null);
            }

            DSSB.setModelConstructor(new FRS<>(pFRS));

            LinkedList<double[]> evaluations = new LinkedList<>();
            evaluations.add(new double[]{0.5d, 2.0d});
            evaluations.add(new double[]{0.7d, 0.7d});
            evaluations.add(new double[]{2.0d, 0.5d});

            evaluations.add(new double[]{-1.0d, 0.0d});
            evaluations.add(new double[]{-0.5d, -0.5d});
            evaluations.add(new double[]{0.0d, -1.0d});

            iemodBuilder.setSpecimensEvaluator(new DoubleEvaluate(v -> evaluations.pop()));
            Utils.OSChangedReport osChangedReport = new Utils.OSChangedReport();
            IOSChangeListener iosChangeListener = (ea, os, prevOS) -> {
                osChangedReport._previousOS = prevOS == null ? null : prevOS.getClone();
                osChangedReport._currentOS = os == null ? null : os.getClone();
            };

            if (mode == 0)
                iemodBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
            else if (mode == 1) iemodBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, null);
            else if (mode == 2) iemodBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, new double[]{5.0d, 10.0d});
            else if (mode == 3)
            {
                iemodBuilder.setDynamicOSBoundsLearningPolicy();
                iemodBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener
                });
            }
            else if (mode == 4)
            {
                iemodBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
                iemodBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 5)
            {
                iemodBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                iemodBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 6)
            {
                iemodBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                iemodBuilder.setOSMParamsAdjuster(p -> {
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 7)
            {
                iemodBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                iemodBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 8)
            {
                iemodBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                iemodBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else //noinspection ConstantValue
                if (mode == 9)
                {
                    iemodBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                            new Linear(), new Linear()}, null, null);
                    iemodBuilder.setOSMParamsAdjuster(p -> {
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
                IEMOD iemod = iemodBuilder.getInstance();
                IRunner runner = new Runner(iemod, iemodBuilder.getPopulationSize());
                runner.executeEvolution(2);

                if (mode == 0)
                {
                    assertNull(iemod.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 1)
                {
                    assertNull(iemod.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 2)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{1.0d, 2.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{5.0d, 10.0d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 3)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-1.0d, 0.7d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 4)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-1.0d, 0.7d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 5)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-1.0d, 0.7d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 6)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -0.5d}, new double[]{-100.0d, 100.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-100.0d, 100.0d})));
                }
                else if (mode == 7)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 0.7d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 0.7d})));
                }
                else if (mode == 8)
                {
                    assertNotNull(iemod.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    // no change was triggered
                    assertNull(osChangedReport._currentOS);
                    assertNull(osChangedReport._previousOS);
                }
                else //noinspection ConstantValue
                    if (mode == 9)
                    {
                        assertNotNull(iemod.getObjectiveSpaceManager());
                        TestUtils.assertEquals(new double[]{2.0d, -1.0d}, iemod.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                        TestUtils.assertEquals(new double[]{0.0d, 0.5d}, iemod.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
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