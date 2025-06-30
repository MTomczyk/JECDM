package statistics;


/**
 * Calculates mean from non-negative samples.
 *
 * @author MTomczyk
 */
public class MeanIgnoreNegatives extends AbstractStatistic implements IStatistic
{
    /**
     * Default constructor.
     */
    public MeanIgnoreNegatives()
    {
        super("MEAN_IN");
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
        if (cnt == 0) return 0.0d;
        else return sum / cnt;
    }
}
