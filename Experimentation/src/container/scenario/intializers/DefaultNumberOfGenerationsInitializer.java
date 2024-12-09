package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Default implementation of {@link INumberOfGenerationsInitializer} (passes generations from params container).
 *
 * @author MTomczyk
 */
public class DefaultNumberOfGenerationsInitializer implements INumberOfGenerationsInitializer
{
    /**
     * Instantiates the number of generations.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public int instantiateGenerations(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        return p._generations;
    }
}
