package reproduction.operators.crossover;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several for the {@link SinglePointCrossoverTO} class.
 *
 * @author MTomczyk
 */
class SinglePointCrossoverTOTest
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

            ICrossoverTO C = new SinglePointCrossoverTO(new SinglePointCrossoverTO.Params());
            for (int t = 0; t < T; t++)
            {
                int[] p1 = new int[n];
                int[] p2 = new int[n];
                for (int i = 0; i < n; i++)
                {
                    p1[i] = 0;
                    p2[i] = 1;
                }

                ICrossoverTO.IntResult ir = C.crossover(p1.clone(), p2.clone(), R);
                assertEquals(n, ir._o1.length);
                assertEquals(n, ir._o2.length);
                for (int i = 0; i < n; i++)
                {
                    r[i] += ir._o1[i];
                    r[i] += ir._o2[i];
                }
            }
            for (int i = 0; i < n; i++)
            {
                r[i] /= T;
                assertEquals(1.0d, r[i], 0.01d);
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

            ICrossoverTO C = new SinglePointCrossoverTO(new SinglePointCrossoverTO.Params());
            for (int t = 0; t < T; t++)
            {
                double[] p1 = new double[n];
                double[] p2 = new double[n];
                for (int i = 0; i < n; i++)
                {
                    p1[i] = 0.0d;
                    p2[i] = 1.0d;
                }

                ICrossoverTO.DoubleResult dr = C.crossover(p1.clone(), p2.clone(), R);
                assertEquals(n, dr._o1.length);
                assertEquals(n, dr._o2.length);
                for (int i = 0; i < n; i++)
                {
                    r[i] += dr._o1[i];
                    r[i] += dr._o2[i];
                }
            }
            for (int i = 0; i < n; i++)
            {
                r[i] /= T;
                assertEquals(1.0d, r[i], 0.01d);
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

            ICrossoverTO C = new SinglePointCrossoverTO(new SinglePointCrossoverTO.Params());
            for (int t = 0; t < T; t++)
            {
                boolean[] p1 = new boolean[n];
                boolean[] p2 = new boolean[n];
                for (int i = 0; i < n; i++)
                {
                    p1[i] = false;
                    p2[i] = true;
                }

                ICrossoverTO.BoolResult br = C.crossover(p1.clone(), p2.clone(), R);
                assertEquals(n, br._o1.length);
                assertEquals(n, br._o2.length);
                for (int i = 0; i < n; i++)
                {
                    if (br._o1[i]) r[i]++;
                    if (br._o2[i]) r[i]++;
                }
            }
            for (int i = 0; i < n; i++)
            {
                r[i] /= T;
                assertEquals(1.0d, r[i], 0.01d);
            }
        }
    }
}