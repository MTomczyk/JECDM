package y2025.ERS.e1_auxiliary;

import color.gradient.Gradient;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Heatmap3DFactory;
import plot.heatmap.Heatmap3D;
import problem.moo.ReferencePointsFactory;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

/**
 * Auxiliary script that tests sampling from a convex sphere.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class SphereUniformityTest
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {

        IRandom R = new MersenneTwister64(0); // RNG
        int points = 100000; // no points to sample

        // Sample points and swap to convex:
        double[][] np = ReferencePointsFactory.getUniformRandomRPsOnConvexSphere(points, 3, R);
        assert np != null;
        for (double[] v : np)
            for (int m = 0; m < 3; m++) v[m] = (1.0d - v[m]);

        // Discretization levels:
        int xDiv = 100;
        int yDiv = 100;
        int zDiv = 100;

        // Ranges:
        Range xR = Range.get0R(1.2f);
        Range yR = Range.get0R(1.2f);
        Range zR = Range.get0R(1.2f);

        // Projects a double point into bucket coordinates (discretized indices compatible with the data object):
        BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{xDiv, yDiv, zDiv},
                new Range[]{xR, yR, zR}, new LinearlyThresholded());

        // Heatmap data to be filled:
        double[][][] data = new double[zDiv][yDiv][xDiv];

        // Generate samples and place them in correct buckets:
        for (int t = 0; t < points; t++)
        {
            int[] c = B.getBucketCoords(np[t]);
            if (c == null) continue;
            data[c[2]][c[1]][c[0]]++;
        }

        // Create heatmap:
        Heatmap3D heatmap3D = Heatmap3DFactory.getHeatmap3D(new WhiteScheme(), "f1", "f2", "f3",
                DRMPFactory.getFor3D(xR, yR, zR),
                5, 5, 5, 5,
                "0.00", "0.00", "0.00", "0.00",
                xDiv, yDiv, zDiv, new DisplayRangesManager.DisplayRange(null, true),
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
