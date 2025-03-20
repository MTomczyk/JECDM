package plot;

import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import scheme.AbstractScheme;
import scheme.WhiteScheme;

import java.text.DecimalFormat;

/**
 * Provides various means for quickly instantiating {@link Plot3D} objects.
 *
 * @author MTomczyk
 */
public class Plot3DFactory
{
    /**
     * Creates a simple 3D plot. It:
     * - uses a white scheme,
     * - sets the display ranges to fixed (non-dynamic) values: [0, xyLimit (parameter)],
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel   X-axis label
     * @param yLabel   Y-axis label
     * @param zLabel   Z-axis label
     * @param xyzLimit upper limits for the display ranges
     * @param fsr      rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 double xyzLimit,
                                 float fsr)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel,
                DRMPFactory.getFor3D(xyzLimit, xyzLimit, xyzLimit),
                5, 5, 5,
                null, null, null,
                fsr, null, null, null);
    }


    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel   X-axis label
     * @param yLabel   Y-axis label
     * @param zLabel   Z-axis label
     * @param pDRM     display ranges manager params container
     * @param xNoTicks adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                null, null, null, 1.0f, null, null, null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel   X-axis label
     * @param yLabel   Y-axis label
     * @param zLabel   Z-axis label
     * @param pDRM     display ranges manager params container
     * @param xNoTicks adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param fsr      rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 float fsr)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                null, null, null, fsr, null, null, null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 String zAxisTicksLabelsFormat,
                                 float fsr)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, fsr, null, null, null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param zLabel             Z-axis label
     * @param pDRM               display ranges manager params container
     * @param xNoTicks           adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks           adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks           adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                null, null, null, fsr, schemeAdjuster, plotParamsAdjuster,
                null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param zLabel             Z-axis label
     * @param pDRM               display ranges manager params container
     * @param xNoTicks           adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks           adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks           adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param schemeAdjuster     can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                null, null, null, 1.0f, schemeAdjuster, plotParamsAdjuster,
                null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                null, null, null, 1.0f, schemeAdjuster, plotParamsAdjuster,
                postPlotCreationAdjuster);
    }


    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                null, null, null, fsr, schemeAdjuster, plotParamsAdjuster,
                postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @param schemeAdjuster         can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster     can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 String zAxisTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, fsr, schemeAdjuster, plotParamsAdjuster,
                null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 String zAxisTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, fsr, schemeAdjuster, plotParamsAdjuster,
                postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                 plot scheme
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(AbstractScheme scheme,
                                 String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 String zAxisTicksLabelsFormat,
                                 float fsr)
    {
        return getPlot(scheme, xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, fsr, null, null, null);
    }

    /**
     * Creates a simple 3D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(AbstractScheme scheme,
                                 String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 String zAxisTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._useAlphaChannel = true;
        AbstractFactory.performCommonParameterization3D(pP, scheme, xLabel, yLabel, zLabel, pDRM, fsr, schemeAdjuster, plotParamsAdjuster);
        Plot3D plot3D = new Plot3D(pP);
        AbstractFactory.adjustAxes3D(plot3D, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, zAxisTicksLabelsFormat,
                xNoTicks, yNoTicks, zNoTicks);
        AbstractFactory.adjustNoMainGridLines3D(plot3D, xNoTicks, yNoTicks, zNoTicks);
        if (postPlotCreationAdjuster != null) postPlotCreationAdjuster.adjust(plot3D);
        return plot3D;
    }
}
