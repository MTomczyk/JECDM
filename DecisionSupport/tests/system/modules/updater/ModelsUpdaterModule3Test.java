package system.modules.updater;


import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.ModelSystemException;
import exeption.ModuleException;
import inconsistency.RemoveOldest;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.definitions.LNorm;
import model.internals.AbstractInternalModel;
import org.junit.jupiter.api.Test;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.model.ModelSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various (more complex) tests for {@link ModelsUpdaterModule}.
 *
 * @author MTomczyk
 */
class ModelsUpdaterModule3Test
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");
        String msg = null;
        IRandom R = new MersenneTwister64(0);

        pP._DMSs = new DecisionMakerSystem[2];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>(true);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._feasibleSamplesToGenerate = 10000;
            pMS._modelConstructor = new FRS<>(pFRS);
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[1];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>(true);
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY));
            pFRS._feasibleSamplesToGenerate = 10000;
            pMS._modelConstructor = new FRS<>(pFRS);
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[1] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        pP._DMs = dms;
        ModelsUpdaterModule MUM = new ModelsUpdaterModule(pP);

        try
        {
            MUM.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);


        // ============================================================================================================
        Criteria C = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 1.0d), new boolean[2]);
        DMContext dmContext = new DMContext(C, LocalDateTime.now(), new Alternatives(new ArrayList<>()),
                os, false, 0, null, R);

        IPreferenceInformation[] pi = new IPreferenceInformation[]
                {
                        PairwiseComparison.getPreference(new Alternative("A0", new double[]{1.0d, 1.0d}),
                                new Alternative("A1", new double[]{0.0d, 0.0d})),
                        PairwiseComparison.getPreference(new Alternative("A2", new double[]{1.0d, 1.0d}),
                                new Alternative("A3", new double[]{0.0d, 0.0d}))
                };

        for (int i = 0; i < 2; i++)
        {
            try
            {
                pP._DMSs[i].registerDecisionMakingContext(dmContext);
                pP._DMSs[i].notifyPreferenceElicitationBegins();
                LinkedList<IPreferenceInformation> lp = new LinkedList<>();
                lp.add(pi[i]);
                pP._DMSs[i].notifyAboutTheMostRecentPreferenceInformation(lp);
                pP._DMSs[i].notifyPreferenceElicitationEnds();
                pP._DMSs[i].unregisterDecisionMakingContext();
            } catch (DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        Report report = null;
        try
        {
            report = MUM.executeProcess(dmContext);
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);

        assertEquals(0, report._iteration);
        assertTrue(report._processingTime >= 0);
        assertTrue(report._reportCreationDateTime.isAfter(report._dmContextDateTime) || report._reportCreationDateTime.isEqual(report._dmContextDateTime));
        assertNotNull(report._DMs);
        assertEquals(2, report._DMs.length);
        assertEquals(dms[0], report._DMs[0]);
        assertEquals(dms[1], report._DMs[1]);
        assertNotNull(report._modelsUpdatesReports);
        assertEquals(2, report._modelsUpdatesReports.size());
        assertTrue(report._modelsUpdatesReports.containsKey(dms[0]));
        assertTrue(report._modelsUpdatesReports.containsKey(dms[1]));

        for (int i = 0; i < 2; i++)
        {
            system.dm.Report dmR = report._modelsUpdatesReports.get(dms[i]);
            assertEquals(0, dmR._iteration);
            assertTrue(dmR._processingTime >= 0);
            assertTrue(dmR._reportCreationDateTime.isAfter(report._dmContextDateTime) || report._reportCreationDateTime.isEqual(report._dmContextDateTime));
            assertNotNull(dmR._reportsOnModelUpdates);
            assertEquals(1, dmR._reportsOnModelUpdates.size());

            {
                system.model.Report<?> mR = dmR._reportsOnModelUpdates.getFirst();
                assertEquals(0, mR._iteration);
                assertTrue(mR._processingTime >= 0);
                assertTrue(mR._reportCreationDateTime.isAfter(report._dmContextDateTime) || report._reportCreationDateTime.isEqual(report._dmContextDateTime));
                assertTrue(mR._inconsistencyOccurred);
                assertNotNull(mR._reportOnInconsistencyHandling);
                assertEquals(1, mR._reportOnInconsistencyHandling._attempts);
                assertNotNull(mR._reportOnInconsistencyHandling._states);
                assertEquals(2, mR._reportOnInconsistencyHandling._states.size());

                {
                    assertTrue(mR._reportOnInconsistencyHandling._states.getFirst()._report._inconsistencyDetected);
                    assertEquals(0, mR._reportOnInconsistencyHandling._states.getFirst()._attempt);
                    assertEquals(0, mR._reportOnInconsistencyHandling._states.getFirst()._report._acceptedNewlyConstructedModels);
                    assertEquals(1000000, mR._reportOnInconsistencyHandling._states.getFirst()._report._rejectedNewlyConstructedModels);
                    assertEquals(0.0d, mR._reportOnInconsistencyHandling._states.getFirst()._report._successRateInConstructing, 0.0001d);
                    assertEquals(1, mR._reportOnInconsistencyHandling._states.getFirst()._preferenceInformation.size());
                    assertInstanceOf(PairwiseComparison.class, mR._reportOnInconsistencyHandling._states.getFirst()._preferenceInformation.getFirst()._preferenceInformation);
                    PairwiseComparison PC = (PairwiseComparison) mR._reportOnInconsistencyHandling._states.getFirst()._preferenceInformation.getFirst()._preferenceInformation;
                    if (i == 0)
                    {
                        assertEquals("A0", PC.getPreferredAlternative().getName());
                        assertEquals("A1", PC.getNotPreferredAlternative().getName());
                    }
                    else
                    {
                        assertEquals("A2", PC.getPreferredAlternative().getName());
                        assertEquals("A3", PC.getNotPreferredAlternative().getName());
                    }
                }
                {
                    assertFalse(mR._reportOnInconsistencyHandling._states.getLast()._report._inconsistencyDetected);
                    assertEquals(1, mR._reportOnInconsistencyHandling._states.getLast()._attempt);
                    assertEquals(10000, mR._reportOnInconsistencyHandling._states.getLast()._report._acceptedNewlyConstructedModels);
                    assertEquals(0, mR._reportOnInconsistencyHandling._states.getLast()._report._rejectedNewlyConstructedModels);
                    assertEquals(1.0d, mR._reportOnInconsistencyHandling._states.getLast()._report._successRateInConstructing, 0.0001d);
                    assertEquals(0, mR._reportOnInconsistencyHandling._states.getLast()._preferenceInformation.size());
                }

                assertEquals(mR._reportOnInconsistencyHandling._consistentState._report, mR._report);

                for (AbstractInternalModel m : mR._report._models)
                {
                    double[] w = m.getWeights();
                    assertTrue(Double.compare(w[0], 1.0d) <= 0);
                    assertTrue(Double.compare(w[0], 0.0d) >= 0);
                    assertTrue(Double.compare(w[1], 1.0d) <= 0);
                    assertTrue(Double.compare(w[1], 0.0d) >= 0);
                    assertEquals(Double.POSITIVE_INFINITY, m.getAuxParam());
                }
            }
        }
        report.printStringRepresentation();

        // ============================================================================================================
        dmContext = new DMContext(C, LocalDateTime.now(), new Alternatives(new ArrayList<>()), os, false, 1, null, R);

        pi = new IPreferenceInformation[]
                {
                        PairwiseComparison.getPreference(new Alternative("A0", new double[]{1.0d, 0.0d}),
                                new Alternative("A1", new double[]{0.0d, 1.0d})),
                        PairwiseComparison.getPreference(new Alternative("A2", new double[]{0.0d, 1.0d}),
                                new Alternative("A3", new double[]{1.0d, 0.0d}))
                };

        for (int i = 0; i < 2; i++)
        {
            try
            {
                pP._DMSs[i].registerDecisionMakingContext(dmContext);
                pP._DMSs[i].notifyPreferenceElicitationBegins();
                LinkedList<IPreferenceInformation> lp = new LinkedList<>();
                lp.add(pi[i]);
                pP._DMSs[i].notifyAboutTheMostRecentPreferenceInformation(lp);
                pP._DMSs[i].notifyPreferenceElicitationEnds();
                pP._DMSs[i].unregisterDecisionMakingContext();
            } catch (DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        report = null;
        try
        {
            report = MUM.executeProcess(dmContext);
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);

        assertEquals(1, report._iteration);
        assertTrue(report._processingTime >= 0);
        assertTrue(report._reportCreationDateTime.isAfter(report._dmContextDateTime) || report._reportCreationDateTime.isEqual(report._dmContextDateTime));
        assertNotNull(report._DMs);
        assertEquals(2, report._DMs.length);
        assertEquals(dms[0], report._DMs[0]);
        assertEquals(dms[1], report._DMs[1]);
        assertNotNull(report._modelsUpdatesReports);
        assertEquals(2, report._modelsUpdatesReports.size());
        assertTrue(report._modelsUpdatesReports.containsKey(dms[0]));
        assertTrue(report._modelsUpdatesReports.containsKey(dms[1]));

        for (int i = 0; i < 2; i++)
        {
            system.dm.Report dmR = report._modelsUpdatesReports.get(dms[i]);
            assertEquals(1, dmR._iteration);
            assertTrue(dmR._processingTime >= 0);
            assertTrue(dmR._reportCreationDateTime.isAfter(report._dmContextDateTime) || report._reportCreationDateTime.isEqual(report._dmContextDateTime));
            assertNotNull(dmR._reportsOnModelUpdates);
            assertEquals(1, dmR._reportsOnModelUpdates.size());

            {
                system.model.Report<?> mR = dmR._reportsOnModelUpdates.getFirst();
                assertEquals(1, mR._iteration);
                assertTrue(mR._processingTime >= 0);
                assertTrue(mR._reportCreationDateTime.isAfter(report._dmContextDateTime) || report._reportCreationDateTime.isEqual(report._dmContextDateTime));
                assertFalse(mR._inconsistencyOccurred);
                assertNull(mR._reportOnInconsistencyHandling);

                assertNotNull(mR._report);
                assertFalse(mR._report._inconsistencyDetected);
                assertTrue(Double.compare(Math.abs(mR._report._successRateInConstructing - 0.5d), 0.1d) <= 0);
                assertEquals(10000, mR._report._models.size());
                assertTrue(Double.compare(Math.abs(mR._report._successRateInPreserving - 0.5d), 0.1d) <= 0);

                for (AbstractInternalModel m : mR._report._models)
                {
                    double[] w = m.getWeights();
                    if (i == 0)
                    {
                        assertTrue(Double.compare(w[0], 0.5d) <= 0);
                        assertTrue(Double.compare(w[1], 0.5d) >= 0);
                    }
                    else
                    {
                        assertTrue(Double.compare(w[0], 0.5d) >= 0);
                        assertTrue(Double.compare(w[1], 0.5d) <= 0);
                    }
                }

            }
        }

        System.out.println("=========================================================================================");
        report.printStringRepresentation();
    }
}