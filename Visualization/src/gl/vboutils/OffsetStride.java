package gl.vboutils;

/**
 * Auxiliary class for capturing current offset and stride, used when filling
 * data arrays for the VBO.
 *
 * @author MTomczyk
 */
public class OffsetStride
{
    /**
     * Vertex array offset.
     */
    public int _vaOffset = 0;

    /**
     * Vertex array stride.
     */
    public int _vaStride = 0;

    /**
     * Indices array offset (points to the locations in the array being filled).
     */
    public int _iaOffset = 0;

    /**
     * Indices array stride.
     */
    public int _iaStride = 0;

    /**
     * Indices offset (points to the locations in the array being read)
     */
    public int _iOffset = 0;

    /**
     * Indices stride.
     */
    public int _iStride = 0;

    /**
     * Color array offset.
     */
    public int _caOffset = 0;

    /**
     * Color array stride.
     */
    public int _caStride = 0;

    /**
     * Shifts the current offset values using their corresponding strides.
     */
    public void applyStrides()
    {
        _vaOffset += _vaStride;
        _iaOffset += _iaStride;
        _iOffset += _iStride;
        _caOffset += _caStride;
    }
}
