package t1_10.t1_visualization_module.t1_plot2d.t5;

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

import java.util.LinkedList;

/**
 * This tutorial provides basics on creating and uploading data sets (breaks in lines).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial5
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        double[][] line1 = new double[][]
                {
                        {0.0d, 0.0},
                        {0.25d, 0.25d},
                };

        double[][] line2 = new double[][]
                {
                        {0.5d, 0.5d},
                        {0.75d, 0.25d},
                        {1.0d, 1.0d},
                };

        // Raw data can be provided in chunks (double[][]) aggregated in a LinkedList. The default painter objects
        // responsible for rendering assume that nulls added to the list serve as breaking points between two chunks
        // interpreted as lines. If you comment out the ``data.add(null);'' line, the data set will be rendered without
        // the break.
        LinkedList<double[][]> data = new LinkedList<>();
        data.add(line1);
        data.add(null); // comment out this line to avoid the break
        data.add(line2);

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
