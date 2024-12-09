package listeners.interact;

import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import thread.swingworker.Interactor;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * This class executes interactions triggered by {@link InteractListener}.
 *
 * @author MTomczyk
 */

public abstract class AbstractExecutor
{
    /**
     * "Move forward" id.
     */
    public static final int MOVE_FORWARD = 0;

    /**
     * "Move back" id.
     */
    public static final int MOVE_BACKWARD = 1;

    /**
     * "Move left" id.
     */
    public static final int MOVE_LEFT = 2;

    /**
     * "Move right" id.
     */
    public static final int MOVE_RIGHT = 3;

    /**
     * "Move up" id.
     */
    public static final int MOVE_UP = 4;

    /**
     * "Move down" id.
     */
    public static final int MOVE_DOWN = 5;

    /**
     * Reference to the global container.
     */
    protected GlobalContainer _GC;

    /**
     * Reference to the plot container.
     */
    protected PlotContainer _PC;

    /**
     * Reference to the projection data kept by {@link AbstractInteractListener}.
     */
    protected Projection _P;

    /**
     * Move flags indicating in which direction the projection should be translated.
     */
    protected boolean[] _moveFlags;

    /**
     * Translation speed (change in vector per second).
     */
    protected float _tSpeed;

    /**
     * Rotation speed (change in angles per percent of screen dimensions passed by the mouse).
     */
    protected float _rSpeed;

    /**
     * Flag indicating whether the left mouse button is pressed or not.
     */
    protected boolean _leftMouseButtonPressed = false;

    /**
     * Flag indicating whether the left mouse button is pressed or not.
     */
    protected boolean _rightMouseButtonPressed = false;

    /**
     * Anchored mouse coordinates.
     */
    protected int[] _mouseAnchoredCoordinates;

    /**
     * The most recently reported mouse coordinates.
     */
    protected int[] _mouseLastReportedCoordinates;

    /**
     * Parameterized constructor.
     *
     * @param GC     global container (shared object; stores references, provides various functionalities)
     * @param PC     plot container
     * @param P      reference to the projection data kept by {@link AbstractInteractListener}
     * @param tSpeed translation speed (change in vector per second)
     * @param rSpeed rotation speed (change in angles per percent of screen dimensions passed by the mouse)
     */
    public AbstractExecutor(GlobalContainer GC, PlotContainer PC, Projection P,
                            float tSpeed, float rSpeed)
    {
        _GC = GC;
        _PC = PC;
        _P = P;
        _tSpeed = tSpeed;
        _rSpeed = rSpeed;
        instantiateVectorFields();
    }

    /**
     * Instantiates some vector fields.
     */
    protected void instantiateVectorFields()
    {
        _moveFlags = new boolean[6];
        _mouseAnchoredCoordinates = new int[2];
        _mouseLastReportedCoordinates = new int[2];
    }


    /**
     * Can be used by an external timer (e.g., {@link Interactor}) to notify
     * the executor about the current time (in ns) and the delta time passed since the previous
     * notification. The executor can accordingly update the projection then.
     *
     * @param currentTime current time (nano)
     * @param deltaTime   delta time passed (in nanoseconds)
     */
    public void notifyTimestamp(long currentTime, long deltaTime)
    {

    }

    /**
     * Adjusts/corrects the input angle (in degrees).
     *
     * @param angle angle to be corrected
     * @return corrected angle
     */
    protected float getAdjustedAngle(float angle)
    {
        while (Float.compare(angle, 360.0f) > 0) angle -= 360.0f;
        while (Float.compare(angle, 0.0f) < 0) angle += 360.0f;
        return angle;
    }

    /**
     * Sets a-axis rotation.
     *
     * @param rx new x-axis rotation
     */
    protected void setXAxisCameraRotation(float rx)
    {
        _P._rC[0] = getAdjustedAngle(rx);
    }

    /**
     * Sets a-axis rotation.
     *
     * @param ry new y-axis rotation
     */
    protected void setYAxisCameraRotation(float ry)
    {
        _P._rC[1] = getAdjustedAngle(ry);
    }

    /**
     * Sets a-axis rotation.
     *
     * @param rx new x-axis rotation
     */
    protected void setXAxisObjectRotation(float rx)
    {
        _P._rO[0] = getAdjustedAngle(rx);
    }

    /**
     * Sets a-axis rotation.
     *
     * @param ry new y-axis rotation
     */
    protected void setYAxisObjectRotation(float ry)
    {
        _P._rO[1] = getAdjustedAngle(ry);
    }

    /**
     * Supportive method setting mouse-related fields.
     *
     * @param x         mouse current x-position
     * @param y         mouse current y-position
     * @param rCAnchorX Anchored (stored) x-axis camera rotation
     * @param rCAnchorY Anchored (stored) y-axis camera rotation
     * @param rOAnchorX Anchored (stored) x-axis object rotation
     * @param rOAnchorY Anchored (stored) y-axis object rotation
     */
    protected void setMouseXYData(int x, int y, float rCAnchorX, float rCAnchorY, float rOAnchorX, float rOAnchorY)
    {
        _mouseAnchoredCoordinates[0] = x;
        _mouseAnchoredCoordinates[1] = y;
        _P._rCAnchor[0] = rCAnchorX;
        _P._rCAnchor[1] = rCAnchorY;
        _P._rOAnchor[0] = rOAnchorX;
        _P._rOAnchor[1] = rOAnchorY;
    }

    /**
     * Setter for the current mouse position.
     *
     * @param x mouse x-coordinate
     * @param y mouse y-coordinate
     */
    public void setCurrentMouseData(int x, int y)
    {
        _mouseLastReportedCoordinates[0] = x;
        _mouseLastReportedCoordinates[1] = y;
    }

    /**
     * Notifies about the mouse clicked event.
     *
     * @param e the event to be processed
     */
    public void mouseClicked(MouseEvent e)
    {

    }

    /**
     * Notifies about the mouse pressed.
     *
     * @param e the event to be processed
     */
    public void mousePressed(MouseEvent e)
    {
        if ((SwingUtilities.isLeftMouseButton(e)) && (!_rightMouseButtonPressed))
        {
            _leftMouseButtonPressed = true;
            setCurrentMouseData(e.getX(), e.getY());
            setMouseXYData(e.getX(), e.getY(), _P._rC[0], _P._rC[1], _P._rO[0], _P._rO[1]);
        }
        else if ((SwingUtilities.isRightMouseButton(e)) && (!_leftMouseButtonPressed))
        {
            _rightMouseButtonPressed = true;
            setCurrentMouseData(e.getX(), e.getY());
            setMouseXYData(e.getX(), e.getY(), _P._rC[0], _P._rC[1], _P._rO[0], _P._rO[1]);
        }
    }

    /**
     * Notifies about the mouse released event.
     *
     * @param e the event to be processed
     */
    public void mouseReleased(MouseEvent e)
    {
        if ((_leftMouseButtonPressed) || (_rightMouseButtonPressed))
        {
            _leftMouseButtonPressed = false;
            _rightMouseButtonPressed = false;
            setMouseXYData(0, 0, _P._rC[0], _P._rC[1], _P._rO[0], _P._rO[1]);
            setCurrentMouseData(e.getX(), e.getY());
        }
    }

    /**
     * Notifies about the mouse dragged event.
     *
     * @param e the event to be processed
     */
    public void mouseDragged(MouseEvent e)
    {
        if ((_leftMouseButtonPressed) || (_rightMouseButtonPressed))
        {
            setCurrentMouseData(e.getX(), e.getY());
        }
    }

    /**
     * Notifies about the mouse entered event.
     *
     * @param e the event to be processed
     */
    public void mouseEntered(MouseEvent e)
    {

    }

    /**
     * Notifies about the mouse exited event.
     *
     * @param e the event to be processed
     */
    public void mouseExited(MouseEvent e)
    {

    }

    /**
     * Notifies about the mouse moved event.
     *
     * @param e the event to be processed
     */
    public void mouseMoved(MouseEvent e)
    {

    }

    /**
     * Notifies the executor that the key has been pressed
     *
     * @param key key code
     */
    public void notifyKeyPressed(int key)
    {

        if ((key == 'w') || (key == 'W'))
        {
            _moveFlags[MOVE_FORWARD] = true;
            _moveFlags[MOVE_BACKWARD] = false;
        }
        if ((key == 's') || (key == 'S'))
        {
            _moveFlags[MOVE_FORWARD] = false;
            _moveFlags[MOVE_BACKWARD] = true;
        }
        if ((key == 'a') || (key == 'A'))
        {
            _moveFlags[MOVE_LEFT] = true;
            _moveFlags[MOVE_RIGHT] = false;
        }
        if ((key == 'd') || (key == 'D'))
        {
            _moveFlags[MOVE_LEFT] = false;
            _moveFlags[MOVE_RIGHT] = true;
        }
        if ((key == 'q') || (key == 'Q'))
        {
            _moveFlags[MOVE_UP] = true;
            _moveFlags[MOVE_DOWN] = false;
        }
        if ((key == 'e') || (key == 'E'))
        {
            _moveFlags[MOVE_UP] = false;
            _moveFlags[MOVE_DOWN] = true;
        }
    }

    /**
     * Notifies the executor that the key has been released
     *
     * @param key key code
     */
    public void notifyKeyReleased(int key)
    {
        if ((key == 'w') || (key == 'W')) _moveFlags[MOVE_FORWARD] = false;
        if ((key == 's') || (key == 'S')) _moveFlags[MOVE_BACKWARD] = false;
        if ((key == 'a') || (key == 'A')) _moveFlags[MOVE_LEFT] = false;
        if ((key == 'd') || (key == 'D')) _moveFlags[MOVE_RIGHT] = false;
        if ((key == 'q') || (key == 'Q')) _moveFlags[MOVE_UP] = false;
        if ((key == 'e') || (key == 'E')) _moveFlags[MOVE_DOWN] = false;
    }

    /**
     * Can be used to notify whether all keys have been released.
     */
    public void notifyAllKeysReleased()
    {
        notifyKeyReleased('q');
        notifyKeyReleased('w');
        notifyKeyReleased('e');
        notifyKeyReleased('a');
        notifyKeyReleased('s');
        notifyKeyReleased('d');
    }

    /**
     * Can be used to notify that all mouse buttons have been released.
     */
    public void notifyAllMouseButtonsReleased()
    {
        _leftMouseButtonPressed = false;
        _rightMouseButtonPressed = false;
        setMouseXYData(0, 0, _P._rC[0], _P._rC[1], _P._rO[0], _P._rO[1]);
    }

    /**
     * Can be called to dispose the data.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, _PC,  "Interact executor timer for plot [id = " + PlotContainer.getID(_PC) + ": dispose method called");
        _PC = null;
        _moveFlags = null;
        _mouseAnchoredCoordinates = null;
        _mouseLastReportedCoordinates = null;
    }

}

