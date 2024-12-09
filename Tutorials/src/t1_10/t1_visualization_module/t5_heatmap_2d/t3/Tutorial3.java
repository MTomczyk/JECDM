package t1_10.t1_visualization_module.t5_heatmap_2d.t3;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;

import java.text.DecimalFormat;


/**
 * This tutorial focuses on creating and displaying a basic 2D heatmap (using non-liner scales).
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
        Heatmap2D.Params pP = new Heatmap2D.Params();

        pP._xDiv = 3;
        pP._yDiv = 4;

        pP._xAxisTitle = null;
        pP._yAxisTitle = null;

        pP._drawMainGridlines = true;
        pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;

        pP._yAxisWithBoxTicks = true;
        pP._xAxisWithBoxTicks = true;

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());

        // The bucket coordinates are obtained using a separate normalizer (normalizer independent of the one set in the
        // associated display range). The two lines below show how to specify the normalizer for bucket coordinates.
        pP._xBucketCoordsNormalizer = new Gamma(0.5f);
        pP._yBucketCoordsNormalizer = new Gamma(2.0f);

        pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true);

        // A heatmap used to color the boxes (buckets) can also employ a nonlinear scale (see the line below).
        pP._heatmapDisplayRange.setNormalizer(new Gamma(0.5f));

        pP._gradient = Gradient.getViridisGradient();

        // Let's  draw the legend.
        pP._drawLegend = true;

        // Also, let's draw the colorbar. Notice that the ticks data getter now points to pP._heatmapDisplayRange.
        pP._colorbar = new Colorbar(pP._gradient, "Value", new FromDisplayRange(pP._heatmapDisplayRange, 5));
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        Heatmap2D plot = new Heatmap2D(pP);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 600, 600);

        double [][] data = new double[][]
                {
                        {2.0f, 2.0f, 3.0f},
                        {2.0f, 3.0f, 2.0f},
                        {2.0f, 5.0f, 2.0f},
                        {1.0f, 2.0f, 1.0f},
                };

        plot.getModel().setDataAndPerformProcessing(data);


        // Extra: a heatmap is just an additional layer rendered over the drawing area of Plot2D, and the functionality
        // of this regular plot is not hidden. Therefore, one can still display "standard" data sets (see the below code block).
        {
            double [][] plot2D_data = new double[][]
                    {
                            {0.0f, 0.0f},
                            {0.5f, 0.5f},
                            {1.0f, 1.0f}
                    };
            MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE);
            LineStyle ls = new LineStyle(1.0f, Color.BLACK);
            IDataSet ds = DataSet.getFor2D("Plot 2D data set", plot2D_data, ms, ls);
            plot.getModel().setDataSet(ds);
        }


        plot.getComponentsContainer().getAxes()[0].getTicksDataGetter().setPredefinedTickLabels(new String[]{"COL 0", "COL 1", "COL 2"});
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setPredefinedTickLabels(new String[]{"ROW 0", "ROW 1", "ROW 2", "ROW3"});

        // The ticks can be adjusted similarly to how it is done in Plot2D.
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat());
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f});

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
