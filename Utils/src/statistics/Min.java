package statistics;

/**
 * Calculates min value from doubles.
 *
 * @author MTomczyk
 */

public class Min extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "MIN";

    /**
     * Default constructor.
     */
    public Min()
    {
        super(_name);
    }

    /**
     * Calculates the statistics (min). Returns 0 if no data is provided.
     *
     * @param v input array
     * @return statistics
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double min = Double.POSITIVE_INFINITY;
        for (double c : v) if (c < min) min = c;
        return min;
    }
}
