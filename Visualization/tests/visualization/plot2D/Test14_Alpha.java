package visualization.plot2D;

import color.gradient.Gradient;
import color.gradient.gradients.Viridis;
import dataset.DataSet;
import dataset.IDataSet;
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
import thread.swingtimer.reporters.IDSRecalculationTimesReporter;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Tests alpha channel (transparency).
 *
 * @author MTomczyk
 */
public class Test14_Alpha
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

        DisplayRangesManager.Params pD = new DisplayRangesManager.Params();
        pD._DR = new DisplayRangesManager.DisplayRange[3];
        pD._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pD._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pD._DR[2] = new DisplayRangesManager.DisplayRange(null, true, false);
        pD._attIdx_to_drIdx = new Integer[] {0, 1, 2};
        pP._pDisplayRangesManager = pD;

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();
        ArrayList<IDataSet> dss = new ArrayList<>();
        {
            int points = 100000;
            double[][] data = new double[points][3];
            IRandom R = new MersenneTwister64(1);
            for (int i = 0; i < points; i++)
            {
                data[i][0] = R.nextGaussian() * 0.2f;
                data[i][1] = R.nextGaussian() * 0.2f;
                data[i][2] = Math.sqrt(Math.pow(data[i][0], 2.0d) + Math.pow(data[i][1], 2.0d));
            }
            MarkerStyle ms = new MarkerStyle(1.0f, Gradient.getGradientFromColorMatrix(Viridis.colors_alpha_decreasing, "Viridis_alpha", 100, false), 2, Marker.SQUARE);
            IDataSet ds = DataSet.getFor2D("data", data, ms);
            dss.add(ds);
        }

        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));
        plot.getModel().setDataSets(dss, true);

        frame.setVisible(true);

    }
}
