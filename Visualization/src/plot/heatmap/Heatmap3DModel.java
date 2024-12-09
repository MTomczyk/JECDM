package plot.heatmap;

import container.GlobalContainer;
import container.PlotContainer;
import plot.AbstractPlot;
import plot.NotifyDisplayRangesChangedUpdater;
import plot.Plot3DModel;
import plot.heatmap.utils.Coords;
import space.Range;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.BlockTypes;
import thread.swingworker.EventTypes;

import java.util.LinkedList;


/**
 * Model for the {@link Heatmap3D class}.
 *
 * @author MTomczyk
 */
public class Heatmap3DModel extends Plot3DModel
{
    /**
     * Reference to the heatmap 3D layer.
     */
    protected final Heatmap3DLayer _heatmap3DLayer;

    /**
     * Parameterized constructor.
     *
     * @param plot           reference to plot
     * @param PC             plot container
     * @param heatmap3DLayer reference to the heatmap 3D layer
     */
    public Heatmap3DModel(AbstractPlot plot, PlotContainer PC, Heatmap3DLayer heatmap3DLayer)
    {
        super(plot, PC);
        _heatmap3DLayer = heatmap3DLayer;
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
        _heatmap3DLayer.establishGlobalContainer(GC);
    }


    /**
     * Sets data (distribution 2D matrix) to illustrate in the form of a heatmap. Order of dimensions: Y, X.
     * Additionally, the method executes object-specific processing (e.g., updating normalization bounds).
     * Note: the processing is done using queued swing workers (see {@link swing.swingworkerqueue.QueuedSwingWorker}).
     * Important note: Heatmap3D operates on a sorted data (buckets sorted according to their values).
     * However, this method provides just raw (unsorted) data. Therefore, the run updater will perform
     * sorting, which may take some time. It is recommended to do presorting on your own and use the other setter,
     * i.e., {@link Heatmap3DModel#setDataAndPerformProcessing(Coords[], double[])}.
     *
     * @param data heatmap raw data (3 dimensional)
     */
    public void setDataAndPerformProcessing(double[][][] data)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new HeatmapUpdater(
                _heatmap3DLayer._HM, data,
                _heatmap3DLayer._HM.getDivisions()[0],
                _heatmap3DLayer._HM.getDivisions()[1],
                _heatmap3DLayer._HM.getDivisions()[2],
                true));
        finalizeSetData(workers);
    }

    /**
     * Sets data (distribution 2D matrix) to illustrate in the form of a heatmap. Order of dimensions: Y, X.
     * Additionally, the method executes object-specific processing (e.g., updating normalization bounds).
     * Note: the processing is done using queued swing workers (see {@link swing.swingworkerqueue.QueuedSwingWorker}).
     * Important note: it is assumed that the input data does not contain any empty entries (null or with value = Double.NEGATIVE_INFINITY).
     *
     * @param sortedCoords data points (buckets) represented as an array sorted based on bucket values; it is assumed that either regular data matrix or this array is not null; if this array is not null, it is used for processing; the use of this array is motivated by enabling more efficient data processing (but at a higher preprocessing cost, though)
     * @param sortedValues buckets' values sorted and stored as an array; optionally used with ``sorted coords'' field
     */
    public void setDataAndPerformProcessing(Coords[] sortedCoords, double[] sortedValues)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new HeatmapUpdater(
                _heatmap3DLayer._HM, 3,
                _heatmap3DLayer._HM.getDivisions()[0],
                _heatmap3DLayer._HM.getDivisions()[1],
                _heatmap3DLayer._HM.getDivisions()[2],
                sortedCoords, sortedValues));
        finalizeSetData(workers);
    }

    /**
     * Method that finalizes heatmap setter.
     *
     * @param workers list of workers to be dispatched
     */
    private void finalizeSetData(LinkedList<QueuedSwingWorker<Void, Void>> workers)
    {
        workers.add(new Heatmap3DBuffersUpdater(_heatmap3DLayer));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_DATA_CHANGED));
        workers.add(new NotifyDisplayRangesChangedUpdater(_PC));
        ExecutionBlock<Void, Void> B = new ExecutionBlock<>(BlockTypes.RENDER_UPDATER_ON_HEATMAP_DATA_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(B);
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
        workers.add(new ValueFilterUpdater3D(_heatmap3DLayer._HM, valueFilter));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_FILTER_CHANGED));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.HEATMAP_VALUE_FILTER_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Setter for the optional value filter.
     * This method assumes that the user can provide bounds in the normalized [0,1] space.
     * It is the method's responsibility to determine the corresponding bounds in the original data space.
     * The left bound should be smaller/equal to the right, and both should be in the [0, 1] range.
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
        workers.add(new ValueFilterUnnormalizedUpdater3D(_heatmap3DLayer._HM, leftNormalizedBound, rightNormalizedBound));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_FILTER_CHANGED));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.HEATMAP_VALUE_FILTER_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Setter for the auxiliary mask disabling rendering selected buckets (true -> rendering disabled) (order of dimensions: Z, Y, X).
     *
     * @param mask filtering mask
     */
    public void setMask(boolean[][][] mask)
    {
        LinkedList<QueuedSwingWorker<Void, Void>> workers = new LinkedList<>();
        workers.add(new MaskUpdater3D(_heatmap3DLayer, mask));
        workers.add(_PC.getDrawingArea().createRenderUpdater(EventTypes.ON_HEATMAP_FILTER_CHANGED));
        ExecutionBlock<Void, Void> block = new ExecutionBlock<>(BlockTypes.HEATMAP_MASK_CHANGED, _PC.getPlotID(), workers);
        _GC.registerWorkers(block);
    }

    /**
     * Can be called to dispose the object and its children.
     */
    @Override
    public void dispose()
    {
        //_heatmap3DLayer.dispose(); // the object is disposed by the drawing area 3D (wrapper)
    }
}
