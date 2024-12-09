package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Object reponsible for determining the per-scenario number of steady-state repeats.
 *
 * @author MTomczyk
 */
public interface INumberOfSteadyStateRepeatsInitializer
{
    /**
     * Instantiates the number of steady-state repeats.
     *
     * @param p params container
     * @return instantiated number of steady state repeats
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    int instantiateSteadyStateRepeats(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
