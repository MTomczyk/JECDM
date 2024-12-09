package t1_10.t1_visualization_module.t7_top_level_components.shared;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import plot.Plot2D;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;

/**
 * Provides factory methods for constructing plot instances.
 *
 * @author MTomczyk
 */
public class Plot
{
    /**
     * This method creates a simple 2D plot that will be used to illustrate data points drawn from the Gaussian
     * distribution and colored based on their distances to the [0, 0] point.
     *
     * @return plot instance
     */
    public static Plot2D getPlot2D_ForGaussianDistribution()
    {
        Plot2D.Params pP = new Plot2D.Params();

        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true);

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        Gradient gradient = Gradient.getViridisGradient();
        ITicksDataGetter tdg = new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 10);

        pP._colorbar = new Colorbar(gradient, "Distance", tdg);
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        return new Plot2D(pP);
    }

    /**
     * This method creates a simple 3D plot that will be used to illustrate data points drawn from the Gaussian
     * distribution and colored based on their distances to the [0, 0, 0] point.
     *
     * @return plot instance
     */
    public static Plot3D getPlot3D_ForGaussianDistribution()
    {
        Plot3D.Params pP = new Plot3D.Params();

        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true);

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";
        pP._zAxisTitle = "Z-coordinate";

        pP._scheme = WhiteScheme.getForPlot3D(0.25f);

        Gradient gradient = Gradient.getViridisGradient();
        ITicksDataGetter tdg = new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 10);

        pP._colorbar = new Colorbar(gradient, "Distance", tdg);

        Plot3D plot3D = new Plot3D(pP);

        return plot3D;
    }
}
