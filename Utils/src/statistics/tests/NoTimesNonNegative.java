package statistics.tests;

import statistics.AbstractStatistic;
import statistics.IStatistic;

/**
 * Calculates no. times the samples take non-negative values
 *
 * @author MTomczyk
 */

public class NoTimesNonNegative extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "NONN";

    /**
     * Default constructor.
     */
    public NoTimesNonNegative()
    {
        super(_name);
    }

    /**
     * Calculates the statistics (max). Returns 0 if no data is provided.
     *
     * @param v input array
     * @return statistics
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double st = 0.0d;
        for (double c : v) if (Double.compare(c, 0.0d) >= 0) st += 1.0d;
        return st;
    }

    /**
     * Auxiliary method for retrieving the string representation ("MAX").
     *
     * @return string representation ("MAX")
     */
    public String getName()
    {
        return toString();
    }

}
