package statistics;

import space.Range;

/**
 * Calculates max from samples that are supposed to be in a specified range (samples out of the range are ignored).
 *
 * @author MTomczyk
 */
public class MaxInRange extends AbstractInRangeStatistic implements IStatistic
{
    /**
     * Parameterized constructor.
     *
     * @param range range for valid samples
     */
    public MaxInRange(Range range)
    {
        super("MAX_IN_RANGE", range);
    }

    /**
     * Calculates the statistic. Returns 0 if no data is provided or all samples are not valid (not in the range).
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        double max = Double.NEGATIVE_INFINITY;
        for (double s : v)
        {
            if (_range.isInRange(s, true))
                if (Double.compare(s, max) > 0) max = s;
        }

        if (Double.compare(max, Double.NEGATIVE_INFINITY) == 0) return 0.0f;
        else return max;
    }

}
