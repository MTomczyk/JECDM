package plot;

import component.AbstractSwingComponent;
import component.drawingarea.AbstractDrawingArea;
import container.ComponentsContainer;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import dataset.Data;
import dataset.IDataSet;
import drmanager.DisplayRangesManager;
import listeners.auxiliary.IDisplayRangesChangedListener;
import scheme.AbstractScheme;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.BlockTypes;
import thread.swingworker.EventTypes;
import utils.Screenshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Model for the {@link AbstractPlot} class.
 *
 * @author MTomczyk
 */
public class PlotModel
{
    /**
     * Plot ID (unique, counts from 0).
     */
    protected Integer _id = null;

    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    protected GlobalContainer _GC;

    /**
     * Plot container: stores references to plot-related objects.
     */
    protected PlotContainer _PC;

    /**
     * Reference to the plot.
     */
    protected final AbstractPlot _plot;

    /**
     * Reference to plot controller.
     */
    protected PlotController _C;

    /**
     * If true -> plot is active and ready for interactions, false = otherwise.
     */
    protected volatile boolean _focused = false;

    /**
     * Plot scheme (determines colors, sizes, alignments, etc).
     */
    protected AbstractScheme _scheme = null;

    /**
     * Components container (stores various plot components).
     */
    protected ComponentsContainer _CC;

    /**
     * Data sets to be rendered by the plot.
     */
    protected volatile ArrayList<IDataSet> _dataSets = null;

    /**
     * List of listeners for the changes in maintained display ranges.
     */
    protected LinkedList<IDisplayRangesChangedListener> _drChangedListeners;

    /**
     * Auxiliary flag determining whether the layout can be updated (true) or not (false0.
     */
    protected boolean _layoutUpdatesEnabled = true;

    /**
     * Auxiliary flag determining whether the scheme can be updated (true) or not (false0.
     */
    protected boolean _schemeUpdatesEnabled = true;

    /**
     * Parameterized constructor.
     *
     * @param plot reference to plot.
     * @param PC   plot container
     */
    public PlotModel(AbstractPlot plot, PlotContainer PC)
    {
        _plot = plot;
        _PC = PC;
    }

    /**
     * Setter for the plot controller.
     *
     * @param C plot controller
     */
    public void setPlotController(PlotController C)
    {
        _C = C;
    }

    /**
     * Setter for the plot id
     *
     * @param id plot id
     */
    public void setPlotID(int id)
    {
        _id = id;
    }

    /**
     * Getter for the plot id.
     *
     * @return plot id
     */
    public Integer getPlotID()
    {
        return _id;
    }

    /**
     * Can be called to set a global container for this object.
     *
     * @param GC global container (shared object; stores references, provides various functionalities).
     */
    public void establishGlobalContainer(GlobalContainer GC)
    {
        _GC = GC;
        Notification.printNotification(_GC, _PC, "Plot model [id = " + _id + "]: global container is set");

        _plot.establishGlobalContainer(GC);
        _plot._layoutManager.establishGlobalContainer(_GC);
        for (AbstractSwingComponent c : _CC.getComponents())
        {
            if (c == null) continue;
            c.establishGlobalContainer(_GC);
        }
    }

    /**
     * Auxiliary method instantiating list of objects listening for changes in display ranges.
     */
    protected void instantiateDisplayRangesChangedListeners()
    {
        _drChangedListeners = new LinkedList<>();
        if (_CC.getAxes() != null)
            _drChangedListeners.addAll(Arrays.asList(_CC.getAxes()));
        if (_CC.getColorbar() != null)
            _drChangedListeners.add(_CC.getColorbar());
    }

    /**
     * Setter for the data sets (overwrites previous ones).
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSet data set to be displayed
     */
    public void setDataSet(IDataSet newDataSet)
    {
        setDataSet(newDataSet, true);
    }

    /**
     * Setter for the data sets (overwrites previous ones).
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSet          data set to be displayed
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     */
    public void setDataSet(IDataSet newDataSet, boolean updateDisplayRanges)
    {
        setDataSet(newDataSet, updateDisplayRanges, false);
    }

    /**
     * Setter for the data sets (overwrites previous ones).
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSet          data set to be displayed
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     * @param updateLegend        if true, the legend is updated after the data is swapped
     */
    public void setDataSet(IDataSet newDataSet, boolean updateDisplayRanges, boolean updateLegend)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: set data sets method called");
        if (newDataSet == null) setDataSets(null, updateDisplayRanges, updateLegend);
        else
        {
            ArrayList<IDataSet> dss = new ArrayList<>(1);
            dss.add(newDataSet);
            setDataSets(dss, updateDisplayRanges, updateLegend);
        }

    }

    /**
     * Setter for the data sets (overwrites previous ones).
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets data set to be displayed;
     */
    public void setDataSets(ArrayList<IDataSet> newDataSets)
    {
        setDataSets(newDataSets, true);
    }

    /**
     * Setter for the data sets (overwrites previous ones).
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets         data set to be displayed;
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     */
    public void setDataSets(ArrayList<IDataSet> newDataSets, boolean updateDisplayRanges)
    {
        setDataSets(newDataSets, updateDisplayRanges, false);
    }


    /**
     * Setter for the data sets (overwrites previous ones).
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets         data set to be displayed;
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     * @param updateLegend        if true, the legend is updated after the data is swapped
     */
    public void setDataSets(ArrayList<IDataSet> newDataSets, boolean updateDisplayRanges, boolean updateLegend)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: set data sets method called");
        updateDataSets(newDataSets, updateDisplayRanges, updateLegend, false);
    }

    /**
     * The method updates a data set whose names match the name of this provided.
     * If a data set is in the input but not in the currently maintained set, it is ignored.
     * If a data set is not in the input but is in te currently maintained set, it remains in the set but
     * its internal data is not updated (saves computational time)
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSet data set to be displayed;
     */
    public void updateSelectedDataSet(IDataSet newDataSet)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: update data sets method called");
        ArrayList<IDataSet> dsa = new ArrayList<>(1);
        dsa.add(newDataSet);
        updateSelectedDataSets(dsa);
    }


    /**
     * The method updates data sets whose names match names of those provided.
     * If a data set is in the input but not in the currently maintained set, it is ignored.
     * If a data set is not in the input but is in te currently maintained set, it remains in the set but
     * its internal data is not updated (saves computational time)
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets data set to be displayed;
     */
    public void updateSelectedDataSets(ArrayList<IDataSet> newDataSets)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: update data sets method called");
        updateSelectedDataSets(newDataSets, true);
    }


    /**
     * The method updates data sets whose names match names of those provided.
     * If a data set is in the input but not in the currently maintained set, it is ignored.
     * If a data set is not in the input but is in te currently maintained set, it remains in the set but
     * its internal data is not updated (saves computational time)
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets         data set to be displayed;
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     */
    public void updateSelectedDataSets(ArrayList<IDataSet> newDataSets, boolean updateDisplayRanges)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: update data sets method called");
        updateDataSets(newDataSets, updateDisplayRanges, false, true);
    }

    /**
     * The method updates a data set whose names match the name of this provided.
     * If a data set is in the input but not in the currently maintained set, it is ignored.
     * If a data set is not in the input but is in te currently maintained set, it remains in the set but
     * its internal data is not updated (saves computational time)
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSet          data set to be displayed;
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     * @param updateLegend        if true, the legend is updated after the data is swapped
     */
    public void updateSelectedDataSet(IDataSet newDataSet, boolean updateDisplayRanges, boolean updateLegend)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: update data sets method called");
        ArrayList<IDataSet> dsa = new ArrayList<>(1);
        dsa.add(newDataSet);
        updateSelectedDataSets(dsa, updateDisplayRanges, updateLegend);
    }

    /**
     * The method updates data sets whose names match names of those provided.
     * If a data set is in the input but not in the currently maintained set, it is ignored.
     * If a data set is not in the input but is in te currently maintained set, it remains in the set but
     * its internal data is not updated (saves computational time)
     * Important note: The method invokes three swing workers to execute a background task that
     * (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets         data set to be displayed;
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method to update data on display ranges, which can be then used to build internal data set structures used for efficient rendering
     * @param updateLegend        if true, the legend is updated after the data is swapped
     */
    public void updateSelectedDataSets(ArrayList<IDataSet> newDataSets, boolean updateDisplayRanges, boolean updateLegend)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: update data sets method called");
        updateDataSets(newDataSets, updateDisplayRanges, updateLegend, true);
    }

    /**
     * The method updates data sets. Important note: The method invokes three swing workers to execute a background task
     * that (1) recalculate display ranges, (2) IDS, (3) and render.
     *
     * @param newDataSets         data set to be displayed
     * @param updateDisplayRanges if true, the method uses {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])}
     *                            method to update data on display ranges, which can be then used to build internal data set
     *                            structures used for efficient rendering (updateMatchingOnly = true, the display ranges
     *                            are updated based on this new data only)
     * @param updateLegend        if true, the legend is updated after the data is updated
     * @param updateMatchingOnly  if true, he method updates data sets whose names match names of those provided; if a data
     *                            set is in the input but not in the currently maintained set, it is ignored; if a data set
     *                            is not in the input but is in te currently maintained set, it remains in the set but;
     *                            if false, the method removes the currently maintained sets and uses the input as the new ones
     */
    private void updateDataSets(ArrayList<IDataSet> newDataSets, boolean updateDisplayRanges, boolean updateLegend, boolean updateMatchingOnly)
    {
        Notification.printNotification(_GC, _PC, "plot model [id = " + PlotContainer.getID(_PC) + "]: update data sets method called");

        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        if (newDataSets != null) workers.add(new DataSetsUpdater(_PC, newDataSets, updateLegend, updateMatchingOnly));
        if (updateDisplayRanges) workers.add(new DisplayRangesUpdater(_PC));

        AbstractDrawingArea drawingArea = _PC.getDrawingArea();
        workers.add(drawingArea.createIDSUpdater(EventTypes.ON_DATA_CHANGED));
        workers.add(drawingArea.createRenderUpdater(EventTypes.ON_DATA_CHANGED));
        if (updateDisplayRanges) workers.add(new NotifyDisplayRangesChangedUpdater(_PC));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.DATA_SETS_UPDATER, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }


    /**
     * Auxiliary method that updates plot IDS, starting from the second level.
     * The method can be invoked manually.
     */
    public void updatePlotIDSsAndRenderOnDemand()
    {
        if (_CC.getDrawingArea() == null) return;
        AbstractDrawingArea da = _CC.getDrawingArea();
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(da.createIDSUpdater(EventTypes.ON_DEMAND));
        workers.add(da.createRenderUpdater(EventTypes.ON_DEMAND));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.IDS_AND_RENDER_UPDATER_ON_DEMAND, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Auxiliary method that updates plot IDS, starting from the second level.
     * The method is invoked by all window-resized-like events.
     */
    public void updatePlotIDSsAndRenderOnScreenResize()
    {
        if (_CC.getDrawingArea() == null) return;
        AbstractDrawingArea da = _CC.getDrawingArea();
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(da.createIDSUpdater(EventTypes.ON_RESIZE));
        workers.add(da.createRenderUpdater(EventTypes.ON_RESIZE));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.IDS_AND_RENDER_UPDATER_ON_RESIZE, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Notify all listeners for changes in display ranges.
     */
    public void notifyDisplayRangesChangedListeners()
    {
        NotifyDisplayRangesChangedUpdater worker = new NotifyDisplayRangesChangedUpdater(_PC);
        ExecutionBlock<Void, Void> executionBlock = new ExecutionBlock<>(BlockTypes.NOTIFY_DISPLAY_RANGES_CHANGED,
                _PC.getPlotID(), worker);
        _GC.registerWorkers(executionBlock);
    }

    /**
     * Auxiliary method that helps automatically create a screenshot. The processing involves:
     * (1) disabling plot visibility,
     * (2) setting projection bounds,
     * (3) updating IDS structures,
     * (4) creating a render,
     * (6) creating a screenshot (synchronously),
     * (7) restoring the plot size,
     * (8) restoring plot visibility,
     * (9) calling countDown on the barrier.
     *
     * @param w new plot width for the screenshot
     * @param h new plot height for the screenshot
     * @return initially empty wrapper for the buffered image (the image will be set by the workers); additionally,
     * the method creates a count-down latch with a size of 1 and passes it via the returned object;  a thread
     * creating a render will call its countDown() upon screenshot creation,  thus allowing for thread synchronization
     */
    public Screenshot requestScreenshotCreation(int w, int h)
    {
        return requestScreenshotCreation(w, h, false);
    }

    /**
     * Auxiliary method that helps automatically create a screenshot. The processing involves:
     * (1) disabling plot visibility,
     * (2) setting projection bounds,
     * (3) updating IDS structures,
     * (4) creating a render,
     * (6) creating a screenshot (synchronously),
     * (7) restoring the plot size,
     * (8) restoring plot visibility,
     * (9) calling countDown on the barrier.
     *
     * @param w               new plot width for the screenshot
     * @param h               new plot height for the screenshot
     * @param useAlphaChannel if true, alpha channel is used; false otherwise
     * @return initially empty wrapper for the buffered image (the image will be set by the workers); additionally,
     * the method creates a count-down latch with a size of 1 and passes it via the returned object;  a thread
     * creating a render will call its countDown() upon screenshot creation,  thus allowing for thread synchronization
     */
    public Screenshot requestScreenshotCreation(int w, int h, boolean useAlphaChannel)
    {
        if (_CC.getDrawingArea() == null) return null;

        Screenshot screenshot = new Screenshot(useAlphaChannel);

        int px = _plot.getX();
        int py = _plot.getY();
        int pw = _plot.getWidth();
        int ph = _plot.getHeight();

        AbstractDrawingArea da = _CC.getDrawingArea();
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new PlotVisibilityUpdater(_PC, false));
        workers.add(new PlotDimensionsUpdater(_PC, px, py, w, h));
        workers.add(da.createIDSUpdater(EventTypes.ON_RESIZE));
        workers.add(da.createRenderUpdater(EventTypes.ON_RESIZE));
        workers.add(new CreateAndWrapScreenshot(_PC, screenshot));
        workers.add(new PlotDimensionsUpdater(_PC, px, py, pw, ph));
        workers.add(new PlotVisibilityUpdater(_PC, true));
        workers.add(new CountDownLatchUpdater(screenshot._barrier));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.CREATE_SCREENSHOT_ON_DEMAND, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
        return screenshot;
    }

    /**
     * Notify all listeners for changes in display ranges.
     *
     * @param report report on the most recent update in display ranges
     */
    protected void notifyDisplayRangesChangedListeners(DisplayRangesManager.Report report)
    {
        Notification.printNotification(_GC, _PC, "Plot model [id = " + _id + "]: listeners for update in display ranges called");
        for (IDisplayRangesChangedListener dr : _drChangedListeners)
            if (dr != null) dr.displayRangesChanged(_PC.getDisplayRangesManager(), report);
    }

    /**
     * Protected data sets setter (just sets the reference).
     *
     * @param dataSets           data sets object
     * @param updateMatchingOnly if true, he method updates data sets whose names match names of those provided; if
     *                           a data set is in the input but not in the currently maintained set, it is ignored;
     *                           if a data set is not in the input but is in te currently maintained set, it remains
     *                           in the set but; if false, the method removes the currently maintained sets and uses
     *                           the input as the new ones
     */
    protected void setDataSetsReference(ArrayList<IDataSet> dataSets, boolean updateMatchingOnly)
    {
        _dataSets = dataSets;
    }


    /**
     * Getter for the data sets.
     *
     * @return data sets displayed by the plot
     */
    public ArrayList<IDataSet> getDataSets()
    {
        return _dataSets;
    }

    /**
     * Getter for the plot scheme object.
     *
     * @return plot scheme
     */
    public AbstractScheme getScheme()
    {
        return _scheme;
    }

    /**
     * Enables layout future updates.
     */
    public void enableLayoutUpdates()
    {
        _layoutUpdatesEnabled = true;
    }

    /**
     * Enables layout future updates.
     */
    public void disableLayoutUpdates()
    {
        _layoutUpdatesEnabled = false;
    }


    /**
     * Enables layout future updates.
     */
    public void enableSchemeUpdates()
    {
        _schemeUpdatesEnabled = true;
    }

    /**
     * Enables layout future updates.
     */
    public void disableSchemeUpdates()
    {
        _schemeUpdatesEnabled = false;
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, "Plot model [id = " + getPlotID() + "]: dispose method called");
        for (AbstractSwingComponent c : _CC.getComponents()) if (c != null) c.dispose();
        _drChangedListeners = null;
        if (_dataSets != null) for (IDataSet ds : _dataSets) ds.dispose();
        _dataSets = null;
        _CC = null;
        _PC = null;
        _GC = null;
        _C = null;
    }
}
