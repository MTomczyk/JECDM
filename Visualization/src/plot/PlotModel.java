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

        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        if (newDataSets != null) workers.add(new DataSetsUpdater(_PC, newDataSets, updateLegend));
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
     * @param dataSets data sets object
     */
    protected void setDataSetsReference(ArrayList<IDataSet> dataSets)
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
