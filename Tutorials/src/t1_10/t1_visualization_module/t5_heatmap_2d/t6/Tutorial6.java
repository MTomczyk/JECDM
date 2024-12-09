package t1_10.t1_visualization_module.t5_heatmap_2d.t6;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import plot.heatmap.utils.Coords;
import plot.heatmap.utils.HeatmapDataProcessor;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;


/**
 * This tutorial focuses on creating and displaying a basic 2D heatmap (masking).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial6
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap2D.Params pP = new Heatmap2D.Params();

        int xDiv = 100;
        int yDiv = 100;
        Range RX = new Range(1.5f, 2.5f);
        Range RY = new Range(-0.5f, 0.5f);

        pP._xDiv = xDiv;
        pP._yDiv = yDiv;

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        pP._drawMainGridlines = false;
        pP._horizontalGridLinesWithBoxTicks = false;
        pP._verticalGridLinesWithBoxTicks = false;

        pP._yAxisWithBoxTicks = false;
        pP._xAxisWithBoxTicks = false;

        pP._drawLegend = false;

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(RX, RY);

        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._gradient = Gradient.getViridisGradient();

        pP._colorbar = new Colorbar(pP._gradient, "Value", new FromDisplayRange(pP._heatmapDisplayRange, 5));
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        Heatmap2D plot = new Heatmap2D(pP);

        //Frame frame = new Frame(plot, 0.31f, 0.5f);
        Frame frame = new Frame(plot, 700, 600);

        double[][] data = new double[yDiv][xDiv];

        // Let's assume that we want to prohibit the rendering of the left half of the heatmap. First,
        // we need to create a mask matrix (as with data, Y dimension goes first, then X):
        boolean [][] mask = new boolean[yDiv][xDiv];

        BucketCoordsTransform B = new BucketCoordsTransform(2, new int[]{xDiv, yDiv},
                new Range[]{RX, RY}, new LinearlyThresholded());

        IRandom R = new MersenneTwister64(0);

        int trials = 100000;
        float std = 0.25f;

        for (int i = 0; i < trials; i++)
        {
            float x = (float) (RX.getLeft() + RX.getInterval() / 2.0f + R.nextGaussian() * std);
            float y = (float) (RY.getLeft() + RY.getInterval() / 2.0f + R.nextGaussian() * std);
            int[] c = B.getBucketCoords(new double[]{x, y});
            if (c != null)
            {
                data[c[1]][c[0]]++;
                // The default false value of the mask matrix indicates that the associated bucket is renderable. The
                // below line will set the flag to true (prohibit rendering) if the bucket is placed on the left half
                // of the heatmap.
                if (c[0] < (float) xDiv / 2) mask[c[1]][c[0]] = true;
            }
        }


        HeatmapDataProcessor hdp = new HeatmapDataProcessor();
        Coords[] SC = hdp.getCoords2D(xDiv, yDiv, data);
        HeatmapDataProcessor.SortedValues SV = hdp.getSortedValues(SC, 2);
        plot.getModel().setDataAndPerformProcessing(SC, SV._sortedValues);

        // The mask can be provided using the below line.
        plot.getModel().setMask(mask);

        //Additionally, let's use a value filter (normalized):
        plot.getModel().setValueFilterInTheNormalizedSpace(0.25f, 1.0f);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}