package listeners;

import container.GlobalContainer;
import container.Notification;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Simple mouse-pressed listener that disables focus on all plots.
 *
 * @author MTomczyk
 */
public class MousePressedLooseFocus implements MouseListener
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    private final GlobalContainer _GC;

    /**
     * Flag indicating whether the listener is enabled (true) or not (false).
     */
    private volatile boolean _enabled;

    /**
     * Parameterized constructor.
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     */
    public MousePressedLooseFocus(GlobalContainer GC)
    {
        _GC = GC;
    }


    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * The method calls the main plot wrapper and invokes a method that looses focus on all plots.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        Notification.printNotification(_GC, null,  "Mouse click loose focus: mouse pressed");
        if (!_enabled) return;
        _GC.getPlotsWrapper().getController().looseFocusToAllPlots();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    /**
     * Auxiliary method that enables te listener.
     */
    public void enable()
    {
        _enabled = true;
    }


    /**
     * Auxiliary method that enables te listener.
     */
    public void disable()
    {
        _enabled = false;
    }
}
