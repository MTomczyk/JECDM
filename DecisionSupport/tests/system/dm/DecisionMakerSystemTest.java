package system.dm;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.HistoryException;
import exeption.ModelSystemException;
import history.History;
import inconsistency.RemoveOldest;
import inconsistency.State;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.AbstractInternalModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import system.model.ModelSystem;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various (and complex) tests for {@link DecisionMakerSystem}.
 *
 * @author MTomczyk
 */
class DecisionMakerSystemTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        String msg = null;
        int toSample = 100000;

        int[] ids = new int[]{0, 1, 2};
        String[] names = new String[]{"DM0", "DM1", "DM1"}; // comparison is based on IDs

        DecisionMakerSystem[] DMS = new DecisionMakerSystem[3];
        for (int i = 0; i < 3; i++)
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = new DM(ids[i], names[i]);
            pDMS._addTimestamps = true;
            pDMS._modelSystems = new ModelSystem<?>[2];

            double[] alpha = new double[]{1.0d, Double.POSITIVE_INFINITY};

            int idx = 0;
            for (double a : alpha)
            {
                ModelSystem.Params<LNorm> pM = new ModelSystem.Params<>();
                pM._preferenceModel = new model.definitions.LNorm();

                IRandomModel<LNorm> RM = new LNormGenerator(2, a);
                FRS.Params<LNorm> pFRS = new FRS.Params<>(RM);
                pFRS._feasibleSamplesToGenerate = toSample;
                pFRS._samplingLimit = 10000000;
                pM._modelConstructor = new FRS<>(pFRS);
                pM._inconsistencyHandler = new RemoveOldest<>(true);

                try
                {
                    pDMS._modelSystems[idx++] = new ModelSystem<>(pM);
                } catch (ModelSystemException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
            }

            try
            {
                DMS[i] = new DecisionMakerSystem(pDMS);
            } catch (DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
            assertNotNull(DMS);
        }

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (i == j) assertEquals(DMS[i], DMS[j]);
                else assertNotEquals(DMS[i], DMS[j]);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        String msg = null;
        IRandom R = new MersenneTwister64(0);
        int toSample = 100000;

        DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
        pDMS._DM = new DM(0, "decision maker");
        pDMS._addTimestamps = true;
        pDMS._modelSystems = new ModelSystem<?>[2];

        double[] alpha = new double[]{1.0d, Double.POSITIVE_INFINITY};

        int idx = 0;
        for (double a : alpha)
        {
            ModelSystem.Params<LNorm> pM = new ModelSystem.Params<>();
            pM._preferenceModel = new model.definitions.LNorm();

            IRandomModel<LNorm> RM = new LNormGenerator(2, a);
            FRS.Params<LNorm> pFRS = new FRS.Params<>(RM);
            pFRS._feasibleSamplesToGenerate = toSample;
            pFRS._samplingLimit = 10000000;
            pM._modelConstructor = new FRS<>(pFRS);
            pM._inconsistencyHandler = new RemoveOldest<>(true);

            try
            {
                pDMS._modelSystems[idx++] = new ModelSystem<>(pM);
            } catch (ModelSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        DecisionMakerSystem DMS = null;
        try
        {
            DMS = new DecisionMakerSystem(pDMS);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(DMS);

        double[][] evals = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.5d, 0.5d},
                        {1.0d, 0.0d},
                        {0.75d, 0.75d}
                };

        ArrayList<Alternative> as = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) as.add(new Alternative("A" + i, evals[i]));
        AbstractAlternatives<?> alternatives = new Alternatives(as);
        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{Range.getNormalRange(), Range.getNormalRange()}, new boolean[2]);

        // PC = 1 ======================================================================================================
        // =============================================================================================================
        // =============================================================================================================

        DMContext dmContext = new DMContext(null, null, alternatives, OS, false, 0, null, R);

        // register dmcontext
        try
        {
            DMS.registerDecisionMakingContext(dmContext);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMS.notifyPreferenceElicitationBegins();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            IPreferenceInformation PI = PairwiseComparison.getPreference(alternatives.get(1), alternatives.get(0));
            DMS.notifyAboutTheMostRecentPreferenceInformation(PI);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMS.notifyPreferenceElicitationEnds();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        Report report = null;
        try
        {
            report = DMS.updateModels();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);

        assertTrue(report._processingTime > 0);

        assertEquals(2, report._reportsOnModelUpdates.size());

        {
            system.model.Report<?> rep = report._reportsOnModelUpdates.get(0);
            assertTrue(rep._processingTime > 0);
            assertFalse(rep._inconsistencyOccurred);
            assertNull(rep._reportOnInconsistencyHandling);
            assertNotNull(rep._report);
            assertFalse(rep._report._inconsistencyDetected);

            assertEquals(0.0d, rep._report._successRateInPreserving, 0.01d);
            assertEquals(0, rep._report._modelsPreservedBetweenIterations, 0.01d);
            assertEquals(0, rep._report._modelsRejectedBetweenIterations, 0.01d);
            assertEquals(1.0d / 2.0d, rep._report._successRateInConstructing, 0.01d);
            assertEquals(toSample, rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertEquals(1.0d, (double) rep._report._rejectedNewlyConstructedModels / rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertTrue(rep._report._normalizationsWereUpdated);
            assertTrue(rep._processingTime >= rep._report._constructionElapsedTime);
        }
        {
            system.model.Report<?> rep = report._reportsOnModelUpdates.get(1);
            assertTrue(rep._processingTime > 0);
            assertFalse(rep._inconsistencyOccurred);
            assertNull(rep._reportOnInconsistencyHandling);
            assertNotNull(rep._report);
            assertFalse(rep._report._inconsistencyDetected);

            assertEquals(0.0d, rep._report._successRateInPreserving, 0.01d);
            assertEquals(0, rep._report._modelsPreservedBetweenIterations, 0.01d);
            assertEquals(0, rep._report._modelsRejectedBetweenIterations, 0.01d);
            assertEquals(2.0d / 3.0d, rep._report._successRateInConstructing, 0.01d);
            assertEquals(toSample, rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertEquals(1.0d / 2.0d, (double) rep._report._rejectedNewlyConstructedModels / rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertTrue(rep._report._normalizationsWereUpdated);
            assertTrue(rep._processingTime >= rep._report._constructionElapsedTime);
        }

        // unregister dmcontext
        try
        {
            DMS.unregisterDecisionMakingContext();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        assertTrue(report._processingTime >= report._reportsOnModelUpdates.get(0)._processingTime +
                report._reportsOnModelUpdates.get(1)._processingTime);

        // top history
        History history = DMS.getHistory();
        assertEquals(1, history.getNoPreferenceExamples());
        String reference = "History for decision maker" + System.lineSeparator() +
                "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = "
                + System.lineSeparator();
        int index = reference.indexOf("date time");
        assertEquals(reference.substring(0, index), history.getFullStringRepresentation().substring(0, index));

        assertEquals(2, DMS.getModelSystems().length);
        {
            history = DMS.getModelSystems()[0].getHistory();
            assertEquals(1, history.getNoPreferenceExamples());
            reference = "History for Preference model (L-norm)" + System.lineSeparator() +
                    "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = "
                    + System.lineSeparator();
            index = reference.indexOf("date time");
            assertEquals(reference.substring(0, index), history.getFullStringRepresentation().substring(0, index));
        }
        {
            history = DMS.getModelSystems()[1].getHistory();
            assertEquals(1, history.getNoPreferenceExamples());
            reference = "History for Preference model (L-norm)" + System.lineSeparator() +
                    "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = "
                    + System.lineSeparator();
            index = reference.indexOf("date time");
            assertEquals(reference.substring(0, index), history.getFullStringRepresentation().substring(0, index));
        }

        // PC = 2 ======================================================================================================
        // =============================================================================================================
        // =============================================================================================================

        dmContext = new DMContext(null, null, alternatives, OS, false, 1, null, R);

        // register dmcontext
        try
        {
            DMS.registerDecisionMakingContext(dmContext);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMS.notifyPreferenceElicitationBegins();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            IPreferenceInformation PI = PairwiseComparison.getPreference(alternatives.get(3), alternatives.get(1));
            DMS.notifyAboutTheMostRecentPreferenceInformation(PI);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMS.notifyPreferenceElicitationEnds();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        report = null;
        try
        {
            report = DMS.updateModels();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);

        assertTrue(report._processingTime > 0);

        assertEquals(2, report._reportsOnModelUpdates.size());

        {
            system.model.Report<?> rep = report._reportsOnModelUpdates.get(0);
            assertTrue(rep._processingTime > 0);
            assertTrue(rep._inconsistencyOccurred);
            assertNotNull(rep._reportOnInconsistencyHandling);
            inconsistency.Report<?> iReport = rep._reportOnInconsistencyHandling;

            assertNotNull(iReport._consistentState);

            assertEquals(5, iReport._states.size());
            boolean[] inc = new boolean[]{true, true, false, true, false};
            String[][] pi = new String[][]{
                    {"[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ",
                            "[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = "},
                    {"[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = "},
                    {},
                    {"[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = "},
                    {"[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = "}
            };

            idx = 0;
            double auxTime = 0;
            for (State<?> st : iReport._states)
            {
                if (idx != 0) auxTime += st._report._constructionElapsedTime;
                assertEquals(idx, st._attempt);
                assertEquals(inc[idx], st._report._inconsistencyDetected);
                assertEquals(pi[idx].length, st._preferenceInformation.size());
                for (int j = 0; j < pi[idx].length; j++)
                {
                    index = pi[idx][j].indexOf("date time");
                    assertEquals(pi[idx][j].substring(0, index), st._preferenceInformation.get(j).toString().substring(0, index));
                }
                idx++;
            }


            assertNotNull(rep._report);
            assertFalse(rep._report._inconsistencyDetected);
            assertEquals(0.0d, rep._report._successRateInPreserving, 0.01d);
            assertEquals(0, rep._report._modelsPreservedBetweenIterations, 0.01d);
            assertEquals(0, rep._report._modelsRejectedBetweenIterations, 0.01d);
            assertEquals(1.0d / 2.0d, rep._report._successRateInConstructing, 0.01d);
            assertEquals(toSample, rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertEquals(1.0d, (double) rep._report._rejectedNewlyConstructedModels / rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertFalse(rep._report._normalizationsWereUpdated);
            assertTrue(iReport._processingTime >= auxTime);
            assertTrue(auxTime >= rep._report._constructionElapsedTime);
            assertTrue(rep._processingTime >= iReport._processingTime);
        }
        {
            system.model.Report<?> rep = report._reportsOnModelUpdates.get(1);
            assertTrue(rep._processingTime > 0);
            assertTrue(rep._inconsistencyOccurred);
            assertNotNull(rep._reportOnInconsistencyHandling);

            inconsistency.Report<?> iReport = rep._reportOnInconsistencyHandling;

            assertNotNull(iReport._consistentState);

            assertEquals(5, iReport._states.size());
            boolean[] inc = new boolean[]{true, true, false, true, false};
            String[][] pi = new String[][]{
                    {"[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ",
                            "[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = "},
                    {"[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = "},
                    {},
                    {"[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = "},
                    {"[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = "}
            };

            idx = 0;
            double auxTime = 0;
            for (State<?> st : iReport._states)
            {
                if (idx != 0) auxTime += st._report._constructionElapsedTime;
                assertEquals(idx, st._attempt);
                assertEquals(inc[idx], st._report._inconsistencyDetected);
                assertEquals(pi[idx].length, st._preferenceInformation.size());
                for (int j = 0; j < pi[idx].length; j++)
                {
                    index = pi[idx][j].indexOf("date time");
                    assertEquals(pi[idx][j].substring(0, index), st._preferenceInformation.get(j).toString().substring(0, index));
                }
                idx++;
            }


            assertNotNull(rep._report);
            assertFalse(rep._report._inconsistencyDetected);
            assertEquals(0.0d, rep._report._successRateInPreserving, 0.01d);
            assertEquals(0, rep._report._modelsPreservedBetweenIterations, 0.01d);
            assertEquals(0, rep._report._modelsRejectedBetweenIterations, 0.01d);
            assertEquals(2.0d / 3.0d, rep._report._successRateInConstructing, 0.01d);
            assertEquals(toSample, rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertEquals(1.0d / 2.0d, (double) rep._report._rejectedNewlyConstructedModels / rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertFalse(rep._report._normalizationsWereUpdated);
            assertTrue(iReport._processingTime >= auxTime);
            assertTrue(auxTime >= rep._report._constructionElapsedTime);
            assertTrue(rep._processingTime >= iReport._processingTime);
        }


        // unregister dmcontext
        try
        {
            DMS.unregisterDecisionMakingContext();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);


        // top history
        history = DMS.getHistory();
        assertEquals(2, history.getNoPreferenceExamples());
        try
        {
            reference = "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ";
            index = reference.indexOf("date time");
            assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(0).toString().substring(0, index));
            reference = "[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = ";
            assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(1).toString().substring(0, index));

        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        assertEquals(2, DMS.getModelSystems().length);
        {
            history = DMS.getModelSystems()[0].getHistory();
            assertEquals(1, history.getNoPreferenceExamples());

            try
            {
                reference = "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ";
                assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(0).toString().substring(0, index));
            } catch (HistoryException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

        }
        {
            history = DMS.getModelSystems()[1].getHistory();
            assertEquals(1, history.getNoPreferenceExamples());

            try
            {
                reference = "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ";
                assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(0).toString().substring(0, index));
            } catch (HistoryException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }


        // PC = 3 ======================================================================================================
        // =============================================================================================================
        // =============================================================================================================

        dmContext = new DMContext(null, null, alternatives, OS, false, 2, null, R);

        // register dmcontext
        try
        {
            DMS.registerDecisionMakingContext(dmContext);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMS.notifyPreferenceElicitationBegins();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            IPreferenceInformation PI = PairwiseComparison.getPreference(alternatives.get(1), alternatives.get(2));
            DMS.notifyAboutTheMostRecentPreferenceInformation(PI);
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            DMS.notifyPreferenceElicitationEnds();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        report = null;
        try
        {
            report = DMS.updateModels();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(report);

        assertTrue(report._processingTime > 0);

        assertEquals(2, report._reportsOnModelUpdates.size());


        {
            system.model.Report<?> rep = report._reportsOnModelUpdates.get(0);
            assertTrue(rep._processingTime > 0);
            assertTrue(rep._inconsistencyOccurred);
            assertNotNull(rep._reportOnInconsistencyHandling);
            inconsistency.Report<?> iReport = rep._reportOnInconsistencyHandling;

            assertNotNull(iReport._consistentState);


            assertEquals(2, iReport._states.size());
            boolean[] inc = new boolean[]{true, false};
            String[][] pi = new String[][]{
                    {"[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ",
                            "[Pairwise comparison: A1 PREFERENCE A2 ; id = 2 ; iteration = 2 ; date time = "},
                    {"[Pairwise comparison: A1 PREFERENCE A2 ; id = 2 ; iteration = 2 ; date time = "},
            };

            idx = 0;
            double auxTime = 0;
            for (State<?> st : iReport._states)
            {
                if (idx != 0) auxTime += st._report._constructionElapsedTime;
                assertEquals(idx, st._attempt);
                assertEquals(inc[idx], st._report._inconsistencyDetected);
                assertEquals(pi[idx].length, st._preferenceInformation.size());
                for (int j = 0; j < pi[idx].length; j++)
                {
                    index = pi[idx][j].indexOf("date time");
                    assertEquals(pi[idx][j].substring(0, index), st._preferenceInformation.get(j).toString().substring(0, index));
                }
                idx++;
            }

            assertNotNull(rep._report);
            assertFalse(rep._report._inconsistencyDetected);
            assertEquals(0.0d, rep._report._successRateInPreserving, 0.01d);
            assertEquals(0, rep._report._modelsPreservedBetweenIterations, 0.01d);
            assertEquals(0, rep._report._modelsRejectedBetweenIterations, 0.01d);
            assertEquals(1.0d / 2.0d, rep._report._successRateInConstructing, 0.01d);
            assertEquals(toSample, rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertEquals(1.0d, (double) rep._report._rejectedNewlyConstructedModels / rep._report._acceptedNewlyConstructedModels, 0.01d);
            assertFalse(rep._report._normalizationsWereUpdated);
            assertTrue(iReport._processingTime >= auxTime);
            assertTrue(auxTime >= rep._report._constructionElapsedTime);
            assertTrue(rep._processingTime >= iReport._processingTime);

            for (AbstractInternalModel model : rep._report._models)
            {
                assertEquals(1.0d, model.getAuxParam());
                double[] w = model.getWeights();
                assertTrue((Double.compare(w[0], 1.0d / 2.0d) > 0));
                assertTrue((Double.compare(w[1], 1.0d / 2.0d) < 0));
            }
        }
        {
            system.model.Report<?> rep = report._reportsOnModelUpdates.get(1);
            assertTrue(rep._processingTime > 0);
            assertFalse(rep._inconsistencyOccurred);
            assertNull(rep._reportOnInconsistencyHandling);
            assertNotNull(rep._report);
            assertFalse(rep._report._inconsistencyDetected);
            assertEquals(0.5d, rep._report._successRateInPreserving, 0.01d);
            assertEquals(0.5d, (double) rep._report._modelsPreservedBetweenIterations / toSample, 0.01d);
            assertEquals(0.5d, (double) rep._report._modelsRejectedBetweenIterations / toSample, 0.01d);
            assertEquals(1.0d / 3.0d, rep._report._successRateInConstructing, 0.01d);
            assertEquals(0.5d, (double) rep._report._acceptedNewlyConstructedModels / toSample, 0.01d);
            assertEquals(0.5d, (double) rep._report._acceptedNewlyConstructedModels / rep._report._rejectedNewlyConstructedModels, 0.01d);

            assertFalse(rep._report._normalizationsWereUpdated);

            for (AbstractInternalModel model : rep._report._models)
            {
                assertEquals(Double.POSITIVE_INFINITY, model.getAuxParam());
                double[] w = model.getWeights();
                assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
            }
        }

        // unregister dmcontext
        try
        {
            DMS.unregisterDecisionMakingContext();
        } catch (DecisionMakerSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        // top history
        history = DMS.getHistory();

        assertEquals(3, history.getNoPreferenceExamples());

        try
        {
            reference = "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ";
            index = reference.indexOf("date time");
            assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(0).toString().substring(0, index));
            reference = "[Pairwise comparison: A3 PREFERENCE A1 ; id = 1 ; iteration = 1 ; date time = ";
            assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(1).toString().substring(0, index));
            reference = "[Pairwise comparison: A1 PREFERENCE A2 ; id = 2 ; iteration = 2 ; date time = ";
            assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(2).toString().substring(0, index));


        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        assertEquals(2, DMS.getModelSystems().length);
        {
            history = DMS.getModelSystems()[0].getHistory();
            assertEquals(1, history.getNoPreferenceExamples());

            try
            {
                reference = "[Pairwise comparison: A1 PREFERENCE A2 ; id = 2 ; iteration = 2 ; date time = ";
                assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(0).toString().substring(0, index));

            } catch (HistoryException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

        }
        {
            history = DMS.getModelSystems()[1].getHistory();
            assertEquals(2, history.getNoPreferenceExamples());

            try
            {
                reference = "[Pairwise comparison: A1 PREFERENCE A0 ; id = 0 ; iteration = 0 ; date time = ";
                assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(0).toString().substring(0, index));
                reference = "[Pairwise comparison: A1 PREFERENCE A2 ; id = 2 ; iteration = 2 ; date time = ";
                assertEquals(reference.substring(0, index), history.getPreferenceInformationCopy().get(1).toString().substring(0, index));

            } catch (HistoryException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }


}