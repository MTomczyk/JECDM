package t1_10.t1_visualization_module.t6_heatmap_3d.t1;

import color.gradient.Gradient;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;


/**
 * This tutorial focuses on creating and displaying a basic 3D heatmap.
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
        Heatmap3D.Params pP = new Heatmap3D.Params();

        pP._xDiv = 3;
        pP._yDiv = 4;
        pP._zDiv = 2;

        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";
        pP._zAxisTitle = "Z - dimension";

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(), Range.getNormalRange(),
                Range.getNormalRange());

        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._gradient = Gradient.getViridisGradient();

        pP._scheme = WhiteScheme.getForHeatmap3D();

        Heatmap3D plot = new Heatmap3D(pP);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 1200, 1200);

        double[][][] data = new double[][][]
                {
                        {{2.0f, 2.0f, 3.0f},
                                {2.0f, 3.0f, 2.0f},
                                {2.0f, 5.0f, 2.0f},
                                {1.0f, 2.0f, 1.0f}},

                        {{3.0f, 3.0f, 4.0f},
                                {3.0f, 4.0f, 3.0f},
                                {3.0f, 6.0f, 3.0f},
                                {2.0f, 3.0f, 2.0f}},
                };

        plot.getModel().setDataAndPerformProcessing(data);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
