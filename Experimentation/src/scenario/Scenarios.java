package scenario;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Encapsulates all experimental scenarios.
 *
 * @author MTomczyk
 */
public class Scenarios
{
    /**
     * Ordered key-values.
     */
    private final KeyValues[] _keyValues;

    /**
     * Wrapped scenarios. The list contains all scenarios, even those that should not be executed due to 'disable' flags (see {@link Value}).
     */
    private final Scenario[] _scenarios;

    /**
     * Provides quick access to the key-value object by using the key's string representation.
     */
    private final HashMap<String, KeyValues> _keyValuesMap;


    /**
     * Parameterized constructor.
     *
     * @param keyValues key-values ordered by {@link ScenariosGenerator}
     * @param scenarios wrapped scenarios; note that they may be sorted by {@link ScenariosGenerator}.
     */
    public Scenarios(KeyValues[] keyValues, Scenario[] scenarios)
    {
        _keyValues = keyValues;
        _scenarios = scenarios;
        _keyValuesMap = new HashMap<>();
        for (KeyValues kv : _keyValues) _keyValuesMap.put(kv.getKey().toString(), kv);
    }

    /**
     * Getter for the scenarios array.
     *
     * @return scenarios array
     */
    public Scenario[] getScenarios()
    {
        return _scenarios;
    }


    /**
     * Getter for the list of ordered key-values.
     *
     * @return ordered key-values
     */
    protected KeyValues[] getOrderedKeyValues()
    {
        return _keyValues;
    }

    /**
     * Getter for the map that provides quick access to the key-value object by using the key's string representation.
     *
     * @return map that provides quick access to the key-value object by using the key's string representation
     */
    public HashMap<String, KeyValues> getKeyValuesMap()
    {
        return _keyValuesMap;
    }

    /**
     * Supportive method that constructs a subset of scenarios whose key-value realizations match the ones provided as inputs.
     *
     * @param keyValues fixed key-value realizations
     * @return subset of scenarios matching the provided pattern
     */
    public Scenario[] getScenariosThatMatch(KeyValue[] keyValues)
    {
        LinkedList<Scenario> scenarios = new LinkedList<>();
        for (Scenario s : _scenarios)
        {
            // check if matches
            boolean match = true;
            for (KeyValue kv : keyValues)
            {
                Value val = s.getKeyValuesMap().get(kv.getKey().toString());
                if (val == null) continue;
                if (!kv.getValue().equals(val))
                {
                    match = false;
                    break;
                }
            }

            if (match) scenarios.add(s);
        }

        Scenario[] result = new Scenario[scenarios.size()];
        int idx = 0;
        for (Scenario sc : scenarios) result[idx++] = sc;
        return result;
    }

}

