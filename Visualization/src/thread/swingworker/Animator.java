package thread.swingworker;

import container.GlobalContainer;


/**
 * Swing-worker-based global timer that proceeds in an infinite loop and calls for the repaint on {@link plotswrapper.AbstractPlotsWrapper}.
 *
 * @author MTomczyk
 */
public class Animator extends AbstractTimer
{
    /**
     * Parameterized constructor.
     *
     * @param GC  reference to the global container
     * @param fps notification frequency (frames per second)
     */
    public Animator(GlobalContainer GC, int fps)
    {
        super(GC, fps);
    }

    /**
     * Timer action (to be overwritten).
     *
     * @param currentTime current time
     * @param deltaTime   delta time passed since the last time the action was triggered
     */
    @Override
    protected void doAction(long currentTime, long deltaTime)
    {
        if ((_GC != null) && (_GC.getPlotsWrapper() != null))
        {
            _GC.getPlotsWrapper().repaint();
        }
    }
}
