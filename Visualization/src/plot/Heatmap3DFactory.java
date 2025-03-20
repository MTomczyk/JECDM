package plot;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import plot.heatmap.Heatmap3D;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import space.Range;

import java.text.DecimalFormat;

/**
 * Provides various means for quickly instantiating {@link plot.heatmap.Heatmap3D} objects.
 *
 * @author MTomczyk
 */
public class Heatmap3DFactory
{
    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel        X-axis label
     * @param yLabel        Y-axis label
     * @param zLabel        Z-axis label
     * @param div           discretization level for the X and Y-axis
     * @param heatmapRange  data range for the display range associated with the heatmap data (disables the dynamic update)
     * @param gradient      gradient used to color heatmap data
     * @param colorbarTitle colorbarTitle
     * @param fsr           rescaling factor used for all font-based objects
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, 5, null,
                div, heatmapRange, gradient, colorbarTitle, fsr);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param zLabel                Z-axis label
     * @param noTicks               adjusts the number of ticks for the X and Y-axis
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not used)
     * @param div                   discretization level for the X and Y-axis
     * @param heatmapRange          data range for the display range associated with the heatmap data (disables the dynamic update)
     * @param gradient              gradient used to color heatmap data
     * @param colorbarTitle         colorbarTitle
     * @param fsr                   rescaling factor used for all font-based objects
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         int noTicks,
                                         String axesTicksLabelsFormat,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, DRMPFactory.getFor3D(), noTicks, axesTicksLabelsFormat,
                div, heatmapRange, gradient, colorbarTitle, fsr);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel        X-axis label
     * @param yLabel        Y-axis label
     * @param zLabel        Z-axis label
     * @param pDRM          display ranges manager params container
     * @param div           discretization level for the X and Y-axis
     * @param heatmapRange  data range for the display range associated with the heatmap data (disables the dynamic update)
     * @param gradient      gradient used to color heatmap data
     * @param colorbarTitle colorbarTitle
     * @param fsr           rescaling factor used for all font-based objects
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, pDRM, 5, null, div, heatmapRange, gradient, colorbarTitle, fsr, null);
    }


    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param zLabel                Z-axis label
     * @param pDRM                  display ranges manager params container
     * @param noTicks               adjusts the number of ticks for the X and Y-axis
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not used)
     * @param div                   discretization level for the X and Y-axis
     * @param heatmapRange          data range for the display range associated with the heatmap data (disables the dynamic update)
     * @param gradient              gradient used to color heatmap data
     * @param colorbarTitle         colorbarTitle
     * @param fsr                   rescaling factor used for all font-based objects
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int noTicks,
                                         String axesTicksLabelsFormat,
                                         int div,
                                         Range heatmapRange,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, pDRM, noTicks, axesTicksLabelsFormat, div, heatmapRange,
                gradient, colorbarTitle, fsr, null);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                X-axis label
     * @param yLabel                Y-axis label
     * @param zLabel                Z-axis label
     * @param pDRM                  display ranges manager params container
     * @param noTicks               adjusts the number of ticks for the X and Y-axis
     * @param axesTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (null if not used)
     * @param div                   discretization level for the X and Y-axis
     * @param heatmapRange          data range for the display range associated with the heatmap data (disables the dynamic update)
     * @param gradient              gradient used to color heatmap data
     * @param colorbarTitle         colorbarTitle
     * @param fsr                   rescaling factor used for all font-based objects
     * @param schemeAdjuster        can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                              of the customization of the plot params container (after all fields are set by default)
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
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
        return getHeatmap3D(xLabel, yLabel, zLabel, pDRM, noTicks, noTicks, noTicks, axesTicksLabelsFormat,
                axesTicksLabelsFormat, axesTicksLabelsFormat, div, div, div,
                new DisplayRangesManager.DisplayRange(heatmapRange, false),
                gradient, colorbarTitle, fsr, schemeAdjuster, null, null);
    }


    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding horizontal grid lines
     * @param xDiv                   discretization level for the X-axis
     * @param yDiv                   discretization level for the Y-axis
     * @param zDiv                   discretization level for the Z-axis
     * @param heatmapDR              display range associated with the heatmap data
     * @param gradient               gradient used to color heatmap data
     * @param colorbarTitle          colorbarTitle
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int zNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String zAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         int zDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, xDiv, yDiv, zDiv, heatmapDR, gradient,
                colorbarTitle, fsr, null, null, null);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param zDiv                     discretization level for the Z-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
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
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int zNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String zAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         int zDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap3D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap3D> postPlotCreationAdjuster)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks, 5, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, null, xDiv, yDiv, zDiv, heatmapDR, gradient, colorbarTitle, fsr,
                schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                 X-axis label
     * @param yLabel                 Y-axis label
     * @param zLabel                 Z-axis label
     * @param pDRM                   display ranges manager params container
     * @param xNoTicks               adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks               adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks               adjusts the number of ticks for the Z-axis and the number of corresponding horizontal grid lines
     * @param cNoTicks               adjusts the number of ticks for the colorbar-axis and the number of corresponding horizontal grid lines
     * @param xDiv                   discretization level for the X-axis
     * @param yDiv                   discretization level for the Y-axis
     * @param zDiv                   discretization level for the Z-axis     *
     * @param heatmapDR              display range associated with the heatmap data
     * @param gradient               gradient used to color heatmap data
     * @param colorbarTitle          colorbarTitle
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param cAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (colorbar-axis; null if not used)
     * @param fsr                    rescaling factor used for all font-based objects
     * @param schemeAdjuster         can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster     can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                               of the customization of the plot params container (after all fields are set by default)
     * @return instantiated heatmap 3D
     */
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int zNoTicks,
                                         int cNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String zAxisTicksLabelsFormat,
                                         String cAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         int zDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap3D.Params> plotParamsAdjuster)
    {
        return getHeatmap3D(xLabel, yLabel, zLabel, pDRM, xNoTicks, yNoTicks, zNoTicks, cNoTicks, xAxisTicksLabelsFormat,
                yAxisTicksLabelsFormat, zAxisTicksLabelsFormat, cAxisTicksLabelsFormat, xDiv, yDiv, zDiv, heatmapDR, gradient,
                colorbarTitle, fsr, schemeAdjuster, plotParamsAdjuster, null);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding horizontal grid lines
     * @param cNoTicks                 adjusts the number of ticks for the colorbar-axis and the number of corresponding horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param zDiv                     discretization level for the Z-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param cAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (colorbar-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated heatmap 3D
     */
    @SuppressWarnings("DuplicatedCode")
    public static Heatmap3D getHeatmap3D(String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int zNoTicks,
                                         int cNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String zAxisTicksLabelsFormat,
                                         String cAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         int zDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap3D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap3D> postPlotCreationAdjuster)
    {
        return getHeatmap3D(WhiteScheme.getForHeatmap3D(0.25f), xLabel, yLabel, zLabel, pDRM,
                xNoTicks, yNoTicks, zNoTicks, cNoTicks, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, zAxisTicksLabelsFormat,
                cAxisTicksLabelsFormat, xDiv, yDiv, zDiv, heatmapDR, gradient, colorbarTitle, fsr, schemeAdjuster, plotParamsAdjuster,
                postPlotCreationAdjuster);
    }

    /**
     * Creates a simple 3D heatmap. It:
     * - uses a white scheme,
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - instantiates the colorbar based on the heatmap display range
     * - sets the axes and colorbar labels number format to decimal.
     *
     * @param scheme                   scheme used to instantiate the plot
     * @param xLabel                   X-axis label
     * @param yLabel                   Y-axis label
     * @param zLabel                   Z-axis label
     * @param pDRM                     display ranges manager params container
     * @param xNoTicks                 adjusts the number of ticks for the X-axis and the number of corresponding vertical grid lines
     * @param yNoTicks                 adjusts the number of ticks for the Y-axis and the number of corresponding horizontal grid lines
     * @param zNoTicks                 adjusts the number of ticks for the Z-axis and the number of corresponding horizontal grid lines
     * @param cNoTicks                 adjusts the number of ticks for the colorbar-axis and the number of corresponding horizontal grid lines
     * @param xDiv                     discretization level for the X-axis
     * @param yDiv                     discretization level for the Y-axis
     * @param zDiv                     discretization level for the Z-axis
     * @param heatmapDR                display range associated with the heatmap data
     * @param gradient                 gradient used to color heatmap data
     * @param colorbarTitle            colorbarTitle
     * @param xAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param cAxisTicksLabelsFormat   string pattern for {@link DecimalFormat} used in ticks labels axes (colorbar-axis; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated heatmap 3D
     */
    @SuppressWarnings("DuplicatedCode")
    public static Heatmap3D getHeatmap3D(AbstractScheme scheme,
                                         String xLabel,
                                         String yLabel,
                                         String zLabel,
                                         DisplayRangesManager.Params pDRM,
                                         int xNoTicks,
                                         int yNoTicks,
                                         int zNoTicks,
                                         int cNoTicks,
                                         String xAxisTicksLabelsFormat,
                                         String yAxisTicksLabelsFormat,
                                         String zAxisTicksLabelsFormat,
                                         String cAxisTicksLabelsFormat,
                                         int xDiv,
                                         int yDiv,
                                         int zDiv,
                                         DisplayRangesManager.DisplayRange heatmapDR,
                                         Gradient gradient,
                                         String colorbarTitle,
                                         float fsr,
                                         ISchemeAdjuster schemeAdjuster,
                                         IPlotParamsAdjuster<Heatmap3D.Params> plotParamsAdjuster,
                                         IPostPlotCreationAdjuster<Heatmap3D> postPlotCreationAdjuster)
    {
        Heatmap3D.Params pP = new Heatmap3D.Params();
        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._zDiv = zDiv;
        pP._heatmapDisplayRange = heatmapDR;
        pP._gradient = gradient;
        pP._colorbar = new Colorbar(gradient, colorbarTitle, new FromDisplayRange(heatmapDR, cNoTicks));
        if (cAxisTicksLabelsFormat == null)
            pP._colorbar.getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat());
        else pP._colorbar.getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat(cAxisTicksLabelsFormat));

        pP._useAlphaChannel = true;

        AbstractFactory.performCommonParameterizationHeatmap3D(pP, scheme, xLabel, yLabel, zLabel, pDRM, fsr,
                schemeAdjuster, plotParamsAdjuster);
        Heatmap3D heatmap3D = new Heatmap3D(pP);

        AbstractFactory.adjustAxes3D(heatmap3D, xAxisTicksLabelsFormat, yAxisTicksLabelsFormat, zAxisTicksLabelsFormat,
                xNoTicks, yNoTicks, zNoTicks);
        if (postPlotCreationAdjuster != null) postPlotCreationAdjuster.adjust(heatmap3D);
        return heatmap3D;
    }
}
