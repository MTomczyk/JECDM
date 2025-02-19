package listeners;


import container.GlobalContainer;
import container.Notification;
import frame.Frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Window state listener used for updating plot layout when necessary,
 * and controlling the execution of background threads. Specifically,
 * when the window is minimized/maximized, the threads are paused/resumed.
 *
 * @author MTomczyk
 */

public class WindowListener extends WindowAdapter implements java.awt.event.WindowListener
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
    public WindowListener(GlobalContainer GC)
    {
        _GC = GC;
    }

    /**
     * Window state changed event.
     *
     * @param evt the event to be processed
     */
    @Override
    public void windowStateChanged(WindowEvent evt)
    {
        if (!_enabled) return;

        if (_GC.getFrame().getExtendedState() == Frame.MAXIMIZED_BOTH)
        {
            Notification.printNotification(_GC, null, "Window listener: window state changed to maximized");
        }
    }

    /**
     * Window closing event.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e)
    {
        Notification.printNotification(_GC, null, "Window listener: window is closing (termination)");

        if (!_enabled) return;

        super.windowClosed(e);
        e.getWindow().dispose();
    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowOpened(WindowEvent e)
    {
        Notification.printNotification(_GC, null, "Window listener: window is opened (for the first time)");

        if (!_enabled) return;

        _GC.notifyWindowVisible();
        _GC.getFrame().getController().startBackgroundThreads();
    }


    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowClosed(WindowEvent e)
    {
        // do not use, "closing is used"
    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowActivated(WindowEvent e)
    {

    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeactivated(WindowEvent e)
    {

    }


    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @param e the event to be processed
     * @see Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e)
    {
        Notification.printNotification(_GC, null, "Window listener: window is minimized");

        if (!_enabled) return;

        // timers can be stopped
        _GC.getFrame().getController().stopBackgroundThreads();
    }

    /**
     * Invoked when a window is changed from a minimized to a normal state.
     * The method updates layout and plots' IDS (starting from the second level).
     *
     * @param e the event to be processed
     */
    @Override
    public void windowDeiconified(WindowEvent e)
    {
        Notification.printNotification(_GC, null, "Window listener: window state changed from minimized to normal");
        if (!_enabled) return;

        // timers can be started
        _GC.getFrame().updateLayout();
        if (_GC.getFrame().getModel().getPlotsWrapper() != null)
            _GC.getFrame().getModel().getPlotsWrapper().getModel().updatePlotsIDSsOnScreenResize();

        _GC.getFrame().repaint();
        _GC.getFrame().getController().startBackgroundThreads();
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
