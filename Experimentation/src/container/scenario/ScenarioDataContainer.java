package container.scenario;

import exception.ScenarioException;

/**
 * Default extension of {@link AbstractScenarioDataContainer}.
 *
 * @author MTomczyk
 */
public class ScenarioDataContainer extends AbstractScenarioDataContainer
{
    /**
     * Parameterized constructor that also instantiates the data.
     *
     * @param p params container
     * @throws ScenarioException the exception can be thrown and passed to the main executor
     */
    public ScenarioDataContainer(Params p) throws ScenarioException
    {
        super(p);
    }


}
