package valuewrapper;

/**
 * Double container; can be extended to serve various purposes.
 *
 * @author MTomczyk
 */

public class DoubleWrapper
{
    /**
     * Stored value.
     */
    protected double _value;

    /**
     * Default constructor (sets value to 0.0d).
     */
    public DoubleWrapper()
    {
        this(0.0d);
    }

    /**
     * Parameterized constructor.
     *
     * @param value value to be stored
     */
    public DoubleWrapper(double value)
    {
        _value = value;
    }

    /**
     * Value getter.
     *
     * @return value
     */
    public double getValue()
    {
        return _value;
    }

    /**
     * Value setter.
     * @param value value
     */
    public void setValue(double value)
    {
        _value = value;
    }
}
