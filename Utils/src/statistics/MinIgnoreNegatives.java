package statistics;

/**
 * Calculates min from non-negative samples.
 *
 * @author MTomczyk
 */
public class MinIgnoreNegatives extends AbstractStatistic implements IStatistic
{
    /**
     * Default constructor.
     */
    public MinIgnoreNegatives()
    {
        super("MIN_IN");
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
        double min = Double.POSITIVE_INFINITY;
        for (double s : v)
        {
            if (Double.compare(s, 0.0d) >= 0)
                if (Double.compare(s, min) < 0) min = s;
        }

        if (Double.compare(min, Double.POSITIVE_INFINITY) == 0) return 0.0f;
        else return min;
    }
}
