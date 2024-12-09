package t1_10.t1_visualization_module.t1_plot2d.t14;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import component.colorbar.Colorbar;
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

/**
 * This tutorial provides basics on scale customization (default plot).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial14a
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        // Create default params with fixed normal ranges.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        Gradient gradient = Gradient.getInfernoGradient();
        // Let's associate the gradient with the y-scale.
        ITicksDataGetter tdg = new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 5);
        pP._colorbar = new Colorbar(gradient, "Y-value", tdg);

        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        Plot2D plot = new Plot2D(pP);

         Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

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
