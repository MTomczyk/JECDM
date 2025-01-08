
package dataset.painter.style;

import color.gradient.Color;
import color.gradient.Gradient;
import container.GlobalContainer;
import container.PlotContainer;
import dataset.painter.style.enums.Arrow;

/**
 * Encapsulates various parameters related to arrow style.
 *
 * @author MTomczyk
 */
public class ArrowStyle extends AbstractStyle
{
    /**
     * Arrow style.
     */
    public final Arrow _arrow;

    /**
     * Arrow width.
     */
    public final float _width;

    /**
     * Supportive field that, if provided (not null), surpass the original size field when determining legend entry size (width).
     */
    public final Float _legendWidth;

    /**
     * Constructs and returns a (deep) copy of this marker style.
     *
     * @return cloned marker style
     */
    public ArrowStyle getClone()
    {
        Gradient g = null;
        if (_color != null) g = _color.getClone();
        ArrowStyle s = new ArrowStyle(_size, _width, g, _drID, _legendSize, _legendWidth, _arrow);
        if (_relativeSize != null) s._relativeSize = _relativeSize.getClone();
        return s;
    }

    /**
     * Determines whether the object is drawable based on the parameters set.
     *
     * @return if true, object can be drawn; false otherwise
     */
    @Override
    public boolean isDrawable()
    {
        if (!super.isDrawable()) return false;
        if (!(Float.compare(_width, 0.0f) > 0)) return false;
        if (_arrow == null) return false;
        return !_arrow.equals(Arrow.NONE);
    }

    /**
     * Parameterized constructor. Sets the arrow style to TRIANGULAR_2D.
     *
     * @param length arrow length (percent value 0-100, relative size, implementation dependent)
     * @param width  arrow width (percent value 0-100, relative size, implementation dependent)
     * @param color  color used when filling the arrow (beginning; can be null: the arrow is not filled); note that it can be either a mono color or a gradient
     */
    public ArrowStyle(float length, float width, Color color)
    {
        this(length, width, color, -1);
    }

    /**
     * Parameterized constructor.
     *
     * @param length arrow length (percent value 0-100, relative size, implementation dependent)
     * @param width  arrow width (percent value 0-100, relative size, implementation dependent)
     * @param color  color used when filling the arrow (beginning; can be null: the arrow is not filled); note that it can be either a mono color or a gradient
     * @param arrow  style used when printing the ending of an arrow (can be null, not used)
     */
    public ArrowStyle(float length, float width, Color color, Arrow arrow)
    {
        this(length, width, color, -1, null, null, arrow);
    }

    /**
     * Parameterized constructor. Sets the arrow style to TRIANGULAR_2D.
     *
     * @param length arrow length (percent value 0-100, relative size, implementation dependent)
     * @param width  arrow width (percent value 0-100, relative size, implementation dependent)
     * @param color  color used when filling the arrow (beginning; can be null: the arrow is not filled); note that it can be either a mono color or a gradient
     * @param drID   supportive index pointing to the display range used when determining the arrow fill gradient color (beginning; can be null: not used)* @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public ArrowStyle(float length, float width, Gradient color, int drID)
    {
        this(length, width, color, drID, null, null, Arrow.TRIANGULAR_2D);
    }

    /**
     * Parameterized constructor.
     *
     * @param length arrow length (percent value 0-100, relative size, implementation dependent)
     * @param width  arrow width (percent value 0-100, relative size, implementation dependent)
     * @param color  color used when filling the arrow (beginning; can be null: the arrow is not filled); note that it can be either a mono color or a gradient
     * @param drID   supportive index pointing to the display range used when determining the arrow fill gradient color (beginning; can be null: not used)* @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     * @param arrow  style used when printing the ending of an arrow (can be null, not used)
     */
    public ArrowStyle(float length, float width, Gradient color, int drID, Arrow arrow)
    {
        this(length, width, color, drID, null, null, arrow);
    }

    /**
     * Parameterized constructor.
     *
     * @param length       arrow length (percent value 0-100, relative size, implementation dependent)
     * @param width        arrow width (percent value 0-100, relative size, implementation dependent)
     * @param color        color used when filling the arrow (beginning; can be null: the arrow is not filled); note that it can be either a mono color or a gradient
     * @param legendLength supportive field that, if provided (not null), surpasses the original size field when determining legend entry size (arrow length)
     * @param legendWidth  supportive field that, if provided (not null), surpasses the original size field when determining legend entry size (arrow width)
     * @param arrow        style used when printing the ending of an arrow (can be null, not used)
     */
    public ArrowStyle(float length, float width, Color color, Float legendLength, Float legendWidth, Arrow arrow)
    {
        this(length, width, color, -1, legendLength, legendWidth, arrow);
    }

    /**
     * Parameterized constructor.
     *
     * @param length       arrow length (percent value 0-100, relative size, implementation dependent)
     * @param width        arrow width (percent value 0-100, relative size, implementation dependent)
     * @param color        color used when filling the arrow (beginning; can be null: the arrow is not filled); note that it can be either a mono color or a gradient
     * @param drID         supportive index pointing to the display range used when determining the arrow fill gradient color (beginning; can be null: not used)* @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     * @param legendLength supportive field that, if provided (not null), surpasses the original size field when determining legend entry size (arrow length)
     * @param legendWidth  supportive field that, if provided (not null), surpasses the original size field when determining legend entry size (arrow width)
     * @param arrow        style used when printing the ending of an arrow (can be null, not used)
     */
    public ArrowStyle(float length, float width, Gradient color, int drID, Float legendLength, Float legendWidth, Arrow arrow)
    {
        super(length, color, drID, legendLength);
        _legendWidth = legendWidth;
        _width = width;
        _arrow = arrow;
    }

    /**
     * Supportive method for calculating an arrow's relative length
     *
     * @param GC     global container
     * @param PC     plot container
     * @param length surpasses the size field value (can be null; then the size field value is used)
     * @return relative size
     */
    public float calculateRelativeLength(GlobalContainer GC, PlotContainer PC, Float length)
    {
        if (length == null) return _relativeSize.getSize(GC, PC, _size);
        return _relativeSize.getSize(GC, PC, length);
    }

    /**
     * Supportive method for calculating an arrow's relative width
     *
     * @param GC    global container
     * @param PC    plot container
     * @param width surpasses the size field value (can be null; then the size field value is used)
     * @return relative size
     */
    public float calculateRelativeWidth(GlobalContainer GC, PlotContainer PC, Float width)
    {
        if (width == null) return _relativeSize.getSize(GC, PC, _width);
        return _relativeSize.getSize(GC, PC, width);
    }
}
