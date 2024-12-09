package visualization.plot2D;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import dataset.painter.style.size.FixedSize;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;


/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Tests per-pixel precision when displaying extreme points (and the middle one).
 *
 * @author MTomczyk
 */
public class Test23_ClipTest
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = false;
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._scheme._sizes.put(SizeFields.DRAWING_AREA_BORDER_WIDTH_FIXED, 0.0f);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();

        MarkerStyle ms = new MarkerStyle(0.5f, Color.RED, Marker.SQUARE);
        ms._relativeSize = new FixedSize(21);
        IDataSet ds = DataSet.getFor2D("data", new double[][]{{0.0d, 0.0d}, {1.0d, 0.0d}, {0.0d, 1.0d}, {1.0d, 1.0d}, {0.5d, 0.5d}}, ms);

        plot.getModel().setDataSet(ds, true);
        frame.setVisible(true);

    }
}
