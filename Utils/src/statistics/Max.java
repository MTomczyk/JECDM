package statistics;

/**
 * Calculates max value from doubles.
 *
 * @author MTomczyk
 */

public class Max extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "MAX";

    /**
     * Default constructor.
     */
    public Max()
    {
        super(_name);
    }

    /**
     * Calculates the statistics (max). Returns 0 if no data is provided.
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double max = Double.NEGATIVE_INFINITY;
        for (double c : v) if (Double.compare(c, max) > 0) max = c;
        return max;
    }

    /**
     * Auxiliary method for retrieving the string representation ("MAX").
     *
     * @return string representation ("MAX")
     */
    @Override
    public String getName()
    {
        return toString();
    }
}
