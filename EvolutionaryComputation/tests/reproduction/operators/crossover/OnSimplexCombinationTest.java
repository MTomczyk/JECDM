package reproduction.operators.crossover;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import space.simplex.Simplex;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provides various tests for {@link OnSimplexCombination}
 *
 * @author MTomczyk
 */
class OnSimplexCombinationTest
{
    /**
     * Test 1.
     */
    @Test
    void crossover1()
    {
        IRandom R = new MersenneTwister64(0);
        ICrossover crossover = new OnSimplexCombination(10.0d);
        double[] p1 = new double[]{0.25d, 0.75d};
        double[] p2 = new double[]{0.75d, 0.25d};

        for (int t = 0; t < 1000; t++)
        {
            double[] o = crossover.crossover(p1, p2, R)._o;
            assertTrue(Simplex.isOnSimplex(o, 1.0E-12));
        }
    }

    /**
     * Test 2.
     */
    @Test
    void crossover2()
    {
        IRandom R = new MersenneTwister64(0);
        ICrossover crossover = new OnSimplexCombination(10.0d);
        double[] p1 = new double[]{0.25d, 0.25d, 0.5d};
        double[] p2 = new double[]{0.8d, 0.1d, 0.1d};

        for (int t = 0; t < 1000; t++)
        {
            double[] o = crossover.crossover(p1, p2, R)._o;
            assertTrue(Simplex.isOnSimplex(o, 1.0E-12));
        }
    }
}