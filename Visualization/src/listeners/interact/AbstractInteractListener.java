package listeners.interact;


import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import swing.keyboard.IKeyEventsListener;
import thread.swingworker.Interactor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Default listener that allows for interactions with the plot.
 *
 * @author MTomczyk
 */

public abstract class AbstractInteractListener implements MouseMotionListener, MouseListener, IKeyEventsListener
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    protected GlobalContainer _GC;

    /**
     * Stores references to plot-related objects and provides various functionalities.
     */
    protected PlotContainer _PC;

    /**
     * Object executing transformations as a response to mouse/buttons event and incoming time notifications received from {@link Interactor}.
     */
    protected AbstractExecutor _executor;

    /**
     * Stores data and provides functionalities related to projection.
     */
    protected final Projection _projection = new Projection();

    /**
     * Parameterized constructor.
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     * @param PC stores references to plot-related objects and provides various functionalities
     */
    public AbstractInteractListener(GlobalContainer GC, PlotContainer PC)
    {
        setContainers(GC, PC);
        initDefaultProjection();
        instantiateExecutor();
    }

    /**
     * Auxiliary method instantiating the object responsible for executing the interaction events.
     */
    protected void instantiateExecutor()
    {
        _executor = new Executor(_GC, _PC, _projection, 1.0f, 1.0f);
    }

    /**
     * Setter for the global and the plot containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    public void setContainers(GlobalContainer GC, PlotContainer PC)
    {
        _GC = GC;
        _PC = PC;
    }

    /**
     * Can be used by an external timer (e.g., {@link Interactor}) to notify
     * the listener about the current time (in ns) and the delta time passed since the previous
     * notification. The listener's executor can then update the projection accordingly.
     *
     * @param currentTime current time (nano)
     * @param deltaTime   delta time passed (in nanoseconds)
     */
    public void notifyTimestamp(long currentTime, long deltaTime)
    {
        if (_executor != null) _executor.notifyTimestamp(currentTime, deltaTime);
    }

    /**
     * Initializes the default projection.
     */
    protected void initDefaultProjection()
    {

    }

    /**
     * Resets projection.
     */
    protected void resetProjection()
    {
        _projection.reset();
    }

    /**
     * Sets the same projection data as in the provided listener.
     *
     * @param listener listener
     */
    public void setDataAsIn(AbstractInteractListener listener)
    {
        _projection._rO = listener._projection._rO;
        _projection._rC = listener._projection._rC;
        _projection._T = listener._projection._T;
    }

    /**
     * Mouse click event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        int id = _PC.getPlot().getModel().getPlotID();
        Notification.printNotification(_GC, _PC,  "Interact listener of plot [id = " + id + "]: mouse clicked");
        _GC.getPlotsWrapper().getController().requestFocusOn(id);

        int button = e.getButton();

        // right mouse button
        if (button == MouseEvent.BUTTON3)
        {
            Point p = new Point(e.getX(), e.getY());
            SwingUtilities.convertPointToScreen(p, _PC.getPlot());
            _PC.getPlot().getController().showPopupMenu(p.x, p.y);
        }

        if (_executor != null) _executor.mouseClicked(e);
    }

    /**
     * Mouse pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        int id = _PC.getPlot().getModel().getPlotID();
        Notification.printNotification(_GC, _PC,  "Interact listener of plot [id = " + id + "]: mouse pressed");
        _GC.getPlotsWrapper().getController().requestFocusOn(id);
        _PC.getPlot().getController().hidePopup();
        if (_executor != null) _executor.mousePressed(e);
    }

    /**
     * Mouse released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: mouse released");
        if (_executor != null) _executor.mouseReleased(e);
    }

    /**
     * Mouse entered event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: mouse entered");
        if (_executor != null) _executor.mouseEntered(e);
    }

    /**
     * Mouse exited event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: mouse exited");
        if (_executor != null) _executor.mouseExited(e);
    }

    /**
     * Mouse dragged event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: mouse dragged");
        _PC.getPlot().getController().hidePopup();
        if (_executor != null) _executor.mouseDragged(e);
    }

    /**
     * Mouse moved event.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: mouse moved");
        if (_executor != null) _executor.mouseMoved(e);
    }

    /**
     * Notifies the listener that the key has been pressed
     *
     * @param key key code
     */
    @Override
    public void notifyKeyPressed(int key)
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: key pressed = " + KeyEvent.getKeyText(key).toLowerCase());
        if (_executor != null) _executor.notifyKeyPressed(key);
    }

    /**
     * Notifies the listener that the key has been released.
     *
     * @param key key code.
     */
    @Override
    public void notifyKeyReleased(int key)
    {
        Notification.printNotification(_GC, _PC,  "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: key released = " + KeyEvent.getKeyText(key).toLowerCase());
        if (_executor != null) _executor.notifyKeyReleased(key);
    }

    /**
     * Can be used to notify that all keys have been released.
     */
    public void notifyAllKeysReleased()
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: all keys released");
        if (_executor != null) _executor.notifyAllKeysReleased();
    }

    /**
     * Can be used to notify that all mouse buttons have been released.
     */
    public void notifyAllMouseButtonsReleased()
    {
        Notification.printNotification(_GC, _PC, "Interact listener of plot [id = " + PlotContainer.getID(_PC) + "]: all mouse buttons released released");
        if (_executor != null) _executor.notifyAllMouseButtonsReleased();
    }

    /**
     * Dispose method.
     */
    public void dispose()
    {
        if (_executor != null) _executor.dispose();
        _executor = null;
        _projection.dispose();
    }

    /**
     * Object rotation along X and Y axes.
     *
     * @return returns object rotations along X and T axes (2-element vector).
     */
    public float[] getObjectRotation()
    {
        return _projection._rO;
    }

    /**
     * Camera rotation along X and Y axes.
     *
     * @return returns camera rotations along X and T axes (2-element vector).
     */
    public float[] getCameraRotation()
    {
        return _projection._rC;
    }

    /**
     * Getter for the translation vector.
     *
     * @return returns translation vector (x, y, z)
     */
    public float[] getTranslation()
    {
        return _projection._T;
    }

}
