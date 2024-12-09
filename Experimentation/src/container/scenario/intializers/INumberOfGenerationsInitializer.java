package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Object reponsible for determining the per-scenario number of generations.
 *
 * @author MTomczyk
 */
public interface INumberOfGenerationsInitializer
{
    /**
     * Instantiates the number of generations.
     *
     * @param p params container
     * @return instantiated number of generations
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    int instantiateGenerations(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
