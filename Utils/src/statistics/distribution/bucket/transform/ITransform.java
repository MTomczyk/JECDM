package statistics.distribution.bucket.transform;

import space.Range;

/**
 * Interface for procedures explicitly transforming a double-like data point into an index (discretized interval).
 *
 * @author MTomczyk
 */

public interface ITransform
{
    /**
     * Returns the index.
     * @param p data point
     * @param r bound for the dimension
     * @param div the number of divisions (discretization level)
     * @return index
     */
    int getIdx(double p, Range r, int div);
}
