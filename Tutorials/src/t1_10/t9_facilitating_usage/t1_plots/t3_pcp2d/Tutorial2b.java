package t1_10.t9_facilitating_usage.t1_plots.t3_pcp2d;

import color.Color;
import drmanager.DRMPFactory;
import frame.Frame;
import plot.PCP2DFactory;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import scheme.enums.ColorFields;

/**
 * This tutorial showcases how to use {@link PCP2DFactory} to quickly instantiate a simple 2D parallel coordinates plot.
 *
 * @author MTomczyk
 */
public class Tutorial2b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Creates a simple PCP2D
        // Important note: the method makes the background transparent, sets the font of text-related components to Times
        // New Roman (by default), sets the axes tick labels formatting to decimal, and instantiates the scheme as white.
        ParallelCoordinatePlot2D PCP2D = PCP2DFactory.getPlot(
                "f", // X-axis label
                new String[]{"1", "2", "3"}, // Y-axes labels
                // sets the upper limits for Y-axes (display ranges fixed at [0, limits] intervals):
                DRMPFactory.getForParallelCoordinatePlot2D(new double[]{1.0d, 2.0d, 4.0d}),
                new int[]{5, 2, 3}, // sets the no. ticks for Y-axes
                new String[]{"0.0", "0.000", "0.00"}, // sets the Y-axes ticks labels formatting,
                1.5f, // rescales the font size of text-related components by 1.5 (increased by 50%)
                scheme -> scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE), // sets the background color to white
                null
        );

        Frame frame = new Frame(PCP2D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        PCP2D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
