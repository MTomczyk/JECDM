package visualization.plot2D;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test25_AnimatedChanges_Lines
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
        pP._drawLegend = false;
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-1.0d, 1.0d),
                new Range(-1.0d, 1.0d));


        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.setVisible(true);
        plot.getModel().notifyDisplayRangesChangedListeners();

        IRandom R = new MersenneTwister64(1);
        MarkerStyle ms = new MarkerStyle(1.0f, Gradient.getViridisGradient(100, false), 0, Marker.SQUARE);
        LineStyle ls = new LineStyle(1.0f, Gradient.getRedBlueGradient(10, false), 1);

        for (int t = 0; t < 10000; t++)
        {
            ArrayList<IDataSet> dss = new ArrayList<>();
            int points = 1000;
            double[][] data = new double[points][2];
            for (int i = 0; i < points; i++)
            {
                float frac = (float) t / 999;
                double r = 0.35d * Math.sin(Math.PI * 2.0f * frac);
                data[i][0] = R.nextGaussian() * r;
                data[i][1] = R.nextGaussian() * r;
            }
            //  if (t < 9999) continue;

            IDataSet ds = DataSet.getFor2D("data", data, ms, ls);
            dss.add(ds);
            if (frame.isTerminating()) break;
            plot.getModel().setDataSets(dss, true);
        }


    }

}
