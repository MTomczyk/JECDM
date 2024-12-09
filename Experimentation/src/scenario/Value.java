package scenario;

/**
 * Represents a value associated with the key (e.g., key = "PROBLEM" and value = "DTLZ2").
 * Important note: it is assumed that the value label is stored in the upper case.
 * @author MTomczyk
 */
public class Value
{
    /**
     * Value (represented as string).
     */
    private final String _value;

    /**
     * If true, the specific value is disabled (excludes the scenario involving this key-value mapping from execution).
     */
    private final boolean _disabled;

    /**
     * Parameterized constructor.
     *
     * @param value    value (represented as string)
     * @param disabled if true, the specific value is disabled (excludes the scenario involving this key-value mapping from execution)
     */
    protected Value(String value, boolean disabled)
    {
        _value = value.toUpperCase();
        _disabled = disabled;
    }

    /**
     * Return the value (represented as string).
     *
     * @return value (represented as string)
     */
    public String getValue()
    {
        return _value;
    }

    /**
     * Can be called to check whether the value is disabled (excludes the scenario involving this key-value mapping from execution).
     *
     * @return true = the value is disabled; false otherwise
     */
    public boolean isDisabled()
    {
        return _disabled;
    }

    /**
     * Returns the value (represented as string).
     *
     * @return value (represented as string)
     */
    @Override
    public String toString()
    {
        return getValue();
    }

    /**
     * Checks whether the key is equal to other key (based on the names' comparison).
     *
     * @param o the other object
     * @return true = objects are equal (their names); false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Value ov)) return false;
        return _value.equals(ov.getValue());
    }

}
