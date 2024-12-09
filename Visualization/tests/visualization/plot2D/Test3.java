package visualization.plot2D;

import color.Color;
import frame.Frame;
import plot.dummy.DummyColorPlot;
import plotswrapper.GridPlots;
import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotShrinkWrapper;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 2 (rows) x 3 (columns) dummy plots ({@link DummyColorPlot}) on a frame.
 * Also, listeners are checked (debug mode -> print to the console)
 * Plots are shrunk by 50% in wrappers.
 *
 * @author MTomczyk
 */
public class Test3
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    @SuppressWarnings("ExtractMethodRecommender")
    public static void main(String[] args)
    {
        DummyColorPlot plot1 = new DummyColorPlot(Color.RED);
        DummyColorPlot plot2 = new DummyColorPlot(Color.GREEN);
        DummyColorPlot plot3 = new DummyColorPlot(Color.BLUE);
        DummyColorPlot plot4 = new DummyColorPlot(Color.WHITE);
        DummyColorPlot plot5 = new DummyColorPlot(Color.GRAY_50);
        DummyColorPlot plot6 = new DummyColorPlot(Color.BLACK);

        AbstractPlotWrapper[] wrappers = new AbstractPlotWrapper[]{
                new PlotShrinkWrapper(plot1, 0.2f, 0.8f),
                new PlotShrinkWrapper(plot2, 0.2f, 0.8f),
                new PlotShrinkWrapper(plot3, 0.2f, 0.8f),
                new PlotShrinkWrapper(plot4, 0.2f, 0.8f),
                new PlotShrinkWrapper(plot5, 0.2f, 0.8f),
                new PlotShrinkWrapper(plot6, 0.2f, 0.8f),
        };

        GridPlots wrapper = new GridPlots(wrappers, 2, 3);

        Frame.Params pF = Frame.Params.getParams(wrapper, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));

        Frame frame = new Frame(pF);

        frame.setVisible(true);
    }
}
