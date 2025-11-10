package visualization.plot3D;

import component.drawingarea.DrawingArea3D;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

/**
 * Tests 3D text parsing.
 *
 * @author MTomczyk
 */
public class Test36_TextParsing
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
       /* // DPI TEST SCALE
        System.setProperty("sun.java2d.uiScale", "1");
        // DPI TEST SCALE*/

        Plot3D.Params pP = new Plot3D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "$X$-ax$is$ $T$es$_{tt}$ (j)";
        //   pP._xAxisTitle = "$X$-ax$is$ $T$es$_{tt}$ (j)";
        pP._yAxisTitle = "$f_1$";
        pP._zAxisTitle = "$Z-Axi$_s T^est (j)";

        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;
        pP._drawLegend = false;
        pP._useAlphaChannel = false;


        pP._scheme = new WhiteScheme();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = null;//DisplayRangesManager.Params.getFor3D();

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setLabelsForXAxes(new String[]{"A", "B$_2$", "C", "D_1", "E"});

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        //   frame.getModel().getPlotsWrapper().getController().addReporter(new IDSRecalculationTimesReporter(frame.getModel().getGlobalContainer()));
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        frame.setVisible(true);
    }
}
