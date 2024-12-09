package visualization.plot3D;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.enums.Line;
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
public class Test21_SpiralLegend_PolyQuadLine
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

            DataSet ds = DataSet.getFor3D("test", DS, null, new LineStyle(0.15f, Gradient.getViridisGradient(), 0, Line.POLY_QUAD));
            dss.add(ds);
        }

        plot.getModel().setDataSets(dss, true);
        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);
    }
}
