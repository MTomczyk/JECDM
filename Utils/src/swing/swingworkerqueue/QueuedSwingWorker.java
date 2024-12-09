package swing.swingworkerqueue;

import javax.swing.*;

/**
 * Extension of a swing worker to be processed on {@link SwingWorkerQueue}.
 *
 * @author MTomczyk
 */
public class QueuedSwingWorker<T, V> extends SwingWorker<T, V>
{
    /**
     * Reference to the queue.
     */
    private SwingWorkerQueue<T, V> _queue;

    /**
     * Setter for the queue.
     *
     * @param queue queue
     */
    public void setQueue(SwingWorkerQueue<T, V> queue)
    {
        _queue = queue;
    }

    /**
     * To be overwritten.
     *
     * @return report on execution
     */
    @Override
    protected T doInBackground()
    {
        return null;
    }

    /**
     * Should be called upon termination to remove itself from the queue and (optionally) start executing next the worker.
     * The method should be called in done() so that it is executed on EDT.
     */
    protected void notifyTermination()
    {
        _queue.removeFirstWorkerAndExecuteNext();
    }
}
