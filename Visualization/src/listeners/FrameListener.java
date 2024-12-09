package listeners;

import container.GlobalContainer;
import container.Notification;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Listens for frame resize: calls for frame update layout methods.
 *
 * @author MTomczyk
 */
public class FrameListener extends ComponentAdapter
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    private final GlobalContainer _GC;

    /**
     * Parameterized constructor.
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     */
    public FrameListener(GlobalContainer GC)
    {
        _GC = GC;
    }

    /**
     * Triggers recalculation of all size-dependent fields (component resized).
     * First, the method updates the layout.
     * Second, the method recalculates IDS (starting from the second level).
     *
     * @param e the event to be processed
     */
    @Override
    public void componentResized(ComponentEvent e)
    {
        Notification.printNotification(_GC, null, "Frame listener: window resized");
        _GC.getFrame().updateLayout();
        _GC.getPlotsWrapper().getModel().updatePlotsIDSsOnScreenResize();
    }

    /**
     * Triggers recalculation of all size-dependent fields (component hidden).
     *
     * @param e the event to be processed
     */
    @Override
    public void componentHidden(ComponentEvent e)
    {

    }

    /**
     * Triggers recalculation of all size-dependent fields (component shown).
     *
     * @param e the event to be processed
     */
    @Override
    public void componentShown(ComponentEvent e)
    {

    }

}
