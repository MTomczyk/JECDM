package scenario;

import combinatorics.Possibilities;
import exception.GlobalException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class supports cross-analyses. It is assumed that among N key-values objects, N-x are fixed while the
 * remaining x are varying and are used as a basis for cross-comparison. This class wraps all the relevant data for
 * identifying such a comparative scenario.
 *
 * @author MTomczyk
 */
public class CrossedScenarios
{
    /**
     * List of all key-value objects that are assumed to be fixed. It is assumed that they keys are provided in an order
     * consistent with the key-order stored in {@link Scenarios}.
     */
    private final KeyValue[] _fixedKeyValues;

    /**
     * List of all key-values objects that are assumed to form the basis for cross-comparison.
     */
    private final KeyValues[] _comparedKeyValues;

    /**
     * An auxiliary map that stores all possible realizations of the key-values being compared. The matrix is a cartesian
     * product where all the values are crossed. Each row is such a product (unique) with a dimensionality that equals
     * the number of compared key-values. The integer values stored are indices for values stored in subsequent elements
     * of {@link CrossedScenarios#_comparedKeyValues}. The rows are sorted in a lexicographical order.
     */
    private int[][] _possibleRealizations = null;


    /**
     * Cross-comparison level (should be at least two and smaller, equal to the total number of unique key-values).
     * If equals 2, two key-values are cross-examined; if equals 3, three, and so on.
     */
    private final int _level;

    /**
     * Contains references to original scenarios (stored in {@link Scenarios}) that can result from generating all
     * possible cross-scenarios.
     */
    private final Scenario[] _referenceScenarios;

    /**
     * Contains references to original scenarios (stored in {@link Scenarios}) that can result from generating all
     * possible cross-scenarios. In contrast to {@link CrossedScenarios#_referenceScenarios}, this array contains
     * scenarios that are sorted as imposed by {@link CrossedScenarios#_possibleRealizations} (1:1 mapping).
     */
    private Scenario[] _referenceScenariosSorted;

    /**
     * Auxiliary map that translates one of the scenarios stored in {@link CrossedScenarios#_referenceScenariosSorted}
     * (i.e., its string representation) into in-array index.
     */
    private HashMap<String, Integer> _referenceScenariosSortedMap;

    /**
     * Auxiliary map that translates one of the compared keys stored in {@link CrossedScenarios#_comparedKeyValues}
     * (i.e., its string representation) into in-array index.
     */
    private final HashMap<String, Integer> _comparedKeyValuesMap;

    /**
     * Parameterized constructor.
     *
     * @param fixedKeyValues     list of all key-value objects that are assumed to be fixed; it is assumed that they keys are
     *                           provided in an order consistent with the key-order stored in {@link Scenarios}.
     * @param comparedKeyValues  list of all key-values objects that are assumed to form the basis for cross-comparison
     * @param referenceScenarios contains references to original scenarios (stored in {@link Scenarios}) that can result
     *                           from generating all possible cross-scenarios
     * @param level              cross-comparison level (should be at least two and smaller, equal to the total number of
     *                           unique key-values), if equals 2, two key-values are cross-examined; if equals 3, three, and so on
     * @param scenarios          provides data on all experimental scenarios
     * @throws GlobalException global-level exception can be thrown when validating the input
     */
    public CrossedScenarios(KeyValue[] fixedKeyValues, KeyValues[] comparedKeyValues, Scenario[] referenceScenarios,
                            int level, Scenarios scenarios)
            throws GlobalException
    {
        _fixedKeyValues = fixedKeyValues;
        _comparedKeyValues = comparedKeyValues;
        _referenceScenarios = referenceScenarios;
        _level = level;
        validate(scenarios);
        generatePossibleRealizationsData();
        _comparedKeyValuesMap = new HashMap<>();
        for (int i = 0; i < comparedKeyValues.length; i++)
            _comparedKeyValuesMap.put(comparedKeyValues[i].getKey().toString(), i);
    }

    /**
     * Validates the input data provided via the constructor.
     *
     * @param scenarios Provides data on all experimental scenarios
     * @throws GlobalException the global-level exception will be thrown if the input data is invalid
     */
    public void validate(Scenarios scenarios) throws GlobalException
    {
        if (_comparedKeyValues == null)
            throw new GlobalException("The compared key-values are no provided (the array is null)", null, this.getClass());
        if (_comparedKeyValues.length == 0)
            throw new GlobalException("The compared key-values are no provided (the array is empty)", null, this.getClass());
        if (_comparedKeyValues.length != _level)
            throw new GlobalException("The number of provided compared key-values differs from the level", null, this.getClass());

        for (KeyValues kv : _comparedKeyValues)
            if (kv == null)
                throw new GlobalException("One of the provided compared key-values is null", null, this.getClass());

        Set<Key> usedKeys = new HashSet<>();
        if (_fixedKeyValues != null)
            for (KeyValue kv : _fixedKeyValues) usedKeys.add(kv.getKey());
        for (KeyValues kv : _comparedKeyValues) usedKeys.add(kv.getKey());
        if (usedKeys.size() != scenarios.getOrderedKeyValues().length)
            throw new GlobalException("The total number of keys is not valid (used = " + usedKeys.size() + "; existing = "
                    + scenarios.getOrderedKeyValues().length + ")", null, this.getClass());

        int expectedFixed = scenarios.getOrderedKeyValues().length - _comparedKeyValues.length;
        if (expectedFixed < 0)
            throw new GlobalException("The expected number of fixed key-values is negative)", null, this.getClass());

        if (expectedFixed > 0)
        {
            if (_fixedKeyValues == null)
                throw new GlobalException("The fixed key-values are no provided (the array is null)", null, this.getClass());
            if (_fixedKeyValues.length == 0)
                throw new GlobalException("The fixed key-values are no provided (the array is empty)", null, this.getClass());
            for (KeyValue kv : _fixedKeyValues)
                if (kv == null)
                    throw new GlobalException("One of the provided fixed key-values is null", null, this.getClass());
        }

        if (_referenceScenarios == null)
            throw new GlobalException("The reference scenarios are not provided (the array is null)", null, this.getClass());
        int expected = 1;
        for (KeyValues kv : _comparedKeyValues) expected *= kv.getValues().length;
        if (expected != _referenceScenarios.length)
            throw new GlobalException("Expected number of crossed scenarios = " + expected + " and does not equal the " +
                    "number of maintained reference scenarios = " + _referenceScenarios.length, null, this.getClass());

        for (KeyValues kv : scenarios.getOrderedKeyValues())
        {
            if (!usedKeys.contains(kv.getKey()))
                throw new GlobalException("The key = " + kv.getKey() + " is not provided", null, this.getClass());
        }

        if (_fixedKeyValues != null)
        {
            for (KeyValue kv : _fixedKeyValues)
                if (!scenarios.getKeyValuesMap().containsKey(kv.getKey().toString()))
                    throw new GlobalException("The key = " + kv.getKey() + " is not used in the original experimental scenarios", null, this.getClass());
        }
        for (KeyValues kv : _comparedKeyValues)
            if (!scenarios.getKeyValuesMap().containsKey(kv.getKey().toString()))
                throw new GlobalException("The key = " + kv.getKey() + " is not used in the original experimental scenarios", null, this.getClass());

        if (_fixedKeyValues != null)
        {
            for (KeyValue kv : _fixedKeyValues)
            {
                if (!scenarios.getKeyValuesMap().get(kv.getKey().toString()).getValueMap().containsKey(kv.getValue().toString()))
                    throw new GlobalException("The value = " + kv.getValue() + " for the key = " + kv.getKey() + " is not " +
                            "used in the original experimental scenarios", null, this.getClass());
            }
        }

        for (KeyValues kv : _comparedKeyValues)
        {
            HashMap<String, Value> valueMap = scenarios.getKeyValuesMap().get(kv.getKey().toString()).getValueMap();
            for (Value v : kv.getValues())
                if (!valueMap.containsKey(v.toString()))
                    throw new GlobalException("The value = " + v + " for the key = " + kv.getKey() + " is not " +
                            "used in the original experimental scenarios", null, this.getClass());
        }
    }

    /**
     * Auxiliary method that generates data on the possible realizations of key-values.
     */
    private void generatePossibleRealizationsData()
    {
        int[] limits = new int[_comparedKeyValues.length];
        for (int i = 0; i < limits.length; i++) limits[i] = _comparedKeyValues[i].getValues().length;
        _possibleRealizations = Possibilities.generateCartesianProduct(limits, false);
        _referenceScenariosSorted = new Scenario[_possibleRealizations.length];
        _referenceScenariosSortedMap = new HashMap<>();
        for (int i = 0; i < _possibleRealizations.length; i++)
        {
            KeyValue[] kvs = new KeyValue[_comparedKeyValues.length];
            for (int j = 0; j < _comparedKeyValues.length; j++)
                kvs[j] = new KeyValue(_comparedKeyValues[j].getKey(), _comparedKeyValues[j].getValues()[_possibleRealizations[i][j]]);

            for (Scenario s : _referenceScenarios)
                if (s.matches(kvs)) // there should be exactly 1
                {
                    _referenceScenariosSorted[i] = s;
                    _referenceScenariosSortedMap.put(_referenceScenariosSorted[i].toString(), i);
                    break;
                }
        }
    }

    /**
     * Can be used to check whether this crossed scenarios object equals another.
     *
     * @param obj another object
     * @return true if both objects are the same (logically); false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof CrossedScenarios cs)) return false;
        if (_fixedKeyValues.length != cs._fixedKeyValues.length) return false;
        for (int i = 0; i < _fixedKeyValues.length; i++)
            if (!_fixedKeyValues[i].equals(cs._fixedKeyValues[i])) return false;
        if (_comparedKeyValues.length != cs._comparedKeyValues.length) return false;
        for (int i = 0; i < _comparedKeyValues.length; i++)
            if (!_comparedKeyValues[i].equals(cs._comparedKeyValues[i])) return false;
        if (_level != cs._level) return false;
        if (_referenceScenarios.length != cs._referenceScenarios.length) return false;
        for (int i = 0; i < _referenceScenarios.length; i++)
            if (!_referenceScenarios[i].equals(cs._referenceScenarios[i])) return false;
        return true;
    }

    /**
     * Constructs the string representation of this object.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return getStringRepresentation();
    }

    /**
     * Constructs the string representation of this object.
     *
     * @return string representation
     */
    public String getStringRepresentation()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FIXED_");
        if ((_fixedKeyValues != null) && (_fixedKeyValues.length > 0))
        {
            for (KeyValue kv : _fixedKeyValues)
                sb.append(kv.getKey().getLabel()).append("_").append(kv.getValue().getValue()).append("_");
        }
        else sb.append("NONE_");

        sb.append("COMPARED_");
        for (int i = 0; i < _comparedKeyValues.length; i++)
        {
            sb.append(_comparedKeyValues[i].getKey().getLabel());
            if (i < _comparedKeyValues.length - 1) sb.append("_");
        }
        return sb.toString();
    }

    /**
     * Getter for the fixed key-values containing the scope.
     *
     * @return fixed key-values
     */
    public KeyValue[] getFixedKeyValue()
    {
        return _fixedKeyValues;
    }

    /**
     * Getter for the key-values being compared in the cross analysis.
     *
     * @return key-values being compared in the cross analysis
     */
    public KeyValues[] getComparedKeyValues()
    {
        return _comparedKeyValues;
    }

    /**
     * Getter for the cross-examination level (dimensionality, number of variables being compared)
     *
     * @return level
     */
    public int getLevel()
    {
        return _level;
    }

    /**
     * Getter for the reference scenarios aligning with the setting.
     *
     * @return reference scenarios
     */
    public Scenario[] getReferenceScenarios()
    {
        return _referenceScenarios;
    }

    /**
     * Getter for an auxiliary map that stores all possible realizations of the key-values being compared.
     * The matrix is a cartesian product where all the values are crossed. Each row is such a product (unique) with
     * a dimensionality that equals the number of compared key-values. The integer values stored are indices for values
     * stored in subsequent elements of {@link CrossedScenarios#_comparedKeyValues}. The rows are sorted in a lexicographical order.
     *
     * @return the map
     */
    public int[][] getPossibleRealizations()
    {
        return _possibleRealizations;
    }

    /**
     * Getter for the array that contains references to original scenarios (stored in {@link Scenarios}) that can result
     * from generating all possible cross-scenarios. In contrast to {@link CrossedScenarios#_referenceScenarios}, this
     * array contains scenarios that are sorted as imposed by {@link CrossedScenarios#_possibleRealizations} (1:1 mapping).
     *
     * @return the array
     */
    public Scenario[] getReferenceScenariosSorted()
    {
        return _referenceScenariosSorted;
    }

    /**
     * Getter for the auxiliary map that translates one of the scenarios stored in
     * {@link CrossedScenarios#_referenceScenariosSorted} (i.e., its string representation) into in-array index.
     *
     * @return the map
     */
    public HashMap<String, Integer> getReferenceScenariosSortedMap()
    {
        return _referenceScenariosSortedMap;
    }

    /**
     * Getter for the auxiliary map that translates one of the compared keys stored in {@link CrossedScenarios#_comparedKeyValues}
     * (i.e., its string representation) into in-array index.
     *
     * @return the map
     */
    public HashMap<String, Integer> getComparedKeysMap()
    {
        return _comparedKeyValuesMap;
    }
}
