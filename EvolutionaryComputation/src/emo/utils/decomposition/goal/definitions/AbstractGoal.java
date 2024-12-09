package emo.utils.decomposition.goal.definitions;

import emo.utils.decomposition.goal.IGoal;
import space.normalization.INormalization;

/**
 * Abstract implementation of {@link IGoal}.
 *
 * @author MTomczyk
 */
public abstract class AbstractGoal implements IGoal
{

    /**
     * Normalization functions used to (normalize) specimen evaluations.
     */
    protected INormalization[] _normalizations;

    /**
     * Can be called to update normalizations used to rescale (normalize) specimen evaluations
     *
     * @param normalizations normalization functions (one per objective)
     */
    @Override
    public void updateNormalizations(INormalization[] normalizations)
    {
        _normalizations = normalizations;
    }

    /**
     * Used to determine preference direction (i.e., whether the smaller or bigger values are preferred).
     *
     * @return true -> smaller values are preferred; false otherwise.
     */
    @Override
    public boolean isLessPreferred()
    {
        return true;
    }


}
