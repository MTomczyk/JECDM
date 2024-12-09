package dataset.painter.style.size;

import container.GlobalContainer;
import container.PlotContainer;

/**
 * Represents a fixed value.
 *
 * @author MTomczyk
 */


public class FixedSize implements IRelativeSize
{
    /**
     * Fixed size.
     */
    private final int _size;

    /**
     * Fixed size.
     *
     * @param size fixed size
     */
    public FixedSize(int size)
    {
        _size = size;
    }

    /**
     * Returns the fixed size
     *
     * @param GC             global container
     * @param PC             plot container
     * @param referenceValue input (fixed) size defined as a percent value (does not matter in this implementation)
     * @return fixed size
     */
    public float getSize(GlobalContainer GC, PlotContainer PC, float referenceValue)
    {
        return _size;
    }

    /**
     * Constructs object's clone.
     *
     * @return deep copy
     */
    @Override
    public IRelativeSize getClone()
    {
        return new FixedSize(_size);
    }
}
