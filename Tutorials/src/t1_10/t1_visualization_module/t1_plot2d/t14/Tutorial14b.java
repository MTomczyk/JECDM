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
import space.normalization.minmax.Gamma;

/**
 * This tutorial provides basics on scale customization (altered normalization functions).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial14b
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

        // A display range can be supplied with a normalizer (min-max based) responsible for mapping the original
        // coordinate values into normalized space. The line below specifies that the Y-coordinate (the second display
        // range) should be normalized using min-max gamma normalization. This normalizer first interpolates the original
        // values into [0, 1] space and then uses a gamma transformation ([0, 1] value raised to the selected power).
        // This example uses a power of 0.25 to ``straighten the plot''. Important note: only the input data points are
        // normalized. If a line style is used, straight lines will be drawn between the normalized points, but the
        // lines will not be adjusted (bent).
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Gamma(0.25d));

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        Gradient gradient = Gradient.getInfernoGradient();
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
