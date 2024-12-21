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
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
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
class ModelSystemFRS1Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        ModelSystem.Params<LNorm> pModelSystem = new ModelSystem.Params<>();

        String msg = null;
        try
        {
            new ModelSystem<>(pModelSystem);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The preference model is not provided", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();
        ModelSystem.Params<LNorm> pModelSystem = new ModelSystem.Params<>();
        pModelSystem._preferenceModel = preferenceModel;

        String msg = null;
        try
        {
            new ModelSystem<>(pModelSystem);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The model constructor is not provided", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();
        IRandomModel<LNorm> randomModel = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        FRS.Params<LNorm> pFRS = new FRS.Params<>(randomModel);
        IConstructor<LNorm> modelConstructor = new FRS<>(pFRS);
        ModelSystem.Params<LNorm> pModelSystem = new ModelSystem.Params<>();
        pModelSystem._preferenceModel = preferenceModel;
        pModelSystem._modelConstructor = modelConstructor;

        String msg = null;
        try
        {
            new ModelSystem<>(pModelSystem);
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }

        assertEquals("The inconsistency handler is not provided", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();
        IRandomModel<LNorm> randomModel = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        IRandom R = new MersenneTwister64(0);
        FRS.Params<LNorm> pFRS = new FRS.Params<>(randomModel);
        pFRS._samplingLimit = 10000000;
        int toSample = 100000;
        pFRS._feasibleSamplesToGenerate = toSample;
        IConstructor<LNorm> modelConstructor = new FRS<>(pFRS);
        IInconsistencyHandler<LNorm> inconsistencyHandler = new RemoveOldest<>();

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
                        {1.0d, 0.0d}
                };

        ArrayList<Alternative> as = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) as.add(new Alternative("A" + i, evals[i]));
        AbstractAlternatives<?> alternatives = new Alternatives(as);
        ObjectiveSpace OS = new ObjectiveSpace(new Range[]{Range.getNormalRange(), Range.getNormalRange()}, new boolean[2]);
        DMContext dmContext = new DMContext(null, null, alternatives, OS, false, 0, null, R);


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

        Report<LNorm> report = null;
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
        assertFalse(report._inconsistencyOccurred);
        assertTrue(report._processingTime > 0);
        assertFalse(report._report._inconsistencyDetected);
        assertEquals(toSample, report._report._models.size());
        assertEquals(0, report._report._modelsRejectedBetweenIterations);
        assertEquals(0, report._report._modelsRejectedBetweenIterations);
        assertEquals(0.0d, report._report._successRateInPreserving, 0.000001d);
        assertEquals(toSample, report._report._acceptedNewlyConstructedModels);
        assertEquals(0.5d, (double) report._report._rejectedNewlyConstructedModels /
                report._report._acceptedNewlyConstructedModels, 0.1d);
        assertEquals(2.0d / 3.0d, report._report._successRateInConstructing, 0.1d);
        assertTrue(report._report._constructionElapsedTime > 0);
        assertTrue(report._report._normalizationsWereUpdated);


        dmContext = new DMContext(null, null, alternatives, OS, false, 1, null, R);

        // register decision making context
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
        assertFalse(report._inconsistencyOccurred);
        assertTrue(report._processingTime > 0);
        assertFalse(report._report._inconsistencyDetected);
        assertEquals(toSample, report._report._models.size());
        assertEquals(0.5d, report._report._successRateInPreserving, 0.01d);
        assertEquals(0.5d, (double) report._report._modelsPreservedBetweenIterations / toSample, 0.01d);
        assertEquals(0.5d, (double) report._report._modelsRejectedBetweenIterations / toSample, 0.01d);

        assertEquals(1.0d / 3.0d, report._report._successRateInConstructing, 0.01d);
        assertEquals(0.5d, (double) report._report._acceptedNewlyConstructedModels / toSample, 0.01d);
        assertEquals(0.5d, (double) report._report._acceptedNewlyConstructedModels /
                report._report._rejectedNewlyConstructedModels, 0.01d);

        assertTrue(report._report._constructionElapsedTime > 0);
        assertFalse(report._report._normalizationsWereUpdated);


        // change normalizations
        ObjectiveSpace newOS = new ObjectiveSpace(new Range[]{new Range(0.0d, 2.0d),
                new Range(0.0d, 2.0d)}, new boolean[2]);
        dmContext = new DMContext(null, null, alternatives, newOS, true, 2, null, R);

        try
        {
            modelSystem.registerDecisionMakingContext(dmContext);
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
        assertFalse(report._inconsistencyOccurred);
        assertTrue(report._processingTime > 0);
        assertFalse(report._report._inconsistencyDetected);
        assertEquals(toSample, report._report._models.size());
        assertEquals(1.0d, report._report._successRateInPreserving, 0.01d);
        assertEquals(toSample, report._report._modelsPreservedBetweenIterations, 0.01d);
        assertEquals(0, report._report._modelsRejectedBetweenIterations, 0.01d);

        assertEquals(0.0d, report._report._successRateInConstructing, 0.01d);
        assertEquals(0, report._report._acceptedNewlyConstructedModels, 0.01d);
        assertEquals(0, report._report._rejectedNewlyConstructedModels, 0.01d);

        assertTrue(report._report._constructionElapsedTime > 0);
        assertTrue(report._report._normalizationsWereUpdated);

        // unregister decision making context
        try
        {
            modelSystem.unregisterDecisionMakingContext();
        } catch (ModelSystemException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

}