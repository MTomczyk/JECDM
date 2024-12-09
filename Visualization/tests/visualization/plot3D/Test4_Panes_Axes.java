package visualization.plot3D;

import color.Color;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.FromFixedInterval;
import component.axis.ticksupdater.ITicksDataGetter;
import component.drawingarea.DrawingArea3D;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import space.Range;
import space.normalization.minmax.Gamma;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test4_Panes_Axes
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
        pP._scheme._colors.put(ColorFields.PANE_3D_BACKGROUND, Color.GREEN);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        pP._axesAlignments = new Align[] {
                Align.FRONT_BOTTOM, Align.FRONT_TOP, Align.FRONT_LEFT, Align.FRONT_RIGHT,
                Align.BACK_TOP, Align.BACK_BOTTOM, Align.BACK_LEFT, Align.BACK_RIGHT,
                Align.LEFT_TOP, Align.LEFT_BOTTOM, Align.RIGHT_BOTTOM, Align.RIGHT_TOP
        };

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        ITicksDataGetter xTDG = new FromFixedInterval(Range.getNormalRange(), 8);
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Gamma(3.0f));
        ITicksDataGetter yTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[1],5);
        yTDG.setForcedUnnormalizedLocations(new float[]{0.0f, 0.25f, 0.5f, 0.75f, 1.0f});
        pP._pDisplayRangesManager._DR[2].setNormalizer(new Gamma(2.0f));
        ITicksDataGetter zTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[2],4);
        zTDG.setForcedUnnormalizedLocations(new float[]{0.0f, 0.3333f, 0.6666f, 1.0f});
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setGridlinesTicksDataGetters(xTDG, yTDG, zTDG);

        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForXAxes(xTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForYAxes(yTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForZAxes(zTDG);
        plot.getModel().notifyDisplayRangesChangedListeners();

        frame.setVisible(true);


    }
}
