package visualization.plot3D;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import space.Dimension;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test28_ProjectionBounds
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

        pP._xProjectionBound = new Dimension(0.0d, 0.3d);
        pP._yProjectionBound = new Dimension(-1.0d, 2.0d);
        pP._zProjectionBound = new Dimension(-3.0d, 2.0d);
        pP._paneAlignments = new Align[]{Align.LEFT, Align.RIGHT, Align.TOP, Align.BOTTOM, Align.FRONT, Align.BACK};
        pP._axesAlignments = new Align[]{Align.FRONT_BOTTOM, Align.FRONT_TOP, Align.FRONT_LEFT, Align.FRONT_RIGHT,
        Align.BACK_BOTTOM, Align.BACK_TOP, Align.BACK_LEFT, Align.BACK_RIGHT, Align.LEFT_BOTTOM, Align.LEFT_TOP,
                Align.RIGHT_BOTTOM, Align.RIGHT_TOP};

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));


        double[][] d = new double[][]{{-1.0d, -1.0d, -1.0d}, {1.0d, 1.0d, 1.0d}};

        ArrayList<IDataSet> dss = new ArrayList<>();
        LineStyle ls = new LineStyle(5.0f, Gradient.getViridisGradient(5,false), 2);
        dss.add(DataSet.getFor3D("ds1", d, null, ls));
        plot.getModel().setDataSets(dss, true);

        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);
    }
}
