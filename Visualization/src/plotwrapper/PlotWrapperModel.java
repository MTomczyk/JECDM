package plotwrapper;

import container.GlobalContainer;
import container.Notification;
import plot.AbstractPlot;

/**
 * Model for the {@link AbstractPlotWrapper} class.
 *
 * @author MTomczyk
 */
public class PlotWrapperModel
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    protected GlobalContainer _GC;

    /**
     * Reference to plot wrapper.
     */
    protected final AbstractPlotWrapper _plotWrapper;

    /**
     * MVC model: plot wrapper controller.
     */
    protected PlotWrapperController _C;

    /**
     * Reference to the plot to be displayed.
     */
    protected AbstractPlot _plot;

    /**
     * Parameterized constructor.
     *
     * @param plotWrapper reference to plot wrapper.
     */
    public PlotWrapperModel(AbstractPlotWrapper plotWrapper)
    {
        _plotWrapper = plotWrapper;
    }

    /**
     * Setter for the plot wrapper controller.
     *
     * @param C plot wrapper controller
     */
    public void setPlotWrapperController(PlotWrapperController C)
    {
        _C = C;
    }


    /**
     * Plot getter.
     *
     * @return the plot
     */
    public AbstractPlot getPlot()
    {
        return _plot;
    }


    /**
     * Setter for the plot.
     *
     * @param plot plot
     */
    public void setPlot(AbstractPlot plot)
    {
        _plot = plot;
    }

    /**
     * Auxiliary method that updates plot IDS, starting from the second level.
     * The method is invoked by all window-resized-like events.
     */
    public void updatePlotIDSsOnScreenResize()
    {
        if (_plot != null) _plot.getModel().updatePlotIDSsAndRenderOnScreenResize();
    }

    /**
     * Setter for the wrapped plot id.
     *
     * @param id plot id
     */
    public void setPlotID(int id)
    {
        _plot.getModel().setPlotID(id);
    }

    /**
     * Getter for the wrapped plot id.
     *
     * @return plot id
     */
    public int getPlotID()
    {
        return _plot.getModel().getPlotID();
    }

    /**
     * Can be called to set a global container in this object and its descendants as well (plot).
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     */
    public void establishGlobalContainer(GlobalContainer GC)
    {
        _GC = GC;
        Notification.printNotification(_GC, null, "Plot wrapper model [id = " + _plot.getModel().getPlotID() + "]: global container is set");
        if (_plot != null) _plot.getModel().establishGlobalContainer(GC);
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, null, "Plot wrapper model [id = " + getPlotID() + "]: dispose method called");
        if (_plot != null) _plot.dispose();
        _C = null;
    }
}
