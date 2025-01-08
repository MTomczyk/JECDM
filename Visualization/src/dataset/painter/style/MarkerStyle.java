package dataset.painter.style;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.painter.style.enums.Marker;

/**
 * Encapsulates various parameters related to marker style.
 *
 * @author MTomczyk
 */


public class MarkerStyle extends AbstractStyle
{
    /**
     * Edge style.
     */
    public final LineStyle _edge;

    /**
     * Marker style.
     */
    public final Marker _style;

    /**
     * If = 1 (default), every data point is expected to be drawn (starting from the first). If _paintEvery > 1, every "_paintEvery" point is to be drawn. Other value have no effect.
     */
    public int _paintEvery;

    /**
     * Offset: determines a data point starting from which markers should be rendered (works with ``paint every'').
     */
    public int _startPaintingFrom = 0;

    /**
     * Parameterized constructor (marker edge is not drawn).
     *
     * @param size      marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param style     marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     */
    public MarkerStyle(float size, Color fillColor, Marker style)
    {
        this(size, fillColor, 0, style, null, null);
    }

    /**
     * Parameterized constructor (marker edge is not drawn).
     *
     * @param size       marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor  color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param style      marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public MarkerStyle(float size, Color fillColor, Marker style, Float legendSize)
    {
        this(size, fillColor, 0, style, null, legendSize);
    }

    /**
     * Parameterized constructor (marker edge is not drawn).
     *
     * @param size       marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor  color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param style      marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public MarkerStyle(float size, Color fillColor, Float legendSize, Marker style)
    {
        this(size, fillColor, 0, style, null, legendSize);
    }

    /**
     * Parameterized constructor (marker edge is not drawn).
     *
     * @param size      marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param drID      supportive index pointing to the display range used when determining the marker fill gradient color (can be null: not used)
     * @param style     marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     */
    public MarkerStyle(float size, Gradient fillColor, int drID, Marker style)
    {
        this(size, fillColor, drID, style, null, null);
    }

    /**
     * Parameterized constructor (marker edge is not drawn).
     *
     * @param size       marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor  color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param drID       supportive index pointing to the display range used when determining the marker fill gradient color (can be null: not used)
     * @param style      marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public MarkerStyle(float size, Gradient fillColor, int drID, Marker style, Float legendSize)
    {
        this(size, fillColor, drID, style, null, legendSize);
    }

    /**
     * Parameterized constructor (marker edge is not drawn).
     *
     * @param size       marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor  color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param drID       supportive index pointing to the display range used when determining the marker fill gradient color (can be null: not used)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     * @param style      marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     */
    public MarkerStyle(float size, Gradient fillColor, int drID, Float legendSize, Marker style)
    {
        this(size, fillColor, drID, style, null, legendSize);
    }

    /**
     * Parameterized constructor (marker is not filled).
     *
     * @param edge edge style
     */
    public MarkerStyle(LineStyle edge)
    {
        this(0.0f, null, 0, null, edge);
    }

    /**
     * Parameterized constructor (marker is not filled).
     *
     * @param edge       edge style
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public MarkerStyle(LineStyle edge, Float legendSize)
    {
        this(0.0f, null, 0, null, edge, legendSize);
    }


    /**
     * Parameterized constructor.
     *
     * @param size      marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param style     marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param edge      edge style (can be null: edge is not drawn)
     */
    public MarkerStyle(float size, Color fillColor, Marker style, LineStyle edge)
    {
        this(size, fillColor, style, edge, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param size       marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor  color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param style      marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param edge       edge style (can be null: edge is not drawn)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public MarkerStyle(float size, Color fillColor, Marker style, LineStyle edge, Float legendSize)
    {
        super(size, fillColor, 0, legendSize);
        _style = style;
        _edge = edge;
        _paintEvery = 1;
    }

    /**
     * Parameterized constructor.
     *
     * @param size      marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param drID      supportive index pointing to the display range used when determining the marker fill gradient color (can be null: not used)
     * @param style     marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param edge      edge style (can be null: edge is not drawn)
     */
    public MarkerStyle(float size, Gradient fillColor, int drID, Marker style, LineStyle edge)
    {
        this(size, fillColor, drID, style, edge, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param size       marker size (percent value 0-100, relative size, implementation dependent)
     * @param fillColor  color used when filling the marker (can be null: the marker is not filled); note that it can be either a mono color or a gradient
     * @param drID       supportive index pointing to the display range used when determining the marker fill gradient color (can be null: not used)
     * @param style      marker style (AbstractScheme.Marker.MARKER_CIRCLE, AbstractScheme.Marker.MARKER_SQUARE; can be null: markers will not be drawn)
     * @param edge       edge style (can be null: edge is not drawn)
     * @param legendSize supportive field that, if provided (not null), surpasses the original size field when determining legend entry size
     */
    public MarkerStyle(float size, Gradient fillColor, int drID, Marker style, LineStyle edge, Float legendSize)
    {
        super(size, fillColor, drID, legendSize);
        _style = style;
        _edge = edge;
        _paintEvery = 1;
    }

    /**
     * Constructs and returns a (deep) copy of this marker style.
     *
     * @return cloned marker style
     */
    public MarkerStyle getClone()
    {
        LineStyle edge = null;
        if (_edge != null) edge = _edge.getClone();
        Gradient g = null;
        if (_color != null) g = _color.getClone();
        MarkerStyle s = new MarkerStyle(_size, g, _drID, _style, edge, _legendSize);
        if (_relativeSize != null) s._relativeSize = _relativeSize.getClone();
        s._paintEvery = _paintEvery;
        s._startPaintingFrom = _startPaintingFrom;
        return s;
    }

    /**
     * Supportive method that determines whether the marker is to be filled.
     *
     * @return if true, fill the marker; false otherwise
     */
    public boolean isToBeFilled()
    {
        if (_style == null) return false;
        return super.isDrawable();
    }

    /**
     * Supportive method that determines whether the edges are to be drawn.
     *
     * @return if true, draw edges; false otherwise
     */
    public boolean areEdgesToBeDrawn()
    {
        if (_style == null) return false;
        if (_edge == null) return false;
        return _edge.isDrawable();
    }

    /**
     * Determines whether the object is drawable based on the parameters set.
     *
     * @return if true, object can be drawn; false otherwise
     */
    @Override
    public boolean isDrawable()
    {
        if (_style == null) return false;
        return super.isDrawable() || ((_edge != null) && (_edge.isDrawable()));
    }
}
