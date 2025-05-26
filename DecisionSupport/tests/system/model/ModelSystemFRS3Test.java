package system.model;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import dmcontext.DMContext;
import exeption.HistoryException;
import exeption.ModelSystemException;
import history.History;
import history.PreferenceInformationWrapper;
import inconsistency.IInconsistencyHandler;
import inconsistency.RemoveOldest;
import inconsistency.State;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister32;
import random.MersenneTwister64;
import random.XoRoShiRo128PP;
import space.Range;
import space.os.ObjectiveSpace;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various (complex) tests for {@link ModelSystem} (uses FRS {@link FRS}).
 *
 * @author MTomczyk
 */
class ModelSystemFRS3Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();
        IRandomModel<LNorm> randomModel = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        IRandom R = new XoRoShiRo128PP(0);
        FRS.Params<LNorm> pFRS = new FRS.Params<>(randomModel);
        pFRS._samplingLimit = 10000000;
        int toSample = 100000;
        pFRS._feasibleSamplesToGenerate = toSample;
        IConstructor<LNorm> modelConstructor = new FRS<>(pFRS);
        IInconsistencyHandler<LNorm> inconsistencyHandler = new RemoveOldest<>(true);

        ModelSystem.Params<LNorm> pModelSystem = new ModelSystem.Params<>();
        pModelSystem._preferenceModel = preferenceModel;
        pModelSystem._modelConstructor = modelConstructor;
        pModelSystem._inconsistencyHandler = inconsistencyHandler;

        String msg = null;
        ModelSystem<LNorm> modelSystem = null;
        try
        {
            modelSystem = new ModelSystem<>(pModelSystem);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(modelSystem);

        double[][] evals = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.25d, 0.75d},
                        {0.5d, 0.5d},
                        {0.75d, 0.25d},
                        {1.0d, 0.0d},
                        {0.75d, 0.75d}
                };

        ArrayList<Alternative> as = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) as.add(new Alternative("A" + i, evals[i]));
        AbstractAlternatives<?> alternatives = new Alternatives(as);
        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{Range.getNormalRange(), Range.getNormalRange()}, new boolean[2]);
        DMContext dmContext = new DMContext(null, null, alternatives, OS, false, 0, null, R);

        // PC = 1 ======================================================================================================
        // =============================================================================================================
        // =============================================================================================================

        // register dmContext
        try
        {
            modelSystem.registerDecisionMakingContext(dmContext);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        History history = new History("H");
        LinkedList<PreferenceInformationWrapper> wrapped = null;

        try
        {
            wrapped = history.registerPreferenceInformation(PairwiseComparison.getPreference(alternatives.get(2), alternatives.get(0)), dmContext.getCurrentIteration(), true);
        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyPreferenceElicitationBegins();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(wrapped);

        try
        {
            modelSystem.notifyAboutMostRecentPreferenceInformation(wrapped);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyPreferenceElicitationEnds();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        system.model.Report<LNorm> report = null;
        try
        {
            report = modelSystem.updateModel();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(report);
        assertNotNull(report._report);
        assertNull(report._reportOnInconsistencyHandling);
        assertFalse(report._report._inconsistencyDetected);
        assertEquals(toSample, report._report._models.size());
        assertEquals(0.0d, report._report._successRateInPreserving, 1.0E-2);
        assertEquals(0, report._report._modelsPreservedBetweenIterations, 1.0E-201d);
        assertEquals(0, report._report._modelsRejectedBetweenIterations, 1.0E-2);

        assertEquals(2.0d / 3.0d, report._report._successRateInConstructing, 1.0E-2);
        assertEquals(toSample, report._report._acceptedNewlyConstructedModels, 1.0E-2);
        assertEquals(1.0d / 2.0d, (double) report._report._rejectedNewlyConstructedModels / report._report._acceptedNewlyConstructedModels, 1.0E-2);

        assertTrue(report._report._constructionElapsedTime > 0);
        assertTrue(report._report._normalizationsWereUpdated);

        // PC = 2 ======================================================================================================
        // =============================================================================================================
        // =============================================================================================================

        // register dmContext
        try
        {
            modelSystem.registerDecisionMakingContext(dmContext);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            wrapped = history.registerPreferenceInformation(PairwiseComparison.getPreference(alternatives.get(2), alternatives.get(4)), dmContext.getCurrentIteration(), true);
        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyPreferenceElicitationBegins();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyAboutMostRecentPreferenceInformation(wrapped);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyPreferenceElicitationEnds();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        report = null;
        try
        {
            report = modelSystem.updateModel();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(report);
        assertNotNull(report._report);
        assertNull(report._reportOnInconsistencyHandling);
        assertFalse(report._report._inconsistencyDetected);
        assertEquals(toSample, report._report._models.size());
        assertEquals(0.5d, report._report._successRateInPreserving, 1.0E-2);
        assertEquals(0.5d, (double) report._report._modelsPreservedBetweenIterations / toSample, 1.0E-2);
        assertEquals(0.5d, (double) report._report._modelsRejectedBetweenIterations / toSample, 1.0E-2);

        assertEquals(1.0d / 3.0d, report._report._successRateInConstructing, 1.0E-2);
        assertEquals(0.5d, (double) report._report._acceptedNewlyConstructedModels / toSample, 1.0E-2);
        assertEquals(0.5d, (double) report._report._acceptedNewlyConstructedModels / report._report._rejectedNewlyConstructedModels, 1.0E-2);

        for (LNorm lNorm : report._report._models)
        {
            assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
            assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

            double[] w = lNorm.getWeights();
            assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
            assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));

            w = lNorm.getLNorm().getWeights();
            assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
            assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
        }

        assertTrue(report._report._constructionElapsedTime > 0);
        assertFalse(report._report._normalizationsWereUpdated);

        // PC = 3 ======================================================================================================
        // =============================================================================================================
        // =============================================================================================================

        // register dmContext
        try
        {
            modelSystem.registerDecisionMakingContext(dmContext);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            wrapped = history.registerPreferenceInformation(PairwiseComparison.getPreference(alternatives.get(5), alternatives.get(2)), dmContext.getCurrentIteration(), true);
        } catch (HistoryException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyPreferenceElicitationBegins();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyAboutMostRecentPreferenceInformation(wrapped);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        try
        {
            modelSystem.notifyPreferenceElicitationEnds();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        report = null;
        try
        {
            report = modelSystem.updateModel();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }

        assertNull(msg);
        assertNotNull(report);
        assertNotNull(report._report);
        assertNotNull(report._reportOnInconsistencyHandling);

        for (State<LNorm> s : report._reportOnInconsistencyHandling._states)
            System.out.println(s);

        assertEquals(7, report._reportOnInconsistencyHandling._states.size());

        boolean[] inconsistency = new boolean[]{true, true, true, false, true, false, false};
        String[][] PIs = new String[][]{
                {"Pairwise comparison: A2 PREFERENCE A0", "Pairwise comparison: A2 PREFERENCE A4", "Pairwise comparison: A5 PREFERENCE A2"},
                {"Pairwise comparison: A2 PREFERENCE A4", "Pairwise comparison: A5 PREFERENCE A2"},
                {"Pairwise comparison: A5 PREFERENCE A2"},
                {},
                {"Pairwise comparison: A5 PREFERENCE A2"},
                {"Pairwise comparison: A2 PREFERENCE A4"},
                {"Pairwise comparison: A2 PREFERENCE A0", "Pairwise comparison: A2 PREFERENCE A4"}
        };

        int idx = 0;
        for (State<LNorm> state : report._reportOnInconsistencyHandling._states)
        {
            assertEquals(idx, state._attempt);
            assertEquals(inconsistency[idx], state._report._inconsistencyDetected);
            assertEquals(PIs[idx].length, state._preferenceInformation.size());
            for (int j = 0; j < PIs[idx].length; j++)
                assertEquals(PIs[idx][j], state._preferenceInformation.get(j)._preferenceInformation.toString());
            idx++;
        }

        {
            Report<LNorm> m = report._reportOnInconsistencyHandling._states.get(5)._report;
            assertFalse(m._inconsistencyDetected);
            assertNotNull(m._models);
            assertEquals(toSample, m._models.size());
            assertTrue(m._constructionElapsedTime > 0);
            assertEquals(0.0d, m._successRateInPreserving, 1.0E-6);
            assertEquals(0, m._modelsPreservedBetweenIterations);
            assertEquals(0, m._modelsRejectedBetweenIterations);
            assertEquals(2.0d / 3.0d, m._successRateInConstructing, 1.0E-2);
            assertEquals(toSample, m._acceptedNewlyConstructedModels);
            assertEquals(2.0d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-2);
            assertFalse(m._normalizationsWereUpdated);

            for (LNorm lNorm : m._models)
            {
                assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                double[] w = lNorm.getWeights();
                assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0));
                assertTrue((Double.compare(w[1], 2.0d / 3.0d) < 0));

                w = lNorm.getLNorm().getWeights();
                assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0));
                assertTrue((Double.compare(w[1], 2.0d / 3.0d) < 0));
            }
        }

        {
            Report<LNorm> m = report._reportOnInconsistencyHandling._states.get(6)._report;
            assertFalse(m._inconsistencyDetected);
            assertNotNull(m._models);
            assertEquals(toSample, m._models.size());
            assertTrue(m._constructionElapsedTime > 0);

            assertEquals(0.5d, m._successRateInPreserving, 1.0E-2);
            assertEquals(0.5d, (double) m._modelsPreservedBetweenIterations / toSample, 1.0E-2);
            assertEquals(0.5d, (double) m._modelsRejectedBetweenIterations / toSample, 1.0E-2);

            assertEquals(1.0d / 3.0d, m._successRateInConstructing, 1.0E-2);
            assertEquals(0.5d, (double) m._acceptedNewlyConstructedModels / toSample, 1.0E-2);
            assertEquals(0.5d, (double) m._acceptedNewlyConstructedModels / m._rejectedNewlyConstructedModels, 1.0E-2);

            assertFalse(m._normalizationsWereUpdated);

            for (LNorm lNorm : m._models)
            {
                assertEquals(Double.POSITIVE_INFINITY, lNorm.getLNorm().getAuxParam());
                assertEquals(Double.POSITIVE_INFINITY, lNorm.getAuxParam());

                double[] w = lNorm.getWeights();
                assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));

                w = lNorm.getLNorm().getWeights();
                assertTrue((Double.compare(w[0], 1.0d / 3.0d) > 0) && (Double.compare(w[0], 2.0d / 3.0d) < 0));
                assertTrue((Double.compare(w[1], 1.0d / 3.0d) > 0) && (Double.compare(w[1], 2.0d / 3.0d) < 0));
            }
        }

        assertFalse(report._report._inconsistencyDetected);
        assertEquals(toSample, report._report._models.size());
    }

}