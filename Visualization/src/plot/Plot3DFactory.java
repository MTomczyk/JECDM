package plot;

import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import scheme.AbstractScheme;
import scheme.WhiteScheme;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Provides various means for quickly instantiating {@link Plot3D} objects.
 *
 * @author MTomczyk
 */
public class Plot3DFactory
{
    /**
     * Creates a simple 3D plot. It: <br>
     * - uses a white scheme, <br>
     * - sets the display ranges to fixed (non-dynamic) values: [0, xyLimit (parameter)], <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel         X-axis label
     * @param yLabel         Y-axis label
     * @param zLabel         Z-axis label
     * @param xyzLimit       upper limits for the display ranges
     * @param fsr            rescaling factor used for all font-based objects
     * @param schemeAdjuster can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                       of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 double xyzLimit,
                                 float fsr,
                                 ISchemeAdjuster schemeAdjuster)
    {
        return getPlot(WhiteScheme.getForPlot3D(), xLabel, yLabel, zLabel,
                DRMPFactory.getFor3D(xyzLimit, xyzLimit, xyzLimit),
                5, 5, 5,
                null, null, null,
                fsr, schemeAdjuster, null, null);
    }

    /**
     * Creates a simple 3D plot. It: <br>
     * - uses a white scheme, <br>
     * - sets the display ranges to fixed (non-dynamic) values: [0, xyLimit (parameter)], <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                               grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding
     *                               horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                               grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if
     *                               not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if
     *                               not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if
     *                               not used)
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param zLabel             Z-axis label
     * @param pDRM               display ranges manager params container
     * @param xNoTicks           adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                           grid lines
     * @param yNoTicks           adjusts the number of ticks for the Y-axis and the number of corresponding horizontal
     *                           grid lines
     * @param zNoTicks           adjusts the number of ticks for the Z-axis and the number of corresponding depth grid
     *                           lines
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param zLabel             Z-axis label
     * @param pDRM               display ranges manager params container
     * @param xNoTicks           adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                           grid lines
     * @param yNoTicks           adjusts the number of ticks for the Y-axis and the number of corresponding horizontal
     *                           grid lines
     * @param zNoTicks           adjusts the number of ticks for the Z-axis and the number of corresponding depth grid
     *                           lines
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                               grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding
     *                               horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                               grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if
     *                               not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if
     *                               not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if
     *                               not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @param schemeAdjuster         can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by
     *                               default)
     * @param plotParamsAdjuster     can be supplied to adjust the plot params on the fly (can be null); executed at the
     *                               end of the customization of the plot params container (after all fields are set by
     *                               default)
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null
     *                                 if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null
     *                                 if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null
     *                                 if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                 plot scheme
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                               grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding
     *                               horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                               grid lines
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if
     *                               not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if
     *                               not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if
     *                               not used)
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
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null
     *                                 if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null
     *                                 if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null
     *                                 if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
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
        return getPlot(scheme, xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, fsr, 1.0f, schemeAdjuster,
                plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null
     *                                 if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null
     *                                 if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null
     *                                 if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param lw                       adjust line width for the Cube, Axes, and Panes objects (1.0f is a default value)
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
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
                                 float lw,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        return getPlot(scheme, xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                xAxisTicksLabelsFormat == null ? new DecimalFormat() : new DecimalFormat(xAxisTicksLabelsFormat),
                yAxisTicksLabelsFormat == null ? new DecimalFormat() : new DecimalFormat(yAxisTicksLabelsFormat),
                zAxisTicksLabelsFormat == null ? new DecimalFormat() : new DecimalFormat(zAxisTicksLabelsFormat),
                fsr, lw, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot), <br>
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param xAxisNumberFormat        x-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param yAxisNumberFormat        y-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param zAxisNumberFormat        z-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param lw                       adjust line width for the Cube, Axes, and Panes objects (1.0f is a default value)
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static Plot3D getPlot(String xLabel,
                                 String yLabel,
                                 String zLabel,
                                 DisplayRangesManager.Params pDRM,
                                 int xNoTicks,
                                 int yNoTicks,
                                 int zNoTicks,
                                 NumberFormat xAxisNumberFormat,
                                 NumberFormat yAxisNumberFormat,
                                 NumberFormat zAxisNumberFormat,
                                 float fsr,
                                 float lw,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        return getPlot(new WhiteScheme(), xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks,
                xAxisNumberFormat, yAxisNumberFormat, zAxisNumberFormat, fsr, lw, schemeAdjuster, plotParamsAdjuster,
                postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D plot. It: <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent (drawing area and plot),
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding depth
     *                                 grid lines
     * @param xAxisNumberFormat        x-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param yAxisNumberFormat        y-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param zAxisNumberFormat        z-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param lw                       adjust line width for the Cube, Axes, and Panes objects (1.0f is a default value)
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the
     *                                 end of the customization of the plot params container (after all fields are set
     *                                 by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at
     *                                 the end of the customization of the plot params container (after all fields are
     *                                 set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be
     *                                 null); executed just before returning the plot
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
                                 NumberFormat xAxisNumberFormat,
                                 NumberFormat yAxisNumberFormat,
                                 NumberFormat zAxisNumberFormat,
                                 float fsr,
                                 float lw,
                                 ISchemeAdjuster schemeAdjuster,
                                 IPlotParamsAdjuster<Plot3D.Params> plotParamsAdjuster,
                                 IPostPlotCreationAdjuster<Plot3D> postPlotCreationAdjuster)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._useAlphaChannel = true;
        AbstractFactory.performCommonParameterization3D(pP, scheme, xLabel, yLabel, zLabel, pDRM, fsr, lw, schemeAdjuster, plotParamsAdjuster);
        Plot3D plot3D = new Plot3D(pP);
        AbstractFactory.adjustAxes3D(plot3D, xAxisNumberFormat, yAxisNumberFormat, zAxisNumberFormat,
                xNoTicks, yNoTicks, zNoTicks);
        AbstractFactory.adjustNoMainGridLines3D(plot3D, xNoTicks, yNoTicks, zNoTicks);
        if (postPlotCreationAdjuster != null) postPlotCreationAdjuster.adjust(plot3D);
        return plot3D;
    }
}
