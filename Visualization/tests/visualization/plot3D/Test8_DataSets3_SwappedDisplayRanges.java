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
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test8_DataSets3_SwappedDisplayRanges
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
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{null, 1, 0, null, 2, 3};

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        IRandom R = new MersenneTwister64(0);
        int objects = 100000;

        double[][] d = new double[objects][6];
        for (int i = 0; i < objects; i++)
        {
            d[i][0] = 0.0f;
            d[i][1] = R.nextGaussian();
            d[i][2] = R.nextGaussian();
            d[i][3] = 0.0f;
            d[i][4] = R.nextGaussian();
            d[i][5] = Math.sqrt(d[i][1] * d[i][1] + d[i][2] * d[i][2] + d[i][4] * d[i][4]);
        }

        ArrayList<IDataSet> dss = new ArrayList<>();
        MarkerStyle ms = new MarkerStyle(0.01f, Gradient.getViridisGradient(10, false), 3, Marker.CUBE_3D);
        //MarkerStyle ms = new MarkerStyle(0.01f, Color.RED, Marker.CUBE);

        dss.add(DataSet.getFor3D("ds1", d, ms, null));
        plot.getModel().setDataSets(dss, true);

        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);

    }
}
