package plotwrapper;

import plot.AbstractPlot;

/**
 * Default implementation of {@link AbstractPlotWrapper} that assumes plot wrapper = plot.
 *
 * @author MTomczyk
 */
public class PlotWrapper extends AbstractPlotWrapper
{
    /**
     * Parameterized constructor.
     *
     * @param plot plot to be displayed
     */
    public PlotWrapper(AbstractPlot plot)
    {
        super(new Params(plot));
    }
}
