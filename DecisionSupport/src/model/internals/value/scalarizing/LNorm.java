package model.internals.value.scalarizing;

import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;
import space.normalization.INormalization;

/**
 * Extension of {@link AbstractInternalModel} for using the L-norms as internal models.
 * Wrapper for {@link space.scalarfunction.LNorm}.
 *
 * @author MTomczyk
 */
public class LNorm extends AbstractScalarizingFunctionInternalModel implements IInternalModel
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
        this(new space.scalarfunction.LNorm(w, alpha, normalizations));
    }

    /**
     * Parameterized constructor.
     *
     * @param lnorm instance of {@link space.scalarfunction.LNorm} to be wrapped
     */
    public LNorm(space.scalarfunction.LNorm lnorm)
    {
        super(lnorm.toString(), lnorm);
    }

    /**
     * Returns the internal LNorm object.
     *
     * @return LNorm object
     */
    public space.scalarfunction.LNorm getLNorm()
    {
        return (space.scalarfunction.LNorm) _f;
    }
}
