package statistics;

/**
 * Calculates max from non-negative samples.
 *
 * @author MTomczyk
 */
public class MaxIgnoreNegatives extends AbstractStatistic implements IStatistic
{
    /**
     * Default constructor.
     */
    public MaxIgnoreNegatives()
    {
        super("MAX_IN");
    }

    /**
     * Calculates the statistic. Returns 0 if no data is provided or all samples are negative.
     *
     * @param v input array
     * @return statistics
     */
    @Override
    public double calculate(double[] v)
    {
        double max = Double.NEGATIVE_INFINITY;
        for (double s : v)
        {
            if (Double.compare(s, 0.0d) >= 0)
                if (Double.compare(s, max) > 0) max = s;
        }

        if (Double.compare(max, Double.NEGATIVE_INFINITY) == 0) return 0.0f;
        else return max;
    }

}
