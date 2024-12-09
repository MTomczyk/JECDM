package gl.vboutils;

import gl.VBOManager;

/**
 * Simple container for VBO data.
 */
public class BufferData
{
    /**
     * Parameterized constructor (not accessible).
     *
     * @param vertices     vertices
     * @param indicesInt   indices (ints)
     * @param indicesShort indices (short)
     * @param colors       color
     */
    protected BufferData(float[] vertices, int[] indicesInt, short[] indicesShort, float[] colors)
    {
        _vertices = vertices;
        _indicesInt = indicesInt;
        _indicesShort = indicesShort;
        _colors = colors;
    }

    /**
     * Parameterized constructor.
     *
     * @param vertices     vertices
     * @param indicesShort indices (short)
     * @param colors       color
     */
    public BufferData(float[] vertices, short[] indicesShort, float[] colors)
    {
        this(vertices, null, indicesShort, colors);
    }

    /**
     * Parameterized constructor.
     *
     * @param vertices   vertices
     * @param indicesInt indices (integers)
     * @param colors     color
     */
    public BufferData(float[] vertices, int[] indicesInt, float[] colors)
    {
        this(vertices, indicesInt, null, colors);
    }

    /**
     * Vertices.
     */
    public final float[] _vertices;

    /**
     * Indices (integers)
     */
    public final int[] _indicesInt;

    /**
     * Indices (shorts)
     */
    public final short[] _indicesShort;

    /**
     * Colors.
     */
    public final float[] _colors;

    /**
     * Creates a clone (deep copy).
     *
     * @return cloned object
     */
    public BufferData getClone()
    {
        float[] v = null;
        if (_vertices != null) v = _vertices.clone();
        int[] ii = null;
        if (_indicesInt != null) ii = _indicesInt.clone();
        short[] is = null;
        if (_indicesShort != null) is = _indicesShort.clone();
        float[] c = null;
        if (_colors != null) c = _colors.clone();
        return new BufferData(v, ii, is, c);
    }

    /**
     * Creates the VBO manager based on the maintained data.
     *
     * @param mode         rendering mode (GL_LINES, etc.)
     * @param vertexStride vertex array stride
     * @param colorStride  color array stride
     * @return the created VBO manager object
     */
    public VBOManager createVBOManager(int mode, int vertexStride, int colorStride)
    {
        if (_indicesShort != null)
            return new VBOManager(_vertices, _indicesShort, _colors, mode, vertexStride, colorStride);
        else return new VBOManager(_vertices, _indicesInt, _colors, mode, vertexStride, colorStride);
    }
}
