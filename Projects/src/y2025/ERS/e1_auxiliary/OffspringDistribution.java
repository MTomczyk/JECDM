package y2025.ERS.e1_auxiliary;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import reproduction.operators.crossover.OnSimplexCombination;
import reproduction.operators.mutation.OnSimplexSimplexMutation;
import drmanager.DRMPFactory;
import drmanager.DisplayRangesManager;
import frame.Frame;
import io.FileUtils;
import io.image.ImageSaver;
import plot.Heatmap3DFactory;
import plot.heatmap.Heatmap3D;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;
import scheme.WhiteScheme;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;
import utils.Screenshot;
import visualization.utils.ReferenceParetoFront;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This executable examines the performance of the developed reproduction operators.
 *
 * @author MTomczyk
 */
class OffspringDistribution
{
    /**
     * Simple script for verifying the distribution of resulting offspring vectors.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // The number of samples to generate:
        int T = 100000000; //100000000;

        // Discretization levels:
        int xDiv = 100;
        int yDiv = 100;
        int zDiv = 100;

        // RNG:
        IRandom R = new MersenneTwister64(0);

        // Operators
        IMutate mutate = new OnSimplexSimplexMutation(0.2d);
        ICrossover crossover = new OnSimplexCombination(0.2d);

        // Parent vectors:
        double[] p1 = new double[]{0.3, 0.2, 0.5};
        double[] p2 = new double[]{0.5, 0.4, 0.1};

        // Heatmap data to be filled:
        double[][][] data = new double[zDiv][yDiv][xDiv];

        // Projects a double point into bucket coordinates (discretized indices compatible with the data object):
        BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{xDiv, yDiv, zDiv},
                new Range[]{Range.getNormalRange(), Range.getNormalRange(), Range.getNormalRange()},
                new LinearlyThresholded());

        // Generate samples and place them in correct buckets:
        for (int t = 0; t < T; t++)
        {
            double[] o = crossover.crossover(p1.clone(), p2.clone(), R)._o;
            mutate.mutate(o, R);
            int[] c = B.getBucketCoords(o);
            if (c == null) continue;
            data[c[2]][c[1]][c[0]]++;
        }



        // Create heatmap:
        Heatmap3D heatmap3D = Heatmap3DFactory.getHeatmap3D(new WhiteScheme(), "w1", "w2", "w3",
                DRMPFactory.getFor3D(1.0d, 1.0d, 1.0d),
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

        // Update auxiliary data sets:
        ArrayList<IDataSet> dataSets = new ArrayList<>(2);
        dataSets.add(DSFactory3D.getDS("Parent vectors", new double[][]{p1, p2}, new MarkerStyle(0.03f,
                Color.BLACK, Marker.SPHERE_HIGH_POLY_3D)));

        IDataSet weightSpaceDS = ReferenceParetoFront.getFlat3DPF("Weight space", 1.0f, 30,
                new MarkerStyle(0.01f, Color.GRAY_50, Marker.SPHERE_HIGH_POLY_3D));
        assert weightSpaceDS != null;
        weightSpaceDS.setDisplayableOnLegend(false);
        dataSets.add(weightSpaceDS);
        heatmap3D.getModel().setDataSets(dataSets);

        // Set projection:
        heatmap3D.getModel().notifyDisplayRangesChangedListeners();
        heatmap3D.getController().getInteractListener().getTranslation()[2] = 1.8f;
        heatmap3D.getController().getInteractListener().getObjectRotation()[1] = 30.0f;

        try
        {
            // Create screenshot
            Screenshot screenshot = heatmap3D.getModel().requestScreenshotCreation(plotWidth * 2, plotHeight * 2,
                    false, new color.Color(255, 255, 255));
            screenshot._barrier.await();
            Path path = FileUtils.getPathRelatedToClass(OffspringDistribution.class, "Projects", "src", File.separatorChar);
            String fp = path.toString() + File.separatorChar + "offspring_distribution";
            ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);
        } catch (InterruptedException | IOException e)
        {
            throw new RuntimeException(e);
        }

        frame.setVisible(true);

    }
}