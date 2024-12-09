package thread.swingtimer;

import container.GlobalContainer;
import statistics.movingaverage.MovingAverageLong;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Abstract wrapper for swing timer. Can be used to supervise ongoing processes.
 * The timer operates on the EDT thread.
 *
 * @author MTomczyk
 */
public abstract class AbstractTimer
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    protected final GlobalContainer _GC;

    /**
     * Expected FPS for the animator.
     */
    protected long _expectedFPS;

    /**
     * Expected delay in milliseconds.
     */
    protected int _expectedMilliDelay;

    /**
     * Action to be performed.
     */
    protected ActionListener _actionListener;

    /**
     * Java swing timer.
     */
    protected Timer _timer;

    /**
     * If true, action execution times are measured and stored.
     */
    protected boolean _measureActionExecutionTime = false;

    /**
     * Optionally stored execution times.
     */
    protected MovingAverageLong _executionTimes = null;

    /**
     * If true, action delays in firing the action event are measured.
     */
    protected boolean _measureActionFireDelays = false;

    /**
     * Optionally stored execution times.
     */
    protected MovingAverageLong _actionFireDelays = null;

    /**
     * Auxiliary field storing timestamp (before execution of the main action);
     */
    protected Long _bExecutionTime = null;

    /**
     * Auxiliary field storing timestamp (after execution of the main action);
     */
    protected long _aExecutionTime = 0;


    /**
     * Parameterized constructor.
     *
     * @param GC  global container (shared object; stores references, provides various functionalities)
     * @param fps frames per second (action execution frequency).
     */
    public AbstractTimer(GlobalContainer GC, int fps)
    {
        this(GC, fps, false, 0);
    }

    /**
     * Parameterized constructor.
     *
     * @param GC                          global container (shared object; stores references, provides various functionalities)
     * @param fps                         frames per second (action execution frequency)
     * @param measureActionExecutionTimes if true main action (triggered by the timer) execution times are measured and stored
     * @param windowSize                  number of samples stored when calculating a moving average of  delays in firing the action event are measured
     */
    public AbstractTimer(GlobalContainer GC, int fps, boolean measureActionExecutionTimes, int windowSize)
    {
        _GC = GC;
        _expectedFPS = fps;
        _expectedMilliDelay = (int) ((1.0d / fps) * 1000);
        if (measureActionExecutionTimes)
        {
            _measureActionExecutionTime = true;
            _executionTimes = new MovingAverageLong(windowSize);
        }
    }


    /**
     * Parameterized constructor.
     *
     * @param GC                          global container (shared object; stores references, provides various functionalities)
     * @param fps                         frames per second (action execution frequency)
     * @param measureActionExecutionTimes if true main action (triggered by the timer) execution times are measured and stored
     * @param measureActionFireDelays     if true, action delays in firing the action event are measured
     * @param windowSize                  number of samples stored when calculating a moving average
     */
    public AbstractTimer(GlobalContainer GC, int fps, boolean measureActionExecutionTimes, boolean measureActionFireDelays, int windowSize)
    {
        _GC = GC;
        _expectedFPS = fps;
        _expectedMilliDelay = (int) ((1.0d / fps) * 1000.0d);

        if (measureActionExecutionTimes)
        {
            _measureActionExecutionTime = true;
            _executionTimes = new MovingAverageLong(windowSize);
        }

        if (measureActionFireDelays)
        {
            _measureActionFireDelays = true;
            _actionFireDelays = new MovingAverageLong(windowSize);
        }
    }

    /**
     * Should be called after constructing new object instance to instantiate the wrapped swing timer object.
     */
    public void instantiateTimer()
    {
        _actionListener = getActionListener();
        _timer = new Timer(_expectedMilliDelay, _actionListener);
        _timer.setRepeats(true);
    }

    /**
     * Getter for the moving average object (supports efficient calculation of moving averages) used when measuring main action execution times (in nanoseconds).
     *
     * @return moving average object
     */
    public MovingAverageLong getExecutionTimes()
    {
        return _executionTimes;
    }

    /**
     * Getter for the moving average object (supports efficient calculation of moving averages) used when measuring action fire delays (in nanoseconds).
     *
     * @return moving average object
     */
    public MovingAverageLong getActionFireDelays()
    {
        return _actionFireDelays;
    }

    /**
     * Instantiates and returns the action listener.
     * It triggers substantially three methods that can be overwritten.
     *
     * @return action listener
     */
    private ActionListener getActionListener()
    {
        return e -> {
            executePreAction();
            executeAction();
            executePostAction();
        };
    }

    /**
     * Action listener pre action (before executing the main action).
     */
    protected void executePreAction()
    {
        if (_measureActionFireDelays)
        {
            if (_bExecutionTime == null) _bExecutionTime = System.nanoTime();
            else
            {
                long t = System.nanoTime();
                _actionFireDelays.addData(t - _bExecutionTime);
                _bExecutionTime = t;
            }

        }

        if ((!_measureActionFireDelays) && (_measureActionExecutionTime))
        {
            _bExecutionTime = System.nanoTime();
        }

    }

    /**
     * Action listener main action.
     */
    protected void executeAction()
    {

    }

    /**
     * Action listener post action (after the main action is finalized).
     */
    protected void executePostAction()
    {
        if (_measureActionExecutionTime)
        {
            _aExecutionTime = System.nanoTime();
            _executionTimes.addData(_aExecutionTime - _bExecutionTime);
        }
    }

    /**
     * Starts (resumes) the timer.
     */
    public void startTimer()
    {
        if (_measureActionFireDelays)
        {
            _actionFireDelays.reset();
            _bExecutionTime = null;
        }
        _timer.start();
    }

    /**
     * Stops (pauses) the timer.
     */
    public void stopTimer()
    {
        _timer.stop();
    }

    /**
     * Can be called to dispose the data.
     */
    public void dispose()
    {
        _timer.stop();
        _timer = null;
        _executionTimes = null;
    }

}
