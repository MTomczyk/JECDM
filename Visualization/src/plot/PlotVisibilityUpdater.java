package plot;

import container.PlotContainer;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for setting plot visibility.
 *
 * @author MTomczyk
 */
class PlotVisibilityUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * Visibility to be set.
     */
    private final boolean _visible;

    /**
     * Parameterized constructor.
     *
     * @param PC      reference to the plot container
     * @param visible visibility to be set
     */
    public PlotVisibilityUpdater(PlotContainer PC, boolean visible)
    {
        _PC = PC;
        _visible = visible;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _PC.getPlot().setVisible(_visible);
        notifyTermination();
        return null;
    }

    /**
     * Finalizes data set update.
     */
    @Override
    protected void done()
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
