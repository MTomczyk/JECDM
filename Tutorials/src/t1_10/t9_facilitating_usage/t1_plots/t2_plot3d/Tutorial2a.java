package t1_10.t9_facilitating_usage.t1_plots.t2_plot3d;

import frame.Frame;
import plot.Plot3D;
import plot.Plot3DFactory;

/**
 * This tutorial showcases how to use {@link plot.Plot3DFactory} to quickly instantiate a simple 3D plot.
 *
 * @author MTomczyk
 */
public class Tutorial2a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Creates a simple 3D plot:
        // - sets X-axis label to "f1"
        // - sets Y-axis label to "f2"
        // - sets Z-axis label to "f3"
        // - sets X Y, and Z-axes limits to [0, 1]
        // - rescales the font size of text-related components by 1.5 (increased by 50%)
        // Important note: the method makes the plot background transparent (and its drawing area, i.e., cube),
        // sets the font of text-related components to Times New Roman (by default), sets the axes tick labels
        // formatting to decimal, and instantiates the scheme as white.
        Plot3D plot3D = Plot3DFactory.getPlot("f1", "f2", "f3", 1.0d, 1.5f);
        Frame frame = new Frame(plot3D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        plot3D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
