package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Object reponsible for determining the per-scenario number of objectives.
 *
 * @author MTomczyk
 */
public interface INumberOfObjectivesInitializer
{
    /**
     * Instantiates the number of objectives.
     *
     * @param p params container
     * @return instantiated number of objectives
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    int instantiateNumberOfObjectives(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
