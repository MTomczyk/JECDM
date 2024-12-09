package visualization.heatmap3D;


import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import scheme.WhiteScheme;
import thread.swingtimer.reporters.IDSRecalculationTimesReporter;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap3D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test12_DataSwap
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap3D.Params pP = new Heatmap3D.Params();
        // pP._debugMode = true;
        pP._title = "Test heatmap";
        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";
        pP._zAxisTitle = "Z - dimension";
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        pP._gradient = Gradient.getViridisGradient();

        // play with parameters and check
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;

        pP._xDiv = 3;
        pP._yDiv = 3;
        pP._zDiv = 3;

        pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._depthGridLinesWithBoxTicks = true;

        pP._xAxisWithBoxTicks = true;
        pP._yAxisWithBoxTicks = true;
        pP._zAxisWithBoxTicks = true;

        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);

        double n = Double.NEGATIVE_INFINITY;
        double[][][] data1 = new double[][][]{
                {{0.0d, 1.0d, 0.5d}, {n, 0.0d, 0.0d}, {-1.0d, 0.0d, 1.0d}}, // front
                {{n, n, n}, {1.0d, 0.0d, 1.0d}, {1.0d, 1.0d, 1.0d}}, // middle
                {{0.0d, 0.0d, 0.0d}, {n, n, n}, {1.0d, 1.0d, 1.0d}}, // back
        };

        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        plot.getModel().setDataAndPerformProcessing(data1);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        double[][][] data2 = new double[][][]{
                {{0.0d, 0.0d, 0.0d}, {n, n, n}, {1.0d, 1.0d, 1.0d}},
                {{0.0d, 1.0d, 0.5d}, {n, 0.0d, 0.0d}, {-1.0d, 0.0d, 1.0d}},
                {{n, n, n}, {1.0d, 0.0d, 1.0d}, {1.0d, 1.0d, 1.0d}},

        };
        plot.getModel().setDataAndPerformProcessing(data2);

    }
}
