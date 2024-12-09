package statistics.incrementalcontainer;

/**
 * Implements a container for floats. The elements can be added incrementally,
 * but the container is of a limited size. Supports restarting and deriving non-null data.
 *
 * @author MTomczyk
 */

public class IncrementalContainerFloat extends AbstractIncrementalContainer
{
    /**
     * Stored floats.
     */
    public final float[] _data;

    /**
     * Container size;
     *
     * @param size container size
     */
    public IncrementalContainerFloat(int size)
    {
        super(size);
        _data = new float[size];
        reset();
    }

    /**
     * Returns the most recently added element.
     *
     * @return the most recently added element
     */
    public float getRecentElement()
    {
        return _data[_cIndex];
    }

    /**
     * Adds a new element to the container. If the size is exceeded, the elements are started being added from the beginning.
     *
     * @param element element to be added.
     */
    public void addElement(float element)
    {
        super.doPreAdd();
        _data[_cIndex] = element;
        super.doPostAdd();
    }

    /**
     * Resets container.
     */
    public void reset()
    {
        super.reset();
        for (int i = 0; i < _size; i++) _data[i] = 0;
    }
}
