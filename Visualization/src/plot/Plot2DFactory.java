package plot;

import drmanager.DisplayRangesManager;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import space.Range;

import java.text.DecimalFormat;

/**
 * Provides various means for quickly instantiating {@link Plot2D} objects.
 *
 * @author MTomczyk
 */
public class Plot2DFactory
{
    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the display ranges to fixed (non-dynamic) values: [0, xyLimit (parameter)],
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel  X-axis label
     * @param yLabel  Y-axis label
     * @param xyLimit upper limits for the display ranges
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel, String yLabel, double xyLimit)
    {
        return getPlot(xLabel, yLabel, xyLimit, 1.0f, null, null);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the display ranges to fixed (non-dynamic) values: [0, xyLimit (parameter)],
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel  X-axis label
     * @param yLabel  Y-axis label
     * @param xyLimit upper limits for the display ranges
     * @param fsr     rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel, String yLabel, double xyLimit, float fsr)
    {
        return getPlot(xLabel, yLabel, xyLimit, fsr, null, null);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the display ranges to fixed (non-dynamic) values: [0, xyLimit (parameter)],
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param xyLimit            upper limits for the display ranges
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel, String yLabel, double xyLimit, float fsr,
                                 ISchemeAdjuster schemeAdjuster, IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, new Range(0.0d, xyLimit), new Range(0.0d, xyLimit), fsr, schemeAdjuster, plotParamsAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param xDR                display range for X-axis (considered fixed)
     * @param yRD                display range for Y-axis (considered fixed)
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel, String yLabel, Range xDR, Range yRD, float fsr,
                                 ISchemeAdjuster schemeAdjuster, IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, xDR, yRD, 5, 5, fsr, schemeAdjuster, plotParamsAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the display ranges to fixed (non-dynamic) values: [0, rLim (parameter)],
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param xDR                display range for X-axis (considered fixed)
     * @param yDR                display range for Y-axis (considered fixed)
     * @param xNoTicks           adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks           adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 Range xDR,
                                 Range yDR,
                                 int xNoTicks,
                                 int yNoTicks,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, DisplayRangesManager.Params.getFor2D(xDR, yDR), xNoTicks,
                yNoTicks, fsr, schemeAdjuster, plotParamsAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - it uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param pDRM               display ranges manager params container
     * @param xNoTicks           adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks           adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                           of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, null, fsr, schemeAdjuster, plotParamsAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param pDRM                  display ranges manager params container
     * @param xNoTicks              adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks              adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not used)
     * @param fsr                   rescaling factor used for all font-based objects
     * @param schemeAdjuster        can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                              of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster    can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                              of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 String axesTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, axesTicksLabelsFormat,
                axesTicksLabelsFormat, fsr, schemeAdjuster, plotParamsAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param schemeAdjuster         can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster     can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, 1.0f,
                schemeAdjuster, plotParamsAdjuster, null);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @param schemeAdjuster         can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster     can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster)
    {
        return getPlot(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, fsr,
                schemeAdjuster, plotParamsAdjuster, null);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot2D> postPlotCreationAdjuster)
    {
        return getPlot(new WhiteScheme(), xLabel, yLabel, pDRM, xNoTicks, yNoTicks, null,
                null, 1.0f, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot2D> postPlotCreationAdjuster)
    {
        return getPlot(new WhiteScheme(), xLabel, yLabel, pDRM, xNoTicks, yNoTicks, null,
                null, fsr, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }


    /**
     * Creates a simple 2D plot. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot2D> postPlotCreationAdjuster)
    {
        return getPlot(new WhiteScheme(), xLabel, yLabel, pDRM, xNoTicks, yNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, fsr, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(AbstractScheme scheme,
                                 String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 float fsr)
    {
        return getPlot(scheme, xLabel, yLabel, pDRM, xNoTicks, yNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, fsr, null, null, null);
    }

    /**
     * Creates a simple 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot2D getPlot(AbstractScheme scheme,
                                 String xLabel,
                                 String yLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 String xAxisTicksLabelsFormat,
                                 String yAxisTicksLabelsFormat,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot2D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot2D> postPlotCreationAdjuster)
    {
        Plot2D.Params pP = new Plot2D.Params();
        AbstractFactory.performCommonParameterization2D(pP, scheme, xLabel, yLabel, pDRM, fsr, schemeAdjuster, plotParamsAdjuster);
        Plot2D plot2D = new Plot2D(pP);
        AbstractFactory.adjustAxes2D(plot2D, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, xNoTicks, yNoTicks);
        AbstractFactory.adjustNoMainAndAuxGridLines2D(plot2D, yNoTicks, xNoTicks, 2 * yNoTicks - 1, 2 * xNoTicks - 1);
        if (postPlotCreationAdjuster != null) postPlotCreationAdjuster.adjust(plot2D);
        return plot2D;
    }
}
