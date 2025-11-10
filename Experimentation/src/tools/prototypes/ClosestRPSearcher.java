package tools.prototypes;

import combinatorics.Possibilities;
import print.PrintUtils;
import space.Range;
import space.distance.Euclidean;
import space.distance.IDistance;
import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * PROTOTYPE: Not recommended for use at this time. This class helps find the closest reference point (a priori
 * provided) to the input one efficiently. Note that the data is assumed to be already normalized. Essentially, the
 * object constructs a tree-like structure based on the input reference points when it is created. The nodes are
 * considered subspaces of the original space, attained by slicing each dimension a specified number of times. The
 * slicing is repeated for each node recursively (but without using recursion). Empty nodes are skipped and are not a
 * part of the tree. Also, the subdivision level is controlled by specifying the upper bound for the number of points
 * that fall into these constructed buckets. Searching is performed using a tree-searching method implemented as a
 * branch-and-bound approach. IMPORTANT: This implementation explicitly assumes that the distance used is the standard
 * Euclidean function. Its properties are exploited to bound the search suitably.
 *
 * @author MTomczyk
 */
public class ClosestRPSearcher
{
    /**
     * Bucket class.
     */
    protected static class Bucket
    {
        /**
         * Parent bucket.
         */
        public Bucket _parent;

        /**
         * Children bucket.
         */
        public LinkedList<Bucket> _children;

        /**
         * Bucket span.
         */
        public double[][] _bSpan;

        /**
         * Reference points span.
         */
        public double[][] _rpsSpan;

        /**
         * Reference points.
         */
        public double[][] _rps;

        /**
         * Bucket id.
         */
        public int _id;

        /**
         * Auxiliary print method.
         */
        public void print()
        {
            System.out.println(_id);
            PrintUtils.print2dDoubles(_bSpan, 2);
        }
    }

    /**
     * All reference points.
     */
    protected final double[][] _rps;

    /**
     * Space dimensionality.
     */
    protected final int _M;

    /**
     * The number of divisions per level (at least 2).
     */
    protected final int _div;

    /**
     * Dimension bounds.
     */
    protected final Range[] _ranges;

    /**
     * Max RPs in a bucket (if a bucket contains more RPs, it will be subdivided)
     */
    protected final int _maxRPsInBucket;

    /**
     * No buckets.
     */
    protected int _noBuckets;

    /**
     * Euclidean distance.
     */
    protected final IDistance _d = new Euclidean();

    /**
     * Root buckets.
     */
    protected LinkedList<Bucket> _rootBuckets;

    /**
     * Auxiliary root.
     */
    protected Bucket _root;

    /**
     * Auxiliary Cartesian product pointing to subdivisions.
     */
    protected int[][] _cp;

    /**
     * Parameterized constructor.
     *
     * @param M              space dimensionality
     * @param div            the number of divisions per level (at least 2)
     * @param ranges         dimension bounds
     * @param maxRPsInBucket max RPs in a bucket (if a bucket contains more RPs, it will be subdivided)
     * @param rps            all reference points
     */
    public ClosestRPSearcher(int M, int div, Range[] ranges, int maxRPsInBucket, double[][] rps)
    {
        _M = M;
        _div = div;
        _maxRPsInBucket = maxRPsInBucket;
        _rps = rps;
        _ranges = ranges;
        _noBuckets = 0;
        constructData();
    }

    /**
     * Constructs the heavy data.
     */
    private void constructData()
    {
        // Prepare CP
        _cp = Possibilities.generateCartesianProduct(ArrayUtils.getIntArray(_M, _div));

        _noBuckets = 1;

        // Create parent root
        _root = new Bucket();
        _root._id = 0;
        _root._parent = null;
        _root._rps = _rps;
        _root._bSpan = new double[_M][2];
        _root._rpsSpan = new double[_M][2];
        for (int m = 0; m < _M; m++)
        {
            _root._bSpan[m][0] = _ranges[m].getLeft();
            _root._bSpan[m][1] = _ranges[m].getRight();
        }
        fillRPSanData(_root);

        // Prepare initial
        _rootBuckets = new LinkedList<>();

        LinkedList<Bucket> tobeSubdivided = new LinkedList<>();
        {
            LinkedList<Bucket> roots = subdivide(_root);
            for (Bucket b : roots)
            {
                b._id = _noBuckets;
                _noBuckets++;
                _rootBuckets.add(b);
                if (b._rps.length > _maxRPsInBucket) tobeSubdivided.add(b);
            }
        }
        _root._children = _rootBuckets;

        boolean process = true;
        while (process)
        {
            LinkedList<Bucket> nl = new LinkedList<>();
            for (Bucket b : tobeSubdivided)
            {
                LinkedList<Bucket> children = subdivide(b);
                b._children = children;
                for (Bucket c : children)
                {
                    c._id = _noBuckets;
                    _noBuckets++;
                    if (c._rps.length > _maxRPsInBucket) nl.add(c);
                }
            }
            tobeSubdivided = nl;
            if (tobeSubdivided.isEmpty()) process = false;
        }
    }

    /**
     * Auxiliary method filling the RPs span data (the data matrix must already be instantiated)
     *
     * @param bucket the bucket to be processed
     */
    protected static void fillRPSanData(Bucket bucket)
    {
        for (int m = 0; m < bucket._rpsSpan.length; m++)
        {
            bucket._rpsSpan[m][0] = Double.POSITIVE_INFINITY;
            bucket._rpsSpan[m][1] = Double.NEGATIVE_INFINITY;
        }

        for (double[] p : bucket._rps)
            for (int m = 0; m < p.length; m++)
            {
                if (Double.compare(p[m], bucket._rpsSpan[m][0]) < 0) bucket._rpsSpan[m][0] = p[m];
                if (Double.compare(p[m], bucket._rpsSpan[m][1]) > 0) bucket._rpsSpan[m][1] = p[m];
            }
    }

    /**
     * Subdivides the bucket.
     *
     * @param parent subdivision request
     * @return List of children non-empty buckets
     */
    private LinkedList<Bucket> subdivide(Bucket parent)
    {
        LinkedList<Bucket> newBuckets = new LinkedList<>();

        double[] l = new double[_M];
        double[] span = new double[_M];
        double[] d = new double[_M];

        for (int m = 0; m < _M; m++)
        {
            span[m] = parent._bSpan[m][1] - parent._bSpan[m][0];
            d[m] = span[m] / _div;
            l[m] = parent._bSpan[m][0];
        }

        boolean hasChildren = false;
        for (int[] s : _cp)
        {
            double[][] bSpan = new double[_M][2];

            for (int m = 0; m < _M; m++)
            {
                bSpan[m][0] = l[m] + d[m] * s[m];
                bSpan[m][1] = bSpan[m][0] + d[m];
            }

            ArrayList<double[]> rps;
            {
                rps = new ArrayList<>(parent._rps.length);
                for (double[] rp : parent._rps)
                    if (isWithin(rp, bSpan))
                        rps.add(rp);
            }
            if (rps.isEmpty()) continue;
            double[][] rRPS = new double[rps.size()][];
            for (int i = 0; i < rps.size(); i++) rRPS[i] = rps.get(i);

            hasChildren = true;
            Bucket bucket = new Bucket();
            bucket._rps = rRPS;
            bucket._bSpan = bSpan;
            bucket._parent = parent;
            bucket._rpsSpan = new double[_M][2];
            fillRPSanData(bucket);
            newBuckets.add(bucket);
        }

        if (hasChildren) parent._rps = null;
        return newBuckets;
    }


    /**
     * Finds the closest reference point using the binary search (BNB).
     *
     * @param p input point
     * @return closest reference point
     */
    public double[] findUsingBNB(double[] p)
    {
        double[] rp = null;
        double d = Double.POSITIVE_INFINITY;

        boolean fallsInBucket = false;

        Bucket fb = null;
        for (Bucket b : _rootBuckets)
        {
            if (isWithin(p, b._bSpan))
            {
                fallsInBucket = true;
                fb = b;
                break;
            }
        }

        // go down and seek
        Bucket mb = fb;
        if (fallsInBucket)
        {
            while ((mb._children != null) && (!mb._children.isEmpty()))
            {
                boolean passed = false;
                for (Bucket b : mb._children)
                    if (isWithin(p, b._bSpan))
                    {
                        mb = b;
                        passed = true;
                        break;
                    }

                if (!passed)
                {
                    while ((mb._children != null) && (!mb._children.isEmpty()))
                        mb = mb._children.getFirst();
                    break; // could no go down any further
                }
            }
        }

        if (fallsInBucket)
        {
            // initial full scan
            double[][] fs = findUsingFullScan(p, mb._rps);
            rp = fs[0]; // current incumbent
            d = fs[1][0];

            while (mb._parent != null) // go up
            {
                int skipID = mb._id;
                mb = mb._parent; // move up;

                // search nb
                for (Bucket b : mb._children)
                {
                    // DFS
                    if (b._id == skipID) continue; // do not seek
                    double bound = calculateMinPossibleDistance(p, b);
                    if (Double.compare(bound, d) >= 0) continue; // no way of improvement
                    // otherwise run scan

                    if (b._children == null) // laeves
                    {
                        double[][] t1 = findUsingFullScan(p, b._rps);
                        if (Double.compare(t1[1][0], d) < 0)
                        {
                            d = t1[1][0];
                            rp = t1[0];
                        }
                    }
                    else
                    {
                        LinkedList<Bucket> toSeek = new LinkedList<>(b._children);
                        while (!toSeek.isEmpty())
                        {
                            Bucket b2 = toSeek.pop();
                            //noinspection DuplicatedCode
                            if (b2._children == null) // laeves
                            {
                                double[][] t1 = findUsingFullScan(p, b2._rps);
                                if (Double.compare(t1[1][0], d) < 0)
                                {
                                    d = t1[1][0];
                                    rp = t1[0];
                                }
                            }
                            else toSeek.addAll(b2._children);
                        }
                    }
                }
            }
        }
        else
        {
            LinkedList<Bucket> toSeek = new LinkedList<>(_rootBuckets); // DFS
            while (!toSeek.isEmpty())
            {
                Bucket b = toSeek.pop();
                double bound = calculateMinPossibleDistance(p, b);
                if (Double.compare(bound, d) >= 0) continue; // no way of improvement
                //noinspection DuplicatedCode
                if (b._children == null) // leaf
                {
                    double[][] t1 = findUsingFullScan(p, b._rps);
                    if (Double.compare(t1[1][0], d) < 0)
                    {
                        d = t1[1][0];
                        rp = t1[0];
                    }
                }
                else toSeek.addAll(b._children);
            }

        }

        return rp;
    }


    /**
     * Calculates a minimal theoretical distance between the input point and the bucket's points. Assumption: the input
     * point is outside the bucket.
     *
     * @param p input point
     * @param b bucket
     * @return minimal theoretical distance (use to bound search)
     */
    protected static double calculateMinPossibleDistance(double[] p, Bucket b)
    {
        double d = 0.0d;

        for (int m = 0; m < p.length; m++)
        {
            int c1 = Double.compare(p[m], b._rpsSpan[m][0]);
            int c2 = Double.compare(p[m], b._rpsSpan[m][1]);
            if (c1 < 0) d += Math.pow(b._rpsSpan[m][0] - p[m], 2.0d);
            else if (c2 > 0) d += Math.pow(p[m] - b._rpsSpan[m][1], 2.0d);
            // otherwise = 0;
        }
        return Math.sqrt(d);
    }

    /**
     * Checks if the reference point is within the bounds.
     *
     * @param rp     the reference point
     * @param bounds the bounds
     * @return true, if the point is within the bounds; false otherwise
     */
    protected static boolean isWithin(double[] rp, double[][] bounds)
    {
        for (int m = 0; m < rp.length; m++)
        {
            if (Double.compare(rp[m], bounds[m][0]) < 0) return false;
            if (Double.compare(rp[m], bounds[m][1]) > 0) return false;
        }
        return true;
    }


    /**
     * Finds the closest reference point using a full scan.
     *
     * @param p input point
     * @return closest reference point
     */
    public double[] findUsingFullScan(double[] p)
    {
        return findUsingFullScan(p, _rps)[0];
    }

    /**
     * Finds the closest reference point using a full scan.
     *
     * @param p   input point
     * @param rps reference points
     * @return closest reference point
     */
    @SuppressWarnings("DuplicatedCode")
    private double[][] findUsingFullScan(double[] p, double[][] rps)
    {
        double[] c = null;
        double dist = Double.POSITIVE_INFINITY;
        for (double[] rp : rps)
        {
            double d = _d.getDistance(rp, p);
            if (Double.compare(d, dist) < 0)
            {
                dist = d;
                c = rp;
            }
        }
        return new double[][]{c, {dist}};
    }
}
