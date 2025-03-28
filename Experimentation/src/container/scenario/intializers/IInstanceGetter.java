package container.scenario.intializers;

import container.scenario.AbstractScenarioDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import exception.ScenarioException;

/**
 * Interface for objects for retrieving instances of {@link AbstractScenarioDataContainer}.
 *
 * @author MTomczyk
 */
public interface IInstanceGetter
{
    /**
     * The signature of the method for retrieving instances of {@link AbstractScenarioDataContainer}.
     * @param p params container generated by {@link ScenarioDataContainerFactory} and passed here
     * @return instance of {@link AbstractScenarioDataContainer}
     * @throws ScenarioException the exception can be thrown and passed to GDC
     */
    AbstractScenarioDataContainer getInstance(AbstractScenarioDataContainer.Params p) throws ScenarioException;
}
