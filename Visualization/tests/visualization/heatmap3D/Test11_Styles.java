package visualization.heatmap3D;

import color.gradient.Gradient;
import dataset.painter.style.BucketStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;
import thread.swingtimer.reporters.IDSRecalculationTimesReporter;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap3D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test11_Styles
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

        Range RX = Range.getNormalRange();
        Range RY = Range.getNormalRange();
        Range RZ = Range.getNormalRange();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(RX, RY, RZ);

        pP._gradient = Gradient.getViridisGradient();

        // play with parameters and check
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;

        // play with parameters and check
        int xDiv = 20;
        int yDiv = 20;
        int zDiv = 20;

        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._zDiv = zDiv;

        pP._horizontalGridLinesWithBoxTicks = false;
        pP._verticalGridLinesWithBoxTicks = false;
        pP._depthGridLinesWithBoxTicks = false;

      //  pP._bucketStyle = new BucketStyle(null, new LineStyle(0.5f, Color.BLACK));
      //  pP._bucketStyle = new BucketStyle(null, new LineStyle(0.5f, Gradient.getPlasmaGradient(), 2));
        pP._bucketStyle = new BucketStyle(1.0f);

        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));


        double[][][] data = new double[zDiv][yDiv][xDiv];
        BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{xDiv, yDiv, zDiv},
                new Range[]{RX, RY, RZ}, new LinearlyThresholded());

        IRandom R = new MersenneTwister64(0);
        int trials = 1000000;
        for (int i = 0; i < trials; i++)
        {
            float x = (float) (0.5f + R.nextGaussian() * 0.2d);
            float y = (float) (0.5f + R.nextGaussian() * 0.2d);
            float z = (float) (0.5f + R.nextGaussian() * 0.2d);
            int[] c = B.getBucketCoords(new double[]{x, y, z});
            if (c != null) data[c[2]][c[1]][c[0]]++;
        }

        plot.getModel().setDataAndPerformProcessing(data);

        frame.setVisible(true);
        plot.getModel().setValueFilterInTheNormalizedSpace(0.25d, 1.0d);
        //plot.getModel().setValueFilter(new Range(50, 1000000));

    }
}
