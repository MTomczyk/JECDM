package space.distance;

import space.normalization.INormalization;

/**
 * Implementation of the Chebyshev distance function (wraps {@link LNorm}).
 * If weight vector = null; weights are not used.
 *
 * @author MTomczyk
 */

public class Chebyshev extends AbstractDistanceFunction implements IDistance
{
    /**
     * Parameterized constructor (normalizations and weights are not used).
     */
    public Chebyshev()
    {
        this(null, null);
    }

    /**
     * Parameterized constructor (normalizations are not used).
     *
     * @param w weight vector
     */
    public Chebyshev(double[] w)
    {
        this(w, null);
    }

    /**
     * Parameterized constructor (weights are not used).
     *
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public Chebyshev(INormalization[] normalizations)
    {
        this(null, normalizations);
    }

    /**
     * Parameterized constructor.
     *
     * @param w              weight vector
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public Chebyshev(double[] w, INormalization[] normalizations)
    {
        super(new space.scalarfunction.LNorm(w, Double.POSITIVE_INFINITY, null), normalizations);
    }
}
