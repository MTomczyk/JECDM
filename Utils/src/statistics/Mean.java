package statistics;

/**
 * Calculates mean value from doubles.
 *
 * @author MTomczyk
 */

public class Mean extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "MEAN";

    /**
     * Default constructor.
     */
    public Mean()
    {
        super(_name);
    }

    /**
     * Calculates the statistics (mean). Returns 0 if no data is provided.
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double s = 0.0d;
        for (double c : v) s += c;
        return s / v.length;
    }
}
