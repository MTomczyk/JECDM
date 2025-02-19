package plotswrapper;

import container.GlobalContainer;
import container.Notification;
import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

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
     * Auxiliary method for checking if the input plot is already in used by this component (uses the .equals method for comparison)
     *
     * @param plot input plot
     * @return true, if the plot is already in the component, false otherwise
     */
    private boolean isPlotAlreadyUsed(AbstractPlot plot)
    {
        if (_wrappers == null) return false;
        for (AbstractPlotWrapper apw : _wrappers)
            if ((apw != null) && (apw.getModel().getPlot().equals(plot))) return true;
        return false;
    }

    /**
     * This method replaces an already existing plot with a new one. Use with caution. The procedure temporarily
     * disables associated background threads and the execution queue (and removes execution blocks of the plot to be
     * removed). Nonetheless, trying, e.g., to update plot data simultaneously with its replacement, should be avoided.
     * The removed plot  will be disposed ({@link AbstractPlot#dispose()}). The method terminates if the input plot already
     * exists in the set.
     *
     * @param plotID          ID of a plot to be replaced
     * @param newPlot         new plot
     * @param disposePrevious if true, the previous instance (plot) is disposed (it involves, e.g., removing its listeners);
     *                        a disposed plot cannot be used again; it is also recommended NOT to manipulate a plot
     *                        that is NOT a member of this component
     */
    public void replacePlotWith(int plotID, AbstractPlot newPlot, boolean disposePrevious)
    {
        if (_wrappers == null) return;
        if (plotID >= _wrappers.length) return;
        if (_wrappers[plotID] == null) return;
        if (isPlotAlreadyUsed(newPlot)) return;

        try
        {
            // Try to be synchronous
            SwingUtilities.invokeAndWait(() -> {

                // Disable the top-level listeners first.
                _GC.getFrame().getController().disableListeners();

                // Temporarily disable various updates.
                _wrappers[plotID].getModel().getPlot().getModel().disableLayoutUpdates();
                _wrappers[plotID].getModel().getPlot().getModel().disableSchemeUpdates();
                newPlot.getModel().disableLayoutUpdates();
                newPlot.getModel().disableSchemeUpdates();

                _wrappers[plotID].getController().stopBackgroundThreads(); // disable background threads (for wrapper and plot)
                _wrappers[plotID].getController().unregisterListeners(); // temporarily unregister listeners (for wrapper and plot)

                _C._queueingSystem.disableAddingExecutionBlocksToQueue(plotID); // disable plot for queue-based processing (synchronous; lock)
                _C._queueingSystem.removeExecutionBlocksWithCallerType(plotID); // remove existing tasks associated with the plot (synchronous; lock)

                // Wait until:
                _C._queueingSystem.waitUntilTheFirstBlockIsNotOfCallerType(plotID);

                _wrappers[plotID].replacePlotWith(newPlot, disposePrevious); // replace the plot in a wrapper
                _C._interactor.setActivePlot(null); // set active plot to null

                _wrappers[plotID].getController().instantiateListeners(); // disable background threads (for wrapper and plot)

                _C._queueingSystem.enableAddingExecutionBlocksToQueue(plotID); // need to enable first to allow for receiving new tasks

                _wrappers[plotID].getController().startBackgroundThreads(); // tart background tasks
                _wrappers[plotID].getModel().getPlot().getModel().enableLayoutUpdates();
                _wrappers[plotID].getModel().getPlot().getModel().enableSchemeUpdates();

                // Enable the top-level listeners
                _GC.getFrame().getController().enableListeners();

                _GC.getFrame().revalidate();
                _wrappers[plotID].updateScheme(null); // update own scheme
                _GC.getFrame().updateLayout(); // update layout

                updatePlotsIDSsOnScreenResize();
                _C.requestFocusOn(plotID);

                System.gc(); // suggest cleanup
            });
        } catch (InterruptedException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
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
