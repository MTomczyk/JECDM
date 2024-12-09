package statistics;

/**
 * Calculates standard deviance value from doubles.
 *
 * @author MTomczyk
 */
public class StandardDeviation extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "STD";
    /**
     * Mean statistics.
     */
    private final IStatistic _var;

    /**
     * Default constructor.
     */
    public StandardDeviation()
    {
        this(true);
    }

    /**
     * Default constructor.
     *
     * @param unbiased unbiased estimator is used
     */
    public StandardDeviation(boolean unbiased)
    {
        super(_name);
        _var = new Variance(unbiased);
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
        return Math.sqrt(_var.calculate(v));
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
