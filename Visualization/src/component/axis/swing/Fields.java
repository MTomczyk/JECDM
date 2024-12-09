package component.axis.swing;

import scheme.enums.*;

/**
 * A supportive container for field references (scheme attributes).
 *
 * @author MTomczyk
 */

public class Fields
{
    /**
     * Reference to the field indicating the axis alignment.
     */
    private AlignFields _align;

    /**
     * Reference to the field indicating the axis color.
     */
    private ColorFields _axisColor = null;

    /**
     * Reference to the field indicating the fixed size of ticks.
     */
    private SizeFields _ticksSizeFixed = null;

    /**
     * Reference to the field indicating the font name of tick labels.
     */
    private FontFields _tickLabelFontName = null;

    /**
     * Reference to the field indicating the relative font size of tick labels.
     */
    private SizeFields _tickLabelFontSizeRelative = null;

    /**
     * Reference to the field indicating the font color of tick labels.
     */
    private ColorFields _tickLabelFontColor = null;

    /**
     * Reference to the field indicating the font name of axis title.
     */
    private FontFields _titleFontName = null;

    /**
     * Reference to the field indicating the relative font size of the title.
     */
    private SizeFields _titleFontSizeRelative = null;


    /**
     * Reference to the field indicating the font color of axis title.
     */
    private ColorFields _titleFontColor = null;

    /**
     * Reference to the field indicating the fixed offset of tick labels.
     */
    private SizeFields _tickLabelOffsetFixed = null;

    /**
     * Reference to the field indicating the fixed offset of the title.
     */
    private SizeFields _titleOffsetFixed = null;

    /**
     * Reference to the field indicating the axis background color.
     */
    private ColorFields _backgroundColor = null;

    /**
     * Reference to the field indicating the axis border color.
     */
    private ColorFields _borderColor = null;

    /**
     * Reference to the field indicating the relative size of ticks.
     */
    private SizeFields _ticksSizeRelative = null;

    /**
     * Reference to the field indicating the width of the main line (fixed).
     */
    private SizeFields _mainLineWidthFixed = null;

    /**
     * Reference to the field indicating the width of the main line (relative).
     */
    private SizeFields _mainLineWidthRelative = null;

    /**
     * Reference to the field indicating the whether to consider main line width fixed or relative.
     */
    private FlagFields _mainLineWidthUseRelative = null;

    /**
     * Reference to the field indicating the width of tick lines (fixed).
     */
    private SizeFields _tickLineWidthFixed = null;

    /**
     * Reference to the field indicating the width of tick lines (relative).
     */
    private SizeFields _tickLineWidthRelative = null;

    /**
     * Reference to the field indicating the whether to consider tick line width fixed or relative.
     */
    private FlagFields _tickLineWidthUseRelative = null;

    /**
     * Reference to the field indicating the axis border width (fixed).
     */
    private SizeFields _borderWidthFixed = null;

    /**
     * Reference to the field indicating the axis border width (relative).
     */
    private SizeFields _borderWidthRelative = null;

    /**
     * Reference to the field indicating the whether to consider border width relative
     */
    private FlagFields _borderWidthUseRelative = null;

    /**
     * Reference to the field indicating the fixed font size of tick labels.
     */
    private SizeFields _tickLabelFontSizeFixed = null;

    /**
     * Reference to the field indicating the relative offset of tick labels.
     */
    private SizeFields _tickLabelOffsetRelative = null;

    /**
     * Reference to the field indicating the fixed font size of the title.
     */
    private SizeFields _titleFontSizeFixed = null;

    /**
     * Reference to the field indicating the relative offset of the tile.
     */
    private SizeFields _titleOffsetRelative = null;

    /**
     * Reference to the field indicating the whether to consider tick size relative.
     */
    private FlagFields _tickSizeUseRelative;

    /**
     * Reference to the field indicating the whether to consider tick label size relative.
     */
    private FlagFields _tickLabelFontSizeUseRelative;

    /**
     * Reference to the field indicating the whether to consider tick label offset relative.
     */
    private FlagFields _tickLabelOffsetUseRelative;

    /**
     * Reference to the field indicating the whether to consider title font size relative.
     */
    private FlagFields _titleFontSizeUseRelative;

    /**
     * Reference to the field indicating the whether to consider title offset relative.
     */
    private FlagFields _titleOffsetUseRelative;

    /**
     * Reference to the field indicating whether the background is opaque.
     */
    private FlagFields _opaque;

    /**
     * Constructs fields container representing the X-axis.
     *
     * @return fields container
     */
    public static Fields getFieldsForXAxis()
    {
        Fields f = new Fields();
        f._axisColor = ColorFields.AXIS_X;
        f._backgroundColor = ColorFields.AXIS_X_BACKGROUND;
        f._borderColor = ColorFields.AXIS_X_BORDER;
        f._tickLabelFontColor = ColorFields.AXIS_X_TICK_LABEL_FONT;
        f._titleFontColor = ColorFields.AXIS_X_TITLE_FONT;
        f._titleFontName = FontFields.AXIS_X_TITLE;
        f._tickLabelFontName = FontFields.AXIS_X_TICK_LABEL;
        f._ticksSizeFixed = SizeFields.AXIS_X_TICK_SIZE_FIXED;
        f._ticksSizeRelative = SizeFields.AXIS_X_TICK_SIZE_RELATIVE_MULTIPLIER;
        f._mainLineWidthFixed = SizeFields.AXIS_X_MAIN_LINE_WIDTH_FIXED;
        f._mainLineWidthRelative = SizeFields.AXIS_X_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._mainLineWidthUseRelative = FlagFields.AXIS_X_MAIN_LINE_USE_RELATIVE_WIDTH;
        f._tickLineWidthFixed = SizeFields.AXIS_X_TICK_LINE_WIDTH_FIXED;
        f._tickLineWidthRelative = SizeFields.AXIS_X_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._tickLineWidthUseRelative = FlagFields.AXIS_X_TICK_LINE_USE_RELATIVE_WIDTH;
        f._borderWidthFixed = SizeFields.AXIS_X_BORDER_WIDTH_FIXED;
        f._borderWidthRelative = SizeFields.AXIS_X_BORDER_WIDTH_RELATIVE_MULTIPLIER;
        f._borderWidthUseRelative = FlagFields.AXIS_X_BORDER_USE_RELATIVE_WIDTH;
        f._tickLabelFontSizeFixed = SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_FIXED;
        f._tickLabelFontSizeRelative = SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._tickLabelOffsetFixed = SizeFields.AXIS_X_TICK_LABEL_OFFSET_FIXED;
        f._tickLabelOffsetRelative = SizeFields.AXIS_X_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER;
        f._titleFontSizeFixed = SizeFields.AXIS_X_TITLE_FONT_SIZE_FIXED;
        f._titleFontSizeRelative = SizeFields.AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._titleOffsetFixed = SizeFields.AXIS_X_TITLE_OFFSET_FIXED;
        f._titleOffsetRelative = SizeFields.AXIS_X_TITLE_OFFSET_RELATIVE_MULTIPLIER;
        f._align = AlignFields.AXIS_X;
        f._tickSizeUseRelative = FlagFields.AXIS_X_TICK_USE_RELATIVE_SIZE;
        f._tickLabelFontSizeUseRelative = FlagFields.AXIS_X_TICK_LABEL_FONT_USE_RELATIVE_SIZE;
        f._tickLabelOffsetUseRelative = FlagFields.AXIS_X_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE;
        f._titleFontSizeUseRelative = FlagFields.AXIS_X_TITLE_FONT_USE_RELATIVE_SIZE;
        f._titleOffsetUseRelative = FlagFields.AXIS_X_TITLE_OFFSET_USE_RELATIVE_SIZE;
        f._opaque = FlagFields.AXIS_X_OPAQUE;
        return f;
    }

    /**
     * Constructs fields container representing the Y-axis.
     *
     * @return fields container
     */
    public static Fields getFieldsForYAxis()
    {
        Fields f = new Fields();
        f._axisColor = ColorFields.AXIS_Y;
        f._backgroundColor = ColorFields.AXIS_Y_BACKGROUND;
        f._borderColor = ColorFields.AXIS_Y_BORDER;
        f._tickLabelFontColor = ColorFields.AXIS_Y_TICK_LABEL_FONT;
        f._titleFontColor = ColorFields.AXIS_Y_TITLE_FONT;
        f._titleFontName = FontFields.AXIS_Y_TITLE;
        f._tickLabelFontName = FontFields.AXIS_Y_TICK_LABEL;
        f._ticksSizeFixed = SizeFields.AXIS_Y_TICK_SIZE_FIXED;
        f._ticksSizeRelative = SizeFields.AXIS_Y_TICK_SIZE_RELATIVE_MULTIPLIER;
        f._mainLineWidthFixed = SizeFields.AXIS_Y_MAIN_LINE_WIDTH_FIXED;
        f._mainLineWidthRelative = SizeFields.AXIS_Y_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._mainLineWidthUseRelative = FlagFields.AXIS_Y_MAIN_LINE_USE_RELATIVE_WIDTH;
        f._tickLineWidthFixed = SizeFields.AXIS_Y_TICK_LINE_WIDTH_FIXED;
        f._tickLineWidthRelative = SizeFields.AXIS_Y_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._tickLineWidthUseRelative = FlagFields.AXIS_Y_TICK_LINE_USE_RELATIVE_WIDTH;
        f._borderWidthFixed = SizeFields.AXIS_Y_BORDER_WIDTH_FIXED;
        f._borderWidthRelative = SizeFields.AXIS_Y_BORDER_WIDTH_RELATIVE_MULTIPLIER;
        f._borderWidthUseRelative = FlagFields.AXIS_Y_BORDER_USE_RELATIVE_WIDTH;
        f._tickLabelFontSizeFixed = SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_FIXED;
        f._tickLabelFontSizeRelative = SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._tickLabelOffsetFixed = SizeFields.AXIS_Y_TICK_LABEL_OFFSET_FIXED;
        f._tickLabelOffsetRelative = SizeFields.AXIS_Y_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER;
        f._titleFontSizeFixed = SizeFields.AXIS_Y_TITLE_FONT_SIZE_FIXED;
        f._titleFontSizeRelative = SizeFields.AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._titleOffsetFixed = SizeFields.AXIS_Y_TITLE_OFFSET_FIXED;
        f._titleOffsetRelative = SizeFields.AXIS_Y_TITLE_OFFSET_RELATIVE_MULTIPLIER;
        f._align = AlignFields.AXIS_Y;
        f._tickSizeUseRelative = FlagFields.AXIS_Y_TICK_USE_RELATIVE_SIZE;
        f._tickLabelFontSizeUseRelative = FlagFields.AXIS_Y_TICK_LABEL_FONT_USE_RELATIVE_SIZE;
        f._tickLabelOffsetUseRelative = FlagFields.AXIS_Y_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE;
        f._titleFontSizeUseRelative = FlagFields.AXIS_Y_TITLE_FONT_USE_RELATIVE_SIZE;
        f._titleOffsetUseRelative = FlagFields.AXIS_Y_TITLE_OFFSET_USE_RELATIVE_SIZE;
        f._opaque = FlagFields.AXIS_Y_OPAQUE;
        return f;
    }

    /**
     * Constructs fields container representing the Z-axis.
     *
     * @return fields container
     */
    public static Fields getFieldsForZAxis()
    {
        Fields f = new Fields();
        f._axisColor = ColorFields.AXIS_Z;
        f._backgroundColor = ColorFields.AXIS_Z_BACKGROUND;
        f._borderColor = ColorFields.AXIS_Z_BORDER;
        f._tickLabelFontColor = ColorFields.AXIS_Z_TICK_LABEL_FONT;
        f._titleFontColor = ColorFields.AXIS_Z_TITLE_FONT;
        f._titleFontName = FontFields.AXIS_Z_TITLE;
        f._tickLabelFontName = FontFields.AXIS_Z_TICK_LABEL;
        f._ticksSizeFixed = SizeFields.AXIS_Z_TICK_SIZE_FIXED;
        f._ticksSizeRelative = SizeFields.AXIS_Z_TICK_SIZE_RELATIVE_MULTIPLIER;
        f._mainLineWidthFixed = SizeFields.AXIS_Z_MAIN_LINE_WIDTH_FIXED;
        f._mainLineWidthRelative = SizeFields.AXIS_Z_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._mainLineWidthUseRelative = FlagFields.AXIS_Z_MAIN_LINE_USE_RELATIVE_WIDTH;
        f._tickLineWidthFixed = SizeFields.AXIS_Z_TICK_LINE_WIDTH_FIXED;
        f._tickLineWidthRelative = SizeFields.AXIS_Z_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._tickLineWidthUseRelative = FlagFields.AXIS_Z_TICK_LINE_USE_RELATIVE_WIDTH;
        f._borderWidthFixed = SizeFields.AXIS_Z_BORDER_WIDTH_FIXED;
        f._borderWidthRelative = SizeFields.AXIS_Z_BORDER_WIDTH_RELATIVE_MULTIPLIER;
        f._borderWidthUseRelative = FlagFields.AXIS_Z_BORDER_USE_RELATIVE_WIDTH;
        f._tickLabelFontSizeFixed = SizeFields.AXIS_Z_TICK_LABEL_FONT_SIZE_FIXED;
        f._tickLabelFontSizeRelative = SizeFields.AXIS_Z_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._tickLabelOffsetFixed = SizeFields.AXIS_Z_TICK_LABEL_OFFSET_FIXED;
        f._tickLabelOffsetRelative = SizeFields.AXIS_Z_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER;
        f._titleFontSizeFixed = SizeFields.AXIS_Z_TITLE_FONT_SIZE_FIXED;
        f._titleFontSizeRelative = SizeFields.AXIS_Z_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._titleOffsetFixed = SizeFields.AXIS_Z_TITLE_OFFSET_FIXED;
        f._titleOffsetRelative = SizeFields.AXIS_Z_TITLE_OFFSET_RELATIVE_MULTIPLIER;
        f._align = AlignFields.AXIS_Z;
        f._tickSizeUseRelative = FlagFields.AXIS_Z_TICK_USE_RELATIVE_SIZE;
        f._tickLabelFontSizeUseRelative = FlagFields.AXIS_Z_TICK_LABEL_FONT_USE_RELATIVE_SIZE;
        f._tickLabelOffsetUseRelative = FlagFields.AXIS_Z_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE;
        f._titleFontSizeUseRelative = FlagFields.AXIS_Z_TITLE_FONT_USE_RELATIVE_SIZE;
        f._titleOffsetUseRelative = FlagFields.AXIS_Z_TITLE_OFFSET_USE_RELATIVE_SIZE;
        f._opaque = FlagFields.AXIS_Z_OPAQUE;
        return f;
    }


    /**
     * Constructs fields container representing the A1-axis.
     *
     * @return fields container
     */
    public static Fields getFieldsForA1Axis()
    {
        Fields f = new Fields();
        f._axisColor = ColorFields.AXIS_A1_COLOR;
        f._backgroundColor = ColorFields.AXIS_A1_BACKGROUND;
        f._borderColor = ColorFields.AXIS_A1_BORDER;
        f._tickLabelFontColor = ColorFields.AXIS_A1_TICK_LABEL_FONT;
        f._titleFontColor = ColorFields.AXIS_A1_TITLE_FONT;
        f._titleFontName = FontFields.AXIS_A1_TITLE;
        f._tickLabelFontName = FontFields.AXIS_A1_TICK_LABEL;
        f._ticksSizeFixed = SizeFields.AXIS_A1_TICK_SIZE_FIXED;
        f._ticksSizeRelative = SizeFields.AXIS_A1_TICK_SIZE_RELATIVE_MULTIPLIER;
        f._mainLineWidthFixed = SizeFields.AXIS_A1_MAIN_LINE_WIDTH_FIXED;
        f._mainLineWidthRelative = SizeFields.AXIS_A1_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._mainLineWidthUseRelative = FlagFields.AXIS_A1_MAIN_LINE_USE_RELATIVE_WIDTH;
        f._tickLineWidthFixed = SizeFields.AXIS_A1_TICK_LINE_WIDTH_FIXED;
        f._tickLineWidthRelative = SizeFields.AXIS_A1_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._tickLineWidthUseRelative = FlagFields.AXIS_A1_TICK_LINE_USE_RELATIVE_WIDTH;
        f._borderWidthFixed = SizeFields.AXIS_A1_BORDER_WIDTH_FIXED;
        f._borderWidthRelative = SizeFields.AXIS_A1_BORDER_WIDTH_RELATIVE_MULTIPLIER;
        f._borderWidthUseRelative = FlagFields.AXIS_A1_BORDER_USE_RELATIVE_WIDTH;
        f._tickLabelFontSizeFixed = SizeFields.AXIS_A1_TICK_LABEL_FONT_SIZE_FIXED;
        f._tickLabelFontSizeRelative = SizeFields.AXIS_A1_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._tickLabelOffsetFixed = SizeFields.AXIS_A1_TICK_LABEL_OFFSET_FIXED;
        f._tickLabelOffsetRelative = SizeFields.AXIS_A1_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER;
        f._titleFontSizeFixed = SizeFields.AXIS_A1_TITLE_FONT_SIZE_FIXED;
        f._titleFontSizeRelative = SizeFields.AXIS_A1_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._titleOffsetFixed = SizeFields.AXIS_A1_TITLE_OFFSET_FIXED;
        f._titleOffsetRelative = SizeFields.AXIS_A1_TITLE_OFFSET_RELATIVE_MULTIPLIER;
        f._align = AlignFields.AXIS_A1;
        f._tickSizeUseRelative = FlagFields.AXIS_A1_TICK_USE_RELATIVE_SIZE;
        f._tickLabelFontSizeUseRelative = FlagFields.AXIS_A1_TICK_LABEL_FONT_USE_RELATIVE_SIZE;
        f._tickLabelOffsetUseRelative = FlagFields.AXIS_A1_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE;
        f._titleFontSizeUseRelative = FlagFields.AXIS_A1_TITLE_FONT_USE_RELATIVE_SIZE;
        f._titleOffsetUseRelative = FlagFields.AXIS_A1_TITLE_OFFSET_USE_RELATIVE_SIZE;
        f._opaque = FlagFields.AXIS_A1_OPAQUE;
        return f;
    }


    /**
     * Constructs fields container representing the colorbar-axis.
     *
     * @return fields container
     */
    public static Fields getFieldsForColorbarAxis()
    {
        Fields f = new Fields();
        f._axisColor = ColorFields.AXIS_COLORBAR_COLOR;
        f._backgroundColor = ColorFields.AXIS_COLORBAR_BACKGROUND;
        f._borderColor = ColorFields.AXIS_COLORBAR_BORDER;
        f._tickLabelFontColor = ColorFields.AXIS_COLORBAR_TICK_LABEL_FONT;
        f._titleFontColor = ColorFields.AXIS_COLORBAR_TITLE_FONT;
        f._titleFontName = FontFields.AXIS_COLORBAR_TITLE;
        f._tickLabelFontName = FontFields.AXIS_COLORBAR_TICK_LABEL;
        f._ticksSizeFixed = SizeFields.AXIS_COLORBAR_TICK_SIZE_FIXED;
        f._ticksSizeRelative = SizeFields.AXIS_COLORBAR_TICK_SIZE_RELATIVE_MULTIPLIER;
        f._mainLineWidthFixed = SizeFields.AXIS_COLORBAR_MAIN_LINE_WIDTH_FIXED;
        f._mainLineWidthRelative = SizeFields.AXIS_COLORBAR_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._mainLineWidthUseRelative = FlagFields.AXIS_COLORBAR_MAIN_LINE_USE_RELATIVE_WIDTH;
        f._tickLineWidthFixed = SizeFields.AXIS_COLORBAR_TICK_LINE_WIDTH_FIXED;
        f._tickLineWidthRelative = SizeFields.AXIS_COLORBAR_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER;
        f._tickLineWidthUseRelative = FlagFields.AXIS_COLORBAR_TICK_LINE_USE_RELATIVE_WIDTH;
        f._borderWidthFixed = SizeFields.AXIS_COLORBAR_BORDER_WIDTH_FIXED;
        f._borderWidthRelative = SizeFields.AXIS_COLORBAR_BORDER_WIDTH_RELATIVE_MULTIPLIER;
        f._borderWidthUseRelative = FlagFields.AXIS_COLORBAR_BORDER_USE_RELATIVE_WIDTH;
        f._tickLabelFontSizeFixed = SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_FIXED;
        f._tickLabelFontSizeRelative = SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._tickLabelOffsetFixed = SizeFields.AXIS_COLORBAR_TICK_LABEL_OFFSET_FIXED;
        f._tickLabelOffsetRelative = SizeFields.AXIS_COLORBAR_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER;
        f._titleFontSizeFixed = SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_FIXED;
        f._titleFontSizeRelative = SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER;
        f._titleOffsetFixed = SizeFields.AXIS_COLORBAR_TITLE_OFFSET_FIXED;
        f._titleOffsetRelative = SizeFields.AXIS_COLORBAR_TITLE_OFFSET_RELATIVE_MULTIPLIER;
        f._align = AlignFields.AXIS_COLORBAR;
        f._tickSizeUseRelative = FlagFields.AXIS_COLORBAR_TICK_USE_RELATIVE_SIZE;
        f._tickLabelFontSizeUseRelative = FlagFields.AXIS_COLORBAR_TICK_LABEL_FONT_USE_RELATIVE_SIZE;
        f._tickLabelOffsetUseRelative = FlagFields.AXIS_COLORBAR_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE;
        f._titleFontSizeUseRelative = FlagFields.AXIS_COLORBAR_TITLE_FONT_USE_RELATIVE_SIZE;
        f._titleOffsetUseRelative = FlagFields.AXIS_COLORBAR_TITLE_OFFSET_USE_RELATIVE_SIZE;
        f._opaque = FlagFields.AXIS_COLORBAR_OPAQUE;
        return f;
    }

    /**
     * Getter for the field indicating the axis alignment.
     *
     * @return field indicating the axis alignment
     */
    public AlignFields getAlign()
    {
        return _align;
    }

    /**
     * Getter for the field indicating the axis color.
     *
     * @return field indicating the axis color
     */
    public ColorFields getAxisColor()
    {
        return _axisColor;
    }

    /**
     * Getter for the field indicating the fixed size of ticks.
     *
     * @return field indicating the fixed size of ticks
     */
    public SizeFields getTicksSizeFixed()
    {
        return _ticksSizeFixed;
    }

    /**
     * Getter for the field indicating the font name of tick labels.
     *
     * @return field indicating the font name of tick labels
     */
    public FontFields getTickLabelFontName()
    {
        return _tickLabelFontName;
    }

    /**
     * Getter for the field indicating the relative font size of tick labels.
     *
     * @return field indicating the relative font size of tick labels
     */
    public SizeFields getTickLabelFontSizeRelative()
    {
        return _tickLabelFontSizeRelative;
    }

    /**
     * Getter for field indicating the font color of tick labels.
     *
     * @return field indicating the font color of tick labels
     */
    public ColorFields getTickLabelFontColor()
    {
        return _tickLabelFontColor;
    }

    /**
     * Getter for the field the indicating font name of axis title.
     *
     * @return field indicating the font name of axis title
     */
    public FontFields getTitleFontName()
    {
        return _titleFontName;
    }

    /**
     * Getter for the field indicating the relative font size of the title.
     *
     * @return field indicating the relative font size of the title
     */
    public SizeFields getTitleFontSizeRelative()
    {
        return _titleFontSizeRelative;
    }

    /**
     * Getter for the field indicating the font color of axis title.
     *
     * @return field indicating the font color of axis title
     */
    public ColorFields getTitleFontColor()
    {
        return _titleFontColor;
    }

    /**
     * Getter for the field indicating the fixed offset of tick labels.
     *
     * @return field indicating the fixed offset of tick labels
     */
    public SizeFields getTickLabelOffsetFixed()
    {
        return _tickLabelOffsetFixed;
    }

    /**
     * Getter for the reference to the field indicating the fixed offset of the title.
     *
     * @return field indicating the fixed offset of the title
     */
    public SizeFields getTitleOffsetFixed()
    {
        return _titleOffsetFixed;
    }

    /**
     * Getter for the field indicating the axis background color.
     *
     * @return field indicating the axis background color
     */
    public ColorFields getBackgroundColor()
    {
        return _backgroundColor;
    }

    /**
     * Getter for the field indicating the axis border color.
     *
     * @return field indicating the axis border color
     */
    public ColorFields getBorderColor()
    {
        return _borderColor;
    }

    /**
     * Getter for the reference to the field indicating the relative size of ticks.
     *
     * @return reference to the field indicating the relative size of ticks
     */
    public SizeFields getTicksSizeRelative()
    {
        return _ticksSizeRelative;
    }

    /**
     * Getter for the field indicating the width of the main line (fixed).
     *
     * @return field indicating the width of the main line (fixed)
     */
    public SizeFields getMainLineWidthFixed()
    {
        return _mainLineWidthFixed;
    }

    /**
     * Getter for the field indicating the width of the main line (relative).
     *
     * @return field indicating the width of the main line (relative)
     */
    public SizeFields getMainLineWidthRelative()
    {
        return _mainLineWidthRelative;
    }

    /**
     * Getter for the field indicating the whether to consider main line width fixed or relative.
     *
     * @return field indicating the whether to consider main line width fixed or relative
     */
    public FlagFields getMainLineWidthUseRelative()
    {
        return _mainLineWidthUseRelative;
    }

    /**
     * Getter for the field indicating the width of tick lines (fixed).
     *
     * @return field indicating the width of tick lines (fixed)
     */
    public SizeFields getTickLineWidthFixed()
    {
        return _tickLineWidthFixed;
    }

    /**
     * Getter for the field indicating the width of tick lines (relative).
     *
     * @return field indicating the width of tick lines (relative)
     */
    public SizeFields getTickLineWidthRelative()
    {
        return _tickLineWidthRelative;
    }

    /**
     * Getter for the field indicating whether to consider tick line width fixed or relative.
     *
     * @return field indicating whether to consider tick line width fixed or relative
     */
    public FlagFields getTickLineWidthUseRelative()
    {
        return _tickLineWidthUseRelative;
    }

    /**
     * Getter for the field indicating the axis border width (fixed).
     *
     * @return field indicating the axis border width (fixed)
     */
    public SizeFields getBorderWidthFixed()
    {
        return _borderWidthFixed;
    }

    /**
     * Getter for the field indicating the axis border width (relative).
     *
     * @return field indicating the axis border width (relative)
     */
    public SizeFields getBorderWidthRelative()
    {
        return _borderWidthRelative;
    }

    /**
     * Getter for the field indicating whether to consider border width relative.
     *
     * @return field indicating whether to consider border width relative
     */
    public FlagFields getBorderWidthUseRelative()
    {
        return _borderWidthUseRelative;
    }

    /**
     * Getter for the field indicating the fixed font size of tick labels.
     *
     * @return field indicating the fixed font size of tick labels
     */
    public SizeFields getTickLabelFontSizeFixed()
    {
        return _tickLabelFontSizeFixed;
    }

    /**
     * Getter for the field indicating the relative offset of tick labels.
     *
     * @return field indicating the relative offset of tick labels
     */
    public SizeFields getTickLabelOffsetRelative()
    {
        return _tickLabelOffsetRelative;
    }

    /**
     * Getter for the field indicating the fixed font size of the title.
     *
     * @return field indicating the fixed font size of the title
     */
    public SizeFields getTitleFontSizeFixed()
    {
        return _titleFontSizeFixed;
    }

    /**
     * Getter for the field indicating the relative offset of the tile.
     *
     * @return field indicating the relative offset of the tile
     */
    public SizeFields getTitleOffsetRelative()
    {
        return _titleOffsetRelative;
    }

    /**
     * Getter for the field indicating whether to consider tick size relative.
     *
     * @return field indicating whether to consider tick size relative
     */
    public FlagFields getTickSizeUseRelative()
    {
        return _tickSizeUseRelative;
    }

    /**
     * Getter for the field indicating whether to consider tick label size relative.
     *
     * @return field indicating whether to consider tick label size relative
     */
    public FlagFields getTickLabelFontSizeUseRelative()
    {
        return _tickLabelFontSizeUseRelative;
    }

    /**
     * Getter for the field indicating whether to consider tick label offset relative.
     *
     * @return field indicating whether to consider tick label offset relative
     */
    public FlagFields getTickLabelOffsetUseRelative()
    {
        return _tickLabelOffsetUseRelative;
    }

    /**
     * Getter for the field indicating whether to consider title font size relative.
     *
     * @return field indicating whether to consider title font size relative
     */
    public FlagFields getTitleFontSizeUseRelative()
    {
        return _titleFontSizeUseRelative;
    }

    /**
     * Getter for the field indicating whether to consider title offset relative.
     *
     * @return field indicating whether to consider title offset relative
     */
    public FlagFields getTitleOffsetUseRelative()
    {
        return _titleOffsetUseRelative;
    }

    /**
     * Getter for the field indicating whether the background is opaque.
     *
     * @return field indicating whether the background is opaque
     */
    public FlagFields getOpaque()
    {
        return _opaque;
    }
}
