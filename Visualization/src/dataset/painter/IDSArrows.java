package dataset.painter;

import color.Color;

/**
 * Internal data structures dedicated to painting arrows.
 *
 * @author MTomczyk
 */
public class IDSArrows
{
    /**
     * Used only when the arrow colors follow some gradient (color is not mono). Colors are pre-determined and stored.
     */
    public Color[] _arrowFillColors = null;

    /**
     * Auxiliary projected data associated with arrows (for rendering).
     */
    public float[] _arrowProjectedData = null;

    /**
     * Stride in _arrowProjectedData.
     */
    public int _stride = 0;

    /**
     * No vertices.
     */
    public int _verts = 0;

    /**
     * Resets (nulls) the data.
     */
    public void reset()
    {
        _verts = 0;
        _stride = 0;
        _arrowFillColors = null;
        _arrowProjectedData = null;
    }
}
