package plotwrapper;

import container.Notification;
import listeners.MousePressedLooseFocus;

/**
 * Controller for the {@link AbstractPlotWrapper} class.
 *
 * @author MTomczyk
 */
public class PlotWrapperController
{
    /**
     * Reference to plot wrapper.
     */
    protected final AbstractPlotWrapper _plotWrapper;

    /**
     * MVC model: plot wrapper model.
     */
    protected PlotWrapperModel _M;

    /**
     * Mouse click -> loose focus to all plots (listener -> action).
     * The goal is to lose focus on all plots anytime something that is not a plot is clicked.
     */
    protected MousePressedLooseFocus _mousePressedLooseFocus;

    /**
     * Parameterized constructor.
     *
     * @param plotWrapper reference to plot wrapper.
     */
    public PlotWrapperController(AbstractPlotWrapper plotWrapper)
    {
        _plotWrapper = plotWrapper;
    }

    /**
     * Setter for the plot wrapper model.
     *
     * @param M plot wrapper model
     */
    public void setPlotWrapperModel(PlotWrapperModel M)
    {
        _M = M;
    }

    /**
     * Instantiates background threads (also calls the same method for descendants (plots)).
     */
    public void instantiateBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper controller [id = " + _M.getPlotID() + "]: instantiate background threads method called");
        if (_M._plot != null) _M._plot.getController().instantiateBackgroundThreads();
    }

    /**
     * Starts all background threads (also calls the same method for descendants (plots)).
     */
    public void startBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper controller [id = " + _M.getPlotID() + "]: start background threads method called");
        if (_M._plot != null) _M._plot.getController().startBackgroundThreads();
    }

    /**
     * Stops all background threads (also calls the same method for descendants (plots)).
     */
    public void stopBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper controller [id = " + _M.getPlotID() + "]: stop background threads method called");
        if (_M._plot != null) _M._plot.getController().stopBackgroundThreads();
    }

    /**
     * Instantiates and registers listeners (also calls the same method for descendants (plots)).
     */
    public void instantiateListeners()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper controller [id = " + _M.getPlotID() + "]: instantiate listeners method called");
        _mousePressedLooseFocus = new MousePressedLooseFocus(_M._GC);
        _plotWrapper.addMouseListener(_mousePressedLooseFocus);
        if (_M._plot != null) _M._plot.getController().instantiateInteractListener();
    }

    /**
     * Unregisters listeners (also calls the same method for descendants (plots)).
     */
    public void unregisterListeners()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper controller [id = " + _M.getPlotID() + "]: unregister listeners method called");
        _plotWrapper.removeMouseListener(_mousePressedLooseFocus);
        if (_M._plot != null) _M._plot.getController().unregisterListeners();
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_M._GC, null, "Plot wrapper controller [id = " + _M.getPlotID() + "]: dispose method called");
        _mousePressedLooseFocus = null;
        _M = null;
    }
}
