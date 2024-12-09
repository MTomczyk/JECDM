package plot.heatmap;

import container.GlobalContainer;
import container.PlotContainer;
import plot.AbstractPlot;
import plot.NotifyDisplayRangesChangedUpdater;
import plot.PlotModel;
import plot.heatmap.utils.Coords;
import space.Range;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.BlockTypes;
import thread.swingworker.EventTypes;

import java.util.LinkedList;


/**
 * Model for the {@link Heatmap2D class}.
 *
 * @author MTomczyk
 */
public class Heatmap2DModel extends PlotModel
{
    /**
     * Reference to the heatmap 2D layer.
     */
    private final Heatmap2DLayer _heatmap2DLayer;

    /**
     * Parameterized constructor.
     *
     * @param plot           reference to plot
     * @param PC             plot container
     * @param heatmap2DLayer reference to the heatmap 2D layer
     */
    public Heatmap2DModel(AbstractPlot plot, PlotContainer PC, Heatmap2DLayer heatmap2DLayer)
    {
        super(plot, PC);
        _heatmap2DLayer = heatmap2DLayer;
    }

    /**
     * Can be called to set a global container for this object.
     *
     * @param GC global container (shared object; stores references, provides various functionalities).
     */
    @Override
    public void establishGlobalContainer(GlobalContainer GC)
    {
        super.establishGlobalContainer(GC);
        _heatmap2DLayer.establishGlobalContainer(GC);
    }

    /**
     * Sets data (distribution 2D matrix) to illustrate in the form of a heatmap. Order of dimensions: Y, X.
     * Additionally, the method executes object-specific processing (e.g., updating normalization bounds).
     * Note: the processing is done using queued swing workers (see {@link swing.swingworkerqueue.QueuedSwingWorker}).
     *
     * @param data heatmap raw data
     */
    public void setDataAndPerformProcessing(double[][] data)
    {
        setDataAndPerformProcessing(data, false);
    }

    /**
     * Sets data (distribution 2D matrix) to illustrate in the form of a heatmap. Order of dimensions: Y, X.
     * Additionally, the method executes object-specific processing (e.g., updating normalization bounds).
     * Note: the processing is done using queued swing workers (see {@link swing.swingworkerqueue.QueuedSwingWorker}).
     *
     * @param data heatmap raw data
     * @param sort if true, the additional preprocessing is done: the buckets are sorted in ascending order of their values; imposes higher preprocessing time but improves filtering efficiency
     */
    public void setDataAndPerformProcessing(double[][] data, boolean sort)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new HeatmapUpdater(_heatmap2DLayer._HM, data, _heatmap2DLayer._HM.getDivisions()[0],
                _heatmap2DLayer._HM.getDivisions()[1], sort));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_DATA_CHANGED));
        workers.add(new NotifyDisplayRangesChangedUpdater(_PC));
        ExecutionBlock<Void, Void> B = new ExecutionBlock<>(BlockTypes.RENDER_UPDATER_ON_HEATMAP_DATA_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(B);
    }

    /**
     * Sets data (distribution 2D matrix) to illustrate in the form of a heatmap. Order of dimensions: Y, X.
     * Additionally, the method executes object-specific processing (e.g., updating normalization bounds).
     * Note: the processing is done using queued swing workers (see {@link swing.swingworkerqueue.QueuedSwingWorker}).
     * Important note: it is assumed that the input data does not contain any empty entries (nul or with value = Double.NEGATIVE_INFINITY).
     *
     * @param sortedCoords data points (buckets) represented as an array sorted based on bucket values; it is assumed that either regular data matrix or this array is not null; if this array is not null, it is used for processing; the use of this array is motivated by enabling more efficient data processing (but at a higher preprocessing cost, though)
     * @param sortedValues buckets' values sorted and stored as an array; optionally used with ``sorted coords'' field
     */
    public void setDataAndPerformProcessing(Coords[] sortedCoords, double[] sortedValues)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new HeatmapUpdater(_heatmap2DLayer._HM, 2, _heatmap2DLayer._HM.getDivisions()[0],
                _heatmap2DLayer._HM.getDivisions()[1], 1, sortedCoords, sortedValues));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_DATA_CHANGED));
        workers.add(new NotifyDisplayRangesChangedUpdater(_PC));
        ExecutionBlock<Void, Void> B = new ExecutionBlock<>(BlockTypes.RENDER_UPDATER_ON_HEATMAP_DATA_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(B);
    }

    /**
     * Setter for the auxiliary mask disabling rendering selected buckets (true -> rendering disabled) (order of dimensions: Y, X).
     *
     * @param mask filtering mask
     */
    public void setMask(boolean[][] mask)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new MaskUpdater2D(_heatmap2DLayer._HM, new boolean[][][]{mask}));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_FILTER_CHANGED));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.HEATMAP_MASK_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }


    /**
     * Setter for the optional value filter.
     * If not null, buckets are accepted only when their values are within the imposed range (closed interval).
     *
     * @param valueFilter value filter
     */
    public void setValueFilter(Range valueFilter)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new ValueFilterUpdater2D(_heatmap2DLayer._HM, valueFilter));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_FILTER_CHANGED));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.HEATMAP_VALUE_FILTER_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Setter for the optional value filter.
     * This method assumes that the user can provide bounds in the normalized [0,1] space.
     * It is the method's responsibility to determine the corresponding bounds in the original data space.
     * The left bound should be smaller/equal to the right, and both should be in [0, 1] range.
     * If these conditions are not satisfied, the bounds are set to 0 and 1, respectively.
     * The method uses the current display range stored by the heatmap model (so the filter may be required to be
     * set up again after the display range is changed).
     *
     * @param leftNormalizedBound  left normalized bound [0, 1] (should be smaller/equal to the right bound)
     * @param rightNormalizedBound right normalized bound [0, 1] (should be greater/equal to the left bound)
     */
    public void setValueFilterInTheNormalizedSpace(double leftNormalizedBound, double rightNormalizedBound)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new ValueFilterUnnormalizedUpdater2D(_heatmap2DLayer._HM, leftNormalizedBound, rightNormalizedBound));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_FILTER_CHANGED));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.HEATMAP_VALUE_FILTER_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Can be called to dispose the object and its children.
     */
    @Override
    public void dispose()
    {
        _heatmap2DLayer.dispose();
    }
}
