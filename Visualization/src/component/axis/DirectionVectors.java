package component.axis;

/**
 * Supportive container-class for drawing axes.
 *
 * @author MTomczyk
 */
public class DirectionVectors
{
    /**
     * Starting coordinates.
     */
    public float[] _s;

    /**
     * Ending coordinates.
     */
    public float[] _e;

    /**
     * Delta when determining tick position (unique per tick, hence 2D matrix).
     */
    public float[][] _tp;

    /**
     * Delta when determining the end position of a tick (unique per tick, hence 2D matrix).
     */
    public float[][] _dt;

    /**
     * Delta when determining the beginning position of a tick label (unique per tick, hence 2D matrix).
     */
    public float[][] _dtl;

    /**
     * Delta when determining the beginning position of a main label.
     */
    public float[] _dl;

    /**
     * No dimensions (for rendering, e.g., 2 = 2D or 3 = 3D).
     */
    public int _dimensions;

    /**
     * Can be called to clear data.
     */
    public void dispose()
    {
        _e = null;
        _s = null;
        _tp = null;
        _dt = null;
        _dtl = null;
        _dl = null;
    }

    /**
     * Parameterized constructor. Instantiates global (not per tick) parameters.
     *
     * @param dimensions number of dimensions
     */
    public DirectionVectors(int dimensions)
    {
        _dimensions = dimensions;
        _s = new float[dimensions];
        _e = new float[dimensions];
        _dl = new float[dimensions];
    }

    /**
     * Called to instantiate ticks-related arrays.
     *
     * @param noTicks no ticks
     */
    public void instantiateTickArrays(int noTicks)
    {
        _tp = new float[noTicks][_dimensions];
        _dt = new float[noTicks][_dimensions];
        _dtl = new float[noTicks][_dimensions];
    }
}
