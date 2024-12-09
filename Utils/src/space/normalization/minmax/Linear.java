package space.normalization.minmax;

import space.normalization.INormalization;

/**
 * Performs min-max normalization (linear interpolation).
 *
 * @author MTomczyk
 */


public class Linear extends AbstractMinMaxNormalization implements INormalization
{
    /**
     * Default constructor (sets min to 0.0 and max to 1.0).
     */
    public Linear()
    {
        this(0.0d, 1.0d);
    }

    /**
     * Parameterized constructor.
     *
     * @param min min value
     * @param max max value
     */
    public Linear(double min, double max)
    {
        super(min, max);
    }

    /**
     * Prints info on the normalization procedure.
     *
     */
    @Override
    public String toString()
    {
        return String.format("Linear normalization: min = %.6f; max = %.6f", _min, _max);
    }

    /**
     * Constructs cloned object.
     *
     * @return cloned object
     */
    @Override
    public AbstractMinMaxNormalization getClone()
    {
        return new Linear(_min, _max);
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
    }
}
