package tools.feedbackgenerators;

import scenario.Scenario;

/**
 * Auxiliary abstract class.
 *
 * @author MTomczyk
 */
abstract class DimensionsDependent
{
    /**
     * Parameterized constructor.
     *
     * @param dimensionsKey key associated with the number of criteria/objectives
     */
    protected DimensionsDependent(String dimensionsKey)
    {
        _dimensionsKey = dimensionsKey;
    }

    /**
     * Key associated with the number of criteria/objectives.
     */
    private final String _dimensionsKey;

    /**
     * Returns the number of criteria/objectives given the input scenario definition
     *
     * @param scenario scenario definition
     * @return the number of criteria/objectives
     */
    protected int getM(Scenario scenario)
    {
        String objectives = scenario.getKeyValuesMap().get(_dimensionsKey).getValue();
        return Integer.parseInt(objectives);
    }
}
