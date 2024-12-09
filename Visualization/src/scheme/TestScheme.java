package scheme;

import color.Color;
import scheme.enums.*;


/**
 * Test scheme (for debugging).
 *
 * @author MTomczyk
 */


public class TestScheme extends AbstractScheme
{
    /**
     * Default constructor.
     */
    public TestScheme()
    {
        super();
    }

    /**
     * Getter for the scheme name.
     *
     * @return scheme name
     */
    @Override
    public String getName()
    {
        return "TestScheme";
    }

    /**
     * Factory-like method for constructing scheme instance (to be overwritten).
     */
    @Override
    public AbstractScheme getInstance()
    {
        return new TestScheme();
    }


    /**
     * Instantiates alignments.
     */
    @Override
    protected void setAlignments()
    {
        super.setAlignments();
        _aligns.put(AlignFields.TITLE, Align.TOP);
        _aligns.put(AlignFields.AXIS_X, Align.BOTTOM);
        _aligns.put(AlignFields.AXIS_Y, Align.LEFT);
        _aligns.put(AlignFields.COLORBAR, Align.RIGHT);
    }

    /**
     * Instantiates colors.
     */
    @Override
    protected void setColors()
    {
        super.setColors();
        _colors.put(ColorFields.PLOT_BORDER, Color.BLACK);
        _colors.put(ColorFields.DRAWING_AREA_BACKGROUND, Color.GREEN);
        _colors.put(ColorFields.DRAWING_AREA_BORDER, Color.RED);
        _colors.put(ColorFields.TITLE_BACKGROUND, Color.BLUE);
        _colors.put(ColorFields.TITLE_BORDER, Color.BLACK);
        _colors.put(ColorFields.AXIS_X_BACKGROUND, new Color(120, 0, 120));
        _colors.put(ColorFields.AXIS_X_BORDER, new Color(240, 0, 240));
        _colors.put(ColorFields.AXIS_X, Color.BLUE);
        _colors.put(ColorFields.AXIS_Y_BACKGROUND, new Color(0, 120, 120));
        _colors.put(ColorFields.AXIS_Y_BORDER, new Color(0, 240, 240));
        _colors.put(ColorFields.AXIS_Y, Color.BLUE);
        _colors.put(ColorFields.COLORBAR_BACKGROUND, new Color(250, 250, 250));
        _colors.put(ColorFields.COLORBAR_BORDER, new Color(120, 120, 120));
    }

    /**
     * Instantiates sizes.
     */
    @Override
    protected void setSizes()
    {
        super.setSizes();
        _sizes.put(SizeFields.PLOT_BORDER_WIDTH_FIXED, 5.0f);
        _sizes.put(SizeFields.PLOT_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        _sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        _sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        _sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        _sizes.put(SizeFields.DRAWING_AREA_BORDER_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_X_BORDER_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_X_MAIN_LINE_WIDTH_FIXED, 5.0f);
        _sizes.put(SizeFields.AXIS_X_TICK_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_X_TICK_LINE_WIDTH_FIXED, 5.0f);
        _sizes.put(SizeFields.AXIS_X_TICK_LABEL_OFFSET_FIXED, 30.0f);
        _sizes.put(SizeFields.AXIS_Y_BORDER_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_Y_MAIN_LINE_WIDTH_FIXED, 5.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LINE_WIDTH_FIXED, 5.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LABEL_OFFSET_FIXED, 30.0f);
        _sizes.put(SizeFields.TITLE_BORDER_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.TITLE_OFFSET_FIXED, 0.0f);
        _sizes.put(SizeFields.TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.0f);
        _sizes.put(SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.1f);

    }

    /**
     * Instantiates flags.
     */
    @Override
    protected void setFlags()
    {
        super.setFlags();
        _flags.put(FlagFields.DRAWING_AREA_USE_BORDER_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.TITLE_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_X_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_X_MAIN_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_X_TICK_USE_RELATIVE_SIZE, false);
        _flags.put(FlagFields.AXIS_X_TICK_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_X_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, false);
        _flags.put(FlagFields.AXIS_Y_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Y_MAIN_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Y_TICK_USE_RELATIVE_SIZE, false);
        _flags.put(FlagFields.AXIS_Y_TICK_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Y_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, false);
    }
}
