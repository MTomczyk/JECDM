package visualization.heatmap3D;

import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import scheme.WhiteScheme;
import scheme.enums.Align;
import space.normalization.minmax.Gamma;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Heatmap3D}) on a frame.
 * Tests heatmap visualization.
 *
 * @author MTomczyk
 */
public class Test1_NonLinearBoxes
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Heatmap3D.Params pP = new Heatmap3D.Params();
        // pP._debugMode = true;
        pP._title = "Test heatmap";
        pP._xAxisTitle = "X - dimension";
        pP._yAxisTitle = "Y - dimension";
        pP._zAxisTitle = "Z - dimension";
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        // play with parameters and check
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;

        pP._xDiv = 2;
        pP._yDiv = 3;
        pP._zDiv = 4;

    /*    pP._horizontalGridLinesWithBoxTicks = true;
        pP._verticalGridLinesWithBoxTicks = true;
        pP._depthGridLinesWithBoxTicks = true;

        pP._xAxisWithBoxTicks = true;
        pP._yAxisWithBoxTicks = true;
        pP._zAxisWithBoxTicks = true;*/

        pP._xBucketCoordsNormalizer = new Gamma(2.0f);
        pP._yBucketCoordsNormalizer = new Gamma(3.0f);
        pP._zBucketCoordsNormalizer = new Gamma(0.25f);

        pP._paneAlignments = new Align[]{Align.LEFT, Align.RIGHT, Align.TOP, Align.BOTTOM, Align.FRONT, Align.BACK};


        // pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();

        Heatmap3D plot = new Heatmap3D(pP);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "Test heatmap";

        Frame frame = new Frame(pF);
        plot.getModel().notifyDisplayRangesChangedListeners();

        frame.setVisible(true);
    }
}
