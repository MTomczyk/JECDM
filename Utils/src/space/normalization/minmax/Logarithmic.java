package space.normalization.minmax;

import space.normalization.INormalization;

/**
 * This implementation effectively emulates a logarithmic scale.
 *
 * @author MTomczyk
 */

public class Logarithmic extends AbstractMinMaxNormalization implements INormalization
{
    /**
     * Log base.
     */
    protected double _base;

    /**
     * Logarithm of min.
     */
    protected double _logMin;

    /**
     * Logarithm of max.
     */
    protected double _logMax;

    /**
     * Difference of log max and log min.
     */
    protected double _logDV;

    /**
     * Parameterized constructor (sets min to 0.0 and max to 1.0, uses a base of 2).
     *
     */
    public Logarithmic()
    {
        this(0.0f, 1.0d, 10.0f);
    }

    /**
     * Parameterized constructor (sets min to 0.0 and max to 1.0).
     *
     * @param base log base ( > 0, != 1)
     */
    public Logarithmic(double base)
    {
        this(0.0f, 1.0d, base);
    }

    /**
     * Parameterized constructor.
     *
     * @param min   min value
     * @param max   max value
     * @param base log base ( > 0, != 1)
     */
    public Logarithmic(double min, double max, double base)
    {
        assert Double.compare(base, 0.0f) > 0;
        assert Double.compare(base, 1.0f) != 0;
        _base = base;
        setMinMax(min, max);
    }


    /**
     * Updates reference min/max values.
     *
     * @param min min value
     * @param max max value
     */
    @Override
    public void setMinMax(double min, double max)
    {
        super.setMinMax(min, max);
        _logMax = Math.log(max) / Math.log(_base);
        _logMin = Math.log(min) / Math.log(_base);
        _logDV = _logMax - _logMin;
    }

    /**
     * Normalizes the input value. When min == max, the value is always mapped to 0.0f.
     * The final [0,1] product is then taken to the gamma power.
     *
     * @param value input value
     * @return normalized value
     */
    @Override
    public double getNormalized(double value)
    {
        double vv = Math.log(value) / Math.log(_base);
        return (vv - _logMin) / (_logDV);
    }

    /**
     * Unnormalized input value. I.e., input in the range [0, 1] is mapped into a value in the initially considered space.
     *
     * @param value input value
     * @return value in the initially considered space
     */
    @Override
    public double getUnnormalized(double value)
    {
        double v = value * _logDV + _logMin;
        return Math.pow(_base, v);
    }

    /**
     * Prints info on the normalization procedure.
     *
     */
    @Override
    public String toString()
    {
        return String.format("Logarithmic normalization: min = %.6f; max = %.6f; base = %.6f", _min, _max, _base);
    }

    /**
     * Constructs cloned object.
     *
     * @return cloned object
     */
    @Override
    public AbstractMinMaxNormalization getClone()
    {
        return new Logarithmic(_min, _max, _base);
    }

    /**
     * Parameterizes this object as the other one.
     * It is assumed that both objects are of the same (sub)type.
     *
     * @param n the other object
     */
    @Override
    public void parameterizeAsIn(AbstractMinMaxNormalization n)
    {
        super.parameterizeAsIn(n);
        if (n instanceof Logarithmic)
        {
            _base = ((Logarithmic) n)._base;
            setMinMax(n.getMin(), n.getMax());
        }
    }
}
