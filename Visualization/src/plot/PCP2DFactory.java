package plot;

import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import space.Range;

import java.text.DecimalFormat;

/**
 * Provides various means for quickly instantiating {@link plot.parallelcoordinate.ParallelCoordinatePlot2D} objects.
 *
 * @author MTomczyk
 */
public class PCP2DFactory
{
    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel   X-axis label
     * @param yLabels  Y-axis label
     * @param yLimits  common limit for the Y-axes (display ranges are fixed and set at [0, yLimit])
     * @param yNoTicks adjusts the number of ticks for the Y-axes
     * @param fsr      rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   double yLimits,
                                                   int yNoTicks,
                                                   float fsr)
    {
        return getPlot(xLabel, yLabels, yLimits, yNoTicks, null, fsr);
    }

    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                  X-axis label
     * @param yLabels                 Y-axis label
     * @param yLimits                 common limit for the Y-axes (display ranges are fixed and set at [0, yLimit])
     * @param yNoTicks                adjusts the number of ticks for the Y-axes
     * @param yAxesTicksLabelsFormats string pattern for {@link DecimalFormat} used in ticks labels of Y-axes
     * @param fsr                     rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   double yLimits,
                                                   int yNoTicks,
                                                   String yAxesTicksLabelsFormats,
                                                   float fsr)
    {
        return getPlot(WhiteScheme.getForPCP2D(), xLabel, yLabels,
                DRMPFactory.getForParallelCoordinatePlot2D(yLabels.length, Range.get0R(yLimits), false),
                yNoTicks,
                yAxesTicksLabelsFormats, fsr,
                null, null, null);
    }


    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                  X-axis label
     * @param yLabels                 Y-axis label
     * @param pDRM                    display ranges manager params container
     * @param yNoTicks                adjusts the number of ticks for the Y-axes
     * @param yAxesTicksLabelsFormats string pattern for {@link DecimalFormat} used in ticks labels of Y-axes
     * @param fsr                     rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int yNoTicks,
                                                   String yAxesTicksLabelsFormats,
                                                   float fsr)
    {
        return getPlot(WhiteScheme.getForPCP2D(), xLabel, yLabels, pDRM, yNoTicks, yAxesTicksLabelsFormats, fsr,
                null, null, null);
    }


    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabels                  Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param yNoTicks                 adjusts the number of ticks for the Y-axes
     * @param yAxesTicksLabelsFormats  string pattern for {@link DecimalFormat} used in ticks labels of Y-axes
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int yNoTicks,
                                                   String yAxesTicksLabelsFormats,
                                                   float fsr,
                                                   ISchemeAdjuster schemeAdjuster,
                                                   IPlotParamsAdjuster<ParallelCoordinatePlot2D.Params> plotParamsAdjuster,
                                                   IPostPlotCreationAdjuster<ParallelCoordinatePlot2D> postPlotCreationAdjuster)
    {
        return getPlot(WhiteScheme.getForPCP2D(), xLabel, yLabels, pDRM, yNoTicks, yAxesTicksLabelsFormats, fsr,
                schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }


    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabels                  Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param yNoTicks                 adjusts the number of ticks for the Y-axes
     * @param yAxesTicksLabelsFormats  string pattern for {@link DecimalFormat} used in ticks labels of Y-axes
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(AbstractScheme scheme,
                                                   String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int yNoTicks,
                                                   String yAxesTicksLabelsFormats,
                                                   float fsr,
                                                   ISchemeAdjuster schemeAdjuster,
                                                   IPlotParamsAdjuster<ParallelCoordinatePlot2D.Params> plotParamsAdjuster,
                                                   IPostPlotCreationAdjuster<ParallelCoordinatePlot2D> postPlotCreationAdjuster)
    {
        int[] yt = new int[yLabels.length];
        String[] yf = new String[yLabels.length];
        for (int i = 0; i < yLabels.length; i++)
        {
            yt[i] = yNoTicks;
            yf[i] = yAxesTicksLabelsFormats;
        }
        return getPlot(scheme, xLabel, yLabels, pDRM, yt, yf, fsr, schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }

    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                  X-axis label
     * @param yLabels                 Y-axis label
     * @param pDRM                    display ranges manager params container
     * @param yNoTicks                adjusts the number of ticks for the Y-axes (each for each Y-axis, 1:1 mapping)
     * @param yAxesTicksLabelsFormats string pattern for {@link DecimalFormat} used in ticks labels of Y-axes (each for each Y-axis,
     *                                1:1 mapping; null if not used)
     * @param fsr                     rescaling factor used for all font-based objects
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int[] yNoTicks,
                                                   String[] yAxesTicksLabelsFormats,
                                                   float fsr)
    {
        return getPlot(WhiteScheme.getForPCP2D(), xLabel, yLabels, pDRM, yNoTicks, yAxesTicksLabelsFormats, fsr,
                null, null, null);
    }

    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabels                  Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param yNoTicks                 adjusts the number of ticks for the Y-axes (each for each Y-axis, 1:1 mapping)
     * @param yAxesTicksLabelsFormats  string pattern for {@link DecimalFormat} used in ticks labels of Y-axes (each for each Y-axis,
     *                                 1:1 mapping; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int[] yNoTicks,
                                                   String[] yAxesTicksLabelsFormats,
                                                   float fsr,
                                                   ISchemeAdjuster schemeAdjuster,
                                                   IPlotParamsAdjuster<ParallelCoordinatePlot2D.Params> plotParamsAdjuster)
    {
        return getPlot(WhiteScheme.getForPCP2D(), xLabel, yLabels, pDRM, yNoTicks, yAxesTicksLabelsFormats, fsr,
                schemeAdjuster, plotParamsAdjuster, null);
    }


    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param xLabel                   X-axis label
     * @param yLabels                  Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param yNoTicks                 adjusts the number of ticks for the Y-axes (each for each Y-axis, 1:1 mapping)
     * @param yAxesTicksLabelsFormats  string pattern for {@link DecimalFormat} used in ticks labels of Y-axes (each for each Y-axis,
     *                                 1:1 mapping; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int[] yNoTicks,
                                                   String[] yAxesTicksLabelsFormats,
                                                   float fsr,
                                                   ISchemeAdjuster schemeAdjuster,
                                                   IPlotParamsAdjuster<ParallelCoordinatePlot2D.Params> plotParamsAdjuster,
                                                   IPostPlotCreationAdjuster<ParallelCoordinatePlot2D> postPlotCreationAdjuster)
    {
        return getPlot(WhiteScheme.getForPCP2D(), xLabel, yLabels, pDRM, yNoTicks, yAxesTicksLabelsFormats, fsr,
                schemeAdjuster, plotParamsAdjuster, postPlotCreationAdjuster);
    }


    /**
     * Creates a simple PCP 2D plot. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the axes labels number format to decimal.
     *
     * @param scheme                   plot scheme
     * @param xLabel                   X-axis label
     * @param yLabels                  Y-axis label
     * @param pDRM                     display ranges manager params container
     * @param yNoTicks                 adjusts the number of ticks for the Y-axes (each for each Y-axis, 1:1 mapping)
     * @param yAxesTicksLabelsFormats  string pattern for {@link DecimalFormat} used in ticks labels of Y-axes (each for each Y-axis,
     *                                 1:1 mapping; null if not used)
     * @param fsr                      rescaling factor used for all font-based objects
     * @param schemeAdjuster           can be supplied to adjust the scheme on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param plotParamsAdjuster       can be supplied to adjust the plot params on the fly (can be null); executed at the end
     *                                 of the customization of the plot params container (after all fields are set by default)
     * @param postPlotCreationAdjuster can be supplied to adjust the plot params on the fly (after its creation; can be null);
     *                                 executed just before returning the plot
     * @return instantiated plot 2D
     */
    public static ParallelCoordinatePlot2D getPlot(AbstractScheme scheme,
                                                   String xLabel,
                                                   String[] yLabels,
                                                   DisplayRangesManager.Params pDRM,
                                                   int[] yNoTicks,
                                                   String[] yAxesTicksLabelsFormats,
                                                   float fsr,
                                                   ISchemeAdjuster schemeAdjuster,
                                                   IPlotParamsAdjuster<ParallelCoordinatePlot2D.Params> plotParamsAdjuster,
                                                   IPostPlotCreationAdjuster<ParallelCoordinatePlot2D> postPlotCreationAdjuster)
    {
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(yLabels.length);
        AbstractFactory.performCommonParameterizationPCP2D(pP, scheme, xLabel, yLabels, pDRM, fsr, schemeAdjuster, plotParamsAdjuster);
        ParallelCoordinatePlot2D pcp = new ParallelCoordinatePlot2D(pP);
        AbstractFactory.adjustAxesPCP2D(pcp, yAxesTicksLabelsFormats, yNoTicks, yLabels.length);

        if (postPlotCreationAdjuster != null) postPlotCreationAdjuster.adjust(pcp);
        return pcp;
    }
}
