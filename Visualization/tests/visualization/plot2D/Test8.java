package visualization.plot2D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.TestScheme;
import space.Range;

import java.text.DecimalFormat;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Tests plot layout + display ranges.
 *
 * @author MTomczyk
 */
public class Test8
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
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), new Range(2.0d, 5.0d));
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Test colorbar axis",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[1],
                        5, new DecimalFormat("0.##E0")));

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.updateScheme(new TestScheme());

        plot.getModel().notifyDisplayRangesChangedListeners();
        plot.getComponentsContainer().getAxes()[0].setTickLabels(new String[]{"T1", null, "T2", null, "T3", null});

        frame.setVisible(true);
    }
}
