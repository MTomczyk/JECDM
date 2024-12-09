package visualization.plot2D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DummyDataSet;
import dataset.IDataSet;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Tests plot layout + display ranges.
 *
 * @author MTomczyk
 */
public class Test9
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
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._sizes.put(SizeFields.MARGIN_BOTTOM_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._sizes.put(SizeFields.MARGIN_LEFT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._aligns.put(AlignFields.COLORBAR, Align.RIGHT);

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "colorbar", new FromDisplayRange(pP._pDisplayRangesManager._DR[0], 5));

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        IDataSet ds1 = new DummyDataSet("DS 1", null);
        IDataSet ds2 = new DummyDataSet("DS 2", null);
        IDataSet ds3 = new DummyDataSet("DS 3", null);
        ArrayList<IDataSet> dss = new ArrayList<>();
        dss.add(ds1);
        dss.add(ds2);
        dss.add(ds3);
        plot.getModel().setDataSets(dss, true);

        ArrayList<IDataSet> dss2 = new ArrayList<>();
        dss2.add(new DummyDataSet("DS 1", null));
        dss2.add(new DummyDataSet("DS 2", new double[][]{{-1.0d, -1.0d}, {0.0d, 0.0d}, {1.0d, 1.0d}}));
        dss2.add(new DummyDataSet("DS 3", new double[][]{{0.0d, 2.0d}}));

        plot.getModel().setDataSets(dss2, true);

        frame.setVisible(true);
    }
}
