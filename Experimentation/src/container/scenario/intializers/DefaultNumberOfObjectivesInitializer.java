package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Default implementation of {@link INumberOfObjectivesInitializer}.
 *
 * @author MTomczyk
 */
public class DefaultNumberOfObjectivesInitializer implements INumberOfObjectivesInitializer
{
    /**
     * Instantiates the number of objectives. If the number cannot be retrieved from {@link scenario.Scenario},
     * the method returns 1 as a default value.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public int instantiateNumberOfObjectives(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        try
        {
            Integer n = p._scenario.getObjectives();
            if (n == null) return p._objectives;
            return n;
        } catch (Exception e)
        {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, p._scenario);
        }
    }
}
