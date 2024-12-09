package search;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;
import valuewrapper.DoubleWrapper;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Several for the {@link InArraySearch class}.
 *
 * @author MTomczyk
 */
class InArraySearchTest
{
    /**
     * Test #1.
     */
    @Test
    void getIndexMassive1()
    {

        int T = 100000;
        int[] LL = new int[]{1, 2, 10, 100};
        IRandom R = new MersenneTwister64(0);

        for (int L : LL)
        {
            System.out.println("testing L = " + L);
            for (int t = 0; t < T; t++)
            {
                // doubles
                {
                    double[] a = new double[L];
                    for (int i = 0; i < L; i++) a[i] = R.nextDouble();
                    Arrays.sort(a);
                    double th = R.nextDouble();

                    InArraySearch<DoubleWrapper> as = new InArraySearch<>();
                    ArrayList<DoubleWrapper> arr = new ArrayList<>();
                    for (double d: a) arr.add(new DoubleWrapper(d));

                    // at least (non-strict)
                    int alNS = InArraySearch.getIndexAtLeast(th, a, false);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                    // at least (strict)
                    alNS = InArraySearch.getIndexAtLeast(th, a, true);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);
                    // at least (non-strict)
                    alNS = as.getIndexAtLeast(th, arr, false);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                    // at least (strict)
                    alNS = as.getIndexAtLeast(th, arr, true);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);

                    // at most (non-strict)
                    alNS = InArraySearch.getIndexAtMost(th, a, false);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);
                    // at most (strict)
                    alNS = InArraySearch.getIndexAtMost(th, a, true);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                    // at most (non-strict)
                    alNS = as.getIndexAtMost(th, arr, false);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);
                    // at most (strict)
                    alNS = as.getIndexAtMost(th, arr, true);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                }

                // integers
                {
                    double[] a = new double[L];
                    for (int i = 0; i < L; i++) a[i] = R.nextInt(100);
                    Arrays.sort(a);
                    double th = R.nextInt(100);

                    InArraySearch<DoubleWrapper> as = new InArraySearch<>();
                    ArrayList<DoubleWrapper> arr = new ArrayList<>();
                    for (double d: a) arr.add(new DoubleWrapper(d));

                    // at least (non-strict)
                    int alNS = InArraySearch.getIndexAtLeast(th, a, false);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                    // at least (strict)
                    alNS = InArraySearch.getIndexAtLeast(th, a, true);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);
                    // at least (non-strict)
                    alNS = as.getIndexAtLeast(th, arr, false);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                    // at least (strict)
                    alNS = as.getIndexAtLeast(th, arr, true);
                    for (int i = 0; i < Math.min(L, alNS); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);

                    // at most (non-strict)
                    alNS = InArraySearch.getIndexAtMost(th, a, false);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);
                    // at most (strict)
                    alNS = InArraySearch.getIndexAtMost(th, a, true);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                    // at most (non-strict)
                    alNS = as.getIndexAtMost(th, arr, false);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) <= 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) > 0);
                    // at most (strict)
                    alNS = as.getIndexAtMost(th, arr, true);
                    for (int i = 0; i < Math.min(L, alNS + 1); i++) assertTrue(Double.compare(a[i], th) < 0);
                    for (int i = Math.max(0, alNS + 1); i < L; i++) assertTrue(Double.compare(a[i], th) >= 0);
                }

            }
        }
    }

    /**
     * Test #2.
     */
    @Test
    void getIndexAtLeast2()
    {
        {
            double[] array = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1.0d, 1.0d};

            assertEquals(0, InArraySearch.getIndexAtLeast(0.0d, array, false));
            assertEquals(27, InArraySearch.getIndexAtLeast(2.0d, array, false));
            assertEquals(25, InArraySearch.getIndexAtLeast(0.99d, array, false));
            assertEquals(25, InArraySearch.getIndexAtLeast(1.0d, array, false));

            double[] th = new double[]{0.11d, 0.21d, 0.211, 0.23, 0.25, 0.24, 0.251, 0.269,
                    0.271, 0.33, 0.41, 0.421, 0.435, 0.45, 0.515, 0.521, 0.55, 0.5500001, 0.71, 0.76, 0.76000001, 0.94, 0.96, 0.99, 1.0000001};
            int[] expI = new int[]{0, 1, 2, 2, 4, 3, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 27};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], InArraySearch.getIndexAtLeast(th[i], array, false));
            th = new double[]{0.0d, 1.0d, 0.12d, 0.121d, 0.119, 0.22};
            expI = new int[]{-1, 26, 0, 0, -1, 1};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], InArraySearch.getIndexAtMost(th[i], array, false));
        }

        {
            assertEquals(0, InArraySearch.getIndexAtLeast(2.0d, new double[]{}, false));
            assertEquals(1, InArraySearch.getIndexAtLeast(2.0d, new double[]{0.0d}, false));
            assertEquals(0, InArraySearch.getIndexAtLeast(2.0d, new double[]{2.0d}, false));
            assertEquals(0, InArraySearch.getIndexAtLeast(2.0d, new double[]{2.0d, 2.0d}, false));
        }
    }

    /**
     * Test #3.
     */
    @Test
    void getIndexAtLeast3_Strict()
    {
        {
            double[] array = new double[]{
                    0.12, 0.21, 0.23, 0.24, 0.25,
                    0.25, 0.26, 0.27, 0.32, 0.34,
                    0.42, 0.43, 0.43, 0.44, 0.51,
                    0.52, 0.53, 0.55, 0.67, 0.72,
                    0.76, 0.76, 0.92, 0.95, 0.97,
                    1.0d, 1.0d};

            assertEquals(0, InArraySearch.getIndexAtLeast(0.0d, array, true));
            assertEquals(27, InArraySearch.getIndexAtLeast(2.0d, array, true));
            assertEquals(25, InArraySearch.getIndexAtLeast(0.99d, array, true));
            assertEquals(27, InArraySearch.getIndexAtLeast(1.0d, array, true));

            double[] th = new double[]{
                    0.11d, 0.21d, 0.211, 0.23, 0.25,
                    0.24, 0.251, 0.269, 0.271, 0.33,
                    0.41, 0.421, 0.435, 0.45, 0.515,
                    0.521, 0.55, 0.5500001, 0.71, 0.76,
                    0.76000001, 0.94, 0.96, 0.99, 1.0000001
            };
            int[] expI = new int[]{0, 2, 2, 3, 6, 4, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 18, 18, 19, 22, 22, 23,
                    24, 25, 27};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], InArraySearch.getIndexAtLeast(th[i], array, true));
            th = new double[]{0.0d, 1.0d, 0.12d, 0.121d, 0.119, 0.22};
            expI = new int[]{-1, 26, 0, 0, -1, 1};
            for (int i = 0; i < th.length; i++) assertEquals(expI[i], InArraySearch.getIndexAtMost(th[i], array, false));
        }

        {
            assertEquals(0, InArraySearch.getIndexAtLeast(2.0d, new double[]{}, true));
            assertEquals(1, InArraySearch.getIndexAtLeast(2.0d, new double[]{0.0d}, true));
            assertEquals(1, InArraySearch.getIndexAtLeast(2.0d, new double[]{2.0d}, true));
            assertEquals(2, InArraySearch.getIndexAtLeast(2.0d, new double[]{2.0d, 2.0d}, true));
        }
    }

    /**
     * Test #4.
     */
    @Test
    void getIndexAtMost4()
    {
        {
            double[] array = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1, 1};

            double[] th = new double[]{0.0d, 1.0d, 0.12d, 0.121d, 0.119, 0.22, 0.23, 0.241, 0.25, 0.26, 0.319999, 0.320001, 0.35, 0.423,
                    0.431, 0.445, 0.511, 0.52, 0.531, 0.556, 0.671, 0.721, 0.761, 0.921, 0.95, 0.97, 1};
            int[] expI = new int[]{-1, 26, 0, 0, -1, 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 26};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], InArraySearch.getIndexAtMost(th[i], array, false));
        }

        {
            assertEquals(-1, InArraySearch.getIndexAtMost(2.0d, new double[]{}, false));
            assertEquals(-1, InArraySearch.getIndexAtMost(2.0d, new double[]{3.0d}, false));
            assertEquals(0, InArraySearch.getIndexAtMost(2.0d, new double[]{2.0d}, false));
            assertEquals(1, InArraySearch.getIndexAtMost(2.0d, new double[]{2.0d, 2.0d}, false));
        }
    }


    /**
     * Test #5.
     */
    @Test
    void getIndexAtMost5_Strict()
    {
        {
            double[] array = new double[]{
                    0.12, 0.21, 0.23, 0.24, 0.25,
                    0.25, 0.26, 0.27, 0.32, 0.34,
                    0.42, 0.43, 0.43, 0.44, 0.51,
                    0.52, 0.53, 0.55, 0.67, 0.72,
                    0.76, 0.76, 0.92, 0.95, 0.97,
                    1.0d, 1.0d};

            double[] th = new double[]{
                    0.0d, 1.0d, 0.12d, 0.121d, 0.119,
                    0.22, 0.23, 0.241, 0.25, 0.26,
                    0.319999, 0.320001, 0.35, 0.423, 0.431,
                    0.445, 0.511, 0.52, 0.531, 0.556,
                    0.671, 0.721, 0.761, 0.921, 0.95,
                    0.97, 1};
            int[] expI = new int[]{
                    -1, 24, -1, 0, -1,
                    1, 1, 3, 3, 5,
                    7, 8, 9, 10, 12,
                    13, 14, 14, 16, 17,
                    18, 19, 21, 22, 22, 23, 24};
            for (int i = 0; i < th.length; i++) assertEquals(expI[i], InArraySearch.getIndexAtMost(th[i], array, true));
        }

        {
            assertEquals(-1, InArraySearch.getIndexAtMost(2.0d, new double[]{}, true));
            assertEquals(-1, InArraySearch.getIndexAtMost(2.0d, new double[]{3.0d}, true));
            assertEquals(-1, InArraySearch.getIndexAtMost(2.0d, new double[]{2.0d}, true));
            assertEquals(-1, InArraySearch.getIndexAtMost(2.0d, new double[]{2.0d, 2.0d}, true));
        }
    }

    /**
     * Test #6.
     */
    @Test
    void getIndexAtLeas6()
    {
        {
            double[] array = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1, 1};
            ArrayList<DoubleWrapper> VG = new ArrayList<>();
            for (double v : array) VG.add(new DoubleWrapper(v));

            InArraySearch<DoubleWrapper> IAS = new InArraySearch<>();
            assertEquals(0, IAS.getIndexAtLeast(0.0d, VG, false));
            assertEquals(27, IAS.getIndexAtLeast(2.0d, VG, false));
            assertEquals(25, IAS.getIndexAtLeast(0.99d, VG, false));
            assertEquals(25, IAS.getIndexAtLeast(1.0d, VG, false));

            double[] th = new double[]{0.11d, 0.21d, 0.211, 0.23, 0.25, 0.24, 0.251, 0.269,
                    0.271, 0.33, 0.41, 0.421, 0.435, 0.45, 0.515, 0.521, 0.55, 0.5500001, 0.71, 0.76, 0.76000001, 0.94, 0.96, 0.99, 1.0000001};
            int[] expI = new int[]{0, 1, 2, 2, 4, 3, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 27};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], IAS.getIndexAtLeast(th[i], VG, false));

            th = new double[]{0.0d, 1.0d, 0.12d, 0.121d, 0.119, 0.22};
            expI = new int[]{-1, 26, 0, 0, -1, 1};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], IAS.getIndexAtMost(th[i], VG, false));
        }

        {
            InArraySearch<DoubleWrapper> IAS = new InArraySearch<>();
            assertEquals(-1, IAS.getIndexAtLeast(2.0d, new ArrayList<>(), false));

            ArrayList<DoubleWrapper> VG = new ArrayList<>();
            VG.add(new DoubleWrapper(0.0d));
            assertEquals(1, IAS.getIndexAtLeast(2.0d, VG, false));
            VG = new ArrayList<>();
            VG.add(new DoubleWrapper(2.0d));
            assertEquals(0, IAS.getIndexAtLeast(2.0d, VG, false));
            VG = new ArrayList<>();
            VG.add(new DoubleWrapper(2.0d));
            VG.add(new DoubleWrapper(2.0d));
            assertEquals(0, IAS.getIndexAtLeast(2.0d, VG, false));
        }
    }


    /**
     * Test #7.
     */
    @Test
    void getIndexAtMost7()
    {
        {
            double[] array = new double[]{0.12, 0.21, 0.23, 0.24, 0.25, 0.25, 0.26, 0.27, 0.32, 0.34, 0.42, 0.43, 0.43, 0.44, 0.51, 0.52, 0.53, 0.55, 0.67, 0.72, 0.76, 0.76, 0.92, 0.95, 0.97, 1, 1};
            ArrayList<DoubleWrapper> VG = new ArrayList<>();
            for (double v : array) VG.add(new DoubleWrapper(v));
            InArraySearch<DoubleWrapper> IAS = new InArraySearch<>();

            double[] th = new double[]{0.0d, 1.0d, 0.12d, 0.121d, 0.119, 0.22, 0.23, 0.241, 0.25, 0.26, 0.319999, 0.320001, 0.35, 0.423,
                    0.431, 0.445, 0.511, 0.52, 0.531, 0.556, 0.671, 0.721, 0.761, 0.921, 0.95, 0.97, 1};
            int[] expI = new int[]{-1, 26, 0, 0, -1, 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 23, 24, 26};
            for (int i = 0; i < th.length; i++)
                assertEquals(expI[i], IAS.getIndexAtMost(th[i], VG, false));
        }

        {
            InArraySearch<DoubleWrapper> IAS = new InArraySearch<>();

            assertEquals(-1, IAS.getIndexAtMost(2.0d, new ArrayList<>(), false));

            ArrayList<DoubleWrapper> VG = new ArrayList<>();
            VG.add(new DoubleWrapper(3.0d));

            assertEquals(-1, IAS.getIndexAtMost(2.0d, VG, false));

            VG = new ArrayList<>();
            VG.add(new DoubleWrapper(2.0d));
            assertEquals(0, IAS.getIndexAtMost(2.0d, VG, false));

            VG = new ArrayList<>();
            VG.add(new DoubleWrapper(2.0d));
            VG.add(new DoubleWrapper(2.0d));
            assertEquals(1, IAS.getIndexAtMost(2.0d, VG, false));
        }
    }

}