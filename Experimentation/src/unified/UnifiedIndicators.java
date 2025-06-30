package unified;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import executor.CrossSummarizer;
import indicator.IIndicator;
import scenario.CrossedScenarios;


/**
 * Auxiliary class used by {{@link CrossSummarizer}}. It unifies all unique indicators utilized throughout all crossed
 * scenarios (indicators may be different for different scenarios).
 */
public class UnifiedIndicators extends AbstractUnified<IIndicator>
{
    /**
     * Parameterized constructor that creates the data based on the provided (instantiated) scenario data containers.
     *
     * @param SDCs                   instantiated scenario data containers
     * @param unifiedIndicatorsNames optional indicator names bypassed via {@link container.global.AbstractGlobalDataContainer}
     * @param crossedScenarios       crossed scenarios being currently processed
     * @throws CrossedScenariosException crossed scenarios exception can be thrown 
     */
    public UnifiedIndicators(AbstractScenarioDataContainer[] SDCs,
                             String[] unifiedIndicatorsNames,
                             CrossedScenarios crossedScenarios) throws CrossedScenariosException
    {
        super(AbstractScenarioDataContainer::getIndicators,
                "The names of the provided unified indicators do not contain a joint set of indicators " +
                "used in all crossed scenarios", SDCs, unifiedIndicatorsNames, crossedScenarios);
    }
}
