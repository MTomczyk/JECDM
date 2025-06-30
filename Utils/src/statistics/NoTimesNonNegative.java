package statistics;

/**
 * Calculates no. times the samples take non-negative values
 *
 * @author MTomczyk
 */

public class NoTimesNonNegative extends AbstractStatistic implements IStatistic
{
    /**
     * Default constructor.
     */
    public NoTimesNonNegative()
    {
        super("NT_NN");
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
        for (double s : v) if (Double.compare(s, 0.0d) >= 0) st += 1.0d;
        return st;
    }
}
