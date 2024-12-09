package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;

/**
 * Default implementation of {@link IProblemIDInitializer}.
 *
 * @author MTomczyk
 */
public class DefaultProblemIDInitializer implements IProblemIDInitializer
{
    /**
     * Instantiates the problem ID. If the string cannot be retrieved from {@link scenario.Scenario},
     * the method returns null as the default value.
     *
     * @param p params container
     * @throws ScenarioException the signature allows the overwriting method to cast exceptions
     */
    @Override
    public String instantiateProblemID(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        try
        {
            return p._scenario.getProblem();
        } catch (Exception e)
        {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, p._scenario);
        }
    }
}
