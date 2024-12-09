package t1_10.t1_visualization_module.t5_heatmap_2d.t1;

import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import space.Range;


/**
 * This tutorial focuses on creating and displaying a basic 2D heatmap.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // First, a params container for the heatmap must be instantiated.
        Heatmap2D.Params pP = new Heatmap2D.Params();

        // The two below param fields are essential as they can be used to specify a discretization level for each
        // dimension. In the example below, the X-dimension will be divided three times, while the Y-dimension will be four.
        pP._xDiv = 3;
        pP._yDiv = 4;

        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";

        // Heatmap 2D uses regular parameterization of display ranges in the params container as it is assumed that the
        // discretization is done on a specified part of 2D plane (as done when using Plot2D). Note that Heatmap2D does
        // not perform the heatmap rendering explicitly on the drawing area maintained by the parent Plot2D class.
        // Instead, it involves an additional layer that is put explicitly on the main drawing area to handle rendering.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());

        // However, an additional display range explicitly associated with the buckets' values must be specified
        // (bucket or box = discretization product). The configuration below states that the range is initially unknown
        // but will be determined dynamically.
        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);

        // Additionally, a gradient used to color the buckets (and associated with the above display range) must be specified.
        pP._gradient = Gradient.getViridisGradient();

        // The following line constructs the Heatmap2D object.
        Heatmap2D plot = new Heatmap2D(pP);


        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        // At the lowest level, the input data for Heatmap2D is a 2D double matrix storing values associated with
        // buckets. It is assumed by default that the first dimension of the matrix (row) is linked to bucket's
        // Y-coordinate (from bottom to top), while the column number reflects the X-coordinate (from left to right).
        // For instance, consider the data below. The data[0][0] = 2.0f value will be reflected at the left-bottom corner
        // of the plot, while data[3][2] = 1.0f value at the right-bottom.
        double [][] data = new double[][]
                {
                        {2.0f, 2.0f, 3.0f},
                        {2.0f, 3.0f, 2.0f},
                        {2.0f, 5.0f, 2.0f},
                        {1.0f, 2.0f, 1.0f},
                };

        // The model object linked to Heatmap2D (plot.getModel()) provides new methods for uploading heatmap data.
        // The line below contains the most basic way for data upload.
        plot.getModel().setDataAndPerformProcessing(data);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
