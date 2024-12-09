package plot.heatmap;

import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;


/**
 * Swing worker object responsible for updating the heatmap mask filter
 *
 * @author MTomczyk
 */
class MaskUpdater3D extends QueuedSwingWorker<Void, Void>
{
    /**
     * Heatmap layer.
     */
    private final Heatmap3DLayer _heatmapLayer;

    /**
     * New mask filter.
     */
    private final boolean[][][] _mask;

    /**
     * Parameterized constructor for 2D processing.
     *
     * @param mask       optional mask that can switch off selected buckets
     * @param heatmapLayer heatmap layer
     */
    public MaskUpdater3D(Heatmap3DLayer heatmapLayer,
                         boolean[][][] mask)
    {
        _heatmapLayer = heatmapLayer;
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
        _heatmapLayer._HM.setMask(_mask);
        _heatmapLayer.updateBuffers();
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
