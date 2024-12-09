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
import space.normalization.minmax.Logarithmic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This tutorial provides basics on scale customization (axes and grids customized).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial14d
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Logarithmic());

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        Gradient gradient = Gradient.getInfernoGradient();
        NumberFormat numberFormat = new DecimalFormat();
        ITicksDataGetter tdg = new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 5, numberFormat);
        pP._colorbar = new Colorbar(gradient, "Y-value", tdg);

        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        Plot2D plot = new Plot2D(pP);

        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{1.0f / 32.0f, 1.0f / 16.0f, 1.0f / 8.0f, 1.0f / 4.0f, 1.0f / 2.0f, 1.0f});
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{1.0f / 32.0f, 1.0f / 16.0f, 1.0f / 8.0f, 1.0f / 4.0f, 1.0f / 2.0f, 1.0f});

       plot.getComponentsContainer().getAxes()[0].getTicksDataGetter().setPredefinedTickLabels(
                new String[]{"A", "B", "C", "D", "E"});

        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setNumberFormat(numberFormat);

        DrawingArea2D drawingArea = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();

       drawingArea.getMainGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 6));
        drawingArea.getMainGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{1.0f / 16.0f, 1.0f / 8.0f, 1.0f / 4.0f, 1.0f / 2.0f, 1.0f});

        drawingArea.getAuxGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 5));
        drawingArea.getAuxGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{1.0f / 32.0f + 1.0f / 64.0f,
                1.0f / 16.0f + 1.0f / 32.0f,
                1.0f / 8.0f + 1.0f / 16.0f,
                1.0f / 4.0f + 1.0f / 8.0f,
                1.0f / 2.0f + 1.0f / 4.0f});

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
