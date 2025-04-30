package visualization.plot3D;

import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test35_LineWidths
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._zAxisTitle = "Z-Axis Test (j)";

        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;
        pP._drawLegend = false;
        pP._useAlphaChannel = false;
        pP._scheme = WhiteScheme.getForPlot3D();
        pP._scheme._sizes.put(SizeFields.CUBE3D_LINES_WIDTH, 3.0f);

        pP._scheme._sizes.put(SizeFields.AXIS3D_X_MAIN_LINE_WIDTH, 1.0f);
        pP._scheme._sizes.put(SizeFields.AXIS3D_X_TICK_LINE_WIDTH, 2.0f);
        pP._scheme._sizes.put(SizeFields.AXIS3D_Y_MAIN_LINE_WIDTH, 3.0f);
        pP._scheme._sizes.put(SizeFields.AXIS3D_Y_TICK_LINE_WIDTH, 4.0f);
        pP._scheme._sizes.put(SizeFields.AXIS3D_Z_MAIN_LINE_WIDTH, 5.0f);
        pP._scheme._sizes.put(SizeFields.AXIS3D_Z_TICK_LINE_WIDTH, 6.0f);

        pP._scheme._sizes.put(SizeFields.PANE3D_BOTTOM_LINES_WIDTH, 6.0f);
        pP._scheme._sizes.put(SizeFields.PANE3D_RIGHT_LINES_WIDTH, 1.0f);
        pP._scheme._sizes.put(SizeFields.PANE3D_BACK_LINES_WIDTH, 3.0f);



        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);
    }
}
