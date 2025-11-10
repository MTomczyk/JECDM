package tools.prototypes;

import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.ReferencePointsFactory;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.distance.Euclidean;
import space.distance.IDistance;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ClosestRPSearcher}.
 *
 * @author MTomczyk
 */
class ClosestRPSearcherTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        {
            ClosestRPSearcher bs = new ClosestRPSearcher(2, 2,
                    Range.getDefaultRanges(2), 100, new double[][]{});
            TestUtils.assertEquals(new int[][]{
                    {0, 0}, {1, 0}, {0, 1}, {1, 1}
            }, bs._cp);
            assertEquals(1, bs._noBuckets);
        }
        {
            ClosestRPSearcher bs = new ClosestRPSearcher(2, 3,
                    Range.getDefaultRanges(2), 100, new double[][]{});
            TestUtils.assertEquals(new int[][]{
                    {0, 0}, {1, 0}, {2, 0}, {0, 1}, {1, 1}, {2, 1}, {0, 2}, {1, 2}, {2, 2},
            }, bs._cp);
            assertEquals(1, bs._noBuckets);
        }
        {
            ClosestRPSearcher bs = new ClosestRPSearcher(3, 3,
                    Range.getDefaultRanges(3), 100, new double[][]{});
            TestUtils.assertEquals(new int[][]{
                    {0, 0, 0}, {1, 0, 0}, {2, 0, 0},
                    {0, 1, 0}, {1, 1, 0}, {2, 1, 0},
                    {0, 2, 0}, {1, 2, 0}, {2, 2, 0},
                    {0, 0, 1}, {1, 0, 1}, {2, 0, 1},
                    {0, 1, 1}, {1, 1, 1}, {2, 1, 1},
                    {0, 2, 1}, {1, 2, 1}, {2, 2, 1},
                    {0, 0, 2}, {1, 0, 2}, {2, 0, 2},
                    {0, 1, 2}, {1, 1, 2}, {2, 1, 2},
                    {0, 2, 2}, {1, 2, 2}, {2, 2, 2},
            }, bs._cp);
            assertEquals(1, bs._noBuckets);
        }
    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        {
            ClosestRPSearcher bs = new ClosestRPSearcher(2, 2,
                    Range.getDefaultRanges(2), 100, new double[][]{
                    {0.4d, 0.1d},
                    {0.6d, 0.4d},
                    {0.8d, 0.8d},
                    {0.9d, 0.9d}
            });
            assertEquals(4, bs._noBuckets);
            assertEquals(3, bs._rootBuckets.size());
            assertNull(bs._root._parent);
            assertNotNull(bs._root._children);
            assertEquals(1, bs._root._children.getFirst()._id);
            assertEquals(2, bs._root._children.get(1)._id);
            assertEquals(3, bs._root._children.get(2)._id);
            {
                ClosestRPSearcher.Bucket b = bs._rootBuckets.getFirst();
                TestUtils.assertEquals(new double[][]{{0.0d, 0.5d}, {0.0d, 0.5d}}, b._bSpan, 1.0E-6);
                TestUtils.assertEquals(new double[][]{{0.4d, 0.4d}, {0.1d, 0.1d}}, b._rpsSpan, 1.0E-6);
                assertNull(b._children);
                assertNotNull(b._parent);
                assertEquals(0, b._parent._id);
                assertEquals(1, b._id);
                assertEquals(1, b._rps.length);
                TestUtils.assertEquals(new double[]{0.4d, 0.1d}, b._rps[0], 1.0E-6);
            }
            {
                ClosestRPSearcher.Bucket b = bs._rootBuckets.get(1);
                TestUtils.assertEquals(new double[][]{{0.5d, 1.0d}, {0.0d, 0.5d}}, b._bSpan, 1.0E-6);
                TestUtils.assertEquals(new double[][]{{0.6d, 0.6d}, {0.4d, 0.4d}}, b._rpsSpan, 1.0E-6);
                assertNull(b._children);
                assertNotNull(b._parent);
                assertEquals(0, b._parent._id);
                assertEquals(2, b._id);
                assertEquals(1, b._rps.length);
                TestUtils.assertEquals(new double[]{0.6d, 0.4d}, b._rps[0], 1.0E-6);
            }
            {
                ClosestRPSearcher.Bucket b = bs._rootBuckets.get(2);
                TestUtils.assertEquals(new double[][]{{0.5d, 1.0d}, {0.5d, 1.0d}}, b._bSpan, 1.0E-6);
                TestUtils.assertEquals(new double[][]{{0.8d, 0.9d}, {0.8d, 0.9d}}, b._rpsSpan, 1.0E-6);
                assertNull(b._children);
                assertNotNull(b._parent);
                assertEquals(0, b._parent._id);
                assertEquals(3, b._id);
                assertEquals(2, b._rps.length);
                TestUtils.assertEquals(new double[]{0.8d, 0.8d}, b._rps[0], 1.0E-6);
                TestUtils.assertEquals(new double[]{0.9d, 0.9d}, b._rps[1], 1.0E-6);
            }
        }
        {
            ClosestRPSearcher bs = new ClosestRPSearcher(2, 2,
                    Range.getDefaultRanges(2), 1, new double[][]{
                    {0.4d, 0.1d},
                    {0.6d, 0.4d},
                    {0.8d, 0.8d},
                    {0.9d, 0.9d}
            });
            assertEquals(7, bs._noBuckets);
            assertEquals(3, bs._rootBuckets.size());
            assertNull(bs._root._parent);
            assertNotNull(bs._root._children);
            assertEquals(1, bs._root._children.getFirst()._id);
            assertEquals(2, bs._root._children.get(1)._id);
            assertEquals(3, bs._root._children.get(2)._id);

            {
                ClosestRPSearcher.Bucket b = bs._rootBuckets.getFirst();
                TestUtils.assertEquals(new double[][]{{0.0d, 0.5d}, {0.0d, 0.5d}}, b._bSpan, 1.0E-6);
                TestUtils.assertEquals(new double[][]{{0.4d, 0.4d}, {0.1d, 0.1d}}, b._rpsSpan, 1.0E-6);
                assertNull(b._children);
                assertNotNull(b._parent);
                assertEquals(0, b._parent._id);
                assertEquals(1, b._id);
                assertEquals(1, b._rps.length);
                TestUtils.assertEquals(new double[]{0.4d, 0.1d}, b._rps[0], 1.0E-6);
            }
            {
                ClosestRPSearcher.Bucket b = bs._rootBuckets.get(1);
                TestUtils.assertEquals(new double[][]{{0.5d, 1.0d}, {0.0d, 0.5d}}, b._bSpan, 1.0E-6);
                TestUtils.assertEquals(new double[][]{{0.6d, 0.6d}, {0.4d, 0.4d}}, b._rpsSpan, 1.0E-6);
                assertNull(b._children);
                assertNotNull(b._parent);
                assertEquals(0, b._parent._id);
                assertEquals(2, b._id);
                assertEquals(1, b._rps.length);
                TestUtils.assertEquals(new double[]{0.6d, 0.4d}, b._rps[0], 1.0E-6);
            }
            {
                ClosestRPSearcher.Bucket b = bs._rootBuckets.get(2);
                TestUtils.assertEquals(new double[][]{{0.5d, 1.0d}, {0.5d, 1.0d}}, b._bSpan, 1.0E-6);
                TestUtils.assertEquals(new double[][]{{0.8d, 0.9d}, {0.8d, 0.9d}}, b._rpsSpan, 1.0E-6);
                assertNotNull(b._parent);
                assertEquals(0, b._parent._id);
                assertNotNull(b._children);
                assertNull(b._rps);
                assertEquals(3, b._id);
                assertEquals(1, b._children.size());
                {
                    b = b._children.getFirst();
                    TestUtils.assertEquals(new double[][]{{0.75d, 1.0d}, {0.75d, 1.0d}}, b._bSpan, 1.0E-6);
                    TestUtils.assertEquals(new double[][]{{0.8d, 0.9d}, {0.8d, 0.9d}}, b._rpsSpan, 1.0E-6);
                    assertNotNull(b._parent);
                    assertNotNull(b._children);
                    assertNull(b._rps);
                    assertEquals(4, b._id);
                    assertEquals(3, b._parent._id);
                    assertEquals(2, b._children.size());
                    {
                        ClosestRPSearcher.Bucket b1 = b._children.getFirst();
                        TestUtils.assertEquals(new double[][]{{0.75d, 0.875d}, {0.75d, 0.875d}}, b1._bSpan, 1.0E-6);
                        TestUtils.assertEquals(new double[][]{{0.8d, 0.8d}, {0.8d, 0.8d}}, b1._rpsSpan, 1.0E-6);
                        assertNotNull(b1._parent);
                        assertNull(b1._children);
                        assertNotNull(b1._rps);
                        assertEquals(1, b1._rps.length);
                        assertEquals(5, b1._id);
                        assertEquals(4, b1._parent._id);
                        TestUtils.assertEquals(new double[]{0.8d, 0.8d}, b1._rps[0], 1.0E-6);
                    }
                    {
                        ClosestRPSearcher.Bucket b2 = b._children.get(1);
                        TestUtils.assertEquals(new double[][]{{0.875d, 1.0d}, {0.875d, 1.0d}}, b2._bSpan, 1.0E-6);
                        TestUtils.assertEquals(new double[][]{{0.9d, 0.9d}, {0.9d, 0.9d}}, b2._rpsSpan, 1.0E-6);
                        assertNotNull(b2._parent);
                        assertNull(b2._children);
                        assertNotNull(b2._rps);
                        assertEquals(1, b2._rps.length);
                        assertEquals(6, b2._id);
                        assertEquals(4, b2._parent._id);
                        TestUtils.assertEquals(new double[]{0.9d, 0.9d}, b2._rps[0], 1.0E-6);
                    }
                }
            }


            double[][] rps = new double[][]{
                    new double[]{0.25d, 0.25d},
                    new double[]{0.75d, 0.25d},
                    new double[]{0.75d, 0.75d},
                    new double[]{0.25d, 0.75d},
                    new double[]{0.49d, 0.99d},
                    new double[]{0.126126126, 0.499499499}
            };
            double[] exp = new double[]
                    {
                            Math.sqrt(Math.pow(0.4d - 0.25d, 2.0d) + Math.pow(0.25d - 0.1d, 2.0d)),
                            Math.sqrt(Math.pow(0.75d - 0.6d, 2.0d) + Math.pow(0.4d - 0.25d, 2.0d)),
                            Math.sqrt(Math.pow(0.8d - 0.75d, 2.0d) + Math.pow(0.8d - 0.75d, 2.0d)),
                            Math.sqrt(Math.pow(0.6d - 0.25d, 2.0d) + Math.pow(0.75d - 0.4d, 2.0d)),
                            Math.sqrt(Math.pow(0.8d - 0.49d, 2.0d) + Math.pow(0.99d - 0.8d, 2.0d)),
                            0.484207186
                    };

            IDistance d = new Euclidean();
            for (int i = 0; i < rps.length; i++)
            {
                double[] c = bs.findUsingBNB(rps[i]);
                assertNotNull(c);
                assertEquals(exp[i], d.getDistance(c, rps[i]), 1.0E-6);
            }

            int D = 1000;
            double dv = 1.0d / (D - 1);
            for (int i = 0; i < D; i++)
            {
                double x = i * dv;
                for (int j = 0; j < D; j++)
                {
                    double y = j * dv;
                    double[] p = new double[]{x, y};
                    double[] c1 = bs.findUsingFullScan(p);
                    double[] c2 = bs.findUsingBNB(p);
                    assertEquals(d.getDistance(p, c1), d.getDistance(p, c2), 1.0E-6);
                }
            }

        }
    }

    /**
     * Tests case 2 (infinity loop).
     */
    @Test
    public void testCase2()
    {
        ClosestRPSearcher bs = new ClosestRPSearcher(2, 2,
                Range.getDefaultRanges(2), 1, new double[][]{
                {0.4d, 0.1d},
                {0.6d, 0.4d},
                {0.8d, 0.8d},
                {0.9d, 0.9d}
        });
        IDistance d = new Euclidean();
        double[] c1 = bs.findUsingBNB(new double[]{0.51d, 0.51d});
        double[] c2 = bs.findUsingFullScan(new double[]{0.51d, 0.51d});
        assertEquals(d.getDistance(c1, new double[]{0.51d, 0.51d}),
                d.getDistance(c2, new double[]{0.51d, 0.51d}), 1.0E-6);
    }


    @Test
    public void testTime1()
    {
        ClosestRPSearcher bs = new ClosestRPSearcher(2, 2,
                Range.getDefaultRanges(2), 1, new double[][]{
                {0.4d, 0.1d},
                {0.6d, 0.4d},
                {0.8d, 0.8d},
                {0.9d, 0.9d}
        });

        IDistance d = new Euclidean();
        double t1 = 0;
        double t2 = 0;

        int D = 100;
        double dv = 1.0d / (D - 1);
        for (int i = 0; i < D; i++)
        {
            double x = i * dv;
            for (int j = 0; j < D; j++)
            {
                double y = j * dv;
                double[] p = new double[]{x, y};
                long t = System.nanoTime();
                double[] c1 = bs.findUsingFullScan(p);
                t1 += (System.nanoTime() - t) / 1000000.0d;
                t = System.nanoTime();
                double[] c2 = bs.findUsingBNB(p);
                t2 += (System.nanoTime() - t) / 1000000.0d;
                assertEquals(d.getDistance(p, c1), d.getDistance(p, c2), 1.0E-6);
            }
        }
        System.out.println("Time full = " + t1 + " ; vs bnb = " + t2);
    }

    /**
     * Tests {@link ClosestRPSearcher#calculateMinPossibleDistance(double[], ClosestRPSearcher.Bucket)}.
     */
    @Test
    public void testCalculateMinPossibleDistance()
    {
        {
            ClosestRPSearcher.Bucket b = new ClosestRPSearcher.Bucket();
            b._rpsSpan = new double[][]{{0.25d, 0.75d}, {0.2d, 0.4d}};
            assertEquals(0.25d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.0d, 0.2d}, b), 1.0E-6);
            assertEquals(0.1d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.15d, 0.2d}, b), 1.0E-6);
            assertEquals(0.25d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.0d, 0.4d}, b), 1.0E-6);
            assertEquals(0.5d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.25d, 0.9d}, b), 1.0E-6);
            assertEquals(0.5d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.75d, 0.9d}, b), 1.0E-6);
            assertEquals(0.3d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{1.05d, 0.4d}, b), 1.0E-6);
            assertEquals(0.3d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{1.05d, 0.2d}, b), 1.0E-6);
            assertEquals(0.1d, ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.75d, 0.1d}, b), 1.0E-6);
            assertEquals(Math.sqrt(2.0d), ClosestRPSearcher.calculateMinPossibleDistance(
                    new double[]{0.25d - 1.0d, 0.4d + 1.0d}, b), 1.0E-6);
        }
    }

    /**
     * Special case.
     */
    @Test
    public void test3()
    {
        ClosestRPSearcher bs = new ClosestRPSearcher(2, 2,
                Range.getDefaultRanges(2), 1, new double[][]{
                {0.4d, 0.1d},
                {0.6d, 0.4d},
                {0.8d, 0.8d},
                {0.9d, 0.9d}
        });
        double[] rp = new double[]{0.126126126, 0.499499499};
        double[] c1 = bs.findUsingBNB(rp);
        double[] c2 = bs.findUsingFullScan(rp);
        IDistance d = new Euclidean();
        double d1 = d.getDistance(c1, rp);
        double d2 = d.getDistance(c2, rp);
        assertEquals(d1, d2, 1.0E-6);
    }

    /**
     * Test 2.
     */
    @Test
    public void testComplex1()
    {
        // DTLZ2; 100000 points
        int T = 1000;
        IRandom R = new MersenneTwister64(0);
        IDistance D = new Euclidean();

        for (Problem problem : new Problem[]{Problem.DTLZ2, Problem.WFG1})
        {
            System.out.println("PROBLEM = " + problem);
            for (int pts : new int[]{100, 1000, 10000, 100000})
            {
                for (int M = 2; M < 6; M++)
                {
                    Range[] ranges = new Range[M];
                    if (problem.equals(Problem.WFG1))
                        for (int m = 0; m < M; m++) ranges[m] = new Range(0.0d, 2.0d * (m + 1));
                    else for (int m = 0; m < M; m++) ranges[m] = new Range();

                    for (int ib : new int[]{50, 100, 200})
                    {
                        double[][] rps = ReferencePointsFactory.getRandomReferencePoints(problem, pts, M, R);
                        long s = System.nanoTime();

                        ClosestRPSearcher bs = new ClosestRPSearcher(M, 2, ranges, ib, rps);
                        double ct = (System.nanoTime() - s) / 1000000.0d;
                        double fst = 0.0d;
                        double bnbt = 0.0d;

                        for (int t = 0; t < T; t++)
                        {
                            double[] p = R.nextDoubles(M);
                            if (problem.equals(Problem.WFG1))
                                for (int m = 0; m < M; m++) p[m] *= 2.0d;

                            s = System.nanoTime();
                            double[] fRP1 = bs.findUsingFullScan(p);
                            fst += (System.nanoTime() - s) / 1000000.0d;

                            s = System.nanoTime();
                            double[] fRP2 = bs.findUsingBNB(p);
                            bnbt += (System.nanoTime() - s) / 1000000.0d;

                            assertEquals(D.getDistance(p, fRP1), D.getDistance(p, fRP2), 1.0E-6);
                        }

                        System.out.println("pts = " + pts + "; M = " + M + "; ib = " + ib + "; div = " + 2 +
                                "; ct = " + ct + "; fst = " + fst + "; bnbt = " + bnbt + "; imp. ratio = " + fst / bnbt);
                    }
                }
            }
        }


    }

    /**
     * Test 2.
     */
    @Test
    public void testComplex2()
    {
        // DTLZ2; 100000 points
        int T = 1000;
        IRandom R = new MersenneTwister64(0);
        IDistance D = new Euclidean();

        for (Problem problem : new Problem[]{Problem.DTLZ2, Problem.WFG1})
        {
            System.out.println("PROBLEM = " + problem);
            for (int pts : new int[]{100, 1000, 10000, 100000})
            {
                for (int M = 2; M < 6; M++)
                {
                    double[][] toFindClosest = ReferencePointsFactory.getRandomReferencePoints(problem, T, M, R);
                    assertNotNull(toFindClosest);

                    Range[] ranges = new Range[M];
                    if (problem.equals(Problem.WFG1))
                        for (int m = 0; m < M; m++) ranges[m] = new Range(0.0d, 2.0d * (m + 1));
                    else for (int m = 0; m < M; m++) ranges[m] = new Range();

                    for (int ib : new int[]{50, 100, 200})
                    {
                        double[][] rps = ReferencePointsFactory.getRandomReferencePoints(problem, pts, M, R);
                        long s = System.nanoTime();
                        ClosestRPSearcher bs = new ClosestRPSearcher(M, 2, ranges, ib, rps);
                        double ct = (System.nanoTime() - s) / 1000000.0d;
                        double fst = 0.0d;
                        double bnbt = 0.0d;

                        for (int t = 0; t < T; t++)
                        {
                            s = System.nanoTime();
                            double[] fRP1 = bs.findUsingFullScan(toFindClosest[t]);
                            fst += (System.nanoTime() - s) / 1000000.0d;

                            s = System.nanoTime();
                            double[] fRP2 = bs.findUsingBNB(toFindClosest[t]);
                            bnbt += (System.nanoTime() - s) / 1000000.0d;

                            assertEquals(D.getDistance(toFindClosest[t], fRP1), D.getDistance(toFindClosest[t], fRP2), 1.0E-6);
                        }

                        System.out.println("pts = " + pts + "; M = " + M + "; ib = " + ib + "; div = " + 2 +
                                "; ct = " + ct + "; fst = " + fst + "; bnbt = " + bnbt + "; imp. ratio = " + fst / bnbt);
                    }
                }
            }
        }
    }
}