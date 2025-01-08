package dataset.painter.style;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.painter.style.enums.Line;

import java.awt.*;

/**
 * Encapsulates various parameters related to line style.
 *
 * @author MTomczyk
 */

public class LineStyle extends AbstractStyle
{
    /**
     * Line stroke.
     */
    public final BasicStroke _stroke;

    /**
     * Line style.
     */
    public final Line _style;

    /**
     * Constructs and returns a (deep) copy of this marker style.
     *
     * @return cloned marker style
     */
    public LineStyle getClone()
    {
        Gradient g = null;
        if (_color != null) g = _color.getClone();
        LineStyle s = new LineStyle(_size, g, _drID, _legendSize, _style);
        if (_relativeSize != null) s._relativeSize = _relativeSize.getClone();
        return s;
    }

    /**
     * Parameterized constructor.
     *
     * @param width line width (percent value 0-100, relative width, implementation dependent)
     * @param color color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     */
    public LineStyle(float width, Color color)
    {
        this(width, color, Line.REGULAR);
    }

    /**
     * Parameterized constructor.
     *
     * @param width line width (percent value 0-100, relative width, implementation dependent)
     * @param color color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param style line style
     */
    public LineStyle(float width, Color color, Line style)
    {
        this(width, color, 0, null, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param width      line width (percent value 0-100, relative width, implementation dependent)
     * @param color      color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public LineStyle(float width, Color color, Float legendSize)
    {
        this(width, color, 0, legendSize);
    }

    /**
     * Parameterized constructor.
     *
     * @param width      line width (percent value 0-100, relative width, implementation dependent)
     * @param color      color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     * @param style line style
     */
    public LineStyle(float width, Color color, Float legendSize, Line style)
    {
        this(width, color, 0, legendSize, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param width line width (percent value 0-100, relative width, implementation dependent)
     * @param color color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param drID  supportive index pointing to the display range used when determining the line gradient color (can be null: not used)
     */
    public LineStyle(float width, Gradient color, int drID)
    {
        this(width, color, drID, Line.REGULAR);
    }

    /**
     * Parameterized constructor.
     *
     * @param width line width (percent value 0-100, relative width, implementation dependent)
     * @param color color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param drID  supportive index pointing to the display range used when determining the line gradient color (can be null: not used)
     * @param style line style
     */
    public LineStyle(float width, Gradient color, int drID, Line style)
    {
        this(width, color, drID, null, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param width      line width (percent value 0-100, relative width, implementation dependent)
     * @param color      color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param drID       supportive index pointing to the display range used when determining the line gradient color (can be null: not used)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public LineStyle(float width, Gradient color, int drID, Float legendSize)
    {
        this(width, color, drID, legendSize, Line.REGULAR);
    }

    /**
     * Parameterized constructor.
     *
     * @param width      line width (percent value 0-100, relative width, implementation dependent)
     * @param color      color used when drawing the line (can be null: the line is not drawn); note that it can be either a mono color or a gradient
     * @param drID       supportive index pointing to the display range used when determining the line gradient color (can be null: not used)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     * @param style      line style
     */
    public LineStyle(float width, Gradient color, int drID, Float legendSize, Line style)
    {
        super(width, color, drID, legendSize);
        if (Float.compare(width, 0.0f) > 0) _stroke = new BasicStroke(width);
        else _stroke = null;
        _style = style;
    }
}
