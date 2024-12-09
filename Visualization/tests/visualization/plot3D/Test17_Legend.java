package visualization.plot3D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test17_Legend
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
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        ArrayList<IDataSet> dss = new ArrayList<>();

        {
            int num = 250;

            double[][] DS = new double[num][];
            for (int i = 0; i <= (num - 1); i++)
            {
                double x = -0.5d + (double) i / num;
                double angle = (double) (i * 5) / num * 2.0d * Math.PI;
                double y = 0.2d * Math.sin(angle);
                double z = 0.2d * Math.cos(angle);
                DS[i] = new double[]{x, y, z};
            }

            DataSet ds = DataSet.getFor3D("test", DS, null, new LineStyle(1.0f, Gradient.getViridisGradient(), 0, 0.5f));
            dss.add(ds);

            dss.add(DataSet.getFor3D("ds1", new double[][]{{0.0d, 0.0d, 0.0d}},
                    new MarkerStyle(2.0f, Color.RED, Marker.POINT_3D, 1.0f), null));
            dss.add(DataSet.getFor3D("ds2", new double[][]{{-0.2d, -0.2d, -0.2d}},
                    new MarkerStyle(0.05f, Color.RED, Marker.CUBE_3D, new LineStyle(0.1f, Color.BLACK, 1.0f), 1.0f), null));

        }

        plot.getModel().setDataSets(dss, true);
        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);
    }
}
