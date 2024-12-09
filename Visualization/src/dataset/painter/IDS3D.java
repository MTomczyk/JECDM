package dataset.painter;

import gl.vboutils.BufferData;

/**
 * Internal data structures for data set (IDS). It contains processed data that supports efficient rendering.
 * The extension introduces 3rd level IDS: vertices, indices, etc., to be uploaded into VBO.
 *
 * @author MTomczyk
 */
public class IDS3D extends IDS
{
    /**
     * Marker fill buffer data.
     */
    public BufferData _markersFillsBuffer;

    /**
     * Marker edge buffer data.
     */
    public BufferData _markersEdgesBuffer;

    /**
     * Marker line buffer data. One per one contiguous line.
     */
    public BufferData[] _linesBuffer;

    /**
     * Can be called to reset (clear) IDS.
     */
    @SuppressWarnings("DuplicatedCode")
    public void reset()
    {
        super.reset();
        _markersFillsBuffer = null;
        _markersEdgesBuffer = null;
        _linesBuffer = null;
    }
}
