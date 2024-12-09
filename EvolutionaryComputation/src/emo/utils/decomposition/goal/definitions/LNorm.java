package emo.utils.decomposition.goal.definitions;

import emo.utils.decomposition.goal.IGoal;

/**
 * L-norm representation of an optimization goal {@link IGoal}.
 *
 * @author MTomczyk
 */

public class LNorm extends AbstractScalarGoal implements IGoal
{
    /**
     * Parameterized constructor.
     *
     * @param lNorm wrapped instance of {@link space.scalarfunction.LNorm};
     */
    public LNorm(space.scalarfunction.LNorm lNorm)
    {
        super(lNorm);
    }
}
