package plot.heatmap;

import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;


/**
 * Swing worker object responsible for updating heatmap VBO buffers
 *
 * @author MTomczyk
 */
class Heatmap3DBuffersUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Heatmap layer.
     */
    private final Heatmap3DLayer _layer;

    /**
     * Parameterized constructor capturing presorted buckets.
     *
     * @param layer heatmap layer
     */
    public Heatmap3DBuffersUpdater(Heatmap3DLayer layer)
    {
        _layer = layer;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        if (_layer.areVBOsNull()) _layer.createBuffers();
        else _layer.updateBuffers();
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
