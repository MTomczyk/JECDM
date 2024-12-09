package t1_10.t1_visualization_module.t1_plot2d.t1;

import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;

/**
 * This tutorial focuses on creating and displaying a basic 2D plot. It concerns basic parameterization (but has not
 * explained how to display sets yet). Its main purpose is to give the user of JECDM a good starting point.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        //Create the params container for 2D plot.
        Plot2D.Params pP = new Plot2D.Params();

        // Create a default display ranges manager (params container) for plot 2D.
        // The known ranges are set to null but updated dynamically each time a new dataset is uploaded.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();

        // This field specifies a title to be displayed above the plot.
        // If nulled, the title is not displayed.
        pP._title = "Tutorial 1";
        //pP._title = null;

        // Can be used to provide a title for the X-axis.
        // If nulled, the title is not displayed (but does not affect ticks).
        pP._xAxisTitle = "X-axis";
        //pP._xAxisTitle = null;

        // The flag below can be false (true, by default) ti turn off displaying the X-axis (title and ticks).
        //pP._drawXAxis = false;

        // The basic parameterization for Y-axis is analogous:
        pP._yAxisTitle = "Y-axis";
        //pP._yAxisTitle = null;
        //pP._drawYAxis = false;

        // If you turn off the axes, you will be able to notice that the plot drawing area is not centered. The broader
        // left margin is to provide more space for horizontally oriented tick labels of the Y-Axis. However,
        // the visualization tool provides various customization means. A scheme object is one of the most fundamental
        // customization components (see {scheme.AbstractScheme}). It is simply a container for key-value relationships
        // that refer to various plot properties. The plot components implemented in JECDM are provided with a scheme
        // object upon the creation so that they can be customized. The code below can be used to customize the plot
        // margins. The ``relative'' term refers to the fact that margins can be dynamically determined based on the
        // percent value of plot size (min of width and height). Note that if the scheme object is not provided
        // via the constructor, it will be instantiated as scheme.WhiteScheme.

        /*pP._scheme = new WhiteScheme();
        pP._scheme._size.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._size.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._size.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._size.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.3f);*/

        // Create the plot object:
        Plot2D plot = new Plot2D(pP);

        // Create the params object for the frame. The Params class provides various static methods that can be
        // considered shortcuts. The below method creates a frame params object that is supposed to handle a
        // single plot (the first argument), and the frame size (width and height) should be set to 50% of the
        // minimum of the screen width and height (the second argument).
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        // One can specify the window title by using the below field.
        pF._title = "Window title";

        // The below line create a frame object.
        Frame frame = new Frame(pF);

        // The below piece of code can be used as a shortcut (no frame parameterization).
        //Frame frame = new Frame(plot, 0.5f);

        // Make the frame visible (Java Swing API)
        frame.setVisible(true);

        // You can also specify a new scheme (or adjust the maintained one) after the frame creation. It can be achieved
        // by calling one of the ``frame.updateScheme'' methods. See the following code that changes the scheme to black.

        /*AbstractScheme scheme = new BlackScheme();
        scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        scheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        frame.updateScheme(scheme);*/
    }
}
