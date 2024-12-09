package t1_10.t1_visualization_module.t5_heatmap_2d.t4;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
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
 * This tutorial focuses on creating and displaying a basic 2D heatmap (Gaussian example).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial4
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap2D.Params pP = new Heatmap2D.Params();

        // Let's now consider a parametric number of x/y divisions:
        int xDiv = 100;
        int yDiv = 100;

        // Additionally, let the display range be parametric.
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

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 700, 600);

        // The following lines intend to create raw heatmap data. This tutorial assumes that multiple data points are
        // drawn from the Gaussian distribution (X and Y coordinates) and used to fill the buckets (representing
        // the number of points falling into specific bounds).

        // A double 2D matrix is first instantiated (note that the first dimension is linked to the Y-axis, while the
        // latter is to X).
        double[][] data = new double[yDiv][xDiv];

        // The object constructed below is a supportive tool that allows retrieving bucket coordinates (indices, i.e.,
        // in the discretized space) given the input x and y coordinates (in the original space). The object constructor
        // requires the following parameters: the number of dimensions and divisions per each dimension (in the "normal"
        // order, i.e., X, Y,...), the bounds (Range) associated with each dimension (in the normal order),
        // and an auxiliary object for retrieving the discretized indices from the double input (here, a simple linear
        // interpolation with thresholding is used).
        BucketCoordsTransform B = new BucketCoordsTransform(2, new int[]{xDiv, yDiv},
                new Range[]{RX, RY}, new LinearlyThresholded());

        // Create the RNG.
        IRandom R = new MersenneTwister64(0);

        // TThe below variable represents the number of input data points to sample.
        int trials = 100000;

        // Standard deviation of the Gaussian distribution
        float std = 0.25f;

        // This loop generates the input points (X and Y coordinates) and projects them onto buckets.
        for (int i = 0; i < trials; i++)
        {
            // Draw X and Y coordinates. Note the mean of the Gaussian distribution is centered near the left-bottom
            // part of the coordinate space (25% of X and 25% of Y).
            float x = (float) (RX.getLeft() + RX.getInterval() * 0.25f + R.nextGaussian() * std);
            float y = (float) (RY.getLeft() + RY.getInterval() * 0.25f + R.nextGaussian() * std);

            // The below line constructs the coordinates of a bucket (discretized space) that encapsulates the X and Y
            // coordinates (note that the buckets' intervals are left-side closed and right-side open). If the point
            // falls outside the display ranges, the method returns null.
            int[] c = B.getBucketCoords(new double[]{x, y});

            // The returned indices are returned in the [X, Y] order. They can be used to increment the proper data
            // entry (note that the heatmap assumes [Y, X] order).
            if (c != null) data[c[1]][c[0]]++;
        }

        // Set the heatmap data.
        plot.getModel().setDataAndPerformProcessing(data);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
