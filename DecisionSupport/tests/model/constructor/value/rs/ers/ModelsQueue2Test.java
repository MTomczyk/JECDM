package model.constructor.value.rs.ers;

import alternative.Alternative;
import compatibility.CompatibilityAnalyzer;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.comparators.KNearest;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Cos;
import model.similarity.lnorm.Euclidean;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Provides various tests for {@link ModelsQueue}
 *
 * @author MTomczyk
 */
class ModelsQueue2Test
{
    /**
     * Test 1 (Euclidean distance).
     */
    @Test
    void test1()
    {
        for (int k : new int[]{1, 2, 3, 4, 5})
        {
            for (int kIdx = 0; kIdx < k; kIdx++)
            {
                IRandom R = new MersenneTwister64(0);
                CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
                IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
                int noModels = 50;
                int improvementAttempts = 50;
                ArrayList<LNorm> models = new ArrayList<>();
                for (int m = 0; m < noModels; m++) models.add(RM.generateModel(R));

                ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, k, CA, new KNearest<>(kIdx), new Euclidean());
                String msg = null;
                try
                {
                    long startTime = System.nanoTime();
                    // Create batch
                    modelsQueue.initializeWithBatch(models, null);
                    modelsQueue.updateClosestModelsAndSortQueue(false, false);
                    assertNull(modelsQueue.isValid());

                    for (int ia = 0; ia < improvementAttempts; ia++)
                    {
                        modelsQueue.insertModel(RM.generateModel(R), null);
                        assertNull(modelsQueue.isValid());
                    }
                    long stopTime = System.nanoTime();
                    double dt = (double) (stopTime - startTime) / 1.0E6;
                    System.out.println("Took " + dt + " ms (includes time-consuming validation; without feedback; k = " + k + "; k for comparator = " + kIdx + ")");

                } catch (ConstructorException e)
                {
                    msg = e.toString();
                }
                assertNull(msg);
            }
        }
    }

    /**
     * Test 2 (Cosine distance).
     */
    @Test
    void test2()
    {
        for (int k : new int[]{1, 2, 3, 4, 5})
        {
            for (int kIdx = 0; kIdx < k; kIdx++)
            {
                IRandom R = new MersenneTwister64(0);
                CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
                IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
                int noModels = 50;
                int improvementAttempts = 50;
                ArrayList<LNorm> models = new ArrayList<>();
                for (int m = 0; m < noModels; m++) models.add(RM.generateModel(R));

                ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, k, CA, new KNearest<>(kIdx), new Cos());
                String msg = null;
                try
                {
                    long startTime = System.nanoTime();
                    // Create batch
                    modelsQueue.initializeWithBatch(models, null);
                    modelsQueue.updateClosestModelsAndSortQueue(false, false);
                    assertNull(modelsQueue.isValid());

                    for (int ia = 0; ia < improvementAttempts; ia++)
                    {
                        modelsQueue.insertModel(RM.generateModel(R), null);
                        assertNull(modelsQueue.isValid());
                    }
                    long stopTime = System.nanoTime();
                    double dt = (double) (stopTime - startTime) / 1.0E6;
                    System.out.println("Took " + dt + " ms (includes time-consuming validation; without feedback; k = " + k + "; k for comparator = " + kIdx + ")");

                } catch (ConstructorException e)
                {
                    msg = e.toString();
                }
                assertNull(msg);
            }
        }
    }


    /**
     * Test 3 (Euclidean distance).
     */
    @Test
    void test3()
    {
        for (int k : new int[]{1, 2, 3, 4, 5})
        {
            for (int kIdx = 0; kIdx < k; kIdx++)
            {
                IRandom R = new MersenneTwister64(0);
                CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
                IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
                int noModels = 1000;
                int improvementAttempts = 9000;
                ArrayList<LNorm> models = new ArrayList<>();
                for (int m = 0; m < noModels; m++) models.add(RM.generateModel(R));

                ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, k, CA, new KNearest<>(kIdx), new Euclidean());
                String msg = null;
                try
                {
                    long startTime = System.nanoTime();
                    // Create batch
                    modelsQueue.initializeWithBatch(models, null);
                    for (int ia = 0; ia < improvementAttempts; ia++)
                        modelsQueue.insertModel(RM.generateModel(R), null);

                    long stopTime = System.nanoTime();
                    double dt = (double) (stopTime - startTime) / 1.0E6;
                    System.out.println("Took " + dt + " ms (without validation; without feedback; k = " + k + "; k for comparator = " + kIdx + ")");

                } catch (ConstructorException e)
                {
                    msg = e.toString();
                }
                assertNull(msg);
            }
        }
    }

    /**
     * Test 4 (Euclidean distance; with feedback).
     */
    @Test
    void test4()
    {
        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.5d, 0.5d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.5d, 0.5d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        for (int k : new int[]{1, 2, 3, 4, 5})
        {
            for (int kIdx = 0; kIdx < k; kIdx++)
            {
                IRandom R = new MersenneTwister64(0);
                CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
                IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
                int noModels = 1000;
                int improvementAttempts = 9000;
                ArrayList<LNorm> models = new ArrayList<>();
                for (int m = 0; m < noModels; m++) models.add(RM.generateModel(R));

                ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, k, CA, new KNearest<>(kIdx), new Euclidean());
                String msg = null;
                try
                {
                    long startTime = System.nanoTime();
                    // Create batch
                    modelsQueue.initializeWithBatch(models, feedback);
                    for (int ia = 0; ia < improvementAttempts; ia++)
                        modelsQueue.insertModel(RM.generateModel(R), feedback);

                    long stopTime = System.nanoTime();
                    double dt = (double) (stopTime - startTime) / 1.0E6;
                    System.out.println("Took " + dt + " ms (without validation; with feedback; k = " + k + "; k for comparator = " + kIdx + ")");

                } catch (ConstructorException e)
                {
                    msg = e.toString();
                }
                assertNull(msg);
            }
        }
    }
}