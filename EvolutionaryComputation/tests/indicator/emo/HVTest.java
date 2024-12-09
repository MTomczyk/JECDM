package indicator.emo;

import org.junit.jupiter.api.Test;
import population.Specimen;
import print.PrintUtils;
import problem.Problem;
import problem.moo.ReferencePointsFactory;
import random.MersenneTwister64;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Various tests related to hypervolume calculation ({@link HV}).
 *
 * @author MTomczyk
 */
class HVTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        {
            double[][] S = new double[][]{{0.0d, 4.0d}, {2.0d, 2.0d}};
            HV hv = new HV(new HV.Params(2, null, new double[]{5.0d, 5.0d}));
            double[][] ls = hv.limitset(S, 0);
            PrintUtils.print2dDoubles(ls, 2);
            assertEquals(1, ls.length);
            assertEquals(2.0d, ls[0][0], 0.00001d);
            assertEquals(4.0d, ls[0][1], 0.00001d);
            double inc = hv.inclhv(ls[0]);
            System.out.println(inc);
            assertEquals(3.0d, inc, 0.00001d);
            inc = hv.inclhv(S[0]);
            System.out.println(inc);
            assertEquals(5.0d, inc, 0.00001d);
            System.out.println("=============================");
            double exc = hv.exclhv(S, 0);
            System.out.println(exc);
            assertEquals(exc, 2.0d, 0.0000001d);
        }
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        {
            double[][] S = new double[][]{{3.0d, 0.0d}, {2.0d, 2.0d}};
            HV hv = new HV(new HV.Params(2, null, new double[]{5.0d, 5.0d}));
            double[][] ls = hv.limitset(S, 0);
            PrintUtils.print2dDoubles(ls, 2);
            assertEquals(1, ls.length);
            assertEquals(3.0d, ls[0][0], 0.00001d);
            assertEquals(2.0d, ls[0][1], 0.00001d);
            double inc = hv.inclhv(ls[0]);
            System.out.println(inc);
            assertEquals(6.0d, inc, 0.00001d);
            inc = hv.inclhv(S[0]);
            System.out.println(inc);
            assertEquals(10.0d, inc, 0.00001d);
            System.out.println("=============================");
            double exc = hv.exclhv(S, 0);
            System.out.println(exc);
            assertEquals(exc, 4.0d, 0.0000001d);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        {
            double[][] S = new double[][]{{0.0d, 4.0d}, {2.0d, 2.0d}, {3.0d, 0.0d}};
            HV hv = new HV(new HV.Params(2, null, new double[]{5.0d, 5.0d}));
            assertEquals(2.0d, hv.exclhv(S, 0), 0.0000001d);
            assertEquals(3.0d, hv.exclhv(S, 1), 0.0000001d);
            assertEquals(10.0d, hv.exclhv(S, 2), 0.0000001d);
            assertEquals(15.0d, hv.wfg(S), 0.0000001d);
        }
    }


    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        {
            double[][] e = new double[][]{{1.0d}};
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(1, null, new double[]{1.0d}));
            double result = hv.evaluate(S);
            assertEquals(0.0d, result, 0.0000001d);
        }
        {
            INormalization[] n = new INormalization[]{new Linear()};
            double[][] e = new double[][]{{1.0d}};
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(1, n, new double[]{1.0d}));
            double result = hv.evaluate(S);
            assertEquals(0.0d, result, 0.0000001d);
        }
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        {
            double[][] e = new double[][]{{1.0d, 1.0d}};
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(2, null, new double[]{1.0d, 1.0d}));
            double result = hv.evaluate(S);
            assertEquals(0.0d, result, 0.0000001d);
        }
        {
            INormalization[] n = new INormalization[]{new Linear(), new Linear()};
            double[][] e = new double[][]{{1.0d, 1.0d}};
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(2, n, new double[]{1.0d, 1.0d}));
            double result = hv.evaluate(S);
            assertEquals(0.0d, result, 0.0000001d);
        }
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        {
            double[][] e = new double[][]{{0.5d, 0.5d}};
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(2, null, new double[]{1.0d, 1.0d}));
            double result = hv.evaluate(S);
            assertEquals(0.25d, result, 0.0000001d);
        }
        {
            INormalization[] n = new INormalization[]{new Linear(0.0d, 2.0d), new Linear(0.0d, 2.0d)};
            double[][] e = new double[][]{{1.0d, 1.0d}};
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(2, n, new double[]{1.0d, 1.0d}));
            double result = hv.evaluate(S);
            assertEquals(0.25d, result, 0.0000001d);
        }
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        for (int M = 2; M < 5; M++)
        {
            double[][] e = new double[1][M];
            for (int i = 0; i < M; i++) e[0][i] = 0.5d;
            double[] rp = new double[M];
            for (int i = 0; i < M; i++) rp[i] = 1.0d;
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(M, null, rp));
            double result = hv.evaluate(S);
            assertEquals(Math.pow(0.5d, M), result, 0.0000001d);
        }
        for (int M = 2; M < 5; M++)
        {
            INormalization[] n = new INormalization[M];
            for (int i = 0; i < M; i++) n[i] = new Linear(0.0d, 2.0d);
            double[][] e = new double[1][M];
            for (int i = 0; i < M; i++) e[0][i] = 1.0d;
            double[] rp = new double[M];
            for (int i = 0; i < M; i++) rp[i] = 1.0d;
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(M, n, rp));
            double result = hv.evaluate(S);
            assertEquals(Math.pow(0.5d, M), result, 0.0000001d);
        }

        for (int M = 2; M < 5; M++)
        {
            double[][] e = new double[1][M];
            for (int i = 0; i < M; i++) e[0][i] = 1.0d;
            double[] rp = new double[M];
            for (int i = 0; i < M; i++) rp[i] = 1.0d;
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(M, null, rp));
            double result = hv.evaluate(S);
            assertEquals(0.0d, result, 0.0000001d);
        }
        for (int M = 2; M < 5; M++)
        {
            INormalization[] n = new INormalization[M];
            for (int i = 0; i < M; i++) n[i] = new Linear(0.0d, 2.0d);
            double[][] e = new double[1][M];
            for (int i = 0; i < M; i++) e[0][i] = 2.0d;
            double[] rp = new double[M];
            for (int i = 0; i < M; i++) rp[i] = 1.0d;
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            HV hv = new HV(new HV.Params(M, n, rp));
            double result = hv.evaluate(S);
            assertEquals(0.0d, result, 0.0000001d);
        }
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        double[][] e = new double[][]
                {
                        {0.0d, 4.0d},
                        {2.0d, 2.0d},
                        {3.0d, 0.0d},
                };
        INormalization[] n = new INormalization[]{new Linear(0.0d, 5.0d), new Linear(0.0d, 5.0d)};
        HV hv = new HV(new HV.Params(2, n, new double[]{1.0d, 1.0d}));
        ArrayList<Specimen> S = new ArrayList<>(e.length);
        for (double[] s : e) S.add(new Specimen(s));
        double result = hv.evaluate(S);
        assertEquals(15.0d / 25.0d, result, 0.0000001d);
    }


    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        System.out.println("Experiment for 2D sphere");
        MersenneTwister64 R = new MersenneTwister64(0);

        for (int points : new int[]{1000, 2000, 5000, 10000})
        {

            long start = System.nanoTime();
            System.out.println(" - for points = " + points);
            //Family F = FamilyFactory.getPointLineProjections(2, divs);
            //System.out.println(" --- produced = " + F.getSize() + " points");
            double[][] rp = ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ2, points, 2, R);
            long dT = System.nanoTime() - start;
            System.out.println(" --- required time = " + (dT / 1000000000.0d));

            {
                System.out.println(" --- WITHOUT SORTING");
                start = System.nanoTime();
                assertNotNull(rp);
                HV.Params pHV = new HV.Params(2, null, new double[]{1.0d, 1.0d});
                pHV._presorting = false;
                HV hv = new HV(pHV);
                ArrayList<Specimen> S = new ArrayList<>(rp.length);
                for (double[] s : rp) S.add(new Specimen(s));
                double result = hv.evaluate(S);
                double expected = 1.0d - Math.PI / 4.0d;
                assertEquals(result, expected, 0.01d);
                System.out.println(" --- result vs expected = " + result + " " + expected);
                dT = System.nanoTime() - start;
                System.out.println(" --- time passed = " + (dT / 1000000000.0d));
            }
            {
                System.out.println(" --- WITH SORTING");
                start = System.nanoTime();
                assertNotNull(rp);
                HV.Params pHV = new HV.Params(2, null, new double[]{1.0d, 1.0d});
                pHV._presorting = true;
                HV hv = new HV(pHV);
                ArrayList<Specimen> S = new ArrayList<>(rp.length);
                for (double[] s : rp) S.add(new Specimen(s));
                double result = hv.evaluate(S);
                double expected = 1.0d - Math.PI / 4.0d;
                assertEquals(result, expected, 0.01d);
                System.out.println(" --- result vs expected = " + result + " " + expected);
                dT = System.nanoTime() - start;
                System.out.println(" --- time passed = " + (dT / 1000000000.0d));
            }
        }
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        System.out.println("Experiment for 3D sphere");
        MersenneTwister64 R = new MersenneTwister64(0);

        for (int points : new int[]{1000, 2000, 5000, 10000, 15000})
        {

            long start = System.nanoTime();
            System.out.println(" - for points = " + points);
            //Family F = FamilyFactory.getPointLineProjections(3, divs);
            //System.out.println(" --- produced = " + F.getSize() + " points");
            double[][] rp = ReferencePointsFactory.getRandomReferencePoints(Problem.DTLZ2, points, 3, R);
            long dT = System.nanoTime() - start;
            System.out.println(" --- required time = " + (dT / 1000000000.0d));

            {
                System.out.println(" --- WITHOUT SORTING");
                start = System.nanoTime();
                assertNotNull(rp);
                HV.Params pHV = new HV.Params(3, null, new double[]{1.0d, 1.0d, 1.0d});
                pHV._presorting = false;
                HV hv = new HV(pHV);
                ArrayList<Specimen> S = new ArrayList<>(rp.length);
                for (double[] s : rp) S.add(new Specimen(s));
                double result = hv.evaluate(S);
                double expected = 1.0d - (4.0d / 3.0d * Math.PI / 8.0d);
                if (points > 10000) assertEquals(result, expected, 0.01d);
                System.out.println(" --- result vs expected = " + result + " " + expected);
                dT = System.nanoTime() - start;
                System.out.println(" --- time passed = " + (dT / 1000000000.0d));
            }
            {
                System.out.println(" --- WITH SORTING");
                start = System.nanoTime();
                assertNotNull(rp);
                HV.Params pHV = new HV.Params(3, null, new double[]{1.0d, 1.0d, 1.0d});
                pHV._presorting = true;
                HV hv = new HV(pHV);
                ArrayList<Specimen> S = new ArrayList<>(rp.length);
                for (double[] s : rp) S.add(new Specimen(s));
                double result = hv.evaluate(S);
                double expected = 1.0d - (4.0d / 3.0d * Math.PI / 8.0d);
                if (points > 10000) assertEquals(result, expected, 0.01d);
                System.out.println(" --- result vs expected = " + result + " " + expected);
                dT = System.nanoTime() - start;
                System.out.println(" --- time passed = " + (dT / 1000000000.0d));
            }
        }
    }

    /**
     * Test 11.
     */
    @Test
    void test11()
    {

        double[][] e = new double[][]
                {
                        {2.0d, 5.0d},
                        {1.0d, 4.0d},
                        {3.0d, 4.0d},
                        {5.0d, 5.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {5.0d, 0.0d},
                        {5.0d, 2.0d},

                        {0.0d, 4.0d},
                        {2.0d, 2.0d},
                        {3.0d, 0.0d},
                };
        {
            INormalization[] n = new INormalization[]{new Linear(0.0d, 5.0d), new Linear(0.0d, 5.0d)};
            HV hv = new HV(new HV.Params(2, n, new double[]{1.0d, 1.0d}));
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            double result = hv.evaluate(S);
            assertEquals(15.0d / 25.0d, result, 0.0000001d);
        }
        {
            INormalization[] n = new INormalization[]{new Linear(0.0d, 5.0d), new Linear(0.0d, 5.0d)};
            HV hv = new HV(new HV.Params(2, n, new double[]{1.0d, 1.0d}));
            ArrayList<Specimen> S = new ArrayList<>(e.length);
            for (double[] s : e) S.add(new Specimen(s));
            double result = hv.evaluate(S);
            assertEquals(15.0d / 25.0d, result, 0.0000001d);
        }
    }


    /**
     * Test 12.
     */
    @Test
    void test12()
    {

        double[][] e = new double[][]
                {
                        {2.0d, 2.0d},
                };

        INormalization[] n = new INormalization[]{new Linear(0.0d, 1.0d), new Linear(0.0d, 1.0d)};
        double[] rp = new double[]{1.0d, 1.0d};
        HV.Params pHV = new HV.Params(2, n, rp);
        pHV._policyForNonDominating = HV.PolicyForNonDominating.NO_POLICY;
        HV hv = new HV(pHV);
        ArrayList<Specimen> S = new ArrayList<>(e.length);
        for (double[] s : e) S.add(new Specimen(s));
        assertEquals(1.0d, hv.evaluate(S), 0.0000001d);
    }

    /**
     * Test 13.
     */
    @Test
    void test13()
    {

        double[][] e = new double[][]
                {
                        {2.0d, 2.0d},
                };

        INormalization[] n = new INormalization[]{new Linear(0.0d, 1.0d), new Linear(0.0d, 1.0d)};
        double[] rp = new double[]{1.0d, 1.0d};
        HV.Params pHV = new HV.Params(2, n, rp);
        pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
        HV hv = new HV(pHV);
        ArrayList<Specimen> S = new ArrayList<>(e.length);
        for (double[] s : e) S.add(new Specimen(s));
        assertEquals(0.0d, hv.evaluate(S), 0.0000001d);
    }

    /**
     * Test 14.
     */
    @Test
    void test14()
    {

        double[][] e = new double[][]
                {
                        {0.0d, 0.8d},
                        {0.5d, 0.5d},
                        {0.9d, 1.0d},
                };

        INormalization[] n = new INormalization[]{new Linear(0.0d, 1.0d), new Linear(0.0d, 1.0d)};
        double[] rp = new double[]{0.7d, 0.7d};
        HV.Params pHV = new HV.Params(2, n, rp);
        pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
        HV hv = new HV(pHV);
        ArrayList<Specimen> S = new ArrayList<>(e.length);
        for (double[] s : e) S.add(new Specimen(s));
        assertEquals(0.04d, hv.evaluate(S), 0.0000001d);
    }

}