package auxiliary;

import color.gradient.Gradient;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Heatmap3DFactory;
import plot.heatmap.Heatmap3D;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;
import scheme.WhiteScheme;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

/**
 * Simple runnable displaying randomly generated 3D normalized weight vectors (drawn randomly from a uniform distribution).
 *
 * @author MTomczyk
 */
public class VisualizeWeights3D
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int n = 1000000;
        double[][] W = new double[n][];
        for (int i = 0; i < n; i++) W[i] = WeightsGenerator.getNormalizedWeightVector(3, R);

        // Projects a double point into bucket coordinates (discretized indices compatible with the data object):
        BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{100, 100, 100},
                Range.getDefaultRanges(3, 1.0d), new LinearlyThresholded());
        // Heatmap data to be filled:
        double[][][] data = new double[100][100][100];

        // Generate samples and place them in correct buckets:
        for (double [] w: W)
        {
            int[] c = B.getBucketCoords(w);
            if (c == null) continue;
            data[c[2]][c[1]][c[0]]++;
        }

        // Create heatmap:
        Heatmap3D heatmap3D = Heatmap3DFactory.getHeatmap3D(new WhiteScheme(), "f1", "f2", "f3",
                DRMPFactory.getFor3D(1.0d, 1.0d, 1.0d),
                5, 5, 5, 5,
                "0.00", "0.00", "0.00", "0.00",
                100, 100, 100, new DisplayRangesManager.DisplayRange(null, true),
                Gradient.getViridisGradient(), "No. samples", 1.5f, 2.0f,
                scheme -> {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, color.Color.WHITE);
                    scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
                    scheme._sizes.put(SizeFields.AXIS_COLORBAR_TITLE_OFFSET_RELATIVE_MULTIPLIER, 0.2f);
                },
                pP1 -> {
                    pP1._heatmapDisplayRange.setNormalizer(new Gamma(0.5f));
                    pP1._verticalGridLinesWithBoxTicks = false;
                    pP1._horizontalGridLinesWithBoxTicks = false;
                    pP1._depthGridLinesWithBoxTicks = false;
                    pP1._drawMainGridlines = false;
                },
                null);

        int plotHeight = 1000;
        int plotWidth = 1100;

        // Update heatmap data:
        Frame frame = new Frame(heatmap3D, plotWidth, plotHeight);
        heatmap3D.getModel().setDataAndPerformProcessing(data);
        heatmap3D.getModel().setValueFilterInTheNormalizedSpace(0.00001f, 1.0f);

        frame.setVisible(true);
    }
}
