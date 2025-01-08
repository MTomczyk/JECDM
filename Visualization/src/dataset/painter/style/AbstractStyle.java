package dataset.painter.style;

import color.gradient.Gradient;
import container.GlobalContainer;
import container.PlotContainer;
import dataset.painter.style.size.IRelativeSize;
import dataset.painter.style.size.RelativeToMinDrawingAreaBoundsSize;

/**
 * Abstract class representing marker/line style.
 *
 * @author MTomczyk
 */
public abstract class AbstractStyle
{
    /**
     * Object size (relative size; can be 0.0: object is not drawn).
     */
    public final float _size;

    /**
     * Object responsible for establishing relative size.
     */
    public IRelativeSize _relativeSize = new RelativeToMinDrawingAreaBoundsSize();

    /**
     * Color used when drawing the object (can be null: the object is not drawn). Note that it can be either
     * a mono color or a gradient.
     */
    public final Gradient _color;

    /**
     * Supportive index pointing to the display range used when determining the gradient color (can be null: not used).
     */
    public final int _drID;

    /**
     * Supportive field that, if provided (not null), surpass the original size field when determining legend entry size.
     */
    public final Float _legendSize;


    /**
     * Parameterized constructor.
     *
     * @param size       object size (can be 0.0: object is not drawn)
     * @param color      color used when drawing the object (can be null: the object is not drawn); note that it can be either a mono color or a gradient
     * @param drID       supportive index pointing to the display range used when determining the line gradient color (can be null: not used)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public AbstractStyle(float size, Gradient color, int drID, Float legendSize)
    {
        _size = size;
        _color = color;
        _drID = drID;
        _legendSize = legendSize;
    }


    /**
     * Determines whether the object is drawable based on the parameters set.
     *
     * @return if true, object can be drawn; false otherwise
     */
    public boolean isDrawable()
    {
        return ((Float.compare(_size, 0.0f) > 0) && (_color != null));
    }

    /**
     * Supportive method for calculating an object's relative size (marker size/line width).
     *
     * @param GC global container
     * @param PC plot container
     * @return relative size
     */
    public float calculateRelativeSize(GlobalContainer GC, PlotContainer PC)
    {
        return calculateRelativeSize(GC, PC, _size);
    }

    /**
     * Supportive method for calculating an object's relative size (marker size/line width).
     *
     * @param GC   global container
     * @param PC   plot container
     * @param size surpasses the size field value (can be null; then the size field value is used)
     * @return relative size
     */
    public float calculateRelativeSize(GlobalContainer GC, PlotContainer PC, Float size)
    {
        if (size == null) return _relativeSize.getSize(GC, PC, _size);
        return _relativeSize.getSize(GC, PC, size);
    }

}
