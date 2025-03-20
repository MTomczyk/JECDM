package t1_10.t9_facilitating_usage.t1_plots.t4_heatmap2d;

import color.Color;
import color.gradient.Gradient;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Heatmap2DFactory;
import plot.heatmap.Heatmap2D;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;

/**
 * This tutorial showcases how to use {@link Heatmap2DFactory} to quickly instantiate a simple 2D heatmap.
 *
 * @author MTomczyk
 */
public class Tutorial4b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Creates a simple 2D heatmap:
        // Important note: the method makes the background transparent, sets the font of text-related components to Times
        // New Roman (by default), sets the axes tick labels formatting to decimal, and instantiates the scheme as white.
        Heatmap2D heatmap2D = Heatmap2DFactory.getHeatmap2D(
                "f1", // sets X-axis label to "f1"
                "f2", // sets Y-axis label to "f2"
                DRMPFactory.getFor2D(1.0d, 2.0d), // sets the plot display ranges to [0, 1] and [0, 2] (fixed)
                5, //  sets the number of X-axis ticks to 5
                8,  //  sets the number of Y-axis ticks to 8
                10,   //  sets the number of colorbar-axis ticks to 10
                "0.0",  // sets the X-axis ticks labels formatting,
                "0.00", // sets the Y-axis ticks labels formatting,
                "0.000", // sets the colorbar-axis ticks labels formatting,
                5, // sets the discretization level for the X-axis to 5
                8,  // sets the discretization level for the Y-axis to 8
                // sets the display range for the heatmap bins to [0, 100] (fixed):
                new DisplayRangesManager.DisplayRange(Range.get0R(100.0f), false),
                Gradient.getViridisGradient(), // sets the gradient used to colorize the bins to Viridis
                "Value", //  sets the colorbar title to "Value"
                1.25f, // rescales the font size of text-related components by 1.5 (increased by 50%)
                scheme -> {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE); // sets the background color to white
                    scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f); // adjusts the right margin
                }, pP -> {
                    pP._drawMainGridlines = true; // draw main grid lines

                    pP._horizontalGridLinesWithBoxTicks = true; // adjusts horizontal (main) grid lines to match bins boundaries
                    pP._yAxisWithBoxTicks = true; // adjusts Y-axis ticks labels locations match bins centers

                    //pP._verticalGridLinesWithBoxTicks = false;
                    //pP._xAxisWithBoxTicks = false;
                });

        Frame frame = new Frame(heatmap2D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        heatmap2D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
