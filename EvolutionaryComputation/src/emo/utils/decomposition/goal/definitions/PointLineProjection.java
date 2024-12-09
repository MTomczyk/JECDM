package emo.utils.decomposition.goal.definitions;

import emo.utils.decomposition.goal.IGoal;

/**
 * Point-line distance function (orthogonal projection) representation of an optimization goal {@link IGoal}.
 *
 * @author MTomczyk
 */

public class PointLineProjection extends AbstractScalarGoal implements IGoal
{
    /**
     * Parameterized constructor.
     *
     * @param plp wrapped instance of {@link space.scalarfunction.PointLineProjection};
     */
    public PointLineProjection(space.scalarfunction.PointLineProjection plp)
    {
        super(plp);
    }
}
