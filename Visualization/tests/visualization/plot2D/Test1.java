package visualization.plot2D;

import color.Color;
import frame.Frame;
import plot.dummy.DummyColorPlot;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test simply displays a dummy plot ({@link DummyColorPlot}) on a frame.
 *
 * @author MTomczyk
 */
public class Test1
{
    /**
     * Runs the visualization.
     * @param args not used
     */
    public static void main(String [] args)
    {
        boolean debug = (args != null) && (args.length > 0) && (args[0].equals("T"));

        // DPI TEST SCALE
        System.setProperty("sun.java2d.uiScale", "1");
        // DPI TEST SCALE

        DummyColorPlot plot = new DummyColorPlot(Color.BLUE);
        Frame frame = new Frame(plot, 0.5f);

        frame.setVisible(true);
    }
}
