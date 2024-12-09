package statistics.movingaverage;

/**
 * Helps to calculate moving average (abstract class).
 *
 * @author MTomczyk
 */

public abstract class AbstractMovingAverage
{

    /**
     * Pointer to the most recently updated data index.
     */
    protected int _pointer;

    /**
     * Denominator (average = nominator / denominator).
     */
    protected int _denom;

    /**
     * Counts the number of times value was added to the data set (reset when the average is recalculated from scratch).
     */
    protected int _counter;

    /**
     * The average is dynamically updated (incremental approach). This field determines, however, the number of
     * additions, after which the average is calculated from scratch to avoid accumulating numerical errors.
     */
    @SuppressWarnings("FieldCanBeLocal")
    protected final int _recalculateFromScratchEvery = 100;


    /**
     * Determines the window size.
     *
     */
    public AbstractMovingAverage()
    {
        _pointer = 0;
        _denom = 0;
        _counter = 0;
    }
}
