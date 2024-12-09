package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;
import indicator.IIndicator;

/**
 * Object reponsible for determining the per-scenario performance indicators.
 *
 * @author MTomczyk
 */
public interface IIndicatorsInitializer
{
    /**
     * Instantiates the performance indicators.
     *
     * @param p params container
     * @return instantiated performance indicators
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    IIndicator[] instantiateIndicators(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
