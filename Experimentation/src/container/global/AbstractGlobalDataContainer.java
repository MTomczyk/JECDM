package container.global;

import condition.ScenarioDisablingConditions;
import condition.TrialDisablingConditions;
import container.AbstractDataContainer;
import container.global.initializers.DefaultRandomNumberGeneratorInitializer;
import container.global.initializers.IRandomNumberGeneratorInitializer;
import exception.GlobalException;
import exception.ScenarioException;
import executor.TrialExecutor;
import io.cross.ICrossSaver;
import io.scenario.IScenarioSaver;
import parser.Parser;
import random.IRandom;
import scenario.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 * A container class that encapsulates all the top-level data required to execute experiments (e.g., main folder path
 * where the results will be stored or experimental scenarios).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public abstract class AbstractGlobalDataContainer extends AbstractDataContainer
{
    /**
     * Params container.
     */
    public static class Params extends AbstractDataContainer.Params
    {
        /**
         * Path to the main folder where the results will be stored.
         */
        public String _mainPath = null;

        /**
         * This number represents the number of threads dispatched to execute an experimental scenario (&gt;= 1). The
         * scenario's test runs will be distributed equally (as possible) among these threads.
         */
        public int _noThreads = 1;

        /**
         * This number represents how many times an algorithm will be run when executing a scenario (number of test/trial
         * runs). The greater the number, the greater the credibility of the results (but also the greater the total
         * execution time of the experiment).
         */
        public int _noTrials = 100;

        /**
         * Labels for scenario characteristics (e.g., "PROBLEM," "ALGORITHM," etc.). Note that this field can be null if
         * the {@link AbstractGlobalDataContainer#instantiateScenarioKeyValues(Params)} is overwritten to supply ready key-values.
         * The allowed characters are: [A-Z], [0-9] by default (see {@link Params#_extraAllowedCharacters}).
         */
        public String[] _scenarioKeys = null;

        /**
         * Auxiliary field (can be null, not used) that provides a series of disabling conditions for scenarios.
         * If a scenario satisfies at least one condition (one object), it will be excluded from processing.
         */
        public ScenarioDisablingConditions[] _scenarioDisablingConditions = null;

        /**
         * Labels for scenario characteristics (their abbreviations, e.g., "PRO," "ALG," etc.).
         * If null, the abbreviations are derived automatically by taking the first three characters from their linked
         * keys (crops to the proper size if the key length is less than 3). Note that this field can be null if
         * the {@link AbstractGlobalDataContainer#instantiateScenarioKeyValues(Params)} is overwritten to supply ready key-values.
         */
        public String[] _scenarioKeysAbbreviations = null;

        /**
         * Realizations of scenario characteristics (e.g., "DTLZ" (for key = "PROBLEM"), "ALGORITHM" (for key = "NSGAII" etc.).
         * There should be one entry per one provided key (1:1 mapping).Note that this field can be null if the
         * {@link AbstractGlobalDataContainer#instantiateScenarioKeyValues(Params)} is overwritten to supply ready key-values.
         * The allowed characters are: [A-Z], [0-9] by default (see {@link Params#_extraAllowedCharacters}).
         */
        public String[][] _scenarioValues = null;

        /**
         * By default, all key/value labels are supposed to consist of only [a-z] (it will be translated into upper case),
         * [A-Z], [0-9] characters; the reason is to minimize potential errors caused, e.g., when creating folders based
         * on the keys' names; nonetheless, if needed, some EXTRA allowed character can be provided via this array.
         */
        public Character[] _extraAllowedCharacters = new Character[]{'_'};

        /**
         * This can be adjusted to introduce reordering in the specified keys. By default, they are ordered as provided
         * by the _keys array, with the proviso that some special keys (see ScenariosGenerator.getOrder method) will be
         * assigned a different ordering priority. Nonetheless, one can customize this field to impose a different order
         * on keys: the provided orders map 1:1 with the keys, and, finally, the keys will be reorganized in the ascending
         * order of these orders.
         */
        public int[] _keyValuesOrder = null;

        /**
         * If true, the monitor thread will be run along the regular trial threads
         * ({@link TrialExecutor}; responsible for executing test
         * runs within a scenario). The monitor will observe the current processing
         * state (e.g., how many test runs are completed and what the progress of
         * the rest is) and print the reports periodically to the console.
         */
        public boolean _useMonitorThread = true;

        /**
         * This number represents the delay (in milliseconds, should be non-negative) between the subsequently printed
         * reports by the monitor thread on the current scenario's progress.
         */
        public int _monitorReportingInterval = 10000;


        /**
         * Auxiliary field (can be null, not used) that provides conditions for excluding a trial from processing.
         */
        public TrialDisablingConditions _trialDisablingConditions;

        /**
         * Provides additional data on the GDC parameterization provided via command line (and parsed by {@link Parser})
         */
        protected Parser.Result _r;

        /**
         * Scenario saver objects responsible for aggregating the trail-level outcomes and storing the results in files.
         * Reference = these objects will be instantiated as clones when processing individual scenarios.
         */
        public LinkedList<IScenarioSaver> _referenceScenarioSavers;

        /**
         * Class representing a setting (to be provided by the user) that helps establish crossed scenarios ({@link CrossedScenarios}).
         */
        public CrossedSetting[] _crossedSettings;

        /**
         * The name of the folder that will store the results of the cross-examination.
         */
        public String _crossedFolderName = "CROSSED_RESULTS";

        /**
         * Saver objects responsible for aggregating the results of cross-analysis. Reference = these objects will be
         * instantiated as clones when processing individual crossed scenarios. Note that each such a saver is dedicated
         * to processing crossed scenarios at an explicitly specified level (dimensionality). Hence, it is required that
         * for each unique level yielded by the provided crossed settings {{@link AbstractGlobalDataContainer.Params#_crossedSettings}},
         * there is at least one saver with a matching level.
         */
        public LinkedList<ICrossSaver> _referenceCrossSavers;

        /**
         * Auxiliary field (can be null). If not null, it is supposed to represent a joint set of indicator names used
         * through all scenarios. It is used in cross-examination (see {}). If null, the unified set will be built
         * automatically. Note that, however, in the latter case, the order in which the identifiers are put is arbitrary.
         * Providing all the identifies in a desired order via this field bypasses the arbitrary order.
         */
        public String[] _unifiedIndicatorsNames;

        /**
         * Auxiliary field (can be null). If not null, it is supposed to represent a joint set of statistic functions names
         * used through all scenarios. It is used in cross-examination (see {}). If null, the unified set will be built
         * automatically. Note that, however, in the latter case, the order in which the identifiers are put is arbitrary.
         * Providing all the identifies in a desired order via this field bypasses the arbitrary order. Note that the
         * names of statistic functions are expected to be in the upper case (they will be transformed into the upper
         * case either way).
         */
        public String[] _unifiedStatisticFunctionsNames;

        /**
         * Random number generator initializer (it is recommended to use the default one).
         */
        public IRandomNumberGeneratorInitializer _RNGI = new DefaultRandomNumberGeneratorInitializer();
    }

    /**
     * Params container.
     */
    private final Params _p;

    /**
     * Encapsulates definitions of all experimental scenarios considered.
     */
    private Scenarios _scenarios;

    /**
     * This number represents the number of threads dispatched to execute an experimental scenario (&gt;= 1). The
     * scenario's test runs will be distributed equally (as possible) among these threads.
     */
    private int _noThreads;

    /**
     * ID of test/trial runs to be executed. This array should generally obey the following pattern: [0, 1, 2, _noTrials].
     * However, a subset of these numbers can be specified, e.g., when rerunning some particular tests or manually
     * partitioning the experiments (executing for different test batches).
     */
    private int[] _trialIDs;


    /**
     * This number represents how many times an algorithm will be run when executing a scenario (number of test/trial
     * runs). The greater the number, the greater the credibility of the results (but also the greater the total
     * execution time of the experiment). Note that the {@link AbstractGlobalDataContainer#_trialIDs} may be a subset
     * of IDs limited by this field.
     */
    private int _noTrials;

    /**
     * Path to the main folder where the results will be stored.
     */
    private String _mainPath;

    /**
     * If true, the monitor thread will be run along the regular trial threads
     * ({@link TrialExecutor}; responsible for executing test
     * runs within a scenario). The monitor will observe the current processing
     * state (e.g., how many test runs are completed and what the progress of
     * the rest is) and print the reports periodically to the console.
     */
    private boolean _useMonitorThread;

    /**
     * This number represents the delay (in milliseconds, should be non-negative) between the subsequently printed
     * reports by the monitor thread on the current scenario's progress.
     */
    private int _monitorReportingInterval;

    /**
     * By default, all key/value labels are supposed to consist of only [a-z] (it will be translated into upper case),
     * [A-Z], [0-9] characters; the reason is to mitigate potential errors caused, e.g., when creating folders based
     * on the keys' names; nonetheless, if needed, some extra allowed character can be provided via this array.
     */
    private Character[] _extraAllowedCharacters;

    /**
     * Auxiliary field (can be null, not used) that provides a series of disabling conditions for scenarios.
     * If a scenario satisfies at least one condition (one object), it will be excluded from processing.
     */
    private ScenarioDisablingConditions[] _scenarioDisablingConditions;

    /**
     * Auxiliary field (can be null, not used) that provides conditions for excluding a trial from processing.
     */
    private TrialDisablingConditions _trialDisablingConditions;

    /**
     * Scenario saver objects responsible for aggregating the trail-level outcomes and storing the results in files.
     * Reference = these objects will be cloned when processing individual scenarios.
     */
    private LinkedList<IScenarioSaver> _referenceScenarioSavers;

    /**
     * Class representing a setting (to be provided by the user) that helps establish crossed scenarios ({@link CrossedScenarios}).
     */
    private CrossedSetting[] _crossedSettings;

    /**
     * The name of the folder that will store the results of the cross-examination.
     */
    private String _crossedFolderName = "CROSSED_RESULTS";

    /**
     * Saver objects responsible for aggregating the results of cross-analysis. Reference = these objects will be
     * instantiated as clones when processing individual crossed scenarios. Note that each such a saver is dedicated
     * to processing crossed scenarios at an explicitly specified level (dimensionality). Hence, it is required that
     * for each unique level yielded by the provided crossed settings {{@link AbstractGlobalDataContainer.Params#_crossedSettings}},
     * there is at least one saver with a matching level.
     */
    private LinkedList<ICrossSaver> _referenceCrossSavers;

    /**
     * Auxiliary field (can be null). If not null, it is supposed to represent a joint set of indicator names used
     * through all scenarios. It is used in cross-examination (see {@link executor.CrossSummarizer}). If null, the
     * unified set will be built automatically. Note that, however, in the latter case, the order in which the
     * identifiers are put is arbitrary. Providing all the identifies in a desired order via this field bypasses the
     * arbitrary order.
     */
    private String[] _unifiedIndicatorsNames;

    /**
     * Auxiliary field (can be null). If not null, it is supposed to represent a joint set of statistic functions names
     * used through all scenarios. It is used in cross-examination (see {@link executor.CrossSummarizer}). If null, the
     * unified set will be built automatically. Note that, however, in the latter case, the order in which the
     * identifiers are put is arbitrary. Providing all the identifies in a desired order via this field bypasses
     * the arbitrary order. Note that the names of statistic functions are expected to be in the upper case (they will
     * be transformed into the upper case either way).
     */
    private String[] _unifiedStatisticFunctionsNames;

    /**
     * Auxiliary field that stores the information on how many requests for RNGs were conducted.
     * Note that the requests for RNGs are done sequentially in a single thread (do not need to use atomic, volatile, etc.).
     */
    private int _requestedRandomNumberGenerators;

    /**
     * Random number generator initializer (it is recommended to use the default one).
     */
    private IRandomNumberGeneratorInitializer _RNGI;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractGlobalDataContainer(Params p)
    {
        super(p);
        _p = p;
    }

    /**
     * Main method to be called when instantiating the data container.
     *
     * @param r provides additional data on the GDC parameterization provided via command line (and parsed by {@link Parser})
     * @throws GlobalException the exception will be thrown when some critical error occurs during data instantiation
     */
    public void instantiateData(Parser.Result r) throws GlobalException
    {
        _p._r = r;
        instantiateAllowedCharacters(_p);
        instantiateRNGInitializer(_p);
        instantiateScenarios(_p);
        instantiateScenarioDisablingConditions(_p);
        instantiateNoTrials(_p);
        instantiateTrialDisablingConditions(_p);
        instantiateTrialIDs(_p);
        instantiateNoThreads(_p);
        instantiateRNGStreams(_p);
        instantiateMainPath(_p);
        instantiateUseMonitorThreadFlag(_p);
        instantiateMonitorReportingDelay(_p);
        instantiateReferenceScenarioSavers(_p);
        instantiateCrossedSettings(_p);
        instantiateCrossedFolderName(_p);
        instantiateReferenceCrossSavers(_p);
        instantiateAdditionalData(_p);
        instantiateUnifiedIndicatorsNames(_p);
        instantiateUnifiedStatisticFunctionsNames(_p);
    }

    /**
     * Called by {@link container.scenario.ScenarioDataContainerFactory} when creating per-trial RNGs.
     *
     * @param scenario trial's scenario requesting the random number generator
     * @param trialID  ID of a trial requesting the random number generator
     * @return random number generator instance
     * @throws ScenarioException exception can be thrown 
     */
    public IRandom requestRandomNumberGenerator(Scenario scenario, int trialID) throws ScenarioException
    {
        _requestedRandomNumberGenerators++;
        return _RNGI.getRNG(scenario, trialID, _noTrials);
    }

    /**
     * Called when starting instantiating a scenario to request RNG streams creation.
     *
     * @param scenario scenario being processed
     * @throws ScenarioException scenario exception can be thrown 
     */
    public void requestStreamsCreationDuringSDCInit(Scenario scenario) throws ScenarioException
    {
        _RNGI.requestStreamsCreationDuringSDCInit(scenario, _noTrials);
    }

    /**
     * Instantiates the RNG initializer.
     *
     * @param p params container
     * @throws GlobalException exception will be thrown when the RNG initializer is not provided.
     */
    private void instantiateRNGInitializer(Params p) throws GlobalException
    {
        _requestedRandomNumberGenerators = 0;
        _RNGI = p._RNGI;

        if (_RNGI == null)
        {
            throw new GlobalException("The random number generator initializer is not provided", null, this.getClass());
        }
    }

    /**
     * Instantiates the RNG streams.
     *
     * @param p params container
     * @throws GlobalException exception will be thrown when the RNG streams cannot be instantiated
     */
    private void instantiateRNGStreams(Params p) throws GlobalException
    {
        // scenarios and trials are already instantiated
        _RNGI.requestStreamsCreationDuringGDCInit(_scenarios.getScenarios().length, _noTrials);
    }

    /**
     * Instantiates definitions of all considered experimental scenarios.
     *
     * @param p params container
     * @throws GlobalException exception will be thrown when the input data for scenarios initialization is invalid
     */
    private void instantiateScenarios(Params p) throws GlobalException
    {
        try
        {
            KeyValues[] kv = instantiateScenarioKeyValues(p);
            _scenarios = ScenariosGenerator.getScenarios(kv, getAllowedCharacters(), p._keyValuesOrder);
        } catch (Exception e)
        {
            throw new GlobalException(e.getMessage(), this.getClass(), e);
        }
    }

    /**
     * Instantiates scenario disabling conditions.
     *
     * @param p params container
     * @throws GlobalException exception will be thrown when there are nulled elements in {@link Params#_scenarioDisablingConditions}.
     */
    private void instantiateScenarioDisablingConditions(Params p) throws GlobalException
    {
        if ((p._r != null) && (p._r._scenarioDisablingConditions != null)) // takes priority
        {
            ScenarioDisablingConditions[] sdc = new ScenarioDisablingConditions[_p._r._scenarioDisablingConditions.size()];
            int idx = 0;
            for (ScenarioDisablingConditions s : _p._r._scenarioDisablingConditions) sdc[idx++] = s;
            _scenarioDisablingConditions = sdc;
        }
        else _scenarioDisablingConditions = p._scenarioDisablingConditions;

        if (_scenarioDisablingConditions != null)
            for (ScenarioDisablingConditions sdc : _scenarioDisablingConditions)
                if (sdc == null)
                    throw new GlobalException("One of the scenario disabling conditions is null", null, this.getClass());
    }

    /**
     * Instantiates the total number of trials.
     *
     * @param p params container
     */
    private void instantiateNoTrials(Params p)
    {
        _noTrials = p._noTrials;
    }

    /**
     * Instantiates trial disabling conditions.
     *
     * @param p params container
     */
    private void instantiateTrialDisablingConditions(Params p)
    {
        if ((p._r != null) && (p._r._disabledTrials != null)) // takes priority
        {
            _trialDisablingConditions = new TrialDisablingConditions(p._noTrials);
            for (Integer id : _p._r._disabledTrials) _trialDisablingConditions.disableTrial(id);
        }
        else _trialDisablingConditions = p._trialDisablingConditions;
    }


    /**
     * This method can be overwritten to supply the container with key-values that define the experimental scenarios
     * (characteristics and their concrete values).
     *
     * @param p params container
     * @return key-values defining the experimental scenarios
     * @throws GlobalException exception will be thrown when the input data for key-values is invalid
     */
    protected KeyValues[] instantiateScenarioKeyValues(Params p) throws GlobalException
    {
        if (p._scenarioKeys == null)
            throw new GlobalException("Scenario keys are not provided (the array is null)", null, this.getClass());
        if (p._scenarioKeys.length == 0)
            throw new GlobalException("Scenario keys are not provided (the array is empty)", null, this.getClass());
        if (p._scenarioValues == null)
            throw new GlobalException("Scenario values are not provided (the array is null)", null, this.getClass());
        if (p._scenarioValues.length != p._scenarioKeys.length)
            throw new GlobalException("The number of scenario keys differs from the length of the array containing scenario values", null, this.getClass());

        // the remaining exception cases are handled by ScenarioGenerator
        KeyValues[] kv = new KeyValues[p._scenarioKeys.length];
        for (int i = 0; i < p._scenarioKeys.length; i++)
        {
            String abbrev = Key.getKeyAbbreviation(p._scenarioKeys[i], p._scenarioKeysAbbreviations, i);
            kv[i] = KeyValues.getInstance(p._scenarioKeys[i], abbrev, p._scenarioValues[i], null);
        }

        return kv;
    }


    /**
     * Instantiates the number of threads.
     *
     * @param p params container
     * @throws GlobalException the exception will be thrown when the number of threads specified is less than 1
     */
    private void instantiateNoThreads(Params p) throws GlobalException
    {
        if ((p._r != null) && (_p._r._noThreads != null)) _noThreads = _p._r._noThreads; // takes priority
        else _noThreads = p._noThreads;
        if (_noThreads < 1)
            throw new GlobalException("The number of threads should be not less than 1", null, this.getClass());
    }

    /**
     * Method that instantiates trial IDs.
     *
     * @param p params container
     * @throws GlobalException the exception will be thrown when the trial IDs are invalid
     */
    private void instantiateTrialIDs(Params p) throws GlobalException
    {
        if (p._noTrials < 1)
            throw new GlobalException("The number of trials should be not less than 1", null, this.getClass());
        LinkedList<Integer> validIds = new LinkedList<>();
        for (int i = 0; i < p._noTrials; i++)
            if ((_trialDisablingConditions == null) || (!_trialDisablingConditions.isTrialDisabled(i)))
                validIds.add(i);

        if (validIds.isEmpty()) throw new GlobalException("Trial IDs is an empty list", null, this.getClass());

        _trialIDs = new int[validIds.size()];
        int idx = 0;
        for (Integer id : validIds) _trialIDs[idx++] = id;

        Set<Integer> ids = new HashSet<>(_trialIDs.length);
        for (int id : _trialIDs)
        {
            if (ids.contains(id))
                throw new GlobalException("Trial ID = " + id + " is not unique", null, this.getClass());
            ids.add(id);
        }
    }


    /**
     * Instantiates the main folder where the results will be stored.
     *
     * @param p params container
     * @throws GlobalException the exception will be thrown when the main path is not provided
     */
    private void instantiateMainPath(Params p) throws GlobalException
    {
        if (p._mainPath == null)
            throw new GlobalException("The main path is not provided (is null)", null, this.getClass());
        _mainPath = p._mainPath;
    }

    /**
     * Instantiates the monitor flag.
     *
     * @param p params container
     */
    private void instantiateUseMonitorThreadFlag(Params p)
    {
        _useMonitorThread = p._useMonitorThread;
    }


    /**
     * Instantiates the monitor reporting interval.
     *
     * @param p params container
     * @throws GlobalException the exception will be thrown when the reporting interval is negative while the monitor thread is requested
     */
    protected void instantiateMonitorReportingDelay(Params p) throws GlobalException
    {
        if ((p._useMonitorThread) && (p._monitorReportingInterval < 0))
            throw new GlobalException("The reporting interval should not be negative if the monitor thread is used", null, this.getClass());
        _monitorReportingInterval = p._monitorReportingInterval;
    }


    /**
     * Instantiates the allowed characters.
     *
     * @param p params container
     */
    private void instantiateAllowedCharacters(Params p)
    {
        _extraAllowedCharacters = p._extraAllowedCharacters;
    }


    /**
     * Instantiates the reference scenario savers.
     *
     * @param p params container
     */
    private void instantiateReferenceScenarioSavers(Params p)
    {
        _referenceScenarioSavers = p._referenceScenarioSavers;
    }


    /**
     * Instantiates the crossed settings.
     *
     * @param p params container
     */
    private void instantiateCrossedSettings(Params p)
    {
        _crossedSettings = p._crossedSettings;
    }

    /**
     * Instantiates the crossed folder name.
     *
     * @param p params container
     */
    private void instantiateCrossedFolderName(Params p)
    {
        _crossedFolderName = p._crossedFolderName;
    }

    /**
     * Instantiates the reference cross-savers.
     *
     * @param p params container
     */
    private void instantiateReferenceCrossSavers(Params p)
    {
        _referenceCrossSavers = p._referenceCrossSavers;
    }

    /**
     * Instantiates the unified indicators names.
     *
     * @param p params container
     */
    private void instantiateUnifiedIndicatorsNames(Params p)
    {
        _unifiedIndicatorsNames = p._unifiedIndicatorsNames;
    }

    /**
     * Instantiates the unified statistic functions names.
     *
     * @param p params container
     */
    private void instantiateUnifiedStatisticFunctionsNames(Params p)
    {
        _unifiedStatisticFunctionsNames = p._unifiedStatisticFunctionsNames;
    }

    /**
     * l
     * Can be overwritten to supply the container with additional data (e.g., heavy, read-only objects).
     *
     * @param p params container
     * @throws GlobalException the method does not throw an exception, but the signature includes the exception so that the method's exception may use it
     */
    protected void instantiateAdditionalData(Params p) throws GlobalException
    {

    }

    /**
     * Getter for the definitions of all experimental scenarios considered.
     *
     * @return all experimental scenarios considered
     */
    public Scenarios getScenarios()
    {
        return _scenarios;
    }

    /**
     * Getter for the  disabling conditions for scenarios.
     * If a scenario satisfies at least one condition (one object), it will be excluded from processing.
     *
     * @return scenario disabling conditions
     */
    public ScenarioDisablingConditions[] getScenarioDisablingConditions()
    {
        return _scenarioDisablingConditions;
    }

    /**
     * Getter for the number of threads dispatched to execute an experimental scenario (&gt;= 1).
     *
     * @return the number of threads to be dispatched
     */
    public int getNoThreads()
    {
        return _noThreads;
    }

    /**
     * Getter for the ID of test/trial runs to be executed.
     *
     * @return test runs IDs
     */
    public int[] getTrialIDs()
    {
        return _trialIDs;
    }

    /**
     * Returns the number of trials (includes disabled trials).
     *
     * @return the number of trials (includes disabled trials)
     */
    public int getNoTrials()
    {
        return _p._noTrials;
    }

    /**
     * Getter for the number of unique trials to be processed (excludes disabled trials).
     *
     * @return the number of unique trials to be processed  (excludes disabled trials)
     */
    public int getNoEnabledTrials()
    {
        return _trialIDs.length;
    }

    /**
     * Getter for the path to the main folder where the results will be stored.
     *
     * @return path to the main folder where the results will be stored
     */
    public String getMainPath()
    {
        return _mainPath;
    }

    /**
     * Can be used to check whether the monitor thread is expected to be dispatched.
     *
     * @return true = the monitor thread is expected to be dispatched; false otherwise
     */
    public boolean useMonitorThread()
    {
        return _useMonitorThread;
    }

    /**
     * Getter for the monitor reporting interval.
     *
     * @return monitor reporting interval
     */
    public int getMonitorReportingInterval()
    {
        return _monitorReportingInterval;
    }

    /**
     * Getter for the reference scenario savers.
     *
     * @return reference scenario savers.
     */
    public LinkedList<IScenarioSaver> getReferenceScenarioSavers()
    {
        return _referenceScenarioSavers;
    }

    /**
     * Getter for all crossed settings for cross-examination
     *
     * @return all crossed settings
     */
    public CrossedSetting[] getCrossedSettings()
    {
        return _crossedSettings;
    }


    /**
     * Getter for the crossed folder name (folder containing the results of cross-examination).
     *
     * @return crossed folder name
     */
    public String getCrossedFolderName()
    {
        return _crossedFolderName;
    }

    /**
     * Getter for the reference cross-savers.
     *
     * @return reference cross-savers.
     */
    public LinkedList<ICrossSaver> getReferenceCrossSavers()
    {
        return _referenceCrossSavers;
    }


    /**
     * Getter for the additional (optional, can be null) characters that can be used, e.g., in scenario names.
     *
     * @return allowed characters (stored in a set)
     */
    public Set<Character> getAllowedCharacters()
    {
        if (_extraAllowedCharacters == null) return null;
        Set<Character> characters = new HashSet<>();
        Collections.addAll(characters, _extraAllowedCharacters);
        return characters;
    }

    /**
     * Getter for the auxiliary field that represents unified indicator names. If not null, it is supposed to represent
     * a joint set of indicator names used through all scenarios. It is used in cross-examination
     * (see {@link executor.CrossSummarizer}). If null, the unified set will be built automatically. Note that, however,
     * in the latter case, the order in which the identifiers are put is arbitrary. Providing all the identifies in
     * a desired order via this field bypasses the arbitrary order.
     *
     * @return unified indicators names
     */
    public String[] getUnifiedIndicatorsNames()
    {
        return _unifiedIndicatorsNames;
    }

    /**
     * Getter for the auxiliary field that represents unified statistic functions names. If not null, it is supposed to
     * represent a joint set of indicator names used through all scenarios. It is used in cross-examination
     * (see {@link executor.CrossSummarizer}). If null, the unified set will be built automatically. Note that, however,
     * in the latter case, the order in which the identifiers are put is arbitrary. Providing all the identifies in
     * a desired order via this field bypasses the arbitrary order.
     *
     * @return unified indicators names
     */
    public String[] getUnifiedStatisticFunctionsNames()
    {
        return _unifiedStatisticFunctionsNames;
    }


    /**
     * Auxiliary method for clearing the data.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _scenarios = null;
        _noThreads = 0;
        _trialIDs = null;
        _mainPath = null;
        _extraAllowedCharacters = null;
        _scenarioDisablingConditions = null;
        _crossedSettings = null;
        _crossedFolderName = null;
        _unifiedIndicatorsNames = null;
        _unifiedStatisticFunctionsNames = null;
    }
}
