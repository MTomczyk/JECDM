package thread.swingtimer;

import container.GlobalContainer;
import container.Notification;

/**
 * Default animator object for rendering the main frame.
 *
 * @author MTomczyk
 */

public class Animator extends AbstractTimer
{

    /**
     * Parameterized constructor.
     *
     * @param GC  global container (shared object; stores references, provides various functionalities)
     * @param fps frames per second (action execution frequency)
     */
    public Animator(GlobalContainer GC, int fps)
    {
        super(GC, fps, false, false, 0);
    }

    /**
     * The main action of the listener.
     */
    @Override
    protected void executeAction()
    {

        _GC.getFrame().getModel().getPlotsWrapper().repaint();
    }

    /**
     * Can be called to dispose the data.
     */
    public void dispose()
    {
        super.dispose();
        Notification.printNotification(_GC, null, "Animator: dispose method called");
    }
}

