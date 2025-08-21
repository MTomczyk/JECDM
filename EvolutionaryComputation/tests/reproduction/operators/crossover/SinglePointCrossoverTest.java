package reproduction.operators.crossover;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several for the {@link reproduction.operators.crossover.SinglePointCrossover} class.
 *
 * @author MTomczyk
 */
class SinglePointCrossoverTest
{
    /**
     * Test 1.
     */
    @Test
    void crossover1()
    {
        IRandom R = new MersenneTwister64(0);

        int T = 100000;
        for (int n : new int[]{1, 2, 5, 10, 20})
        {
            double[] r = new double[n];

            ICrossover C = new SinglePointCrossover(new SinglePointCrossover.Params());
            for (int t = 0; t < T; t++)
            {
                int[] p1 = new int[n];
                int[] p2 = new int[n];
                for (int i = 0; i < n; i++)
                {
                    p1[i] = 0;
                    p2[i] = 1;
                }

                int[] o = C.crossover(p1.clone(), p2.clone(), R)._o;
                assertEquals(n, o.length);
                for (int i = 0; i < n; i++) r[i] += o[i];
            }
            for (int i = 0; i < n; i++)
            {
                r[i] /= T;
                assertEquals(0.5d, r[i], 0.01d);
            }
        }
    }

    /**
     * Test 2.
     */
    @Test
    void crossover2()
    {
        IRandom R = new MersenneTwister64(0);

        int T = 100000;
        for (int n : new int[]{1, 2, 5, 10, 20})
        {
            double[] r = new double[n];

            ICrossover C = new SinglePointCrossover(new SinglePointCrossover.Params());
            for (int t = 0; t < T; t++)
            {
                double[] p1 = new double[n];
                double[] p2 = new double[n];
                for (int i = 0; i < n; i++)
                {
                    p1[i] = 0.0d;
                    p2[i] = 1.0d;
                }


                double[] o = C.crossover(p1.clone(), p2.clone(), R)._o;
                assertEquals(n, o.length);
                for (int i = 0; i < n; i++) r[i] += o[i];
            }
            for (int i = 0; i < n; i++)
            {
                r[i] /= T;
                assertEquals(0.5d, r[i], 0.01d);
            }
        }
    }

    /**
     * Test 3.
     */
    @Test
    void crossover3()
    {
        IRandom R = new MersenneTwister64(0);

        int T = 100000;
        for (int n : new int[]{1, 2, 5, 10, 20})
        {
            double[] r = new double[n];

            ICrossover C = new SinglePointCrossover(new SinglePointCrossover.Params());
            for (int t = 0; t < T; t++)
            {
                boolean[] p1 = new boolean[n];
                boolean[] p2 = new boolean[n];
                for (int i = 0; i < n; i++)
                {
                    p1[i] = false;
                    p2[i] = true;
                }

                boolean[] o = C.crossover(p1.clone(), p2.clone(), R)._o;
                assertEquals(n, o.length);
                for (int i = 0; i < n; i++) if (o[i]) r[i]++;
            }
            for (int i = 0; i < n; i++)
            {
                r[i] /= T;
                assertEquals(0.5d, r[i], 0.01d);
            }
        }
    }
}