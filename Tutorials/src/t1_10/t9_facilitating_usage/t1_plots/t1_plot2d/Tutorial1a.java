package t1_10.t9_facilitating_usage.t1_plots.t1_plot2d;

import frame.Frame;
import plot.Plot2D;
import plot.Plot2DFactory;

/**
 * This tutorial showcases how to use {@link plot.Plot2DFactory} to quickly instantiate a simple 2D plot.
 *
 * @author MTomczyk
 */
public class Tutorial1a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Creates a simple 2D plot:
        // - sets X-axis label to "f1"
        // - sets Y-axis label to "f2"
        // - sets Z and Y-axes limits to [0, 1]
        // - rescales the font size of text-related components by 1.5 (increased by 50%)
        // Important note: the method makes the background transparent, sets the font of text-related components to Times
        // New Roman (by default), sets the axes tick labels formatting to decimal, and instantiates the scheme as white.
        Plot2D plot2D = Plot2DFactory.getPlot("f1", "f2", 1.0d, 1.5f);
        Frame frame = new Frame(plot2D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        plot2D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
