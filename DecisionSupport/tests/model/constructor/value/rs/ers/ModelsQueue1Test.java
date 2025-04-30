package model.constructor.value.rs.ers;

import alternative.Alternative;
import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import exeption.FeedbackProviderException;
import exeption.ReferenceSetsConstructorException;
import history.PreferenceInformationWrapper;
import interaction.feedbackprovider.dm.DMResult;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSets;
import model.constructor.value.rs.ers.comparators.KNearest;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ModelsQueue}
 *
 * @author MTomczyk
 */
class ModelsQueue1Test
{
    /**
     * Test 1.
     */
    @Test
    void initializeWithBatch1()
    {
        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
        int Ws = 11;
        double[][] W = new double[11][2];
        for (int i = 0; i < Ws; i++)
        {
            W[i][0] = (double) i / (Ws - 1);
            W[i][1] = 1.0d - W[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>();
        for (double[] w : W) models.add(new LNorm(w, Double.POSITIVE_INFINITY));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.5d, 0.5d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.5d, 0.5d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        double[] expectedCD = new double[]
                {-0.5, -0.35, -0.2, -0.05, 0.1, 0.25, 0.1, -0.05, -0.2, -0.35, -0.5};

        for (int i = 0; i < models.size(); i++)
        {
            double cd = CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(feedback, models.get(i));
            assertEquals(expectedCD[i], cd, 1.0E-5);
        }

        String msg = null;

        try
        { // can be different for Euclidean and Cos
            ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(Ws, 1, CA, new KNearest<>(0), new Euclidean());
            modelsQueue.initializeWithBatch(models, feedback);
            modelsQueue.updateClosestModelsAndSortQueue(false, false);
            assertNull(modelsQueue.isValid());
            LinkedList<SortedModel<LNorm>> queue = modelsQueue.getQueue();

            assertFalse(modelsQueue.areAllSortedModelsCompatible());

            boolean[] expCompatible = new boolean[]{true, true, true, false, false, false, false, false, false, false, false};
            int[][] expIds = new int[][]{{4, 5, 6}, {4, 5, 6}, {4, 5, 6}, {3, 7}, {3, 7}, {2, 8}, {2, 8}, {1, 9}, {1, 9}, {0, 10}, {0, 10}};
            int[][] expIdsNb = new int[][]{{4, 5, 6}, {4, 5, 6}, {4, 5, 6}, null, null, null, null, null, null, null, null};

            assertEquals(Ws, queue.size());
            int i = -1;
            for (SortedModel<LNorm> sm : queue)
            {
                i++;
                assertEquals(expCompatible[i], sm._isCompatible);
                HashSet<Integer> expIDsSet = new HashSet<>();
                for (int j : expIds[i]) expIDsSet.add(j);
                assertTrue(expIDsSet.contains(sm._id));

                if (expIdsNb[i] == null) assertEquals(0, sm._closestModels.getNoStoredModels());
                else
                {
                    HashSet<Integer> expIDsNBsSet = new HashSet<>();
                    assertNotNull(expIdsNb[i]);
                    assert expIdsNb[i] != null;
                    //noinspection DataFlowIssue
                    for (int j : expIdsNb[i]) expIDsNBsSet.add(j);
                    assertTrue(expIDsNBsSet.contains(sm._closestModels._ids[0]));
                    assertEquals(Math.sqrt(2.0d) / 10.0d, sm._closestModels._similarities[0], 1.0E-5);
                }

                assertEquals(expectedCD[sm._id], sm._compatibilityDegree, 1.0E-5);
            }

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

    /**
     * Test 2.
     */
    @Test
    void initializeWithBatch2()
    {
        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
        double[][] W = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.098d, 0.902d},
                        {0.21d, 0.79d},
                        {0.304d, 0.696d},
                        {0.397d, 0.603d},
                        {0.52d, 0.48d},
                        {0.5999d, 0.4001d},
                        {0.731d, 0.269d},
                        {0.7931d, 0.2069d},
                        {0.901d, 0.099d},
                        {1.0d, 0.0d}
                };

        ArrayList<LNorm> models = new ArrayList<>();
        for (double[] w : W) models.add(new LNorm(w, Double.POSITIVE_INFINITY));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.25d, 0.75d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.75d, 0.25d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        double[] expectedCD = new double[]
                {-0.25, -0.1275, 0.0125, 0.076, 0.09925, 0.12, 0.100025, 0.06725, 0.008625, -0.12625, -0.25};

        for (int i = 0; i < models.size(); i++)
        {
            double cd = CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(feedback, models.get(i));
            assertEquals(expectedCD[i], cd, 1.0E-5);
        }

        String msg = null;
        try
        { // can be different for Euclidean and Cos
            ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(W.length, 1, CA, new KNearest<>(0), new Euclidean());
            modelsQueue.initializeWithBatch(models, feedback);
            modelsQueue.updateClosestModelsAndSortQueue(false, false);
            assertNull(modelsQueue.isValid());
            LinkedList<SortedModel<LNorm>> queue = modelsQueue.getQueue();

            int i = -1;
            double[] expSimilarity = new double[]
                    {0.132936074863071, 0.13152186130069785, 0.13152186130069785, 0.11299566363361026, 0.11299566363361026,
                            0.08782266222336925, 0.08782266222336925, -1.0d, -1.0d, -1.0d, -1.0d,};

            boolean[] expCompatible = new boolean[]{true, true, true, true, true, true, true, false, false, false, false};
            int[][] expIds = new int[][]{{2}, {4, 3}, {4, 3}, {5, 6}, {5, 6}, {8, 7}, {8, 7}, {9}, {1}, {0, 10}, {0, 10}};
            int[][] expIdsNb = new int[][]{{3}, {3, 4}, {3, 4}, {5, 6}, {5, 6}, {7, 8}, {7, 8}, null, null, null, null};


            for (SortedModel<LNorm> sm : queue)
            {
                i++;
                assertEquals(expCompatible[i], sm._isCompatible);
                HashSet<Integer> expIDsSet = new HashSet<>();
                for (int j : expIds[i]) expIDsSet.add(j);
                assertTrue(expIDsSet.contains(sm._id));

                if (expIdsNb[i] == null) assertEquals(0, sm._closestModels.getNoStoredModels());
                else
                {
                    assertEquals(expSimilarity[i], sm._closestModels._similarities[0], 1.0E-5);
                    HashSet<Integer> expIDsNBsSet = new HashSet<>();
                    assertNotNull(expIdsNb[i]);
                    assert expIdsNb[i] != null;
                    //noinspection DataFlowIssue
                    for (int j : expIdsNb[i]) expIDsNBsSet.add(j);
                    assertTrue(expIDsNBsSet.contains(sm._closestModels._ids[0]));
                }
            }

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }


    /**
     * Test 3.
     */
    @Test
    void initializeWithBatch3()
    {
        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(1, 1, null, new KNearest<>(0), new Euclidean());
        String msg = null;
        try
        {
            modelsQueue.initializeWithBatch(null, null);
        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertEquals("Exception handled by: model.constructor.value.rs.ers.ModelsQueue, caused by: null, happened in line: null, message: The compatibility analyzer is null", msg);
    }

    /**
     * Test 4.
     */
    @Test
    void initializeWithBatch4()
    {
        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(1, 1, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        String msg = null;
        try
        {
            modelsQueue.initializeWithBatch(null, null);
        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertEquals("Exception handled by: model.constructor.value.rs.ers.ModelsQueue, caused by: null, happened in line: null, message: The input model array is null", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void initializeWithBatch5()
    {
        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(1, 1, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(null);

        String msg = null;
        try
        {
            modelsQueue.initializeWithBatch(models, null);
        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertEquals("Exception handled by: model.constructor.value.rs.ers.ModelsQueue, caused by: null, happened in line: null, message: One of the supplied models is null", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void initializeWithBatch6()
    {
        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(1, 1, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(new LNorm(Double.POSITIVE_INFINITY));
        models.add(new LNorm(Double.POSITIVE_INFINITY));

        String msg = null;
        try
        {
            modelsQueue.initializeWithBatch(models, null);
        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertEquals("Exception handled by: model.constructor.value.rs.ers.ModelsQueue, caused by: null, happened in line: null, message: The number of supplied models (2) does not equal the queue size limit (1)", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void initializeWithBatch7()
    {
        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(1, 1, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY));

        String msg = null;
        try
        {
            modelsQueue.initializeWithBatch(models, null);
            assertNull(modelsQueue.isValid());
            LinkedList<SortedModel<LNorm>> queue = modelsQueue.getQueue();
            assertTrue(modelsQueue.areAllSortedModelsCompatible());
            assertEquals(1, queue.size());
            assertEquals(0, queue.getFirst()._id);
            assertTrue(Double.compare(queue.getFirst()._compatibilityDegree, 0.0d) > 0);
            assertTrue(queue.getFirst()._isCompatible);
            assertEquals(0, queue.getFirst()._closestModels.getNoStoredModels());

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }


    /**
     * Test 8.
     */
    @Test
    void initializeWithBatch8()
    {
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(new LNorm(new double[]{0.52d, 0.48d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.25d, 0.75d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.75d, 0.25d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(3, 1, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        String msg = null;
        try
        {
            // Create batch
            modelsQueue.initializeWithBatch(models, feedback);
            assertNull(modelsQueue.isValid());
            assertFalse(modelsQueue.areAllSortedModelsCompatible());
            assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
            assertFalse(modelsQueue.getQueue().get(1)._isCompatible);
            assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
            assertEquals(3, modelsQueue.getQueue().size());

            // Insert model that improves the incompatibility
            {
                LNorm model = new LNorm(new double[]{0.098d, 0.902d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertFalse(modelsQueue.getQueue().get(1)._isCompatible);
                assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
                assertEquals(-0.1275d, modelsQueue.getQueue().get(1)._compatibilityDegree);
                assertEquals(-0.25d, modelsQueue.getQueue().getLast()._compatibilityDegree);
                assertEquals(3, modelsQueue.getQueue().size());
            }

            // Insert model that improves the incompatibility
            {
                LNorm model = new LNorm(new double[]{0.901d, 0.099d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertEquals(0, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
                assertFalse(modelsQueue.getQueue().get(1)._isCompatible);
                assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
                assertEquals(-0.12625, modelsQueue.getQueue().get(1)._compatibilityDegree);
                assertEquals(-0.1275d, modelsQueue.getQueue().getLast()._compatibilityDegree);
                assertEquals(3, modelsQueue.getQueue().size());
            }

            // Insert model that should be rejected
            {
                LNorm model = new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertEquals(0, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
                assertFalse(modelsQueue.getQueue().get(1)._isCompatible);
                assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
                assertEquals(-0.12625, modelsQueue.getQueue().get(1)._compatibilityDegree);
                assertEquals(-0.1275d, modelsQueue.getQueue().getLast()._compatibilityDegree);
                assertEquals(3, modelsQueue.getQueue().size());
            }

            // Insert model that improves the incompatibility
            {
                LNorm model = new LNorm(new double[]{0.099d, 0.901}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertEquals(0, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
                assertFalse(modelsQueue.getQueue().get(1)._isCompatible);
                assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
                assertEquals(-0.12625, modelsQueue.getQueue().get(1)._compatibilityDegree);
                assertEquals(-0.12625, modelsQueue.getQueue().getLast()._compatibilityDegree);
                assertEquals(3, modelsQueue.getQueue().size());
            }

            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertTrue(modelsQueue.getQueue().get(1)._isCompatible);

                assertEquals(1, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
                assertEquals(0.028284271, modelsQueue.getQueue().getFirst()._closestModels._similarities[0], 1.0E-5);
                assertEquals(1, modelsQueue.getQueue().get(1)._closestModels.getNoStoredModels());
                assertEquals(0.028284271, modelsQueue.getQueue().get(1)._closestModels._similarities[0], 1.0E-5);

                assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
                assertEquals(-0.12625, modelsQueue.getQueue().getLast()._compatibilityDegree);
                assertEquals(3, modelsQueue.getQueue().size());
            }
            // Insert incompatible model
            {
                LNorm model = new LNorm(new double[]{0.85d, 0.15d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertTrue(modelsQueue.getQueue().get(1)._isCompatible);
                assertFalse(modelsQueue.getQueue().getLast()._isCompatible);
                assertEquals(-0.0625, modelsQueue.getQueue().getLast()._compatibilityDegree);
                assertEquals(3, modelsQueue.getQueue().size());
            }
            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.49d, 0.51d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertNull(modelsQueue.isValid());
                assertTrue(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertTrue(modelsQueue.getQueue().get(1)._isCompatible);
                assertTrue(modelsQueue.getQueue().getLast()._isCompatible);

                assertEquals(1, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
                assertEquals(1, modelsQueue.getQueue().get(1)._closestModels.getNoStoredModels());
                assertEquals(1, modelsQueue.getQueue().getLast()._closestModels.getNoStoredModels());

                assertEquals(0.028284271, modelsQueue.getQueue().getFirst()._closestModels._similarities[0], 1.0E-5);
                assertEquals(0.014142136, modelsQueue.getQueue().get(1)._closestModels._similarities[0], 1.0E-5);
                assertEquals(0.014142136, modelsQueue.getQueue().getLast()._closestModels._similarities[0], 1.0E-5);
                assertEquals(3, modelsQueue.getQueue().size());
            }

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

    /**
     * Test 9.
     */
    @Test
    void initializeWithBatch9()
    {
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.25d, 0.75d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.75d, 0.25d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(1, 1, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        String msg = null;
        try
        {
            // Create batch
            modelsQueue.initializeWithBatch(models, feedback);
            assertFalse(modelsQueue.areAllSortedModelsCompatible());
            assertFalse(modelsQueue.getQueue().getFirst()._isCompatible);
            assertEquals(-0.25d, modelsQueue.getQueue().getFirst()._compatibilityDegree);

            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.52d, 0.48d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertTrue(modelsQueue.areAllSortedModelsCompatible());
                assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
                assertEquals(0, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
                assertEquals(1, modelsQueue.getQueue().size());
            }

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }


    /**
     * Test 10.
     */
    @Test
    void initializeWithBatch10()
    {
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{0.51d, 0.49d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.25d, 0.75d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.75d, 0.25d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(4, 4, new CompatibilityAnalyzer(), new KNearest<>(0), new Euclidean());
        String msg = null;
        try
        {
            // Create batch
            modelsQueue.initializeWithBatch(models, feedback);
            modelsQueue.updateClosestModelsAndSortQueue(false, true);
            assertFalse(modelsQueue.areAllSortedModelsCompatible());

            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.52d, 0.48d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
            }
            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.48d, 0.52d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
            }
            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.44d, 0.56d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertTrue(modelsQueue.areAllSortedModelsCompatible());
            }

            assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
            assertTrue(modelsQueue.getQueue().get(1)._isCompatible);
            assertTrue(modelsQueue.getQueue().get(2)._isCompatible);
            assertTrue(modelsQueue.getQueue().getLast()._isCompatible);

            assertEquals(3, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
            assertEquals(3, modelsQueue.getQueue().get(1)._closestModels.getNoStoredModels());
            assertEquals(3, modelsQueue.getQueue().get(2)._closestModels.getNoStoredModels());
            assertEquals(3, modelsQueue.getQueue().getLast()._closestModels.getNoStoredModels());

            assertEquals(0.056568542, modelsQueue.getQueue().getFirst()._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.098994949, modelsQueue.getQueue().getFirst()._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.113137085, modelsQueue.getQueue().getFirst()._closestModels._similarities[2], 1.0E-5);

            assertEquals(0.042426407, modelsQueue.getQueue().get(1)._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.056568542, modelsQueue.getQueue().get(1)._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.056568542, modelsQueue.getQueue().get(1)._closestModels._similarities[2], 1.0E-5);

            assertEquals(0.014142136, modelsQueue.getQueue().get(2)._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.056568542, modelsQueue.getQueue().get(2)._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.113137085, modelsQueue.getQueue().get(2)._closestModels._similarities[2], 1.0E-5);

            assertEquals(0.014142136, modelsQueue.getQueue().getLast()._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.042426407, modelsQueue.getQueue().getLast()._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.098994949, modelsQueue.getQueue().getLast()._closestModels._similarities[2], 1.0E-5);

            assertNull(modelsQueue.isValid());

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

    /**
     * Test 11.
     */
    @Test
    void initializeWithBatch11()
    {
        ArrayList<LNorm> models = new ArrayList<>();
        models.add(new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{0.51d, 0.49d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY));
        models.add(new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.25d, 0.75d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.75d, 0.25d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(4, 4, new CompatibilityAnalyzer(), new KNearest<>(2), new Euclidean());
        String msg = null;
        try
        {
            // Create batch
            modelsQueue.initializeWithBatch(models, feedback);
            modelsQueue.updateClosestModelsAndSortQueue(false, true);
            assertFalse(modelsQueue.areAllSortedModelsCompatible());

            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.52d, 0.48d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
            }
            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.48d, 0.52d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertFalse(modelsQueue.areAllSortedModelsCompatible());
            }
            // Insert compatible model
            {
                LNorm model = new LNorm(new double[]{0.44d, 0.56d}, Double.POSITIVE_INFINITY);
                modelsQueue.insertModel(model, feedback);
                assertTrue(modelsQueue.areAllSortedModelsCompatible());
            }

            assertTrue(modelsQueue.getQueue().getFirst()._isCompatible);
            assertTrue(modelsQueue.getQueue().get(1)._isCompatible);
            assertTrue(modelsQueue.getQueue().get(2)._isCompatible);
            assertTrue(modelsQueue.getQueue().getLast()._isCompatible);

            assertEquals(3, modelsQueue.getQueue().getFirst()._closestModels.getNoStoredModels());
            assertEquals(3, modelsQueue.getQueue().get(1)._closestModels.getNoStoredModels());
            assertEquals(3, modelsQueue.getQueue().get(2)._closestModels.getNoStoredModels());
            assertEquals(3, modelsQueue.getQueue().getLast()._closestModels.getNoStoredModels());

            assertEquals(0.056568542, modelsQueue.getQueue().getFirst()._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.098994949, modelsQueue.getQueue().getFirst()._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.113137085, modelsQueue.getQueue().getFirst()._closestModels._similarities[2], 1.0E-5);

            assertEquals(0.014142136, modelsQueue.getQueue().get(1)._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.056568542, modelsQueue.getQueue().get(1)._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.113137085, modelsQueue.getQueue().get(1)._closestModels._similarities[2], 1.0E-5);

            assertEquals(0.014142136, modelsQueue.getQueue().get(2)._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.042426407, modelsQueue.getQueue().get(2)._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.098994949, modelsQueue.getQueue().get(2)._closestModels._similarities[2], 1.0E-5);

            assertEquals(0.042426407, modelsQueue.getQueue().getLast()._closestModels._similarities[0], 1.0E-5);
            assertEquals(0.056568542, modelsQueue.getQueue().getLast()._closestModels._similarities[1], 1.0E-5);
            assertEquals(0.056568542, modelsQueue.getQueue().getLast()._closestModels._similarities[2], 1.0E-5);

            assertNull(modelsQueue.isValid());

        } catch (ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }


    /**
     * Test 12.
     */
    @Test
    void initializeWithBatch12()
    {
        int trials = 20;
        IRandom R = new MersenneTwister64(0);
        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();

        String msg = null;
        try
        {
            for (int noModels : new int[]{100, 1000, 2000})
            {
                for (int noFeedback : new int[]{2, 5, 10})
                {
                    System.out.println("Processing (2D case): no. models = " + noModels + "; no. feedback = " + noFeedback);

                    double ms = 0.0d;

                    for (int t = 0; t < trials; t++)
                    {
                        // Create random models:
                        ArrayList<LNorm> models = new ArrayList<>();
                        for (int m = 0; m < noModels; m++)
                            models.add(new LNorm(WeightsGenerator.getNormalizedWeightVector(2, R), Double.POSITIVE_INFINITY));

                        // Create random DM:
                        IDMFeedbackProvider dm = new ArtificialValueDM<>(new model.definitions.LNorm(
                                new LNorm(WeightsGenerator.getNormalizedWeightVector(2, R), Double.POSITIVE_INFINITY)));
                        dm.registerDecisionMakingContext(new DMContext(null, null, null, null, false, 0));

                        // Create feedback:
                        LinkedList<PreferenceInformationWrapper> piw = new LinkedList<>();
                        for (int pc = 0; pc < noFeedback; pc++)
                        {
                            HashMap<Integer, LinkedList<ReferenceSet>> rs = new HashMap<>();
                            LinkedList<ReferenceSet> lrs = new LinkedList<>();

                            lrs.add(new ReferenceSet(new Alternative("A0", WeightsGenerator.getNormalizedWeightVector(2, R)),
                                    new Alternative("A1", WeightsGenerator.getNormalizedWeightVector(2, R))));
                            rs.put(2, lrs);
                            ReferenceSets rss = new ReferenceSets(1, new int[]{2}, rs);
                            DMResult dmResult = dm.getFeedback(rss);
                            piw.add(PreferenceInformationWrapper.getTestInstance(dmResult._feedback.getFirst()));
                        }

                        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, 1, CA, new KNearest<>(0), new Euclidean());
                        long startTime = System.nanoTime();
                        modelsQueue.initializeWithBatch(models, piw);
                        long stopTime = System.nanoTime();
                        ms += (double) (stopTime - startTime) / 1.0E6d;
                    }

                    ms /= trials;
                    System.out.println("Took " + ms + " ms");
                }
            }
        } catch (ReferenceSetsConstructorException | FeedbackProviderException | ConstructorException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}