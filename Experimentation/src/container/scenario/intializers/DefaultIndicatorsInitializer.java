package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;
import indicator.IIndicator;

/**
 * Default implementation of {@link IIndicatorsInitializer} (passes indicators from params container).
 *
 * @author MTomczyk
 */
public class DefaultIndicatorsInitializer implements IIndicatorsInitializer
{
    /**
     * Instantiates the performance indicators.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public IIndicator[] instantiateIndicators(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        return p._indicators;
    }
}
