package tools.feedbackgenerators;

import scenario.Scenario;

/**
 * Implementation of {@link ICriteriaTypesProvider} that treats all criteria/objectives as of a cost type (to be minimized)
 *
 * @author MTomczyk
 */
public class Cost extends DimensionsDependent implements ICriteriaTypesProvider
{
    /**
     * Parameterized constructor.
     * @param dimensionsKey key associated with the number of criteria/objectives
     */
    public Cost(String dimensionsKey)
    {
        super(dimensionsKey);
    }

    /**
     * The main method. Returns a suitable number of false flags.
     * @param scenario scenario being processed
     * @return suitable number of false flags.
     */
    @Override
    public boolean[] getTypes(Scenario scenario)
    {
        return new boolean[getM(scenario)];
    }
}
