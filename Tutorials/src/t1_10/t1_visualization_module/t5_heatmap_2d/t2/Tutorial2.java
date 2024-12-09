package t1_10.t1_visualization_module.t5_heatmap_2d.t2;

import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import space.Range;


/**
 * This tutorial focuses on creating and displaying a basic 2D heatmap (customization).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap2D.Params pP = new Heatmap2D.Params();

        pP._xDiv = 3;
        pP._yDiv = 4;

        // Don't draw axes' titles.
        pP._xAxisTitle = null;
        pP._yAxisTitle = null;

        // The below line will make the main grid lines renderable, while the following two lines will enforce the
        // grid lines to align with the buckets' boundaries.
        pP._drawMainGridlines = true;
        pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;

        // The two lines below will enforce the axes' ticks to be located at the centers of columns/rows that emerge
        // from the buckets/boxes.
        pP._yAxisWithBoxTicks = true;
        pP._xAxisWithBoxTicks = true;

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._gradient = Gradient.getViridisGradient();

        Heatmap2D plot = new Heatmap2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        double [][] data = new double[][]
                {
                        {2.0f, 2.0f, 3.0f},
                        {2.0f, 3.0f, 2.0f},
                        {2.0f, 5.0f, 2.0f},
                        {1.0f, 2.0f, 1.0f},
                };
        plot.getModel().setDataAndPerformProcessing(data);

        // The two lines below provide custom labels for axes' ticks (that point to columns/rows).
        plot.getComponentsContainer().getAxes()[0].getTicksDataGetter().setPredefinedTickLabels(new String[]{"COL 0", "COL 1", "COL 2"});
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setPredefinedTickLabels(new String[]{"ROW 0", "ROW 1", "ROW 2", "ROW3"});

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
