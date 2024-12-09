package plotswrapper;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotWrapper;

/**
 * Plot container that organizes two plots horizontally.
 *
 * @author MTomczyk
 */
public class TwoPlotsHorizontally extends GridPlots
{
    /**
     * Params container.
     */
    public static class Params extends GridPlots.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param leftPlot  left plot (default wrapper is used)
         * @param rightPlot right plot (default wrapper is used)
         */
        public Params(AbstractPlot leftPlot, AbstractPlot rightPlot)
        {
            this(new PlotWrapper(leftPlot), new PlotWrapper(rightPlot));
        }

        /**
         * Parameterized constructor.
         *
         * @param leftWrapper  left plot wrapper
         * @param rightWrapper right plot wrapper
         */
        public Params(AbstractPlotWrapper leftWrapper, AbstractPlotWrapper rightWrapper)
        {
            super(new AbstractPlotWrapper[]{leftWrapper, rightWrapper}, 1, 2);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param leftPlot  left plot (default wrapper is used)
     * @param rightPlot rightPlot (default wrapper is used)
     */
    public TwoPlotsHorizontally(AbstractPlot leftPlot, AbstractPlot rightPlot)
    {
        this(new PlotWrapper(leftPlot), new PlotWrapper(rightPlot));
    }

    /**
     * Parameterized constructor.
     *
     * @param leftWrapper  left plot wrapper
     * @param rightWrapper right plot wrapper
     */
    public TwoPlotsHorizontally(AbstractPlotWrapper leftWrapper, AbstractPlotWrapper rightWrapper)
    {
        this(new Params(leftWrapper, rightWrapper));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public TwoPlotsHorizontally(Params p)
    {
        super(p);
    }
}
