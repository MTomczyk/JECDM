package dataset.painter;

import color.Color;

import java.util.LinkedList;

/**
 * Internal data structures for data set (IDS). It contains processed data that supports efficient rendering.
 *
 * @author MTomczyk
 */
public class IDS
{
    /**
     * No. attributes.
     */
    public int _noAttributes = 0;

    /**
     * The size of a projected entry (e.g., a point).
     */
    public int _pSize = 0;

    /**
     * Additional field for mapping the data point index to display range index (usually 1:1 mapping). Can be null if the attribute does not affect a display range.
     */
    public Integer[] _attIdx_to_drIdx;

    /**
     * Additional field for mapping the display range index to the data point index (usually 1:1 mapping).
     */
    public int[] _drIdx_to_attIdx;

    /**
     * Additional field for mapping the display range index to data point index (usually 1:1 mapping).
     * Flat = the mapping is considered as if '_attIdx_to_drIdx' had no null and all entries were shifted to
     * the left.
     */
    public int[] _drIdx_to_flatAttIdx;

    /**
     * No valid marker points to be rendered.
     */
    public int _noMarkerPoints = 0;

    /**
     * Total number of individual line points (among all segments).
     */
    public int _noLinePoints = 0;

    /**
     * Total number of individual line points for each contiguous line.
     */
    public LinkedList<Integer> _noLinePointsInContiguousLines;

    /**
     * Normalized marker points coordinates (to 0->1 bounds, as imposed by display ranges).
     * Data points are stored sequentially, taking n subsequent elements, where n denotes the number of attributes/dimensions.
     */
    public float[] _normalizedMarkers = null;

    /**
     * Used only when the marker colors follow some gradient (color is not mono). Colors are pre-determined and stored.
     */
    public Color[] _markerFillGradientColors = null;

    /**
     * Used only when the marker colors follow some gradient (color is not mono). Colors are pre-determined and stored.
     */
    public Color[] _markerEdgeGradientColors = null;

    /**
     * List of contiguous lines. Data points are stored sequentially, taking n subsequent elements, where n denotes the number of attributes/dimensions.
     */
    public LinkedList<float[]> _normalizedContiguousLines = null;

    /**
     * Used only when the line colors follow some gradient (color is not mono). Colors are pre-determined and stored (each entry is linked to a different contiguous line).
     */
    public LinkedList<Color[]> _lineGradientColors = null;

    /**
     * Normalized marker points coordinates in the rendering space.
     * Data points are stored sequentially, taking n subsequent elements, where n denotes the number of attributes/dimensions.
     */
    public float[] _projectedMarkers = null;

    /**
     * List of contiguous lines in the rendering space.
     * Data points are stored sequentially, taking n subsequent elements, where n denotes the number of attributes/dimensions.
     */
    public LinkedList<float[]> _projectedContiguousLines = null;

    // === aux data for gradient lines

    /**
     * Determines the number of additional data points per one line segment (used when the line uses a gradient color).
     */
    public LinkedList<int[]> _auxProjectedLinesNoPoints = null;

    /**
     * Determines the coordinates of auxiliary points per line segment (used when the line uses a gradient color).
     */
    public LinkedList<float[]> _auxProjectedContiguousLines = null;

    /**
     * Determines the auxiliary gradient colors per one line segment (used when the line uses a gradient color).
     */
    public LinkedList<Color[]> _auxLineGradientColors = null;

    /**
     * Can be called to reset (clear) IDS.
     */
    public void reset()
    {
        _attIdx_to_drIdx = null;
        _drIdx_to_attIdx = null;
        _drIdx_to_flatAttIdx = null;

        _noAttributes = 0;
        _noMarkerPoints = 0;
        _noLinePoints = 0;

        _noLinePointsInContiguousLines = null;

        _normalizedMarkers = null;
        _markerFillGradientColors = null;
        _markerEdgeGradientColors = null;

        _normalizedContiguousLines = null;
        _lineGradientColors = null;

        _projectedMarkers = null;
        _projectedContiguousLines = null;

        _auxProjectedContiguousLines = null;
        _auxProjectedLinesNoPoints = null;
        _auxLineGradientColors = null;
    }
}
