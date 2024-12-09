package component.grid;

import color.Color;
import component.AbstractSwingComponent;
import component.axis.ticksupdater.FromFixedInterval;
import component.axis.ticksupdater.ITicksDataGetter;
import container.PlotContainer;
import scheme.AbstractScheme;
import scheme.enums.ColorFields;
import scheme.enums.FlagFields;
import scheme.enums.NumberFields;
import scheme.enums.SizeFields;
import space.Range;
import utils.Projection;
import utils.Size;

import java.awt.*;

/**
 * Class responsible for drawing grid lines (plot 2D).
 *
 * @author MTomczyk
 */


public class Grid extends AbstractSwingComponent
{
    /**
     * Auxiliary object for getting ticks location (equivalent to grid lines locations); for vertical lines.
     */
    private ITicksDataGetter _verticalTicksDataGetter;

    /**
     * Auxiliary object for getting ticks location (equivalent to grid lines locations); for vertical lines.
     */
    private ITicksDataGetter _horizontalTicksDataGetter;

    /**
     * Color of the lines to be drawn.
     */
    private Color _linesColor;

    /**
     * Field reference (lines color) used to update data accordingly based on the scheme used.
     */
    private final ColorFields _linesColorField;

    /**
     * Field reference (lines width fixed) used to update data accordingly based on the scheme used.
     */
    private final SizeFields _linesWidthFixedField;

    /**
     * Field reference (lines width relative) used to update data accordingly based on the scheme used.
     */
    private final SizeFields _linesWidthRelativeField;

    /**
     * Field reference indicating whether line width is fixed or relative.
     */
    private final FlagFields _linesWidthUseRelativeField;

    /**
     * Field reference (dash pattern) used to update data accordingly based on the scheme used.
     */
    private final NumberFields _dashPatternField;

    /**
     * Line width.
     */
    private final Size _linesWidth;

    /**
     * Stroke used when drawing lines.
     */
    private Stroke _stroke = null;

    /**
     * Represents the number of breaks. if 0 -> solid line is drawn.
     * If > 0, the lines is equally divided into smaller lines (broken line).
     */
    private int _noBreaks = 0;

    /**
     * Parameterized constructor.
     *
     * @param name                       component name
     * @param PC                         container for relevant fields, shared among different objects (allows easy access to necessary data)
     * @param linesColorField            field reference (lines color) used to accordingly update data based on the used scheme
     * @param linesWidthFixedField       field reference (lines width fixed) used to accordingly update data based on the used scheme
     * @param linesWidthRelativeField    field reference (lines width relative) used to accordingly update data based on the used scheme
     * @param linesWidthUseRelativeField field reference (use relative width) used to accordingly update data based on the used scheme
     * @param dashPatternField           field reference (no. dashes) used to accordingly update data based on the used scheme
     * @param verticalTicksDataGetter    auxiliary object for getting ticks location (equivalent to grid lines locations); for vertical lines
     * @param horizontalTicksDataGetter  auxiliary object for getting ticks location (equivalent to grid lines locations); for horizontal lines
     */
    public Grid(String name,
                PlotContainer PC,
                ColorFields linesColorField,
                SizeFields linesWidthFixedField,
                SizeFields linesWidthRelativeField,
                FlagFields linesWidthUseRelativeField,
                NumberFields dashPatternField,
                ITicksDataGetter verticalTicksDataGetter,
                ITicksDataGetter horizontalTicksDataGetter)
    {
        super(name, PC);
        _linesColorField = linesColorField;
        _linesWidthFixedField = linesWidthFixedField;
        _linesWidthRelativeField = linesWidthRelativeField;
        _linesWidthUseRelativeField = linesWidthUseRelativeField;
        _dashPatternField = dashPatternField;
        _verticalTicksDataGetter = verticalTicksDataGetter;
        _horizontalTicksDataGetter = horizontalTicksDataGetter;
        _linesWidth = new Size();
    }

    /**
     * Supportive method creating the main grid lines.
     *
     * @param PC container for relevant fields that is shared among different objects (allows easy access to necessary data).
     * @return main grid lines object
     */
    public static Grid getMainGrid(PlotContainer PC)
    {
        return new Grid("Main grid", PC,
                ColorFields.GRID_MAIN_LINES,
                SizeFields.GRID_MAIN_LINES_WIDTH_FIXED,
                SizeFields.GRID_MAIN_LINES_WIDTH_RELATIVE_MULTIPLIER,
                FlagFields.GRID_MAIN_LINES_USE_RELATIVE_WIDTH,
                NumberFields.GRID_MAIN_DASH_PATTERN,
                new FromFixedInterval(Range.getNormalRange(), 5),
                new FromFixedInterval(Range.getNormalRange(), 5));
    }

    /**
     * Supportive method creating the aux grid lines.
     *
     * @param PC container for relevant fields that is shared among different objects (allows easy access to necessary data).
     * @return aux grid lines object
     */
    public static Grid getAuxGrid(PlotContainer PC)
    {
        return new Grid("Aux grid", PC,
                ColorFields.GRID_AUX_LINES,
                SizeFields.GRID_AUX_LINES_WIDTH_FIXED,
                SizeFields.GRID_AUX_LINES_WIDTH_RELATIVE_MULTIPLIER,
                FlagFields.GRID_AUX_LINES_USE_RELATIVE_WIDTH,
                NumberFields.GRID_AUX_DASH_PATTERN,
                new FromFixedInterval(Range.getNormalRange(), 9),
                new FromFixedInterval(Range.getNormalRange(), 9));
    }


    /**
     * Draws grid lines.
     *
     * @param g Java AWT graphics context (should be called by the DrawArea object).
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics g2 = g.create();
        ((Graphics2D) g2).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(_linesColor);

        if (_stroke == null) return;

        ((Graphics2D) g2).setStroke(_stroke);
        float[] vertLocs = _verticalTicksDataGetter.getTicksLocations().clone();
        float[] horLocs = _horizontalTicksDataGetter.getTicksLocations().clone();

        int n = (_noBreaks * 2 + 1);
        float[] vertBounds = new float[n + 1];
        float[] horBounds = new float[n + 1];
        float vertDiv = (float) (_primaryDrawingArea.height - 1) / n;
        float horDiv = (float) (_primaryDrawingArea.width - 1) / n;

        for (int i = 0; i < n; i++)
        {
            vertBounds[i] = _translationVector[1] + i * vertDiv;
            horBounds[i] = _translationVector[0] + i * horDiv;
        }

        vertBounds[n] = _translationVector[1] + _primaryDrawingArea.height - 1;
        horBounds[n] = _translationVector[0] + _primaryDrawingArea.width - 1;

        // HORIZONTAL
        for (float horLoc : horLocs)
        {
            if ((Float.compare(horLoc, 0.0f) < 0) || (Float.compare(horLoc, 1.0f) > 0)) continue;
            float y = _translationVector[1] + (1.0f - horLoc) * (_primaryDrawingArea.height - 1);

            for (int i = 0; i < _noBreaks + 1; i++)
            {
                g2.drawLine(Projection.getP(horBounds[2 * i]), Projection.getP(y),
                        Projection.getP(horBounds[2 * i + 1]), Projection.getP(y));
            }
        }
        // VERTICAL
        for (float vertLoc : vertLocs)
        {
            if ((Float.compare(vertLoc, 0.0f) < 0) || (Float.compare(vertLoc, 1.0f) > 0)) continue;
            float x = _translationVector[0] + vertLoc * (_primaryDrawingArea.width - 1);

            for (int i = 0; i < _noBreaks + 1; i++)
            {
                g2.drawLine(Projection.getP(x), Projection.getP(vertBounds[2 * i]),
                        Projection.getP(x), Projection.getP(vertBounds[2 * i + 1]));
            }
        }
        g2.dispose();
    }


    /**
     * Update scheme.
     *
     * @param scheme scheme object
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        _linesColor = scheme.getColors(_surpassedColors, _linesColorField);

        _linesWidth.setFixedSize(scheme.getSizes(_surpassedSizes, _linesWidthFixedField));
        _linesWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, _linesWidthRelativeField));
        _linesWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, _linesWidthUseRelativeField));

        _noBreaks = scheme.getNumbers(_surpassedNumbers, _dashPatternField);
        if (_noBreaks < 0) _noBreaks = 0;
        _stroke = getStroke(_linesWidth, 0);
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
    }

    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    @Override
    public void updateRelativeFields()
    {
        _stroke = getStroke(_linesWidth, 0);
    }

    /**
     * Setter for the auxiliary object for getting ticks locations (equivalent to grid lines locations); for vertical lines.
     *
     * @param verticalTicksDataGetter ticks data getter
     */
    public void setVerticalTicksDataGetter(ITicksDataGetter verticalTicksDataGetter)
    {
        _verticalTicksDataGetter = verticalTicksDataGetter;
    }

    /**
     * Getter for the auxiliary object for getting ticks locations (equivalent to grid lines locations); for vertical lines.
     *
     * @return ticks data getter
     */
    public ITicksDataGetter getVerticalTicksDataGetter()
    {
        return _verticalTicksDataGetter;
    }

    /**
     * Setter for the auxiliary object for getting ticks location (equivalent to grid lines locations); for vertical lines.
     *
     * @param horizontalTicksDataGetter ticks data getter
     */
    public void setHorizontalTicksDataGetter(ITicksDataGetter horizontalTicksDataGetter)
    {
        _horizontalTicksDataGetter = horizontalTicksDataGetter;
    }

    /**
     * Getter for the auxiliary object for getting ticks locations (equivalent to grid lines locations); for horizontal lines.
     *
     * @return ticks data getter
     */
    public ITicksDataGetter getHorizontalTicksDataGetter()
    {
        return _horizontalTicksDataGetter;
    }

}
