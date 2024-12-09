package visualization.plot3D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Line;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test26_PolyOctoLine_Random
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._zAxisTitle = "Z-Axis Test (j)";

        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;
        pP._drawLegend = true;
        pP._useAlphaChannel = false;
        pP._scheme = new WhiteScheme();

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        ArrayList<IDataSet> dss = new ArrayList<>();
        IRandom R = new MersenneTwister64(0);
        int P = 100;
        {
            double[][] d = new double[P][3];
            for (int i = 0; i < P; i++)
            {
                d[i][0] = -2.0d + R.nextGaussian();
                d[i][1] = R.nextGaussian();
                d[i][2] = R.nextGaussian();
            }
            DataSet ds = DataSet.getFor3D("test1", d, null, new LineStyle(0.01f, Color.RED, Line.POLY_OCTO));

            dss.add(ds);
        }
        {
            double[][] d = new double[P][3];
            for (int i = 0; i < P; i++)
            {
                d[i][0] = 2.0d + R.nextGaussian();
                d[i][1] = R.nextGaussian();
                d[i][2] = R.nextGaussian();
            }

            DataSet ds = DataSet.getFor3D("test2", d, null, new LineStyle(0.01f, Gradient.getViridisGradient(), 0, Line.POLY_OCTO));
            dss.add(ds);
        }


        plot.getModel().setDataSets(dss, true);
        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);
    }
}
