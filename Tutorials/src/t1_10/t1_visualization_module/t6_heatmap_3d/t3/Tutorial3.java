package t1_10.t1_visualization_module.t6_heatmap_3d.t3;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import component.drawingarea.DrawingArea3D;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;
import space.normalization.minmax.Gamma;

import java.text.DecimalFormat;


/**
 * This tutorial focuses on creating and displaying a basic 3D heatmap (using non-liner scales).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial3
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

        pP._drawMainGridlines = true;
        pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._depthGridLinesWithBoxTicks = true;

        pP._yAxisWithBoxTicks = true;
        pP._xAxisWithBoxTicks = true;
        pP._zAxisWithBoxTicks = true;

        pP._scheme = WhiteScheme.getForHeatmap3D(0.25f);


        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(), Range.getNormalRange(),
                Range.getNormalRange());

        pP._xBucketCoordsNormalizer = new Gamma(0.5f);
        pP._yBucketCoordsNormalizer = new Gamma(2.0f);
        pP._zBucketCoordsNormalizer = new Gamma(3.0f);

        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);

        pP._heatmapDisplayRange.setNormalizer(new Gamma(0.5f));
        pP._gradient = Gradient.getViridisGradient();

        pP._colorbar = new Colorbar(pP._gradient, "Value", new FromDisplayRange(pP._heatmapDisplayRange, 6));


        Heatmap3D plot = new Heatmap3D(pP);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 1600, 1200);


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

        ((DrawingArea3D)(plot.getComponentsContainer().getDrawingArea())).getAxes()[0].getTicksDataGetter().setPredefinedTickLabels(new String[]{"COL 0", "COL 1", "COL 2"});
        ((DrawingArea3D)(plot.getComponentsContainer().getDrawingArea())).getAxes()[1].getTicksDataGetter().setPredefinedTickLabels(new String[]{"ROW 0", "ROW 1", "ROW 2", "ROW3"});
        ((DrawingArea3D)(plot.getComponentsContainer().getDrawingArea())).getAxes()[2].getTicksDataGetter().setPredefinedTickLabels(new String[]{"DEPTH 0", "DEPTH 1"});

        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat());
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f});

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
