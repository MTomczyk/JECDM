package plot;

import component.axis.gl.Axis3D;
import component.drawingarea.DrawingArea2D;
import component.drawingarea.DrawingArea3D;
import component.pane.Pane;
import drmanager.DisplayRangesManager;
import plot.heatmap.Heatmap2D;
import plot.heatmap.Heatmap3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import scheme.AbstractScheme;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Provides various plot parameterization methods.
 *
 * @author MTomczyk
 */
abstract class AbstractFactory
{
    /**
     * Performs a default parameterization of the plot (its params container) and its scheme. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the exes labels number format to decimal.
     *
     * @param pP     plot's params container to be adjusted
     * @param scheme scheme to be used
     * @param xLabel X-axis label
     * @param yLabel Y-axis label
     * @param pDRM   display ranges manager params container
     * @param fsr    rescaling factor used for all font-based objects
     * @param <T>    params container class
     */
    private static <T extends AbstractPlot.Params> void performCommonParameterization(T pP, AbstractScheme scheme,
                                                                                      String xLabel,
                                                                                      String yLabel,
                                                                                      DisplayRangesManager.Params pDRM,
                                                                                      float fsr)
    {
        pP._scheme = scheme;
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme._colors.put(ColorFields.PLOT_BACKGROUND, null);
        pP._xAxisTitle = xLabel;
        pP._yAxisTitle = yLabel;
        pP._pDisplayRangesManager = pDRM;

        scheme.rescale(fsr, SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.TITLE_FONT_SIZE_FIXED);

        scheme.rescale(fsr, SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_FIXED);
        scheme.rescale(fsr, SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_FIXED);

        scheme.rescale(fsr, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.LEGEND_ENTRY_FONT_SIZE_FIXED);
    }

    /**
     * Performs common parameterization (2D plots)
     *
     * @param scheme scheme to be used
     * @param fsr    rescaling factor used for all font-based objects
     */
    private static void performCommonParameterization2D(AbstractScheme scheme, float fsr)
    {
        scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.05f);
        scheme._sizes.put(SizeFields.MARGIN_TOP_SIZE_FIXED, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.05f);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_SIZE_FIXED, 0.0f);

        scheme.rescale(fsr, SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_FIXED);
        scheme.rescale(fsr, SizeFields.AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.AXIS_X_TITLE_FONT_SIZE_FIXED);

        scheme.rescale(fsr, SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_FIXED);
        scheme.rescale(fsr, SizeFields.AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
        scheme.rescale(fsr, SizeFields.AXIS_Y_TITLE_FONT_SIZE_FIXED);
    }

    /**
     * Performs a default parameterization of the plot (its params container) and its scheme. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the exes labels number format to decimal.
     *
     * @param pP                 plot's params container to be adjusted
     * @param scheme             scheme to be used
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param pDRM               display ranges manager params container
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on fly (can be null)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on fly (can be null)
     * @param <T>                params container class
     */
    protected static <T extends Plot2D.Params> void performCommonParameterization2D(T pP,
                                                                                    AbstractScheme scheme,
                                                                                    String xLabel,
                                                                                    String yLabel,
                                                                                    DisplayRangesManager.Params pDRM,
                                                                                    float fsr,
                                                                                    ISchemeAdjuster schemeAdjuster,
                                                                                    IPlotParamsAdjuster<T> plotParamsAdjuster)
    {
        performCommonParameterization(pP, scheme, xLabel, yLabel, pDRM, fsr);
        performCommonParameterization2D(scheme, fsr);
        if (schemeAdjuster != null) schemeAdjuster.adjust(scheme);
        if (plotParamsAdjuster != null) plotParamsAdjuster.adjust(pP);
    }


    /**
     * Performs a default parameterization of the plot (its params container) and its scheme. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the exes labels number format to decimal.
     *
     * @param pP                 plot's params container to be adjusted
     * @param scheme             scheme to be used
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param pDRM               display ranges manager params container
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on fly (can be null)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on fly (can be null)
     * @param <T>                params container class
     */
    protected static <T extends Heatmap2D.Params> void performCommonParameterizationHeatmap2D(T pP,
                                                                                              AbstractScheme scheme,
                                                                                              String xLabel,
                                                                                              String yLabel,
                                                                                              DisplayRangesManager.Params pDRM,
                                                                                              float fsr,
                                                                                              ISchemeAdjuster schemeAdjuster,
                                                                                              IPlotParamsAdjuster<T> plotParamsAdjuster)
    {
        performCommonParameterization(pP, scheme, xLabel, yLabel, pDRM, fsr);
        performCommonParameterization2D(scheme, fsr);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        if (schemeAdjuster != null) schemeAdjuster.adjust(scheme);
        if (plotParamsAdjuster != null) plotParamsAdjuster.adjust(pP);
    }


    /**
     * Performs a default parameterization of the parallel coordinate plot (its params container) and its scheme. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent,
     * - sets the exes labels number format to decimal.
     *
     * @param pP                 plot's params container to be adjusted
     * @param scheme             scheme to be used
     * @param xLabel             X-axis label
     * @param yLabels            Y-axes labels
     * @param pDRM               display ranges manager params container
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on fly (can be null)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on fly (can be null)
     * @param <T>                params container class
     */
    protected static <T extends ParallelCoordinatePlot2D.Params> void performCommonParameterizationPCP2D(T pP,
                                                                                                         AbstractScheme scheme,
                                                                                                         String xLabel,
                                                                                                         String[] yLabels,
                                                                                                         DisplayRangesManager.Params pDRM,
                                                                                                         float fsr,
                                                                                                         ISchemeAdjuster schemeAdjuster,
                                                                                                         IPlotParamsAdjuster<T> plotParamsAdjuster)
    {
        performCommonParameterization(pP, scheme, xLabel, null, pDRM, fsr);
        performCommonParameterization2D(scheme, fsr);
        pP._axesTitles = yLabels;
        if (schemeAdjuster != null) schemeAdjuster.adjust(scheme);
        if (plotParamsAdjuster != null) plotParamsAdjuster.adjust(pP);
    }


    /**
     * Auxiliary method that adjusts ticks labels formats and the gridlines of 2D Plots (sets the label formats to decimal, by default).
     *
     * @param plot                   reference to the plot
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param xNoTicks               the number of ticks for the X-axis
     * @param yNoTicks               the number of ticks for the Y-axis
     */
    protected static void adjustAxes2D(Plot2D plot, String xAxisTicksLabelsFormat, String yAxisTicksLabelsFormat,
                                       int xNoTicks, int yNoTicks)
    {
        AbstractFactory.adjustXAxis2D(plot, xAxisTicksLabelsFormat != null ?
                new DecimalFormat(xAxisTicksLabelsFormat) : new DecimalFormat(), xNoTicks);
        AbstractFactory.adjustYAxis2D(plot, yAxisTicksLabelsFormat != null ?
                new DecimalFormat(yAxisTicksLabelsFormat) : new DecimalFormat(), yNoTicks);
    }

    /**
     * Auxiliary method that adjusts ticks labels formats and the gridlines of 2D Plots (sets the label formats to decimal, by default).
     *
     * @param plot                    reference to the plot
     * @param yAxesTicksLabelsFormats string pattern for {@link DecimalFormat} used in ticks labels of Y-axes (each
     *                                for each Y-axis, 1:1 mapping; null if not used)
     * @param yNoTicks                adjusts the number of ticks for the Y-axes (each for each Y-axis, 1:1 mapping)
     * @param dimensions              the number of dimensions (the number of parallel vertical axes)
     */
    protected static void adjustAxesPCP2D(ParallelCoordinatePlot2D plot, String[] yAxesTicksLabelsFormats, int[] yNoTicks, int dimensions)
    {
        for (int i = 0; i < dimensions; i++)
        {
            DecimalFormat df;
            if ((yAxesTicksLabelsFormats != null) && (yAxesTicksLabelsFormats.length > i) &&
                    (yAxesTicksLabelsFormats[i] != null)) df = new DecimalFormat(yAxesTicksLabelsFormats[i]);
            else df = new DecimalFormat();
            if ((yNoTicks != null) && (yNoTicks.length > i)) adjustAxis2D(plot, df, yNoTicks[i], i + 1);
        }

    }

    /**
     * Auxiliary method for customizing the X axis of 2D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     */
    protected static void adjustXAxis2D(Plot2D plot, NumberFormat numberFormat, int noTicks)
    {
        adjustAxis2D(plot, numberFormat, noTicks, 0);
    }


    /**
     * Auxiliary method for customizing the Y axis of 2D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     */
    protected static void adjustYAxis2D(Plot2D plot, NumberFormat numberFormat, int noTicks)
    {
        adjustAxis2D(plot, numberFormat, noTicks, 1);
    }

    /**
     * Auxiliary method for customizing the X and Y axes of 2D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     * @param axisID       axis index (0 = X; 1 = Y)
     */
    private static void adjustAxis2D(Plot2D plot, NumberFormat numberFormat, int noTicks, int axisID)
    {
        if (plot == null) return;

        if (plot.getComponentsContainer().getAxes() != null)
        {
            if (plot.getComponentsContainer().getAxes()[axisID] != null)
            {
                plot.getComponentsContainer().getAxes()[axisID].getTicksDataGetter().setNumberFormat(numberFormat);
                plot.getComponentsContainer().getAxes()[axisID].getTicksDataGetter().setNoTicks(noTicks);
            }
        }
    }


    /**
     * Auxiliary method that adjusts the number of main and auxiliary grid lines (for 2D plots).
     *
     * @param plot reference to the plot
     * @param mH   the number of horizontal main grid lines
     * @param mV   the number of vertical main frid lines
     * @param aH   the number of horizontal auxiliary grid lines
     * @param aV   the number of vertical auxiliary grid lines
     */
    protected static void adjustNoMainAndAuxGridLines2D(Plot2D plot, int mH, int mV, int aH, int aV)
    {
        if (plot == null) return;
        if (plot.getComponentsContainer().getDrawingArea() == null) return;
        if (!(plot.getComponentsContainer().getDrawingArea() instanceof DrawingArea2D d2d)) return;

        if (d2d.getMainGrid() != null)
        {
            d2d.getMainGrid().getHorizontalTicksDataGetter().setNoTicks(mH);
            d2d.getMainGrid().getVerticalTicksDataGetter().setNoTicks(mV);
        }

        if (d2d.getAuxGrid() != null)
        {
            d2d.getAuxGrid().getHorizontalTicksDataGetter().setNoTicks(aH);
            d2d.getAuxGrid().getVerticalTicksDataGetter().setNoTicks(aV);
        }
    }

    /**
     * Performs common parameterization (2D plots)
     *
     * @param scheme scheme to be used
     * @param fsr    rescaling factor used for all font-based objects
     */
    protected static void performCommonParameterization3D(AbstractScheme scheme, float fsr)
    {
        scheme._colors.put(ColorFields.PLOT_BACKGROUND, null);

        scheme.rescale(fsr, SizeFields.AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE);
        scheme.rescale(fsr, SizeFields.AXIS3D_X_TITLE_FONT_SIZE_SCALE);
        scheme.rescale(fsr, SizeFields.AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE);
        scheme.rescale(fsr, SizeFields.AXIS3D_Y_TITLE_FONT_SIZE_SCALE);
        scheme.rescale(fsr, SizeFields.AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE);
        scheme.rescale(fsr, SizeFields.AXIS3D_Z_TITLE_FONT_SIZE_SCALE);

        scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_TOP_SIZE_FIXED, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_SIZE_FIXED, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_BOTTOM_SIZE_FIXED, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.0f);
        scheme._sizes.put(SizeFields.MARGIN_LEFT_SIZE_FIXED, 0.0f);
    }


    /**
     * Performs a default parameterization of the plot (its params container) and its scheme. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot),
     * - sets the exes labels number format to decimal.
     *
     * @param pP                 plot's params container to be adjusted
     * @param scheme             scheme to be used
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param zLabel             Z-axis label
     * @param pDRM               display ranges manager params container
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on fly (can be null)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on fly (can be null)
     * @param <T>                params container class
     */
    protected static <T extends Plot3D.Params> void performCommonParameterization3D(T pP,
                                                                                    AbstractScheme scheme,
                                                                                    String xLabel,
                                                                                    String yLabel,
                                                                                    String zLabel,
                                                                                    DisplayRangesManager.Params pDRM,
                                                                                    float fsr,
                                                                                    ISchemeAdjuster schemeAdjuster,
                                                                                    IPlotParamsAdjuster<T> plotParamsAdjuster)
    {
        performCommonParameterization(pP, scheme, xLabel, yLabel, pDRM, fsr);
        performCommonParameterization3D(scheme, fsr);
        pP._zAxisTitle = zLabel;
        if (schemeAdjuster != null) schemeAdjuster.adjust(scheme);
        if (plotParamsAdjuster != null) plotParamsAdjuster.adjust(pP);
    }


    /**
     * Performs a default parameterization of the plot (its params container) and its scheme. It:
     * - sets the font to Times New Roman,
     * - makes the background transparent (drawing area and plot)
     * - sets the exes labels number format to decimal.
     *
     * @param pP                 plot's params container to be adjusted
     * @param scheme             scheme to be used
     * @param xLabel             X-axis label
     * @param yLabel             Y-axis label
     * @param zLabel             Z-axis label
     * @param pDRM               display ranges manager params container
     * @param fsr                rescaling factor used for all font-based objects
     * @param schemeAdjuster     can be supplied to adjust the scheme on fly (can be null)
     * @param plotParamsAdjuster can be supplied to adjust the plot params on fly (can be null)
     * @param <T>                params container class
     */
    protected static <T extends Heatmap3D.Params> void performCommonParameterizationHeatmap3D(T pP,
                                                                                              AbstractScheme scheme,
                                                                                              String xLabel,
                                                                                              String yLabel,
                                                                                              String zLabel,
                                                                                              DisplayRangesManager.Params pDRM,
                                                                                              float fsr,
                                                                                              ISchemeAdjuster schemeAdjuster,
                                                                                              IPlotParamsAdjuster<T> plotParamsAdjuster)
    {
        performCommonParameterization(pP, scheme, xLabel, yLabel, pDRM, fsr);
        performCommonParameterization3D(scheme, fsr);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pP._zAxisTitle = zLabel;
        if (schemeAdjuster != null) schemeAdjuster.adjust(scheme);
        if (plotParamsAdjuster != null) plotParamsAdjuster.adjust(pP);
    }


    /**
     * Auxiliary method that adjusts ticks labels formats and the gridlines of 3D plots (sets the label formats to decimal, by default).
     *
     * @param plot                   reference to the plot
     * @param xAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (X-axis; null if not used)
     * @param yAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Y-axis; null if not used)
     * @param zAxisTicksLabelsFormat string pattern for {@link DecimalFormat} used in ticks labels axes (Z-axis; null if not used)
     * @param xNoTicks               the number of ticks for the X-axis
     * @param yNoTicks               the number of ticks for the Y-axis
     * @param zNoTicks               the number of ticks for the Z-axis
     */
    protected static void adjustAxes3D(Plot3D plot, String xAxisTicksLabelsFormat, String yAxisTicksLabelsFormat,
                                       String zAxisTicksLabelsFormat,
                                       int xNoTicks, int yNoTicks, int zNoTicks)
    {
        AbstractFactory.adjustXAxes3D(plot, xAxisTicksLabelsFormat != null ?
                new DecimalFormat(xAxisTicksLabelsFormat) : new DecimalFormat(), xNoTicks);
        AbstractFactory.adjustYAxes3D(plot, yAxisTicksLabelsFormat != null ?
                new DecimalFormat(yAxisTicksLabelsFormat) : new DecimalFormat(), yNoTicks);
        AbstractFactory.adjustZAxes3D(plot, zAxisTicksLabelsFormat != null ?
                new DecimalFormat(zAxisTicksLabelsFormat) : new DecimalFormat(), zNoTicks);
    }

    /**
     * Auxiliary method for customizing the X axes of 3D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     */
    protected static void adjustXAxes3D(Plot3D plot, NumberFormat numberFormat, int noTicks)
    {
        adjustAxes3D(plot, numberFormat, noTicks, 0);
    }

    /**
     * Auxiliary method for customizing the X axes of 3D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     */
    protected static void adjustYAxes3D(Plot3D plot, NumberFormat numberFormat, int noTicks)
    {
        adjustAxes3D(plot, numberFormat, noTicks, 1);
    }

    /**
     * Auxiliary method for customizing the Z axes of 3D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     */
    protected static void adjustZAxes3D(Plot3D plot, NumberFormat numberFormat, int noTicks)
    {
        adjustAxes3D(plot, numberFormat, noTicks, 2);
    }

    /**
     * Auxiliary method for customizing the X, Y, and Z axes of 2D plots.
     * Sets the
     *
     * @param plot         plot reference
     * @param numberFormat format used in tick labels
     * @param noTicks      the number of ticks
     * @param axisID       axis index (0 = X; 1 = Y, Z = 2)
     */
    private static void adjustAxes3D(Plot3D plot, NumberFormat numberFormat, int noTicks, int axisID)
    {
        if (plot == null) return;
        if (plot.getComponentsContainer().getDrawingArea() == null) return;
        if (!(plot.getComponentsContainer().getDrawingArea() instanceof DrawingArea3D d3d)) return;

        if (d3d.getAxes() != null)
        {
            for (Axis3D a : d3d.getAxes())
            {
                if (a.getAssociatedDisplayRangeID() == axisID)
                {
                    a.getTicksDataGetter().setNumberFormat(numberFormat);
                    a.getTicksDataGetter().setNoTicks(noTicks);
                    a.updateBuffers();
                }
            }
        }
    }


    /**
     * Auxiliary method that adjusts the number of main and auxiliary grid lines (for 3D plots).
     *
     * @param plot     reference to the plot
     * @param xNoTicks the number of ticks for the X-axis
     * @param yNoTicks the number of ticks for the Y-axis
     * @param zNoTicks the number of ticks for the Z-axis
     */
    protected static void adjustNoMainGridLines3D(Plot3D plot, int xNoTicks, int yNoTicks, int zNoTicks)
    {
        if (plot == null) return;
        if (plot.getComponentsContainer().getDrawingArea() == null) return;
        if (!(plot.getComponentsContainer().getDrawingArea() instanceof DrawingArea3D d3d)) return;
        if (d3d.getPanes() == null) return;
        for (Pane pane : d3d.getPanes())
        {
            if (pane == null) continue;
            if (pane.getXTicksDataGetter() != null) pane.getXTicksDataGetter().setNoTicks(xNoTicks);
            if (pane.getYTicksDataGetter() != null) pane.getYTicksDataGetter().setNoTicks(yNoTicks);
            if (pane.getZTicksDataGetter() != null) pane.getZTicksDataGetter().setNoTicks(zNoTicks);
            pane.updateBuffers();
        }
    }
}
