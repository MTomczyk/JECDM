package visualization.plot2D;

import color.Color;
import frame.Frame;
import plot.AbstractPlot;
import plot.dummy.DummyColorPlot;
import plotswrapper.GridPlots;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 2 (rows) x 3 (columns) dummy plots ({@link DummyColorPlot}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 *
 * @author MTomczyk
 */
public class Test2
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        DummyColorPlot plot1 = new DummyColorPlot(Color.RED);
        DummyColorPlot plot2 = new DummyColorPlot(Color.GREEN);
        DummyColorPlot plot3 = new DummyColorPlot(Color.BLUE);
        DummyColorPlot plot4 = new DummyColorPlot(Color.WHITE);
        DummyColorPlot plot5 = new DummyColorPlot(Color.GRAY_50);
        DummyColorPlot plot6 = new DummyColorPlot(Color.BLACK);

        AbstractPlot[] plots = new AbstractPlot[]{plot1, plot2, plot3, plot4, plot5, plot6};

        GridPlots wrapper = new GridPlots(plots, 2, 3);

        Frame.Params pF = Frame.Params.getParams(wrapper, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));

        Frame frame = new Frame(pF);

        frame.setVisible(true);
    }
}
