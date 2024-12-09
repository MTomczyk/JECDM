package scenario;

/**
 * Represents a single realization of the key-values mapping ({@link KeyValues}), e.g., "PROBLEM":"DTLZ2".
 *
 * @author MTomczyk
 */
public class KeyValue
{
    /**
     * Considered key.
     */
    private final Key _key;

    /**
     * Associated value.
     */

    private final Value _value;

    /**
     * Parameterized constructor.
     *
     * @param key   considered key
     * @param value associated value
     */
    public KeyValue(Key key, Value value)
    {
        _key = key;
        _value = value;
    }

    /**
     * Getter for the key.
     *
     * @return the key
     */
    public Key getKey()
    {
        return _key;
    }

    /**
     * Getter for the value.
     *
     * @return the value
     */
    public Value getValue()
    {
        return _value;
    }

    /**
     * Returns the string representation (delegated to the key object).
     *
     * @return string representation.
     */
    @Override
    public String toString()
    {
        return _key.getLabel() + "_" + _value.toString();
    }

    /**
     * Getter for the string representation (abbreviated).
     *
     * @return string representation (abbreviated)
     */
    public String getStringRepresentationAbbreviated()
    {
        return _key.getAbbreviation() + "_" + _value.toString();
    }

    /**
     * Checks whether the key-value is equal to other key-value (based on the comparison of keys and values).
     *
     * @param o the other object
     * @return true = objects are equal (their names); false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof KeyValue okv)) return false;
        if (!_key.equals(okv._key)) return false;
        return _value.equals(okv._value);
    }

    /**
     * Delegates the return of the hash code to the key object.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode()
    {
        return _key.hashCode();
    }
}
