package visualization.heatmap2D;

import frame.Frame;
import plot.heatmap.Heatmap2D;
import scheme.WhiteScheme;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link plot.heatmap.Heatmap2D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test1
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap2D.Params pP = new Heatmap2D.Params();
        // pP._debugMode = true;
        pP._title = "Test heatmap";
        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = null;

        // play with parameters and check
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._xDiv = 7;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._xAxisWithBoxTicks = true;
        pP._yDiv = 5;
        pP._horizontalGridLinesWithBoxTicks = false;
        pP._yAxisWithBoxTicks = false;

        Heatmap2D plot = new Heatmap2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        frame.setVisible(true);
    }
}
