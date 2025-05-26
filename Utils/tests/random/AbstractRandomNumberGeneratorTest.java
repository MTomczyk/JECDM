package random;

import org.apache.commons.math4.legacy.stat.correlation.KendallsCorrelation;
import org.apache.commons.math4.legacy.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math4.legacy.stat.correlation.SpearmansCorrelation;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.jupiter.api.Test;
import print.PrintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test various RNG-related functionalities.
 *
 * @author MTomczyk
 */
class AbstractRandomNumberGeneratorTest
{
    /**
     * Examines random sources.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        for (RandomSource rs : RandomSource.values())
            System.out.println(rs + " " + rs.isJumpable() + " " + rs.isSplittable());
    }

    /**
     * Performs simple validations.
     */
    @Test
    void testValidations()
    {
        {
            MersenneTwister64 R = new MersenneTwister64(0);
            assertFalse(R.isJumpable());
            assertFalse(R.isSplittable());
            assertNull(R.createInstanceViaJump());
            assertNull(R.createInstancesViaJumps(10));
            assertNull(R.createSplitInstance());
            assertNull(R.createSplitInstances(10));
        }
        {
            MersenneTwister32 R = new MersenneTwister32(0);
            assertFalse(R.isJumpable());
            assertFalse(R.isSplittable());
            assertNull(R.createInstanceViaJump());
            assertNull(R.createInstancesViaJumps(10));
            assertNull(R.createSplitInstance());
            assertNull(R.createSplitInstances(10));
        }
        {
            PcgXshRR32 R = new PcgXshRR32(0);
            assertFalse(R.isJumpable());
            assertFalse(R.isSplittable());
            assertNull(R.createInstanceViaJump());
            assertNull(R.createInstancesViaJumps(10));
            assertNull(R.createSplitInstance());
            assertNull(R.createSplitInstances(10));
        }
        {
            L32_X64_MIX R = new L32_X64_MIX(0);
            assertTrue(R.isJumpable());
            assertTrue(R.isSplittable());
            IRandom R2 = R.createInstanceViaJump();
            assertNotNull(R2);
            assertInstanceOf(L32_X64_MIX.class, R2);
            Stream<IRandom> S = R.createInstancesViaJumps(10);
            assertNotNull(S);
            List<IRandom> L = S.toList();
            assertEquals(10, L.size());
            for (IRandom R3 : L) assertInstanceOf(L32_X64_MIX.class, R3);
            R2 = R.createSplitInstance();
            assertNotNull(R2);
            assertInstanceOf(L32_X64_MIX.class, R2);
            S = R.createSplitInstances(10);
            assertNotNull(S);
            L = S.toList();
            assertEquals(10, L.size());
            for (IRandom R3 : L) assertInstanceOf(L32_X64_MIX.class, R3);
        }
        {
            XoRoShiRo128PP R = new XoRoShiRo128PP(0);
            assertFalse(R.isSplittable());
            assertNull(R.createSplitInstance());
            assertNull(R.createSplitInstances(10));
            assertTrue(R.isJumpable());
            IRandom R2 = R.createInstanceViaJump();
            assertNotNull(R2);
            assertInstanceOf(XoRoShiRo128PP.class, R2);
            Stream<IRandom> S = R.createInstancesViaJumps(10);
            assertNotNull(S);
            List<IRandom> L = S.toList();
            assertEquals(10, L.size());
            for (IRandom R3 : L) assertInstanceOf(XoRoShiRo128PP.class, R3);
        }
    }

    /**
     * Performs simple performance verification (drawing doubles).
     */
    @Test
    void testPerformance()
    {
        int T = 100000000; // ^8
        IRandom[] Rs = new IRandom[]{
                new MersenneTwister32(System.currentTimeMillis()),
                new MersenneTwister64(System.currentTimeMillis()),
                new PcgXshRR32(System.currentTimeMillis()),
                new L32_X64_MIX(System.currentTimeMillis()),
                new XoRoShiRo128PP(System.currentTimeMillis())};
        System.out.println("Test doubles:");
        for (IRandom R : Rs)
        {
            long startTime = System.nanoTime();
            for (int t = 0; t < T; t++)
                R.nextDouble();
            System.out.println("Result for " + R + ": " + (double) (System.nanoTime() - startTime) / 1.0E6 + " ms");
        }
        System.out.println("Test booleans:");
        for (IRandom R : Rs)
        {
            long startTime = System.nanoTime();
            for (int t = 0; t < T; t++)
                R.nextBoolean();
            System.out.println("Result for " + R + ": " + (double) (System.nanoTime() - startTime) / 1.0E6 + " ms");
        }
        System.out.println("Test integers ([0, 10]:");
        for (IRandom R : Rs)
        {
            long startTime = System.nanoTime();
            for (int t = 0; t < T; t++)
                R.nextInt(10);
            System.out.println("Result for " + R + ": " + (double) (System.nanoTime() - startTime) / 1.0E6 + " ms");
        }
    }

    /**
     * Performs simple parallelization test.
     */
    @Test
    void testParallelization()
    {
        int threads = 4;

        IRandom R = new L32_X64_MIX(0);
        ArrayList<Stream<IRandom>> streams = new ArrayList<>();
        streams.add(R.createSplitInstances(threads));
        streams.add(R.createInstancesViaJumps(threads));
        R = new XoRoShiRo128PP(0);
        streams.add(R.createInstancesViaJumps(threads));

        for (Stream<IRandom> stream : streams)
        {
            int trials = 1000000;
            ArrayList<double[]> results = new ArrayList<>(threads);

            Lock lock = new ReentrantLock();

            CountDownLatch barrier = new CountDownLatch(threads);
            stream.forEach(iRandom -> ForkJoinPool.commonPool().execute(() -> {

                double[] d = new double[trials];
                for (int t = 0; t < trials; t++) d[t] = iRandom.nextDouble();
                lock.lock();
                results.add(d);
                lock.unlock();
                barrier.countDown();
            }));

            String msg = null;
            try
            {
                barrier.await();
            } catch (InterruptedException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            double[][] r = new double[threads][trials];
            for (int i = 0; i < threads; i++) r[i] = results.get(i);

            // validate
            for (int t1 = 0; t1 < threads; t1++)
                for (int t2 = 0; t2 < trials; t2++)
                {
                    assertTrue(Double.compare(r[t1][t2], 0.0d) >= 0);
                    assertTrue(Double.compare(r[t1][t2], 1.0d) <= 0);
                }

            // simple correlation test
            performCorrelationTests(r);
        }
    }

    /**
     * Tests getIntWithProbability and getIdxWithProbability methods.
     */
    @Test
    void getIntAndIdxWithProbability()
    {
        int trials = 10000000;
        IRandom[] Rs = new IRandom[]{
                new MersenneTwister32(System.currentTimeMillis()),
                new MersenneTwister64(System.currentTimeMillis()),
                new PcgXshRR32(System.currentTimeMillis()),
                new L32_X64_MIX(System.currentTimeMillis()),
                new XoRoShiRo128PP(System.currentTimeMillis())};

        for (IRandom R : Rs)
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

        for (IRandom R : Rs)
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

        for (IRandom R : Rs)
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

        for (IRandom R : Rs)
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

        for (IRandom R : Rs)
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

        for (IRandom R : Rs)
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


    /**
     * Performs (extremely) simplified correlation test.
     */
    @Test
    void simpleCorrelationTest()
    {
        int L = 5;
        int T = 1000000;


        for (int i = 0; i < 5; i++)
        {
            IRandom[] R = new IRandom[L];
            if (i == 0) for (int l = 0; l < L; l++) R[l] = new MersenneTwister32(l);
            else if (i == 1) for (int l = 0; l < L; l++) R[l] = new MersenneTwister64(l);
            else if (i == 2) for (int l = 0; l < L; l++) R[l] = new PcgXshRR32(l);
            else if (i == 3) for (int l = 0; l < L; l++) R[l] = new L32_X64_MIX(l);
            else for (int l = 0; l < L; l++) R[l] = new XoRoShiRo128PP(l);

            double[][] data = new double[L][T];
            for (int l = 0; l < L; l++)
                for (int t = 0; t < T; t++)
                    data[l][t] = R[l].nextDouble();
            performCorrelationTests(data);
        }
    }

    /**
     * Simple seed test #1.
     */
    @Test
    void seedTest1()
    {
        for (int i = 0; i < 5; i++)
        {
            IRandom R1;
            IRandom R2;

            if (i == 0)
            {
                R1 = new MersenneTwister32(0);
                R2 = new MersenneTwister32(1);
            } else if (i == 1)
            {
                R1 = new MersenneTwister64(0);
                R2 = new MersenneTwister64(1);
            } else if (i == 2)
            {
                R1 = new PcgXshRR32(0);
                R2 = new PcgXshRR32(1);
            } else if (i == 3)
            {
                R1 = new L32_X64_MIX(0);
                R2 = new L32_X64_MIX(1);
            } else
            {
                R1 = new XoRoShiRo128PP(0);
                R2 = new XoRoShiRo128PP(1);
            }

            int T = 10;
            double[][] r = new double[T][2];
            for (int t = 0; t < T; t++)
            {
                r[t][0] = R1.nextDouble();
                r[t][1] = R2.nextDouble();
                if (t > 1) assertNotEquals(r[t][1], r[t - 1][0], 1.0E-5);
            }
            PrintUtils.print2dDoubles(r, 2);
        }

    }

    /**
     * Simple seed test #2.
     */
    @Test
    void seedTest2()
    {
        for (int i = 0; i < 5; i++)
        {
            IRandom R1;
            IRandom R2;

            if (i == 0)
            {
                R1 = new MersenneTwister32(0);
                R2 = new MersenneTwister32(0);
            } else if (i == 1)
            {
                R1 = new MersenneTwister64(0);
                R2 = new MersenneTwister64(0);
            } else if (i == 2)
            {
                R1 = new PcgXshRR32(0);
                R2 = new PcgXshRR32(0);
            } else if (i == 3)
            {
                R1 = new L32_X64_MIX(0);
                R2 = new L32_X64_MIX(0);
            } else
            {
                R1 = new XoRoShiRo128PP(0);
                R2 = new XoRoShiRo128PP(0);
            }

            int T = 10;
            double[][] r = new double[T][2];
            for (int t = 0; t < T; t++)
            {
                r[t][0] = R1.nextDouble();
                r[t][1] = R2.nextDouble();
                assertEquals(r[t][0], r[t][1], 1.0E-5);
            }
            PrintUtils.print2dDoubles(r, 2);
        }
    }

    /**
     * Auxiliary method for performing simple correlation tests.
     *
     * @param data data to be checked
     */
    private static void performCorrelationTests(double[][] data)
    {
        PearsonsCorrelation PC = new PearsonsCorrelation();
        KendallsCorrelation KC = new KendallsCorrelation();
        SpearmansCorrelation SC = new SpearmansCorrelation();

        for (int l1 = 0; l1 < data.length; l1++)
            for (int l2 = 0; l2 < data.length; l2++)
                if (l1 != l2)
                {
                    double c = PC.correlation(data[l1], data[l2]);
                    System.out.println("PC: " + l1 + " " + l2 + " " + c);
                    assertTrue(Math.abs(c) < 1.0E-2);
                    c = KC.correlation(data[l1], data[l2]);
                    System.out.println("KC: " + l1 + " " + l2 + " " + c);
                    assertTrue(Math.abs(c) < 1.0E-2);
                    c = SC.correlation(data[l1], data[l2]);
                    System.out.println("KC: " + l1 + " " + l2 + " " + c);
                    assertTrue(Math.abs(c) < 1.0E-2);
                }
    }
}