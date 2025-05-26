package scheme;

/**
 * White scheme.
 *
 * @author MTomczyk
 */
public class WhiteScheme extends AbstractScheme
{
    /**
     * Default constructor.
     */
    public WhiteScheme()
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
        return "White";
    }

    /**
     * Method for cloning the scheme.
     *
     * @return scheme instance
     */
    @Override
    public WhiteScheme getClone()
    {
        WhiteScheme whiteScheme = new WhiteScheme();
        whiteScheme.setFieldsAsIn(this);
        return whiteScheme;
    }

    /**
     * This method creates a white scheme that is well-suited to plot 3D. The changes are as follows:
     * - all margins are set to 0
     * - drawing area border size is set to 0
     * @return white scheme (customized for plot 3d)
     */
    public static WhiteScheme getForPlot3D()
    {
        return getForPlot3D(0.0f);
    }

    /**
     * This method creates a white scheme that is well-suited to plot 3D. The changes are as follows:
     * - all margins are set to 0, except for the right one that can be specified (relative size multiplier)
     * - drawing area border size is set to 0
     *
     * @param rightMarginRelativeSizeMultiplier right margin relative size multiplier
     * @return white scheme (customized for plot 3d)
     */
    public static WhiteScheme getForPlot3D(float rightMarginRelativeSizeMultiplier)
    {
        WhiteScheme whiteScheme = new WhiteScheme();
        applyCustomizationForPlot3D(whiteScheme, rightMarginRelativeSizeMultiplier);
        return whiteScheme;
    }

    /**
     * This method creates a white scheme that is well-suited to heatmap 3D. The changes are as follows:
     * - all margins are set to 0
     * - drawing area border size is set to 0
     * @return white scheme (customized for heatmap 3d)
     */
    public static WhiteScheme getForHeatmap3D()
    {
        return getForHeatmap3D(0.0f);
    }

    /**
     * This method creates a white scheme that is well-suited to heatmap 3D. The changes are as follows:
     * - all margins are set to 0, except for the right one that can be specified (relative size multiplier)
     * - drawing area border size is set to 0
     *
     * @param rightMarginRelativeSizeMultiplier right margin relative size multiplier
     * @return white scheme (customized for heatmap 3d)
     */
    public static WhiteScheme getForHeatmap3D(float rightMarginRelativeSizeMultiplier)
    {
        WhiteScheme whiteScheme = new WhiteScheme();
        applyCustomizationForPlot3D(whiteScheme, rightMarginRelativeSizeMultiplier);
        return whiteScheme;
    }

    /**
     * This method creates a white scheme that is well-suited to PCP 2D. The changes are as follows:
     * - drawing area border size is set to 0
     * @return white scheme (customized for parallel coordinate plot 2d)
     */
    public static WhiteScheme getForPCP2D()
    {
        WhiteScheme whiteScheme = new WhiteScheme();
        applyCustomizationForPCP2D(whiteScheme);
        return whiteScheme;
    }

}
