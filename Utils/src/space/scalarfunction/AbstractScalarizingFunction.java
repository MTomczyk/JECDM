package space.scalarfunction;

import space.normalization.INormalization;

/**
 * Abstract, general implementation of {@link IScalarizingFunction} interface.
 *
 * @author MTomczyk
 */

public abstract class AbstractScalarizingFunction implements IScalarizingFunction
{
    /**
     * Weight vector.
     */
    protected double[] _w;

    /**
     * Implementation-specific auxiliary parameter.
     */
    protected double _a;

    /**
     * Normalizations that can be used to rescale processed points.
     */
    protected INormalization[] _normalizations;

    /**
     * Parameterized constructor: sets the default fields.
     *
     * @param w              weight vector
     * @param a              auxiliary parameter
     * @param normalizations normalizations that can be used to rescale processed points
     */
    protected AbstractScalarizingFunction(double[] w, double a, INormalization[] normalizations)
    {
        _w = w;
        _a = a;
        _normalizations = normalizations;
    }

    /**
     * Auxiliary method for normalizing the processed point i-th evaluation.
     *
     * @param e processed point
     * @param i index (i-th position)
     * @return normalized point
     */
    protected double getNormalized(double[] e, int i)
    {
        if (_normalizations == null) return e[i];
        return _normalizations[i].getNormalized(e[i]);
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
        return 0;
    }

    /**
     * High-abstraction (implementation-specific) params getter.
     *
     * @param normalizations normalizations used to rescale input points
     */
    @Override
    public void setNormalizations(INormalization[] normalizations)
    {
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

    }

    /**
     * High-abstraction (implementation-specific) params getter.
     */
    @Override
    public double[][] getParams()
    {
        return new double[0][];
    }

    /**
     * Implementation-specific weights setter.
     *
     * @param w weight vector
     */
    @Override
    public void setWeights(double[] w)
    {
        _w = w;
    }

    /**
     * Implementation-specific setter for some auxiliary parameter (e.g., compensation level in L-norms).
     *
     * @param a auxiliary parameter
     */
    @Override
    public void setAuxParam(double a)
    {
        _a = a;
    }

    /**
     * Implementation-specific weights getter.
     */
    @Override
    public double[] getWeights()
    {
        return _w;
    }

    /**
     * Implementation-specific auxiliary param getter.
     */
    @Override
    public double getAuxParam()
    {
        return _a;
    }

}
