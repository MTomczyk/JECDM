package visualization.plot2D;

import color.gradient.Color;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import plotswrapper.GridPlots;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 *
 * @author MTomczyk
 */
public class Test27_AnimatedChanges_SwingWorkerOverflow
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int rows = 5;
        int columns = 5;
        int total = rows * columns;
        Plot2D[] plots = new Plot2D[total];

        for (int i = 0; i < total; i++)
        {
            Plot2D.Params pP = new Plot2D.Params();
            // pP._debugMode = true;
            pP._title = "Test (j)";
            pP._xAxisTitle = "X-axis Test (j)";
            pP._yAxisTitle = "Y-Axis Test (j)";
            pP._drawXAxis = true;
            pP._drawYAxis = true;
            pP._drawLegend = false;
            pP._clipDrawingArea = true;
            pP._scheme = new WhiteScheme();
            pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-1.0d, 1.0d),
                    false, false, new Range(-1.0d, 1.0d), false, false);
            plots[i] = new Plot2D(pP);
        }

        GridPlots.Params pW = new GridPlots.Params(plots, rows, columns, 1);
        GridPlots pw = new GridPlots(pW);

        Frame.Params pF = Frame.Params.getParams(pw, 0.8f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.setVisible(true);

        int T = 100000;
        double F = 3.0d;

        for (int t = 0; t < T; t++)
        {
            for (int p = 0; p < total; p++)
            {
                double shift = (double) p / (total - 1);
                double angle = shift + (double) t / (T - 1) * F;
                double x = Math.sin(2.0d * Math.PI * angle);
                double y = Math.cos(2.0d * Math.PI * angle);
                MarkerStyle ms = new MarkerStyle(10.0f, Color.RED, Marker.SQUARE);
                DataSet ds = DataSet.getFor2D("DS " + p, new double[][]{{x, y}}, ms);
                if (frame.isTerminating()) break;
                plots[p].getModel().setDataSet(ds, false);
            }
        }
        System.out.println("END");
    }
}
