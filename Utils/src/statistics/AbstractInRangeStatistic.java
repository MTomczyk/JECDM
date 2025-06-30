package statistics;

import space.Range;

/**
 * Abstract implementation of {@link IStatistic}. Founds the calculation process on samples that are supposed to be
 * in the specified range.
 *
 * @author MTomczyk
 */
public class AbstractInRangeStatistic extends AbstractStatistic implements IStatistic
{
    /**
     * Range for valid samples.
     */
    protected final Range _range;

    /**
     * Parameterized constructor.
     *
     * @param name  string representation
     * @param range range for valid samples
     */
    public AbstractInRangeStatistic(String name, Range range)
    {
        super(name);
        _range = range;
    }
}
