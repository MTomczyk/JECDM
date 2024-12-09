package component.legend;


import component.AbstractSwingComponent;
import container.PlotContainer;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import scheme.AbstractScheme;
import scheme.enums.AlignFields;
import scheme.enums.ColorFields;
import scheme.enums.FlagFields;
import scheme.enums.SizeFields;
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
     * Supportive class storing information on the properties of the legend drawing area.
     */
    public static class Dimensions
    {
        /**
         * Supportive field used when drawing entries: height of one entry.
         */
        public float _entryHeight = 0.0f;

        /**
         * Supportive field used when drawing entries: width of the left column where the markers are drawn.
         */
        public float _markerColumnWidth = 0.0f;

        /**
         * X-translation of a center marker.
         */
        public float _markerCenterShiftX = 0.0f;

        /**
         * X-translation of a left marker (if used).
         */
        public float _markerLeftShiftX = 0.0f;

        /**
         * X-translation of a right marker (if used).
         */
        public float _markerRightShiftX = 0.0f;

        /**
         * Supportive field used when drawing entries: width of the right column where the labels are drawn.
         */
        public float _labelColumnWidth = 0.0f;

        /**
         * Supportive field used when drawing entries: spacing between the left and the right column
         */
        public float _columnsSeparator = 0.0f;

        /**
         * Supportive field for scaling legend entries (markers).
         */
        public float _markerScalingFactor = 1.0f;

        /**
         * Supportive field for additionally rescaling the "_markerScalingFactor" field.
         */
        public float _markerScalingFactorMultiplier = 1.0f;

        /**
         * Supportive field for scaling legend entries (lines).
         */
        public float _lineScalingFactor = 1.0f;

        /**
         * Supportive field for additionally rescaling the "_lineScalingFactor" field.
         */
        public float _lineScalingFactorMultiplier = 1.0f;

        /**
         * Expected dimensions of the legend.
         */
        public float[] _expectedDimensions = new float[]{0.0f, 0.0f};

        /**
         * Expected number of entries to be drawn.
         */
        public int _noEntries = 0;

        /**
         * Default constructor.
         */
        public Dimensions()
        {
            reset();
        }


        /**
         * Resets the fields.
         */
        public void reset()
        {
            _entryHeight = 0.0f;
            _markerColumnWidth = 0.0f;
            _labelColumnWidth = 0.0f;
            _columnsSeparator = 0.0f;
            _expectedDimensions[0] = 0.0f;
            _expectedDimensions[1] = 0.0f;
            _markerScalingFactor = 0.75f;
            _noEntries = 0;
        }
    }

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
    protected final Size _offset;

    /**
     * Inner offset : distance between legend entries and legend borders.
     */
    protected final Size _innerOffset;

    /**
     * Spacing: distance between legend entries.
     */
    protected final Size _spacing;

    /**
     * Spacing between the left column (markers) and the right (labels).
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
        _offset = new Size();
        _innerOffset = new Size();
        _spacing = new Size();
        _columnsSeparator = new Size();
        _entryFont = new Font();
        _dimensions = new Dimensions();

        instantiateEntryPainter();
    }

    /**
     * Auxiliary method for instantiating entry painter.
     */
    protected void instantiateEntryPainter()
    {

    }


    /**
     * Called by {@link layoutmanager.BaseLayoutManager} to determine expected dimension so that the legend can be properly positioned.
     * The result is stored in the object "_dimensions" field.
     *
     * @param g graphics context
     */
    @SuppressWarnings("SuspiciousNameCombination")
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
        float maximumMarkerLineSize = -1.0f; // for rescalling

        boolean markers = false;
        boolean markersUseGradients = false;
        boolean lines = false;

        if ((_PC.getDrawingArea() != null) && (_PC.getDataSets() != null))
        {
            for (IDataSet ds : _PC.getDataSets())
            {
                if (ds == null) continue;
                if (ds.getName() == null) continue;
                if (!ds.isDisplayableOnLegend()) continue;
                _dimensions._noEntries++;

                Rectangle2D b = Font.getCorrectDimensions(g2d, ds.getName());

                if (b.getWidth() > labelColumnWidth) labelColumnWidth = (float) b.getWidth();
                if (b.getHeight() > entryHeight) entryHeight = (float) b.getHeight();

                if ((ds.getMarkerStyle() != null) && (ds.getMarkerStyle().isDrawable()))
                {
                    markers = true;

                    MarkerStyle MS = ds.getMarkerStyle();
                    float ms = MS.calculateRelativeSize(_GC, _PC);
                    if (MS._legendSize != null)
                    {
                        ms = MS._legendSize; // surpass
                    }

                    if (ms > maximumMarkerLineSize) maximumMarkerLineSize = ms;
                    if ((MS.isToBeFilled()) && (!MS._color.isMonoColor())) markersUseGradients = true;
                    if ((MS.areEdgesToBeDrawn()) && (!MS._edge._color.isMonoColor())) markersUseGradients = true;
                }
                if ((ds.getLineStyle() != null) && (ds.getLineStyle().isDrawable()))
                {
                    lines = true;
                    LineStyle LS = ds.getLineStyle();
                    float ls = LS.calculateRelativeSize(_GC, _PC);
                    if (LS._legendSize != null) ls = LS._legendSize; // surpass
                    if (ls > maximumMarkerLineSize) maximumMarkerLineSize = ls;
                }

            }
        }

        if (_dimensions._noEntries == 0) return;

        _dimensions._labelColumnWidth = labelColumnWidth;
        _dimensions._entryHeight = entryHeight;
        _dimensions._markerColumnWidth = 0.0f;
        _dimensions._expectedDimensions[0] += labelColumnWidth; // offset + labels

        if (markers)
        {
            if (markersUseGradients)
            {
                _dimensions._expectedDimensions[0] += entryHeight * 4.0f;
                _dimensions._markerColumnWidth += entryHeight * 4.0f;
            }
            else
            {
                _dimensions._expectedDimensions[0] += entryHeight;
                _dimensions._markerColumnWidth += entryHeight;
            }
        }

        if (lines)
        {
            _dimensions._expectedDimensions[0] += entryHeight;
            _dimensions._markerColumnWidth += entryHeight;
        }

        if (markers || lines)
        {
            _dimensions._expectedDimensions[0] += _columnsSeparator._actualSize;
            _dimensions._columnsSeparator = _columnsSeparator._actualSize;
        }

        _dimensions._expectedDimensions[1] += (_dimensions._noEntries - 1) * _spacing._actualSize + _dimensions._noEntries * entryHeight;

        if (Float.compare(maximumMarkerLineSize, 0.0f) > 0)
        {
            if (markers)
                _dimensions._markerScalingFactor = entryHeight / maximumMarkerLineSize * _dimensions._markerScalingFactorMultiplier;
            if (lines)
                _dimensions._lineScalingFactor = entryHeight / maximumMarkerLineSize * _dimensions._lineScalingFactorMultiplier;
        }

        _dimensions._markerCenterShiftX = _dimensions._markerColumnWidth / 2.0f;
        if (markersUseGradients)
        {
            if (lines)
            {
                _dimensions._markerLeftShiftX = _dimensions._markerColumnWidth * (1.0f / 5.0f);
                _dimensions._markerRightShiftX = _dimensions._markerColumnWidth * (4.0f / 5.0f);
            }
            else
            {
                _dimensions._markerLeftShiftX = _dimensions._markerColumnWidth * (1.0f / 8.0f);
                _dimensions._markerRightShiftX = _dimensions._markerColumnWidth * (7.0f / 8.0f);
            }
        }
        else
        {
            _dimensions._markerLeftShiftX = -1.0f;
            _dimensions._markerRightShiftX = -1.0f;
        }


        g2d.dispose();
    }

    /**
     * Getter for dimensions (supportive container-class storing information on the properties of the legend drawing area.)
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
        return _offset;
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

        float y = _translationVector[1] + _innerOffset._actualSize + _dimensions._entryHeight / 2.0f;
        float x = _translationVector[0] + _innerOffset._actualSize;
        float labelShift = _dimensions._markerColumnWidth + _dimensions._columnsSeparator;

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
            for (IDataSet ds : _PC.getDataSets())
            {
                if (ds == null) continue;
                if (!ds.isDisplayableOnLegend()) continue;
                if (ds.getName() == null) continue;

                float mod = (float) (referenceB.getHeight() / 2.0f);

                g2d.setStroke(defaultStroke);
                g2.setColor(_entryFont._color);

                g2.drawString(ds.getName(),
                        Projection.getP((float) (x + labelShift - referenceB.getMinX())) - 1,
                        Projection.getP(y + mod) - 1);

                LineStyle LS = ds.getLineStyle();
                MarkerStyle MS = ds.getMarkerStyle();

                if ((LS != null) && (LS.isDrawable()))
                {
                    _entryPainter.setLineStyle(LS);
                    float ratio = _dimensions._lineScalingFactor * LS.calculateRelativeSize(_GC, _PC) / LS._size;
                    if (LS._legendSize != null)
                        ratio = _dimensions._lineScalingFactor * LS._legendSize; // surpass

                    BasicStroke modStroke = DrawUtils.constructRescaledStroke(ratio, LS._stroke);
                    g2d.setStroke(modStroke);

                    if (LS._color.isMonoColor())
                    {
                        g2.setColor(LS._color.getColor(0.0f));
                        _entryPainter.drawLine(g2, x - 1, y - 1, x + _dimensions._markerColumnWidth - 1, y - 1);
                    }
                    else
                        _entryPainter.drawGradientLine(g2, LS._color, modStroke.getLineWidth(), x - 1, x + _dimensions._markerColumnWidth - 1, y - 1);
                }

                if ((MS != null) && (MS.isDrawable()))
                {
                    _entryPainter.setMarkerStyle(MS);
                    float mSize = MS.calculateRelativeSize(_GC, _PC) * _dimensions._markerScalingFactor;
                    if (MS._legendSize != null) mSize = MS._legendSize * _dimensions._markerScalingFactor; // surpass

                    boolean drawThree = (((MS.isToBeFilled()) && (!MS._color.isMonoColor())) ||
                            ((MS.areEdgesToBeDrawn()) && (!MS._edge._color.isMonoColor())));

                    if (MS.isToBeFilled())
                    {

                        g2.setColor(MS._color.getColor(0.0f));
                        if (!drawThree)
                        {
                            _entryPainter.drawMarker(g2, x + _dimensions._markerCenterShiftX - 1, y - 1, mSize,
                                    null, null, true, false);
                        }
                        else
                        {
                            g2.setColor(MS._color.getColor(0.0f));
                            _entryPainter.drawMarker(g2, x + _dimensions._markerLeftShiftX - 1, y - 1, mSize,
                                    null, null, true, false);

                            g2.setColor(MS._color.getColor(0.5f));
                            _entryPainter.drawMarker(g2, x + _dimensions._markerCenterShiftX - 1, y - 1, mSize,
                                    null, null, true, false);

                            g2.setColor(MS._color.getColor(1.0f));
                            _entryPainter.drawMarker(g2, x + _dimensions._markerRightShiftX - 1, y - 1, mSize,
                                    null, null, true, false);
                        }
                    }

                    if (MS.areEdgesToBeDrawn())
                    {
                        float ratio = _dimensions._markerScalingFactor * MS._edge.calculateRelativeSize(_GC, _PC) / MS._edge._size;
                        if (MS._edge._legendSize != null)
                            ratio = _dimensions._markerScalingFactor * MS._edge._legendSize; // surpass

                        g2d.setStroke(DrawUtils.constructRescaledStroke(ratio, MS._edge._stroke));
                        g2.setColor(MS._edge._color.getColor(0.0f));
                        if (!drawThree)
                        {
                            _entryPainter.drawMarker(g2, x + _dimensions._markerCenterShiftX - 1, y - 1, mSize,
                                    null, null, false, true);
                        }
                        else
                        {
                            _entryPainter.drawMarker(g2, x + _dimensions._markerLeftShiftX - 1, y - 1, mSize,
                                    null, null, false, true);
                            g2.setColor(MS._edge._color.getColor(0.5f));
                            _entryPainter.drawMarker(g2, x + _dimensions._markerCenterShiftX - 1, y - 1, mSize,
                                    null, null, false, true);
                            g2.setColor(MS._edge._color.getColor(1.0f));
                            _entryPainter.drawMarker(g2, x + _dimensions._markerRightShiftX - 1, y - 1, mSize,
                                    null, null, false, true);
                        }
                    }
                }

                // ====================================================================================================
                y += (_dimensions._entryHeight + _spacing._actualSize);
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

        _offset.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_OFFSET_FIXED));
        _offset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_OFFSET_RELATIVE_MULTIPLIER));
        _offset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_OFFSET_USE_RELATIVE_SIZE));
        _offset.computeActualSize(RV);

        _innerOffset.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_INNER_OFFSET_FIXED));
        _innerOffset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_INNER_OFFSET_RELATIVE_MULTIPLIER));
        _innerOffset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_INNER_OFFSET_USE_RELATIVE_SIZE));
        _innerOffset.computeActualSize(RV);

        _spacing.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_ENTRIES_SPACING_FIXED));
        _spacing.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_ENTRIES_SPACING_RELATIVE_MULTIPLIER));
        _spacing.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_ENTRIES_SPACING_USE_RELATIVE_SIZE));
        _spacing.computeActualSize(RV);

        _columnsSeparator.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_COLUMNS_SEPARATOR_FIXED));
        _columnsSeparator.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.LEGEND_COLUMNS_SEPARATOR_RELATIVE_MULTIPLIER));
        _columnsSeparator.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.LEGEND_COLUMNS_SEPARATOR_USE_RELATIVE_SIZE));
        _columnsSeparator.computeActualSize(RV);

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
        _offset.computeActualSize(RV);
        _innerOffset.computeActualSize(RV);
        _spacing.computeActualSize(RV);
        _columnsSeparator.computeActualSize(RV);
        _entryFont._size.computeActualSize(RV);
        _entryFont.prepareFont();
        _borderStroke = getStroke(_borderWidth);
    }

}
