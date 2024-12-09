package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;
import statistics.IStatistic;

/**
 * Object reponsible for determining the per-scenario statistic functions (aggregate trial outcomes).
 *
 * @author MTomczyk
 */
public interface IStatisticFunctionsInitializer
{
    /**
     * Instantiates the statistic functions.
     *
     * @param p params container
     * @return instantiated statistic functions
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    IStatistic[] instantiateStatisticFunctions(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
