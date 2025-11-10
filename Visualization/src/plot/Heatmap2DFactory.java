package plot;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import plot.heatmap.Heatmap2D;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import space.Range;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * Provides various means for quickly instantiating {@link plot.heatmap.Heatmap2D} objects.
 *
 * @author MTomczyk
 */
public class Heatmap2DFactory
{
    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel        X-axis label
     * @param yLabel        Y-axis label
     * @param div           discretization level for the X and Y-axis
     * @param heatmapRange  data range for the display range associated with the heatmap data (disables the dynamic
     *                      update)
     * @param gradient      gradient used to color heatmap data
     * @param colorbarTitle colorbarTitle
     * @param fsr           rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap2D(xLabel, yLabel, 5, null,
                div, heatmapRange, gradient, colorbarTitle, fsr);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param noTicks               adjusts the number of ticks for the X and Y-axis
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not
     *                              used)
     * @param div                   discretization level for the X and Y-axis
     * @param heatmapRange          data range for the display range associated with the heatmap data (disables the
     *                              dynamic update)
     * @param gradient              gradient used to color heatmap data
     * @param colorbarTitle         colorbarTitle
     * @param fsr                   rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         int noTicks,
                                         String axesTicksLabelsFormat,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap2D(xLabel, yLabel, DRMPFactory.getFor2D(), noTicks, axesTicksLabelsFormat,
                div, heatmapRange, gradient, colorbarTitle, fsr);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel        X-axis label
     * @param yLabel        Y-axis label
     * @param pDRM          display ranges manager params container
     * @param div           discretization level for the X and Y-axis
     * @param heatmapRange  data range for the display range associated with the heatmap data (disables the dynamic
     *                      update)
     * @param gradient      gradient used to color heatmap data
     * @param colorbarTitle colorbarTitle
     * @param fsr           rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap2D(xLabel, yLabel, pDRM, 5, null, div, heatmapRange, gradient, colorbarTitle, fsr, null);
    }


    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param pDRM                  display ranges manager params container
     * @param noTicks               adjusts the number of ticks for the X and Y-axis
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not
     *                              used)
     * @param div                   discretization level for the X and Y-axis
     * @param heatmapRange          data range for the display range associated with the heatmap data (disables the
     *                              dynamic update)
     * @param gradient              gradient used to color heatmap data
     * @param colorbarTitle         colorbarTitle
     * @param fsr                   rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int noTicks,
                                         String axesTicksLabelsFormat,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap2D(xLabel, yLabel, pDRM, noTicks, axesTicksLabelsFormat, div, heatmapRange, gradient, colorbarTitle, fsr, null);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param pDRM                  display ranges manager params container
     * @param noTicks               adjusts the number of ticks for the X and Y-axis
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not
     *                              used)
     * @param div                   discretization level for the X and Y-axis
     * @param heatmapRange          data range for the display range associated with the heatmap data (disables the
     *                              dynamic update)
     * @param gradient              gradient used to color heatmap data
     * @param colorbarTitle         colorbarTitle
     * @param fsr                   rescaling factor used for all font-based objects
     * @param schemeAdjuster        can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                              of the customization of the plot params container (after all fields are set by
     *                              default)
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int noTicks,
                                         String axesTicksLabelsFormat,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster)
    {
        return getHeatmap2D(xLabel, yLabel, pDRM, noTicks, noTicks, axesTicksLabelsFormat, axesTicksLabelsFormat,
                div, div, new DisplayRangesManager.DisplayRange(heatmapRange, false),
                gradient, colorbarTitle, fsr, schemeAdjuster, null, null);
    }


    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                               grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding
     *                               horizontal grid lines
     * @param xDiv                   discretization level for the X-axis
     * @param yDiv                   discretization level for the Y-axis
     * @param heatmapDR              display range associated with the heatmap data
     * @param gradient               gradient used to color heatmap data
     * @param colorbarTitle          colorbarTitle
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if
     *                               not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if
     *                               not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap2D(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat,
                xDiv, yDiv, heatmapDR, gradient, colorbarTitle, fsr, null, null, null);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null
     *                                 if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null
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
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap2D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap2D> postPlotCreationAdjuster)
    {
        return getHeatmap2D(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, 5, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, null, xDiv, yDiv, heatmapDR, gradient, colorbarTitle, fsr,
                schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical
     *                               grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding
     *                               horizontal grid lines
     * @param cNoTicks               adjusts the number of ticks for the colorbar-axis and the number of corresponding
     *                               horizontal grid lines
     * @param xDiv                   discretization level for the X-axis
     * @param yDiv                   discretization level for the Y-axis
     * @param heatmapDR              display range associated with the heatmap data
     * @param gradient               gradient used to color heatmap data
     * @param colorbarTitle          colorbarTitle
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if
     *                               not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if
     *                               not used)
     * @param cAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (colorbar-axis;
     *                               null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @param schemeAdjuster         can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by
     *                               default)
     * @param plotParamsAdjuster     can be supplied to adjust the plot params on the fly (can be null); executed at
     *                               the
     *                               end of the customization of the plot params container (after all fields are set by
     *                               default)
     * @return instantiated plot 2D
     */
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int cNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String cAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap2D.Params> plotParamsAdjuster)
    {
        return getHeatmap2D(xLabel, yLabel, pDRM, xNoTicks, yNoTicks, cNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, cAxisTicksLabelsFormat, xDiv, yDiv, heatmapDR, gradient,
                colorbarTitle, fsr, schemeAdjuster, plotParamsAdjuster, null);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param cNoTicks                 adjusts the number of ticks for the colorbar-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null
     *                                 if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null
     *                                 if not used)
     * @param cAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes
     *                                 (colorbar-axis; null if not used)
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
    @SuppressWarnings("DuplicatedCode")
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int cNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String cAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap2D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap2D> postPlotCreationAdjuster)
    {
        return getHeatmap2D(new WhiteScheme(), xLabel, yLabel, pDRM, xNoTicks, yNoTicks, cNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, cAxisTicksLabelsFormat, xDiv, yDiv, heatmapDR, gradient, colorbarTitle,
                fsr, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range, <br>
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param scheme                   scheme used to instantiate the plot
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param cNoTicks                 adjusts the number of ticks for the colorbar-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null
     *                                 if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null
     *                                 if not used)
     * @param cAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes
     *                                 (colorbar-axis; null if not used)
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
    @SuppressWarnings("DuplicatedCode")
    public static Heatmap2D getHeatmap2D(AbstractScheme scheme,
                                         String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int cNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String cAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap2D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap2D> postPlotCreationAdjuster)
    {
        return getHeatmap2D(scheme, xLabel, yLabel, pDRM, xNoTicks, yNoTicks, cNoTicks,
                xAxisTicksLabelsFormat == null ? new DecimalFormat() : new DecimalFormat(xAxisTicksLabelsFormat),
                yAxisTicksLabelsFormat == null ? new DecimalFormat() : new DecimalFormat(yAxisTicksLabelsFormat),
                cAxisTicksLabelsFormat == null ? new DecimalFormat() : new DecimalFormat(cAxisTicksLabelsFormat),
                xDiv, yDiv, heatmapDR, gradient, colorbarTitle, fsr, schemeAdjuster,
                plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param cNoTicks                 adjusts the number of ticks for the colorbar-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisNumberFormat        x-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param yAxisNumberFormat        y-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param cAxisNumberFormat        colorbar-axis number format (default {@link DecimalFormat} instance is used, if
     *                                 null)
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
    @SuppressWarnings("DuplicatedCode")
    public static Heatmap2D getHeatmap2D(String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int cNoTicks,
                                         NumberFormat xAxisNumberFormat,
                                         NumberFormat yAxisNumberFormat,
                                         NumberFormat cAxisNumberFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap2D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap2D> postPlotCreationAdjuster)
    {
        return getHeatmap2D(new WhiteScheme(), xLabel, yLabel, pDRM, xNoTicks, yNoTicks, cNoTicks,
                xAxisNumberFormat, yAxisNumberFormat, cAxisNumberFormat, xDiv, yDiv, heatmapDR, gradient,
                colorbarTitle, fsr, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 2D heatmap. It: <br>
     * - uses a white scheme, <br>
     * - sets the font to Times New Roman, <br>
     * - makes the background transparent, <br>
     * - instantiates the colorbar based on the heatmap display range.
     *
     * @param scheme                   scheme used to instantiate the plot
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding
     *                                 vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param cNoTicks                 adjusts the number of ticks for the colorbar-axis and the number of corresponding
     *                                 horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisNumberFormat        x-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param yAxisNumberFormat        y-axis number format (default {@link DecimalFormat} instance is used, if null)
     * @param cAxisNumberFormat        colorbar-axis number format (default {@link DecimalFormat} instance is used, if
     *                                 null)
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
    @SuppressWarnings("DuplicatedCode")
    public static Heatmap2D getHeatmap2D(AbstractScheme scheme,
                                         String xLabel,
                                         String yLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int cNoTicks,
                                         NumberFormat xAxisNumberFormat,
                                         NumberFormat yAxisNumberFormat,
                                         NumberFormat cAxisNumberFormat,
                                         int xDiv,
                                         int yDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap2D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap2D> postPlotCreationAdjuster)
    {
        Heatmap2D.Params pP = new Heatmap2D.Params();
        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._heatmapDisplayRange = heatmapDR;
        pP._gradient = gradient;
        pP._colorbar = new Colorbar(gradient, colorbarTitle, new FromDisplayRange(heatmapDR, cNoTicks));
        pP._colorbar.getAxis().getTicksDataGetter().setNumberFormat(Objects.requireNonNullElseGet(cAxisNumberFormat,
                DecimalFormat::new));

        AbstractFactory.performCommonParameterizationHeatmap2D(pP, scheme, xLabel, yLabel, pDRM, fsr,
                schemeAdjuster, plotParamsAdjuster);
        Heatmap2D heatmap2D = new Heatmap2D(pP);

        AbstractFactory.adjustAxes2D(heatmap2D, xAxisNumberFormat, yAxisNumberFormat, xNoTicks, yNoTicks);
        if (postPlotCreationAdjuster != null) postPlotCreationAdjuster.adjust(heatmap2D);
        return heatmap2D;
    }
}
