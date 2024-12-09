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
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test26_OnDemand
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    @SuppressWarnings("ExtractMethodRecommender")
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
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();

        // marker: fill = red, no edge; no line
        IDataSet ds1 = DataSet.getFor2D("DS 1   aaaa", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE));
        // marker: fill = red, edge = black; no line
        IDataSet ds2 = DataSet.getFor2D("DS 2j", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Color.GREEN)));
        // marker: fill = viridis, edge = none; no line
        IDataSet ds3 = DataSet.getFor2D("DS 3", (double[][]) null, new MarkerStyle(5, Gradient.getViridisGradient(), 0, Marker.SQUARE));
        // marker: fill = none, edge = green; no line
        IDataSet ds4 = DataSet.getFor2D("DS 4", (double[][]) null, new MarkerStyle(5, null, Marker.SQUARE, new LineStyle(1.0f, Color.GREEN)));
        // marker: fill = red, edge = viridis; no line
        IDataSet ds5 = DataSet.getFor2D("DS 5", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Gradient.getViridisGradient(), 0)));
        // marker: fill = red, edge = viridis; black line
        IDataSet ds6 = DataSet.getFor2D("DS 6", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Gradient.getViridisGradient(), 0)), new LineStyle(2.0f, Color.BLACK));
        // no marker; blue->red gradient
        IDataSet ds7 = DataSet.getFor2D("DS 7", (double[][]) null, new LineStyle(5.0f, Gradient.getBlueRedGradient(), 0));

        ArrayList<IDataSet> dss = new ArrayList<>();
        dss.add(ds1);
        dss.add(ds2);
        dss.add(ds3);
        dss.add(ds4);
        dss.add(ds5);
        dss.add(ds6);
        dss.add(ds7);

        plot.getModel().setDataSets(dss, true);

        // ======================================================
        ArrayList<IDataSet> dss2 = new ArrayList<>();
        dss2.add(DataSet.getFor2D("DS 1   aaaa", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE)));
        dss2.add(DataSet.getFor2D("DS 2j", new double[][]{{-1.0d, -1.0d}, {0.0d, 0.0d}, {1.0d, 1.0d}}, new MarkerStyle(5, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Color.GREEN))));
        dss2.add(DataSet.getFor2D("DS 3", new double[][]{{0.0d, 2.0d}}, new MarkerStyle(5, Gradient.getViridisGradient(), 0, Marker.SQUARE)));
        dss2.add(DataSet.getFor2D("DS 4", (double[][]) null, new MarkerStyle(5, null, Marker.SQUARE, new LineStyle(1.0f, Color.GREEN))));
        dss2.add(DataSet.getFor2D("DS 5", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Gradient.getViridisGradient(), 0))));
        dss2.add(DataSet.getFor2D("DS 6", (double[][]) null, new MarkerStyle(5, Color.RED, Marker.SQUARE, new LineStyle(1.0f, Gradient.getViridisGradient(), 0)), new LineStyle(2.0f, Color.BLACK)));
        dss2.add(DataSet.getFor2D("DS 7", (double[][]) null, new LineStyle(5.0f, Gradient.getBlueRedGradient(), 0)));

        plot.getModel().setDataSets(dss2, true);

        frame.setVisible(true);

        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        pP._pDisplayRangesManager._DR[0].getR().setRight(2.0d);
        pP._pDisplayRangesManager._DR[0].getNormalizer().setMinMax(-0.0d, 2.0d);
        plot.getModel().updatePlotIDSsAndRenderOnDemand();
        plot.getModel().notifyDisplayRangesChangedListeners();
    }
}
