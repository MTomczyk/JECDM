package reproduction.operators.crossover;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

/**
 * Provides various tests for {@link OnSimplexCombination}
 *
 * @author MTomczyk
 */
class OnSimplexCombinationTest2D
{
    /**
     * Simple script for verifying the distribution of resulting offspring vectors.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int T = 1000000;
        int xDiv = 50;
        int yDiv = 50;

        IRandom R = new MersenneTwister64(0);
        ICrossover crossover = new OnSimplexCombination(0.2d);

        double[] p1 = new double[]{0.25d, 0.75d};
        double[] p2 = new double[]{0.75d, 0.25d};

        double[][] data = new double[yDiv][xDiv];
        BucketCoordsTransform B = new BucketCoordsTransform(2, new int[]{xDiv, yDiv},
                new Range[]{Range.getNormalRange(), Range.getNormalRange()}, new LinearlyThresholded());

        for (int t = 0; t < T; t++)
        {
            double[] o = crossover.crossover(p1, p2, R)._o;
            int[] c = B.getBucketCoords(o);
            if (c == null) continue;
            data[c[1]][c[0]]++;
        }

        Heatmap2D.Params pP = new Heatmap2D.Params();
        pP._title = "Test heatmap";
        pP._xAxisTitle = "x1";
        pP._yAxisTitle = "x2";
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(),
                Range.getNormalRange());
        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Hits", new FromDisplayRange(pP._heatmapDisplayRange, 5));
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
        Frame frame = new Frame(plot, 0.5f);
        plot.getModel().setDataAndPerformProcessing(data);
        frame.setVisible(true);
    }
}