package model.constructor.value.rs.ers;

import model.similarity.ISimilarity;
import model.similarity.lnorm.Euclidean;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ClosestModels}.
 *
 * @author MTomczyk
 */
class ClosestModelsTest
{
    /**
     * Test 1.
     */
    @Test
    void insert1()
    {
        LNorm reference = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);

        ISimilarity<LNorm> similarity = new Euclidean();
        ClosestModels<LNorm> cms = new ClosestModels<>(1, similarity.isLessMeaningCloser());

        assertEquals(0, cms.getNoStoredModels());
        double sim1;
        {
            LNorm m = new LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY);
            sim1 = similarity.calculateSimilarity(reference, m);
            assertTrue(cms.canAlterTheQueue(sim1));
            cms.insert(new SortedModel<>(0, 0, true, m, 0.0d), 1, sim1);
            assertEquals(1, cms.getNoStoredModels());
            assertEquals(1, cms._ids[0]);
            assertEquals(sim1, cms._similarities[0], 1.0E-5);
        }
        {
            LNorm m = new LNorm(new double[]{0.8d, 0.2d}, Double.POSITIVE_INFINITY);
            double sim = similarity.calculateSimilarity(reference, m);
            assertFalse(cms.canAlterTheQueue(sim));
        }
        {
            LNorm m = new LNorm(new double[]{0.6d, 0.4d}, Double.POSITIVE_INFINITY);
            double sim = similarity.calculateSimilarity(reference, m);
            assertTrue(cms.canAlterTheQueue(sim));
            cms.insert(new SortedModel<>(0, 0, true, m, 0.0d), 2, sim);
            assertEquals(1, cms.getNoStoredModels());
            assertEquals(2, cms._ids[0]);
            assertEquals(sim, cms._similarities[0], 1.0E-5);
        }
    }


    /**
     * Test 2.
     */
    @Test
    void insert2()
    {
        LNorm reference = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);

        ISimilarity<LNorm> similarity = new Euclidean();
        ClosestModels<LNorm> cms = new ClosestModels<>(2, similarity.isLessMeaningCloser());

        assertEquals(0, cms.getNoStoredModels());
        double sim1;
        double sim2;
        double sim3;
        {
            LNorm m = new LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY);
            sim1 = similarity.calculateSimilarity(reference, m);
            assertTrue(cms.canAlterTheQueue(sim1));
            cms.insert(new SortedModel<>(0, 0, true, m, 0.0d), 1, sim1);
            assertEquals(1, cms.getNoStoredModels());
            assertEquals(1, cms._ids[0]);
            assertEquals(sim1, cms._similarities[0], 1.0E-5);
        }
        {
            LNorm m = new LNorm(new double[]{0.8d, 0.2d}, Double.POSITIVE_INFINITY);
            sim2 = similarity.calculateSimilarity(reference, m);
            assertTrue(cms.canAlterTheQueue(sim2));
            cms.insert(new SortedModel<>(0, 0, true, m, 0.0d), 2, sim2);
            assertEquals(2, cms.getNoStoredModels());
            assertEquals(1, cms._ids[0]);
            assertEquals(2, cms._ids[1]);
            assertEquals(sim1, cms._similarities[0], 1.0E-5);
            assertEquals(sim2, cms._similarities[1], 1.0E-5);
        }
        {
            LNorm m = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
            sim3 = similarity.calculateSimilarity(reference, m);
            assertTrue(cms.canAlterTheQueue(sim3));
            cms.insert(new SortedModel<>(0, 0, true, m, 0.0d), 3, sim3);
            assertEquals(2, cms.getNoStoredModels());
            assertEquals(3, cms._ids[0]);
            assertEquals(1, cms._ids[1]);
            assertEquals(sim3, cms._similarities[0], 1.0E-5);
            assertEquals(sim1, cms._similarities[1], 1.0E-5);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void insert3()
    {
        ISimilarity<LNorm> similarity = new Euclidean();
        IRandom R = new MersenneTwister64(0);
        int toAdd = 1000;

        long startTime = System.nanoTime();
        for (int m = 2; m < 5; m++)
        {
            for (int k = 1; k < 10; k++)
            {
                LNorm reference = new LNorm(WeightsGenerator.getNormalizedWeightVector(m, R), Double.POSITIVE_INFINITY);
                ClosestModels<LNorm> cms = new ClosestModels<>(k, similarity.isLessMeaningCloser());

                for (int t = 0; t < 100; t++)
                {
                    ArrayList<ModelPackage<LNorm>> packages = new ArrayList<>(toAdd);
                    for (int i = 0; i < toAdd; i++)
                    {
                        LNorm model = new LNorm(WeightsGenerator.getNormalizedWeightVector(m, R), Double.POSITIVE_INFINITY);
                        double s = similarity.calculateSimilarity(reference, model);
                        if (cms.canAlterTheQueue(s)) cms.insert(new SortedModel<>(0, 0, true, model, 0.0d), i, s);
                        packages.add(new ModelPackage<>(model, i, s));
                    }
                    assertEquals(k, cms.getNoStoredModels());
                    packages.sort(Comparator.comparingDouble(o -> o._similarity));
                    Set<Integer> ids = new HashSet<>(k);
                    for (int i = 0; i < k; i++)
                    {
                        if (i > 0) assertNotEquals(cms._ids[i - 1], cms._ids[i]);
                        ids.add(cms._ids[i]);
                        assertEquals(packages.get(i)._id, cms._ids[i]);
                        assertEquals(packages.get(i)._similarity, cms._similarities[i], 1.0E-5);
                    }
                    assertEquals(ids.size(), k);
                    cms.reset();
                }
            }
        }

        long stopTime = System.nanoTime();
        double ms = (stopTime - startTime) / 1.0E6;
        System.out.println("Took " + ms + " ms");
    }
}