package visualization.convergence2D;

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
import java.util.LinkedList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Tests painting convergence plots.
 *
 * @author MTomczyk
 */
public class Test7_Disable_DSs
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
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();
        ArrayList<IDataSet> dss = new ArrayList<>();

        {
            MarkerStyle ms = null;
            //new MarkerStyle(3.0f, Gradient.getViridisGradient(20,false), 0, Marker.CIRCLE);
            int disc = 100;
            LineStyle ls = new LineStyle(0.2f, Color.RED);

            LinkedList<double[][]> d = new LinkedList<>();
            double[][] d1 = new double[20][4];
            for (int i = 0; i < 20; i++)
            {
                d1[i][0] = i / (float) (disc - 1);
                d1[i][1] = 1.0f / (i + 1);
                d1[i][2] = d1[i][1] + 0.1f;
                d1[i][3] = d1[i][1] - 0.1f;
            }
            d.add(d1);
            d.add(null);

            double[][] d2 = new double[50][4];
            for (int i = 0; i < 50; i++)
            {
                d2[i][0] = (50 + i) / (float) (disc - 1);
                d2[i][1] = 1.0f / ((50 + i) + 1);
                d2[i][2] = d2[i][1] + 0.1f;
                d2[i][3] = d2[i][1] - 0.1f;
            }
            d.add(d2);

            IDataSet ds = DataSet.getForConvergencePlot2D("CP1", d, ms, ls,
                    new color.Color(1.0f, 0.0f, 0.0f, 0.25f));
            dss.add(ds);
        }
        {
            MarkerStyle ms = new MarkerStyle(3.0f, Gradient.getViridisGradient(20, false),
                    0, Marker.CIRCLE);
            ms._paintEvery = 5;

            int disc = 100;
            LineStyle ls = new LineStyle(0.2f, Color.GREEN);
            double[][] d = new double[disc][4];
            for (int i = 0; i < disc; i++)
            {
                    d[i][0] = 1.0f - i / (float) (disc - 1);
                    d[i][1] = i / (float) (disc - 1);
                    d[i][2] = d[i][1] + 0.2f;
                    d[i][3] = d[i][1] - 0.1f;
            }
            IDataSet ds = DataSet.getForConvergencePlot2D("CP1", d, ms, ls,
                    new color.Color(0.0f, 1.0f, 0.0f, 0.25f));
            ds.setDisplayableOnLegend(true);
            ds.setSkipRendering(true);
            dss.add(ds);
        }

        plot.getModel().setDataSets(dss, true);
        frame.setVisible(true);
    }
}
