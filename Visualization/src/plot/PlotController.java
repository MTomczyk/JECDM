package plot;

import container.Notification;
import listeners.interact.AbstractInteractListener;
import listeners.interact.InteractListener;
import popupmenu.AbstractRightClickPopupMenu;

/**
 * Controller for the {@link AbstractPlot} class.
 *
 * @author MTomczyk
 */
public class PlotController
{
    /**
     * Reference to plot.
     */
    protected final AbstractPlot _plot;

    /**
     * Reference to plot model.
     */
    protected PlotModel _M;

    /**
     * Interact listener (capture mouse + key related events).
     */
    protected AbstractInteractListener _interactListener = null;

    /**
     * Reference to the right-click popup menu.
     */
    protected AbstractRightClickPopupMenu _menu = null;


    /**
     * Parameterized constructor.
     *
     * @param plot reference to the plot.
     */
    public PlotController(AbstractPlot plot)
    {
        _plot = plot;
    }

    /**
     * Setter for the plot model.
     *
     * @param M plot model
     */
    public void setPlotModel(PlotModel M)
    {
        _M = M;
    }

    /**
     * Instantiates background threads.
     */
    public void instantiateBackgroundThreads()
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: instantiate background threads method called");
    }

    /**
     * Starts all background threads.
     */
    public void startBackgroundThreads()
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: start background threads method called");
    }

    /**
     * Stops all background threads.
     */
    public void stopBackgroundThreads()
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: stop background threads called");
    }

    /**
     * Auxiliary method instantiating the default interact listeners.
     */
    public void instantiateInteractListener()
    {
        _interactListener = new InteractListener(_M._GC, _M._PC);
        _plot.addMouseListener(_interactListener);
        _plot.addMouseMotionListener(_interactListener);
    }

    /**
     * Unregisters the interact listener.
     */
    public void unregisterListeners()
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: unregister listeners method called");
        if (_interactListener != null)
        {
            _plot.removeMouseListener(_interactListener);
            _plot.removeMouseMotionListener(_interactListener);
        }

        if (_menu != null) _menu.unregisterListeners();
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: dispose method called");
        if (_interactListener != null) _interactListener.dispose();
        _interactListener = null;

        if (_menu != null)
        {
            _menu.dispose();
            _menu = null;
        }
        _M = null;
    }

    /**
     * Getter for the interact listener.
     *
     * @return interact listener
     */
    public AbstractInteractListener getInteractListener()
    {
        return _interactListener;
    }

    /**
     * Allows adding a right-click popup menu.
     * If some menu is already assigned, it is removed prior addition (replacement). The method sets menu containers and
     * also accordingly sets the listener so that the menu can be displayed upon the right mouse button.
     * click ({@link listeners.interact.AbstractInteractListener}).
     *
     * @param menu popup menu
     */
    public void addRightClickPopupMenu(AbstractRightClickPopupMenu menu)
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: right click popup menu added");
        if (_menu != null) _plot.remove(_menu);
        _menu = menu;
        _plot.add(_menu);
        _menu.setContainers(_M._GC, _M._PC);
        _menu.updateScheme(_M._scheme);
    }

    /**
     * Setter for the flag determining whether the plot is focused (active and ready for interactions).
     *
     * @param focused true -> plot is active and ready for interactions; false otherwise
     */
    public void setFocused(boolean focused)
    {
        Notification.printNotification(_M._GC, _M._PC, "Plot controller [id = " + _M.getPlotID() + "]: new focus value = " + focused);
        _M._focused = focused;
    }

    /**
     * Shows the popup menu (if set) at specified coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void showPopupMenu(int x, int y)
    {
        if (_menu == null) return;
        _menu.setLocation(x, y);
        _menu.setVisible(true);
    }


    /**
     * Hides the popup menu (if set) at specified coordinates.
     */
    public void hidePopup()
    {
        if (_menu == null) return;
        _menu.setVisible(false);
    }

    /**
     * Can be called to check if the plot is active and ready for interactions.
     *
     * @return true -> plot is active and ready for interactions; false otherwise
     */
    public boolean isFocused()
    {
        return _M._focused;
    }

}
