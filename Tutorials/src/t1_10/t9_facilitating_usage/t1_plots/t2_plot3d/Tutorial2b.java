package t1_10.t9_facilitating_usage.t1_plots.t2_plot3d;

import color.Color;
import drmanager.DRMPFactory;
import frame.Frame;
import plot.Plot3D;
import plot.Plot3DFactory;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;

/**
 * This tutorial showcases how to use {@link plot.Plot3DFactory} to quickly instantiate a simple 3D plot.
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
        // Creates a simple 3D plot.
        // Important note: the method makes the plot background transparent (and its drawing area, i.e., cube),
        // sets the font of text-related components to Times New Roman (by default), sets the axes tick labels
        // formatting to decimal, and instantiates the scheme as white.

        Plot3D plot3D = Plot3DFactory.getPlot(
                "f1", //sets X-axis label to "f1"
                "f2", //sets Y-axis label to "f2"
                "f3", //sets Z-axis label to "f3"
                DRMPFactory.getFor3D(1.0f, 2.0d, 4.0d),
                // sets display ranges as fixed at [0, 1], [0, 2], [0, 4] intervals
                5, // sets the number of X-axis ticks to 5
                10, // sets the number of Y-axis ticks to 10
                15, // sets the number of Z-axis ticks to 10
                "0.0", // formatting pattern for the X-axis tick labels
                "0.00", // formatting pattern for the Y-axis tick labels
                "0.000", // formatting pattern for the Z-axis tick labels
                1.5f, // rescales the font size of text-related components by 1.5 (increased by 50%)
                scheme -> {
                    // Adjusts the scheme object created by the method:
                    // - sets the plot background color and drawing area transparent
                    // - adjusts the right and top margins
                    // Note that the scheme customizations are applied after the default adjustments are done
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                    scheme._colors.put(ColorFields.DRAWING_AREA_BACKGROUND, Color.WHITE);
                    scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.05f);
                    scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.1f);
                }, pP -> {
                    // Adjusts the plot params container created by the method:
                    // - sets the plot title as provided
                    // Note that the plot params container customizations are applied after the default adjustments are done
                    pP._title = "Example title";
                });

        Frame frame = new Frame(plot3D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        plot3D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
