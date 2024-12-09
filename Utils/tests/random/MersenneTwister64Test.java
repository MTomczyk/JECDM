package random;

import org.junit.jupiter.api.Test;
import print.PrintUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Several tests for the {@link MersenneTwister64 class}.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class MersenneTwister64Test
{
    /**
     * Test 1.
     */
    @Test
    void getIntWithProbability()
    {
        int trials = 10000000;
        IRandom R = new MersenneTwister64(System.currentTimeMillis());

        {
            int[] d = new int[]{0};
            double[] p = new double[]{1.0d};
            int[] D = new int[d.length];

            for (int t = 0; t < trials; t++)
            {
                int sel = R.getIntWithProbability(d, p);
                D[sel]++;
            }

            for (int i = 0; i < D.length; i++)
                assertEquals(p[i], (double) D[i] / (double) trials, 0.001d);

            PrintUtils.printVectorOfIntegers(D);
        }

        {
            int[] d = new int[]{0, 1};
            double[] p = new double[]{0.3d, 0.7d};
            int[] D = new int[d.length];

            for (int t = 0; t < trials; t++)
            {
                int sel = R.getIntWithProbability(d, p);
                D[sel]++;
            }

            for (int i = 0; i < D.length; i++)
                assertEquals(p[i], (double) D[i] / (double) trials, 0.001d);

            PrintUtils.printVectorOfIntegers(D);
        }

        {
            int[] d = new int[]{0, 1, 2};
            double[] p = new double[]{0.3d, 0.2d, 0.5d};
            int[] D = new int[d.length];

            for (int t = 0; t < trials; t++)
            {
                int sel = R.getIntWithProbability(d, p);
                D[sel]++;
            }

            for (int i = 0; i < D.length; i++)
                assertEquals(p[i], (double) D[i] / (double) trials, 0.001d);

            PrintUtils.printVectorOfIntegers(D);
        }

        // ===========================

        {
            double[] p = new double[]{1.0d};
            int[] D = new int[1];

            for (int t = 0; t < trials; t++)
            {
                int sel = R.getIdxWithProbability(p);
                D[sel]++;
            }

            for (int i = 0; i < D.length; i++)
                assertEquals(p[i], (double) D[i] / (double) trials, 0.001d);

            PrintUtils.printVectorOfIntegers(D);
        }

        {
            double[] p = new double[]{0.3d, 0.7d};
            int[] D = new int[2];

            for (int t = 0; t < trials; t++)
            {
                int sel = R.getIdxWithProbability(p);
                D[sel]++;
            }

            for (int i = 0; i < D.length; i++)
                assertEquals(p[i], (double) D[i] / (double) trials, 0.001d);

            PrintUtils.printVectorOfIntegers(D);
        }

        {
            double[] p = new double[]{0.3d, 0.2d, 0.5d};
            int[] D = new int[3];

            for (int t = 0; t < trials; t++)
            {
                int sel = R.getIdxWithProbability(p);
                D[sel]++;
            }

            for (int i = 0; i < D.length; i++)
                assertEquals(p[i], (double) D[i] / (double) trials, 0.001d);

            PrintUtils.printVectorOfIntegers(D);
        }
    }
}