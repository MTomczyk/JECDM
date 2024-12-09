package space;

import statistics.incrementalcontainer.IncrementalContainerFloat;
import statistics.incrementalcontainer.IncrementalContainerLong;

/**
 * Interpolates path (points; timestamps) using velocity deltas (delta position/delta time).
 *
 * @author MTomczyk
 */

public class VectorPathInterpolation
{
    /**
     * X-coordinates stored.
     */
    private final IncrementalContainerFloat _x;

    /**
     * X-velocity, 1st delta.
     */
    private final IncrementalContainerFloat[] _vx;

    /**
     * X-coordinates stored.
     */
    private final IncrementalContainerFloat _y;

    /**
     * Y-velocity, 1st delta.
     */
    private final IncrementalContainerFloat[] _vy;

    /**
     * Timestamps stored (longs).
     */
    private final IncrementalContainerLong _t;

    /**
     * Used to rescale delta times (e.g., to move from nanoseconds to seconds).
     */
    private final float _deltaTimeScale;

    /**
     * Parameterized constructor.
     *
     * @param size the number of past data points to be stored (at least 3; assertion).
     */
    public VectorPathInterpolation(int size)
    {
        this(size, 1.0f);
    }

    /**
     * Parameterized constructor.
     *
     * @param size            the number of past data points to be stored (at least 2; assertion; if >= 3, second deltas are used).
     * @param deltaTimesScale used to rescale delta times (e.g., to move from nanoseconds to seconds).
     */
    public VectorPathInterpolation(int size, float deltaTimesScale)
    {
        assert size > 1;
        _x = new IncrementalContainerFloat(size);
        if (size > 2)
        {
            _vx = new IncrementalContainerFloat[]{
                    new IncrementalContainerFloat(size - 1),
                    new IncrementalContainerFloat(size - 2)
            };
            _y = new IncrementalContainerFloat(size);
            _vy = new IncrementalContainerFloat[]{
                    new IncrementalContainerFloat(size - 1),
                    new IncrementalContainerFloat(size - 2)
            };
        }
        else
        {
            _vx = new IncrementalContainerFloat[]{
                    new IncrementalContainerFloat(size - 1)
            };
            _y = new IncrementalContainerFloat(size);
            _vy = new IncrementalContainerFloat[]{
                    new IncrementalContainerFloat(size - 1)
            };
        }

        _t = new IncrementalContainerLong(size);
        _deltaTimeScale = deltaTimesScale;
    }

    /**
     * Resets data.
     */
    public void reset()
    {
        _x.reset();
        _y.reset();
        _t.reset();
        _vx[0].reset();
        if (_vx.length > 1) _vx[1].reset();
        _vy[0].reset();
        if (_vy.length > 1) _vy[1].reset();
    }

    /**
     * Calculates the interpolated position after a specific time.
     *
     * @param time current time
     * @return interpolated positions
     */
    public float[] getInterpolatedPosition(long time)
    {
        float[] r = new float[2];
        if (_x.getNoStoredElements() == 0) return r; // <-- interpolated as 0 vector

        r[0] = _x.getRecentElement();
        r[1] = _y.getRecentElement();

        if (_x.getNoStoredElements() == 1) return r; // <- no first delta;

        float vx = _vx[0].getRecentElement();
        float vy = _vy[0].getRecentElement();

        if ((_x.getNoStoredElements() > 2) && (_vx.length > 1)) // second delta exists
        {
            vx += _vx[1].getRecentElement();
            vy += _vy[1].getRecentElement();
        }

        float dt = (time - _t.getRecentElement()) * _deltaTimeScale;
        r[0] += vx * dt;
        r[1] += vy * dt;

        return r;
    }

    /**
     * Updates data.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param t timestamp
     */
    public void addPoint(float x, float y, long t)
    {
        // CHECK TIME CONDITIONS
        long dtl = 0;

        if (_x.getNoStoredElements() > 0)
        {
            dtl = t - _t.getRecentElement();
            if (dtl == 0) return;
        }

        float dt = dtl * _deltaTimeScale;

        _x.addElement(x);
        _y.addElement(y);
        _t.addElement(t);
        if (_x.getNoStoredElements() < 2) return;

        int cp = _x.getCurrentIndex();
        int pp = cp - 1;
        if (pp < 0) pp = _x.getContainerSize() - 1;

        // update velocity
        _vx[0].addElement((_x._data[cp] - _x._data[pp]) / dt);
        _vy[0].addElement((_y._data[cp] - _y._data[pp]) / dt);

        if (_vx[0].getNoStoredElements() < 2) return;
        cp = _vx[0].getCurrentIndex();
        pp = cp - 1;
        if (pp < 0) pp = _vx[0].getContainerSize() - 1;

        if ((_vx.length > 1))
        {
            _vx[1].addElement(_vx[0]._data[cp] - _vx[0]._data[pp]);
            _vy[1].addElement(_vy[0]._data[cp] - _vy[0]._data[pp]);
        }
    }

    /**
     * If true, there are some data points stored. False otherwise.
     *
     * @return true, there are some data points stored. False otherwise
     */
    public boolean hasData()
    {
        return _vx[0].getNoStoredElements() > 0;
    }

    /**
     * Returns velocities and deltas.
     *
     * @return velocities and deltas
     */
    public IncrementalContainerFloat[] getVX()
    {
        return _vx;
    }

    /**
     * Returns velocities and deltas.
     *
     * @return velocities and deltas
     */
    public IncrementalContainerFloat[] getVY()
    {
        return _vy;
    }


    /**
     * Returns x data.
     *
     * @return x data
     */
    public IncrementalContainerFloat getX()
    {
        return _x;
    }

    /**
     * Returns y data.
     *
     * @return y data
     */
    public IncrementalContainerFloat getY()
    {
        return _y;
    }


    /**
     * Calculates the time that passed since the most recent element was added. Note that the delta time is rescaled using
     * the _deltaTimeScale field. If no elements were added yet, it returns 0.
     *
     * @param currentTime current timestamp
     * @return delta time (rescaled)
     */
    public float getTimePassedUntilLastCall(long currentTime)
    {
        if (_t.getNoStoredElements() == 0) return 0;
        return _deltaTimeScale * (currentTime - _t.getRecentElement());
    }

}
