package statistics;

/**
 * Abstract implementation of {@link IStatistic}.
 *
 * @author MTomczyk
 */
public class AbstractStatistic implements IStatistic
{
    /**
     * Parameterized constructor.
     *
     * @param name string representation
     */
    public AbstractStatistic(String name)
    {
        _name = name.toUpperCase();
    }

    /**
     * String representation;
     */
    private final String _name;


    /**
     * Calculates the statistic.
     *
     * @param v input array
     * @return statistics
     */
    @Override
    public double calculate(double[] v)
    {
        return 0;
    }

    /**
     * Auxiliary method for retrieving the string representation.
     *
     * @return string representation.
     */
    public String getName()
    {
        return toString();
    }

    /**
     * Method for retrieving the string representation..
     *
     * @return string representation.
     */
    @Override
    public String toString()
    {
        return _name;
    }
}
