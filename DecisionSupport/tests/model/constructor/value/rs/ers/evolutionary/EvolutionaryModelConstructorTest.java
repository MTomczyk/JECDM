package model.constructor.value.rs.ers.evolutionary;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.ModelsQueue;
import model.constructor.value.rs.ers.SortedModel;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import org.junit.jupiter.api.Test;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link EvolutionaryModelConstructor}
 *
 * @author MTomczyk
 */
class EvolutionaryModelConstructorTest
{
    /**
     * Test 1 (tournament size = 1)
     */
    @Test
    void getModel1()
    {
        int trials = 10000;
        IRandom R = new MersenneTwister64(0);
        DMContext dmc = new DMContext(null, null, null, null, false, 0, null, R);

        for (int noModels : new int[]{1, 2, 3, 4, 5})
        {
            IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
            ArrayList<LNorm> models = new ArrayList<>(noModels);
            for (int i = 0; i < noModels; i++) models.add(RM.generateModel(R));
            ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, 3, new CompatibilityAnalyzer(),
                    new MostSimilarWithTieResolving<>(), new Euclidean());
            String msg = null;


            int[] returned = new int[noModels];

            try
            {
                modelsQueue.initializeWithBatch(models, null);

                HashMap<LNorm, Integer> modelToPlace = new HashMap<>(noModels);
                int idx = 0;
                for (SortedModel<LNorm> sm : modelsQueue.getQueue()) modelToPlace.put(sm._model, idx++);
                EvolutionaryModelConstructor<LNorm> emc = new EvolutionaryModelConstructor<>((p1, p2, dmContext) -> {
                    if (R.nextBoolean()) return p1._model;
                    return p2._model;
                }, 1);

                for (int t = 0; t < trials; t++)
                {
                    LNorm sm = emc.getModel(modelsQueue, dmc);
                    assertTrue(modelToPlace.containsKey(sm));
                    returned[modelToPlace.get(sm)]++;
                }

                for (int i = 0; i < noModels; i++)
                    assertEquals(1.0d / noModels, (double) returned[i] / trials, 1.0E-2);

            } catch (ConstructorException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }

    /**
     * Test 2 (tournament size = 2)
     */
    @Test
    void getModel2()
    {
        int trials = 10000;
        IRandom R = new MersenneTwister64(0);
        DMContext dmc = new DMContext(null, null, null, null, false, 0, null, R);

        for (int noModels : new int[]{5})
        {
            IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
            ArrayList<LNorm> models = new ArrayList<>(noModels);
            for (int i = 0; i < noModels; i++) models.add(RM.generateModel(R));
            ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, 3, new CompatibilityAnalyzer(),
                    new MostSimilarWithTieResolving<>(), new Euclidean());
            String msg = null;


            int[] returned = new int[noModels];

            try
            {
                modelsQueue.initializeWithBatch(models, null);

                HashMap<LNorm, Integer> modelToPlace = new HashMap<>(noModels);
                int idx = 0;
                for (SortedModel<LNorm> sm : modelsQueue.getQueue()) modelToPlace.put(sm._model, idx++);
                EvolutionaryModelConstructor<LNorm> emc = new EvolutionaryModelConstructor<>((p1, p2, dmContext) -> {
                    if (R.nextBoolean()) return p1._model;
                    return p2._model;
                }, 2);

                for (int t = 0; t < trials; t++)
                {
                    LNorm sm = emc.getModel(modelsQueue, dmc);
                    assertTrue(modelToPlace.containsKey(sm));
                    returned[modelToPlace.get(sm)]++;
                }

                PrintUtils.printVectorOfIntegers(returned);
                for (int i = 0; i < noModels; i++)
                {
                    double dv = noModels - i;
                    double p1 = Math.pow(dv / noModels, 2.0d);
                    double p2 = 1.0d / dv + (dv - 1) / (dv * dv);
                    assertEquals(p1 * p2, (double) returned[i] / trials, 1.0E-2);

                }

            } catch (ConstructorException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }
    }
}