package t1_10.t9_facilitating_usage.t1_plots.t4_heatmap3d;

import color.gradient.Gradient;
import drmanager.DRMPFactory;
import frame.Frame;
import plot.Heatmap3DFactory;
import plot.heatmap.Heatmap3D;
import space.Range;

/**
 * This tutorial showcases how to use {@link Heatmap3DFactory} to quickly instantiate a simple 3D heatmap.
 *
 * @author MTomczyk
 */
public class Tutorial5a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Creates a simple 3D heatmap:
        // - sets X-axis label to "f1"
        // - sets Y-axis label to "f2"
        // - sets Z-axis label to "f3"
        // - sets X, Y and Z-axes limits to [0, 1] and [0, 2], [0, 4]
        // - sets the discretization level for the X, Y, and Z axes to 5
        // - sets the display range for the heatmap bins to [0, 100] (fixed)
        // - sets the gradient used to colorize the bins to Viridis
        // - sets the colorbar title to "Value"
        // - rescales the font size of text-related components by 1.5 (increased by 50%)
        // Important note: the method makes the background transparent, sets the font of text-related components to Times
        // New Roman (by default), sets the axes tick labels formatting to decimal, and instantiates the scheme as white.
        Heatmap3D heatmap3D = Heatmap3DFactory.getHeatmap3D("f1", "f2", "f3",
                DRMPFactory.getFor3D(1.0d, 2.0d, 4.0d),
                5,
                Range.get0R(100.0f),
                Gradient.getViridisGradient(), "Value", 1.5f);

        Frame frame = new Frame(heatmap3D, 0.5f); // create the frame
        // if not called, the axes label will not be displayed until some data sets are supplied
        heatmap3D.getModel().notifyDisplayRangesChangedListeners();
        // displays the plot:
        frame.setVisible(true);
    }
}
