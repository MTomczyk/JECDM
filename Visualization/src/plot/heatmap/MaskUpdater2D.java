package plot.heatmap;

import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;


/**
 * Swing worker object responsible for updating the heatmap mask filter
 *
 * @author MTomczyk
 */
class MaskUpdater2D extends QueuedSwingWorker<Void, Void>
{
    /**
     * Heatmap layer model.
     */
    private final HeatmapLayerModel _layerModel;

    /**
     * New mask filter.
     */
    private final boolean[][][] _mask;

    /**
     * Parameterized constructor for 2D processing.
     *
     * @param mask       optional mask that can switch off selected buckets
     * @param layerModel heatmap layer model
     */
    public MaskUpdater2D(HeatmapLayerModel layerModel,
                         boolean[][][] mask)
    {
        _layerModel = layerModel;
        _mask = mask;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _layerModel.setMask(_mask);
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
