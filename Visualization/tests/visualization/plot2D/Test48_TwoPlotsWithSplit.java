package visualization.plot2D;

import color.Color;
import frame.Frame;
import plot.AbstractPlot;
import plot.dummy.DummyColorPlot;
import plotswrapper.TwoPlotsHorizontallyWithSplit;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 2 plots using {@link plotswrapper.TwoPlotsHorizontallyWithSplit}
 *
 * @author MTomczyk
 */
public class Test48_TwoPlotsWithSplit
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        AbstractPlot plot1 = new DummyColorPlot(Color.RED);
        AbstractPlot plot2 = new DummyColorPlot(Color.BLUE);
        TwoPlotsHorizontallyWithSplit twoPlots = new TwoPlotsHorizontallyWithSplit(plot1, plot2);
        Frame frame = new Frame(twoPlots, 0.5f, 0.5f);

        frame.setVisible(true);
        twoPlots.hideLeftPane();
        twoPlots.hideRightPane();
        twoPlots.equallySpacePanel();
    }
}
