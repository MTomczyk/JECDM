package thread.swingworker;

import container.GlobalContainer;
import container.Notification;
import plot.AbstractPlot;

/**
 * Swing-worker-based global timer that proceeds in an infinite loop and notifies
 * interact listener {@link listeners.interact.AbstractInteractListener} of
 * the active plot about the current timestamp and delta time passed since the previous notification.
 *
 * @author MTomczyk
 */
public class Interactor extends AbstractTimer
{
    /**
     * Reference to the active plot.
     */
    protected volatile AbstractPlot _activePlot = null;

    /**
     * Parameterized constructor.
     *
     * @param GC  reference to the global container
     * @param fps notification frequency (frames per second)
     */
    public Interactor(GlobalContainer GC, int fps)
    {
        super(GC, fps);
    }

    /**
     * Setter for the reference to the active plot.
     *
     * @param activePlot active plot
     */
    public void setActivePlot(AbstractPlot activePlot)
    {
        Notification.printNotification(_GC, null, "Interactor: set active plot method called");

        if (((_activePlot == null) && (activePlot != null)) ||
                ((_activePlot != null) && (activePlot == null)) ||
                ((_activePlot != null) && (activePlot != null)
                        && (!_activePlot.getModel().getPlotID().equals(activePlot.getModel().getPlotID()))))
            _pTime = System.nanoTime();
        _activePlot = activePlot;
    }


    /**
     * Timer action (to be overwritten).
     *
     * @param currentTime current time
     * @param deltaTime delta time passed since the last time the action was triggered
     */
    @Override
    protected void doAction(long currentTime, long deltaTime)
    {
        if (_activePlot != null)
            _activePlot.getController().getInteractListener().notifyTimestamp(currentTime, deltaTime);
    }

    /**
     * Can be called to dispose the data.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _activePlot = null;
    }
}
