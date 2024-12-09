package statistics;

/**
 * Calculates the sum value of doubles.
 *
 * @author MTomczyk
 */

public class Sum extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "SUM";

    /**
     * Default constructor.
     */
    public Sum()
    {
        super(_name);
    }

    /**
     * Calculates the statistics (sum). Returns 0 if no data is provided.
     *
     * @param v input array
     * @return statistics
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double s = 0.0d;
        for (double c : v) s += c;
        return s;
    }
}
