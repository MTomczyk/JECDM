package plot;

import listeners.interact.InteractListener3D;

/**
 * Controller for the {@link Plot3D} class.
 *
 * @author MTomczyk
 */
public class Plot3DController extends PlotController
{
    /**
     * Reference to the interact listener (viewed as interact listener 3D).
     */
    private InteractListener3D _interactListener3D;

    /**
     * Parameterized constructor.
     *
     * @param plot reference to the plot.
     */
    public Plot3DController(AbstractPlot plot)
    {
        super(plot);
    }

    /**
     * Auxiliary method instantiating the default interact listeners.
     */
    @Override
    public void instantiateInteractListener()
    {
        _interactListener3D = new InteractListener3D(_M._GC, _M._PC);
        _interactListener = _interactListener3D;
        _plot.addMouseListener(_interactListener);
        _plot.addMouseMotionListener(_interactListener);
    }
}
