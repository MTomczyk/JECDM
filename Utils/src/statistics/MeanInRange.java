package statistics;


import space.Range;

/**
 * Calculates mean from samples that are supposed to be in a specified range (samples out of the range are ignored).
 *
 * @author MTomczyk
 */
public class MeanInRange extends AbstractInRangeStatistic implements IStatistic
{
    /**
     * Parameterized constructor.
     *
     * @param range range for valid samples
     */
    public MeanInRange(Range range)
    {
        super("MEAN_IN_RANGE", range);
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
        if (cnt == 0) return 0.0d;
        else return sum / cnt;
    }
}
