package component.axis.swing;

import color.Color;
import component.AbstractSwingComponent;
import component.axis.DirectionVectors;
import component.axis.Tick;
import component.axis.ticksupdater.AbstractTicksDataGetter;
import component.axis.ticksupdater.FromFixedInterval;
import component.axis.ticksupdater.ITicksDataGetter;
import container.Notification;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import listeners.auxiliary.IDisplayRangesChangedListener;
import scheme.AbstractScheme;
import scheme.enums.Align;
import space.Range;
import utils.Font;
import utils.Projection;
import utils.Size;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

/**
 * Class representing plot axis (2D).
 *
 * @author MTomczyk
 */
abstract public class AbstractAxis extends AbstractSwingComponent implements IDisplayRangesChangedListener
{
    /**
     * Axis types (A1 = 1# auxiliary).
     */
    public enum Type
    {
        /**
         * X-axis.
         */
        X,

        /**
         * Y-Axis.
         */
        Y,

        /**
         * Z-axis.
         */
        Z,

        /**
         * Auxiliary axis.
         */
        A1,

        /**
         * Colorbar axis.
         */
        COLORBAR
    }

    /**
     * Params container.
     */
    public static class Params extends AbstractSwingComponent.Params
    {
        /**
         * Axis type (ID).
         */
        public Type _type = Type.X;

        /**
         * A supportive container for field references (scheme attributes).
         */
        public Fields _fields;

        /**
         * Axis main title. Can be null -> not displayed.
         */
        public String _title = null;

        /**
         * Number of dimensions (for rendering, i.e., 2D or 3D).
         */
        public int _dimensions = 2;

        /**
         * Auxiliary object that can be used to automatically update labels.
         */
        public ITicksDataGetter _ticksDataGetter = null;

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
     * Axis type (id).
     */
    protected Type _type;

    /**
     * A supportive container for field references (scheme attributes) for the axis.
     */
    protected Fields _fields;

    /**
     * Color used when drawing the axis.
     */
    protected Color _axColor;

    /**
     * Axis label (can be null -> not displayed).
     */
    protected String _title;

    /**
     * Offset = distance between the axis and the main label.
     */
    protected Size _titleOffset;

    /**
     * Font used when drawing the main label.
     */
    protected Font _titleFont;

    /**
     * Main line stroke
     */
    protected Stroke _lineStroke;

    /**
     * Main line width
     */
    protected final Size _lineWidth;

    /**
     * Supportive object storing auxiliary data related to drawing.
     */
    private DirectionVectors _DV;

    /**
     * Tick objects to be rendered.
     */
    private Tick[] _ticks;

    /**
     * Auxiliary object that can be used to automatically update labels.
     */
    protected ITicksDataGetter _ticksDataGetter;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractAxis(Params p)
    {
        super(p._name, p._PC);

        _type = p._type;
        _fields = p._fields;
        _title = p._title;

        _lineWidth = new Size();
        _titleOffset = new Size();
        _titleFont = new Font();

        _ticksDataGetter = p._ticksDataGetter;

        _DV = new DirectionVectors(p._dimensions);
    }

    /**
     * Translates axis type into a rendering space ID (x -> 0, y -> 1, etc., i.e., rendering space dimension no.)
     *
     * @param type axis type
     * @return rendering space dimension index (null -> no mapping).
     */
    public static Integer getDefaultIDFromType(Type type)
    {
        return switch (type)
        {
            case X -> 0;
            case Y -> 1;
            case Z -> 2;
            default -> null;
        };
    }

    /**
     * Update scheme.
     *
     * @param scheme scheme object
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);

        _align = scheme.getAlignments(_surpassedAlignments, _fields.getAlign());
        _backgroundColor = scheme.getColors(_surpassedColors, _fields.getBackgroundColor());
        _borderColor = scheme.getColors(_surpassedColors, _fields.getBorderColor());
        setOpaque(scheme.getFlags(_surpassedFlags, _fields.getOpaque()));

        _borderColor = scheme.getColors(_surpassedColors, _fields.getBorderColor());
        _borderWidth.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getBorderWidthFixed()));
        _borderWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getBorderWidthRelative()));
        _borderWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getBorderWidthUseRelative()));
        _borderStroke = getStroke(_borderWidth);

        _axColor = scheme.getColors(_surpassedColors, _fields.getAxisColor());

        _lineWidth.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getMainLineWidthFixed()));
        _lineWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getMainLineWidthRelative()));
        _lineWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getMainLineWidthUseRelative()));
        _lineWidth.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());
        _lineStroke = getStroke(_lineWidth);

        _titleFont._fontName = scheme.getFonts(_surpassedFonts, _fields.getTitleFontName());
        _titleFont._color = scheme.getColors(_surpassedColors, _fields.getTitleFontColor());
        _titleFont._size.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getTitleFontSizeFixed()));
        _titleFont._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getTitleFontSizeRelative()));
        _titleFont._size.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getTitleFontSizeUseRelative()));
        _titleFont._size.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

        _titleOffset.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getTitleOffsetFixed()));
        _titleOffset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getTitleOffsetRelative()));
        _titleOffset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getTitleOffsetUseRelative()));
        _titleOffset.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

        {
            int noTicks = _ticksDataGetter.getNoTicks();
            if ((_ticks == null) || (_ticks.length != noTicks))
            {
                _ticks = new Tick[noTicks];
                for (int i = 0; i < noTicks; i++) _ticks[i] = new Tick();
                _DV.instantiateTickArrays(noTicks);
            }

            Size tickLineWidth = new Size();
            tickLineWidth.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getTickLineWidthFixed()));
            tickLineWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getTickLineWidthRelative()));
            tickLineWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getTickLineWidthUseRelative()));

            Stroke tickLineStroke = getStroke(tickLineWidth);
            Size tickSize = new Size();
            tickSize.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getTicksSizeFixed()));
            tickSize.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getTicksSizeRelative()));
            tickSize.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getTickSizeUseRelative()));
            tickSize.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

            Font tickLabelFont = new Font();
            tickLabelFont._fontName = scheme.getFonts(_surpassedFonts, _fields.getTickLabelFontName());
            tickLabelFont._color = scheme.getColors(_surpassedColors, _fields.getTickLabelFontColor());
            tickLabelFont._size.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getTickLabelFontSizeFixed()));
            tickLabelFont._size.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getTickLabelFontSizeRelative()));
            tickLabelFont._size.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getTickLabelFontSizeUseRelative()));
            tickLabelFont._size.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

            Size tickLabelOffset = new Size();
            tickLabelOffset.setFixedSize(scheme.getSizes(_surpassedSizes, _fields.getTickLabelOffsetFixed()));
            tickLabelOffset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _fields.getTickLabelOffsetRelative()));
            tickLabelOffset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _fields.getTickLabelOffsetUseRelative()));
            tickLabelOffset.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

            for (int i = 0; i < noTicks; i++)
                _ticks[i].setAll(i, tickLabelFont, _axColor, tickLineStroke, tickLineWidth, tickSize, tickLabelOffset);
        }
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
        updateRelativeFields();
        instantiateDirectionVectors();
    }


    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    @Override
    public void updateRelativeFields()
    {
        float reference = _PC.getReferenceValueGetter().getReferenceValue();
        _lineStroke = getStroke(_lineWidth);

        if (_ticks != null)
            for (int i = 0; i < _ticksDataGetter.getNoTicks(); i++)
            {
                _ticks[i]._lineStroke = getStroke(_ticks[i]._lineWidth);
                _ticks[i]._size.computeActualSize(reference);
                _ticks[i]._labelOffset.computeActualSize(reference);
                _ticks[i]._lineWidth.computeActualSize(reference);
                _ticks[i]._labelFont._size.computeActualSize(reference);
                _ticks[i]._labelFont.prepareFont();
            }

        _titleOffset.computeActualSize(reference);
        _titleFont._size.computeActualSize(reference);
        _titleFont.prepareFont();
    }

    /**
     * Instantiates the auxiliary projection data.
     */
    protected void instantiateDirectionVectors()
    {

        _DV._s[0] = _translationVector[0] + _primaryDrawingArea.width - 1.0f;
        _DV._s[1] = _translationVector[1] + _primaryDrawingArea.height - 1.0f;
        _DV._e[0] = _translationVector[0] + _primaryDrawingArea.width - 1.0f;
        _DV._e[1] = _translationVector[1];

        float[] locs = _ticksDataGetter.getTicksLocations().clone();
        int noTicks = _ticksDataGetter.getNoTicks();

        for (int i = 0; i < noTicks; i++)
        {
            _ticks[i]._tickDisabled = i >= locs.length;
            if ((i < locs.length) && (!AbstractTicksDataGetter.isTickLockNormal(locs[i])))
                _ticks[i]._tickDisabled = true;
        }

        // left
        for (int i = 0; i < Math.min(noTicks, locs.length); i++)
        {
            if (_ticks[i]._tickDisabled) continue;
            _DV._tp[i][0] = 0.0f;
            _DV._tp[i][1] = -(_primaryDrawingArea.height - 1.0f) * locs[i];
            _DV._dt[i][0] = -_ticks[i]._size._actualSize;
            _DV._dt[i][1] = 0.0f;
            _DV._dtl[i][0] = -_ticks[i]._labelOffset._actualSize;
            _DV._dtl[i][1] = 0.0f;
        }

        if (_align == Align.TOP)
        {
            _DV._s[0] = _translationVector[0];
            _DV._e[1] = _translationVector[1] + _primaryDrawingArea.height - 1.0f;

            for (int i = 0; i < Math.min(noTicks, locs.length); i++)
            {
                if (_ticks[i]._tickDisabled) continue;
                _DV._tp[i][0] = (_primaryDrawingArea.width - 1.0f) * locs[i];
                _DV._tp[i][1] = 0.0f;
                _DV._dt[i][0] = 0.0f;
                _DV._dt[i][1] = -_ticks[i]._size._actualSize;
                _DV._dtl[i][0] = 0.0f;
                _DV._dtl[i][1] = -_ticks[i]._labelOffset._actualSize;
            }

        }
        else if (_align == Align.RIGHT)
        {
            _DV._s[0] = _translationVector[0];
            _DV._e[0] = _translationVector[0];

            for (int i = 0; i < Math.min(noTicks, locs.length); i++)
            {
                if (_ticks[i]._tickDisabled) continue;
                _DV._dt[i][0] = _ticks[i]._size._actualSize;
                _DV._dt[i][1] = 0.0f;
                _DV._dtl[i][0] = _ticks[i]._labelOffset._actualSize;
                _DV._dtl[i][1] = 0.0f;
            }
        }
        else if (_align == Align.BOTTOM)
        {
            _DV._s[0] = _translationVector[0];
            _DV._s[1] = _translationVector[1];


            for (int i = 0; i < Math.min(noTicks, locs.length); i++)
            {
                if (_ticks[i]._tickDisabled) continue;
                _DV._tp[i][0] = (_primaryDrawingArea.width - 1.0f) * locs[i];
                _DV._tp[i][1] = 0.0f;
                _DV._dt[i][0] = 0.0f;
                _DV._dt[i][1] = _ticks[i]._size._actualSize;
                _DV._dtl[i][0] = 0.0f;
                _DV._dtl[i][1] = _ticks[i]._labelOffset._actualSize;
            }
        }
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

        Graphics g2 = g.create();
        ((Graphics2D) g2).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(_axColor);
        drawMainLine(g2);
        for (Tick t : _ticks)
        {
            t.drawTick(g2, _DV);
            t.drawTickLabels(g2, _DV, _align);
        }

        drawTitle(g2);
        g2.dispose();
    }

    /**
     * Auxiliary method drawing title.
     *
     * @param g Java AWT Graphics context
     */
    protected void drawTitle(Graphics g)
    {
        if (_title != null)
        {
            g.setFont(_titleFont._font);
            g.setColor(_titleFont._color);
            Graphics2D g2d = (Graphics2D) g;

            Rectangle2D dRef = Font.getReferenceTextCorrectDimensions(g2d);
            Rectangle2D d = Font.getCorrectDimensions(g2d, _title);

            if (_align == Align.LEFT)
            {
                float x = _DV._s[0] - _titleOffset._actualSize;
                float y = _DV._s[1] - _primaryDrawingArea.height / 2.0f + (float) d.getWidth() / 2.0f + (float) d.getMinX() / 2.0f;
                Font.drawRotatedString(g2d, _title, x, y, (float) (-Math.PI / 2.0f));
            }
            else if (_align == Align.RIGHT)
            {
                float x = _DV._s[0] + _titleOffset._actualSize;
                float y = _DV._s[1] - _primaryDrawingArea.height / 2.0f - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
                Font.drawRotatedString(g2d, _title, x, y, (float) (Math.PI / 2.0f));
            }
            else if (_align == Align.BOTTOM)
            {
                float x = _DV._s[0] + _primaryDrawingArea.width / 2.0f - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
                float y = _DV._s[1] + _titleOffset._actualSize + (float) dRef.getHeight();
                g.drawString(_title, Projection.getP(x), Projection.getP(y));
            }
            else if (_align == Align.TOP)
            {
                float x = _DV._s[0] + _primaryDrawingArea.width / 2.0f - (float) d.getWidth() / 2.0f - (float) d.getMinX() / 2.0f;
                float y = _DV._s[1] - _titleOffset._actualSize;
                g.drawString(_title, Projection.getP(x), Projection.getP(y));
            }
        }
    }

    /**
     * Auxiliary method drawing main line.
     *
     * @param g Java AWT Graphics context
     */
    protected void drawMainLine(Graphics g)
    {
        if (_lineStroke != null)
        {
            ((Graphics2D) g).setStroke(_lineStroke);
            g.drawLine(Projection.getP(_DV._s[0]), Projection.getP(_DV._s[1]),
                    Projection.getP(_DV._e[0]), Projection.getP(_DV._e[1]));
        }
    }


    /**
     * Method notifying on changes in display ranges.
     * Updates axis tick labels.
     *
     * @param drm    display ranges manager
     * @param report report on the last update of display ranges
     */
    @Override
    public void displayRangesChanged(DisplayRangesManager drm, DisplayRangesManager.Report report)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: display ranges changed method called");
        if (_ticksDataGetter == null) return;
        String[] l = _ticksDataGetter.getLabels();
        for (int i = 0; i < Math.min(_ticks.length, l.length); i++)
        {
            if (_ticks[i] == null) continue;
            if (l[i] == null) continue;
            _ticks[i]._label = l[i];
        }
    }

    /**
     * Setter for tick labels. If the input entry is null, the entry is skipped and the method proceeds.
     * Thus, the method can be used to update only some specific ticks.
     *
     * @param labels tick labels (explicit string set)
     */
    public void setTickLabels(String[] labels)
    {
        if (_ticks == null) return;
        if (labels == null) return;
        _ticksDataGetter.setPredefinedTickLabels(labels);
        displayRangesChanged(_PC.getDisplayRangesManager(), null);
    }

    /**
     * Constructs tick labels from provided double range. All values are linearly interpolated.
     *
     * @param minValue left (smaller) value
     * @param maxValue right (greater) value
     * @param noTicks  number of tick labels to generate
     * @param format   number format used when translating doubles into string; can be null -> not used
     */
    public void setTickLabels(double minValue, double maxValue, int noTicks, NumberFormat format)
    {
        FromFixedInterval FFI = new FromFixedInterval(new Range(minValue, maxValue), noTicks, format);
        setTickLabels(FFI.getLabels());
    }

    /**
     * Setter for the plot container.
     *
     * @param PC plot container
     */
    public void setPlotContainer(PlotContainer PC)
    {
        _PC = PC;
    }

    /**
     * Setter for the labels updater (auxiliary object that can be used to automatically update labels).
     *
     * @param ticksDataGetter ticks data getter
     */
    public void setTicksDataGetter(ITicksDataGetter ticksDataGetter)
    {
        _ticksDataGetter = ticksDataGetter;
    }

    /**
     * Getter for the ticks data getter (auxiliary object that can be used to automatically update labels).
     *
     * @return ticks data getter
     */
    public ITicksDataGetter getTicksDataGetter()
    {
        return _ticksDataGetter;
    }

    /**
     * Getter for the supportive container for field references (scheme attributes) for the axis.
     *
     * @return fields
     */
    public Fields getFields()
    {
        return _fields;
    }

    /**
     * can be called to clear memory.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _DV.dispose();
        _DV = null;
        _fields = null;
        _ticksDataGetter = null;
        _titleFont.dispose();
        _titleFont = null;
        if (_ticks != null) for (Tick t : _ticks) t.dispose();
        _ticks = null;
        _title = null;
        _titleOffset = null;
        _lineStroke = null;
        _type = null;
        _axColor = null;
    }
}
