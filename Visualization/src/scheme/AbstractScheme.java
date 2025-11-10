package scheme;

import color.Color;
import scheme.enums.*;

import java.util.HashMap;

/**
 * Scheme (to be extended) that automatically fills all essential entries.
 *
 * @author MTomczyk
 */

public abstract class AbstractScheme
{
    /**
     * Scheme: field -> color mapping.
     */
    public HashMap<ColorFields, Color> _colors = null;

    /**
     * Scheme: field -> size mapping.
     */
    public HashMap<SizeFields, Float> _sizes = null;

    /**
     * Scheme: field -> font (string) mapping.
     */
    public HashMap<FontFields, String> _fonts = null;

    /**
     * Scheme: field -> align mapping.
     */
    public HashMap<AlignFields, Align> _aligns = null;

    /**
     * Scheme: field -> number (integer) mapping.
     */
    public HashMap<NumberFields, Integer> _numbers = null;

    /**
     * Scheme: field -> flags mapping.
     */
    public HashMap<FlagFields, Boolean> _flags = null;

    /**
     * Default constructor.
     */
    public AbstractScheme()
    {
        setColors();
        setFonts();
        setSizes();
        setAlignments();
        setNumbers();
        setFlags();
    }

    /**
     * Method for cloning the scheme.
     *
     * @return scheme instance
     */
    public AbstractScheme getClone()
    {
        return null;
    }

    /**
     * Instantiates default colors.
     */
    protected void setColors()
    {
        _colors = new HashMap<>(20);
        _colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
        _colors.put(ColorFields.PLOT_BORDER, null);

        _colors.put(ColorFields.LOADING_PANEL_BACKGROUND, new Color(200, 200, 200));
        _colors.put(ColorFields.LOADING_PANEL_FONT_COLOR, Color.BLACK);

        _colors.put(ColorFields.DRAWING_AREA_BACKGROUND, Color.WHITE);
        _colors.put(ColorFields.DRAWING_AREA_BORDER, Color.BLACK);

        _colors.put(ColorFields.TITLE_BACKGROUND, null);
        _colors.put(ColorFields.TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.TITLE_BORDER, Color.BLACK);

        _colors.put(ColorFields.POPUP_MENU_ITEM_FONT, Color.BLACK);

        _colors.put(ColorFields.GRID_MAIN_LINES, new Color(160, 160, 160));
        _colors.put(ColorFields.GRID_AUX_LINES, new Color(200, 200, 200));

        _colors.put(ColorFields.LEGEND_BACKGROUND, Color.WHITE);
        _colors.put(ColorFields.LEGEND_BORDER, Color.BLACK);
        _colors.put(ColorFields.LEGEND_ENTRY_FONT, Color.BLACK);

        _colors.put(ColorFields.COLORBAR_BACKGROUND, null);
        _colors.put(ColorFields.COLORBAR_BORDER, Color.BLACK);
        _colors.put(ColorFields.COLORBAR_EDGE, Color.BLACK);

        _colors.put(ColorFields.AXIS_X, Color.BLACK);
        _colors.put(ColorFields.AXIS_Y, Color.BLACK);
        _colors.put(ColorFields.AXIS_Z, Color.BLACK);
        _colors.put(ColorFields.AXIS_A1_COLOR, Color.BLACK);
        _colors.put(ColorFields.AXIS_COLORBAR_COLOR, Color.BLACK);

        _colors.put(ColorFields.AXIS_X_BACKGROUND, null);
        _colors.put(ColorFields.AXIS_Y_BACKGROUND, null);
        _colors.put(ColorFields.AXIS_Z_BACKGROUND, null);
        _colors.put(ColorFields.AXIS_A1_BACKGROUND, null);
        _colors.put(ColorFields.AXIS_COLORBAR_BACKGROUND, null);

        _colors.put(ColorFields.AXIS_X_BORDER, null);
        _colors.put(ColorFields.AXIS_Y_BORDER, null);
        _colors.put(ColorFields.AXIS_Z_BORDER, null);
        _colors.put(ColorFields.AXIS_A1_BORDER, null);
        _colors.put(ColorFields.AXIS_COLORBAR_BORDER, null);

        _colors.put(ColorFields.AXIS_X_TICK_LABEL_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_Y_TICK_LABEL_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_Z_TICK_LABEL_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_A1_TICK_LABEL_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_COLORBAR_TICK_LABEL_FONT, Color.BLACK);

        _colors.put(ColorFields.AXIS_X_TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_Y_TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_Z_TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_A1_TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS_COLORBAR_TITLE_FONT, Color.BLACK);

        _colors.put(ColorFields.CUBE_3D_EDGE, Color.BLACK);
        _colors.put(ColorFields.PANE_3D_BACKGROUND, null);
        _colors.put(ColorFields.PANE_3D_LINE, new Color(70, 70, 70, 255));

        _colors.put(ColorFields.AXIS3D_X, new Color(70, 70, 70, 255));
        _colors.put(ColorFields.AXIS3D_Y, new Color(70, 70, 70, 255));
        _colors.put(ColorFields.AXIS3D_Z, new Color(70, 70, 70, 255));

        _colors.put(ColorFields.AXIS3D_X_TICK_LABEL_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS3D_Y_TICK_LABEL_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS3D_Z_TICK_LABEL_FONT, Color.BLACK);

        _colors.put(ColorFields.AXIS3D_X_TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS3D_Y_TITLE_FONT, Color.BLACK);
        _colors.put(ColorFields.AXIS3D_Z_TITLE_FONT, Color.BLACK);
    }

    /**
     * Auxiliary method that sets all fonts as specified.
     *
     * @param fontName font name
     */
    public void setAllFontsTo(String fontName)
    {
        _fonts.put(FontFields.TITLE, fontName);

        _fonts.put(FontFields.POPUP_MENU_ITEM, fontName);

        _fonts.put(FontFields.LOADING_PANEL, fontName);

        _fonts.put(FontFields.LEGEND_ENTRY, fontName);

        _fonts.put(FontFields.AXIS_X_TICK_LABEL, fontName);
        _fonts.put(FontFields.AXIS_Y_TICK_LABEL, fontName);
        _fonts.put(FontFields.AXIS_Z_TICK_LABEL, fontName);
        _fonts.put(FontFields.AXIS_A1_TICK_LABEL, fontName);
        _fonts.put(FontFields.AXIS_COLORBAR_TICK_LABEL, fontName);

        _fonts.put(FontFields.AXIS_X_TITLE, fontName);
        _fonts.put(FontFields.AXIS_Y_TITLE, fontName);
        _fonts.put(FontFields.AXIS_Z_TITLE, fontName);
        _fonts.put(FontFields.AXIS_A1_TITLE, fontName);
        _fonts.put(FontFields.AXIS_COLORBAR_TITLE, fontName);

        _fonts.put(FontFields.AXIS3D_X_TICK_LABEL, fontName);
        _fonts.put(FontFields.AXIS3D_Y_TICK_LABEL, fontName);
        _fonts.put(FontFields.AXIS3D_Z_TICK_LABEL, fontName);

        _fonts.put(FontFields.AXIS3D_X_TITLE, fontName);
        _fonts.put(FontFields.AXIS3D_Y_TITLE, fontName);
        _fonts.put(FontFields.AXIS3D_Z_TITLE, fontName);
    }


    /**
     * Instantiates default fonts (names).
     */
    protected void setFonts()
    {
        _fonts = new HashMap<>(20);
        setAllFontsTo("Helvetica");
    }

    /**
     * Instantiates default sizes.
     */
    protected void setSizes()
    {
        _sizes = new HashMap<>(100);

        _sizes.put(SizeFields.PLOT_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.PLOT_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.000f);

        _sizes.put(SizeFields.LOADING_PANEL_FONT_SIZE_FIXED, 30.0f);
        _sizes.put(SizeFields.LOADING_PANEL_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);

        _sizes.put(SizeFields.MARGIN_LEFT_SIZE_FIXED, 75.0f);
        _sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.15f);
        _sizes.put(SizeFields.MARGIN_TOP_SIZE_FIXED, 75.0f);
        _sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.075f);
        _sizes.put(SizeFields.MARGIN_RIGHT_SIZE_FIXED, 75.0f);
        _sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.05f);
        _sizes.put(SizeFields.MARGIN_BOTTOM_SIZE_FIXED, 75.0f);
        _sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.1f);

        _sizes.put(SizeFields.DRAWING_AREA_BORDER_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.DRAWING_AREA_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);

        _sizes.put(SizeFields.DRAWING_AREA_INNER_OFFSET_FIXED, 5.0f);
        _sizes.put(SizeFields.DRAWING_AREA_INNER_OFFSET_RELATIVE_MULTIPLIER, 0.02f);

        _sizes.put(SizeFields.TITLE_FONT_SIZE_FIXED, 30.0f);
        _sizes.put(SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.04f);
        _sizes.put(SizeFields.TITLE_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.TITLE_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);
        _sizes.put(SizeFields.TITLE_OFFSET_FIXED, 25.0f);
        _sizes.put(SizeFields.TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.03f);

        _sizes.put(SizeFields.POPUP_MENU_ITEM_FONT_SIZE_FIXED, 30.0f);
        _sizes.put(SizeFields.POPUP_MENU_ITEM_FONT_SIZE_RELATIVE_MULTIPLIER, 0.02f);

        _sizes.put(SizeFields.GRID_MAIN_LINES_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.GRID_MAIN_LINES_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.GRID_AUX_LINES_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.GRID_AUX_LINES_WIDTH_RELATIVE_MULTIPLIER, 0.001f);

        _sizes.put(SizeFields.LEGEND_BORDER_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.LEGEND_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.LEGEND_OFFSET_FIXED, 5.0f);
        _sizes.put(SizeFields.LEGEND_OFFSET_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.LEGEND_INNER_OFFSET_FIXED, 5.0f);
        _sizes.put(SizeFields.LEGEND_INNER_OFFSET_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);
        _sizes.put(SizeFields.LEGEND_ENTRIES_SPACING_FIXED, 5.0f);
        _sizes.put(SizeFields.LEGEND_ENTRIES_SPACING_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.LEGEND_DRAWING_LABEL_SEPARATOR_FIXED, 5.0f);
        _sizes.put(SizeFields.LEGEND_DRAWING_LABEL_SEPARATOR_RELATIVE_MULTIPLIER, 0.02f); // 0.02f
        _sizes.put(SizeFields.LEGEND_COLUMNS_SEPARATOR_FIXED, 5.0f);
        _sizes.put(SizeFields.LEGEND_COLUMNS_SEPARATOR_RELATIVE_MULTIPLIER, 0.02f);
        _sizes.put(SizeFields.LEGEND_MARKER_SCALING_FACTOR_MULTIPLIER, 0.6f);
        _sizes.put(SizeFields.LEGEND_LINE_SCALING_FACTOR_MULTIPLIER, 0.6f);

        _sizes.put(SizeFields.COLORBAR_OFFSET_FIXED, 5.0f);
        _sizes.put(SizeFields.COLORBAR_OFFSET_RELATIVE_MULTIPLIER, 0.02f);
        _sizes.put(SizeFields.COLORBAR_WIDTH_FIXED, 20.0f);
        _sizes.put(SizeFields.COLORBAR_WIDTH_RELATIVE_MULTIPLIER, 0.04f);
        _sizes.put(SizeFields.COLORBAR_SHRINK, 0.33f);
        _sizes.put(SizeFields.COLORBAR_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.COLORBAR_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.COLORBAR_EDGE_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.COLORBAR_EDGE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);

        _sizes.put(SizeFields.AXIS_X_TICK_SIZE_FIXED, 4.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_SIZE_FIXED, 4.0f);
        _sizes.put(SizeFields.AXIS_Z_TICK_SIZE_FIXED, 4.0f);
        _sizes.put(SizeFields.AXIS_A1_TICK_SIZE_FIXED, 4.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_SIZE_FIXED, 4.0f);

        _sizes.put(SizeFields.AXIS_X_TICK_SIZE_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.AXIS_Y_TICK_SIZE_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.AXIS_Z_TICK_SIZE_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.AXIS_A1_TICK_SIZE_RELATIVE_MULTIPLIER, 0.01f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_SIZE_RELATIVE_MULTIPLIER, 0.01f);

        _sizes.put(SizeFields.AXIS_X_MAIN_LINE_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_Y_MAIN_LINE_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_Z_MAIN_LINE_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_A1_MAIN_LINE_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_MAIN_LINE_WIDTH_FIXED, 0.0f);

        _sizes.put(SizeFields.AXIS_X_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_Y_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_Z_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_A1_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_COLORBAR_MAIN_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);

        _sizes.put(SizeFields.AXIS_X_TICK_LINE_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LINE_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_Z_TICK_LINE_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_A1_TICK_LINE_WIDTH_FIXED, 1.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_LINE_WIDTH_FIXED, 1.0f);

        _sizes.put(SizeFields.AXIS_X_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_Z_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_A1_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_LINE_WIDTH_RELATIVE_MULTIPLIER, 0.001f);

        _sizes.put(SizeFields.AXIS_X_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_Y_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_Z_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_A1_BORDER_WIDTH_FIXED, 0.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_BORDER_WIDTH_FIXED, 0.0f);

        _sizes.put(SizeFields.AXIS_X_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);
        _sizes.put(SizeFields.AXIS_Y_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);
        _sizes.put(SizeFields.AXIS_Z_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);
        _sizes.put(SizeFields.AXIS_A1_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);
        _sizes.put(SizeFields.AXIS_COLORBAR_BORDER_WIDTH_RELATIVE_MULTIPLIER, 0.002f);

        _sizes.put(SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_Z_TICK_LABEL_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_A1_TICK_LABEL_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_FIXED, 20.0f);

        _sizes.put(SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER, 0.025f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER, 0.025f);
        _sizes.put(SizeFields.AXIS_Z_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER, 0.025f);
        _sizes.put(SizeFields.AXIS_A1_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER, 0.025f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER, 0.025f);

        _sizes.put(SizeFields.AXIS_X_TICK_LABEL_OFFSET_FIXED, 10.0f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LABEL_OFFSET_FIXED, 10.0f);
        _sizes.put(SizeFields.AXIS_Z_TICK_LABEL_OFFSET_FIXED, 10.0f);
        _sizes.put(SizeFields.AXIS_A1_TICK_LABEL_OFFSET_FIXED, 10.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_LABEL_OFFSET_FIXED, 10.0f);

        _sizes.put(SizeFields.AXIS_X_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER, 0.02f);
        _sizes.put(SizeFields.AXIS_Y_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER, 0.02f);
        _sizes.put(SizeFields.AXIS_Z_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER, 0.02f);
        _sizes.put(SizeFields.AXIS_A1_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER, 0.02f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TICK_LABEL_OFFSET_RELATIVE_MULTIPLIER, 0.02f);

        _sizes.put(SizeFields.AXIS_X_TITLE_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_Y_TITLE_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_Z_TITLE_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_A1_TITLE_FONT_SIZE_FIXED, 20.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_FIXED, 20.0f);

        _sizes.put(SizeFields.AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);
        _sizes.put(SizeFields.AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);
        _sizes.put(SizeFields.AXIS_Z_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);
        _sizes.put(SizeFields.AXIS_A1_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.03f);

        _sizes.put(SizeFields.AXIS_X_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.06f);
        _sizes.put(SizeFields.AXIS_Y_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.11f);
        _sizes.put(SizeFields.AXIS_Z_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.125f);
        _sizes.put(SizeFields.AXIS_A1_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.125f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.125f);

        _sizes.put(SizeFields.AXIS_X_TITLE_OFFSET_FIXED, 40.0f);
        _sizes.put(SizeFields.AXIS_Y_TITLE_OFFSET_FIXED, 40.0f);
        _sizes.put(SizeFields.AXIS_Z_TITLE_OFFSET_FIXED, 40.0f);
        _sizes.put(SizeFields.AXIS_A1_TITLE_OFFSET_FIXED, 40.0f);
        _sizes.put(SizeFields.AXIS_COLORBAR_TITLE_OFFSET_FIXED, 40.0f);

        _sizes.put(SizeFields.AXIS3D_X_TICK_SIZE, 0.02f);
        _sizes.put(SizeFields.AXIS3D_Y_TICK_SIZE, 0.02f);
        _sizes.put(SizeFields.AXIS3D_Z_TICK_SIZE, 0.02f);

        _sizes.put(SizeFields.AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE, 0.0015f);
        _sizes.put(SizeFields.AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE, 0.0015f);
        _sizes.put(SizeFields.AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE, 0.0015f);

        _sizes.put(SizeFields.AXIS3D_X_TITLE_FONT_SIZE_SCALE, 0.002f);
        _sizes.put(SizeFields.AXIS3D_Y_TITLE_FONT_SIZE_SCALE, 0.002f);
        _sizes.put(SizeFields.AXIS3D_Z_TITLE_FONT_SIZE_SCALE, 0.002f);

        _sizes.put(SizeFields.AXIS3D_X_TICK_LABEL_OFFSET, 0.06f);
        _sizes.put(SizeFields.AXIS3D_Y_TICK_LABEL_OFFSET, 0.06f);
        _sizes.put(SizeFields.AXIS3D_Z_TICK_LABEL_OFFSET, 0.06f);

        _sizes.put(SizeFields.AXIS3D_X_TITLE_OFFSET, 0.2f);
        _sizes.put(SizeFields.AXIS3D_Y_TITLE_OFFSET, 0.2f);
        _sizes.put(SizeFields.AXIS3D_Z_TITLE_OFFSET, 0.2f);

        _sizes.put(SizeFields.FONT_3D_QUALITY_UPSCALING, 8.0f);

        _sizes.put(SizeFields.CUBE3D_LINES_WIDTH, null);

        _sizes.put(SizeFields.PANE3D_FRONT_LINES_WIDTH, null);
        _sizes.put(SizeFields.PANE3D_BACK_LINES_WIDTH, null);
        _sizes.put(SizeFields.PANE3D_LEFT_LINES_WIDTH, null);
        _sizes.put(SizeFields.PANE3D_RIGHT_LINES_WIDTH, null);
        _sizes.put(SizeFields.PANE3D_TOP_LINES_WIDTH, null);
        _sizes.put(SizeFields.PANE3D_BOTTOM_LINES_WIDTH, null);

        _sizes.put(SizeFields.AXIS3D_X_TICK_LINE_WIDTH, null);
        _sizes.put(SizeFields.AXIS3D_X_MAIN_LINE_WIDTH, null);
        _sizes.put(SizeFields.AXIS3D_Y_TICK_LINE_WIDTH, null);
        _sizes.put(SizeFields.AXIS3D_Y_MAIN_LINE_WIDTH, null);
        _sizes.put(SizeFields.AXIS3D_Z_TICK_LINE_WIDTH, null);
        _sizes.put(SizeFields.AXIS3D_Z_MAIN_LINE_WIDTH, null);
    }

    /**
     * Auxiliary method for rescaling existing size entry.
     *
     * @param rescale   rescaling factor
     * @param sizeField size entry
     */
    public void rescale(float rescale, SizeFields sizeField)
    {
        if (!_sizes.containsKey(sizeField)) return;
        Float current = _sizes.get(sizeField);
        if (current == null) return;
        _sizes.put(sizeField, current * rescale);
    }

    /**
     * Instantiates default alignments.
     */
    protected void setAlignments()
    {
        _aligns = new HashMap<>(20);
        _aligns.put(AlignFields.TITLE, Align.TOP);
        _aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        _aligns.put(AlignFields.COLORBAR, Align.RIGHT);
        _aligns.put(AlignFields.AXIS_X, Align.BOTTOM);
        _aligns.put(AlignFields.AXIS_Y, Align.LEFT);
        _aligns.put(AlignFields.AXIS_Z, Align.RIGHT);
        _aligns.put(AlignFields.AXIS_A1, Align.RIGHT);
        _aligns.put(AlignFields.AXIS_COLORBAR, Align.RIGHT);

        _aligns.put(AlignFields.AXIS3D_X, Align.FRONT_BOTTOM);
        _aligns.put(AlignFields.AXIS3D_Y, Align.BACK_LEFT);
        _aligns.put(AlignFields.AXIS3D_Z, Align.RIGHT_TOP);
    }

    /**
     * Instantiates default numbers.
     */
    protected void setNumbers()
    {
        _numbers = new HashMap<>(10);
        _numbers.put(NumberFields.GRID_MAIN_DASH_PATTERN, 0);
        _numbers.put(NumberFields.GRID_AUX_DASH_PATTERN, 100);
        _numbers.put(NumberFields.LEGEND_NO_ENTRIES_PER_COLUMN_LIMIT, Integer.MAX_VALUE);
    }

    /**
     * Instantiates default flags.
     */
    protected void setFlags()
    {
        _flags = new HashMap<>(20);
        _flags.put(FlagFields.PLOT_OPAQUE, true);
        _flags.put(FlagFields.PLOT_BORDER_USE_RELATIVE_WIDTH, true);

        _flags.put(FlagFields.LOADING_PANEL_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.MARGIN_LEFT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.MARGIN_TOP_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.MARGIN_RIGHT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.MARGIN_BOTTOM_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.MARGINS_CONDITIONALLY_IGNORE, false);

        _flags.put(FlagFields.TITLE_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.TITLE_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.TITLE_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.TITLE_OPAQUE, false);

        _flags.put(FlagFields.POPUP_MENU_ITEM_FONT_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.DRAWING_AREA_USE_BORDER_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.DRAWING_AREA_USE_INNER_OFFSET_RELATIVE_SIZE, true);
        _flags.put(FlagFields.DRAWING_AREA_OPAQUE, false);

        _flags.put(FlagFields.GRID_MAIN_LINES_USE_RELATIVE_WIDTH, true);
        _flags.put(FlagFields.GRID_AUX_LINES_USE_RELATIVE_WIDTH, true);

        _flags.put(FlagFields.LEGEND_BORDER_USE_RELATIVE_WIDTH, true);
        _flags.put(FlagFields.LEGEND_DRAWING_LABEL_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.LEGEND_INNER_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.LEGEND_ENTRY_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.LEGEND_ENTRIES_SPACING_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.LEGEND_DRAWING_LABEL_SEPARATOR_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.LEGEND_COLUMNS_SEPARATOR_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.LEGEND_OPAQUE, false);

        _flags.put(FlagFields.COLORBAR_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.COLORBAR_WIDTH_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.COLORBAR_OPAQUE, false);
        _flags.put(FlagFields.COLORBAR_BORDER_USE_RELATIVE_WITH, false);
        _flags.put(FlagFields.COLORBAR_EDGE_USE_RELATIVE_WIDTH, true);

        _flags.put(FlagFields.AXIS_X_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Y_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Z_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_A1_BORDER_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_COLORBAR_BORDER_USE_RELATIVE_WIDTH, false);

        _flags.put(FlagFields.AXIS_X_MAIN_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Y_MAIN_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_Z_MAIN_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_A1_MAIN_LINE_USE_RELATIVE_WIDTH, false);
        _flags.put(FlagFields.AXIS_COLORBAR_MAIN_LINE_USE_RELATIVE_WIDTH, false);

        _flags.put(FlagFields.AXIS_X_TICK_LINE_USE_RELATIVE_WIDTH, true);
        _flags.put(FlagFields.AXIS_Y_TICK_LINE_USE_RELATIVE_WIDTH, true);
        _flags.put(FlagFields.AXIS_Z_TICK_LINE_USE_RELATIVE_WIDTH, true);
        _flags.put(FlagFields.AXIS_A1_TICK_LINE_USE_RELATIVE_WIDTH, true);
        _flags.put(FlagFields.AXIS_COLORBAR_TICK_LINE_USE_RELATIVE_WIDTH, true);

        _flags.put(FlagFields.AXIS_X_TICK_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Y_TICK_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Z_TICK_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_A1_TICK_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_COLORBAR_TICK_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.AXIS_X_TICK_LABEL_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Y_TICK_LABEL_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Z_TICK_LABEL_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_A1_TICK_LABEL_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_COLORBAR_TICK_LABEL_FONT_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.AXIS_X_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Y_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Z_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_A1_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_COLORBAR_TICK_LABEL_OFFSET_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.AXIS_X_TITLE_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Y_TITLE_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Z_TITLE_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_A1_TITLE_FONT_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_COLORBAR_TITLE_FONT_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.AXIS_X_TITLE_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Y_TITLE_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_Z_TITLE_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_A1_TITLE_OFFSET_USE_RELATIVE_SIZE, true);
        _flags.put(FlagFields.AXIS_COLORBAR_TITLE_OFFSET_USE_RELATIVE_SIZE, true);

        _flags.put(FlagFields.AXIS_X_OPAQUE, false);
        _flags.put(FlagFields.AXIS_Y_OPAQUE, false);
        _flags.put(FlagFields.AXIS_Z_OPAQUE, false);
        _flags.put(FlagFields.AXIS_A1_OPAQUE, false);
        _flags.put(FlagFields.AXIS_COLORBAR_OPAQUE, false);

        _flags.put(FlagFields.DEBUG_DISABLE_FRONT_DRAWING, false);
    }


    /**
     * Supportive method for determining the alignment.
     * It considers (and prioritizes) the optional "surpassed" map
     * that overwrites the fields stored in the scheme.
     *
     * @param surpassed "surpassed" map
     * @param field     considered field
     * @return alignment
     */
    public Align getAlignments(HashMap<AlignFields, Align> surpassed, AlignFields field)
    {
        if (surpassed == null) return _aligns.get(field);
        if (surpassed.containsKey(field)) return surpassed.get(field);
        else return _aligns.get(field);
    }

    /**
     * Supportive method for determining the color.
     * It considers (and prioritizes) the optional "surpassed" map
     * that overwrites the fields stored in the scheme.
     *
     * @param surpassed "surpassed" map
     * @param field     considered field
     * @return color
     */
    public Color getColors(HashMap<ColorFields, Color> surpassed, ColorFields field)
    {
        if (surpassed == null) return _colors.get(field);
        if (surpassed.containsKey(field)) return surpassed.get(field);
        else return _colors.get(field);
    }


    /**
     * Supportive method for determining the size.
     * It considers (and prioritizes) the optional "surpassed" map
     * that overwrites the fields stored in the scheme.
     *
     * @param surpassed "surpassed" map
     * @param field     considered field
     * @return size
     */
    public Float getSizes(HashMap<SizeFields, Float> surpassed, SizeFields field)
    {
        if (surpassed == null) return _sizes.get(field);
        if (surpassed.containsKey(field)) return surpassed.get(field);
        else return _sizes.get(field);
    }

    /**
     * Supportive method for determining the flag.
     * It considers (and prioritizes) the optional "surpassed" map
     * that overwrites the fields stored in the scheme.
     *
     * @param surpassed "surpassed" map
     * @param field     considered field
     * @return flag
     */
    public Boolean getFlags(HashMap<FlagFields, Boolean> surpassed, FlagFields field)
    {
        if (surpassed == null) return _flags.get(field);
        if (surpassed.containsKey(field)) return surpassed.get(field);
        else return _flags.get(field);
    }

    /**
     * Supportive method for determining the font (name).
     * It considers (and prioritizes) the optional "surpassed" map
     * that overwrites the fields stored in the scheme.
     *
     * @param surpassed "surpassed" map
     * @param field     considered field
     * @return font name
     */
    public String getFonts(HashMap<FontFields, String> surpassed, FontFields field)
    {
        if (surpassed == null) return _fonts.get(field);
        if (surpassed.containsKey(field)) return surpassed.get(field);
        else return _fonts.get(field);
    }

    /**
     * Supportive method for determining the number.
     * It considers (and prioritizes) the optional "surpassed" map
     * that overwrites the fields stored in the scheme.
     *
     * @param surpassed "surpassed" map
     * @param field     considered field
     * @return number
     */
    public Integer getNumbers(HashMap<NumberFields, Integer> surpassed, NumberFields field)
    {
        if (surpassed == null) return _numbers.get(field);
        if (surpassed.containsKey(field)) return surpassed.get(field);
        else return _numbers.get(field);
    }

    /**
     * Getter for the scheme name.
     *
     * @return scheme name
     */
    public String getName()
    {
        return "Abstract";
    }



    /**
     * Auxiliary method that sets fields' values as in other object.
     *
     * @param reference reference scheme
     */
    protected void setFieldsAsIn(AbstractScheme reference)
    {
        for (AlignFields af : AlignFields.values()) _aligns.put(af, reference.getAlignments(null, af));
        for (ColorFields cf : ColorFields.values()) _colors.put(cf, reference.getColors(null, cf));
        for (FlagFields ff : FlagFields.values()) _flags.put(ff, reference.getFlags(null, ff));
        for (FontFields ff : FontFields.values()) _fonts.put(ff, reference.getFonts(null, ff));
        for (NumberFields nf : NumberFields.values()) _numbers.put(nf, reference.getNumbers(null, nf));
        for (SizeFields sf : SizeFields.values()) _sizes.put(sf, reference.getSizes(null, sf));
    }


    /**
     * This method customizes the input scheme to be well-suited to plot 3D. The changes are as follows:
     * - all margins are set to 0, except for the right one that can be specified (relative size multiplier)
     * - drawing area border size is set to 0
     *
     * @param abstractScheme                    scheme to be customized
     * @param rightMarginRelativeSizeMultiplier right margin relative size multiplier
     */
    protected static void applyCustomizationForPlot3D(AbstractScheme abstractScheme, float rightMarginRelativeSizeMultiplier)
    {
        abstractScheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.0f);
        abstractScheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, rightMarginRelativeSizeMultiplier);
        abstractScheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.0f);
        abstractScheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.0f);

        abstractScheme._flags.put(FlagFields.MARGIN_BOTTOM_USE_RELATIVE_SIZE, true);
        abstractScheme._flags.put(FlagFields.MARGIN_RIGHT_USE_RELATIVE_SIZE, true);
        abstractScheme._flags.put(FlagFields.MARGIN_TOP_USE_RELATIVE_SIZE, true);
        abstractScheme._flags.put(FlagFields.MARGIN_LEFT_USE_RELATIVE_SIZE, true);

        abstractScheme._sizes.put(SizeFields.DRAWING_AREA_BORDER_WIDTH_FIXED, 0.0f);
        abstractScheme._flags.put(FlagFields.DRAWING_AREA_USE_BORDER_RELATIVE_WIDTH, false);
    }

    /**
     * This method customizes the input scheme to be well-suited to the PCP 2D. The changes are as follows:
     * - the right margin relative size is set as provided
     * - drawing area border size is set to 0
     *
     * @param abstractScheme scheme to be customized
     */
    protected static void applyCustomizationForPCP2D(AbstractScheme abstractScheme)
    {
        abstractScheme._sizes.put(SizeFields.DRAWING_AREA_BORDER_WIDTH_FIXED, 0.0f);
        abstractScheme._flags.put(FlagFields.DRAWING_AREA_USE_BORDER_RELATIVE_WIDTH, false);
    }

}
