package t1_10.t1_visualization_module.t1_plot2d.t4;

import color.gradient.Color;
import dataset.DataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import space.Range;

/**
 * This tutorial provides basics on creating and uploading data sets (drawing a line).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial4
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        // Create a default display ranges manager (params container) for plot 2D with display ranges set to [0, 1]
        // intervals (dynamic updates are disabled).
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        double[][] data = new double[][]
                {
                        {0.0d, 0.0},
                        {0.25d, 0.25d},
                        {0.5d, 0.5d},
                        {0.75d, 0.25d},
                        {1.0d, 1.0d},
                };

        MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE);
        // The below line construct a line style object (line width set to 1% of minimum of plot width and height; color
        // is set to blue).
        LineStyle ls = new LineStyle(1.0f, Color.BLUE);

        // This tutorial uses a data set constructor that associates a marker and line styles with the data set.
        // Note that you can avoid providing a marker style when a line style is given (DataSet dataSet = DataSet.getFor2D
        // ("Test data set", data, ls)); the markers will not be drawn then (the line will be drawn if there are at least
        // two connected points).
        DataSet dataSet = DataSet.getFor2D("Test data set", data, ms, ls);
        plot.getModel().setDataSet(dataSet, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
