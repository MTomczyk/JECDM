package scenario;

import java.util.HashMap;

/**
 * This class describes the experimental scenario. It encapsulates all keys, each associated with its one linked value.
 *
 * @author MTomczyk
 */
public class Scenario
{
    /**
     * Wraps all considered keys and their linked (single) values (1:1 mapping).
     * The elements are ordered as imposed by the ordering procedure in {@link ScenariosGenerator}.
     */
    private final KeyValue[] _keyValues;

    /**
     * Allows quick access to the value by providing a key represented as a string.
     */
    private final HashMap<String, Value> _keyValuesMap;

    /**
     * String representation (lazy init when called).
     */
    private String _stringRepresentation = null;

    /**
     * String representation (abbreviated; lazy init when called).
     */
    private String _stringRepresentationAbbreviated = null;

    /**
     * Disabled flag. If flag = true, then one of the scenario's flags is disabled.
     * Therefore, such a scenario can be excluded from the processing.
     */
    private boolean _disabled;

    /**
     * Parameterized constructor.
     *
     * @param keyValues all considered keys and their linked (single) values (1:1 mapping)
     */
    public Scenario(KeyValue[] keyValues)
    {
        _keyValues = keyValues;
        _keyValuesMap = new HashMap<>(keyValues.length);
        _disabled = false;
        for (KeyValue kv : keyValues)
        {
            _keyValuesMap.put(kv.getKey().toString(), kv.getValue());
            if (kv.getValue().isDisabled()) _disabled = true;
        }
    }

    /**
     * Returns the value (represented as string).
     *
     * @return value (represented as string)
     */
    @Override
    public String toString()
    {
        if (_stringRepresentation == null)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _keyValues.length; i++)
            {
                sb.append(_keyValues[i].toString());
                if (i < _keyValues.length - 1) sb.append("_");
            }
            _stringRepresentation = sb.toString();
        }

        return _stringRepresentation;
    }


    /**
     * Getter for the string representation (abbreviated).
     *
     * @return string representation (abbreviated)
     */
    public String getStringRepresentationAbbreviated()
    {
        if (_stringRepresentationAbbreviated == null)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _keyValues.length; i++)
            {
                sb.append(_keyValues[i].getStringRepresentationAbbreviated());
                if (i < _keyValues.length - 1) sb.append("_");
            }
            _stringRepresentationAbbreviated = sb.toString();
        }

        return _stringRepresentationAbbreviated;
    }


    /**
     * Can be called to check whether the scenario is disabled.
     *
     * @return true, if the scenario is disabled; false otherwise
     */
    public boolean isDisabled()
    {
        return _disabled;
    }

    /**
     * Checks whether the scenario is equal to other scenario (based on their string representation).
     *
     * @param o the other object
     * @return true = objects are equal (their names); false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Scenario ov)) return false;
        return toString().equals(ov.toString());
    }

    /**
     * Provides quick access for a value representing a problem.
     * If the scenario does not contain a problem-related key (see {@link Keys}), the method returns null.
     *
     * @return problem-related value, or null if not specified
     */
    public Value getProblemValue()
    {
        if (_keyValuesMap == null) return null;
        return _keyValuesMap.getOrDefault(Keys.KEY_PROBLEM, null);
    }


    /**
     * Provides quick access for a string representing a problem.
     * If the scenario does not contain a problem-related key (see {@link Keys}), the method returns null.
     *
     * @return problem-related value, or null if not specified
     */
    public String getProblem()
    {
        Value value = getProblemValue();
        if (value != null) return value.getValue();
        else return null;
    }

    /**
     * Provides quick access for a value representing an algorithm.
     * If the scenario does not contain an algorithm-related key (see {@link Keys}), the method returns null.
     *
     * @return algorithm-related value, or null if not specified
     */
    public Value getAlgorithmValue()
    {
        if (_keyValuesMap == null) return null;
        return _keyValuesMap.getOrDefault(Keys.KEY_ALGORITHM, null);
    }


    /**
     * Provides quick access for a string representing an algorithm.
     * If the scenario does not contain an algorithm-related key (see {@link Keys}), the method returns null.
     *
     * @return algorithm-related value, or null if not specified
     */
    public String getAlgorithm()
    {
        Value value = getAlgorithmValue();
        if (value != null) return value.getValue();
        else return null;
    }

    /**
     * Provides quick access for a value representing the number of objectives considered.
     * If the scenario does not contain an objectives-related key (see {@link Keys}), the method returns null.
     *
     * @return objectives-related value, or null if not specified
     */
    public Value getObjectivesValue()
    {
        if (_keyValuesMap == null) return null;
        return _keyValuesMap.getOrDefault(Keys.KEY_OBJECTIVES, null);
    }

    /**
     * Provides quick access for an integer representing the number of objective considered.
     * If the scenario does not contain an objectives-related key (see {@link Keys}), the method returns null.
     *
     * @return objectives-related value, or null if not specified
     */
    public Integer getObjectives()
    {
        Value value = getObjectivesValue();
        if (value == null) return null;
        else return Integer.parseInt(value.getValue());
    }

    /**
     * Provides quick access for a value representing the number of generations considered.
     * If the scenario does not contain a generations-related key (see {@link Keys}), the method returns null.
     *
     * @return generations-related value, or null if not specified
     */
    public Value getGenerationsValue()
    {
        if (_keyValuesMap == null) return null;
        return _keyValuesMap.getOrDefault(Keys.KEY_GENERATIONS, null);
    }

    /**
     * Provides quick access for an integer representing the number of generations considered.
     * If the scenario does not contain a generations-related key (see {@link Keys}), the method returns null.
     *
     * @return generations-related value, or null if not specified
     */
    public Integer getGenerations()
    {
        Value value = getGenerationsValue();
        if (value == null) return null;
        else return Integer.parseInt(value.getValue());
    }


    /**
     * Getter for the key-values constituting the scenario.
     *
     * @return key-values
     */
    public KeyValue[] getKeyValues()
    {
        return _keyValues;
    }


    /**
     * Getter for the map that allows quick access to the value by providing a key represented as a string.
     *
     * @return associative array (string representing a key -> value).
     */
    public HashMap<String, Value> getKeyValuesMap()
    {
        return _keyValuesMap;
    }

    /**
     * An auxiliary method that can be used to check whether the provided key-value realizations match the scenario
     * (the input key-values can be a subset of the stored such pairs).
     *
     * @param keyValues key-value realizations
     * @return true, if the input is equivalent to the scenario; false otherwise
     */
    public boolean matches(KeyValue[] keyValues)
    {
        if (keyValues == null) return false;
        for (KeyValue keyValue : keyValues)
        {
            Value val = _keyValuesMap.get(keyValue.getKey().toString());
            if (val == null) return false;
            if (!val.equals(keyValue.getValue())) return false;
        }

        return true;
    }


    /**
     * An auxiliary method for deriving a sub-scenario for the current one that excludes one selected key.
     * If the requested key does not exist or is the only one in the current scenario, the method returns null.
     *
     * @param keyToExclude (string representation)
     * @return sub-scenario
     */
    public Scenario deriveSubScenario(String keyToExclude)
    {
        if (!_keyValuesMap.containsKey(keyToExclude)) return null;
        else if (_keyValues.length == 1) return null;
        KeyValue[] kvs = new KeyValue[_keyValues.length - 1];
        int idx = 0;
        for (KeyValue keyValue : _keyValues)
            if (!keyValue.getKey().toString().equals(keyToExclude)) kvs[idx++] = keyValue;
        return new Scenario(kvs);
    }
}
