package visualization.plot3D;

import color.Color;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import space.Range;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test2_Panes_Axes
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
        pP._scheme = new WhiteScheme();

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";


        pP._paneAlignments = new Align[]{};
        pP._drawCube = false;
        pP._scheme._colors.put(ColorFields.AXIS3D_X, Color.GREEN);
        pP._scheme._colors.put(ColorFields.AXIS3D_Y, Color.RED);
        pP._scheme._colors.put(ColorFields.AXIS3D_Z, Color.BLUE);

        pP._scheme._colors.put(ColorFields.AXIS3D_X_TICK_LABEL_FONT, Color.CYAN);
        pP._scheme._colors.put(ColorFields.AXIS3D_Y_TICK_LABEL_FONT, Color.MAGENTA);
        pP._scheme._colors.put(ColorFields.AXIS3D_Z_TICK_LABEL_FONT, Color.YELLOW);

        pP._scheme._colors.put(ColorFields.AXIS3D_X_TITLE_FONT, Color.GRAY_75);
        pP._scheme._colors.put(ColorFields.AXIS3D_Y_TITLE_FONT, Color.GRAY_50);
        pP._scheme._colors.put(ColorFields.AXIS3D_Z_TITLE_FONT, Color.GRAY_25);

        DisplayRangesManager.Params pD = new DisplayRangesManager.Params();
        pD._DR = new DisplayRangesManager.DisplayRange[3];
        pD._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d));
        pD._DR[1] = new DisplayRangesManager.DisplayRange(new Range(0.0d, 2.0d));
        pD._DR[2] = new DisplayRangesManager.DisplayRange(new Range(2.0d, 4.0d));
        pD._attIdx_to_drIdx = new Integer[]{0, 1, 2};
        pP._pDisplayRangesManager = pD;


        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        frame.setVisible(true);

    }
}
