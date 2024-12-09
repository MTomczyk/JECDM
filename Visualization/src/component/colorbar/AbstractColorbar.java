package component.colorbar;

import color.Color;
import color.gradient.Gradient;
import component.AbstractSwingComponent;
import component.axis.swing.AbstractAxis;
import component.axis.swing.ColorbarAxis;
import component.axis.ticksupdater.ITicksDataGetter;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import listeners.auxiliary.IDisplayRangesChangedListener;
import scheme.AbstractScheme;
import scheme.enums.*;
import utils.DrawUtils;
import utils.Projection;
import utils.Size;

import java.awt.*;

/**
 * Class representing plot colorbar.
 *
 * @author MTomczyk
 */
public abstract class AbstractColorbar extends AbstractSwingComponent implements IDisplayRangesChangedListener
{
    /**
     * Params container.
     */
    public static class Params extends AbstractSwingComponent.Params
    {
        /**
         * Gradient used.
         */
        public final Gradient _gradient;

        /**
         * If true, axis is used. False = otherwise.
         */
        public boolean _useAxis = true;

        /**
         * Supportive object that is used to automatically update tick labels.
         */
        public ITicksDataGetter _ticksDataGetter;


        /**
         * Parameterized constructor.
         *
         * @param name     component name
         * @param PC       plot container: allows accessing various plot components
         * @param gradient gradient used
         */
        public Params(String name, PlotContainer PC, Gradient gradient)
        {
            super(name, PC);
            _gradient = gradient;
        }

        /**
         * Parameterized constructor.
         *
         * @param name            component name
         * @param PC              plot container: allows accessing various plot components
         * @param gradient        gradient used
         * @param useAxis         if true -> axis is rendered, false = otherwise
         * @param title           colorbar (its axis) title (can be null -> not used)
         * @param ticksDataGetter supportive object that is used to automatically update tick labels
         */
        public Params(String name, PlotContainer PC, Gradient gradient, boolean useAxis, String title, ITicksDataGetter ticksDataGetter)
        {
            super(name, PC);
            _gradient = gradient;
            _useAxis = useAxis;
            _title = title;
            _ticksDataGetter = ticksDataGetter;
        }
    }

    /**
     * Gradient used.
     */
    private final Gradient _gradient;

    /**
     * Axis used along the colorbar. Can be null -> not used.
     */
    private AbstractAxis _axis;

    /**
     * Offset: distance from the plot drawing area.
     */
    private final Size _offset;

    /**
     * Colorbar width;
     */
    private final Size _width;

    /**
     * Bar edge color.
     */
    private Color _edgeColor;

    /**
     * Colorbar edge width.
     */
    private final Size _edgeWidth;

    /**
     * Colorbar edge stroke.
     */
    private Stroke _edgeStroke;

    /**
     * Shrink value: shrinks the length of the colorbar.
     */
    private float _shrink;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractColorbar(Params p)
    {
        super(p._name, p._PC);
        _gradient = p._gradient;

        if (p._useAxis)
        {
            ColorbarAxis.Params pAxis = new ColorbarAxis.Params(null);
            pAxis._title = p._title;
            pAxis._ticksDataGetter = p._ticksDataGetter;
            pAxis._scheme = p._scheme;
            _axis = new ColorbarAxis(pAxis);
            setLayout(null);
            add(_axis);
        }
        else _axis = null;

        _offset = new Size();
        _width = new Size();
        _edgeWidth = new Size();
    }

    /**
     * Setter for plot container.
     *
     * @param PC plot container: allows accessing various plot components
     */
    public void setPlotContainer(PlotContainer PC)
    {
        _PC = PC;
        if (_axis != null) _axis.setPlotContainer(PC);
    }

    /**
     * Can be used to set a global container.
     *
     * @param GC global container: allows accessing various components of the main frame
     */
    @Override
    public void establishGlobalContainer(GlobalContainer GC)
    {
        super.establishGlobalContainer(GC);
        if (_axis != null) _axis.establishGlobalContainer(GC);
    }


    /**
     * Called to update the appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);

        setOpaque(scheme._flags.get(FlagFields.COLORBAR_OPAQUE));
        _align = scheme.getAlignments(_surpassedAlignments, AlignFields.COLORBAR);

        // surpass axis alignment accordingly to colorbar
        if (_axis != null)
        {
            AlignFields currentField = _axis.getFields().getAlign();
            _axis.getSurpassedAlignments().put(currentField, _align);
            _axis.updateScheme(scheme);
        }

        _backgroundColor = scheme.getColors(_surpassedColors, ColorFields.COLORBAR_BACKGROUND);
        _borderColor = scheme.getColors(_surpassedColors, ColorFields.COLORBAR_BORDER);

        _borderWidth.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_BORDER_WIDTH_FIXED));
        _borderWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_BORDER_WIDTH_RELATIVE_MULTIPLIER));
        _borderWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.COLORBAR_BORDER_USE_RELATIVE_WITH));
        _borderStroke = getStroke(_borderWidth);

        _edgeColor = scheme.getColors(_surpassedColors, ColorFields.COLORBAR_EDGE);

        _edgeWidth.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_EDGE_WIDTH_FIXED));
        _edgeWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_EDGE_WIDTH_RELATIVE_MULTIPLIER));
        _edgeWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.COLORBAR_EDGE_USE_RELATIVE_WIDTH));
        _edgeStroke = getStroke(_edgeWidth);

        _offset.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_OFFSET_FIXED));
        _offset.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_OFFSET_RELATIVE_MULTIPLIER));
        _offset.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.COLORBAR_OFFSET_USE_RELATIVE_SIZE));
        _offset.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

        _width.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_WIDTH_FIXED));
        _width.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_WIDTH_RELATIVE_MULTIPLIER));
        _width.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.COLORBAR_WIDTH_USE_RELATIVE_SIZE));
        _width.computeActualSize(_PC.getReferenceValueGetter().getReferenceValue());

        _shrink = scheme.getSizes(_surpassedSizes, SizeFields.COLORBAR_SHRINK);
    }

    /**
     * Updates bounds of the panel.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    @Override
    public void setLocationAndSize(int x, int y, int w, int h)
    {
        super.setLocationAndSize(x, y, w, h);
        if (_axis != null) _axis.setLocationAndSize(0, 0, w, h);
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
        float nx = x;
        float ny = y;
        float nw = w;
        float nh = h;

        updateRelativeFields();

        if ((_align == Align.RIGHT) || (_align == Align.LEFT))
        {
            ny += _shrink * h / 2.0f;
            nh -= h * _shrink;
        }
        else if ((_align == Align.TOP) || (_align == Align.BOTTOM))
        {
            nx += _shrink * w / 2.0f;
            nw -= w * _shrink;
        }

        super.setPrimaryDrawingArea(Projection.getP(nx), Projection.getP(ny), Projection.getP(nw), Projection.getP(nh));

        if (_axis != null)
        {
            if (_align == Align.RIGHT)
            {
                float dx = _translationVector[0] + _offset._actualSize + _width._actualSize;
                float dw = _offset._actualSize + _width._actualSize;
                _axis.setPrimaryDrawingArea(Projection.getP(dx), Projection.getP(ny), Projection.getP(w - dw), Projection.getP(nh));
            }
            else if (_align == Align.LEFT)
            {
                float dx = _offset._actualSize + _width._actualSize;
                _axis.setPrimaryDrawingArea(0, Projection.getP(ny), Projection.getP(w - dx), Projection.getP(nh));
            }
            else if (_align == Align.TOP)
            {
                float dy = _offset._actualSize + _width._actualSize;
                _axis.setPrimaryDrawingArea(Projection.getP(nx), 0, Projection.getP(nw), Projection.getP(h - dy));
            }
            else if (_align == Align.BOTTOM)
            {
                float dy = _offset._actualSize + _width._actualSize + _translationVector[1];
                float dh = _offset._actualSize + _width._actualSize;
                _axis.setPrimaryDrawingArea(Projection.getP(nx), Projection.getP(dy), Projection.getP(nw), Projection.getP(h - dh));
            }
        }
    }


    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    @Override
    public void updateRelativeFields()
    {
        _borderStroke = getStroke(_borderWidth);
        _edgeStroke = getStroke(_edgeWidth);

        float rv = _PC.getReferenceValueGetter().getReferenceValue();
        _offset.computeActualSize(rv);
        _width.computeActualSize(rv);
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

        float x = _translationVector[0] + _offset._actualSize;
        float y = _translationVector[1];
        float w = _width._actualSize;
        float h = _primaryDrawingArea.height;

        if ((_align == Align.RIGHT) || (_align == Align.LEFT))
        {
            if (_align == Align.LEFT)
                x = _translationVector[0] + _primaryDrawingArea.width - _offset._actualSize - _width._actualSize;
            DrawUtils.drawVerticalColorbar(g2, _gradient, x, y, w, h);
        }
        else if ((_align == Align.TOP) || (_align == Align.BOTTOM))
        {
            x = _translationVector[0];
            y = _translationVector[1] + _primaryDrawingArea.height - _offset._actualSize - _width._actualSize;
            w = _primaryDrawingArea.width;
            h = _width._actualSize;

            if (_align == Align.BOTTOM)
                y = _translationVector[1] + _offset._actualSize;
            DrawUtils.drawHorizontalColorbar(g2, _gradient, x, y, w, h);
        }


        if ((_edgeColor != null) && (_edgeStroke != null))
        {
            g2.setColor(_edgeColor);
            ((Graphics2D) g2).setStroke(_edgeStroke);
            DrawUtils.drawBorder(g2, x, y, w, h);
        }

        g2.dispose();
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
        if (_axis != null)
        {
            _axis.displayRangesChanged(drm, report);
        }
    }

    /**
     * Getter for the maintained axis object.
     *
     * @return axis object
     */
    public AbstractAxis getAxis()
    {
        return _axis;
    }

    /**
     * Can be called to clear data.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _edgeColor = null;
        _edgeStroke = null;
        if (_axis != null)
        {
            _axis.dispose();
            _axis = null;
        }
    }
}