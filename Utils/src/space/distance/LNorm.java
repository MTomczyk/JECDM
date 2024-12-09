package space.distance;

import space.normalization.INormalization;

/**
 * Implementation of the L-norm distance function.
 * If weight vector = null; weights are not used.
 *
 * @author MTomczyk
 */

public class LNorm extends AbstractDistanceFunction implements IDistance
{
    /**
     * Parameterized constructor (normalizations and weights are not used).
     *
     * @param alpha compensation level (a = 1 -> linear function; a = Double.POSITIVE_INFINITY -> Chebyshev function).
     */
    public LNorm(double alpha)
    {
        this(null, alpha, null);
    }

    /**
     * Parameterized constructor (normalizations are not used).
     *
     * @param w     weight vector
     * @param alpha compensation level (a = 1 -> linear function; a = Double.POSITIVE_INFINITY -> Chebyshev function).
     */
    public LNorm(double[] w, double alpha)
    {
        this(w, alpha, null);
    }

    /**
     * Parameterized constructor (weights are not used).
     *
     * @param alpha          compensation level (a = 1 -> linear function; a = Double.POSITIVE_INFINITY -> Chebyshev function).
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public LNorm(double alpha, INormalization[] normalizations)
    {
        this(null, alpha, normalizations);
    }

    /**
     * Parameterized constructor.
     *
     * @param w              weight vector
     * @param alpha          compensation level (a = 1 -> linear function; a = Double.POSITIVE_INFINITY -> Chebyshev function).
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public LNorm(double[] w, double alpha, INormalization[] normalizations)
    {
        super(new space.scalarfunction.LNorm(w, alpha, null), normalizations);
    }
}
