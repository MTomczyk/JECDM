package space.distance;

import space.normalization.INormalization;

/**
 * Implementation of the Euclidean distance function (wraps {@link LNorm}).
 * If weight vector = null; weights are not used.
 *
 * @author MTomczyk
 */

public class Euclidean extends AbstractDistanceFunction implements IDistance
{
    /**
     * Parameterized constructor (normalizations and weights are not used).
     *
     */
    public Euclidean()
    {
        this(null, null);
    }

    /**
     * Parameterized constructor (normalizations are not used).
     *
     * @param w weight vector
     */
    public Euclidean(double[] w)
    {
        this(w, null);
    }

    /**
     * Parameterized constructor (weights are not used).
     *
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public Euclidean(INormalization[] normalizations)
    {
        this(null, normalizations);
    }

    /**
     * Parameterized constructor.
     *
     * @param w              weight vector
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public Euclidean(double[] w, INormalization[] normalizations)
    {
        super(new space.scalarfunction.LNorm(w, 2.0d, null), normalizations);
    }
}
