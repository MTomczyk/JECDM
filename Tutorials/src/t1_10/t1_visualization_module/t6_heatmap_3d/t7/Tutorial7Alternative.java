package t1_10.t1_visualization_module.t6_heatmap_3d.t7;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import component.drawingarea.DrawingArea3D;
import dataset.painter.style.BucketStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
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

import java.text.DecimalFormat;


/**
 * This tutorial focuses on creating and displaying a basic 3D heatmap (bucket style customization).
 * Note that this is a revised version of {@link Tutorial7}.
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial7Alternative
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap3D.Params pP = new Heatmap3D.Params();

        // Let's now consider a parametric number of x/y divisions:
        int xDiv = 50;
        int yDiv = 50;
        int zDiv = 50;

        // Additionally, let the display range be parametric.
        Range RX = new Range(-0.5f, 0.5f);
        Range RY = new Range(-0.5f, 0.5f);
        Range RZ = new Range(-0.5f, 0.5f);

        pP._xDiv = xDiv;
        pP._yDiv = yDiv;
        pP._zDiv = zDiv;

        pP._xAxisTitle = "X";
        pP._yAxisTitle = "Y";
        pP._zAxisTitle = "Z";

        pP._drawMainGridlines = false;
        pP._horizontalGridLinesWithBoxTicks = false;
        pP._verticalGridLinesWithBoxTicks = false;
        pP._depthGridLinesWithBoxTicks = false;

        pP._zAxisWithBoxTicks = false;
        pP._yAxisWithBoxTicks = false;
        pP._xAxisWithBoxTicks = false;


        pP._bucketStyle = BucketStyle.getDefault();
        //pP._bucketStyle = BucketStyle.getFillAndEdges(new LineStyle(0.1f, Color.BLACK));


        pP._drawLegend = false;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(RX, RY, RZ);
        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);
        pP._gradient = Gradient.getViridisGradient();
        pP._colorbar = new Colorbar(pP._gradient, "Value", new FromDisplayRange(pP._heatmapDisplayRange, 5));
        pP._scheme = WhiteScheme.getForHeatmap3D(0.25f);
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme.rescale(1.5f, SizeFields.AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE);
        pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE);
        pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE);
        pP._scheme.rescale(1.5f, SizeFields.AXIS3D_X_TITLE_FONT_SIZE_SCALE);
        pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Y_TITLE_FONT_SIZE_SCALE);
        pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Z_TITLE_FONT_SIZE_SCALE);
        pP._scheme.rescale(1.5f, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);

        Heatmap3D plot = new Heatmap3D(pP);

        Frame frame = new Frame(plot, 1000, 1000);
        //Frame frame = new Frame(plot, 1600, 1200);


        double[][][] data = new double[yDiv][xDiv][zDiv];
        boolean [][][] mask = new boolean[zDiv][yDiv][xDiv];

        BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{xDiv, yDiv, zDiv},
                new Range[]{RX, RY, RZ}, new LinearlyThresholded());

        IRandom R = new MersenneTwister64(0);

        int trials = 1000000;
        float std = 0.25f;

        for (int i = 0; i < trials; i++)
        {

            float x = (float) (RX.getLeft() + RX.getInterval() * 0.5f + R.nextGaussian() * std);
            float y = (float) (RY.getLeft() + RY.getInterval() * 0.5f + R.nextGaussian() * std);
            float z = (float) (RZ.getLeft() + RZ.getInterval() * 0.5f + R.nextGaussian() * std);

            int[] c = B.getBucketCoords(new double[]{x, y, z});
            if (c != null)
            {
                if (c[2] < (float) zDiv / 2) mask[c[2]][c[1]][c[0]] = true;
                data[c[2]][c[1]][c[0]]++;
            }

        }

        HeatmapDataProcessor hdp = new HeatmapDataProcessor();
        Coords[] SC = hdp.getCoords3D(xDiv, yDiv, zDiv, data);
        HeatmapDataProcessor.SortedValues SV = hdp.getSortedValues(SC, 3);
        plot.getModel().setDataAndPerformProcessing(SC, SV._sortedValues);

        plot.getModel().setValueFilterInTheNormalizedSpace(0.2f, 1.0f);
        plot.getModel().setMask(mask);

        // Adjust formatters:
        for (int i = 0; i < 3; i++)
            ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).getAxes()[i].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
