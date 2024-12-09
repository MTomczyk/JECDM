package listeners.interact;

/**
 * Contains data and provides functionalities related to camera manipulation.
 *
 * @author MTomczyk
 */
public class Projection
{
    /**
     * Object rotation [X, Y] axis.
     */
    public volatile float[] _rO = new float[2];

    /**
     * Camera rotation [X, Y] axis.
     */
    public volatile float[] _rC = new float[2];

    /**
     * Translation vector.
     */
    public volatile float[] _T = new float[3];

    /**
     * Default object-rotation data.
     */
    protected volatile float[] _defRO = new float[2];

    /**
     * Default camera-rotation data.
     */
    protected volatile float[] _defRC = new float[2];

    /**
     * Default translationVector;
     */
    protected volatile float[] _defT = new float[3];

    /**
     * Anchored (stored) camera X and Y axis rotation.
     */
    protected volatile float[] _rCAnchor = new float[2];

    /**
     * Anchored (stored) object X and Y axis rotation.
     */
    protected volatile float[] _rOAnchor = new float[2];

    /**
     * Setter for the default projection.
     *
     * @param defRO default object-rotation
     * @param defRC default camera-rotation
     * @param defT  default translation
     */
    public void setDefaultProjection(float[] defRO, float[] defRC, float[] defT)
    {
        _defRO = defRO;
        _defRC = defRC;
        _defT = defT;
    }

    /**
     * Sets the projection to default;
     */
    public void reset()
    {
        _rO = _defRO.clone();
        _rC = _defRC.clone();
        _T = _defT.clone();
        _rCAnchor[0] = 0.0f;
        _rCAnchor[1] = 0.0f;
        _rOAnchor[0] = 0.0f;
        _rOAnchor[1] = 0.0f;
    }

    /**
     * Clears data.
     */
    public void dispose()
    {
        _rO = null;
        _rC = null;
        _defRO = null;
        _defRC = null;
        _rCAnchor = null;
        _rOAnchor = null;
    }
}
