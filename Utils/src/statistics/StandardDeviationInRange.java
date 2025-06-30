package statistics;

import space.Range;

/**
 * Calculates mean from non-negative samples
 *
 * @author MTomczyk
 */
public class StandardDeviationInRange extends AbstractInRangeStatistic implements IStatistic
{
    /**
     * Parameterized constructor.
     *
     * @param range range for valid samples
     */
    public StandardDeviationInRange(Range range)
    {
        super("STD_IN_RANGE", range);
    }

    /**
     * Calculates the statistic. Returns 0 if no data is provided or all samples are negative.
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        double sum = 0.0d;
        int cnt = 0;
        for (double s : v)
        {
            if (_range.isInRange(s, true))
            {
                sum += s;
                cnt++;
            }
        }

        if (cnt <= 1) return 0.0d;
        double mean = sum / cnt;
        double stat = 0.0d;

        for (double s : v)
        {
            if (_range.isInRange(s, true))
                stat += Math.pow(Math.abs(mean - s), 2.0d);
        }
        stat /= cnt;
        return Math.sqrt(stat);
    }
}
