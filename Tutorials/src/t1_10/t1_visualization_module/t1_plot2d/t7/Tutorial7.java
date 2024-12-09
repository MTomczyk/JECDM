package t1_10.t1_visualization_module.t1_plot2d.t7;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;

/**
 * This tutorial provides basics on creating and uploading data sets (multiple data sets and legend).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial7
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

        // The following flag has to be set to true to enable legend rendering. Note that the legend entries will be
        // automatically specified based on the data sets being maintained by the plot.
        pP._drawLegend = true;

        // The legend alignment can be adjusted via the scheme object. Note that the "EXTERNAL" alignments (e.g.,
        // Align.RIGHT_TOP_EXTERNAL) will place the legend outside the plot, which may require adjusting plot margins.
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.CENTER_TOP);

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        // The data sets should be stored in an array list
        ArrayList<IDataSet> dataSets = new ArrayList<>(2);

        { // The following block creates the first data set (blue line)
            // blue line
            double[][] data = new double[][]
                    {
                            {0.0d, 1.0d},
                            {1.0d, 0.0d},
                    };
            LineStyle ls = new LineStyle(1.0f, Color.BLUE);
            dataSets.add(DataSet.getFor2D("DS1", data, ls));
        }
        { // The following block creates the second data set (red dots)
            // red points
            double[][] data = new double[][]
                    {
                            {0.0d, 0.0d},
                            {0.5d, 0.5d},
                            {1.0d, 1.0d}
                    };
            // Line style can also be used as a marker edge style (see lines below).
            LineStyle mes = new LineStyle(1.0f, Color.BLACK);
            MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE, mes);
            dataSets.add(DataSet.getFor2D("DS2", data, ms));
        }

        plot.getModel().setDataSets(dataSets, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
