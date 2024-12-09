package visualization.parallelcoordinate2D;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import frame.Frame;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import scheme.WhiteScheme;


/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link plot.parallelcoordinate.ParallelCoordinatePlot2D}) on a frame.
 * Test parallel coordinate plot.
 *
 * Wrong painter is used (default). Expected outcome: bounds of the two vertical axes are updated, while the third is NaN.
 *
 * @author MTomczyk
 */
public class Test1_WrongPainter
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(3);
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "Objectives";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._axesTitles = new String[]{"D1", "D2", "D3"};
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = null;// DisplayRangesManager.Params.getFor2D();

        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        double [][] data = new double[][]{{0.0d, 0.0d}, {0.5d, 0.5d}, {1.0d, 1.0d}};
        IDataSet ds = DataSet.getFor2D("DS 1", data, new MarkerStyle(5, Color.RED, Marker.SQUARE));

        plot.getModel().setDataSet(ds, true);
        frame.setVisible(true);
    }
}
