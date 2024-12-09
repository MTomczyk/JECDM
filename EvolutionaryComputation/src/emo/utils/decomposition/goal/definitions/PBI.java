package emo.utils.decomposition.goal.definitions;

import emo.utils.decomposition.goal.IGoal;

/**
 * PBI representation of an optimization goal {@link IGoal}.
 *
 * @author MTomczyk
 */

public class PBI extends AbstractScalarGoal implements IGoal
{
    /**
     * Parameterized constructor.
     *
     * @param pbi wrapped instance of {@link space.scalarfunction.PBI};
     */
    public PBI(space.scalarfunction.PBI pbi)
    {
        super(pbi);
    }
}
