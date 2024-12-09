package search;

/**
 * Auxiliary class for objects that are supposed to return a double value (to be extended).
 *
 * @author MTomczyk
 */

public class ValueGetter
{
    /**
     * Value to be returned.
     */
    public double _value;

    /**
     * Parameterized constructor.
     * @param value stored value
     */
    public ValueGetter(double value)
    {
        _value = value;
    }
}
