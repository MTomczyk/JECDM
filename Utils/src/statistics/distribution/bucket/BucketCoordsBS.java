package statistics.distribution.bucket;

import search.BinarySearcher;
import space.Range;

/**
 * Supportive class for determining buckets IDs for double-like data points (useful when, e.g., generating
 * distribution plots). Searching is done using the binary search (BS) method.
 *
 * @author MTomczyk
 */

public class BucketCoordsBS extends AbstractBucketCoords
{
    /**
     * Binary searcher used to determine bucket locations.
     */
    private final BinarySearcher _bs = new BinarySearcher(0, 1);

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     */
    public BucketCoordsBS(int dim, int div)
    {
        super(dim, div);
    }

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     */
    public BucketCoordsBS(int dim, int div, Range r)
    {
        super(dim, div, r);
    }

    /**
     * Parameterized constructor.
     *
     * @param dim the number of dimensions considered.
     * @param div the number of divisions (discretization level) per dimension.
     * @param r   bounds for dimensions.
     */
    public BucketCoordsBS(int dim, int[] div, Range[] r)
    {
        super(dim, div, r);
    }


    /**
     * Returns bucket coords (i.e., where the input double-like data point should be assigned to based on the assumed ranges for dimensions and discretization levels).
     * It is assumed that (i) returns null if a point is outside the bounds; (ii) the bucket intervals are left-side closed and right-side open, except for the last bucket that is closed at both ends.
     *
     * @param p input data point
     * @return bucket coords (null if the point is outside the bounds or data is missing, e.g., bounds are not provided).
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
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

            _bs.setData(0, _div[i] - 2);
            int idx;
            boolean again = true;
            int cnt = 0;

            do
            {
                idx = _bs.generateIndex();
                if ((Double.compare(p[i], _r[i].getLeft() + idx * _dv[i])) < 0) _bs.goLeft();
                else if ((Double.compare(p[i], _r[i].getLeft() + (idx + 1) * _dv[i])) >= 0) _bs.goRight();
                else again = false;
                cnt++;
            }
            while ((again) && (cnt <= _div[i]));
            _bs.generateIndex();
            c[i] = idx;
        }
        return c;
    }
}
