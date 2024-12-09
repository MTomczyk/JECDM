package t1_10.t1_visualization_module.t3_convergence_plot_2d.t1;

import color.gradient.Color;
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

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial focuses on creating and displaying a basic 2D convergence plot.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // The convergence plot is built on a regular Plot2D.
        Plot2D.Params pP = new Plot2D.Params();

        // The parameterization for a display range manager for the convergence plot differs from this for Plot2D.
        // Although the number of display ranges (and higher purpose) is the same, i.e., two: for the X and the Y axes,
        // the attribute-to-display-range mapping is different. Specifically, it is assumed that the input data points
        // for the convergence plot can take one of the two following forms: [X-coordinate, Y-coordinate] or
        // [X-coordinate, Y-coordinate, Y-coordinate + upper deviation, Y-coordinate - lower deviation]. The latter form
        // can be used if deviation-like data accompanies the original (X, Y) data and one wants to visualize it
        // (the convergence plot illustrates an envelope for this purpose). Notably, these two extra attributes should
        // be linked to the Y-axis. Therefore, the mapping for the convergence plot is, by default, set as
        // [0 (X-dimension), 1 (Y-dimension), 1, 1] instead of [0, 1]. Such suitably adjusted parameterization can be
        // obtained via the below getter. Important note: the X-axis is considered a timeline. Therefore, the first
        // attribute values of the input data points should be monotonically increasing.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();

        // Let's indicate that the X-axis is a timeline
        pP._xAxisTitle = "Iteration";
        // Let's think about the Y-axis as the performance value
        pP._yAxisTitle = "Performance";
        // We will draw a legend in this tutorial.
        pP._drawLegend = true;

        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);

        // The blow code block instantiates data series
        ArrayList<IDataSet> dataSets = new ArrayList<>();
        int iterations = 100;
        {
            // The following data set uses the two-element tuple representation (no deviation). It will be depicted
            // using a regular red line (no markers).
            double[][] data = new double[iterations][2];
            for (int i = 0; i < iterations; i++)
            {
                data[i][0] = i; // X-coordinate
                data[i][1] = 0.25f + 1.0f / Math.pow(i + 1, 0.5f); //Y-coordinate
            }
            LineStyle ls = new LineStyle(1.0f, Color.RED);
            // DataSet.getForConvergencePlot2D must be used to instantiate a data set to be illustrated in a convergence plot.
            IDataSet ds = DataSet.getForConvergencePlot2D("CP1", data, null, ls, null);
            dataSets.add(ds);
        }

        {
            // The below data uses the four-tuple representation (deviation is used). It will be depicted using a red
            // line and a transparent reddish envelope.
            double[][] data = new double[iterations][4];
            for (int i = 0; i < iterations; i++)
            {
                data[i][0] = i;
                data[i][1] = 1.0f / (i + 1);
                data[i][2] = data[i][1] + 0.1f;
                data[i][3] = data[i][1] - 0.1f;
            }
            LineStyle ls = new LineStyle(0.2f, Color.RED);
            // The envelope color can be provided as the last constructor parameter (transparency is supported).
            IDataSet ds = DataSet.getForConvergencePlot2D("CP2", data, ls,
                    new color.Color(1.0f, 0.0f, 0.0f, 0.25f));
            dataSets.add(ds);
        }

        {
            // This data will be depicted using an envelope only (no lines or markers).
            double[][] data = new double[iterations][4];
            for (int i = 0; i < iterations; i++)
            {
                data[i][0] = i;
                data[i][1] = 1.5f - (float) i /100.0f;
                data[i][2] = data[i][1] + 0.1f;
                data[i][3] = data[i][1] - 0.1f;
            }
            IDataSet ds = DataSet.getForConvergencePlot2D("CP3", data, (LineStyle) null,
                    new color.Color(0.0f, 1.0f, 0.0f, 0.25f));
            dataSets.add(ds);
        }

        {
            // This data will use markers (colored using gradients), the line, and the envelope. Note that the input
            // data points are densely spaced, meaning marker rendering may obscure visibility. However, one can adjust
            // the starting data point index (phase) and after how many data points a marker should be rendered (interval).
            // These can be specified as two marker style fields: ``_paintEvery'' and ''_startPaintingFrom''
            // (see the lines below).
            double[][] data = new double[iterations][4];
            for (int i = 0; i < iterations; i++)
            {
                data[i][0] = i;
                data[i][1] = 1.75f - (float) i /100.0f;
                data[i][2] = data[i][1] + 0.1f;
                data[i][3] = data[i][1] - 0.1f;
            }
            MarkerStyle ms = new MarkerStyle(2.0f, Gradient.getMagmaGradient(), 0, Marker.SQUARE);
            LineStyle ls = new LineStyle(0.5f, Color.BLUE);
            ms._paintEvery = 5;
            ms._startPaintingFrom = 5;
            IDataSet ds = DataSet.getForConvergencePlot2D("CP4", data, ms, ls,
                    new color.Color(0.0f, 0.0f, 1.0f, 0.25f));
            dataSets.add(ds);
        }
        {
            // The data set below demonstrates that the break can be introduced into rendered convergence curves
            // (in the same way as breaks are introduced into regular lines).
            LinkedList<double[][]> data = new LinkedList<>();
            {
                double[][] d = new double[40][4];
                for (int i = 0; i < 40; i++)
                {
                    d[i][0] = i;
                    d[i][1] = 2.0f - (float) i /100.0f;
                    d[i][2] = d[i][1] + 0.1f;
                    d[i][3] = d[i][1] - 0.1f;
                }
                data.add(d);
            }
            data.add(null);
            {
                double[][] d = new double[40][4];
                int start = 60;
                for (int i = start; i < iterations; i++)
                {
                    d[i - start][0] = i;
                    d[i - start][1] = 2.0f - (float) i /100.0f;
                    d[i - start][2] = d[i - start][1] + 0.1f;
                    d[i - start][3] = d[i - start][1] - 0.1f;
                }
                data.add(d);
            }

            MarkerStyle ms = new MarkerStyle(2.0f, Gradient.getRedBlueGradient(), 0, Marker.CIRCLE);
            ms._paintEvery = 10;
            ms._startPaintingFrom = 5;
            IDataSet ds = DataSet.getForConvergencePlot2D("CP5", data, ms,
                    new color.Color(1.0f, 0.0f, 1.0f, 0.25f));
            dataSets.add(ds);
        }

        Plot2D plot = new Plot2D(pP);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot,  600, 600);

        plot.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
