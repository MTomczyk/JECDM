package plot;

import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for updating a count-down latch (barrier).
 *
 * @author MTomczyk
 */
class CountDownLatchUpdater extends QueuedSwingWorker<Void, Void>
{

    /**
     * Reference to the count-down latch (its countDown method will be called).
     */
    private final CountDownLatch _barrier;

    /**
     * Parameterized constructor.
     *
     * @param barrier reference to the count-down latch (its countDown method will be called)
     */
    public CountDownLatchUpdater(CountDownLatch barrier)
    {
        _barrier = barrier;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _barrier.countDown();
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
