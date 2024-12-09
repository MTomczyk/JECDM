package listeners.interact;

import container.GlobalContainer;
import container.PlotContainer;
import space.Vector;
import swing.swingworkerqueue.ExecutionBlock;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.BlockTypes;
import thread.swingworker.EventTypes;
import thread.swingworker.Interactor;


/**
 * Implementation of {@link AbstractExecutor} for the default interactions with the 3D plot {@link plot.Plot3D}.
 *
 * @author MTomczyk
 */

public class Executor3D extends AbstractExecutor
{
    /**
     * Parameterized constructor.
     *
     * @param GC     global container (shared object; stores references, provides various functionalities)
     * @param PC     plot container
     * @param P      reference to the projection data kept by {@link AbstractInteractListener}
     * @param tSpeed translation speed (change in vector per second)
     * @param rSpeed rotation speed (change in angles per percent of panel dimensions passed by the mouse)
     */
    public Executor3D(GlobalContainer GC, PlotContainer PC, Projection P, float tSpeed, float rSpeed)
    {
        super(GC, PC, P, tSpeed, rSpeed);
    }


    /**
     * Can be used by an external timer (e.g., {@link Interactor}) to notify
     * the executor about the current time (in ns) and the delta time passed since the previous
     * notification. The executor can then update the projection accordingly.
     *
     * @param currentTime current time (nano)
     * @param deltaTime   delta time passed (in nanoseconds)
     */
    public void notifyTimestamp(long currentTime, long deltaTime)
    {
        float move = _tSpeed * deltaTime / 1000000000.0f;
        boolean changeRequired = false;

        if (_moveFlags[MOVE_FORWARD])
        {
            changeRequired = true;
            moveForward(-move);
        }
        if (_moveFlags[MOVE_BACKWARD])
        {
            changeRequired = true;
            moveForward(move);
        }

        if (_moveFlags[MOVE_LEFT])
        {
            changeRequired = true;
            moveSide(move, 90.0f);
        }
        if (_moveFlags[MOVE_RIGHT])
        {
            changeRequired = true;
            moveSide(move, -90.0f);
        }

        if (_moveFlags[MOVE_UP])
        {
            changeRequired = true;
            moveUp(move, -90.0f);
        }
        if (_moveFlags[MOVE_DOWN])
        {
            changeRequired = true;
            moveUp(move, 90.0f);
        }

        if ((_leftMouseButtonPressed) || (_rightMouseButtonPressed))
        {
            float dx = _mouseLastReportedCoordinates[0] - _mouseAnchoredCoordinates[0];
            float dy = _mouseLastReportedCoordinates[1] - _mouseAnchoredCoordinates[1];
            if ((Float.compare(dx, 0.0f) != 0) || (Float.compare(dy, 0.0f) != 0)) changeRequired = true;

            if (_PC.getDrawingArea() != null)
            {
                dx /= _PC.getDrawingArea().getWidth();
                dy /= _PC.getDrawingArea().getHeight();
            }

            if (_leftMouseButtonPressed)
            {
                setXAxisCameraRotation(_P._rCAnchor[0] + dy * _rSpeed);
                setYAxisCameraRotation(_P._rCAnchor[1] + dx * _rSpeed);
            }
            else
            {
                setXAxisObjectRotation(_P._rOAnchor[0] + dy * _rSpeed);
                setYAxisObjectRotation(_P._rOAnchor[1] + dx * _rSpeed);
            }
        }

        if (changeRequired)
        {
            QueuedSwingWorker<Void, Void> w = _PC.getDrawingArea().createRenderUpdater(EventTypes.ON_INTERACTION);
            ExecutionBlock<Void, Void> B = new ExecutionBlock<>(BlockTypes.RENDER_UPDATER_ON_INTERACTION, _PC.getPlotID(), w);
            _GC.registerWorkers(B);
        }
    }


    /**
     * Applies translation (moving camera forwards/backwards).
     *
     * @param d movement distance.
     */
    protected void moveForward(float d)
    {
        double[] t = Vector.getDirectionVectorUn(_P._rC[0], _P._rC[1]);
        _P._T[0] += (float) (t[0] * d);
        _P._T[1] -= (float) (t[1] * d);
        _P._T[2] += (float) (t[2] * d);
    }

    /**
     * Applies translation (moving camera sideways).
     *
     * @param d  movement distance.
     * @param dy modifier for the y-axis angle (0.0f = no modifier is applied)
     */
    protected void moveSide(float d, float dy)
    {
        float aY = (float) ((_P._rC[1] + dy) * Math.PI * 2.0f / 360.0f);
        _P._T[0] += (float) (Math.sin(aY) * d);
        _P._T[2] += (float) (Math.cos(aY) * d);
    }

    /**
     * Applies translation moving camera up/down.
     *
     * @param d  movement distance.
     * @param dx modifier for the x-axis angle (0.0f = no modifier is applied)
     */
    protected void moveUp(float d, float dx)
    {
        float aX = (float) ((_P._rC[0] + dx) * Math.PI * 2.0f / 360.0f);
        _P._T[1] -= (float) (Math.sin(aX) * d);
    }

    /**
     * Notifies the executor that the key has been pressed
     *
     * @param key key code
     */
    @Override
    public void notifyKeyPressed(int key)
    {
        if ((key == 'r') || (key == 'R'))
        {
            _mouseAnchoredCoordinates[0] = _mouseLastReportedCoordinates[0];
            _mouseAnchoredCoordinates[1] = _mouseLastReportedCoordinates[1];
            _P.reset();
            QueuedSwingWorker<Void, Void> w = _PC.getDrawingArea().createRenderUpdater(EventTypes.ON_INTERACTION);
            ExecutionBlock<Void, Void> B = new ExecutionBlock<>(BlockTypes.RENDER_UPDATER_ON_INTERACTION, _PC.getPlotID(), w);
            _GC.registerWorkers(B);
            return;
        }

        super.notifyKeyPressed(key);
    }

}

