package plot.heatmap;

import space.Range;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;


/**
 * Swing worker object responsible for updating the heatmap mask filter
 *
 * @author MTomczyk
 */
class ValueFilterUpdater3D extends QueuedSwingWorker<Void, Void>
{
    /**
     * Heatmap layer model.
     */
    private final HeatmapLayerModel _layerModel;

    /**
     * Allowed heatmap values.
     */
    private final Range _valueFilter;

    /**
     * Parameterized constructor for 2D processing.
     *
     * @param layerModel  heatmap layer model
     * @param valueFilter new value filter
     */
    public ValueFilterUpdater3D(HeatmapLayerModel layerModel,
                                Range valueFilter)
    {
        _layerModel = layerModel;
        _valueFilter = valueFilter;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _layerModel.setValueFilter(_valueFilter);
        _layerModel.determineTheIndicesInterval();
        notifyTermination();
        return null;
    }

    /**
     * Catches exceptions.
     */
    @Override
    public void done()
    {
        if (isCancelled()) return;
        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}
