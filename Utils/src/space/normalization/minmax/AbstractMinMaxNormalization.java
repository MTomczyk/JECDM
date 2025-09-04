package space.normalization.minmax;

import space.normalization.INormalization;

/**
 * Abstract, common class for normalization procedures based on min and max value.
 * The class extensions are intended to produce normalized outputs that fall into [0, 1] interval.
 *
 * @author MTomczyk
 */
public abstract class AbstractMinMaxNormalization implements INormalization
{
    /**
     * Min value.
     */
    protected double _min;

    /**
     * Max value.
     */
    protected double _max;

    /**
     * Difference between max and min.
     */
    protected double _dv;

    /**
     * Default constructor (sets min to 0.0, and max to 1.0).
     */
    public AbstractMinMaxNormalization()
    {
        this(0.0d, 1.0d);
    }

    /**
     * Parameterized constructor.
     *
     * @param min min value
     * @param max max value
     */
    public AbstractMinMaxNormalization(double min, double max)
    {
        setMinMax(min, max);
    }

    /**
     * Normalizes input value (linear normalization). In the case when min == max, the value is always mapped to 0.0f.
     *
     * @param value input value
     * @return normalized value
     */
    @Override
    public double getNormalized(double value)
    {
        if (Double.compare(_dv, 0.0d) == 0) return 0.0f;
        return (value - _min) / _dv;
    }

    /**
     * Unnormalized input value. I.e., input in the range [0, 1] is mapped into a value in the initially considered space.
     * In the case when min == max, the result is always mapped into min.
     *
     * @param value normalized value [0, 1]
     * @return value in the initially considered space
     */
    public double getUnnormalized(double value)
    {
        if (Double.compare(_dv, 0.0d) == 0) return _min;
        return value * _dv + _min;
    }

    /**
     * Prints information on the normalization procedure.
     */
    @Override
    public String toString()
    {
        return String.format("Abstract Min-Max normalization: min = %.6f; max = %.6f", _min, _max);
    }

    /**
     * Updates reference min and max values.
     *
     * @param min min value
     * @param max max value
     */
    public void setMinMax(double min, double max)
    {
        _max = max;
        _min = min;
        _dv = _max - _min;
    }

    /**
     * Getter for the reference min value.
     *
     * @return reference min value
     */
    public double getMin()
    {
        return _min;
    }

    /**
     * Getter for the reference max value.
     *
     * @return reference max value
     */
    public double getMax()
    {
        return _max;
    }

    /**
     * Constructs cloned object.
     *
     * @return cloned object
     */
    @Override
    public AbstractMinMaxNormalization getClone()
    {
        return null;
    }

    /**
     * Parameterizes this object as the other one.
     * It is assumed that both objects are of the same (sub)type (not checked).
     *
     * @param n the other object
     */
    public void parameterizeAsIn(AbstractMinMaxNormalization n)
    {
        setMinMax(n._min, n._max);
    }
}
