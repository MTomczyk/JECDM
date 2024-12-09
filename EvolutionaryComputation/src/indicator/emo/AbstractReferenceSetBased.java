package indicator.emo;

import indicator.AbstractPerformanceIndicator;
import indicator.IPerformanceIndicator;
import space.distance.IDistance;

/**
 * Abstract class that provides common functionalities for performance indicators based on comparison with a reference set.
 *
 * @author MTomczyk
 */


public abstract class AbstractReferenceSetBased extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Distance function employed.
     */
    protected final IDistance _distance;

    /**
     * Reference set employed.
     */
    protected final double[][] _referenceSet;

    /**
     * Parameterized constructor.
     *
     * @param distance     distance function employed
     * @param referenceSet reference set employed
     */
    public AbstractReferenceSetBased(IDistance distance, double[][] referenceSet)
    {
        super(true);
        _distance = distance;
        _referenceSet = referenceSet;
    }
}
