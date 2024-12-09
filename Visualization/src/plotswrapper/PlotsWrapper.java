package plotswrapper;

import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import plotwrapper.PlotWrapper;

import java.awt.*;

/**
 * Default plots wrapper that handles just one plot.
 *
 * @author MTomczyk
 */
public class PlotsWrapper extends AbstractPlotsWrapper
{
    /**
     * Parameterized constructor.
     *
     * @param abstractPlot plot to be displayed (default wrapper is used ({@link PlotWrapper}))
     */
    public PlotsWrapper(AbstractPlot abstractPlot)
    {
        this(new PlotWrapper(abstractPlot));
    }

    /**
     * Parameterized constructor.
     *
     * @param plotWrapper plot encapsulated using the wrapper object
     */
    public PlotsWrapper(AbstractPlotWrapper plotWrapper)
    {
        super(new Params(new AbstractPlotWrapper[]{plotWrapper}));
    }

    /**
     * Instantiates the layout.
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(Params p)
    {
        setLayout(new BorderLayout());
        add(p._wrappers[0], BorderLayout.CENTER);
    }
}
