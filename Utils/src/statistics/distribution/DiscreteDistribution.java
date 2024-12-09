package statistics.distribution;

import space.Range;

/**
 * Supportive class for building a discrete distribution (histogram) based on input data.
 *
 * @author MTomczyk
 */


public class DiscreteDistribution
{
    /**
     * Bounds for buckets.
     */
    private Range[] _ranges = null;

    /**
     * Histogram data.
     */
    private int[] _hist = null;

    /**
     * Number of samples.
     */
    private int _samples = 0;

    /**
     * Initializes buckets.
     *
     * @param buckets number of buckets for the histogram
     * @param lb      data lower bound
     * @param rb      data upper bound
     */
    public void init(int buckets, double lb, double rb)
    {
        assert rb > lb;
        assert buckets > 0;
        _samples = 0;
        double db = (rb - lb) / buckets;
        _hist = new int[buckets];
        _ranges = new Range[buckets];

        for (int i = 0; i < buckets; i++)
            _ranges[i] = new Range(lb + i * db, lb + (i + 1) * db);
    }

    /**
     * Adds value to the histogram.
     *
     * @param v input value
     */
    public void add(double v)
    {
        if (Double.compare(v, _ranges[0].getLeft()) < 0) return;
        if (Double.compare(v, _ranges[_ranges.length - 1].getRight()) > 0) return;

        for (int i = 0; i < _ranges.length; i++)
        {
            if ((i < _ranges.length - 1) && (Double.compare(v, _ranges[i].getRight()) >= 0)) continue;
            _hist[i]++;
            _samples++;
            break;
        }
    }

    /**
     * Returns the distribution.
     *
     * @return distribution data
     */
    public int[] getDistribution()
    {
        return _hist;
    }

    /**
     * Returns the normalized distribution.
     *
     * @return distribution distribution data
     */
    public double[] getNormalizedDistribution()
    {
        double[] norm = new double[_hist.length];
        for (int i = 0; i < _hist.length; i++) norm[i] = (double) _hist[i] / (double) _samples;
        return norm;
    }

    /**
     * Returns buckets' bounds.
     *
     * @return distribution data
     */
    public Range[] getBucketsBounds()
    {
        return _ranges;
    }

    /**
     * Returns the number of successful hits (samples).
     *
     * @return the number of successful hits (samples)
     */
    public int getNoSamples()
    {
        return _samples;
    }
}
