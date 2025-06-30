package statistics;

import space.Range;

/**
 * Calculates min from samples that are supposed to be in a specified range (samples out of the range are ignored).
 *
 * @author MTomczyk
 */
public class MinInRange extends AbstractInRangeStatistic implements IStatistic
{
    /**
     * Parameterized constructor.
     *
     * @param range range for valid samples
     */
    public MinInRange(Range range)
    {
        super("MIN_IN_RANGE", range);
    }

    /**
     * Calculates the statistic.Returns 0 if no data is provided or all samples are not valid (not in the range).
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        double min = Double.POSITIVE_INFINITY;
        for (double s : v)
        {
            if (_range.isInRange(s, true))
                if (Double.compare(s, min) < 0) min = s;
        }

        if (Double.compare(min, Double.POSITIVE_INFINITY) == 0) return 0.0f;
        else return min;
    }
}
