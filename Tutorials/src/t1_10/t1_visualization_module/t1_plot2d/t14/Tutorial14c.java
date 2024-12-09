package t1_10.t1_visualization_module.t1_plot2d.t14;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import component.colorbar.Colorbar;
import component.drawingarea.DrawingArea2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This tutorial provides basics on scale customization (axes and grids customized).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial14c
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Gamma(0.25d));

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        Gradient gradient = Gradient.getInfernoGradient();
        // Number format is associated with a ticks data getter and can be provided in the following way (for colorbar):
        NumberFormat numberFormat = new DecimalFormat();
        ITicksDataGetter tdg = new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 5, numberFormat);
        pP._colorbar = new Colorbar(gradient, "Y-value", tdg);

        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        Plot2D plot = new Plot2D(pP);

        // Now, imagine that we want the ticks of the Y-axis to be precisely located at [0, 0.2, 0.4,...,1] values. The
        // ticks' locations (and labels) can be customized only after the plot is instantiated. To access plot
        // components, one has to use plot.getComponentsContainer(). The container stores references to various plot
        // components (and provides various getters). We can use it to access the Y-axis via retrieving the axes array
        // (getAxes()) and the element under the index of 1 (y-axis). The received object is an extension of
        // component.axis.AbstractAxis and provides a getter to an implementation of ITicksDataGetter. We can use its
        // ``setForcedUnnormalizedLocations'' to specify an array of tick locations in the before-normalized space (they
        // will directly point to expected values). Similarly, a method called ``setForcedNormalizedLocations'' will
        // allow the adjustment of the ticks' locations in the normalized space (they will point to desired [0-1]
        // normalized positions).
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f});
        // Let's do the same but for the colorbar:
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f});

        // The ticks data getter also allows providing custom (non-numerical; data independent labels):
        plot.getComponentsContainer().getAxes()[0].getTicksDataGetter().setPredefinedTickLabels(
                new String[]{"A", "B", "C", "D", "E"});

        // Number format can be established in the following way (for Y-axis):
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setNumberFormat(numberFormat);

        // The following lines intend to adjust the positions of the grid lines (only the horizontal lines that should
        // be associated with the Y-axis's ticks). First, we need to derive the drawing area component:
        DrawingArea2D drawingArea = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();

        // Next, there are two types of grid components: the main grid and the auxiliary. Both can be customized. In
        // this case, the position adjustment process can also rely on a ticks data getter. The following four lines
        // specify appropriate tick data getters for the main and auxiliary grid and establish the (unnormalized) ticks'
        // locations.
        drawingArea.getMainGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 6));
        drawingArea.getMainGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f});

        drawingArea.getAuxGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 5));
        drawingArea.getAuxGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{0.1f, 0.3f, 0.5f, 0.7f, 0.9f});


        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 600, 600);

        int n = 100;
        double[][] data = new double[n][2];
        for (int i = 0; i < n; i++)
        {
            data[i][0] = (double) i / (n - 1);
            data[i][1] = 1.0d / (double) (i + 1);
        }

        LineStyle ls = new LineStyle(0.5f, Color.BLACK);
        MarkerStyle ms = new MarkerStyle(4.0f, gradient, 1, Marker.CIRCLE);

        IDataSet dataSet = DataSet.getFor2D("Data set", data, ms, ls);

        plot.getModel().setDataSet(dataSet, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
