package scenario;

import condition.ScenarioDisablingConditions;
import exception.GlobalException;

import java.util.HashMap;

/**
 * Class representing a setting (to be provided by the user) that helps to establish crossed scenarios ({@link CrossedScenarios}).
 *
 * @author MTomczyk
 */
public class CrossedSetting
{
    /**
     * Parameterized constructor.
     *
     * @param comparedKeys   raw (string) representation of keys to be compared
     * @param comparedValues raw (string) representation of values to be compared (one array for each key)
     */
    public CrossedSetting(String[] comparedKeys, String[][] comparedValues)
    {
        this(comparedKeys, comparedValues, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param comparedKeys                raw (string) representation of keys to be compared
     * @param comparedValues              raw (string) representation of values to be compared (one array for each key)
     * @param scenarioDisablingConditions specified (by the user) scenario disabling conditions (allows excluding some scenarios from the whole scenario set, narrowing the examination scope)
     */
    public CrossedSetting(String[] comparedKeys, String[][] comparedValues, ScenarioDisablingConditions[] scenarioDisablingConditions)
    {
        _comparedKeys = comparedKeys;
        _comparedValues = comparedValues;
        _scenarioDisablingConditions = scenarioDisablingConditions;
    }

    /**
     * Raw (string) representation of keys to be compared.
     */
    private final String[] _comparedKeys;

    /**
     * Raw (string) representation of values to be compared (one array for each key).
     */
    private final String[][] _comparedValues;

    /**
     * Key-values that serve as a basis for cross-examination.
     */
    private KeyValues[] _comparedKeyValues;

    /**
     * Specified (by the user) scenario disabling conditions (allows excluding some scenarios from the whole scenario set,
     * narrowing the examination scope).
     */
    private final ScenarioDisablingConditions[] _scenarioDisablingConditions;

    /**
     * Auxiliary map that allows accessing the compared key-values via the key's string representation.
     */
    private HashMap<String, KeyValues> _comparedKeyValuesMap;


    /**
     * The main method for parameterizing the setting.
     *
     * @param scenarios all experimental scenarios
     * @throws GlobalException global-level exception can be thrown when creating the setting data
     */
    public void instantiateSetting(Scenarios scenarios) throws GlobalException
    {
        if (_comparedKeys == null)
            throw new GlobalException("No keys are provided (the array is null)", null, this.getClass());
        if (_comparedKeys.length == 0)
            throw new GlobalException("No keys are provided (the array is empty)", null, this.getClass());
        for (String s : _comparedKeys)
            if (s == null) throw new GlobalException("One of the keys provided is null", null, this.getClass());
        if (_comparedValues == null)
            throw new GlobalException("No values are provided (the array is null)", null, this.getClass());
        if (_comparedValues.length == 0)
            throw new GlobalException("No values are provided (the array is empty)", null, this.getClass());
        if (_comparedKeys.length != _comparedValues.length)
            throw new GlobalException("The number of keys to be compared differs from the number of value arrays", null, this.getClass());

        _comparedKeyValues = new KeyValues[_comparedKeys.length];
        for (int i = 0; i < _comparedKeys.length; i++)
        {
            if (_comparedValues[i] == null)
                throw new GlobalException("The value array for key = " + _comparedKeys[i] + " is not provided (the array is null)", null, this.getClass());
            if (_comparedValues[i].length == 0)
                throw new GlobalException("The value array for key = " + _comparedKeys[i] + " is not provided (the array is empty)", null, this.getClass());

            String reqKey = _comparedKeys[i].toUpperCase();
            KeyValues matched = scenarios.getKeyValuesMap().get(reqKey);
            if (matched == null)
                throw new GlobalException("Could not find a matching key = " + reqKey + " in maintained scenarios", null, this.getClass());

            Value[] values = new Value[_comparedValues[i].length];
            for (int j = 0; j < _comparedValues[i].length; j++)
            {
                String reqVal = _comparedValues[i][j].toUpperCase();
                values[j] = matched.getValueMap().get(reqVal);
                if (values[j] == null) throw new GlobalException("Could not find a matching value = " + reqVal +
                        " for key = " + reqKey + " in maintained scenarios", null, this.getClass());
            }
            _comparedKeyValues[i] = new KeyValues(matched.getKey(), values);
        }

        _comparedKeyValuesMap = new HashMap<>(_comparedKeyValues.length);
        for (KeyValues kv : _comparedKeyValues) _comparedKeyValuesMap.put(kv.getKey().toString(), kv);
    }

    /**
     * Getter for the key-values that determine the scenarios to be cross-compared.
     *
     * @return key-value
     */
    public KeyValues[] getComparedKeyValues()
    {
        return _comparedKeyValues;
    }

    /**
     * Getter for the scenario disabling conditions used at the cross-examination level.
     *
     * @return scenario disabling conditions
     */
    public ScenarioDisablingConditions[] getScenarioDisablingConditions()
    {
        return _scenarioDisablingConditions;
    }

    /**
     * Getter for the auxiliary map that allows accessing the compared key-values via the key's string representation.
     *
     * @return map that allows accessing the compared key-values via the key's string representation
     */
    public HashMap<String, KeyValues> getComparedKeyValuesMap()
    {
        return _comparedKeyValuesMap;
    }
}
