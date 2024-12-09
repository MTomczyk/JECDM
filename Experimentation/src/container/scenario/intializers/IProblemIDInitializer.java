package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Object reponsible for determining the per-scenario problem id (string).
 *
 * @author MTomczyk
 */
public interface IProblemIDInitializer
{
    /**
     * Instantiates the problem id (string).
     *
     * @param p params container
     * @return instantiated number of objectives
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    String instantiateProblemID(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
