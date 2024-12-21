package space.scalarfunction;

import space.normalization.INormalization;

/**
 * Implementation of the L-norm scalarizing function.
 * If weight vector = null; weights are not used (they are applied after the normalization).
 *
 * @author MTomczyk
 */
public class LNorm extends AbstractScalarizingFunction implements IScalarizingFunction
{
    /**
     * Inverse alpha (compensation level).
     */
    protected double _ia;

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
        super(w, alpha, normalizations);
        if (Double.compare(alpha, Double.POSITIVE_INFINITY) != 0)
            _ia = 1.0d / alpha;
        else _ia = 0.0d;
    }

    /**
     * Can be called to get a scalarizing value from an evaluation vector.
     *
     * @param e evaluation vector
     * @return final scalarizing score
     */
    @Override
    public double evaluate(double[] e)
    {
        double r = 0.0d;

        if (Double.compare(_a, Double.POSITIVE_INFINITY) == 0) // Chebyshev
        {
            r = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < e.length; i++)
            {
                double ne = Math.abs(getNormalized(e, i));
                if (_w != null) ne *= _w[i];
                if (Double.compare(ne, r) > 0) r = ne;
            }
        }
        else if (Double.compare(_a, 1.0d) == 0) // weighted sum, separate case to avoid calculating the power
        {
            for (int i = 0; i < e.length; i++)
            {
                if (_w != null) r += Math.abs(getNormalized(e, i) * _w[i]);
                else r += Math.abs(getNormalized(e, i));
            }
        }
        else // alpha in (1, infinity)
        {
            for (int i = 0; i < e.length; i++)
            {
                if (_w != null) r += Math.abs(Math.pow(getNormalized(e, i) * _w[i], _a));
                else r += Math.abs(Math.pow(getNormalized(e, i), _a));
            }
            r = Math.pow(r, _ia);
        }
        return r;
    }


    /**
     * Params setter. It is assumed that the first element of the 2D input array is the new weight vector, while the second
     * element is the new alpha value (i.e., [w-vector, [alpha]]).
     *
     * @param p params to be set
     */
    @Override
    public void setParams(double[][] p)
    {
        _w = p[0];
        _a = p[1][0];
        if (Double.compare(_a, Double.POSITIVE_INFINITY) != 0)
            _ia = 1.0d / _a;
    }

    /**
     * Params getter. It returns the weight vector and the compensation level alpha stored in a 2D matrix
     * of the following form [w-vector, [alpha]]
     */
    @Override
    public double[][] getParams()
    {
        return new double[][]{_w.clone(), {_a}};
    }


    /**
     * Returns the string representation
     *
     * @return string representation
     */
    public String toString()
    {
        if (Double.compare(getAuxParam(), Double.POSITIVE_INFINITY) == 0) return "Infinity-norm";
        else return String.format("%.4f-norm", getAuxParam());
    }
}
