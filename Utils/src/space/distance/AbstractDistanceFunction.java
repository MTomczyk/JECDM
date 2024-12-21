package space.distance;

import space.normalization.INormalization;
import space.scalarfunction.IScalarizingFunction;

/**
 * Abstract, general implementation of {@link IScalarizingFunction} interface.
 * It wraps the {@link IScalarizingFunction}.
 *
 * @author MTomczyk
 */

public abstract class AbstractDistanceFunction implements IDistance
{
    /**
     * Scalar function object used when calculating the distances.
     */
    protected final IScalarizingFunction _sf;

    /**
     * Reference to the normalizations is kept.
     */
    protected INormalization[] _normalizations;

    /**
     * Parameterized constructor: sets the default fields.
     *
     * @param sf             scalar function object used when calculating the distances
     * @param normalizations reference to the normalizations is kept
     */
    protected AbstractDistanceFunction(IScalarizingFunction sf, INormalization[] normalizations)
    {
        _sf = sf;
        _normalizations = normalizations;
    }

    /**
     * Can be called to calculate the distance between two vectors (of equal dimensionality).
     * It is assumed that dist(a,b) should equal dist(b,a) (metric space condition).
     *
     * @param a the first vector
     * @param b the second vector
     * @return distance between a and b
     */
    @Override
    public double getDistance(double[] a, double[] b)
    {
        double[] d = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            double na = a[i];
            double nb = b[i];
            if (_normalizations != null)
            {
                na = _normalizations[i].getNormalized(na);
                nb = _normalizations[i].getNormalized(nb);
            }
            d[i] = na - nb;
        }
        return _sf.evaluate(d);
    }

    /**
     * High-abstraction (implementation-specific) params getter.
     *
     * @param normalizations normalizations used to rescale input points
     */
    @Override
    public void setNormalizations(INormalization[] normalizations)
    {
        _sf.setNormalizations(null);
        _normalizations = normalizations;
    }

    /**
     * High-abstraction (implementation-specific) params setter.
     *
     * @param p params to be set
     */
    @Override
    public void setParams(double[][] p)
    {
        _sf.setParams(p);
    }

    /**
     * High-abstraction (implementation-specific) params getter.
     */
    @Override
    public double[][] getParams()
    {
        return _sf.getParams();
    }

    /**
     * Implementation-specific weights setter.
     *
     * @param w weight vector
     */
    @Override
    public void setWeights(double[] w)
    {
        _sf.setWeights(w);
    }

    /**
     * Implementation-specific setter for some auxiliary parameter (e.g., compensation level in L-norms).
     *
     * @param a auxiliary parameter
     */
    @Override
    public void setAuxParam(double a)
    {
        _sf.setAuxParam(a);
    }

    /**
     * Implementation-specific weights getter.
     */
    @Override
    public double[] getWeights()
    {
        return _sf.getWeights();
    }

    /**
     * Implementation-specific auxiliary param getter.
     */
    @Override
    public double getAuxParam()
    {
        return _sf.getAuxParam();
    }

    /**
     * Can be used to check whether less/more means closer/further.
     *
     * @return true, if less means closer; false otherwise
     */
    @Override
    public boolean isLessMeaningCloser()
    {
        return true;
    }
}
