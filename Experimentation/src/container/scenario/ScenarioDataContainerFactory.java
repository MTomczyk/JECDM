package container.scenario;

import container.AbstractDataContainer;
import container.global.AbstractGlobalDataContainer;
import container.scenario.intializers.*;
import exception.ScenarioException;
import indicator.IIndicator;
import io.trial.BinaryLoader;
import scenario.Scenario;
import statistics.IStatistic;


/**
 * Factory object for creating extensions of {@link AbstractScenarioDataContainer}.
 * This class and its main method are intended to be overwritten.
 *
 * @author MTomczyk
 */


public class ScenarioDataContainerFactory extends AbstractDataContainer
{
    /**
     * Params container
     */
    public static class Params extends AbstractDataContainer.Params
    {
        /**
         * The number of generations the EA will be run.
         */
        public int _generations = 1;

        /**
         * The number of steady-state repeats per one generation.
         */
        public int _steadyStateRepeats = 1;

        /**
         * The number of objectives considered.
         */
        public int _objectives = 1;

        /**
         * Performance indicators used when assessing the performance of the algorithms.
         */
        public IIndicator[] _indicators;

        /**
         * Statistic functions used to aggregate the results of all trial runs. One statistic is returned per generation.
         */
        public IStatistic[] _statistics;

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
        public int _dataStoringInterval = Integer.MAX_VALUE;

        /**
         * This field has a similar purpose to {}. The difference is that, however, this field is related to loading,
         * not saving. When all trials are successfully completed, the scenario executor proceeds to generate a scenario
         * summary file. For this reason, it must load all the per-trial-related data. To avoid excessive memory usage,
         * the data can be loaded in chunks, where this field specifies an upper cap for the number of elements
         * (generations) loaded in one iteration of data parsing.
         */
        public int _dataLoadingInterval = Integer.MAX_VALUE;

        /**
         * The object for retrieving instances of {@link AbstractScenarioDataContainer}.
         */
        public IInstanceGetter _instanceGetter;

        /**
         * Object responsible for initializing the per-scenario problem id (string representation)
         */
        public IProblemIDInitializer _problemIdInitializer;

        /**
         * Object responsible for initializing the per-scenario number of objectives.
         */
        public INumberOfObjectivesInitializer _numberOfObjectivesInitializer;

        /**
         * Object responsible for initializing the per-scenario number of generations EAs are supposed to run for.
         */
        public INumberOfGenerationsInitializer _numberOfGenerationsInitializer;

        /**
         * Object responsible for initializing the per-scenario number of steady-state repeats.
         */
        public INumberOfSteadyStateRepeatsInitializer _numberOfSteadyStateRepeatsInitializer;

        /**
         * Object responsible for initializing the per-scenario performance indicators.
         */
        public IIndicatorsInitializer _indicatorsInitializer;

        /**
         * Object responsible for initializing the per-scenario statistic functions (aggregate per-trial outcomes).
         */
        public IStatisticFunctionsInitializer _statisticFunctionsInitializer;
    }

    /**
     * Specifies a binary loader for loading per-trial data.
     */
    private final BinaryLoader _binaryLoader;

    /**
     * The number of generations the EA will be run for.
     */
    private final int _generations;

    /**
     * The number of objectives considered.
     */
    private final int _objectives;

    /**
     * The number of steady-state repeats per one generation.
     */
    private final int _steadyStateRepeats;

    /**
     * Performance indicators used when assessing the performance of the algorithms.
     */
    private final IIndicator[] _indicators;

    /**
     * Statistic functions used to aggregate the results of all trial runs. One statistic is returned per generation.
     */
    private final IStatistic[] _statistics;

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
    private final int _dataStoringInterval;

    /**
     * This field has a similar purpose to {@link ScenarioDataContainerFactory#_dataStoringInterval}. The difference is
     * that, however, this field is related to loading, not saving. When all trials are successfully completed, the
     * scenario executor proceeds to generate a scenario summary file. For this reason, it must load all the
     * per-trial-related data. To avoid excessive memory usage, the data can be loaded in chunks, where this field
     * specifies an upper cap for the number of elements (generations) loaded in one iteration of data parsing.
     */
    private final int _dataLoadingInterval;

    /**
     * The object for retrieving instances of {@link AbstractScenarioDataContainer}.
     */
    private final IInstanceGetter _instanceGetter;

    /**
     * Object responsible for initializing the per-scenario problem id (string representation)
     */
    private final IProblemIDInitializer _problemIdInitializer;

    /**
     * Object responsible for initializing the per-scenario number of objectives.
     */
    private final INumberOfObjectivesInitializer _numberOfObjectivesInitializer;

    /**
     * Object responsible for initializing the per-scenario number of generations EAs are supposed to run for.
     */
    private final INumberOfGenerationsInitializer _numberOfGenerationsInitializer;

    /**
     * Object responsible for initializing the per-scenario number of steady-state repeats.
     */
    private final INumberOfSteadyStateRepeatsInitializer _numberOfSteadyStateRepeatsInitializer;

    /**
     * Object responsible for initializing the per-scenario performance indicators.
     */
    private final IIndicatorsInitializer _indicatorsInitializer;

    /**
     * Object responsible for initializing the per-scenario statistic functions (aggregate per-trial outcomes).
     */
    private final IStatisticFunctionsInitializer _statisticFunctionsInitializer;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    @SuppressWarnings("ReplaceNullCheck")
    public ScenarioDataContainerFactory(Params p)
    {
        super(p);
        _binaryLoader = new BinaryLoader();
        _generations = p._generations;
        _steadyStateRepeats = p._steadyStateRepeats;
        _objectives = p._objectives;
        _indicators = p._indicators;
        _dataStoringInterval = p._dataStoringInterval;
        _dataLoadingInterval = p._dataLoadingInterval;
        _statistics = p._statistics;
        if (p._instanceGetter != null) _instanceGetter = p._instanceGetter;
        else _instanceGetter = new DefaultInstanceGetter();
        if (p._problemIdInitializer != null) _problemIdInitializer = p._problemIdInitializer;
        else _problemIdInitializer = new DefaultProblemIDInitializer();
        if (p._numberOfGenerationsInitializer != null)
            _numberOfGenerationsInitializer = p._numberOfGenerationsInitializer;
        else _numberOfGenerationsInitializer = new DefaultNumberOfGenerationsInitializer();
        if (p._numberOfObjectivesInitializer != null) _numberOfObjectivesInitializer = p._numberOfObjectivesInitializer;
        else _numberOfObjectivesInitializer = new DefaultNumberOfObjectivesInitializer();
        if (p._numberOfSteadyStateRepeatsInitializer != null)
            _numberOfSteadyStateRepeatsInitializer = p._numberOfSteadyStateRepeatsInitializer;
        else _numberOfSteadyStateRepeatsInitializer = new DefaultNumberOfSteadyStateRepeatsInitializer();
        if (p._indicatorsInitializer != null) _indicatorsInitializer = p._indicatorsInitializer;
        else _indicatorsInitializer = new DefaultIndicatorsInitializer();
        if (p._statisticFunctionsInitializer != null) _statisticFunctionsInitializer = p._statisticFunctionsInitializer;
        else _statisticFunctionsInitializer = new DefaultStatisticFunctionsInitializer();
    }

    /**
     * Default getter for the scenario data container instance. This method instantiates the params container and
     * delegates the scenario data getter creation to {@link ScenarioDataContainerFactory#getInstance(AbstractScenarioDataContainer.Params)}.
     * The latter method is intended to be overwritten.
     *
     * @param GDC       the global data container
     * @param scenario  the input experimental scenario considered
     * @param validator validator used to check the data
     * @return the instance of the scenario data container
     * @throws ScenarioException the exception can be thrown and passed to GDC
     */
    public AbstractScenarioDataContainer getInstance(AbstractGlobalDataContainer GDC, Scenario scenario, Validator validator) throws ScenarioException
    {
        validator.validateDataStoringInterval(_dataStoringInterval);
        validator.validateDataLoadingInterval(_dataLoadingInterval);

        AbstractScenarioDataContainer.Params p = new AbstractScenarioDataContainer.Params(GDC, scenario, validator);
        passParams(p);

        p._binaryLoader = _binaryLoader;
        p._generations = _generations;
        p._objectives = _objectives;
        p._steadyStateRepeats = _steadyStateRepeats;
        p._indicators = _indicators;
        p._dataStoringInterval = _dataStoringInterval;
        p._dataLoadingInterval = _dataLoadingInterval;
        p._statistics = _statistics;
        p._problemIdInitializer = _problemIdInitializer;
        p._numberOfObjectivesInitializer = _numberOfObjectivesInitializer;
        p._numberOfGenerationsInitializer = _numberOfGenerationsInitializer;
        p._numberOfSteadyStateRepeatsInitializer = _numberOfSteadyStateRepeatsInitializer;
        p._indicatorsInitializer = _indicatorsInitializer;
        p._statisticFunctionsInitializer = _statisticFunctionsInitializer;
        return getInstance(p);
    }

    /**
     * Getter for the scenario data container instance (intended to be overwritten).
     *
     * @param p params container
     * @return the instance of the scenario data container
     * @throws ScenarioException the exception can be thrown and passed to GDC
     */
    private AbstractScenarioDataContainer getInstance(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        return _instanceGetter.getInstance(p);
    }


    /**
     * Auxiliary method for data release.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        if (_indicators != null) for (IIndicator indicator : _indicators) indicator.dispose();
    }
}
