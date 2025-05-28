package model.constructor.value.rs.ers.evolutionary;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.ERS;
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
            ERS.Params<LNorm> pERS = new ERS.Params<>(RM);
            pERS._similarity = new Euclidean();
            pERS._kMostSimilarNeighbors = 3;
            pERS._feasibleSamplesToGenerate = noModels;
            pERS._compatibilityAnalyzer = new CompatibilityAnalyzer();
            pERS._comparator = new MostSimilarWithTieResolving<>();
            ERS<LNorm> ers = new ERS<>(pERS);

            ArrayList<LNorm> models = new ArrayList<>(noModels);
            for (int i = 0; i < noModels; i++) models.add(RM.generateModel(R));

            String msg = null;

            int[] returned = new int[noModels];

            try
            {
                ers.getModelsQueue().initializeWithBatch(models, null);

                HashMap<LNorm, Integer> modelToPlace = new HashMap<>(noModels);
                int idx = 0;
                for (SortedModel<LNorm> sm : ers.getModelsQueue().getQueue()) modelToPlace.put(sm._model, idx++);
                EvolutionaryModelConstructor<LNorm> emc = new EvolutionaryModelConstructor<>((dmContext, parents) -> {
                    if (R.nextBoolean()) return parents.get(0)._model;
                    return parents.get(1)._model;
                }, new Tournament<>(1));

                for (int t = 0; t < trials; t++)
                {
                    LNorm sm = emc.getModel(dmc, ers);
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
            ERS.Params<LNorm> pERS = new ERS.Params<>(RM);
            pERS._similarity = new Euclidean();
            pERS._kMostSimilarNeighbors = 3;
            pERS._feasibleSamplesToGenerate = noModels;
            pERS._compatibilityAnalyzer = new CompatibilityAnalyzer();
            pERS._comparator = new MostSimilarWithTieResolving<>();
            ERS<LNorm> ers = new ERS<>(pERS);

            ArrayList<LNorm> models = new ArrayList<>(noModels);
            for (int i = 0; i < noModels; i++) models.add(RM.generateModel(R));

            String msg = null;


            int[] returned = new int[noModels];

            try
            {
                ers.getModelsQueue().initializeWithBatch(models, null);

                HashMap<LNorm, Integer> modelToPlace = new HashMap<>(noModels);
                int idx = 0;
                for (SortedModel<LNorm> sm : ers.getModelsQueue().getQueue()) modelToPlace.put(sm._model, idx++);
                EvolutionaryModelConstructor<LNorm> emc = new EvolutionaryModelConstructor<>((dmContext, parents) -> {
                    if (R.nextBoolean()) return parents.get(0)._model;
                    return parents.get(1)._model;
                }, new Tournament<>(2));

                for (int t = 0; t < trials; t++)
                {
                    LNorm sm = emc.getModel(dmc, ers);
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