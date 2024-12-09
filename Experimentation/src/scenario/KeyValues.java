package scenario;

import exception.GlobalException;

import java.util.HashMap;

/**
 * Encapsulates a key (represents some feature related to the experiments) with its values/realizations, e.g.,
 * key = "PROBLEM" and values = "DTLZ1", "DTLZ2", etc.
 *
 * @author MTomczyk
 */
public class KeyValues
{
    /**
     * Allows retrieving the object instance encapsulating considered problems.
     *
     * @param values   key's realizations (each one will be converted to upper case)
     * @param disabled flags associated with values (1:1) mapping; if a flag is true, the linked value is disabled
     *                 (excludes the scenario involving this key-value mapping from execution); can be null ->
     *                 all values are enabled
     * @return object instance
     * @throws Exception the exception is thrown when the input data is invalid (e.g., nulls, empty strings, etc.)
     */
    public static KeyValues getInstanceForProblems(String[] values, boolean[] disabled) throws Exception
    {
        return getInstance(Keys.KEY_PROBLEM, Keys.KEY_PROBLEM_ABB, values, disabled);
    }

    /**
     * Allows retrieving the object instance encapsulating considered algorithms.
     *
     * @param values   key's realizations (each one will be converted to upper case)
     * @param disabled flags associated with values (1:1) mapping; if a flag is true, the linked value is disabled
     *                 (excludes the scenario involving this key-value mapping from execution); can be null ->
     *                 all values are enabled
     * @return object instance
     * @throws Exception the exception is thrown when the input data is invalid (e.g., nulls, empty strings, etc.)
     */
    public static KeyValues getInstanceForAlgorithms(String[] values, boolean[] disabled) throws Exception
    {
        return getInstance(Keys.KEY_ALGORITHM, Keys.KEY_ALGORITHM_ABB, values, disabled);
    }

    /**
     * Allows retrieving the object instance encapsulating considered objectives.
     *
     * @param values   key's realizations (each one will be converted to upper case)
     * @param disabled flags associated with values (1:1) mapping; if a flag is true, the linked value is disabled
     *                 (excludes the scenario involving this key-value mapping from execution); can be null ->
     *                 all values are enabled
     * @return object instance
     * @throws Exception the exception is thrown when the input data is invalid (e.g., nulls, empty strings, etc.)
     */
    public static KeyValues getInstanceForObjectives(String[] values, boolean[] disabled) throws Exception
    {
        return getInstance(Keys.KEY_OBJECTIVES, Keys.KEY_OBJECTIVES_ABB, values, disabled);
    }


    /**
     * Allows retrieving the object instance.
     *
     * @param key      key/label/feature (will be converted to upper case)
     * @param abb      key/label/name (abbreviated) (will be converted to upper case)
     * @param values   key's realizations (each one will be converted to upper case)
     * @param disabled flags associated with values (1:1) mapping; if a flag is true, the linked value is disabled
     *                 (excludes the scenario involving this key-value mapping from execution); can be null ->
     *                 all values are enabled
     * @return object instance
     * @throws GlobalException the exception is thrown when the input data is invalid (e.g., nulls, empty strings, etc.)
     */
    public static KeyValues getInstance(String key, String abb, String[] values, boolean[] disabled) throws GlobalException
    {
        if (key == null) throw new GlobalException("No key is provided", KeyValues.class);
        if (key.isEmpty()) throw new GlobalException("The key is an empty string", KeyValues.class);
        if (abb == null) throw new GlobalException("No key abbreviation is provided", KeyValues.class);
        if (abb.isEmpty()) throw new GlobalException("The key abbreviation is an empty string", KeyValues.class);
        if (values == null)
            throw new GlobalException("The values for the key = " + key + " are not provided (the array is null)", KeyValues.class);
        if (values.length == 0)
            throw new GlobalException("The values for the key = " + key + " are not provided (the array is empty)", KeyValues.class);
        for (String v : values)
            if (v == null) throw new GlobalException("The value for the key = " + key + " is null", KeyValues.class);
        for (String v : values)
            if (v.isEmpty())
                throw new GlobalException("The value for the key = " + key + " is an empty string", KeyValues.class);
        if ((disabled != null) && (disabled.length != values.length))
            throw new GlobalException("The disable flags array for the key = " + key + " is of different length than the values array", KeyValues.class);

        Value[] vs = new Value[values.length];
        boolean[] d = new boolean[values.length];
        if (disabled != null) System.arraycopy(disabled, 0, d, 0, values.length);
        for (int i = 0; i < values.length; i++) vs[i] = new Value(values[i].toUpperCase(), d[i]);
        return new KeyValues(new Key(key, abb), vs);
    }


    /**
     * The key.
     */
    private final Key _key;

    /**
     * Associated values.
     */
    private final Value[] _values;

    /**
     * Helps to access the value by its string representation.
     */
    private final HashMap<String, Value> _valueMap;

    /**
     * Helps to access the value index (in-array position) by its string representation.
     */
    private final HashMap<String, Integer> _valueIndexMap;

    /**
     * Parameterized constructor.
     *
     * @param key    the key
     * @param values associated values
     */
    protected KeyValues(Key key, Value[] values)
    {
        _key = key;
        _values = values;
        _valueMap = new HashMap<>(values.length);
        _valueIndexMap = new HashMap<>();
        for (Value v : values) _valueMap.put(v.toString(), v);
        for (int i = 0; i < values.length; i++) _valueIndexMap.put(values[i].toString(), i);
    }

    /**
     * Getter for the key object.
     *
     * @return key object
     */
    public Key getKey()
    {
        return _key;
    }

    /**
     * Getter for the values associated with the key.
     *
     * @return the values associated with the key
     */
    public Value[] getValues()
    {
        return _values;
    }

    /**
     * Getter for the map that allows accessing values by their string representations.
     *
     * @return value map
     */
    public HashMap<String, Value> getValueMap()
    {
        return _valueMap;
    }

    /**
     * Getter for the map that allows accessing values indices by their string representations.
     *
     * @return value map index
     */
    public HashMap<String, Integer> getValueIndexMap()
    {
        return _valueIndexMap;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation.
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder(_key.toString());
        str.append("_");
        for (int i = 0; i < _values.length; i++)
        {
            str.append(_values[i]);
            if (i < _values.length - 1) str.append("_");
        }
        return str.toString();
    }

    /**
     * Checks whether the key-value is equal to other key-value (based on the comparison of keys and values (the string
     * representation); abbreviation and disable flags are not involved in the comparison).
     *
     * @param o the other object
     * @return true = objects are equal (their names); false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof KeyValues okv)) return false;
        return toString().equals(okv.toString());
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
