package t1_10.t1_visualization_module.t5_heatmap_2d.t5;

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
 * This tutorial focuses on creating and displaying a basic 2D heatmap (pre-sorted mode and filtering).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial5
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
        BucketCoordsTransform B = new BucketCoordsTransform(2, new int[]{xDiv, yDiv},
                new Range[]{RX, RY}, new LinearlyThresholded());

        IRandom R = new MersenneTwister64(0);

        int trials = 100000;
        float std = 0.25f;

        for (int i = 0; i < trials; i++)
        {
            float x = (float) (RX.getLeft() + RX.getInterval() * 0.25f + R.nextGaussian() * std);
            float y = (float) (RY.getLeft() + RY.getInterval() * 0.25f + R.nextGaussian() * std);
            int[] c = B.getBucketCoords(new double[]{x, y});
            if (c != null) data[c[1]][c[0]]++;
        }

        // The "plot.getModel().setDataAndPerformProcessing(data);" method explicitly passes the processing to another
        // public method "plot.getModel().setDataAndPerformProcessing(int [][] data, boolean sort)", with the "sort"
        // flag set to false. When this flag is true, the input buckets' relevant data (coordinates + values) is wrapped
        // by auxiliary classes Coords and SortedValues and sorted in the ascending order of stored values. The reason
        // for such a presorting is to boost filtering efficiency (at the cost of prolonged data preparation time).
        // Specifically, Heatmap2D (and 3D) allows for the provision of value filters (intervals that specify which
        // buckets should be displayed and which should not). If the input data is not presorted, all the buckets will
        // be inspected to determine whether they should be displayed when creating a plot render. However, if the
        // buckets are presorted, the left and right bounds for array indices are quickly determined using the binary
        // search. These indices specify the subset of buckets whose values fall into the specified interval and, thus,
        // should be rendered. This way of processing significantly reduces the computational complexity of filtering,
        // especially for the 3D mode, where the minimization of to-GPU transfers is demanded. When the data is presorted,
        // no such transfer is required as the OpenGL library allows for the selection of a subset of a preloaded buffer
        // to be rendered (which is implemented in this framework).

        // The presorting can be executed on one's own. It may be convenient when, e.g., implementing an application
        // that makes use of data stored on the disc. If so, it is recommended to assume a data format in which the bucket
        // data is presented in the file. This way, no additional sorting would be required during runtime. Such sorted
        // data can be provided via the "public void setDataAndPerformProcessing(Coords[] sortedCoords, double[]
        // sortedValues)" method, where the first array specifies the coordinates (sorted according to values), while
        // the second represents the sorted values) (the elements in both arrays are assumed to be connected).

        // The below three lines introduce an auxiliary tool, "HeatmapDataProcessor," that can construct sorted
        // bucket-related data from the input raw data matrix. The fourth line shows how to supply the heatmap with
        // the presorted data.
        HeatmapDataProcessor hdp = new HeatmapDataProcessor();
        Coords[] SC = hdp.getCoords2D(xDiv, yDiv, data);
        HeatmapDataProcessor.SortedValues SV = hdp.getSortedValues(SC, 2);
        plot.getModel().setDataAndPerformProcessing(SC, SV._sortedValues);

        // Note that you can use the below line if you do not want to execute the sorting on your own:
        //plot.getModel().setDataAndPerformProcessing(data, true);

        // Heatmap introduces two filtering options (that can be employed jointly): interval-based and masked-based.
        // This tutorial introduces the first. Consider the following four cases.

        // Case 1: First, a value filter can be specified in the normalized space, i.e., [0-1]. The left and right bounds
        // provided via the following method will select an interval for buckets' normalized values accepted for rendering
        // (both sides closed). That is, if a bucket's normalized value does not fall into this interval, it will not be
        // rendered. The below line sets the normalized filtering interval to [0.0; 0.5].
       // plot.getModel().setValueFilterInTheNormalizedSpace(0.0f, 0.5f);

        // Case 2: the normalized interval is set to [0.5; 1.0].
        //plot.getModel().setValueFilterInTheNormalizedSpace(0.5f, 1.0f);

        // Case 3: The filtering interval can also be specified in the original data space. The line below specifies that
        // the buckets whose original values (provided in data[][]) are in the given range of [0.0; 10.0f] will be rendered.
        //plot.getModel().setValueFilter(new Range(0.0f, 10.0f));

        // Case 4: As in case 3, but the interval is set to [10.0; 1000000.0].
         plot.getModel().setValueFilter(new Range(10.1f, 1000000.0f));

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
