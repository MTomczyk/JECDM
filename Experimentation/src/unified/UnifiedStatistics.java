package unified;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import scenario.CrossedScenarios;
import statistics.IStatistic;


/**
 * Auxiliary class used by {{@link CrossSummarizer}}. It unifies all unique statistic functions utilized throughout all
 * crossed scenarios (functions may be different for different scenarios).
 */
public class UnifiedStatistics extends AbstractUnified<IStatistic>
{
    /**
     * Parameterized constructor that creates the data based on the provided (instantiated) scenario data containers.
     *
     * @param SDCs                   instantiated scenario data containers
     * @param unifiedStatisticsNames optional statistic functions names bypassed via {@link container.global.AbstractGlobalDataContainer}
     * @param crossedScenarios       crossed scenarios being currently processed
     * @throws CrossedScenariosException crossed scenarios exception can be thrown and propagated higher
     */
    public UnifiedStatistics(AbstractScenarioDataContainer[] SDCs,
                             String[] unifiedStatisticsNames,
                             CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        super(AbstractScenarioDataContainer::getStatisticFunctions,
                "The names of the provided unified statistic functions do not contain a joint set of functions " +
                        "used in all crossed scenarios", SDCs, unifiedStatisticsNames, crossedScenarios);
    }
}
