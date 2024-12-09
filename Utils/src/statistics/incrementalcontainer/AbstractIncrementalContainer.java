package statistics.incrementalcontainer;

/**
 * Implements an abstract container. The elements can be added incrementally,
 * but the container is of a limited size. Supports restarting and deriving non-null data.
 *
 * @author MTomczyk
 */

public abstract class AbstractIncrementalContainer
{
    /**
     * Container size.
     */
    protected final int _size;

    /**
     * The number of stored elements.
     */
    protected int _noElements = 0;

    /**
     * Index ending data sequence (newest data point).
     */
    protected int _cIndex;

    /**
     * Index starting data sequence (oldest data point).
     */
    protected int _bIndex = 0;

    /**
     * Flag indicating whether the elements were started being added from the beginning.
     */
    protected boolean _cycle = false;

    /**
     * Parameterized constructor.
     *
     * @param size container size
     */
    public AbstractIncrementalContainer(int size)
    {
        _size = size;
    }

    /**
     * Resets container.
     */
    public void reset()
    {
        _cIndex = -1;
        _bIndex = 0;
        _noElements = 0;
        _cycle = false;
    }

    /**
     * Returns the number of stored elements.
     *
     * @return the number of stored elements
     */
    public int getNoStoredElements()
    {
        return _noElements;
    }

    /**
     * Returns the index starting data sequence (oldest data point).
     *
     * @return the index starting data sequence (oldest data point)
     */
    public int getBeginIndex()
    {
        return _bIndex;
    }

    /**
     * Returns the index ending data sequence (newest data point).
     *
     * @return the index ending data sequence (newest data point)
     */
    public int getCurrentIndex()
    {
        return _cIndex;
    }

    /**
     * Indicates whether the elements were started being added from the beginning.
     *
     * @return flag indicating whether the elements were started being added from the beginning.
     */
    public boolean cycleBegan()
    {
        return _cycle;
    }

    /**
     * Returns container size.
     *
     * @return container size
     */
    public int getContainerSize()
    {
        return _size;
    }

    /**
     * Auxiliary method updating fields prior to adding a new element to the container.
     */
    protected void doPreAdd()
    {
        // SHIFT INDEX
        if (_cIndex >= _size - 1)
        {
            _cIndex = 0;
            _cycle = true;
        }
        else _cIndex++;
    }

    /**
     * Auxiliary method updating fields after adding a new element to the container.
     */
    protected void doPostAdd()
    {
        if (!_cycle) _noElements++;
        if (_cycle)
        {
            _bIndex = _cIndex + 1;
            if (_bIndex > _size - 1) _bIndex = 0;
        }
    }
}
