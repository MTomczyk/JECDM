package t1_10.t1_visualization_module.t1_plot2d.t3;

import color.gradient.Color;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;

/**
 * This tutorial provides basics on creating and uploading data sets (dynamic ranges).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial3
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        // Creates a default display ranges manager (params container) for plot 2D.
        // The display ranges are initially set to null but will be updated dynamically each time a new data set is uploaded.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();


        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        double[][] data = new double[][]
                {
                        {1.0d, 0.0},
                        {1.5d, 0.5d},
                        {2.0d, 1.0d},
                        {2.5d, 1.5d}
                };

        MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE);

        DataSet dataSet = DataSet.getFor2D("Test data set", data, ms);
        plot.getModel().setDataSet(dataSet, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
