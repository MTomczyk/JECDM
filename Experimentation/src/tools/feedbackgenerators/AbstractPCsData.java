package tools.feedbackgenerators;

import condition.ScenarioDisablingConditions;
import exception.Exception;
import exception.GlobalException;
import scenario.KeyValues;
import scenario.Scenarios;
import scenario.ScenariosGenerator;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import utils.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides common methods and functionalities.
 *
 * @author MTomczyk
 */
abstract sealed class AbstractPCsData permits PCsDataGenerator, PCsDataLoader
{
    /**
     * Auxiliary preference states.
     */
    protected enum PreferenceStates
    {
        /**
         * Preferred (alternative) state.
         */
        PREFERRED,

        /**
         * Not preferred (alternative) state.
         */
        NOT_PREFERRED,

        /**
         * Equal (alternatives) state.
         */
        EQUAL,
    }

    /**
     * Params container.
     */
    public static sealed class Params permits PCsDataGenerator.Params, PCsDataLoader.Params
    {
        /**
         * Scenario keys. IMPORTANT NOTE: order matters; will be converted to upper case.
         */
        public String[] _keys;

        /**
         * Scenario per-key values (1:1) mapping (order matters; will be converted to upper case).
         */
        public String[][] _values;

        /**
         * No. trials per scenario (at least 1).
         */
        public int _trials;

        /**
         * Object responsible for providing no. interactions given a processed scenario (at least 1).
         */
        public INoInteractionsProvider _noInteractionsProvider;

        /**
         * Absolute path to the file for storing the results. The path should include the file name and its
         * extension ".txt" (the only format supported). E.g., "C:\results.txt".
         */
        public String _filePath;

        /**
         * By default, all key/value labels are supposed to consist of only [a-z] (it will be translated into upper
         * case),
         * [A-Z], [0-9] characters; the reason is to mitigate potential errors caused, e.g., when creating folders
         * based
         * on the keys' names; nonetheless, if needed, some extra allowed character can be provided via this array.
         */
        public Character[] _extraAllowedCharacters = new Character[]{'_'};

        /**
         * Auxiliary scenario disabling conditions that can be provided to turn off, i.e., exclude from considerations,
         * some of the scenarios resulting from instantiating a Cartesian product of key/values.
         */
        public ScenarioDisablingConditions _scenarioDisablingConditions;

        /**
         * Indent for logging.
         */
        public int _indent = 4;

        /**
         * If true, notifications are print into the console.
         */
        public boolean _notify = true;

    }

    /**
     * Scenario keys. IMPORTANT NOTE: order matters; upper case.
     */
    protected final String[] _keys;

    /**
     * No. trials per scenario (at least 1).
     */
    protected final int _trials;

    /**
     * Object responsible for providing no. interactions given a processed scenario (at least 1).
     */
    protected final INoInteractionsProvider _noInteractionsProvider;

    /**
     * Absolute path to the file for storing the results. The path should include the file name and its
     * extension ".txt" (the only format supported). E.g., "C:\results.txt".
     */
    protected final String _filePath;


    /**
     * Scenarios key values.
     */
    protected final KeyValues[] _kv;

    /**
     * Scenarios to process.
     */
    protected final Scenarios _scenarios;


    /**
     * Auxiliary scenario disabling conditions that can be provided to turn off, i.e., exclude from considerations,
     * some of the scenarios resulting from instantiating a Cartesian product of key/values.
     */
    protected final ScenarioDisablingConditions _scenarioDisablingConditions;

    /**
     * Contains the number of scenarios being disabled.
     */
    protected int _disabledScenarios;

    /**
     * Auxiliary object for logging.
     */
    protected final Log _log;

    /**
     * If true, notifications are print into the console.
     */
    protected final boolean _notify;

    /**
     * Indent for logging.
     */
    protected final int _indent;

    /**
     * Indent doubled.
     */
    protected final int _dIndent;

    /**
     * Normalizations builder object.
     */
    protected final INormalizationBuilder _normalizationsBuilder;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws Exception exception can be thrown 
     */
    protected AbstractPCsData(Params p) throws Exception
    {
        validate(p);
        _keys = p._keys;
        _trials = p._trials;
        _noInteractionsProvider = p._noInteractionsProvider;
        _filePath = p._filePath;
        _scenarioDisablingConditions = p._scenarioDisablingConditions;
        _disabledScenarios = p._trials;
        _kv = instantiateScenarioKeyValues(p);
        _scenarios = instantiateScenarios(p);
        _notify = p._notify;
        if (_notify)
            _log = new Log(true, true, true,
                    true, true, true);
        else _log = null;
        _indent = Math.max(0, p._indent);
        _dIndent = _indent * 2;
        _normalizationsBuilder = new StandardLinearBuilder();
    }

    /**
     * Auxiliary method for instantiating scenario key-values.
     *
     * @param p params container
     * @return key-values data
     * @throws Exception exception can be thrown 
     */
    private KeyValues[] instantiateScenarioKeyValues(Params p) throws Exception
    {
        KeyValues[] kv = new KeyValues[p._keys.length];
        try
        {
            for (int i = 0; i < p._keys.length; i++)
                kv[i] = KeyValues.getInstance(p._keys[i], p._keys[i], p._values[i], null);
            return kv;
        } catch (GlobalException e)
        {
            throw new Exception("Exception occurred when instantiating key/values (message = " + e.getMessage() + ")",
                    null, this.getClass());
        }
    }

    /**
     * Auxiliary method for instantiating scenario key-values.
     *
     * @param p params container
     * @return scenarios data
     * @throws Exception exception can be thrown 
     */
    private Scenarios instantiateScenarios(Params p) throws Exception
    {
        try
        {
            Set<Character> characters = new HashSet<>();
            if (p._extraAllowedCharacters != null) Collections.addAll(characters, p._extraAllowedCharacters);
            return ScenariosGenerator.getScenarios(_kv, characters, null);
        } catch (GlobalException e)
        {
            throw new Exception("Exception occurred when instantiating scenarios (message = " + e.getMessage() + ")",
                    null, this.getClass());
        }
    }

    /**
     * Auxiliary method for validating params container's data.
     *
     * @param p params container
     * @throws Exception exception can be thrown 
     */
    @SuppressWarnings("ExtractMethodRecommender")
    protected void validate(Params p) throws Exception
    {
        Set<String> uniqueKeys;
        { // keys
            if (p._keys == null) throw new Exception("No keys are provided (the array is null)",
                    null, this.getClass());
            if (p._keys.length == 0)
                throw new Exception("No keys are provided (the array is empty)", null, this.getClass());
            for (String k : p._keys)
                if (k == null) throw new Exception("One of the provided keys is null", null, this.getClass());
            uniqueKeys = new HashSet<>(p._keys.length);
            for (String k : p._keys)
            {
                if (uniqueKeys.contains(k)) throw new Exception("The key = " + k + " is not unique",
                        null, this.getClass());
                uniqueKeys.add(k);
            }
        } // scenarios
        {
            if (p._values == null)
                throw new Exception("No values are provided (the array is null)", null, this.getClass());
            if (p._values.length != p._keys.length)
                throw new Exception("The number of value-arrays does not equal the number of keys ("
                        + p._values.length + " vs " + p._keys.length + ")", null, this.getClass());
            for (int i = 0; i < p._values.length; i++)
            {
                String[] v = p._values[i];
                if (v == null)
                    throw new Exception("One of the provided value-arrays for a key = " + p._keys[i] + " is null",
                            null, this.getClass());
                if (v.length == 0)
                    throw new Exception("One of the provided value-arrays for a key = " + p._keys[i] + " is empty",
                            null, this.getClass());
                for (String vv : v)
                    if (vv == null)
                        throw new Exception("One of the provided values for a key = " + p._keys[i] + " is null", null,
                                this.getClass());
                Set<String> unique = new HashSet<>(p._keys.length);
                for (String vv : v)
                {
                    if (unique.contains(vv))
                        throw new Exception("The value = " + vv + " is not unique for a key = " + p._keys[i], null,
                                this.getClass());
                    unique.add(vv);
                }
            }
        }
        if (p._trials < 1)
            throw new Exception("The number of trials must be at least 1 (but equals " + p._trials + ")", null,
                    this.getClass());
        if (p._noInteractionsProvider == null)
            throw new Exception("The no. interactions provider is null", null, this.getClass());
        if (p._filePath == null)
            throw new Exception("The file path is null", null, this.getClass());
    }
}

