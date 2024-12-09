package visualization.heatmap3D;

import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import space.normalization.minmax.Gamma;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap3D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test13_NonLinearData
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

        pP._xBucketCoordsNormalizer = new Gamma(2.0f);
        pP._yBucketCoordsNormalizer = new Gamma(3.0f);
        pP._zBucketCoordsNormalizer = new Gamma(0.25f);

        pP._paneAlignments = new Align[]{Align.LEFT, Align.RIGHT, Align.TOP, Align.BOTTOM, Align.FRONT, Align.BACK};

        // pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();
        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        double n = Double.NEGATIVE_INFINITY;
        double[][][] data = new double[][][]{
                {{0.0d, 1.0d, 0.5d}, {n, 0.0d, 0.0d}, {-1.0d, 0.0d, 1.0d}}, // front
                {{n, n, n}, {1.0d, 0.0d, 1.0d}, {1.0d, 1.0d, 1.0d}}, // middle
                {{0.0d, 0.0d, 0.0d}, {n, n, n}, {1.0d, 1.0d, 1.0d}}, // back
        };
        plot.getModel().setDataAndPerformProcessing(data);

        frame.setVisible(true);
    }
}
