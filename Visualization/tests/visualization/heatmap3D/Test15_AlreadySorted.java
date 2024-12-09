package visualization.heatmap3D;


import color.gradient.Gradient;
import dataset.painter.style.BucketStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import plot.heatmap.utils.Coords;
import plot.heatmap.utils.HeatmapDataProcessor;
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
public class Test15_AlreadySorted
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

        pP._bucketStyle = new BucketStyle();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(RX, RY, RZ);

        pP._gradient = Gradient.getViridisGradient();

        // play with parameters and check
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;

        // play with parameters and check
        int xDiv = 30;
        int yDiv = 30;
        int zDiv = 30;

        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._zDiv = zDiv;

        pP._horizontalGridLinesWithBoxTicks = false;
        pP._verticalGridLinesWithBoxTicks = false;
        pP._depthGridLinesWithBoxTicks = false;


        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));


        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);


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

        HeatmapDataProcessor hdp = new HeatmapDataProcessor();
        Coords [] SC = hdp.getCoords3D(xDiv, yDiv, zDiv, data);
        HeatmapDataProcessor.SortedValues SV = hdp.getSortedValues(SC, 3, false);
        plot.getModel().setDataAndPerformProcessing(SC, SV._sortedValues);

        boolean[][][] mask = new boolean[zDiv][yDiv][xDiv];
        for (int i = 0; i < zDiv; i++)
            for (int ii = 0; ii < yDiv; ii++)
                for (int iii = 0; iii < xDiv; iii++)
                {
                    if (i < zDiv / 2) mask[i][ii][iii] = true;
                }

        plot.getModel().setMask(mask);

        for (int t = 0; t < 10000; t++)
        {
            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }


            double p = t / 9999.0d;
            double c = 0.5d + 0.5d * Math.sin(5.0d * 2.0d * Math.PI * p);
            if (c < 0.001d) c = 0.0d; // to avoid numerical errors
            if (frame.isTerminating()) return;
            plot.getModel().setValueFilterInTheNormalizedSpace(c, 1.0d);
        }



    }
}
