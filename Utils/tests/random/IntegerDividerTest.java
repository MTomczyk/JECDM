package random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Contains various tests for {@link IntegerDivider} class.
 *
 * @author MTomczyk
 */
class IntegerDividerTest
{
    /**
     * Test 1 (nulls).
     */
    @Test
    void test1()
    {
        assertNull(IntegerDivider.split(1, 1, null));
        assertNull(IntegerDivider.split(10, 11, new MersenneTwister64(0), true));
        assertNull(IntegerDivider.split(-1, 5, new MersenneTwister64(0), true));
        assertNull(IntegerDivider.split(10, 0, new MersenneTwister64(0), true));
    }

    /**
     * Test 2 (1-split).
     */
    @Test
    void test2()
    {
        IRandom R = new MersenneTwister64(0);
        int trials = 10000;
        for (int k: new int[] {5, 6, 6, 10, 1000})
        {
            for (int t = 0; t < trials; t++)
            {
                {
                    int [] s = IntegerDivider.split(k, 1, R);
                    assertEquals(1, s.length);
                    assertEquals(k, s[0]);
                }
                {
                    int [] s = IntegerDivider.split(k, 1, R, true);
                    assertEquals(1, s.length);
                    assertEquals(k, s[0]);
                }
            }
        }
    }

    /**
     * Test 3 (2-split).
     */
    @Test
    void test3()
    {
        IRandom R = new MersenneTwister64(0);
        int trials = 10000000;
        for (int k: new int[] {2, 3, 4, 5})
        {
            {
                int [][] d = new int[k + 1][k + 1];
                for (int t = 0; t < trials; t++)
                {
                    {
                        int [] s = IntegerDivider.split(k, 2, R);
                        assertEquals(2, s.length);
                        d[s[0]][s[1]]++;
                    }
                }
                int sum = 0;
                double min = Double.POSITIVE_INFINITY;
                double max = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < k + 1; i++)
                {
                    for (int ii = 0; ii < k + 1; ii++)
                    {
                        sum += d[i][ii];
                        if (i + ii != k) assertEquals(0, d[i][ii]);
                        else
                        {
                            double val = (double) d[i][ii] / trials;
                            if (Double.compare(val, max) > 0) max = val;
                            if (Double.compare(val, min) < 0) min = val;
                        }
                    }
                }
                assertEquals(trials, sum);
                assertTrue(Double.compare(max, min) >= 0);
                assertTrue(max - min < 0.01d);
            }

            {
                int [][] d = new int[k + 1][k + 1];
                for (int t = 0; t < trials; t++)
                {
                    {
                        int [] s = IntegerDivider.split(k, 2, R, true);
                        assertEquals(2, s.length);
                        d[s[0]][s[1]]++;
                    }
                }
                int sum = 0;
                double min = Double.POSITIVE_INFINITY;
                double max = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < k + 1; i++)
                {
                    for (int ii = 0; ii < k + 1; ii++)
                    {
                        sum += d[i][ii];
                        if ( (i + ii != k) || (i == 0) || (ii == 0)) assertEquals(0, d[i][ii]);
                        else
                        {
                            double val = (double) d[i][ii] / trials;
                            if (Double.compare(val, max) > 0) max = val;
                            if (Double.compare(val, min) < 0) min = val;
                        }
                    }
                }
                assertEquals(trials, sum);
                assertTrue(Double.compare(max, min) >= 0);
                assertTrue(max - min < 0.01d);
            }

        }
    }


    /**
     * Test 4 (3-split).
     */
    @Test
    void test4()
    {
        IRandom R = new MersenneTwister64(0);
        int trials = 100000;

        for (int k: new int[]{0, 1, 2, 3, 4, 5})
        {
            int[][][] d = new int[k + 1][k + 1][k + 1];
            for (int t = 0; t < trials; t++)
            {
                {
                    int[] s = IntegerDivider.split(k, 3, R);
                    assertEquals(3, s.length);
                    d[s[0]][s[1]][s[2]]++;
                }
            }

            int sum = 0;
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < k + 1; i++)
            {
                for (int ii = 0; ii < k + 1; ii++)
                {
                    for (int iii = 0; iii < k + 1; iii++)
                    {
                        sum += d[i][ii][iii];
                        if (i + ii + iii != k) assertEquals(0, d[i][ii][iii]);
                        else
                        {
                            double val = (double) d[i][ii][iii] / trials;
                            if (Double.compare(val, max) > 0) max = val;
                            if (Double.compare(val, min) < 0) min = val;
                        }
                    }
                }
            }
            assertEquals(trials, sum);
            assertTrue(Double.compare(max, min) >= 0);
            assertTrue(max - min < 0.01d);

        }

        for (int k: new int[]{3, 4, 5})
        {
            int[][][] d = new int[k + 1][k + 1][k + 1];
            for (int t = 0; t < trials; t++)
            {
                {
                    int[] s = IntegerDivider.split(k, 3, R, true);
                    assertEquals(3, s.length);
                    d[s[0]][s[1]][s[2]]++;
                }
            }

            int sum = 0;
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < k + 1; i++)
            {
                for (int ii = 0; ii < k + 1; ii++)
                {
                    for (int iii = 0; iii < k + 1; iii++)
                    {
                        sum += d[i][ii][iii];
                        if ((i + ii + iii != k) || (i == 0) || (ii == 0) || (iii == 0))
                            assertEquals(0, d[i][ii][iii]);
                        else
                        {
                            double val = (double) d[i][ii][iii] / trials;
                            if (Double.compare(val, max) > 0) max = val;
                            if (Double.compare(val, min) < 0) min = val;
                        }
                    }
                }
            }
            assertEquals(trials, sum);
            assertTrue(Double.compare(max, min) >= 0);
            assertTrue(max - min < 0.01d);
        }
    }

    /**
     * Test 5 (4-split).
     */
    @Test
    void test5()
    {
        IRandom R = new MersenneTwister64(0);
        int trials = 100000;

        for (int k: new int[]{0, 1, 2, 3, 4, 5})
        {
            int[][][][] d = new int[k + 1][k + 1][k + 1][k+1];
            for (int t = 0; t < trials; t++)
            {
                {
                    int[] s = IntegerDivider.split(k, 4, R);
                    assertEquals(4, s.length);
                    d[s[0]][s[1]][s[2]][s[3]]++;
                }
            }

            int sum = 0;
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < k + 1; i++)
            {
                for (int ii = 0; ii < k + 1; ii++)
                {
                    for (int iii = 0; iii < k + 1; iii++)
                    {
                        for (int iiii = 0; iiii < k + 1; iiii++)
                        {
                            sum += d[i][ii][iii][iiii];
                            if (i + ii + iii + iiii != k) assertEquals(0, d[i][ii][iii][iiii]);
                            else
                            {
                                double val = (double) d[i][ii][iii][iiii] / trials;
                                if (Double.compare(val, max) > 0) max = val;
                                if (Double.compare(val, min) < 0) min = val;
                            }
                        }
                    }
                }
            }

            assertEquals(trials, sum);
            assertTrue(Double.compare(max, min) >= 0);
            assertTrue(max - min < 0.01d);

        }

        for (int k: new int[]{4, 5, 6})
        {
            int[][][][] d = new int[k + 1][k + 1][k + 1][k + 1];
            for (int t = 0; t < trials; t++)
            {
                {
                    int[] s = IntegerDivider.split(k, 4, R, true);
                    assertEquals(4, s.length);
                    d[s[0]][s[1]][s[2]][s[3]]++;
                }
            }

            int sum = 0;
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < k + 1; i++)
            {
                for (int ii = 0; ii < k + 1; ii++)
                {
                    for (int iii = 0; iii < k + 1; iii++)
                    {
                        for (int iiii = 0; iiii < k + 1; iiii++)
                        {
                            sum += d[i][ii][iii][iiii];
                            if ((i + ii + iii + iiii != k) || (i == 0) || (ii == 0) || (iii == 0) || (iiii == 0))
                                assertEquals(0, d[i][ii][iii][iiii]);
                            else
                            {
                                double val = (double) d[i][ii][iii][iiii] / trials;
                                if (Double.compare(val, max) > 0) max = val;
                                if (Double.compare(val, min) < 0) min = val;
                            }
                        }
                    }
                }
            }
            assertEquals(trials, sum);
            assertTrue(Double.compare(max, min) >= 0);
            assertTrue(max - min < 0.01d);
        }
    }

}