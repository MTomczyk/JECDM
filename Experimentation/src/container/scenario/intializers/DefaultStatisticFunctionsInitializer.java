package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;
import statistics.IStatistic;

/**
 * Default implementation of {@link IStatisticFunctionsInitializer} (passes statistic functions from params container).
 *
 * @author MTomczyk
 */
public class DefaultStatisticFunctionsInitializer implements IStatisticFunctionsInitializer
{

    /**
     * Instantiates the statistic functions.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public IStatistic[] instantiateStatisticFunctions(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        return p._statistics;
    }
}
