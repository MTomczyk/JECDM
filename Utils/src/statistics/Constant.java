package statistics;

/**
 * Returns a constant (pre-defined) value.
 *
 * @author MTomczyk
 */

public class Constant extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "CONSTANT";

    /**
     * Pre-defined value.
     */
    private final double _value;

    /**
     * Parameterized constructor.
     *
     * @param value constant value
     */
    public Constant(double value)
    {
        super(_name);
        _value = value;
    }

    /**
     * Calculates the statistics (min). Returns 0 if no data is provided.
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        return _value;
    }
}
