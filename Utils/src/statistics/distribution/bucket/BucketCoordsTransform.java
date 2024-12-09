package statistics.distribution.bucket;

import space.Range;
import statistics.distribution.bucket.transform.ITransform;

/**
 * Supportive method for determining buckets IDs for double-like data points (useful when, e.g., generating
 * distribution plots). Indices are explicitly derived using a transformation procedure.
 *
 * @author MTomczyk
 */

public class BucketCoordsTransform extends AbstractBucketCoords
{
    /**
     * Object transforming data points into bucket IDs.
     */
    private final ITransform _T;

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param T   transformation function
     */
    public BucketCoordsTransform(int dim, int div, ITransform T)
    {
        super(dim, div);
        _T = T;
    }

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     * @param T   transformation function
     */
    public BucketCoordsTransform(int dim, int div, Range r, ITransform T)
    {
        super(dim, div, r);
        _T = T;
    }

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     * @param T   transformation function
     */
    public BucketCoordsTransform(int dim, int[] div, Range[] r, ITransform T)
    {
        super(dim, div, r);
        _T = T;
    }

    /**
     * Returns bucket coords (i.e., where the input double-like data point should be assigned to based on the assumed ranges for dimensions and discretization levels).
     * It is assumed that (i) returns null if a point is outside the bounds (left-side closed; right-side opened); (ii) the bucket intervals are left-side closed and right side open.
     *
     * @param p input data point
     * @return bucket coords (null if the point is outside the bounds or data is missing, e.g., bounds are not provided).
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    public int[] getBucketCoords(double[] p)
    {
        if (_r == null) return null;
        // check outside first
        for (int i = 0; i < _dim; i++)
        {
            if (Double.compare(p[i], _r[i].getLeft()) < 0) return null;
            if (Double.compare(p[i], _r[i].getRight()) >= 0) return null;
        }

        // is in bounds
        int[] c = new int[_dim];
        for (int i = 0; i < _dim; i++)
        {
            c[i] = _div[i] - 1;
            if (_div[i] == 1) continue;
            if (Double.compare(p[i], _r[i].getLeft() + _dv[i] * (_div[i] - 1)) >= 0)
            {
                c[i] = _div[i] - 1;
                continue;
            }
            c[i] = _T.getIdx(p[i], _r[i], _div[i]);
        }
        return c;
    }

}
