package t1_10.t1_visualization_module.t1_plot2d.t2;

import color.gradient.Color;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import space.Range;

/**
 * This tutorial provides basics on creating and uploading data sets (fixed ranges).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        // Create a default display ranges manager (params container) for plot 2D
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();

        // Fix both display ranges (for X and Y axis) and prohibit their dynamic update (overwrite the array elements)
        // X-axis
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(1.0d, 2.0d), false);
        // Y-axis
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false);

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        Plot2D plot = new Plot2D(pP);


        Frame frame = new Frame(plot, 0.5f);

        // Alternatively, the following constructor can be used to specify the initial frame size to 600x600 pixels.
        //Frame frame = new Frame(plot, 600, 600);

        // Special case (extra hint): when no data sets are provided, the plot is in a particular state where there was
        // no call for creating data structures used to make axes tick labels. Thus, they are not displayed. One of the
        // below lines can be used to resolve this situation. The first one (preferred solution) explicitly triggers
        // ``display ranges changed'' listeners. The AbstractAxis (component.axis.swing.AbstractAxis) is one of
        // the such listeners. When called, it builds internal data structures that allow tick label rendering.
        // Another solution is to call for a plot data update but with a nulled input data set (see the next line).
        // The second argument says that the display ranges should be updated, ultimately triggering the ``display
        // ranges changed'' listeners.
        // frame.getModel().getPlotsWrapper().getModel().notifyDisplayRangesChangedListeners();
        // plot.getModel().setDataSet(null, true);

        // The data sets can be provided after the frame is created.
        // Let's create the raw data first (each row is one data point). The first column is the X-attribute, while the
        // second is Y.
        double[][] data = new double[][]
                {
                        {1.0d, 0.0},
                        {1.5d, 0.5d},
                        {2.0d, 1.0d},
                        {2.5d, 1.5d} // will not be visible
                };

        // The below line creates a marker style. This tutorial assumes that each data point is drawn using red dots
        // (Marker.CIRCLE). The size of dots is set to 5% of the minimum of the plot width and height (in pixels; note
        // that the size is implementation-dependent).
        MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE);

        // The following two lines create the test data set and then upload it to the plot.
        DataSet dataSet = DataSet.getFor2D("Test data set", data, ms);
        plot.getModel().setDataSet(dataSet, true);

        // The API for JECDM 1.0 allows adding simple popup menus triggered when right-clicking on the plot. The
        // following three lines create a popup menu, add a menu item that opens a save dialogue box (which allows
        // saving the plot as an image), and register the popup menu.
        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);


        frame.setVisible(true);
    }
}
