package t1_10.t9_facilitating_usage.t1_plots.t3_pcp2d;

import frame.Frame;
import plot.PCP2DFactory;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;

/**
 * This tutorial showcases how to use {@link PCP2DFactory} to quickly instantiate a simple 2D parallel coordinates plot.
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
        // Creates a simple PCP2D:
        // - sets X-axis label to "f"
        // - sets Y-axes labels to "1", "2", "3"
        // - sets the Y-axes limits to [0, 2]
        // - sets the no. ticks for Y-axes to 5
        // - sets
        // - rescales the font size of text-related components by 1.5 (increased by 50%)
        // Important note: the method makes the background transparent, sets the font of text-related components to Times
        // New Roman (by default), sets the axes tick labels formatting to decimal, and instantiates the scheme as white.
        ParallelCoordinatePlot2D PCP2D = PCP2DFactory.getPlot("f",
                new String[]{"1", "2", "3"},
                2.0f,
                5, "0.00", 1.0f
        );

        Frame frame = new Frame(PCP2D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        PCP2D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
