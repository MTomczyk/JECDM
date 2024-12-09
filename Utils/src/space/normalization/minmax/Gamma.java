package space.normalization.minmax;

import space.normalization.INormalization;

/**
 * Performs min-max normalization (linear interpolation) and then scales the 0-1 points using the gamma transformation.
 *
 * @author MTomczyk
 */

public class Gamma extends Linear implements INormalization
{
    /**
     * Gamma level.
     */
    protected double _gamma;

    /**
     * Inverse gamma coefficient.
     */
    protected double _invGamma;

    /**
     * Parameterized constructor (sets min to 0.0, and max to 1.0).
     *
     * @param gamma gamma level ( > 0)
     */
    public Gamma(double gamma)
    {
        this(0.0f, 1.0d, gamma);
    }

    /**
     * Parameterized constructor.
     *
     * @param min   min value
     * @param max   max value
     * @param gamma gamma level ( > 0)
     */
    public Gamma(double min, double max, double gamma)
    {
        super(min, max);
        assert Double.compare(gamma, 0.0f) > 0;
        _gamma = gamma;
        _invGamma = 1.0f / _gamma;
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
        return Math.pow(super.getNormalized(value), _gamma);
    }

    /**
     * Unnormalized input value. I.e., input in the range [0, 1] is mapped into a value in the initially considered space.
     * In the case when min == max, the result is always mapped into min.
     *
     * @param value input value
     * @return value in the initially considered space
     */
    @Override
    public double getUnnormalized(double value)
    {
        return Math.pow(value, _invGamma) * _dv + _min;
    }

    /**
     * Prints info on the normalization procedure.
     *
     */
    @Override
    public String toString()
    {
        return String.format("Gamma normalization: min = %.6f; max = %.6f; gamma = %.6f", _min, _max, _gamma);
    }

    /**
     * Constructs cloned object.
     *
     * @return cloned object
     */
    @Override
    public AbstractMinMaxNormalization getClone()
    {
        return new Gamma(_min, _max, _gamma);
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
        if (n instanceof Gamma)
        {
            _gamma = ((Gamma) n)._gamma;
            _invGamma = ((Gamma) n)._invGamma;
        }
    }
}
