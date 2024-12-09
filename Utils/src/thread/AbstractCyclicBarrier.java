package thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Abstract runnable class that is a wrapper to Cyclic Barrier used to synchronize threads.
 *
 * @author MTomczyk
 */

public abstract class AbstractCyclicBarrier implements Runnable
{
    /**
     * Reference to the cyclic barrier.
     */
    protected CyclicBarrier _barrier;

    /**
     * Default constructor.
     */
    public AbstractCyclicBarrier()
    {
        this(null);
    }


    /**
     * Parameterized constructor.
     *
     * @param barrier cyclic barrier
     */
    public AbstractCyclicBarrier(CyclicBarrier barrier)
    {
        _barrier = barrier;
    }


    /**
     * Default implementation separating processing into init, main, and end steps.
     */
    @Override
    public void run()
    {
        if (prematureStop()) return;
        doInit();
        doMain();
        doEnd();
    }

    /**
     * Init step (default/empty implementation).
     */
    protected void doInit()
    {

    }

    /**
     * Main step (default/empty implementation).
     */
    protected void doMain()
    {

    }

    /**
     * End step (default/empty implementation).
     */
    protected void doEnd()
    {
        endBarrier();
    }


    /**
     * Auxiliary method terminating the execution at the start.
     *
     * @return true: terminate the main function, false otherwise
     */
    protected boolean prematureStop()
    {
        return false;
    }


    /**
     * Supportive method for reaching the barrier and waiting.
     */
    protected void endBarrier()
    {
        if (_barrier != null)
        {
            try
            {
                _barrier.await();
            } catch (InterruptedException | BrokenBarrierException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
