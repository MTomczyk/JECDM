package visualization.heatmap3D;


import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap3D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test5_DataBigger_Animated
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
        int xDiv = 50;
        int yDiv = 50;
        int zDiv = 50;

        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._zDiv = zDiv;

        pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._depthGridLinesWithBoxTicks = true;


        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();


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

        double max = -1;
        for (int i = 0; i < zDiv; i++)
            for (int ii = 0; ii < yDiv; ii++)
                for (int iii = 0; iii < xDiv; iii++)
                    if (data[i][ii][iii] > max) max = data[i][ii][iii];
        System.out.println(max);
        plot.getModel().setDataAndPerformProcessing(data);
        frame.setVisible(true);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 10000; i++)
        {
            try
            {
                Thread.sleep(2);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            double p = i / 9999.0d;
            double c = 0.5d + 0.5d * Math.sin(5.0d * 2.0d * Math.PI * p);
            if (c < 0.001d) c = 0.0d; // to avoid numerical errors
            if (frame.isTerminating()) return;
            plot.getModel().setValueFilterInTheNormalizedSpace(c, 1.0d);
        }
    }
}
