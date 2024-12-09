package statistics;

/**
 * Calculates variance value from doubles.
 *
 * @author MTomczyk
 */
public class Variance extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "VAR";

    /**
     * If true, unbiased estimator is used; false otherwise.
     */
    private final boolean _unbiased;


    /**
     * Mean statistics.
     */
    private final IStatistic _mean;

    /**
     * Default constructor.
     */
    public Variance()
    {
        this(true);
    }

    /**
     * Default constructor.
     *
     * @param unbiased unbiased estimator is used
     */
    public Variance(boolean unbiased)
    {
        super(_name);
        _unbiased = unbiased;
        _mean = new Mean();
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
        if (v == null) return 0;
        if (v.length < 2) return 0.0d;
        double mean = _mean.calculate(v);
        double dev = 0.0d;
        for (double d : v) dev += Math.pow(d - mean, 2.0d);
        if (_unbiased) return dev / (double) (v.length - 1);
        else return dev / (double) v.length;
    }

    /**
     * Auxiliary method for retrieving the string representation ("VAR").
     *
     * @return string representation ("VAR")
     */
    public String getName()
    {
        return toString();
    }

}
