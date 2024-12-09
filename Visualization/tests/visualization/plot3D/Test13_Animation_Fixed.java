package visualization.plot3D;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test13_Animation_Fixed
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
        pP._drawLegend = false;
        pP._useAlphaChannel = false;
        pP._scheme = new WhiteScheme();

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-2.0f, 2.0f));
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-2.0f, 2.0f));
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-2.0f, 2.0f));
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(new Range(-2.0f, 2.0f));
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.setVisible(true);
        plot.getModel().notifyDisplayRangesChangedListeners();

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100000; t++)
        {
            double p = (double) t / (100000 - 1);
            float std = (float) Math.sin(Math.PI * 2.0d * p);
            std *= 1;

            int objects = 10000;
            double[][] d = new double[objects][4];
            for (int i = 0; i < objects; i++)
            {
                d[i][0] = R.nextGaussian() * std;
                d[i][1] = R.nextGaussian() * std;
                d[i][2] = R.nextGaussian() * std;
                d[i][3] = Math.sqrt(d[i][0] * d[i][0] + d[i][1] * d[i][1] + d[i][2] * d[i][2]);
            }
            ArrayList<IDataSet> dss = new ArrayList<>();
            MarkerStyle ms = new MarkerStyle(0.02f, Gradient.getViridisGradient(), 3, Marker.CUBE_3D);
            dss.add(DataSet.getFor3D("ds1", d, ms, null));
            if (frame.isTerminating()) break;
            plot.getModel().setDataSets(dss, false);
        }
        System.out.println("DONE");


    }
}
