package plot;

import container.PlotContainer;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for notifying that the display ranges changed (triggered manually).
 *
 * @author MTomczyk
 */
public class NotifyDisplayRangesChangedUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * Parameterized constructor.
     *
     * @param PC reference to the plot container
     */
    public NotifyDisplayRangesChangedUpdater(PlotContainer PC)
    {
        _PC = PC;
    }


    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _PC.getPlot()._M.notifyDisplayRangesChangedListeners(null);

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
        //_PC.getPlot()._M.notifyDisplayRangesChangedListeners(null);

        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }


}
