package visualization.heatmap2D;

import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;


/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap2D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test17_AnimatedChanges_SortedMode
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap2D.Params pP = new Heatmap2D.Params();
        // pP._debugMode = true;
        pP._title = "Test heatmap";
        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);

        Range RX = Range.getNormalRange();
        Range RY = Range.getNormalRange();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(RX, RY);

        // play with parameters and check
        int xDiv = 50;
        int yDiv = 50;

        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._horizontalGridLinesWithBoxTicks = true;
        pP._xAxisWithBoxTicks = false;
        pP._yAxisWithBoxTicks = false;
        pP._drawMainGridlines = false;

        pP._gradient = Gradient.getViridisGradient();

        Heatmap2D plot = new Heatmap2D(pP);


        //==============================================================================================================

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);

        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 100000; t++)
        {
            //==============================================================================================================
            double[][] data = new double[yDiv][xDiv];
            BucketCoordsTransform B = new BucketCoordsTransform(2, new int[]{xDiv, yDiv},
                    new Range[]{RY, RX}, new LinearlyThresholded());
            int trials = 100000;

            float angle = 100.0f * (float) t / 99999.0f * (float) Math.PI * 2.0f;

            for (int i = 0; i < trials; i++)
            {
                float x = (float) (0.5f + Math.sin(angle) * 0.25f + R.nextGaussian() * 0.2d);
                float y = (float) (0.5f + Math.cos(angle) * 0.25f + R.nextGaussian() * 0.2d);
                int[] c = B.getBucketCoords(new double[]{x, y});
                if (c == null) continue;
                data[c[1]][c[0]]++;
            }
            if (frame.isTerminating()) break;
            plot.getModel().setDataAndPerformProcessing(data, true);
        }

    }
}
