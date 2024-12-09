package visualization.convergence2D;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
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
 * Tests painting convergence plots.
 *
 * @author MTomczyk
 */
public class Test5_AnimatedChanges
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

        frame.setVisible(true);

        for (int t = 0; t < 1000000; t++)
        {
            ArrayList<IDataSet> dss = new ArrayList<>();
            MarkerStyle ms = null;

            //new MarkerStyle(3.0f, Gradient.getViridisGradient(20,false), 0, Marker.CIRCLE);
            int disc = 10;
            LineStyle ls = new LineStyle(0.2f, Color.RED);
            double[][] d = new double[disc][4];
            for (int i = 0; i < disc; i++)
            {
                d[i][0] = i / (float) (disc - 1);
                d[i][1] = Math.pow(1.0f / (i + 1), 2.0f * (float) t / 999999.0f);
                d[i][2] = d[i][1] + 0.1f;
                d[i][3] = d[i][1] - 0.1f;
            }
            IDataSet ds = DataSet.getForConvergencePlot2D("CP1", d, ms, ls,
                    new color.Color(1.0f, 0.0f, 0.0f, 0.25f));
            dss.add(ds);

            if (frame.isTerminating()) break;
            plot.getModel().setDataSets(dss, true);
        }

    }
}
