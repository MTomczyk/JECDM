package t1_10.t1_visualization_module.t4_parallel_coordinate_plot_2d.t2;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;

import java.util.ArrayList;


/**
 * This tutorial focuses on creating and displaying a basic 2D parallel coordinate plot.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(3);

        pP._xAxisTitle = "Objectives";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._axesTitles = new String[]{"D1", "D2", "D3"};
        pP._scheme = new WhiteScheme();
        pP._drawLegend = true;

        // Use a customized white scheme
        pP._scheme = WhiteScheme.getForPCP2D();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(3, Range.getNormalRange(), true);

        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);


        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 600, 600);

        // Create two data sets
        ArrayList<IDataSet> dataSets = new ArrayList<>();
        {
            double[][] data = new double[][]{{0.0d, 0.0d, 0.0d}, {0.15d, 0.25d, 0.4d}, {0.3d, 0.2d, 0.1d}};
            LineStyle ls = new LineStyle(0.5f, Color.BLACK);
            LineStyle mes = new LineStyle(0.5f, Color.BLACK);
            MarkerStyle ms = new MarkerStyle(2.0f, Color.RED, Marker.SQUARE, mes);
            dataSets.add(DataSet.getForParallelCoordinatePlot2D("DS1", 3, data, ms, ls));
        }
        {
            double[][] data = new double[][]{{0.6d, 0.7d, 0.8d}, {0.7d, 0.4d, 0.6d}, {0.5d, 0.6d, 0.7d}};
            LineStyle ls = new LineStyle(0.5f, Color.BLUE);
            MarkerStyle ms = new MarkerStyle(2.0f, Color.RED, Marker.CIRCLE);
            dataSets.add(DataSet.getForParallelCoordinatePlot2D("DS2", 3, data, ms, ls));
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        plot.getModel().setDataSets(dataSets, true);
        frame.setVisible(true);

    }
}
