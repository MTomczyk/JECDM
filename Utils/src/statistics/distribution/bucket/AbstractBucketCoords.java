package statistics.distribution.bucket;

import space.Range;

import java.util.Arrays;

/**
 * Abstract class for methods implementing ways of identifying buckets (space discretization).
 *
 * @author MTomczyk
 */

public class AbstractBucketCoords
{
    /**
     * The number of dimensions considered.
     */
    protected int _dim;

    /**
     * The number of divisions (discretization level) per dimension.
     */
    protected int[] _div;

    /**
     * Bounds for dimensions.
     */
    protected Range[] _r;

    /**
     * Bucket intervals (per dimension).
     */
    protected double[] _dv;

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     */
    public AbstractBucketCoords(int dim, int div)
    {
        int[] dv = new int[dim];
        Arrays.fill(dv, div);
        init(dim, dv, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     */
    public AbstractBucketCoords(int dim, int div, Range r)
    {
        int[] dv = new int[dim];
        Range[] ranges = new Range[dim];
        Arrays.fill(dv, div);
        Arrays.fill(ranges, r);
        init(dim, dv, ranges);
    }

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     */
    public AbstractBucketCoords(int dim, int[] div, Range[] r)
    {
        init(dim, div, r);
    }

    /**
     * Inits data.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     */
    protected void init(int dim, int[] div, Range[] r)
    {
        _dim = dim;
        _div = div;
        _r = r;
        calculateIntervals();
    }

    /**
     * Calculates buckets intervals.
     */
    public void calculateIntervals()
    {
        if (_r == null) return;
        _dv = new double[_dim];
        for (int i = 0; i < _dim; i++) _dv[i] = (_r[i].getInterval() / _div[i]);
    }

    /**
     * Returns bucket coordinates (default method to be overwritten).
     * @param p input data point
     * @return bucket coordinates (null if the point is outside the bounds or data is missing, e.g., bounds are not provided).
     */
    public int[] getBucketCoords(double[] p)
    {
       return null;
    }


    /**
     * Setter for the number of divisions (discretization level) per dimension.
     * @param div number of divisions (discretization level) per dimension
     */
    public void setDiv(int[] div)
    {
        _div = div;
    }


    /**
     * Setter for the number of divisions (discretization level) per dimension (the same value is set for all).
     * @param div number of divisions for all dimensions
     */
    public void setDiv(int div)
    {
        for (int i = 0; i < _dim; i++) _div[i] = div;
    }

    /**
     * Setter for the bounds for dimensions.
     *
     * @param r bounds for dimensions
     */
    public void setRanges(Range[] r)
    {
        _r = r;
    }
}
