package space.distance;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;
import space.normalization.minmax.LinearWithFlip;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link LNorm} class.
 *
 * @author MTomczyk
 */
class LNormTest
{
    /**
     * Test #1.
     */
    @Test
    void evaluate1()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();
            double[] e = new double[]{0.3 + dx, 0.7 + dy};
            LNorm LN = new LNorm(new double[]{0.5d, 0.5d}, 1.0d, null);
            assertEquals(0.5d, LN.getDistance(e, new double[]{dx, dy}), 0.0000001d);
        }
    }

    /**
     * Test #2 (alpha 1).
     */
    @Test
    void evaluate2()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();

            INormalization[] normalizations = new INormalization[2];
            normalizations[0] = new Linear(0.0d, 1.0d);
            normalizations[1] = new LinearWithFlip(1.0d, 2.0d, 1.0d);
            LNorm LN = new LNorm(new double[]{0.3d, 0.7d}, 1.0d, normalizations);

            double[][] e = new double[21][2];
            for (int i = 0; i < 21; i++)
            {
                e[i][0] = 0.7d + dx;
                e[i][1] = 2.0d - ((double) i * 0.05d + dy);
            }

            double[] ans =
                    {
                            0.21,
                            0.245,
                            0.28,
                            0.315,
                            0.35,
                            0.385,
                            0.42,
                            0.455,
                            0.49,
                            0.525,
                            0.56,
                            0.595,
                            0.63,
                            0.665,
                            0.7,
                            0.735,
                            0.77,
                            0.805,
                            0.84,
                            0.875,
                            0.91,


                    };

            for (int i = 0; i < 21; i++)
            {
                double v = LN.getDistance(e[i], new double[]{dx, 2.0d - dy});
                assertEquals(ans[i], v, 0.0000001d);
            }
        }
    }

    /**
     * Test #3 (alpha 1.7).
     */
    @Test
    void evaluate3()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();

            INormalization[] normalizations = new INormalization[2];
            normalizations[0] = new Linear(0.0d, 1.0d);
            normalizations[1] = new LinearWithFlip(1.0d, 2.0d, 1.0d);
            LNorm LN = new LNorm(new double[]{0.3d, 0.7d}, 1.7d, normalizations);

            double[][] e = new double[21][2];
            for (int i = 0; i < 21; i++)
            {
                e[i][0] = 0.7d + dx;
                e[i][1] = 2.0d - (double) (i * 0.05d + dy);
            }

            double[] ans =
                    {
                            0.21,
                            0.21581747,
                            0.228517196,
                            0.245906668,
                            0.266766453,
                            0.290242284,
                            0.315715397,
                            0.342732973,
                            0.370960797,
                            0.400149679,
                            0.430111658,
                            0.460703203,
                            0.491813336,
                            0.523355198,
                            0.555260009,
                            0.587472701,
                            0.619948723,
                            0.652651684,
                            0.685551587,
                            0.718623501,
                            0.751846538,

                    };

            for (int i = 0; i < 21; i++)
            {
                double v = LN.getDistance(e[i], new double[]{dx, 2.0d - dy});
                assertEquals(ans[i], v, 0.0000001d);
            }
        }
    }


    /**
     * Test #4 (alpha infinity).
     */
    @Test
    void evaluate4()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();

            INormalization[] normalizations = new INormalization[2];
            normalizations[0] = new Linear(0.0d, 1.0d);
            normalizations[1] = new LinearWithFlip(1.0d, 2.0d, 1.0d);
            LNorm LN = new LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY, normalizations);

            double[][] e = new double[21][2];
            for (int i = 0; i < 21; i++)
            {
                e[i][0] = 0.7d + dx;
                e[i][1] = 2.0d - (double) (i * 0.05d + dy);
            }

            double[] ans =
                    {
                            0.21,
                            0.21,
                            0.21,
                            0.21,
                            0.21,
                            0.21,
                            0.21,
                            0.245,
                            0.28,
                            0.315,
                            0.35,
                            0.385,
                            0.42,
                            0.455,
                            0.49,
                            0.525,
                            0.56,
                            0.595,
                            0.63,
                            0.665,
                            0.7,
                    };

            for (int i = 0; i < 21; i++)
            {
                double v = LN.getDistance(e[i], new double[]{dx, 2.0d - dy});
                assertEquals(ans[i], v, 0.0000001d);
            }
        }
    }


    /**
     * Test #5.
     */
    @Test
    void evaluate5()
    {
        LNorm LN = new LNorm(new double[]{0.5d, 0.5d}, 1.0d, null);
        LN.setParams(new double[][]{new double[]{0.1d, 0.9d}, new double[]{0.5d}});

        double[][] params = LN.getParams();
        assertEquals(0.1d, params[0][0], 0.00001d);
        assertEquals(0.9d, params[0][1], 0.00001d);
        assertEquals(0.5d, params[1][0], 0.00001d);
    }

    /**
     * Test #6 (alpha 1, no weights).
     */
    @Test
    void evaluate6()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();

            INormalization[] normalizations = new INormalization[2];
            normalizations[0] = new Linear(0.0d, 1.0d);
            normalizations[1] = new Linear(0.0d, 2.0d);
            LNorm LN = new LNorm(null, 1.0d, normalizations);

            double[][] e = new double[21][2];
            for (int i = 0; i < 21; i++)
            {
                e[i][0] = i + dx;
                e[i][1] = 2 * i + dy;
            }

            double[] ans = new double[21];
            for (int i = 0; i < 21; i++) ans[i] = 2 * i;

            for (int i = 0; i < 21; i++)
            {
                double v = LN.getDistance(e[i], new double[]{dx, dy});
                assertEquals(ans[i], v, 0.0000001d);
            }
        }
    }

    /**
     * Test #7 (alpha infinity, no weights).
     */
    @Test
    void evaluate7()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();
            INormalization[] normalizations = new INormalization[2];
            normalizations[0] = new Linear(0.0d, 1.0d);
            normalizations[1] = new Linear(0.0d, 2.0d);
            LNorm LN = new LNorm(null, Double.POSITIVE_INFINITY, normalizations);

            double[][] e = new double[21][2];
            for (int i = 0; i < 21; i++)
            {
                e[i][0] = i + dx;
                e[i][1] = 2 * i + dy;
            }

            double[] ans = new double[21];
            for (int i = 0; i < 21; i++) ans[i] = i;

            for (int i = 0; i < 21; i++)
            {
                double v = LN.getDistance(e[i], new double[]{dx, dy});
                assertEquals(ans[i], v, 0.0000001d);
            }
        }
    }

    /**
     * Test #8 (alpha 2, no weights).
     */
    @Test
    void evaluate8()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100; t++)
        {
            double dx = -1.0d + 2.0d * R.nextDouble();
            double dy = -1.0d + 2.0d * R.nextDouble();

            INormalization[] normalizations = new INormalization[2];
            normalizations[0] = new Linear(0.0d, 1.0d);
            normalizations[1] = new Linear(0.0d, 2.0d);
            LNorm LN = new LNorm(null, 2.0d, normalizations);

            double[][] e = new double[21][2];
            for (int i = 0; i < 21; i++)
            {
                e[i][0] = i + dx;
                e[i][1] = 2 * (i + dy);
            }

            double[] ans = new double[21];
            for (int i = 0; i < 21; i++) ans[i] = i * Math.sqrt(2);

            for (int i = 0; i < 21; i++)
            {
                double v = LN.getDistance(e[i], new double[]{dx, 2 * dy});
                assertEquals(ans[i], v, 0.0000001d);
            }
        }
    }


}