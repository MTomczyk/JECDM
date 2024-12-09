package visualization.plot2D;

import frame.Frame;
import plot.Plot2D;
import scheme.TestScheme;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Tests customization of the main plot area + scheme overwrite
 *
 * @author MTomczyk
 */
public class Test6
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D plot = new Plot2D(new Plot2D.Params());
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.updateScheme(new TestScheme());

        frame.setVisible(true);
    }
}
