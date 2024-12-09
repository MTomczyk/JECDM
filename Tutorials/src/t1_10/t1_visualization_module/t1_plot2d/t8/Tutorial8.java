package t1_10.t1_visualization_module.t1_plot2d.t8;

import color.gradient.Color;
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
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.Range;

import java.util.ArrayList;

/**
 * This tutorial provides basics on creating and uploading data sets (multiple data sets and legend).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial8
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
        // The legend can be customized by adjusting selected fields in the scheme object. The below three lines set
        // the legend background to a light gray and place it outside the plot area (right/top position). Additionally,
        // the right plot margin is extended to fit the legend.
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP_EXTERNAL);
        pP._scheme._colors.put(ColorFields.LEGEND_BACKGROUND, new color.Color(0.8f, 0.8f, 0.8f));
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.175f);

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        // This tutorial creates 5 data sets. The first two are renderable, and their legend entries are displayable.
        // The third data set is renderable, but its legend entry is not displayable. The fourth data set uses the
        // opposite properties. The last data set is neither renderable nor has a displayable legend entry.
        ArrayList<IDataSet> dataSets = new ArrayList<>(2);
        {
            // renderable and entry legend displayable
            double[][] data = new double[][]
                    {
                            {0.0d, 1.0d},
                            {1.0d, 0.0d},
                    };
            LineStyle ls = new LineStyle(1.0f, Color.BLUE);
            dataSets.add(DataSet.getFor2D("DS1", data, ls));
        }
        {   // renderable and entry legend displayable
            double[][] data = new double[][]
                    {
                            {0.0d, 0.0d},
                            {0.5d, 0.5d},
                            {1.0d, 1.0d}
                    };
            LineStyle mes = new LineStyle(1.0f, Color.BLACK);
            MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.CIRCLE, mes);
            dataSets.add(DataSet.getFor2D("DS2", data, ms));
        }
        {   // renderable but entry legend is not displayable
            double[][] data = new double[][]
                    {
                            {0.25d, 0.75d},
                            {0.5d, 0.75d},
                            {0.75d, 0.75d}
                    };
            MarkerStyle ms = new MarkerStyle(5.0f, Color.GREEN, Marker.SQUARE);
            DataSet ds = DataSet.getFor2D("DS3", data, ms);
            // Set ``displayable on legend'' flag to false (disables legend entry rendering)
            ds.setDisplayableOnLegend(false);
            dataSets.add(ds);
        }
        {   // not renderable but entry legend is displayable
            double[][] data = new double[][]{}; // data is not needed
            LineStyle edge = new LineStyle(1.0f, Color.BLACK);
            MarkerStyle ms = new MarkerStyle(5.0f, Color.BLUE, Marker.TRIANGLE_UP, edge);
            DataSet ds = DataSet.getFor2D("DS4", data, ms);
            // Set ``skip rendering'' flag to true (disables data set rendering; but legend entry will be rendered)
            ds.setSkipRendering(true);
            dataSets.add(ds);
        }
        {   // not renderable and entry legend is not displayable
            double[][] data = new double[][]{}; // data is not needed
            MarkerStyle ms = new MarkerStyle(5.0f, Color.BLACK, Marker.TRIANGLE_DOWN);
            DataSet ds = DataSet.getFor2D("DS5", data, ms);
            // Set ``displayable on legend'' flag to false (disables legend entry rendering)
            ds.setDisplayableOnLegend(false);
            // Set ``skip rendering'' flag to true (disables data set rendering)
            ds.setSkipRendering(true);
            dataSets.add(ds);
        }


        plot.getModel().setDataSets(dataSets, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
