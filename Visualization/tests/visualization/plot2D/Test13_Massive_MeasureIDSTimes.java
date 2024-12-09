package visualization.plot2D;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;
import thread.swingtimer.reporters.IDSRecalculationTimesReporter;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Tests rendering times when many points are displayed.
 *
 * @author MTomczyk
 */
public class Test13_Massive_MeasureIDSTimes
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
        pP._drawLegend = false;

        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-1.0d, 1.0d),
                new Range(-1.0d, 1.0d));

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();
        ArrayList<IDataSet> dss = new ArrayList<>();
        {
            int points = 1000000;
            double[][] data = new double[points][2];
            for (int i = 0; i < points; i++)
            {
                data[i][0] = -1.0f + 2.0f * (float) i / (points - 1);
                float angle = (float) (data[i][0] * 2.0f * Math.PI);
                data[i][1] = Math.sin(angle);
            }
            MarkerStyle ms = new MarkerStyle(2.0f, Gradient.getViridisGradient(100, false), 0, Marker.SQUARE);
            LineStyle ls = new LineStyle(1.0f, Gradient.getCividisGradient(100, false), 0);
            IDataSet ds = DataSet.getFor2D("test data", data, ms, ls);
            dss.add(ds);
        }


        frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));
        plot.getModel().setDataSets(dss, true);


        frame.setVisible(true);
    }
}
