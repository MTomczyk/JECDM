package component.legend;

/**
 * Supportive class storing information on the properties of the legend drawing area.
 */
public class Dimensions
{
    /**
     * Supportive field used when drawing entries: height of one entry.
     */
    public float _entryHeight = 0.0f;

    /**
     * Supportive field used when drawing entries: width of the left sub-column where the drawings (markers/lines/arrows) are drawn.
     */
    public float _drawingColumnWidth = 0.0f;

    /**
     * Represents a discrete number of segments in the drawing column (used when determining x-coordinates of markers/lines/arrows)
     */
    public int _noHalfSegments = 0;

    /**
     * No. segment that starts the line.
     */
    public int _lineBeginningHalfSegment = 0;

    /**
     * No. segment that ends the line.
     */
    public int _lineEndingHalfSegment = 0;

    /**
     * No. segments that represents the centers of beginning arrows
     */
    public int[] _bArrowsCentersNoHalfSegments = null;

    /**
     * No. segments that represents the centers of beginning arrows
     */
    public int[] _eArrowsCentersNoHalfSegments = null;

    /**
     * No. segments that represents the centers of beginning arrows
     */
    public int[] _markerCentersNoHalfSegments = null;

    /**
     * Precalculated marker X-coordinates.
     */
    public float[] _markersX = null;

    /**
     * Precalculated beginning arrow X-coordinates.
     */
    public float[] _bArrowsX = null;

    /**
     * Precalculated ending arrow X-coordinates.
     */
    public float[] _eArrowsX = null;

    /**
     * Line beginning X coordinate.
     */
    public float _lineBeginnningX = 0.0f;

    /**
     * Line ending X coordinate.
     */
    public float _lineEndingX = 0.0f;

    /**
     * Supportive field used when drawing entries: width of the right column where the labels are drawn.
     */
    public float _labelColumnWidth = 0.0f;

    /**
     * Supportive field used when drawing entries: spacing between the left and the right sub-column (drawing and labels)
     */
    public float _drawingLabelColumnsSeparator = 0.0f;

    /**
     * Supportive field for scaling legend entries (markers).
     */
    public float _markerScalingFactor = 1.0f;

    /**
     * Supportive field for additionally rescaling the "_markerScalingFactor" field.
     */
    public float _markerScalingFactorMultiplier = 1.0f;

    /**
     * Supportive field for scaling legend entries (lines).
     */
    public float _lineScalingFactor = 1.0f;

    /**
     * Supportive field for additionally rescaling the "_lineScalingFactor" field.
     */
    public float _lineScalingFactorMultiplier = 1.0f;

    /**
     * Supportive field for scaling legend entries (arrows).
     */
    public float _arrowsScalingFactor = 1.0f;

    /**
     * Supportive field for additionally rescaling the "_arrowsScalingFactor" field.
     */
    public float _arrowsScalingFactorMultiplier = 1.0f;

    /**
     * Expected dimensions of the legend.
     */
    public float[] _expectedDimensions = new float[]{0.0f, 0.0f};

    /**
     * Expected number of entries to be drawn.
     */
    public int _noEntries = 0;

    /**
     * If true, the markers are to be drawn.
     */
    public boolean _drawMarkers = false;

    /**
     * If true, the lines are to be drawn.
     */
    public boolean _drawLines = false;

    /**
     * If true, marker fills/edges use gradients.
     */
    public boolean _markersUseGradient = false;

    /**
     * If true, the arrow beginnings are to be drawn.
     */
    public boolean _drawBArrows = false;

    /**
     * If true, the arrow beginnings use gradients.
     */
    public boolean _bArrowsUseGradients = false;

    /**
     * If true, the arrow endings are to be drawn.
     */
    public boolean _drawEArrows = false;

    /**
     * If true, the arrow endings use gradients.
     */
    public boolean _eArrowsUseGradients = false;

    /**
     * The effective number of columns (when the no. of rows is limited).
     */
    public int _noColumns = 0;


    /**
     * Default constructor.
     */
    public Dimensions()
    {
        reset();
    }


    /**
     * Resets the fields.
     */
    public void reset()
    {
        _noHalfSegments = 0;
        _entryHeight = 0.0f;
        _drawingColumnWidth = 0.0f;
        _labelColumnWidth = 0.0f;
        _drawingLabelColumnsSeparator = 0.0f;
        _expectedDimensions[0] = 0.0f;
        _expectedDimensions[1] = 0.0f;
        _markerScalingFactor = 1.0f;
        _lineScalingFactor = 1.0f;
        _arrowsScalingFactor = 1.0f;
        _noEntries = 0;
        _drawMarkers = false;
        _markersUseGradient = false;
        _drawLines = false;
        _drawBArrows = false;
        _bArrowsUseGradients = false;
        _drawEArrows = false;
        _eArrowsUseGradients = false;
        _lineBeginningHalfSegment = 0;
        _lineEndingHalfSegment = 0;
        _bArrowsCentersNoHalfSegments = null;
        _eArrowsCentersNoHalfSegments = null;
        _markerCentersNoHalfSegments = null;
        _markersX = null;
        _bArrowsX = null;
        _eArrowsX = null;
        _lineBeginnningX = 0.0f;
        _lineEndingX = 0.0f;
    }
}
