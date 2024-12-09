package model.constructor.random;

import model.internals.value.scalarizing.LNorm;
import random.IRandom;
import random.WeightsGenerator;
import space.normalization.INormalization;

/**
 * Implementation of {@link IRandomModel} for creating random L-norm models.
 *
 * @author MTomczyk
 */
public class LNormGenerator extends AbstractRandomModel <LNorm> implements IRandomModel <LNorm>
{
    /**
     * Alpha (compensation level) for L-norms.
     */
    private final double _alpha;

    /**
     * Parameterized constructor.
     *
     * @param criteria       the space dimensionality (i.e., number of criteria)
     * @param alpha          alpha (compensation level) for L-norms (Double.POSITIVE_INFINITY = Chebyshev function)
     */
    public LNormGenerator(int criteria, double alpha)
    {
        this(criteria, alpha, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param criteria       the space dimensionality (i.e., number of criteria)
     * @param alpha          alpha (compensation level) for L-norms (Double.POSITIVE_INFINITY = Chebyshev function)
     * @param normalizations normalizations used to rescale the dimensions
     */
    public LNormGenerator(int criteria, double alpha, INormalization[] normalizations)
    {
        super(criteria);
        _alpha = alpha;
        _normalizations = normalizations;
    }

    /**
     * Constructs random model instance.
     *
     * @param R random number generator
     * @return random model instance
     */
    @Override
    public LNorm generateModel(IRandom R)
    {
        double[] w = WeightsGenerator.getNormalizedWeightVector(_criteria, R);
        space.scalarfunction.LNorm lnorm = new space.scalarfunction.LNorm(w, _alpha, _normalizations);
        return new LNorm(lnorm);
    }
}
