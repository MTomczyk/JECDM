package tools.feedbackgenerators;

import scenario.Scenario;
import utils.ArrayUtils;

/**
 * Implementation of {@link ICriteriaTypesProvider} that treats all criteria/objectives as of a gain type (to be maximized)
 *
 * @author MTomczyk
 */
public class Gain extends DimensionsDependent implements ICriteriaTypesProvider
{
    /**
     * Parameterized constructor.
     *
     * @param dimensionsKey key associated with the number of criteria/objectives
     */
    public Gain(String dimensionsKey)
    {
        super(dimensionsKey);
    }

    /**
     * The main method. Returns a suitable number of true flags.
     *
     * @param scenario scenario being processed
     * @return suitable number of true flags.
     */
    @Override
    public boolean[] getTypes(Scenario scenario)
    {
        return ArrayUtils.getBooleanArray(getM(scenario), true);
    }
}
