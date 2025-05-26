package visualization.heatmap3D;


import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.BucketStyle;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import org.apache.commons.math4.legacy.stat.StatUtils;
import plot.heatmap.Heatmap3D;
import plot.heatmap.utils.Coords;
import plot.heatmap.utils.HeatmapDataProcessor;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;
import thread.swingtimer.reporters.IDSRecalculationTimesReporter;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap3D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test17_WithData
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        DisplayRangesManager.DisplayRange heatmapDR = new DisplayRangesManager.DisplayRange(null, true, false);

        Heatmap3D.Params pP = new Heatmap3D.Params(heatmapDR);
        // pP._debugMode = true;
        pP._title = "Test heatmap";
        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";
        pP._zAxisTitle = "Z - dimension";
        pP._scheme = WhiteScheme.getForPlot3D();

        Range RX = Range.getNormalRange();
        Range RY = Range.getNormalRange();
        Range RZ = Range.getNormalRange();

        pP._bucketStyle = new BucketStyle();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

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

        pP._horizontalGridLinesWithBoxTicks = false;
        pP._verticalGridLinesWithBoxTicks = false;
        pP._depthGridLinesWithBoxTicks = false;

        pP._colorbar = new Colorbar(pP._gradient, "Heatmap", new FromDisplayRange(heatmapDR, 10));

        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        plot.getComponentsContainer().getMargins().getSurpassedSizes().put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        frame.updateScheme();
        frame.updateLayout();

        ArrayList<IDataSet> dss = new ArrayList<>();

        {
            int num = 250;

            double[][] DS = new double[num][];
            for (int i = 0; i <= (num - 1); i++)
            {
                double x = (double) i / num;
                double angle = (double) (i * 5) / num * 2.0d * Math.PI;
                double y = 0.5d +  0.2d * Math.sin(angle);
                double z = 0.5d +  0.2d * Math.cos(angle);
                DS[i] = new double[]{x, y, z};
            }

            DataSet ds = DataSet.getFor3D("test", DS, null, new LineStyle(1.0f, Gradient.getViridisGradient(), 0));
            dss.add(ds);
        }

        plot.getModel().setDataSets(dss, true);

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
        System.out.println(StatUtils.min(SV._sortedValues) + " " + StatUtils.max(SV._sortedValues));
        plot.getModel().setDataAndPerformProcessing(SC, SV._sortedValues);

        boolean[][][] mask = new boolean[zDiv][yDiv][xDiv];
        for (int i = 0; i < zDiv; i++)
            for (int ii = 0; ii < yDiv; ii++)
                for (int iii = 0; iii < xDiv; iii++)
                {
                    if (i < zDiv / 2) mask[i][ii][iii] = true;
                }

        plot.getModel().setMask(mask);



    }
}
