package container.scenario;

import container.global.AbstractGlobalDataContainer;
import exception.ScenarioException;
import indicator.IIndicator;
import io.FileUtils;
import scenario.Scenario;
import statistics.IStatistic;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides auxiliary methods for data validation (for scenario-level).
 *
 * @author MTomczyk
 */
public class Validator
{
    /**
     * Reference to the currently processed scenario.
     */
    private final Scenario _scenario;

    /**
     * If true, statistic functions are checked; false otherwise.
     */
    private final boolean _validateStatisticFunctions;

    /**
     * Parameterized constructor.
     *
     * @param scenario                   reference to the currently processed scenario
     * @param validateStatisticFunctions if true, statistic functions are checked; false otherwise
     */
    public Validator(Scenario scenario,
                     boolean validateStatisticFunctions)
    {
        _scenario = scenario;
        _validateStatisticFunctions = validateStatisticFunctions;
    }

    /**
     * Auxiliary method for validating the generation number.
     *
     * @param generations generations number
     * @throws ScenarioException the exception will be thrown if the input interval number is less than 1
     */
    protected void validateGenerations(int generations) throws ScenarioException
    {
        if (generations < 1)
            throw new ScenarioException("The generations number for data storing is lesser than 1 (" + generations + ")", this.getClass(), _scenario);
    }

    /**
     * Auxiliary method for validating the steady-state repeats number.
     *
     * @param steadyStateRepeats generations number
     * @throws ScenarioException the exception will be thrown if the input interval number is less than 1
     */
    protected void validateSteadyStateRepeats(int steadyStateRepeats) throws ScenarioException
    {
        if (steadyStateRepeats < 1)
            throw new ScenarioException("The steady-state repeats number for data storing is lesser than 1 (" + steadyStateRepeats + ")", this.getClass(), _scenario);
    }


    /**
     * Auxiliary method for validating the "data storing interval" field.
     *
     * @param dataStoreInterval "data storing interval" field
     * @throws ScenarioException the exception will be thrown if the input interval number is less than 1
     */
    protected void validateDataStoringInterval(int dataStoreInterval) throws ScenarioException
    {
        if (dataStoreInterval < 1)
            throw new ScenarioException("The generation interval for data storing is lesser than 1 (" + dataStoreInterval + ")",
                    this.getClass(), _scenario);
    }

    /**
     * Auxiliary method for validating the "data loading interval" field.
     *
     * @param dataLoadingInterval "data loading interval" field
     * @throws ScenarioException the exception will be thrown if the input interval number is less than 1
     */
    protected void validateDataLoadingInterval(int dataLoadingInterval) throws ScenarioException
    {
        if (dataLoadingInterval < 1)
            throw new ScenarioException("The generation interval for data loading is lesser than 1 (" + dataLoadingInterval + ")",
                    this.getClass(), _scenario);
    }


    /**
     * Auxiliary method for validating performance indicators.
     *
     * @param indicators performance indicators
     * @param GDC        global data container
     * @throws ScenarioException the exception will be thrown if the indicators are not valid (e.g., the list is empty or wrong names are used)
     */
    protected void validateIndicators(IIndicator[] indicators, AbstractGlobalDataContainer GDC) throws ScenarioException
    {
        if (indicators == null)
            throw new ScenarioException("The performance indicators are not provided (the array is null)", this.getClass(), _scenario);
        if (indicators.length == 0)
            throw new ScenarioException("The performance indicators are not provided (the array is empty)", this.getClass(), _scenario);

        Set<String> names = new HashSet<>();
        for (IIndicator indicator : indicators)
        {
            if (indicator.getName() == null)
                throw new ScenarioException("Name of one of the indicators is not provided (null)", this.getClass(), _scenario);
            else if (indicator.getName().isEmpty())
                throw new ScenarioException("Name of one of the indicators is not provided (empty string)", this.getClass(), _scenario);

            if (names.contains(indicator.getName()))
                throw new ScenarioException("The indicator = " + indicator.getName() + " is not unique",
                        this.getClass(), _scenario);
            names.add(indicator.getName());
        }
        Set<Character> characters = GDC.getAllowedCharacters();

        for (IIndicator indicator : indicators)
        {
            if (!FileUtils.isAlphanumeric(indicator.getName(), characters))
                throw new ScenarioException("The indicator = " + indicator.getName() + " contains forbidden characters", this.getClass(), _scenario);
        }
    }

    /**
     * Auxiliary method for validating statistic functions used for aggregating outcomes of all trial runs.
     *
     * @param statistics statistic functions
     * @param GDC        global data container
     * @throws ScenarioException the exception will be thrown if the statistic functions are not valid (e.g., the list is empty or wrong names are used)
     */
    protected void validateStatisticFunctions(IStatistic[] statistics, AbstractGlobalDataContainer GDC) throws ScenarioException
    {
        if (!_validateStatisticFunctions) return;

        if (statistics == null)
            throw new ScenarioException("The statistic functions are not provided (the array is null)", this.getClass(), _scenario);
        if (statistics.length == 0)
            throw new ScenarioException("The statistic functions are not provided (the array is empty)", this.getClass(), _scenario);

        Set<String> names = new HashSet<>();
        for (IStatistic statistic : statistics)
        {
            if (statistic.getName() == null)
                throw new ScenarioException("Name of one of the statistic functions is not provided (null)", this.getClass(), _scenario);
            else if (statistic.getName().isEmpty())
                throw new ScenarioException("Name of one of the statistic functions is not provided (empty string)", this.getClass(), _scenario);

            if (names.contains(statistic.getName()))
                throw new ScenarioException("The statistic function = " + statistic.getName() + " is not unique",
                        this.getClass(), _scenario);
            names.add(statistic.getName());
        }
        Set<Character> characters = GDC.getAllowedCharacters();

        for (IStatistic statistic : statistics)
        {
            if (!FileUtils.isAlphanumeric(statistic.getName(), characters))
                throw new ScenarioException("The statistic function = " + statistic.getName() + " contains forbidden characters", this.getClass(), _scenario);
        }
    }

}
