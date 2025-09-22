package emo.interactive.nemo.nemoii;

import criterion.Criteria;
import emo.aposteriori.Utils;
import emo.interactive.StandardDSSBuilder;
import emo.interactive.nemo.nemo0.NEMO0;
import emo.interactive.nemo.nemo0.NEMO0Builder;
import emo.utils.front.Front;
import exception.EAException;
import exception.RunnerException;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.representative.MDVF;
import model.constructor.value.representative.RepresentativeModel;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import os.IOSChangeListener;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import population.ISpecimenGetter;
import population.Specimen;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
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
 * Provides various tests for {@link NEMOII}.
 *
 * @author MTomczyk
 */
class NEMOIITest
{
    /**
     * Tests the "performed evaluations counter".
     */
    @Test
    void testPerformedEvaluations()
    {
        for (int[] l : new int[][]{{50, 50, 50}, {20, 50, 30},
                {100, 10, 40}, {30, 50, 20}})
        {
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            int ps = l[0];
            int gens = l[1];
            int off = l[2];

            NEMO0Builder<LNorm> nemo0Builder = new NEMO0Builder<>(new L32_X64_MIX(0));
            nemo0Builder.setParentsSelector(new Random(2));
            nemo0Builder.setPopulationSize(ps);
            nemo0Builder.setCriteria(problemBundle._criteria);
            nemo0Builder.setParentsSelector(new Random(2));
            nemo0Builder.setInitialPopulationConstructor(problemBundle._construct); // dummy constructor
            nemo0Builder.setParentsReproducer(problemBundle._reproduce); /// dummy reproducer
            nemo0Builder.setSpecimensEvaluator(problemBundle._evaluate);
            StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
            nemo0Builder.setStandardDSSBuilder(DSSB);
            nemo0Builder.setUseUtopiaIncumbent(false);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS =
                    new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 100000;
            DSSB.setModelConstructor(new RepresentativeModel<>(new RepresentativeModel.Params<>(new FRS<>(pFRS), new MDVF<>())));
            DSSB.setInteractionRule(new IterationInterval(5, 1000, 1));
            DSSB.setReferenceSetConstructor(new RandomPairs());
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            DSSB.setPreferenceModel(new model.definitions.LNorm());
            nemo0Builder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations, problemBundle._utopia, problemBundle._nadir);
            nemo0Builder.setEAParamsAdjuster(p -> p._offspringSize = off);
            String msg = null;
            NEMO0 nemo0 = null;
            try
            {
                nemo0 = nemo0Builder.getInstance();
            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(nemo0);
            IRunner runner = new Runner(new Runner.Params(nemo0));
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(ps, nemo0.getSpecimensContainer().getPopulation().size());
            int expected = ps + off * (gens - 1);
            assertEquals(expected, nemo0.getSpecimensContainer().getNoPerformedFunctionEvaluations());
        }

        for (int[] l : new int[][]{{50, 50, 50, 25}, {20, 50, 30, 25},
                {100, 10, 40, 25}, {30, 50, 20, 25}, {30, 50, 20, -5}})
        {
            AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 2, 10);
            int ps = l[0];
            int gens = l[1];
            int off = l[2];
            int offLimit = l[3];
            NEMOIIBuilder<LNorm> nemoiiBuilder = new NEMOIIBuilder<>(new L32_X64_MIX(0));
            nemoiiBuilder.setParentsSelector(new Random(2));
            nemoiiBuilder.setPopulationSize(ps);
            nemoiiBuilder.setCriteria(problemBundle._criteria);
            nemoiiBuilder.setParentsSelector(new Random(2));
            nemoiiBuilder.setInitialPopulationConstructor(problemBundle._construct); // dummy constructor
            nemoiiBuilder.setParentsReproducer(problemBundle._reproduce); /// dummy reproducer
            nemoiiBuilder.setSpecimensEvaluator(problemBundle._evaluate);
            StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
            nemoiiBuilder.setStandardDSSBuilder(DSSB);
            nemoiiBuilder.setUseUtopiaIncumbent(false);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS =
                    new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 100000;
            DSSB.setModelConstructor(new RepresentativeModel<>(new RepresentativeModel.Params<>(new FRS<>(pFRS), new MDVF<>())));
            DSSB.setInteractionRule(new IterationInterval(5, 1000, 1)); // execute at least once
            DSSB.setReferenceSetConstructor(new RandomPairs());
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            DSSB.setPreferenceModel(new model.definitions.LNorm());
            nemoiiBuilder.setFixedOSBoundsLearningPolicy(problemBundle._normalizations, problemBundle._utopia, problemBundle._nadir);
            nemoiiBuilder.setEAParamsAdjuster(p -> {
                p._offspringSize = off;
                p._offspringLimitPerGeneration = offLimit;
            });
            String msg = null;
            NEMOII nemoii = null;
            try
            {
                nemoii = nemoiiBuilder.getInstance();
            } catch (EAException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(nemoii);
            IRunner runner = new Runner(new Runner.Params(nemoii));
            TestUtils.compare(null, () -> runner.executeEvolution(gens));
            assertEquals(ps, nemoii.getSpecimensContainer().getPopulation().size());
            int expected = ps + Math.max(0, Math.min(offLimit, off)) * (gens - 1);
            assertEquals(expected, nemoii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
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
            NEMOIIBuilder<LNorm> nemoiiBuilder = new NEMOIIBuilder<>(new L32_X64_MIX(0));
            nemoiiBuilder.setParentsSelector(new Random(2));
            Criteria C = Criteria.constructCriteria("C", new boolean[]{true, false});
            nemoiiBuilder.setPopulationSize(3);
            nemoiiBuilder.setCriteria(C);
            nemoiiBuilder.setParentsSelector(new Random(2));
            nemoiiBuilder.setInitialPopulationConstructor(new DoubleConstruct(R -> new double[0])); // dummy constructor
            nemoiiBuilder.setParentsReproducer(new DoubleReproduce((p1, p2, R) -> new double[0])); /// dummy reproducer

            StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
            nemoiiBuilder.setStandardDSSBuilder(DSSB);
            nemoiiBuilder.setUseUtopiaIncumbent(false);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS =
                    new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 1000;
            DSSB.setModelConstructor(new FRS<>(pFRS));
            DSSB.setInteractionRule(new IterationInterval(1000, 1000));
            DSSB.setReferenceSetConstructor(new RandomPairs());
            DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                    new model.internals.value.scalarizing.LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY))
            ));
            DSSB.setPreferenceModel(new model.definitions.LNorm());

            LinkedList<double[]> evaluations = new LinkedList<>();
            evaluations.add(new double[]{0.5d, 2.0d});
            evaluations.add(new double[]{0.7d, 0.7d});
            evaluations.add(new double[]{2.0d, 0.5d});

            evaluations.add(new double[]{-1.0d, 0.0d});
            evaluations.add(new double[]{-0.5d, -0.5d});
            evaluations.add(new double[]{0.0d, -1.0d});

            nemoiiBuilder.setSpecimensEvaluator(new DoubleEvaluate(v -> evaluations.pop()));
            Utils.OSChangedReport osChangedReport = new Utils.OSChangedReport();
            IOSChangeListener iosChangeListener = (ea, os, prevOS) -> {
                osChangedReport._previousOS = prevOS == null ? null : prevOS.getClone();
                osChangedReport._currentOS = os == null ? null : os.getClone();
            };

            if (mode == 0)
                nemoiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
            else if (mode == 1) nemoiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, null);
            else if (mode == 2) nemoiiBuilder.setFixedOSBoundsLearningPolicy(new INormalization[]{new Linear(),
                    new Linear()}, new double[]{1.0d, 2.0d}, new double[]{5.0d, 10.0d});
            else if (mode == 3)
            {
                nemoiiBuilder.setDynamicOSBoundsLearningPolicy();
                nemoiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener
                });
            }
            else if (mode == 4)
            {
                nemoiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{new Linear(), new Linear()});
                nemoiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 5)
            {
                nemoiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nemoiiBuilder.setOSMParamsAdjuster(p -> p._listeners = new IOSChangeListener[]{
                        iosChangeListener});
            }
            else if (mode == 6)
            {
                nemoiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nemoiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 7)
            {
                nemoiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nemoiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else if (mode == 8)
            {
                nemoiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                        new Linear(), new Linear()}, new double[]{100.0d, -100.0d}, new double[]{-100.0d, 100.0d});
                nemoiiBuilder.setOSMParamsAdjuster(p -> {
                    p._updateUtopiaUsingIncumbent = true;
                    p._updateNadirUsingIncumbent = true;
                    p._listeners = new IOSChangeListener[]{
                            iosChangeListener};
                });
            }
            else //noinspection ConstantValue
                if (mode == 9)
                {
                    nemoiiBuilder.setDynamicOSBoundsLearningPolicy(new INormalization[]{
                            new Linear(), new Linear()}, null, null);
                    nemoiiBuilder.setOSMParamsAdjuster(p -> {
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
                NEMOII nemoii = nemoiiBuilder.getInstance();
                IRunner runner = new Runner(nemoii);
                runner.executeEvolution(2);

                if (mode == 0)
                {
                    assertNull(nemoii.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 1)
                {
                    assertNull(nemoii.getObjectiveSpaceManager());
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 2)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{1.0d, 2.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{5.0d, 10.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertNull(osChangedReport._previousOS);
                    assertNull(osChangedReport._currentOS);
                }
                else if (mode == 3)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 4)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 5)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 6)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{2.0d, 0.5d}, new double[]{-100.0d, 100.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{2.0d, -1.0d}, new double[]{-100.0d, 100.0d})));
                }
                else if (mode == 7)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-1.0d, 2.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    assertTrue(osChangedReport._previousOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{0.5d, 2.0d})));
                    assertTrue(osChangedReport._currentOS.isEqual(new ObjectiveSpace(new double[]{100.0d, -100.0d}, new double[]{-1.0d, 2.0d})));
                }
                else if (mode == 8)
                {
                    assertNotNull(nemoii.getObjectiveSpaceManager());
                    TestUtils.assertEquals(new double[]{100.0d, -100.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                    TestUtils.assertEquals(new double[]{-100.0d, 100.0d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
                    // no change was triggered
                    assertNull(osChangedReport._currentOS);
                    assertNull(osChangedReport._previousOS);
                }
                else //noinspection ConstantValue
                    if (mode == 9)
                    {
                        assertNotNull(nemoii.getObjectiveSpaceManager());
                        TestUtils.assertEquals(new double[]{2.0d, -1.0d}, nemoii.getObjectiveSpaceManager().getOS()._utopia, 1.0E-6);
                        TestUtils.assertEquals(new double[]{0.0d, 0.5d}, nemoii.getObjectiveSpaceManager().getOS()._nadir, 1.0E-6);
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