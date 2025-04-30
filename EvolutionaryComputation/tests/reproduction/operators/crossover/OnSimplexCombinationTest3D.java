package reproduction.operators.crossover;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import reproduction.operators.crossover.OnSimplexCombination;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.operators.crossover.ICrossover;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.simplex.DasDennis;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

import java.util.ArrayList;

/**
 * Provides various tests for {@link OnSimplexCombination}
 *
 * @author MTomczyk
 */
class OnSimplexCombinationTest3D
{
    /**
     * Simple script for verifying the distribution of resulting offspring vectors.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int T = 1000000;
        int xDiv = 100;
        int yDiv = 100;
        int zDiv = 100;

        IRandom R = new MersenneTwister64(0);
        ICrossover crossover = new OnSimplexCombination(0.2d);

        double[] p1 = new double[]{0.25d, 0.25d, 0.5d};
        double[] p2 = new double[]{0.4d, 0.3d, 0.3d};

        double[][][] data = new double[zDiv][yDiv][xDiv];
        BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{xDiv, yDiv, zDiv},
                new Range[]{Range.getNormalRange(), Range.getNormalRange(), Range.getNormalRange()},
                new LinearlyThresholded());

        for (int t = 0; t < T; t++)
        {
            double[] o = crossover.crossover(p1, p2, R);
            int[] c = B.getBucketCoords(o);
            if (c == null) continue;
            data[c[2]][c[1]][c[0]]++;
        }

        ArrayList<double[]> ref = DasDennis.getWeightVectors(3, 30);
        double[][] rps = new double[ref.size()][];
        for (int i = 0; i < ref.size(); i++) rps[i] = ref.get(i);


        Heatmap3D.Params pP = new Heatmap3D.Params();
        pP._title = "Test heatmap";
        pP._xAxisTitle = "x1";
        pP._yAxisTitle = "x2";
        pP._zAxisTitle = "x3";
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(),
                Range.getNormalRange(), Range.getNormalRange());
        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Hits", new FromDisplayRange(pP._heatmapDisplayRange, 5));
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;
        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._zDiv = zDiv;
        pP._verticalGridLinesWithBoxTicks = false;
        pP._horizontalGridLinesWithBoxTicks = false;
        pP._depthGridLinesWithBoxTicks = false;
        pP._drawMainGridlines = false;
        pP._gradient = Gradient.getViridisGradient();
        Heatmap3D plot = new Heatmap3D(pP);
        Frame frame = new Frame(plot, 0.5f);
        plot.getModel().setDataAndPerformProcessing(data);
        plot.getModel().setValueFilterInTheNormalizedSpace(0.00001f, 1.0f);
        plot.getModel().setDataSet(DataSet.getFor3D("Simplex", rps,
                new MarkerStyle(0.02f, Color.RED, Marker.SPHERE_LOW_POLY_3D)));



        frame.setVisible(true);
    }
}