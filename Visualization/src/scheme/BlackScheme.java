package scheme;

import color.Color;
import scheme.enums.ColorFields;


/**
 * Black scheme.
 *
 * @author MTomczyk
 */


public class BlackScheme extends AbstractScheme
{
    /**
     * Default constructor.
     */
    public BlackScheme()
    {
        super();
    }

    /**
     * Instantiates colors.
     */
    @Override
    protected void setColors()
    {
        super.setColors();
        _colors.put(ColorFields.PLOT_BACKGROUND, new Color(49, 51, 54));
        _colors.put(ColorFields.PLOT_BORDER, null);

        _colors.put(ColorFields.LOADING_PANEL_BACKGROUND, new Color(49, 51, 54));
        _colors.put(ColorFields.LOADING_PANEL_FONT_COLOR, Color.WHITE);

        _colors.put(ColorFields.DRAWING_AREA_BACKGROUND, new Color(49, 51, 54));
        _colors.put(ColorFields.DRAWING_AREA_BORDER, new Color(85, 85, 85));

        _colors.put(ColorFields.TITLE_BACKGROUND, null);
        _colors.put(ColorFields.TITLE_FONT, Color.WHITE);
        _colors.put(ColorFields.TITLE_BORDER, new Color(49, 51, 54));

        _colors.put(ColorFields.POPUP_MENU_ITEM_FONT, Color.BLACK);

        _colors.put(ColorFields.GRID_MAIN_LINES, new Color(120, 120, 120));
        _colors.put(ColorFields.GRID_AUX_LINES, new Color(80, 80, 80));

        _colors.put(ColorFields.LEGEND_BACKGROUND, new Color(49, 51, 54));
        _colors.put(ColorFields.LEGEND_BORDER, new Color(85, 85, 85));
        _colors.put(ColorFields.LEGEND_ENTRY_FONT, new Color(200, 200, 200));

        _colors.put(ColorFields.COLORBAR_BACKGROUND, null);
        _colors.put(ColorFields.COLORBAR_BORDER, new Color(180, 180, 180));
        _colors.put(ColorFields.COLORBAR_EDGE, new Color(180, 180, 180));

        _colors.put(ColorFields.AXIS_X, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_Y, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_Z, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_A1_COLOR, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_COLORBAR_COLOR, new Color(200, 200, 200));

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

        _colors.put(ColorFields.AXIS_X_TICK_LABEL_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_Y_TICK_LABEL_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_Z_TICK_LABEL_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_A1_TICK_LABEL_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_COLORBAR_TICK_LABEL_FONT, new Color(200, 200, 200));

        _colors.put(ColorFields.AXIS_X_TITLE_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_Y_TITLE_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_Z_TITLE_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_A1_TITLE_FONT, new Color(200, 200, 200));
        _colors.put(ColorFields.AXIS_COLORBAR_TITLE_FONT, new Color(200, 200, 200));

        _colors.put(ColorFields.CUBE_3D_EDGE, new Color(255, 255, 255));
        _colors.put(ColorFields.PANE_3D_BACKGROUND, null);
        _colors.put(ColorFields.PANE_3D_LINE, new Color(100, 100, 100));

        _colors.put(ColorFields.AXIS3D_X, new Color(255, 255, 255));
        _colors.put(ColorFields.AXIS3D_Y, new Color(255, 255, 255));
        _colors.put(ColorFields.AXIS3D_Z, new Color(255, 255, 255));

        _colors.put(ColorFields.AXIS3D_X_TICK_LABEL_FONT, new Color(255, 255, 255));
        _colors.put(ColorFields.AXIS3D_Y_TICK_LABEL_FONT, new Color(255, 255, 255));
        _colors.put(ColorFields.AXIS3D_Z_TICK_LABEL_FONT, new Color(255, 255, 255));

        _colors.put(ColorFields.AXIS3D_X_TITLE_FONT, new Color(255, 255, 255));
        _colors.put(ColorFields.AXIS3D_Y_TITLE_FONT, new Color(255, 255, 255));
        _colors.put(ColorFields.AXIS3D_Z_TITLE_FONT, new Color(255, 255, 255));

    }

    /**
     * Getter for the scheme name.
     *
     * @return scheme name
     */
    @Override
    public String getName()
    {
        return "Black";
    }

    /**
     * Factory-like method for constructing scheme instance (to be overwritten).
     */
    @Override
    public AbstractScheme getInstance()
    {
        return new BlackScheme();
    }


    /**
     * This method creates a black scheme that is well-suited to plot 3D. The changes are as follows:
     * - all margins are set to 0
     * - drawing area border size is set to 0
     * @return black scheme (customized for plot 3d)
     */
    public static BlackScheme getForPlot3D()
    {
        return getForPlot3D(0.0f);
    }

    /**
     * This method creates a black scheme that is well-suited to plot 3D. The changes are as follows:
     * - all margins are set to 0, except for the right one that can be specified (relative size multiplier)
     * - drawing area border size is set to 0
     *
     * @param rightMarginRelativeSizeMultiplier right margin relative size multiplier
     *
     * @return black scheme (customized for plot 3d)
     */
    public static BlackScheme getForPlot3D(float rightMarginRelativeSizeMultiplier)
    {
        BlackScheme blackScheme = new BlackScheme();
        applyCustomizationForPlot3D(blackScheme, rightMarginRelativeSizeMultiplier);
        return blackScheme;
    }

    /**
     * This method creates a black scheme that is well-suited to heatmap 3D. The changes are as follows:
     * - all margins are set to 0
     * - drawing area border size is set to 0
     *
     * @return black scheme (customized for heatmap 3d)
     */
    public static BlackScheme getForHeatmap3D()
    {
        return getForHeatmap3D(0.0f);
    }

    /**
     * This method creates a black scheme that is well-suited to heatmap 3D. The changes are as follows:
     * - all margins are set to 0, except for the right one that can be specified (relative size multiplier)
     * - drawing area border size is set to 0
     *
     * @param rightMarginRelativeSizeMultiplier right margin relative size multiplier
     *
     * @return black scheme (customized for heatmap 3d)
     */
    public static BlackScheme getForHeatmap3D(float rightMarginRelativeSizeMultiplier)
    {
        BlackScheme blackScheme = new BlackScheme();
        applyCustomizationForPlot3D(blackScheme, rightMarginRelativeSizeMultiplier);
        return blackScheme;
    }

    /**
     * This method creates a black scheme that is well-suited to PCP 2D. The changes are as follows:
     * - drawing area border size is set to 0
     *
     * @return black scheme (customized for parallel coordinate plot 2d)
     */
    public static BlackScheme getForPCP2D()
    {
        BlackScheme blackScheme = new BlackScheme();
        applyCustomizationForPCP2D(blackScheme);
        return blackScheme;
    }
}
