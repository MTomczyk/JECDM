package t1_10.t1_visualization_module.t2_plot3d.t1;

import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;

/**
 * This tutorial focuses on creating and displaying a basic 3D plot.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1a
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        //Create the params container for 3D plot.
        Plot3D.Params pP = new Plot3D.Params();

        // Create a default display ranges manager (params container) for plot 3D.
        // The first display ranges are reserved for the X, Y, and Z axes, respectively.
        // The known ranges are set to null but updated dynamically each time a new dataset is uploaded.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        pP._title = "Tutorial 1";
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = null; // the label will not be displayed
        pP._zAxisTitle = "Z-axis";

        // This method returns a white scheme that is suitably customized for plot 3d (no margins, no drawing area border)
        pP._scheme = WhiteScheme.getForPlot3D();

        // Create the plot object:
        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._title = "Window title";
        Frame frame = new Frame(pF);
        frame.setVisible(true);
    }
}
