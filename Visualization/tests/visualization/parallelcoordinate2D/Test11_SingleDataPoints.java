package visualization.parallelcoordinate2D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link ParallelCoordinatePlot2D}) on a frame.
 * Test parallel coordinate plot (added to see if one data point will be visible)
 *
 * @author MTomczyk
 */
public class Test11_SingleDataPoints
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int dims = 3;
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(dims);
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "Objectives";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._axesTitles = new String[dims];
        for (int i = 0; i < dims; i++) pP._axesTitles[i] = "D" + (i + 1);
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(dims, new Range(0.0d, 1.0d), false, false);

        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        ArrayList<IDataSet> DSs = new ArrayList<>(10);
        {
            double[][] data = new double[][]{{1.0d, 0.5d, 0.0d}};
            LineStyle ls = new LineStyle(2.0f, Color.RED);
            IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 1", dims, data, null, ls);
            DSs.add(ds);
        }
        {
            double[][] data = new double[][]{{0.0d, 0.5d, 1.0d}};
            LineStyle ls = new LineStyle(2.0f, Gradient.getPlasmaGradient(), 0);
            IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 2", dims, data, null, ls);
            DSs.add(ds);
        }
        {
            LinkedList<double[][]> data = new LinkedList<>();
            data.add(new double[][]{{0.25d, 0.25d, 0.25d}});
            data.add(null);
            data.add(new double[][]{{0.75d, 0.75d, 0.75d}});
            LineStyle ls = new LineStyle(2.0f, Gradient.getPlasmaGradient(), 0);
            IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 3", dims, data, null, ls);
            DSs.add(ds);
        }
        plot.getModel().setDataSets(DSs, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);


        frame.setVisible(true);

    }
}
