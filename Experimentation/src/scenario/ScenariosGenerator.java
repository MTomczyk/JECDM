package scenario;

import combinatorics.Possibilities;
import exception.GlobalException;
import io.FileUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides auxiliary methods for creating all possible experimental scenarios based on key-values provided ({@link KeyValues}).
 *
 * @author MTomczyk
 */
public class ScenariosGenerator
{
    /**
     * Constructs and retrieves all possible experimental scenarios.
     * The method first validates all key-values (e.g., checks if there are no duplicates in the keys).
     * Then, it reorders them, prioritizing the most essential keys (by default: PROBLEM and OBJECTIVES).
     *
     * @param keyValues         all specified keys and their values
     * @param allowedCharacters by default, all key/value labels are supposed to consist of only [a-z] (it will be
     *                          translated into upper case), [A-Z], [0-9] characters; the reason is to mitigate potential
     *                          errors caused, e.g., when creating folders based on the keys' names; nonetheless, if needed,
     *                          some extra allowed character can be provided via this set
     * @param keyValuesOrder    optional parameter; when provided, the key-values are assigned orders as imposed by
     *                          this array (1:1) mapping; and sorted (in the ascending order; the numbers do not need to
     *                          be unique; however, they will be reorganized into 0,1,... form in the end)
     * @return an object that encapsulates all possible experimental scenarios, constructed based on the provided keys and their values
     * @throws GlobalException exception will be thrown when the input data for scenarios initialization is invalid
     */
    public static Scenarios getScenarios(KeyValues[] keyValues,
                                         Set<Character> allowedCharacters,
                                         int[] keyValuesOrder) throws GlobalException
    {
        validate(keyValues, allowedCharacters, keyValuesOrder);
        orderKeyValues(keyValues, keyValuesOrder);
        return createScenarios(keyValues);
    }

    /**
     * Creates all specified experimental scenarios, wraps them using a dedicated object, and returns.
     *
     * @param keyValues all specified keys and their values (they are sorted at this stage)
     * @return scenarios object
     */
    private static Scenarios createScenarios(KeyValues[] keyValues)
    {
        int[] s = new int[keyValues.length];
        for (int i = 0; i < keyValues.length; i++) s[i] = keyValues[i].getValues().length;
        int[][] cp = Possibilities.generateCartesianProduct(s, true);

        Scenario[] scenarios = new Scenario[cp.length];

        for (int scenario = 0; scenario < cp.length; scenario++)
        {
            KeyValue[] kvs = new KeyValue[keyValues.length];
            for (int i = 0; i < keyValues.length; i++)
            {
                Key key = keyValues[i].getKey();
                Value value = keyValues[i].getValues()[cp[scenario][i]];
                kvs[i] = new KeyValue(key, value);
            }
            scenarios[scenario] = new Scenario(kvs);
        }

        return new Scenarios(keyValues, scenarios);
    }

    /**
     * Validates the keys and their values.
     *
     * @param keyValues         all specified keys and their values
     * @param allowedCharacters by default, all key/value labels are supposed to consist of only [a-z] (it will be
     *                          translated into upper case), [A-Z], [0-9] characters; the reason is to mitigate potential
     *                          errors caused, e.g., when creating folders based on the keys' names; nonetheless, if needed,
     *                          some extra allowed character can be provided via this set
     * @param keyValuesOrder    optional parameter; when provided, the key-values are assigned orders as imposed by
     *                          this array (1:1) mapping; and sorted (in the ascending order; the numbers do not need to
     *                          be unique; however, they will be reorganized into 0,1,... form in the end)
     * @throws GlobalException exception is thrown when the input is invalid (e.g., the keys are empty, or not unique)
     */
    private static void validate(KeyValues[] keyValues, Set<Character> allowedCharacters, int[] keyValuesOrder) throws GlobalException
    {
        if (keyValues == null)
            throw new GlobalException("The key-values are not specified (the array is null)", ScenariosGenerator.class);
        if (keyValues.length == 0)
            throw new GlobalException("The key-values are not specified (the array is empty)", ScenariosGenerator.class);
        for (KeyValues kv : keyValues)
            if (kv == null)
                throw new GlobalException("One of the provided key-values is not specified (is null)", ScenariosGenerator.class);
        if ((keyValuesOrder != null) && (keyValues.length != keyValuesOrder.length))
            throw new GlobalException("The number of provided custom orders is of a different size than the key-values array length", ScenariosGenerator.class);

        // check keys and their abbreviations
        Set<Key> keys = new HashSet<>(keyValues.length);
        Set<String> abbreviations = new HashSet<>(keyValues.length);

        for (KeyValues kv : keyValues)
        {
            if (keys.contains(kv.getKey()))
                throw new GlobalException("The key = " + kv.getKey() + " is not unique", ScenariosGenerator.class);
            keys.add(kv.getKey());
            if (abbreviations.contains(kv.getKey().getAbbreviation()))
                throw new GlobalException("The key abbreviation = " + kv.getKey().getAbbreviation() + " is not unique", ScenariosGenerator.class);
            abbreviations.add(kv.getKey().getAbbreviation());
        }

        for (KeyValues kv : keyValues)
        {
            if (!FileUtils.isAlphanumeric(kv.getKey().getLabel(), allowedCharacters))
                throw new GlobalException("The key = " + kv.getKey() + " contains forbidden characters", ScenariosGenerator.class);
            if (!FileUtils.isAlphanumeric(kv.getKey().getAbbreviation(), allowedCharacters))
                throw new GlobalException("The key abbreviation = " + kv.getKey().getAbbreviation() + " contains forbidden characters", ScenariosGenerator.class);
            for (Value v : kv.getValues())
                if (!FileUtils.isAlphanumeric(v.getValue(), allowedCharacters))
                    throw new GlobalException("The value = " + v.getValue() + " of the key = " + kv.getKey() + " contains forbidden characters", ScenariosGenerator.class);
        }
    }

    /**
     * The method orders the keys. By default, the problem and the objectives key will be prioritized while the
     * algorithms are ordered last. The remaining (e.g., custom) keys are ordered as provided. The reason for such
     * ordering is that the catalog hierarchy in which the results are stored is based on the key order. Such ordering
     * facilitates the data inspection.
     *
     * @param keyValues      all specified keys and their values
     * @param keyValuesOrder optional parameter; when provided, the key-values are assigned orders as imposed by
     *                       this array (1:1) mapping; and sorted (in the ascending order; the numbers do not need to
     *                       be unique; however, they will be reorganized into 0,1,... form in the end)
     */
    private static void orderKeyValues(KeyValues[] keyValues, int[] keyValuesOrder)
    {
        // assign orders
        if (keyValuesOrder != null)
        {
            for (int i = 0; i < keyValuesOrder.length; i++)
                keyValues[i].getKey().setOrder(keyValuesOrder[i]);
        }
        else
        {
            int idx = 0;
            for (int i = 0; i < keyValues.length; i++)
            {
                Integer order = getOrder(keyValues, i);
                if (order == null) order = keyValues.length + idx++;
                keyValues[i].getKey().setOrder(order);
            }
        }

        //sort and flat
        Arrays.sort(keyValues, Comparator.comparingInt(o -> o.getKey().getOrder()));
        for (int i = 0; i < keyValues.length; i++) keyValues[i].getKey().setOrder(i);
    }

    /**
     * Retrieves a pre-defined order for a key.
     *
     * @param keyValues all key-values specified
     * @param i         index of the key-value being analyzed
     * @return order (null, if the pre-defined order is not specified)
     */
    protected static Integer getOrder(KeyValues[] keyValues, int i)
    {
        if (keyValues[i].getKey().toString().equals(Keys.KEY_PROBLEM)) return 0;
        else if (keyValues[i].getKey().toString().equals(Keys.KEY_OBJECTIVES)) return 1;
        else if (keyValues[i].getKey().toString().equals(Keys.KEY_GENERATIONS)) return 2;
        else if (keyValues[i].getKey().toString().equals(Keys.KEY_ALGORITHM)) return keyValues.length * 2 + 1;
        return null;
    }
}
