package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import container.scenario.ScenarioDataContainer;
import exception.ScenarioException;

/**
 * Default implementation of {@link container.trial.initialziers.IInstanceGetter}.
 *
 * @author MTomczyk
 */
public class DefaultInstanceGetter implements IInstanceGetter
{
    /**
     * The method returns a default instance of SDC ({@link ScenarioDataContainer}).
     *
     * @param p params container generated by {@link container.scenario.ScenarioDataContainerFactory} and passed here
     * @return instance of {@link AbstractScenarioDataContainer}
     * @throws ScenarioException the exception can be thrown and passed to GDC
     */
    @Override
    public AbstractScenarioDataContainer getInstance(AbstractScenarioDataContainer.Params p) throws ScenarioException
    {
        return new ScenarioDataContainer(p);
    }
}
