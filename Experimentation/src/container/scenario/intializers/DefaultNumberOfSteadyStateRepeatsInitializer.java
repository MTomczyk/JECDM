package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Default implementation of {@link INumberOfGenerationsInitializer} (passes no. steady state repeats from params container).
 *
 * @author MTomczyk
 */
public class DefaultNumberOfSteadyStateRepeatsInitializer implements INumberOfSteadyStateRepeatsInitializer
{
    /**
     * Instantiates the number of steady-state repeats.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public int instantiateSteadyStateRepeats(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        return p._steadyStateRepeats;
    }
}
