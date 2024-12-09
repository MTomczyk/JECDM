package space.normalization.minmax;

import space.normalization.INormalization;

/**
 * Performs min-max normalization with a flip (linear interpolation).
 *
 * @author MTomczyk
 */


public class LinearWithFlip extends AbstractMinMaxNormalization implements INormalization
{
    /**
     * Value by which the normalization product will be flipped.
     */
    private double _flipThreshold;

    /**
     * Default constructor (sets min to 0.0, max to 1.0, flip threshold = 1.0).
     */
    public LinearWithFlip()
    {
        this(0.0d, 1.0d, 1.0d);
    }


    /**
     * Parameterized constructor (sets min to 0.0, max to 1.0).
     *
     * @param flipThreshold value by which the product of normalization will be flipped
     */
    public LinearWithFlip(double flipThreshold)
    {
        this(0.0d, 1.0d, flipThreshold);
    }

    /**
     * Parameterized constructor.
     *
     * @param min           min value
     * @param max           max value
     * @param flipThreshold value by which the product of normalization will be flipped
     */
    public LinearWithFlip(double min, double max, double flipThreshold)
    {
        super(min, max);
        _flipThreshold = flipThreshold;
    }

    /**
     * Normalizes input value.
     *
     * @param value input value
     * @return normalized value
     */
    @Override
    public double getNormalized(double value)
    {
        return _flipThreshold - (value - _min) / _dv;
    }

    /**
     * Unnormalized input value. I.e., input in the range [0, 1] is mapped into a value in the initially considered space
     *
     * @param value normalized value [0, 1]
     * @return value in the initially considered space
     */
    @Override
    public double getUnnormalized(double value)
    {
        if (Double.compare(_dv, 0.0d) == 0) return _max;
        return (_flipThreshold - value) * _dv + _min;
    }

    /**
     * Prints info on the normalization procedure.
     */
    @Override
    public String toString()
    {
        return String.format("Min-Max with flip normalization: min = %.6f; max = %.6f; threshold = %.6f", _min, _max, _flipThreshold);
    }

    /**
     * Constructs cloned object.
     *
     * @return cloned object
     */
    public AbstractMinMaxNormalization getClone()
    {
        return new LinearWithFlip(_min, _max, _flipThreshold);
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
        if (n instanceof LinearWithFlip) _flipThreshold = ((LinearWithFlip) n)._flipThreshold;
    }
}
