package io.cross.excel.utils;

import org.junit.jupiter.api.Test;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link RankerUtils}.
 *
 * @author MTomczyk
 */
class RankerUtilsTest
{
    /**
     * Test 1.
     */
    @Test
    void countWins()
    {
        {
            double[] v1 = new double[]{1.0d, 2.0d, 3.0d};
            double[] v2 = new double[]{0.0d, 1.0d, 2.0d};
            assertEquals(3, RankerUtils.countWins(v1, v2, false, 0.0d));
            assertEquals(0, RankerUtils.countWins(v1, v2, true, 0.0d));
            assertEquals(0, RankerUtils.countWins(v1, v2, false, 1.1d));
            assertEquals(0, RankerUtils.countWins(v1, v2, true, 1.1d));
        }

        {
            double[] v1 = new double[]{1.0d, 0.0d, 3.0d};
            double[] v2 = new double[]{0.0d, 1.0d, 2.0d};
            assertEquals(2, RankerUtils.countWins(v1, v2, false, 0.0d));
            assertEquals(1, RankerUtils.countWins(v1, v2, true, 0.0d));
            assertEquals(0, RankerUtils.countWins(v1, v2, false, 1.1d));
            assertEquals(0, RankerUtils.countWins(v1, v2, true, 1.1d));
        }
    }

    /**
     * Test 2.
     */
    @Test
    void countTies()
    {
        {
            double[] v1 = new double[]{1.0d, 2.0d, 3.0d};
            double[] v2 = new double[]{1.0d, 2.0d, 3.0d};
            assertEquals(3, RankerUtils.countTies(v1, v2, 0.0d));
        }

        {
            double[] v1 = new double[]{0.0d, 0.0d, 0.0d};
            double[] v2 = new double[]{1.0d, 2.0d, 3.0d};
            assertEquals(0, RankerUtils.countTies(v1, v2, 0.0d));
            assertEquals(1, RankerUtils.countTies(v1, v2, 1.1d));
            assertEquals(2, RankerUtils.countTies(v1, v2, 2.1d));
            assertEquals(3, RankerUtils.countTies(v1, v2, 3.1d));
        }
    }

    /**
     * Test 3.
     */
    @Test
    void calculateAttainedRanks1()
    {
        double[][] m = new double[][]
                {
                        {2.0d, 5.0d, 4.0d, 3.0d, 2.0d},
                        null,
                        {1.0d, 1.0d, 2.0d, 3.0d, 5.0d},
                        {3.0d, 1.0d, 1.0d, 1.0d, 2.0},
                        null
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, true, 0.0d);
        PrintUtils.print2dDoubles(r, 2);

        double[][] exp = new double[][]
                {
                        {0.5d, 2.0d, 2.5d},
                        {0.0d, 0.0d, 0.0d},
                        {1.5d, 2.0d, 1.5d},
                        {3.0d, 1.0d, 1.0d},
                        {0.0d, 0.0d, 0.0d}
                };

        assertEquals(exp.length, r.length);
        assertEquals(exp[0].length, r[0].length);

        for (int i = 0; i < exp.length; i++)
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], r[i][j], 0.000001d);
    }

    /**
     * Test 4.
     */
    @Test
    void calculateAttainedRanks2()
    {
        double[][] m = new double[][]
                {
                        {2.0d, 5.0d, 4.0d, 3.0d, 2.0d},
                        null,
                        {1.0d, 1.0d, 2.0d, 3.0d, 5.0d},
                        {3.0d, 1.0d, 1.0d, 1.0d, 2.0},
                        null
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, false, 0.0d);
        PrintUtils.print2dDoubles(r, 2);

        double[][] exp = new double[][]
                {
                        {2.5d, 2.0d, 0.5d},
                        {0.0d, 0.0d, 0.0d},
                        {1.5d, 2.0d, 1.5d},
                        {1.0d, 1.0d, 3.0d},
                        {0.0d, 0.0d, 0.0d}
                };

        assertEquals(exp.length, r.length);
        assertEquals(exp[0].length, r[0].length);

        for (int i = 0; i < exp.length; i++)
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], r[i][j], 0.000001d);
    }


    /**
     * Test 5.
     */
    @Test
    void calculateAttainedRanks3()
    {
        double[][] m = new double[][]
                {
                        {2.0d, 5.0d, 4.0d, 3.0d, 2.0d},
                        null,
                        {1.0d, 1.0d, 2.0d, 3.0d, 5.0d},
                        {3.0d, 1.0d, 1.0d, 1.0d, 2.0},
                        null
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, true, 10.0d);
        PrintUtils.print2dDoubles(r, 2);

        double[][] exp = new double[][]
                {
                        {5.0d / 3.0d, 5.0d / 3.0d, 5.0d / 3.0d},
                        {0.0d, 0.0d, 0.0d},
                        {5.0d / 3.0d, 5.0d / 3.0d, 5.0d / 3.0d},
                        {5.0d / 3.0d, 5.0d / 3.0d, 5.0d / 3.0d},
                        {0.0d, 0.0d, 0.0d}
                };

        assertEquals(exp.length, r.length);
        assertEquals(exp[0].length, r[0].length);

        for (int i = 0; i < exp.length; i++)
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], r[i][j], 0.000001d);
    }


    /**
     * Test 6.
     */
    @Test
    void calculateAttainedRanks4()
    {
        double[][] m = new double[][]
                {
                        {2.0d, 5.0d, 4.0d, 3.0d, 2.0d},
                        null,
                        {1.0d, 1.0d, 2.0d, 3.0d, 5.0d},
                        {3.0d, 1.0d, 1.0d, 1.0d, 2.0},
                        null
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, false, 10.0d);
        PrintUtils.print2dDoubles(r, 2);

        double[][] exp = new double[][]
                {
                        {5.0d / 3.0d, 5.0d / 3.0d, 5.0d / 3.0d},
                        {0.0d, 0.0d, 0.0d},
                        {5.0d / 3.0d, 5.0d / 3.0d, 5.0d / 3.0d},
                        {5.0d / 3.0d, 5.0d / 3.0d, 5.0d / 3.0d},
                        {0.0d, 0.0d, 0.0d}
                };

        assertEquals(exp.length, r.length);
        assertEquals(exp[0].length, r[0].length);

        for (int i = 0; i < exp.length; i++)
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], r[i][j], 0.000001d);
    }

    /**
     * Test 7.
     */
    @Test
    void calculateAttainedRanks5()
    {
        double[][] m = new double[][]
                {
                        null,
                        null,
                        null,
                        null,
                        null
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, false, 10.0d);
        PrintUtils.print2dDoubles(r, 2);
        assertEquals(5, r.length);
        assertEquals(0, r[0].length);
    }

    /**
     * Test 8.
     */
    @Test
    void calculateAttainedRanks6()
    {
        double[][] m = new double[][]
                {
                        {1.0d, 2.0d, 3.0d, 4.0d},
                        {4.0d, 4.0d, 4.0d, 4.0d},
                        {2.0d, 3.0d, 1.0d, 1.0d},
                        {1.0d, 2.0d, 2.0d, 2.0d},
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, true, 0.0d);
        PrintUtils.print2dDoubles(r, 2);

        double[][] exp = new double[][]
                {
                        {1.0d, 1.0d, 1.5d, 0.5d},
                        {0.0d, 0.0d, 0.5d, 3.5d},
                        {2.0d, 0.0d, 2.0d, 0.0d},
                        {1.0d, 3.0d, 0.0d, 0.0d},
                };

        assertEquals(exp.length, r.length);
        assertEquals(exp[0].length, r[0].length);

        for (int i = 0; i < exp.length; i++)
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], r[i][j], 0.000001d);
    }

    /**
     * Test 9.
     */
    @Test
    void calculateAttainedRanks7()
    {
        double[][] m = new double[][]
                {
                        {1.0d, 2.0d, 3.0d, 4.0d},
                        {4.0d, 4.0d, 4.0d, 4.0d},
                        {2.0d, 3.0d, 1.0d, 1.0d},
                        {1.0d, 2.0d, 2.0d, 2.0d},
                };

        double[][] r = RankerUtils.calculateAttainedRanks(m, false, 0.0d);
        PrintUtils.print2dDoubles(r, 2);

        double[][] exp = new double[][]
                {
                        {0.5d, 1.5d, 1.0d, 1.0d},
                        {3.5d, 0.5d, 0.0d, 0.0d},
                        {0.0d, 2.0d, 0.0d, 2.0d},
                        {0.0d, 0.0d, 3.0d, 1.0d},
                };

        assertEquals(exp.length, r.length);
        assertEquals(exp[0].length, r[0].length);

        for (int i = 0; i < exp.length; i++)
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], r[i][j], 0.000001d);
    }


    /**
     * Test 10.
     */
    @Test
    void calculateAttainedRanks8()
    {
        IRandom R = new MersenneTwister64(0);
        for (int scenarios = 1; scenarios < 10; scenarios++)
        {
            for (int trials = 1; trials < 10; trials++)
            {
                double[][] m = new double[scenarios][trials];
                for (int s = 0; s < scenarios; s++)
                    for (int t = 0; t < trials; t++)
                        m[s][t] = R.nextDouble();

                double [][] result = RankerUtils.calculateAttainedRanks(m, false, 0.0d);
                assertEquals(scenarios, result.length);
                assertEquals(scenarios, result[0].length);
                // sum in rows
                for (int row = 0; row < scenarios; row++)
                {
                    double sum = 0.0d;
                    for (int c = 0; c < scenarios; c++) sum += result[row][c];
                    assertEquals(sum, trials, 0.00001d);
                }

                // sum in columns
                for (int column = 0; column < scenarios; column++)
                {
                    double sum = 0.0d;
                    for (int r = 0; r < scenarios; r++) sum += result[r][column];
                    assertEquals(sum, trials, 0.00001d);
                }
            }
        }
    }
}