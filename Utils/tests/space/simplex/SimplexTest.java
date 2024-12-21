package space.simplex;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;
import space.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Simplex}.
 *
 * @author MTomczyk
 */
class SimplexTest
{
    /**
     * Test 1 (is on simplex).
     */
    @Test
    public void isOnSimplex()
    {
        {
            assertFalse(Simplex.isOnSimplex(null, 0.0d));
            assertFalse(Simplex.isOnSimplex(new double[0], 0.0d));
        }
        {
            assertTrue(Simplex.isOnSimplex(new double[]{1.0d}, 0.0d));
            assertTrue(Simplex.isOnSimplex(new double[]{0.5d, 0.5d}, 0.0d));
            assertTrue(Simplex.isOnSimplex(new double[]{0.25d, 0.75d}, 0.0d));
            assertFalse(Simplex.isOnSimplex(new double[]{-0.00001d, 0.9999d}, 0.0d));
            assertFalse(Simplex.isOnSimplex(new double[]{0.0d, 1.00001d}, 0.0d));
        }
        {
            IRandom R = new MersenneTwister64(0);
            for (int l = 2; l < 5; l++)
            {
                for (int t = 0; t < 1000; t++)
                {
                    double[] w = WeightsGenerator.getNormalizedWeightVector(l, R);
                    assertTrue(Simplex.isOnSimplex(w, 1.0E-12));
                }
            }
        }
    }


    /**
     * Test 2 (is on simplex).
     */
    @Test
    public void getMinMaxCombinationWeights()
    {
        {
            assertNull(Simplex.getMinMaxCombinationWeights(null, null));
            assertNull(Simplex.getMinMaxCombinationWeights(null, new double[1]));
            assertNull(Simplex.getMinMaxCombinationWeights(new double[1], null));
            assertNull(Simplex.getMinMaxCombinationWeights(new double[1], new double[2]));
            assertNull(Simplex.getMinMaxCombinationWeights(new double[]{1.0d, 2.0d}, new double[]{1.0d, 2.0d}));
        }
        {
            double[] r = Simplex.getMinMaxCombinationWeights(new double[]{0.25d, 0.75}, new double[]{0.5d, 0.5});
            assertEquals(-1.0d, r[0], 1.0E-9);
            assertEquals(3.0d, r[1], 1.0E-9);
        }
        {
            double[] r = Simplex.getMinMaxCombinationWeights(new double[]{0.0d, 1.0}, new double[]{1.0d, 0.0});
            assertEquals(0.0d, r[0], 1.0E-9);
            assertEquals(1.0d, r[1], 1.0E-9);
        }
        {
            IRandom R = new MersenneTwister64(0);
            for (int m = 2; m < 5; m++)
            {
                for (int t = 0; t < 1000; t++)
                {
                    double[] w1 = WeightsGenerator.getNormalizedWeightVector(m, R);
                    double[] w2 = WeightsGenerator.getNormalizedWeightVector(m, R);
                    double[] r = Simplex.getMinMaxCombinationWeights(w1, w2);
                    double[] W1 = Vector.getCombination(w1, w2, r[0]);
                    double[] W2 = Vector.getCombination(w1, w2, r[1]);
                    Vector.thresholdAtOneFromAbove(W1);
                    Vector.thresholdAtZeroFromBelow(W1);
                    Vector.thresholdAtOneFromAbove(W2);
                    Vector.thresholdAtZeroFromBelow(W2);
                    assertTrue(Simplex.isOnSimplex(W1, 1.0E-12));
                    assertTrue(Simplex.isOnSimplex(W2, 1.0E-12));
                }
            }
        }

        {
            double[] p1 = new double[]{0.2d, 0.2d, 0.6d}; // parent #1
            double[] p2 = new double[]{0.7d, 0.2d, 0.1d}; // parent #2
            double[] wB = Simplex.getMinMaxCombinationWeights(p1, p2);
            assertEquals(-0.4d, wB[0], 1.0E-6);
            assertEquals(1.2d, wB[1], 1.0E-6);
        }
    }


}