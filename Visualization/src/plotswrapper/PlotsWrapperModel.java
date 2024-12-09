package plotswrapper;

import container.GlobalContainer;
import container.Notification;
import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;

/**
 * Model for the {@link AbstractPlotsWrapper} class.
 *
 * @author MTomczyk
 */
public class PlotsWrapperModel
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    protected GlobalContainer _GC;

    /**
     * Reference to plots wrapper.
     */
    protected final AbstractPlotsWrapper _plotsWrapper;

    /**
     * Reference to plots wrapper controller.
     */
    protected PlotsWrapperController _C;

    /**
     * Plots to be displayed (wrapped).
     */
    public AbstractPlotWrapper[] _wrappers;


    /**
     * Parameterized constructor.
     *
     * @param plotsWrapper reference to plots wrapper.
     * @param wrappers     plots to be displayed (wrapped).
     */
    public PlotsWrapperModel(AbstractPlotsWrapper plotsWrapper, AbstractPlotWrapper[] wrappers)
    {
        _wrappers = wrappers;
        _plotsWrapper = plotsWrapper;
        assignPlotIDs();
    }

    /**
     * Setter for the plots wrapper model.
     *
     * @param C plots wrapper controller
     */
    public void setPlotsWrapperController(PlotsWrapperController C)
    {
        _C = C;
    }

    /**
     * Flag that is immediately set to true when the frame termination began.
     */
    protected volatile boolean _isTerminating = false;

    /**
     * Can be called to assign unique IDs to contained plots (counts from 0);
     */
    protected void assignPlotIDs()
    {
        int id = 0;
        for (AbstractPlotWrapper w : _wrappers)
        {
            if (w == null) continue;
            w.getModel().setPlotID(id++);
        }
    }

    /**
     * Can be called to set the global container in this object and its descendants as well (plot wrapper -> plot).
     *
     * @param GC global container (shared object; stores references, provides various functionalities).
     */
    public void establishGlobalContainer(GlobalContainer GC)
    {
        _GC = GC;
        Notification.printNotification(_GC, null, "Plots wrapper model: global container is set");

        if (_wrappers != null)
        {
            for (AbstractPlotWrapper w : _wrappers)
            {
                if (w == null) continue;
                w.getModel().establishGlobalContainer(GC);
            }
        }
    }

    /**
     * Auxiliary method that updates all plots' IDS, starting from the second level.
     * The method is invoked by all window-resized-like events.
     */
    public void updatePlotsIDSsOnScreenResize()
    {
        if (_wrappers != null)
        {
            for (AbstractPlotWrapper apw : _wrappers)
            {
                if (apw == null) continue;
                apw.getModel().updatePlotIDSsOnScreenResize();
            }
        }
    }

    /**
     * Notify all plots' listeners for changes in display ranges.
     */
    public void notifyDisplayRangesChangedListeners()
    {
        Notification.printNotification(_GC, null, "Plots wrapper model: notify display ranges changed listeners");
        if (_wrappers != null)
            for (AbstractPlotWrapper w : _wrappers)
            {
                if (w == null) continue;
                AbstractPlot plot = w.getModel().getPlot();
                if (plot != null) plot.getModel().notifyDisplayRangesChangedListeners();
            }
    }

    /**
     * Returns the reference to the plot that is assigned the given ID.
     * If no such plot can be found, the method returns null
     *
     * @param plotID reference to the plot with a given id (null, if there is no such plot).
     * @return the plot
     */
    public AbstractPlot getPlot(int plotID)
    {
        if (_wrappers == null) return null;
        for (AbstractPlotWrapper apw : _wrappers)
            if (apw.getModel().getPlotID() == plotID)
                return apw.getModel().getPlot();
        return null;
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, null, "Plots wrapper model: dispose method called");

        if (_wrappers != null)
            for (AbstractPlotWrapper w : _wrappers)
                if (w != null) w.dispose();
        _C = null;
    }

    /**
     * Sets the termination flag to true.
     */
    public void notifyTerminating()
    {
        _isTerminating = true;
    }

    /**
     * Can be called to check if the termination began.
     *
     * @return true, if the termination began
     */
    public boolean isTerminating()
    {
        return _isTerminating;
    }

}
