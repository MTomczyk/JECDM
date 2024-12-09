package visualization.plot2D;

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
import thread.swingtimer.reporters.IDSRecalculationTimesReporter;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test31_Legend_Surpassed_sizes
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();


        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();
        ArrayList<IDataSet> dss = new ArrayList<>();

        {
            MarkerStyle ms = new MarkerStyle(5.0f, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Color.BLACK,0 ,0.1f), 1.0f);
            double[][] d = new double[][]{{-1.0d, 1.0d}, {0.0d, 1.0d}, {1.0d, 1.0d}};
            IDataSet ds = DataSet.getFor2D("DS 1", d, ms);
            dss.add(ds);
        }

        {
            MarkerStyle ms = new MarkerStyle(3.0f, Gradient.getViridisGradient(20, false), 0, Marker.CIRCLE, 1.0f);
            double[][] d = new double[1000][2];
            for (int i = 0; i < 1000; i++)
            {
                d[i][0] = -1.0f + 2.0f * i / 999.0f;
                d[i][1] = 0.0f;
            }
            IDataSet ds = DataSet.getFor2D("DS 2", d, ms, null);
            dss.add(ds);
        }

       {
            LineStyle ls = new LineStyle(2.0f, Color.RED, 0.2f);
            MarkerStyle ms = new MarkerStyle(5.0f, Gradient.getViridisGradient(), 0, Marker.CIRCLE,
                    new LineStyle(1.0f, Gradient.getInfernoGradient(), 0, 0.2f), 1.0f);
            double[][] d = new double[][]{{-1.0d, -1.0d}, {0.0d, -1.0d}, {1.0d, -1.0d}};
            IDataSet ds = DataSet.getFor2D("DS 3", d, ms, ls);
            dss.add(ds);
        }


        {
            LineStyle ls = new LineStyle(2.0f, Gradient.getViridisGradient(), 0, 0.5f);
            double[][] d = new double[][]{{-1.0d, -0.5d}, {0.0d, -0.5d}, {1.0d, -0.5d}};
            IDataSet ds = DataSet.getFor2D("DS 4", d, null, ls);
            dss.add(ds);
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);


        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));
        plot.getModel().setDataSets(dss, true);

        frame.setVisible(true);
    }
}
