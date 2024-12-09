package visualization.plot2D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
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
 * Tests painting
 *
 * @author MTomczyk
 */
public class Test11_Animator
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
        ArrayList<IDataSet> dss = new ArrayList<>();

        {
            MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Color.BLACK));
            double[][] d = new double[][]{{-1.0d, 1.0d}, {0.0d, 1.0d}, {1.0d, 1.0d}};
            IDataSet ds = DataSet.getFor2D("DS 1", d, ms);
            dss.add(ds);
        }

        {
            MarkerStyle ms = new MarkerStyle(3.0f, Gradient.getViridisGradient(20, false), 0, Marker.CIRCLE);
            double[][] d = new double[100][2];
            for (int i = 0; i < 100; i++)
            {
                d[i][0] = -1.0f + 2.0f * i / 99.0f;
                d[i][1] = 0.0f;
            }
            IDataSet ds = DataSet.getFor2D("DS 2", d, ms, null);
            dss.add(ds);
        }

        {
            LineStyle ls = new LineStyle(2.0f, Color.RED);
            MarkerStyle ms = new MarkerStyle(5.0f, Gradient.getViridisGradient(), 0, Marker.CIRCLE,
                    new LineStyle(1.0f, Gradient.getInfernoGradient(), 0));
            double[][] d = new double[][]{{-1.0d, -1.0d}, {0.0d, -1.0d}, {1.0d, -1.0d}};
            IDataSet ds = DataSet.getFor2D("DS 3", d, ms, ls);
            dss.add(ds);
        }


        {
            LineStyle ls = new LineStyle(2.0f, Gradient.getViridisGradient(), 0);
            double[][] d = new double[][]{{-1.0d, -0.5d}, {0.0d, -0.5d}, {1.0d, -0.5d}};
            IDataSet ds = DataSet.getFor2D("DS 4", d, null, ls);
            dss.add(ds);
        }

        plot.getModel().setDataSets(dss, true);

        frame.setVisible(true);
    }
}
