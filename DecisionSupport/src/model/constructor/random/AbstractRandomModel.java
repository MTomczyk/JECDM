package model.constructor.random;

import model.internals.AbstractInternalModel;
import space.normalization.INormalization;

/**
 * Abstract implementation of {@link IRandomModel}.
 *
 * @author MTomczyk
 */
public abstract class AbstractRandomModel <T extends AbstractInternalModel> implements IRandomModel <T>
{
    /**
     * The space dimensionality (i.e., number of criteria).
     */
    protected final int _criteria;

    /**
     * Normalizations objects.
     */
    protected INormalization[] _normalizations;

    /**
     * Parameterized constructor.
     *
     * @param criteria the space dimensionality (i.e., number of criteria)
     */
    public AbstractRandomModel(int criteria)
    {
        _criteria = criteria;
    }

    /**
     * Auxiliary method for setting new normalizations (used to rescale alternative evaluations given the considered criteria).
     *
     * @param normalizations normalizations used to rescale the dimensions
     */
    public void setNormalizations(INormalization[] normalizations)
    {
        _normalizations = normalizations;
    }
}
