package tools.ivemo.heatmap.visualization;

import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import plot.AbstractPlot;
import plot.heatmap.Heatmap2D;
import plot.heatmap.Heatmap3D;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import tools.ivemo.heatmap.io.params.PlotParams;

/**
 * Provides methods for creating plot (heatmap) objects.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class PlotFactory
{
    /**
     * Creates and returns heatmap 2D (plot 2D) object.
     *
     * @param PP plot params.
     * @return heatmap 2D object
     */
    public static Heatmap2D getHeatmap2D(PlotParams PP)
    {
        // Start constructing heatmap object
        Heatmap2D.Params pH = new Heatmap2D.Params();
        doBasicParameterization(pH, PP);

        pH._xDiv = PP._xAxisDivisions;
        pH._yDiv = PP._yAxisDivisions;
        pH._verticalGridLinesWithBoxTicks = false;
        pH._horizontalGridLinesWithBoxTicks = false;
        pH._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(PP._heatmapDisplayRange, false, false);
        pH._gradient = PP._gradient;
        pH._colorbar = new Colorbar(PP._gradient, PP._heatmapTitle, new FromDisplayRange(pH._heatmapDisplayRange, 5));
        pH._xAxisWithBoxTicks = false;
        pH._yAxisWithBoxTicks = false;

        Heatmap2D h2D = new Heatmap2D(pH);
        h2D.getComponentsContainer().getMargins().getSurpassedSizes().put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);

        return h2D;
    }

    /**
     * Creates and returns heatmap 3D (plot 3D) object.
     *
     * @param PP plot params.
     * @return heatmap 3D object
     */
    public static Heatmap3D getHeatmap3D(PlotParams PP)
    {
        // Start constructing heatmap object
        Heatmap3D.Params pH = new Heatmap3D.Params();
        doBasicParameterization(pH, PP);

        pH._xDiv = PP._xAxisDivisions;
        pH._yDiv = PP._yAxisDivisions;
        pH._zDiv = PP._zAxisDivisions;
        pH._drawZAxis = true;
        pH._zAxisTitle = PP._yAxisTitle;
        pH._verticalGridLinesWithBoxTicks = false;
        pH._horizontalGridLinesWithBoxTicks = false;
        pH._depthGridLinesWithBoxTicks = false;
        pH._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(PP._heatmapDisplayRange, false, false);
        pH._gradient = PP._gradient;
        pH._colorbar = new Colorbar(PP._gradient, PP._heatmapTitle, new FromDisplayRange(pH._heatmapDisplayRange, 5));
        pH._xAxisWithBoxTicks = false;
        pH._yAxisWithBoxTicks = false;
        pH._zAxisWithBoxTicks = false;

        Heatmap3D h3D = new Heatmap3D(pH);
        h3D.getComponentsContainer().getMargins().getSurpassedSizes().put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);

        return h3D;
    }

    /**
     * Auxiliary method that performs basic parameterization that is common to both heatmaps (2D and 3D).
     * @param pp plot params to be filled
     * @param PP plot params (parser's params wrapper)
     */
    private static void doBasicParameterization(AbstractPlot.Params pp, PlotParams PP)
    {
        pp._scheme = PP._scheme.getClone(); // set scheme and customize
        pp._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.2f);
        pp._scheme._aligns.put(AlignFields.TITLE, Align.TOP);
        pp._title = PP._title;
        pp._drawXAxis = true;
        pp._xAxisTitle = PP._xAxisTitle;
        pp._drawYAxis = true;
        pp._yAxisTitle = PP._yAxisTitle;
        pp._drawMainGridlines = true;
        pp._pDisplayRangesManager = PP._pDisplayRangesManager;
    }


}
