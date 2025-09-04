package component.legend;


import component.AbstractSwingComponent;
import container.PlotContainer;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import scheme.AbstractScheme;
import scheme.enums.*;
import utils.DrawUtils;
import utils.Font;
import utils.Projection;
import utils.Size;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Class representing a plot legend.
 *
 * @author MTomczyk
 */
public abstract class AbstractLegend extends AbstractSwingComponent
{

    /**
     * Params container.
     */
    public static class Params extends AbstractSwingComponent.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name component name
         * @param PC   plot container: allows accessing various plot components
         */
        public Params(String name, PlotContainer PC)
        {
            super(name, PC);
        }
    }

    /**
     * Supporting object for rendering legend entries.
     */
    protected IEntryPainter _entryPainter;

    /**
     * Stores information on the properties of the legend drawing area.
     */
    protected final Dimensions _dimensions;

    /**
     * Offset : distance from the borders of the drawing area.
     */
    protected final Size _borderOffset;

    /**
     * Inner offset : distance between legend entries and legend borders.
     */
    protected final Size _innerOffset;

    /**
     * Spacing: distance between legend entries (rows).
     */
    protected final Size _spacing;

    /**
     * Spacing between the left column (drawings) and the right (labels).
     */
    protected final Size _drawingLabelSeparator;

    /**
     * Entries per column limit
     */
    protected int _entriesPerColumnLimit;

    /**
     * Spacing between the left column (drawings) and the right (labels).
     */
    protected final Size _columnsSeparator;

    /**
     * Font used when displaying entries.
     */
    protected final Font _entryFont;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractLegend(AbstractLegend.Params p)
    {
        super(p._name, p._PC);
        _borderOffset = new Size();
        _innerOffset = new Size();
        _spacing = new Size();
        _drawingLabelSeparator = new Size();
        _entryFont = new Font();
        _dimensions = new Dimensions();
        _columnsSeparator = new Size();
        _entriesPerColumnLimit = 1;
        instantiateEntryPainter();
    }

    /**
     * Auxiliary method for instantiating entry painter.
     */
    protected void instantiateEntryPainter()
    {

    }

    /**
     * Called by {@link layoutmanager.BaseLayoutManager} to determine expected dimension so that the legend can be
     * properly positioned.
     * The result is stored in the object "_dimensions" field.
     *
     * @param g graphics context
     */
    @SuppressWarnings({"DuplicatedCode"})
    public void calculateExpectedDimensions(Graphics g)
    {
        if (g == null) return;

        Graphics2D g2d = (Graphics2D) (g.create());
        g2d.setFont(_entryFont._font);
        g2d.setColor(_entryFont._color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1.0f));

        _dimensions.reset();
        _dimensions._expectedDimensions[0] = _innerOffset._actualSize * 2.0f;
        _dimensions._expectedDimensions[1] = _innerOffset._actualSize * 2.0f;

        float labelColumnWidth = -1.0f;
        float entryHeight = -1.0f;
        float maximumMarkerLineArrowSize = -1.0f; // for rescalling

        if ((_PC.getDrawingArea() != null) && (_PC.getDataSets() != null))
        {
            for (IDataSet ds : _PC.getDataSets())
            {
                if (ds == null) continue;
                if (!ds.isDisplayableOnLegend()) continue;
                String usedName = ds.getLegendLabel();
                if (usedName == null) usedName = ds.getName();
                if (usedName == null) continue;

                _dimensions._noEntries++;

                // Label-based adjustment
                Rectangle2D b = Font.getCorrectDimensions(g2d, usedName);
                if (Double.compare(b.getWidth(), labelColumnWidth) > 0) labelColumnWidth = (float) b.getWidth();
                if (Double.compare(b.getHeight(), entryHeight) > 0) entryHeight = (float) b.getHeight();

                // Marker based adjustment
                if ((ds.getMarkerStyle() != null) && (ds.getMarkerStyle().isDrawable()))
                {
                    _dimensions._drawMarkers = true;
                    MarkerStyle MS = ds.getMarkerStyle();
                    float ms = MS.calculateRelativeSize(_GC, _PC, MS._legendSize);
                    if (ms > maximumMarkerLineArrowSize) maximumMarkerLineArrowSize = ms;
                    if ((MS.isToBeFilled()) && (!MS._color.isMonoColor())) _dimensions._markersUseGradient = true;
                    if ((MS.areEdgesToBeDrawn()) && (!MS._edge._color.isMonoColor()))
                        _dimensions._markersUseGradient = true;
                }
                if ((ds.getLineStyle() != null) && (ds.getLineStyle().isDrawable()))
                {
                    _dimensions._drawLines = true;
                    LineStyle LS = ds.getLineStyle();
                    float ls = LS.calculateRelativeSize(_GC, _PC, LS._legendSize);
                    if (ls > maximumMarkerLineArrowSize) maximumMarkerLineArrowSize = ls;
                }
                if (ds.getArrowStyles() != null)
                {
                    if (ds.getArrowStyles().isBeginningDrawable())
                    {
                        _dimensions._drawBArrows = true;
                        ArrowStyle as = ds.getArrowStyles()._bas;
                        float ll = as.calculateRelativeLength(_GC, _PC, as._legendSize);
                        float lw = as.calculateRelativeWidth(_GC, _PC, as._legendWidth);
                        if (Float.compare(ll, maximumMarkerLineArrowSize) > 0) maximumMarkerLineArrowSize = ll;
                        if (Float.compare(lw, maximumMarkerLineArrowSize) > 0) maximumMarkerLineArrowSize = lw;
                        if (!as._color.isMonoColor()) _dimensions._bArrowsUseGradients = true;
                    }
                    if (ds.getArrowStyles().isEndingDrawable())
                    {
                        _dimensions._drawEArrows = true;
                        ArrowStyle as = ds.getArrowStyles()._eas;
                        float ll = as.calculateRelativeLength(_GC, _PC, as._legendSize);
                        float lw = as.calculateRelativeWidth(_GC, _PC, as._legendWidth);
                        if (Float.compare(ll, maximumMarkerLineArrowSize) > 0) maximumMarkerLineArrowSize = ll;
                        if (Float.compare(lw, maximumMarkerLineArrowSize) > 0) maximumMarkerLineArrowSize = lw;
                        if (!as._color.isMonoColor()) _dimensions._eArrowsUseGradients = true;
                    }
                }
            }
        }

        if (_dimensions._noEntries == 0) return;

        _dimensions._noColumns = _dimensions._noEntries / _entriesPerColumnLimit;
        if (_dimensions._noEntries % _entriesPerColumnLimit != 0) _dimensions._noColumns++;


        _dimensions._labelColumnWidth = labelColumnWidth;
        _dimensions._entryHeight = entryHeight;
        _dimensions._drawingColumnWidth = 0.0f;

        _dimensions._expectedDimensions[0] += (labelColumnWidth * _dimensions._noColumns); // offset + labels

        // CALCULATE HALF SEGMENTS
        _dimensions._noHalfSegments = 0;
        if (_dimensions._drawMarkers)
        {
            if (_dimensions._markersUseGradient) _dimensions._noHalfSegments += 8;
            else _dimensions._noHalfSegments += 2;
            if (_dimensions._drawLines) _dimensions._noHalfSegments += 2;
        }
        else if (_dimensions._drawLines) _dimensions._noHalfSegments += 6;

        if (_dimensions._drawBArrows)
        {
            _dimensions._noHalfSegments += 2;
            if (_dimensions._bArrowsUseGradients) _dimensions._noHalfSegments += 6;
        }
        if (_dimensions._drawEArrows)
        {
            _dimensions._noHalfSegments += 2;
            if (_dimensions._eArrowsUseGradients) _dimensions._noHalfSegments += 6;
        }

        // CALCULATE STARTING SEGMENTS
        if (_dimensions._drawLines)
        {
            _dimensions._lineBeginningHalfSegment = 0;
            _dimensions._lineEndingHalfSegment = _dimensions._noHalfSegments;
        }

        int offset = 0;
        if (_dimensions._drawBArrows)
        {
            _dimensions._lineBeginningHalfSegment = 1;
            offset += 2;
            if (_dimensions._bArrowsUseGradients)
            {
                offset += 6;
                _dimensions._bArrowsCentersNoHalfSegments = new int[]{1, 4, 7};
            }
            else _dimensions._bArrowsCentersNoHalfSegments = new int[]{1};
        }
        if (_dimensions._drawEArrows)
        {
            _dimensions._lineEndingHalfSegment = _dimensions._noHalfSegments - 1;
            if (_dimensions._eArrowsUseGradients) _dimensions._eArrowsCentersNoHalfSegments
                    = new int[]{_dimensions._noHalfSegments - 7,
                    _dimensions._noHalfSegments - 4,
                    _dimensions._noHalfSegments - 1};
            else _dimensions._eArrowsCentersNoHalfSegments = new int[]{_dimensions._noHalfSegments - 1};
        }

        if (_dimensions._drawMarkers)
        {
            if (_dimensions._drawLines) offset++;
            if (_dimensions._markersUseGradient) _dimensions._markerCentersNoHalfSegments
                    = new int[]{offset + 1, offset + 4, offset + 7};
            else _dimensions._markerCentersNoHalfSegments = new int[]{offset + 1};
        }

        float drawingColumnWidth = _dimensions._noHalfSegments * entryHeight / 2.0f;

        _dimensions._expectedDimensions[0] += drawingColumnWidth * _dimensions._noColumns; // separators are already included
        _dimensions._drawingColumnWidth = drawingColumnWidth;

        float dw = drawingColumnWidth / _dimensions._noHalfSegments;

        // CALCULATE CENTERS
        if (_dimensions._drawMarkers)
        {
            _dimensions._markersX = new float[_dimensions._markerCentersNoHalfSegments.length];
            for (int i = 0; i < _dimensions._markerCentersNoHalfSegments.length; i++)
                _dimensions._markersX[i] = dw * _dimensions._markerCentersNoHalfSegments[i];
        }
        if (_dimensions._drawLines)
        {
            _dimensions._lineBeginnningX = _dimensions._lineBeginningHalfSegment * dw;
            _dimensions._lineEndingX = _dimensions._lineEndingHalfSegment * dw;
        }

        if (_dimensions._drawBArrows)
        {
            _dimensions._bArrowsX = new float[_dimensions._bArrowsCentersNoHalfSegments.length];
            for (int i = 0; i < _dimensions._bArrowsCentersNoHalfSegments.length; i++)
                _dimensions._bArrowsX[i] = dw * _dimensions._bArrowsCentersNoHalfSegments[i];
        }

        if (_dimensions._drawEArrows)
        {
            _dimensions._eArrowsX = new float[_dimensions._eArrowsCentersNoHalfSegments.length];
            for (int i = 0; i < _dimensions._eArrowsCentersNoHalfSegments.length; i++)
                _dimensions._eArrowsX[i] = dw * _dimensions._eArrowsCentersNoHalfSegments[i];
        }

        if (_dimensions._drawMarkers || _dimensions._drawLines) // arrows can be drawn only when lines are
        {
            _dimensions._drawingLabelColumnsSeparator = _drawingLabelSeparator._actualSize;

            float d = _columnsSeparator._actualSize * (_dimensions._noColumns - 1) +
                    _dimensions._drawingLabelColumnsSeparator * _dimensions._noColumns;
            _dimensions._expectedDimensions[0] += d;

        }

        if (_dimensions._noEntries > _entriesPerColumnLimit)
            _dimensions._expectedDimensions[1] += (_entriesPerColumnLimit - 1) * _spacing._actualSize + _entriesPerColumnLimit * entryHeight;
        else
            _dimensions._expectedDimensions[1] += (_dimensions._noEntries - 1) * _spacing._actualSize + _dimensions._noEntries * entryHeight;

        if (Float.compare(maximumMarkerLineArrowSize, 0.0f) > 0)
        {
            if (_dimensions._drawMarkers)
                _dimensions._markerScalingFactor = entryHeight / maximumMarkerLineArrowSize * _dimensions._markerScalingFactorMultiplier;
            if (_dimensions._drawLines)
                _dimensions._lineScalingFactor = entryHeight / maximumMarkerLineArrowSize * _dimensions._lineScalingFactorMultiplier;
            if (_dimensions._drawBArrows || _dimensions._drawEArrows)
                _dimensions._arrowsScalingFactor = entryHeight / maximumMarkerLineArrowSize * _dimensions._arrowsScalingFactorMultiplier;
        }

        g2d.dispose();
    }

    /**
     * Getter for dimensions (supportive container-class storing information on the properties of the legend drawing
     * area.)
     *
     * @return legend dimensions
     */
    public Dimensions getExpectedDimensions()
    {
        return _dimensions;
    }

    /**
     * Returns current offset (distance from the borders of the drawing area)
     *
     * @return current offset (distance from the borders of the drawing area)
     */
    public Size getOffset()
    {
        return _borderOffset;
    }

    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (_dimensions._noEntries == 0) return;

        Graphics g2 = g.create();
        Graphics2D g2d = (Graphics2D) g2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float by = _translationVector[1] + _innerOffset._actualSize + _dimensions._entryHeight / 2.0f;
        float bx = _translationVector[0] + _innerOffset._actualSize; // base x
        float labelShift = _dimensions._drawingColumnWidth + _dimensions._drawingLabelColumnsSeparator;

        g2.setFont(_entryFont._font);
        g2.setColor(_entryFont._color);
        g2.setClip(Projection.getP(_translationVector[0]),
                Projection.getP(_translationVector[1]),
                Projection.getP(_primaryDrawingArea.width),
                Projection.getP(_primaryDrawingArea.height));

        Rectangle2D referenceB = Font.getReferenceTextCorrectDimensions(g2d);

        Stroke defaultStroke = new BasicStroke(1.0f);

        if (_PC.getDataSets() != null)
        {
            int no = 0;
            for (IDataSet ds : _PC.getDataSets())
            {
                if (ds == null) continue;
                if (!ds.isDisplayableOnLegend()) continue;
                String usedName = ds.getLegendLabel();
                if (usedName == null) usedName = ds.getName();
                if (usedName == null) continue;

                float mod = (float) (referenceB.getHeight() / 2.0f);

                g2d.setStroke(defaultStroke);
                g2.setColor(_entryFont._color);

                g2.drawString(usedName,
                        Projection.getP((float) (bx + labelShift - referenceB.getMinX())) - 1,
                        Projection.getP(by + mod) - 1);

                LineStyle LS = ds.getLineStyle();
                MarkerStyle MS = ds.getMarkerStyle();
                ArrowStyles AS = ds.getArrowStyles();

                // draw lines
                if ((_dimensions._drawLines) && (ds.getLineStyle() != null) && (ds.getLineStyle().isDrawable()))
                {
                    _entryPainter.setLineStyle(LS);
                    float ratio = _dimensions._lineScalingFactor * LS.calculateRelativeSize(_GC, _PC, LS._legendSize) / LS._size;

                    BasicStroke modStroke = DrawUtils.constructRescaledStroke(ratio, LS._stroke);
                    g2d.setStroke(modStroke);
                    float x1 = bx + _dimensions._lineBeginnningX;
                    float x2 = bx + _dimensions._lineEndingX;
                    if (LS._color.isMonoColor())
                    {
                        g2.setColor(LS._color.getColor(0.0f));
                        _entryPainter.drawLine(g2, x1 - 1.0f, by - 1, x2 - 1, by - 1.0f);
                    }
                    else _entryPainter.drawGradientLine(g2, LS._color, modStroke.getLineWidth(),
                            x1 - 1.0f, x2 - 1.0f, by - 1);
                }

                if ((_dimensions._drawMarkers) && (ds.getMarkerStyle() != null) && (ds.getMarkerStyle().isDrawable()))
                {
                    _entryPainter.setMarkerStyle(MS);
                    float mSize = MS.calculateRelativeSize(_GC, _PC, MS._legendSize) * _dimensions._markerScalingFactor;

                    if (MS.isToBeFilled())
                    {
                        int lim = _dimensions._markersX.length;
                        if (lim == 1) g2.setColor(MS._color.getColor(0.0f));

                        for (int i = 0; i < _dimensions._markersX.length; i++)
                        {
                            if (lim > 1) g2.setColor(MS._color.getColor((float) i / (lim - 1)));
                            _entryPainter.drawMarker(g2, bx + _dimensions._markersX[i] - 1, by - 1, mSize,
                                    null, null, true, false);
                        }
                    }

                    if (MS.areEdgesToBeDrawn())
                    {
                        float ratio = _dimensions._markerScalingFactor * MS._edge.calculateRelativeSize(_GC, _PC) / MS._edge._size;
                        if (MS._edge._legendSize != null)
                            ratio = _dimensions._markerScalingFactor * MS._edge._legendSize; // surpass

                        g2d.setStroke(DrawUtils.constructRescaledStroke(ratio, MS._edge._stroke));
                        int lim = _dimensions._markersX.length;
                        if (lim == 1) g2.setColor(MS._edge._color.getColor(0.0f));

                        for (int i = 0; i < _dimensions._markersX.length; i++)
                        {
                            if (lim > 1) g2.setColor(MS._edge._color.getColor((float) i / (lim - 1)));
                            _entryPainter.drawMarker(g2, bx + _dimensions._markersX[i] - 1, by - 1, mSize,
                                    null, null, false, true);
                        }
                    }
                }

                if ((_dimensions._drawBArrows) && (AS != null) && (AS.isBeginningDrawable()))
                {
                    _entryPainter.setArrowStyle(AS._bas);
                    float mLength = AS._bas.calculateRelativeLength(_GC, _PC, AS._bas._legendSize)
                            * _dimensions._arrowsScalingFactor;
                    float mWidth = AS._bas.calculateRelativeWidth(_GC, _PC, AS._bas._legendWidth)
                            * _dimensions._arrowsScalingFactor;

                    if (AS._bas._color.isMonoColor())
                    {
                        g2.setColor(AS._bas._color.getColor(0.0f));
                        _entryPainter.drawArrow(g2, bx + _dimensions._bArrowsX[0] - 1, by - 1, mLength, mWidth, null, true);
                    }
                    else
                    {
                        int lim = _dimensions._bArrowsX.length;
                        if (lim == 1) g2.setColor(AS._bas._color.getColor(0.0f));
                        for (int i = 0; i < _dimensions._bArrowsX.length; i++)
                        {
                            if (lim > 1) g2.setColor(AS._bas._color.getColor((float) i / (lim - 1)));
                            _entryPainter.drawArrow(g2, bx + _dimensions._bArrowsX[i] - 1, by - 1, mLength, mWidth, null, true);
                        }
                    }
                }

                if ((_dimensions._drawEArrows) && (AS != null) && (AS.isEndingDrawable()))
                {
                    _entryPainter.setArrowStyle(AS._eas);
                    float mLength = AS._eas.calculateRelativeLength(_GC, _PC, AS._eas._legendSize)
                            * _dimensions._arrowsScalingFactor;
                    float mWidth = AS._eas.calculateRelativeWidth(_GC, _PC, AS._eas._legendWidth)
                            * _dimensions._arrowsScalingFactor;

                    if (AS._eas._color.isMonoColor())
                    {
                        g2.setColor(AS._eas._color.getColor(0.0f));
                        _entryPainter.drawArrow(g2, bx + _dimensions._eArrowsX[_dimensions._eArrowsX.length - 1] - 1, by - 1, mLength, mWidth, null, true);
                    }
                    else
                    {
                        int lim = _dimensions._eArrowsX.length;
                        if (lim == 1) g2.setColor(AS._eas._color.getColor(0.0f));

                        for (int i = 0; i < _dimensions._eArrowsX.length; i++)
                        {
                            if (lim > 1) g2.setColor(AS._eas._color.getColor((float) i / (lim - 1)));
                            _entryPainter.drawArrow(g2, bx + _dimensions._eArrowsX[i] - 1, by - 1, mLength, mWidth, null, true);
                        }
                    }

                }

                // ====================================================================================================
                by += (_dimensions._entryHeight + _spacing._actualSize);
                no++;
                if (no >= _entriesPerColumnLimit)
                {
                    no = 0;
                    bx += (_dimensions._drawingColumnWidth + _dimensions._drawingLabelColumnsSeparator +
                            _dimensions._labelColumnWidth + _columnsSeparator._actualSize);
                    by = _translationVector[1] + _innerOffset._actualSize + _dimensions._entryHeight / 2.0f;
                }
            }
        }

        g2d.setStroke(defaultStroke);
        g2.dispose();
    }


    /**
     * Called to update the component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);
        _align = scheme.getAlignments(_surpassedAlignments, AlignFields.LEGEND);
        _backgroundColor = scheme.getColors(_surpassedColors, ColorFields.LEGEND_BACKGROUND);

        _borderColor = scheme.getColors(_surpassedColors, ColorFields.LEGEND_BORDER);
        _borderWidth.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_BORDER_WIDTH_FIXED));
        _borderWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_BORDER_WIDTH_RELATIVE_MULTIPLIER));
        _borderWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_BORDER_USE_RELATIVE_WIDTH));
        _borderStroke = getStroke(_borderWidth);

        setOpaque(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_OPAQUE));

        float RV = _PC.getReferenceValueGetter().getReferenceValue();

        _entriesPerColumnLimit = scheme.getNumbers(_surpassedNumbers, NumberFields.LEGEND_NO_ENTRIES_PER_COLUMN_LIMIT);

        _borderOffset.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_OFFSET_FIXED));
        _borderOffset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_OFFSET_RELATIVE_MULTIPLIER));
        _borderOffset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_DRAWING_LABEL_OFFSET_USE_RELATIVE_SIZE));
        _borderOffset.computeActualSize(RV);

        _innerOffset.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_INNER_OFFSET_FIXED));
        _innerOffset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_INNER_OFFSET_RELATIVE_MULTIPLIER));
        _innerOffset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_INNER_OFFSET_USE_RELATIVE_SIZE));
        _innerOffset.computeActualSize(RV);

        _spacing.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_ENTRIES_SPACING_FIXED));
        _spacing.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_ENTRIES_SPACING_RELATIVE_MULTIPLIER));
        _spacing.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_ENTRIES_SPACING_USE_RELATIVE_SIZE));
        _spacing.computeActualSize(RV);

        _drawingLabelSeparator.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_DRAWING_LABEL_SEPARATOR_FIXED));
        _drawingLabelSeparator.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_DRAWING_LABEL_SEPARATOR_RELATIVE_MULTIPLIER));
        _drawingLabelSeparator.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_DRAWING_LABEL_SEPARATOR_USE_RELATIVE_SIZE));
        _drawingLabelSeparator.computeActualSize(RV);

        _columnsSeparator.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_COLUMNS_SEPARATOR_FIXED));
        _columnsSeparator.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_COLUMNS_SEPARATOR_RELATIVE_MULTIPLIER));
        _columnsSeparator.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_COLUMNS_SEPARATOR_USE_RELATIVE_SIZE));
        _columnsSeparator.computeActualSize(RV);

        _entryFont._fontName = scheme.getFonts(_surpassedFonts, FontFields.LEGEND_ENTRY);
        _entryFont._size.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_ENTRY_FONT_SIZE_FIXED));
        _entryFont._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER));
        _entryFont._size.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_ENTRY_FONT_USE_RELATIVE_SIZE));
        _entryFont._size.computeActualSize(RV);
        _entryFont.prepareFont();
        _entryFont._color = scheme.getColors(_surpassedColors, ColorFields.LEGEND_ENTRY_FONT);

        _dimensions._markerScalingFactorMultiplier = scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_MARKER_SCALING_FACTOR_MULTIPLIER);
        _dimensions._lineScalingFactorMultiplier = scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_LINE_SCALING_FACTOR_MULTIPLIER);
    }

    /**
     * Updates bounds of the primary drawing area (should be enclosed within the panel bounds).
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    @Override
    public void setPrimaryDrawingArea(int x, int y, int w, int h)
    {
        super.setPrimaryDrawingArea(x, y, w, h);
    }

    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    @Override
    public void updateRelativeFields()
    {
        float RV = _PC.getReferenceValueGetter().getReferenceValue();
        _borderOffset.computeActualSize(RV);
        _innerOffset.computeActualSize(RV);
        _columnsSeparator.computeActualSize(RV);
        _spacing.computeActualSize(RV);
        _drawingLabelSeparator.computeActualSize(RV);
        _entryFont._size.computeActualSize(RV);
        _entryFont.prepareFont();
        _borderStroke = getStroke(_borderWidth);
    }

}
