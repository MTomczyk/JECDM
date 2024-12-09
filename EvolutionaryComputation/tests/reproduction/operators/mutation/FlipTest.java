package reproduction.operators.mutation;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for the {@link reproduction.operators.mutation.Flip} class.
 *
 * @author MTomczyk
 */

class FlipTest
{

    /**
     * Test 1.
     */
    @Test
    void mutate1()
    {
        IRandom R = new MersenneTwister64(System.currentTimeMillis());

        int T = 1000000;
        for (int n : new int[]{1, 2, 5, 10, 20})
        {

            double CNT = 0.0d;
            double DIFF = 0.0d;

            IMutate M = new Flip(new Flip.Params(1.0d / n));
            for (int t = 0; t < T; t++)
            {
                int[] p = new int[n];
                for (int i = 0; i < n; i++)
                {
                    if (R.nextBoolean()) p[i] = 0;
                    else p[i] = 2;
                }
                int[] o = p.clone();
                M.mutate(o, R);

                double cnt = 0;
                double diff = 0.0d;
                for (int i = 0; i < n; i++)
                {
                    if (p[i] != o[i])
                    {
                        cnt++;
                        diff += Math.abs(p[i] - o[i]);
                    }
                }
                DIFF += diff;
                CNT += cnt;

            }

            CNT /= T;
            DIFF /= T;
            assertEquals(1.0d, CNT, 0.01d);
            assertEquals(2.0d, DIFF, 0.01d);
        }
    }

    /**
     * Test 2.
     */
    @Test
    void mutate2()
    {
        IRandom R = new MersenneTwister64(System.currentTimeMillis());

        int T = 1000000;
        for (int n : new int[]{1, 2, 5, 10, 20})
        {

            double CNT = 0.0d;
            double DIFF = 0.0d;

            IMutate M = new Flip(new Flip.Params(1.0d / n));
            for (int t = 0; t < T; t++)
            {
                double[] p = new double[n];
                for (int i = 0; i < n; i++)
                {
                    if (R.nextBoolean()) p[i] = 0;
                    else p[i] = 2;
                }
                double[] o = p.clone();
                M.mutate(o, R);

                double cnt = 0;
                double diff = 0.0d;
                for (int i = 0; i < n; i++)
                {
                    if (Double.compare(p[i], o[i]) != 0)
                    {
                        cnt++;
                        diff += Math.abs(p[i] - o[i]);
                    }
                }
                DIFF += diff;
                CNT += cnt;

            }

            CNT /= T;
            DIFF /= T;
            assertEquals(1.0d, CNT, 0.01d);
            assertEquals(2.0d, DIFF, 0.01d);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void mutate3()
    {
        IRandom R = new MersenneTwister64(System.currentTimeMillis());

        int T = 1000000;
        for (int n : new int[]{1, 2, 5, 10, 20})
        {

            double CNT = 0.0d;
            double DIFF = 0.0d;

            IMutate M = new Flip(new Flip.Params(1.0d / n));
            for (int t = 0; t < T; t++)
            {
                boolean[] p = new boolean[n];
                for (int i = 0; i < n; i++) p[i] = R.nextBoolean();
                boolean[] o = p.clone();
                M.mutate(o, R);

                double cnt = 0;
                double diff = 0.0d;
                for (int i = 0; i < n; i++)
                {
                    if (p[i] !=  o[i])
                    {
                        cnt++;
                        diff += 1;
                    }
                }
                DIFF += diff;
                CNT += cnt;

            }

            CNT /= T;
            DIFF /= T;
            assertEquals(1.0d, CNT, 0.01d);
            assertEquals(1.0d, DIFF, 0.01d);
        }
    }
}