package container.scenario;

import container.AbstractDataContainer;
import container.global.AbstractGlobalDataContainer;
import container.scenario.intializers.*;
import exception.ScenarioException;
import exception.TrialException;
import indicator.IIndicator;
import io.trial.BinaryLoader;
import io.trial.TLPITrialWrapper;
import scenario.Scenario;
import statistics.IStatistic;

import java.io.File;

/**
 * A container class that limits the scope to a single scenario (i.e., the problem is selected, the algorithm, etc.).
 * This abstract class provides various fields and methods that can be commonly used in diverse implementations.
 * Important note: this class is supposed to be extended, and its method is to be overwritten to impose some
 * interpretability on the (input) scenario.
 *
 * @author MTomczyk
 */
public abstract class AbstractScenarioDataContainer extends AbstractDataContainer
{
    /**
     * Params container
     */
    public static class Params extends ScenarioDataContainerFactory.Params
    {
        /**
         * Reference to the global data container.
         */
        public AbstractGlobalDataContainer _GDC;

        /**
         * Reference to the concrete scenario being processed.
         */
        public Scenario _scenario;

        /**
         * Provides auxiliary methods for data validation.
         */
        protected Validator _validator;

        /**
         * Specifies a binary loader for loading per-trial data.
         */
        protected BinaryLoader _binaryLoader;

        /**
         * Params container.
         *
         * @param GDC       reference to the global data container
         * @param scenario  reference to the concrete scenario being processed
         * @param validator provides auxiliary methods for data validation
         */
        public Params(AbstractGlobalDataContainer GDC, Scenario scenario, Validator validator)
        {
            _GDC = GDC;
            _scenario = scenario;
            _validator = validator;
        }

    }

    /**
     * Reference to the global data container.
     */
    protected AbstractGlobalDataContainer _GDC;

    /**
     * Specifies a binary loader for loading per-trial data.
     */
    protected BinaryLoader _binaryLoader;

    /**
     * The object containing binary loaders responsible for retrieving already stored data given the trial ID and performance indicator.
     */
    protected TLPITrialWrapper _binaryLoaders;

    /**
     * Reference to the concrete scenario being processed.
     */
    protected Scenario _scenario;

    /**
     * The number of generations the EA will be run.
     */
    protected int _generations = 1;

    /**
     * The number of steady-state repeats per one generation.
     */
    protected int _steadyStateRepeats = 1;

    /**
     * The number of objectives considered.
     */
    protected int _objectives = 1;

    /**
     * Performance indicators used when assessing the performance of the algorithms.
     */
    protected IIndicator[] _indicators;

    /**
     * Statistic functions used to aggregate the results of all trial runs. One statistic is returned per generation.
     */
    protected IStatistic[] _statisticFunctions;

    /**
     * By default, each trial result (one run assessed via one indicator) is stored in a single binary file.
     * The result matrices are instantiated before processing. It is assumed that they will handle the results
     * obtained throughout all generations, and then, after the simulation, this data will be transferred to
     * relevant files. However, in this view, extensive simulations may consume a lot of memory due to matrix
     * allocation. As a resolution, the data may be stored in smaller matrices but transferred into linked-in
     * files more frequently instead. Overall, this field represents the upper cap for the number of generations
     * involved in one data transfer. By default, it is set to Integer.MAX_VALUE, meaning that there will be just
     * one data transfer. Setting it to a value smaller than the number of generations an algorithm is to be run
     * will chunk this process into smaller pieces. E.g., if it equals 100, the data will be transferred after
     * every 100 generations. Note that it is not required that the generation number will be an integer multiple
     * of this field.
     */
    protected int _dataStoringInterval;

    /**
     * This field has a similar purpose to data storing interval of {@link ScenarioDataContainerFactory}. The difference is
     * that, however, this field is related to loading, not saving. When all trials are successfully completed, the
     * scenario executor proceeds to generate a scenario summary file. For this reason, it must load all the
     * per-trial-related data. To avoid excessive memory usage, the data can be loaded in chunks, where this field
     * specifies an upper cap for the number of elements (generations) loaded in one iteration of data parsing.
     */
    protected int _dataLoadingInterval;

    /**
     * Problem ID.
     */
    protected String _problemID;

    /**
     * Stores the information on the main path leading to the scenario folder (global main path + separator + scenario's name).
     */
    protected String _mainPath;

    /**
     * Object responsible for initializing the per-scenario problem id (string representation)
     */
    protected final IProblemIDInitializer _problemIdInitializer;

    /**
     * Object responsible for initializing the per-scenario number of objectives.
     */
    protected INumberOfObjectivesInitializer _numberOfObjectivesInitializer;

    /**
     * Object responsible for initializing the per-scenario number of generations EAs are supposed to run for.
     */
    protected INumberOfGenerationsInitializer _numberOfGenerationsInitializer;

    /**
     * Object responsible for initializing the per-scenario number of steady-state repeats.
     */
    protected INumberOfSteadyStateRepeatsInitializer _numberOfSteadyStateRepeatsInitializer;

    /**
     * Object responsible for initializing the per-scenario performance indicators.
     */
    protected IIndicatorsInitializer _indicatorsInitializer;

    /**
     * Object responsible for initializing the per-scenario statistic functions (aggregate per-trial outcomes).
     */
    protected IStatisticFunctionsInitializer _statisticFunctionsInitializer;


    /**
     * Parameterized constructor that also instantiates the data.
     *
     * @param p params container
     * @throws ScenarioException the exception can be thrown and passed to the main executor
     */
    public AbstractScenarioDataContainer(Params p) throws ScenarioException
    {
        super(p);
        _GDC = p._GDC;
        _scenario = p._scenario;
        _problemIdInitializer = p._problemIdInitializer;
        _numberOfObjectivesInitializer = p._numberOfObjectivesInitializer;
        _numberOfGenerationsInitializer = p._numberOfGenerationsInitializer;
        _numberOfSteadyStateRepeatsInitializer = p._numberOfSteadyStateRepeatsInitializer;
        _indicatorsInitializer = p._indicatorsInitializer;
        _statisticFunctionsInitializer = p._statisticFunctionsInitializer;
        instantiateData(p);
    }

    /**
     * The main method that calls the auxiliary ones to instantiate the scenario-related data.
     *
     * @param p params container
     * @throws ScenarioException the exception can be thrown and passed to the main executor
     */
    protected void instantiateData(Params p) throws ScenarioException
    {
        instantiateProblemID(p);
        instantiateTheNumberOfObjectives(p);
        instantiateGenerations(p);
        instantiateSteadyStateRepeats(p);
        instantiateIndicators(p);
        instantiateStatisticFunctions(p);

        instantiateMainPath(p);
        instantiateDataStoringInterval(p);
        instantiateDataLoadingInterval(p);
        instantiateReferenceBinaryLoader(p);
        instantiateBinaryLoaders(p);

        requestStreamsCreationDuringSDCInit(p._scenario);

        p._validator.validateDataStoringInterval(_dataStoringInterval);
        p._validator.validateDataLoadingInterval(_dataLoadingInterval);
        p._validator.validateGenerations(_generations);
        p._validator.validateSteadyStateRepeats(_steadyStateRepeats);
        p._validator.validateIndicators(_indicators, _GDC);
        p._validator.validateStatisticFunctions(_statisticFunctions, _GDC);
    }

    /**
     * Called when starting instantiating a scenario to request RNG streams creation.
     *
     * @param scenario scenario being processed
     * @throws ScenarioException scenario exception can be thrown and propagated higher
     */
    private void requestStreamsCreationDuringSDCInit(Scenario scenario) throws ScenarioException
    {
        _GDC.requestStreamsCreationDuringSDCInit(scenario);
    }

    /**
     * Instantiates the scenario's main path (global main path + separator + scenario's name).
     *
     * @param p params container
     */
    private void instantiateMainPath(Params p)
    {
        _mainPath = p._GDC.getMainPath() + File.separatorChar + _scenario.toString();
    }

    /**
     * Instantiates the field representing how often (interval; in generations) the data stored in the results matrices
     * should be transferred into the files.
     *
     * @param p params container
     */
    private void instantiateDataStoringInterval(Params p)
    {
        _dataStoringInterval = p._dataStoringInterval;
    }

    /**
     * Instantiates the field representing how often (interval; in generations) the data stored in the trial-level files
     * should be transferred into the memory for further processing
     *
     * @param p params container
     */
    private void instantiateDataLoadingInterval(Params p)
    {
        _dataLoadingInterval = p._dataLoadingInterval;
    }

    /**
     * Instantiates the binary loader.
     *
     * @param p params container
     */
    private void instantiateReferenceBinaryLoader(Params p)
    {
        _binaryLoader = p._binaryLoader;
    }

    /**
     * Instantiates the loaders.
     *
     * @param p params container
     * @throws ScenarioException the exception will be thrown if the loaders cannot be instantiated
     */
    private void instantiateBinaryLoaders(Params p) throws ScenarioException
    {
        _binaryLoaders = new TLPITrialWrapper(p._GDC);
        for (Integer id : _GDC.getTrialIDs())
        {
            try
            {
                _binaryLoaders.addLoaders(_binaryLoader, _indicators, _mainPath, _scenario, id);
            } catch (TrialException e)
            {
                throw new ScenarioException("The binary loader for trial ID = " + id + " could not be instantiated " + e.getDetailedReasonMessage(), this.getClass(), e, _scenario);
            }
        }
    }

    /**
     * Instantiates the scenario's problem ID (string).
     *
     * @param p params container
     * @throws ScenarioException the exception can be thrown and passed to the main executor
     */
    private void instantiateProblemID(Params p) throws ScenarioException
    {
        _problemID = _problemIdInitializer.instantiateProblemID(p);
    }

    /**
     * Auxiliary method that instantiates the number of objectives
     *
     * @param p params container
     * @throws ScenarioException the exception can be thrown and passed to the main executor
     */
    protected void instantiateTheNumberOfObjectives(Params p) throws ScenarioException
    {
        _objectives = _numberOfObjectivesInitializer.instantiateNumberOfObjectives(p);
    }

    /**
     * Instantiates the number of generations.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    protected void instantiateGenerations(Params p) throws ScenarioException
    {
        _generations = _numberOfGenerationsInitializer.instantiateGenerations(p);
    }

    /**
     * Instantiates the number of steady-state repeats.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    protected void instantiateSteadyStateRepeats(Params p) throws ScenarioException
    {
        try
        {
            _steadyStateRepeats = _numberOfSteadyStateRepeatsInitializer.instantiateSteadyStateRepeats(p);
        } catch (Exception e)
        {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, _scenario);
        }
    }

    /**
     * Instantiates the indicators used when assessing the performance of the algorithms.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    protected void instantiateIndicators(Params p) throws ScenarioException
    {
        _indicators = _indicatorsInitializer.instantiateIndicators(p);
    }


    /**
     * Instantiates the statistic functions used when aggregating the outcomes of test runs
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    protected void instantiateStatisticFunctions(Params p) throws ScenarioException
    {
        _statisticFunctions = _statisticFunctionsInitializer.instantiateStatisticFunctions(p);
    }


    /**
     * Returns the string representation (the same as for the wrapped {@link Scenario} object).
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _scenario.toString();
    }

    /**
     * Getter for the global data container.
     *
     * @return global data container
     */
    public AbstractGlobalDataContainer getGDC()
    {
        return _GDC;
    }

    /**
     * Getter for the scenario object.
     *
     * @return scenario object
     */
    public Scenario getScenario()
    {
        return _scenario;
    }

    /**
     * Getter for the number of generations.
     *
     * @return the number of generations
     */
    public int getGenerations()
    {
        return _generations;
    }


    /**
     * Getter for the number of steady-state repeats.
     *
     * @return the number of steady-state repeats
     */
    public int getSteadyStateRepeats()
    {
        return _steadyStateRepeats;
    }

    /**
     * Getter for the indicators used when assessing the performance of the algorithms.
     *
     * @return indicators used when assessing the performance of the algorithms
     */
    public IIndicator[] getIndicators()
    {
        return _indicators;
    }

    /**
     * Getter for the statistic functions used to aggregate the results of all trial runs. One statistic is returned per generation.
     *
     * @return statistic functions
     */
    public IStatistic[] getStatisticFunctions()
    {
        return _statisticFunctions;
    }

    /**
     * Getter for the value representing how often (interval; in generations) the data stored in the results matrices
     * should be transferred into the files.
     *
     * @return the value representing how often (interval; in generations) the data stored in the results matrices should
     * be transferred into the files
     */
    public int getDataStoreInterval()
    {
        return _dataStoringInterval;
    }

    /**
     * Getter for the value representing the maximum size of data chunks being loaded when generating scenario-level result files.
     *
     * @return the value representing the maximum size of data chunks being loaded when generating scenario-level result files
     */
    public int getDataLoadingInterval()
    {
        return _dataLoadingInterval;
    }

    /**
     * Returns problem ID (can be null if not instantiated).
     *
     * @return problem ID
     */
    public String getProblemID()
    {
        return _problemID;
    }

    /**
     * Getter for the number of objectives.
     *
     * @return the number of objectives (null = the number of objectives was not supplied)
     */
    public Integer getObjectives()
    {
        return _objectives;
    }

    /**
     * Getter for the scenario's main path (global main path + separator + scenario's name).
     *
     * @return scenario's main path
     */
    public String getMainPath()
    {
        return _mainPath;
    }

    /**
     * Getter for the binary loaders.
     *
     * @return binary loaders
     */
    public TLPITrialWrapper getBinaryLoaders()
    {
        return _binaryLoaders;
    }

    /**
     * Auxiliary method for clearing the data.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public void dispose()
    {
        super.dispose();
        if (_indicators != null) for (IIndicator i : _indicators) i.dispose();
        _mainPath = null;
        _indicators = null;
        _binaryLoader = null;
        _GDC = null;
        _scenario = null;
        _numberOfObjectivesInitializer = null;
        _numberOfGenerationsInitializer = null;
        _numberOfSteadyStateRepeatsInitializer = null;
        _indicatorsInitializer = null;
        _statisticFunctionsInitializer = null;
    }
}
