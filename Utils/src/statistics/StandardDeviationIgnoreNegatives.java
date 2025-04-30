package statistics;

/**
 * Calculates mean from non-negative samples
 *
 * @author MTomczyk
 */
public class StandardDeviationIgnoreNegatives extends AbstractStatistic implements IStatistic
{
    /**
     * Default constructor.
     */
    public StandardDeviationIgnoreNegatives()
    {
        super("STD_IN");
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
        double sum = 0.0d;
        int cnt = 0;
        for (double s : v)
        {
            if (Double.compare(s, 0.0d) >= 0)
            {
                sum += s;
                cnt++;
            }
        }

        if (cnt <= 1) return 0.0d;
        double mean = sum / cnt;
        double stat = 0.0d;

        for (double s : v)
        {
            if (Double.compare(s, 0.0d) >= 0)
                stat += Math.pow(Math.abs(mean - s), 2.0d);
        }
        stat /= cnt;
        return Math.sqrt(stat);
    }
}
