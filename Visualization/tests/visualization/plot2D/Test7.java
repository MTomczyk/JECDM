package visualization.plot2D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromFixedInterval;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import scheme.AbstractScheme;
import scheme.TestScheme;
import space.Range;

import java.text.DecimalFormat;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Tests plot layout.
 *
 * @author MTomczyk
 */
public class Test7
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
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Test colorbar axis",
                new FromFixedInterval(Range.getNormalRange(), 5, new DecimalFormat("0.##E0")));
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();

        Plot2D plot = new Plot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        AbstractScheme s = new TestScheme();
        plot.updateScheme(s);

       // plot.getModel().notifyDisplayRangesChangedListeners();

        plot.getComponentsContainer().getAxes()[0].setTickLabels(new String[]{"T1", "T2", "T3"});
        plot.getComponentsContainer().getAxes()[1].setTickLabels(-1.0d, 1.0d, 5, new DecimalFormat("0.##E0"));
        plot.getComponentsContainer().getColorbar().getAxis().setTickLabels(-1.0d, 1.0d, 5, new DecimalFormat("0.##E0"));

        frame.setVisible(true);
    }
}
