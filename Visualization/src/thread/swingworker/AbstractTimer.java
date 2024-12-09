package thread.swingworker;

import container.GlobalContainer;
import container.Notification;

import javax.swing.*;

/**
 * Swing-worker-based abstract timer that proceeds in an infinite loop and executes an action at specified intervals.
 *
 * @author MTomczyk
 */
public abstract class AbstractTimer extends SwingWorker<Void, Void>
{
    /**
     * FPS linked to responding to interactions.
     */
    protected final int _fps;

    /**
     * Delta time (in nanoseconds) required to trigger the action.
     */
    protected final int _deltaNS;

    /**
     * Previous timestamp (used to calculate delta time).
     */
    protected volatile long _pTime = 0;

    /**
     * Global container.
     */
    protected final GlobalContainer _GC;

    /**
     * If true, the interactions are paused.
     */
    protected volatile boolean _paused;

    /**
     * If true, the background tasks proceed. If switched to false, the main loop can be exited and, thus,
     * the background tasks normally terminate.
     */
    protected volatile boolean _active;


    /**
     * Parameterized constructor.
     *
     * @param GC  reference to the global container
     * @param fps notification frequency (frames per second)
     */
    public AbstractTimer(GlobalContainer GC, int fps)
    {
        _GC = GC;
        _fps = fps;
        double dS = 1.0d / _fps;
        _deltaNS = (int) (dS * 1000000000.0d);
        _active = true;
        _paused = true;
    }

    /**
     * Method for resuming the timer.
     */
    public void resumeTimer()
    {
        _pTime = System.nanoTime();
        _paused = false;
    }

    /**
     * Method for pausing the timer.
     */
    public void pauseTimer()
    {
        _paused = true;
    }

    /**
     * Deactivates the main flag keeping the main loop active.
     */
    public void deactivate()
    {
        _active = false;
    }

    /**
     * Background timer task.
     * @return na
     */
    @Override
    public Void doInBackground()
    {
        while (_active)
        {
            if (_paused) continue;

            long cTime = System.nanoTime();
            long deltaTime = cTime - _pTime;
            if (deltaTime > _deltaNS)
            {
                _pTime = cTime;
                doAction(cTime, deltaTime);
            }
        }

        return null;
    }

    /**
     * Timer action (to be overwritten).
     *
     * @param currentTime current time
     * @param deltaTime delta time passed since the last time the action was triggered
     */
    protected void doAction(long currentTime, long deltaTime)
    {

    }

    /**
     * Can be called to dispose the data.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, null, "Interactions timer: dispose method called");
    }
}
