package statistics.distribution.bucket.transform;

import space.Range;

/**
 * Simple transformation based on calculating linear interpolation and executing proper thresholding.
 *
 * @author MTomczyk
 */

public class LinearlyThresholded implements ITransform
{
    /**
     * Constructs bucket ID using a linear interpolation
     * @param p data point
     * @param r bound for the dimension
     * @param div the number of divisions (discretization level)
     * @return constructed ID
     */
    @Override
    public int getIdx(double p, Range r, int div)
    {
        double np = (p - r.getLeft()) / r.getInterval();
        np *= div;
        return (int) (np);
    }
}
