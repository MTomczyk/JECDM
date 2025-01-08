package dataset.painter.style;

/**
 * Wrapper for the arrow styles (beginning and ending).
 *
 * @author MTomczyk
 */
public class ArrowStyles
{
    /**
     * Arrow style (beginning).
     */
    public final ArrowStyle _bas;

    /**
     * Arrow style (ending).
     */
    public final ArrowStyle _eas;

    /**
     * Parameterized constructor. Sets the beginning arrow style to null.
     *
     * @param eas ending arrow style
     */
    public ArrowStyles(ArrowStyle eas)
    {
        this(null, eas);
    }

    /**
     * Parameterized constructor.
     *
     * @param bas arrow style (beginning).
     * @param eas arrow style (ending).
     */
    public ArrowStyles(ArrowStyle bas, ArrowStyle eas)
    {
        _bas = bas;
        _eas = eas;
    }

    /**
     * Returns true if beginning or ending is drawable.
     *
     * @return true if beginning or ending is drawable, false if both are not renderable
     */
    public boolean isDrawable()
    {
        return ((isBeginningDrawable()) || (isEndingDrawable()));
    }

    /**
     * Returns true if the beginning of the arrow is drawable.
     *
     * @return true if the beginning of the arrow is drawable, false otherwise
     */
    public boolean isBeginningDrawable()
    {
        if (_bas == null) return false;
        return (_bas.isDrawable());
    }

    /**
     * Returns true if the ending of the arrow is drawable.
     *
     * @return true if the ending of the arrow is drawable, false otherwise
     */
    public boolean isEndingDrawable()
    {
        if (_eas == null) return false;
        return (_eas.isDrawable());
    }

    /**
     * Clones the object.
     *
     * @return cloned object
     */
    public ArrowStyles getClone()
    {
        ArrowStyle bs = null;
        ArrowStyle es = null;
        if (_bas != null) bs = _bas.getClone();
        if (_eas != null) es = _eas.getClone();
        return new ArrowStyles(bs, es);
    }
}
