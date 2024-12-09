package runner.threads;

import thread.AbstractCyclicBarrier;
import visualization.IVisualization;

import java.util.concurrent.CyclicBarrier;

/**
 * Thread responsible for displaying the visualization module and waiting (till it is displayed).
 *
 * @author MTomczyk
 */


public class DisplayAndWaitThread extends AbstractCyclicBarrier implements Runnable
{
    /**
     * Visualization module.
     */
    protected IVisualization _visualization;

    /**
     * Parameterized constructor.
     *
     * @param barrier       cyclic barrier used to synchronize evolution with the visualization module (wait till the window is displayed)
     * @param visualization visualization module
     */
    public DisplayAndWaitThread(CyclicBarrier barrier, IVisualization visualization)
    {
        super(barrier);
        _visualization = visualization;
    }

    /**
     * Main step (default/empty implementation).
     */
    @Override
    protected void doMain()
    {
        // display call
        _visualization.init();
        _visualization.display();

        boolean repeat = true;
        do
        {
            if (_visualization.isWindowDisplayed()) repeat = false;
        } while (repeat);
    }

    /**
     * Auxiliary method terminating the execution at the start.
     *
     * @return true: terminate the main function, false otherwise
     */
    @Override
    protected boolean prematureStop()
    {
        return _visualization == null;
    }

}
