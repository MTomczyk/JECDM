package t1_10.t1_visualization_module.t1_plot2d.t6;

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

/**
 * This tutorial provides basics on creating and uploading data sets (custom display ranges mapping).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial6
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        // Create a display ranges manager (params container).
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[2];
        // The display range for the X and Y axes is set to null with a dynamic update flag turned on.
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(null, true);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(null, true);
        // The follow line constructs attribute-display range mapping. It states that the first attribute should be
        // mapped into the Y-axis (index of 1), the second attribute should be ignored (null), and the last attribute
        // should be linked to the X-axis (index of 0). Note that the indexes in the below array should be from a set
        // {0, 1,...,n}, where n denotes the number of display ranges considered. Their validity will be checked when
        // instantiating the display ranges manager (simple assertion).
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{1, null, 0};

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        // The input data points provided below are organized as follows: the first attribute that should be mapped
        // into Y-axis, the second attribute that is irrelevant, and the third attribute that should be mapped into X-axis.
        double[][] data = new double[][]
                {
                        {0.0d, 100.0d, 0.0},
                        {0.25d, -5.0d, 0.5d},
                        {0.5d, 99.0d, 0.75d},
                        {2.0d, 15.0d, 1.0d}
                };

        MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE);
        LineStyle ls = new LineStyle(1.0f, Color.BLUE);

        DataSet dataSet = DataSet.getFor2D("Test data set", data, ms, ls);
        plot.getModel().setDataSet(dataSet, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
