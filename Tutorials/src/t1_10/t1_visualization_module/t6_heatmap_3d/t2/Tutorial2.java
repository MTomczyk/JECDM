package t1_10.t1_visualization_module.t6_heatmap_3d.t2;

import color.gradient.Gradient;
import component.drawingarea.DrawingArea3D;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;


/**
 * This tutorial focuses on creating and displaying a basic 3D heatmap (customization).
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
        Heatmap3D.Params pP = new Heatmap3D.Params();

        pP._xDiv = 3;
        pP._yDiv = 4;
        pP._zDiv = 2;

        pP._xAxisTitle = null;
        pP._yAxisTitle = null;
        pP._zAxisTitle = null;

        pP._scheme = WhiteScheme.getForHeatmap3D();

        // The below are various flags relevant for the customization scenario
        pP._drawMainGridlines = true;
        pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._depthGridLinesWithBoxTicks = true;

        pP._yAxisWithBoxTicks = true;
        pP._xAxisWithBoxTicks = true;
        pP._zAxisWithBoxTicks = true;

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(), Range.getNormalRange(),
                Range.getNormalRange());

        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._gradient = Gradient.getViridisGradient();

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
        // The below lines set the predefined ticks' labels
        ((DrawingArea3D)(plot.getComponentsContainer().getDrawingArea())).getAxes()[0].getTicksDataGetter().setPredefinedTickLabels(new String[]{"COL 0", "COL 1", "COL 2"});
        ((DrawingArea3D)(plot.getComponentsContainer().getDrawingArea())).getAxes()[1].getTicksDataGetter().setPredefinedTickLabels(new String[]{"ROW 0", "ROW 1", "ROW 2", "ROW3"});
        ((DrawingArea3D)(plot.getComponentsContainer().getDrawingArea())).getAxes()[2].getTicksDataGetter().setPredefinedTickLabels(new String[]{"DEPTH 0", "DEPTH 1"});

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
