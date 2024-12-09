package condition;

import exception.GlobalException;
import scenario.KeyValue;
import scenario.KeyValues;
import scenario.Scenario;
import scenario.Value;

import java.util.LinkedList;

/**
 * Auxiliary class that wraps requirements for scenario disabling (pairs of keys and values).
 * If a scenario has all such pairs provided, it will be disabled. Note that a scenario may be defined by more key-values
 * than those stored in the object of this class (if so, one such object may turn off multiple scenarios). E.g., a scenario
 * may be defined by "ALGORITHM = NSGAII"; "OBJECTIVES = 2". A condition "ALGORITHM = NSGAII" is enough to disable this scenario.
 *
 * @author MTomczyk
 */
public class ScenarioDisablingConditions
{
    /**
     * Conditions for disabling a scenario (all must be satisfied).
     */
    protected final KeyValue[] _keyValues;

    /**
     * Parameterized constructor. Requires providing a key and a value. This constructor produces a simple key-value
     * disabling condition.
     *
     * @param key   key
     * @param value value
     */
    public ScenarioDisablingConditions(String key, String value)
    {
        this(new String[]{key}, new String[]{value});
    }

    /**
     * Parameterized constructor. Requires providing 1:1 key-value relationships via two input arrays.
     * They should be non-empty and of equal length. If empty, there are no disabling conditions (scenario is always
     * enabled). If not of equal size, the longer array will be truncated. Nulls are ignored. If no valid key-values are
     * found after parsing the input, the examined scenario is always considered enabled.
     *
     * @param keys   keys
     * @param values values
     */
    public ScenarioDisablingConditions(String[] keys, String[] values)
    {
        if (((keys == null) || (keys.length == 0)) ||
                ((values == null) || (values.length == 0)))
        {
            _keyValues = null;
            return;
        }

        LinkedList<KeyValue> validPairs = new LinkedList<>();

        int l = keys.length;
        if (values.length < l) l = values.length;

        for (int i = 0; i < l; i++)
        {
            try
            {
                KeyValues kv = KeyValues.getInstance(keys[i], keys[i], new String[]{values[i]}, null);
                validPairs.add(new KeyValue(kv.getKey(), kv.getValues()[0]));
            } catch (GlobalException ignored)
            {

            }
        }

        if (validPairs.isEmpty())
        {
            _keyValues = null;
            return;
        }

        int idx = 0;
        _keyValues = new KeyValue[validPairs.size()];
        for (KeyValue kv : validPairs) _keyValues[idx++] = kv;
    }

    /**
     * The main method for checking if a scenario should be disabled given the maintained disabling conditions.
     *
     * @param scenario the examined scenario
     * @return true = the scenario matches all the maintained disabling conditions (hence, should be excluded from
     * processing); false otherwise
     */
    public boolean shouldBeDisabled(Scenario scenario)
    {
        if (_keyValues == null) return false;
        if (_keyValues.length == 0) return false;

        for (KeyValue kv : _keyValues)
        {
            Value value = scenario.getKeyValuesMap().get(kv.getKey().toString());
            if (value == null) return false;
            if (!kv.getValue().equals(value)) return false;
        }
        return true;
    }

    /**
     * Constructs a string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < _keyValues.length;i++)
        {
            s.append(_keyValues[i].toString());
            if (i < _keyValues.length - 1) s.append("_");
        }
        return s.toString();
    }


}
