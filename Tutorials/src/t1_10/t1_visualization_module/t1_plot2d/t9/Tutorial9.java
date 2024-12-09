package t1_10.t1_visualization_module.t1_plot2d.t9;

import color.gradient.Gradient;
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
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;

/**
 * This tutorial provides basics on gradients.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial9
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
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        pP._drawLegend = true;

        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.CENTER_TOP);

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Using gFrame frame = new Frame(plot, 600, 600);

        ArrayList<IDataSet> dataSets = new ArrayList<>(2);

        {
            double[][] data = new double[][]
                    {
                            {0.0d, 1.0d},
                            {1.0d, 0.0d},
                    };
            // The line can be colored using a gradient. The following constructor creates a line style that uses a
            // Viridis gradient. Furthermore, the gradient color is determined using the first attribute value. The
            // provided zero index specifies a connection between the gradient and the first (zeroth) display range
            // in the display range manager. This connection is used, e.g., to derive the normalization function from
            // the display range to accordingly map the attribute value into value in the [0, 1] bound (the gradient
            // object maps a [0, 1] value into a color). Note that an error may occur if the display range at the
            // specified index does not exist.
            LineStyle ls = new LineStyle(1.0f, Gradient.getViridisGradient(), 0);
            dataSets.add(DataSet.getFor2D("DS1", data, ls));
        }

        {
            double[][] data = new double[][]
                    {
                            {0.0d, 0.0d},
                            {0.5d, 0.5d},
                            {1.0d, 1.0d}
                    };

            // Markers can also be colorized using a gradient (it is also true for their edges). This can be achieved
            // similarly to how a gradient for the line was specified (the following lines bound the marker edge gradient
            // (red-blue) and the marker fill gradient (inferno) with the second display range).
            LineStyle mes = new LineStyle(1.0f, Gradient.getRedBlueGradient(), 1);
            MarkerStyle ms = new MarkerStyle(5.0f, Gradient.getInfernoGradient(), 1, Marker.CIRCLE, mes);
            dataSets.add(DataSet.getFor2D("DS2", data, ms));
        }

        plot.getModel().setDataSets(dataSets, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
