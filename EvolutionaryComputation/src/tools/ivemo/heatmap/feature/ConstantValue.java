package tools.ivemo.heatmap.feature;

import population.Specimen;

/**
 * Always returns a constant value. Can be used to illustrate data representing "how many times" events (i.e., when returning one and aggregating the numbers).
 *
 * @author MTomczyk
 */

public class ConstantValue implements IFeatureGetter
{
    /**
     * Value to be returned.
     */
    private final double _v;

    /**
     * Default constructor: sets value to be returned to 0.
     */
    public ConstantValue()
    {
        this(1.0d);
    }

    /**
     * Parameterized constructor.
     *
     * @param v value to be returned
     */
    public ConstantValue(double v)
    {
        _v = v;
    }

    @Override
    public double getFeature(Specimen specimen, int generation, int steadyStateRepeat)
    {
        return _v;
    }
}
