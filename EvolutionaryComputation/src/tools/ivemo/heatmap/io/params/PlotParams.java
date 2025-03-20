package tools.ivemo.heatmap.io.params;

import color.gradient.Gradient;
import dataset.painter.style.enums.Bucket;
import plot.AbstractPlot;
import space.Range;

/**
 * Params container for a single plot.
 *
 * @author MTomczyk
 */

public class PlotParams extends AbstractPlot.Params
{
    /**
     * Name of a binary file that contains heatmap data (without extension).
     */
    public String _fileName = null;

    /**
     * No. dimensions (2 or 3).
     */
    public int _dimensions = 2;

    /**
     * Title for the Z-axis.
     */
    public String _zAxisTitle;

    /**
     * Number of divisions on the X-axis (used to construct buckets).
     */
    public int _xAxisDivisions = -1;

    /**
     * Number of divisions on the Y-axis (used to construct buckets).
     */
    public int _yAxisDivisions = -1;

    /**
     * Number of divisions on the Y-axis (used to construct buckets).
     */
    public int _zAxisDivisions = -1;

    /**
     * Supportive field keeping max/min heatmap data values.
     */
    public Range _heatmapDisplayRange = null;

    /***
     * Heatmap title.
     */
    public String _heatmapTitle = null;

    /**
     * Data point style.
     */
    public Bucket _bucketStyle = Bucket.SQUARE_2D;

    /**
     * Marker size (currently used only for POINT style for 3D visualization).
     */
    public float _pointSize = 1.0f;

    /**
     * Gradient used to colorize heatmap entries.
     */
    public Gradient _gradient = null;

}
