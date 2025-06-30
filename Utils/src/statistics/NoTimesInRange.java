package statistics;

import space.Range;

/**
 * Calculates no. times the samples are in the specified range (samples out of the range are ignored).
 *
 * @author MTomczyk
 */

public class NoTimesInRange extends AbstractInRangeStatistic implements IStatistic
{
    /**
     * Parameterized constructor.
     *
     * @param range range for valid samples
     */
    public NoTimesInRange(Range range)
    {
        super("NT_IN_RANGE", range);
    }

    /**
     * Calculates the statistics. Returns 0 if no data is provided (or is valid).
     *
     * @param v input array
     * @return statistics
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double st = 0.0d;
        for (double s : v) if (_range.isInRange(s, true)) st += 1.0d;
        return st;
    }
}
