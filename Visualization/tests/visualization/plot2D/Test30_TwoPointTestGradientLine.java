package visualization.plot2D;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Tests legend.
 *
 * @author MTomczyk
 */
public class Test30_TwoPointTestGradientLine
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = true;
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();

        // ======================================================
        ArrayList<IDataSet> dss1 = new ArrayList<>();
        dss1.add(DataSet.getFor2D("DS 1", new double[][]{{-1.0d, -1.0d}, {1.0d, 1.0d}},
                new LineStyle(1.0f, Gradient.getViridisGradient(), 0)));

        plot.getModel().setDataSets(dss1, true);
        frame.setVisible(true);
    }
}
