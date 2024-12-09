package plot.heatmap;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import drmanager.DisplayRangesManager;
import plot.heatmap.utils.Coords;
import search.InArraySearch;
import space.Range;
import space.normalization.minmax.AbstractMinMaxNormalization;


/**
 * Heatmap layer model that keeps the most essential data and performs the processing.
 *
 * @author MTomczyk
 */
public class HeatmapLayerModel
{
    /**
     * If true, the sorted mode is used. In this mode, instead of using raw heatmap data (double [][][]), the
     * sorted array of bucket coordinates is used. It supports more efficient value filtering (especially in 3D rendering).
     */
    private volatile boolean _sortedMode = false;

    /**
     * The number of divisions on axes (x-axis, y-axis, z-axis).
     */
    private final int[] _divs;

    /**
     * Gradient used when drawing the heatmap.
     */
    private final Gradient _gradient;

    /**
     * The normalizations are handled by {@link DisplayRangesManager}. It handles three (or four, depending on the dimensionality) display ranges:
     * 0 - responsible for providing x-coordinates for buckets (in normalized space),
     * 1 - responsible for providing y-coordinates for buckets (in normalized space),
     * (2) - responsible for providing z-coordinates for buckets (in normalized space),
     * 2 (3) - responsible for normalizing heatmap data.
     */
    private final DisplayRangesManager _heatmapDRM;

    /**
     * Data to illustrate in the form of a heatmap (order of dimensions: Z, Y, X). It is assumed that values of Double.NEGATIVE_INFINITY are skipped.
     */
    private double[][][] _data;

    /**
     * Data points (buckets) represented as an array sorted based on bucket values. It is assumed that either
     * regular data matrix or this array is not null. If this array is not null, it is used for processing.
     * The use of this array is motivated by enabling more efficient data processing (but at a higher preprocessing cost, though).
     */
    private Coords[] _sortedCoords;

    /**
     * Buckets' values sorted and stored as an array. Optionally used with ``sorted coords'' field.
     */
    private double[] _sortedValues;

    /**
     * Ticks data getter is used to construct normalized bucket coordinates (x/y/z-coordinates).
     */
    private final ITicksDataGetter[] _refTicks;

    /**
     * Optional filter. If not null, buckets are accepted only when their values are within the imposed range (closed interval).
     */
    private Range _valueFilter = null;

    /**
     * Integer range for the filtered bucked [left; right]. Used only when the buckets are presorted.
     * Indicates left and right (inclusive) bound for the indices interval indicating buckets to be drawn.
     */
    private int[] _filteredIndices = null;

    /**
     * Auxiliary mask (can be null) disabling rendering selected buckets (true -> rendering disabled) (order of dimensions: Z, Y, X).
     */
    private boolean[][][] _mask;

    /**
     * Heatmap dimensionality.
     */
    private final int _dimensions;


    /**
     * Parameterized constructor.
     *
     * @param xDiv            divisions along x-axis
     * @param yDiv            divisions along y-axis
     * @param zDiv            divisions along z-axis
     * @param normalizers     can be used to provide a normalized used by {@link Heatmap2DLayer} to determine bucket coordinates in the normalized [0-1] space (x-coordinates, y-coordinate, z-coordinate) -- used construct non-uniform heatmaps, if the element is null, the default linear normalized is used, note that min/max values of the provided normalizer are always overwritten by 0/1.
     * @param dimensions      heatmap dimensionality (2 for 2D, 3 for 3D)
     * @param heatmapGradient gradient for the heatmap buckets
     * @param heatmapDR       heatmap display range
     */
    public HeatmapLayerModel(int xDiv, int yDiv, int zDiv,
                             AbstractMinMaxNormalization[] normalizers,
                             int dimensions, Gradient heatmapGradient,
                             DisplayRangesManager.DisplayRange heatmapDR)
    {
        _data = new double[][][]{null};
        _dimensions = dimensions;
        _gradient = heatmapGradient;
        _divs = new int[]{xDiv, yDiv, zDiv};
        _heatmapDRM = createHeatmapDisplayRangesManager(heatmapDR, normalizers, dimensions);
        _refTicks = getReferenceTicksGetters(dimensions);
    }

    /**
     * Auxiliary method creating the ticks getters used when determining bucket coordinates.
     *
     * @param dimensions no. dimensions (2 for 2D and 3 for 3D).
     * @return ticks getters
     */
    protected ITicksDataGetter[] getReferenceTicksGetters(int dimensions)
    {
        ITicksDataGetter[] refTicks = new FromDisplayRange[dimensions];
        for (int i = 0; i < dimensions; i++)
            refTicks[i] = new FromDisplayRange(_heatmapDRM.getDisplayRange(i), _divs[i] + 1);
        return refTicks;
    }

    /**
     * Setter for the value filter.
     *
     * @param valueFilter value filter
     */
    public void setValueFilter(Range valueFilter)
    {
        _valueFilter = valueFilter;
    }


    /**
     * Returns the left index for the indices interval (indices pointing to buckets to be painted; left side closed).
     * Used only in the ''sorted mode'' (i.e., sorted coords array is used).
     *
     * @return left index
     */
    public int getLeftIndicesBound()
    {
        if (_filteredIndices == null) return 0;
        else return _filteredIndices[0];
    }

    /**
     * Returns the right index for the indices interval (indices pointing to buckets to be painted; right side open).
     * Used only in the ''sorted mode'' (i.e., sorted coords array is used).
     *
     * @return right index
     */
    public int getRightIndicesBound()
    {
        if (_filteredIndices == null)
        {
            if (_sortedCoords != null) return _sortedCoords.length - 1;
            else return 0;
        }
        else return _filteredIndices[1];
    }

    /**
     * Can be called to determine left (closed) and right (open) bound for the indices of buckets that
     * satisfy the value filter.
     */
    protected void determineTheIndicesInterval()
    {
        if (_filteredIndices == null) _filteredIndices = new int[2]; // lazy init
        if (!_sortedMode) return;

        _filteredIndices[0] = InArraySearch.getIndexAtLeast(_valueFilter.getLeft(), _sortedValues, false);
        _filteredIndices[1] = InArraySearch.getIndexAtMost(_valueFilter.getRight(), _sortedValues, false);

        if (_filteredIndices[0] < 0) _filteredIndices[0] = 0;
        if (_filteredIndices[1] < 0) _filteredIndices[0] = 0;
        if (_filteredIndices[0] >= _sortedValues.length) _filteredIndices[0] = _sortedValues.length - 1;
        if (_filteredIndices[1] >= _sortedValues.length) _filteredIndices[1] = _sortedValues.length - 1;
    }

    /**
     * Calculates the number of buckets that are not masked. Works only in the sorted mode.
     * In the case the raw data is used, the method returns -1.
     *
     * @return the number of buckets that are not masked,
     */
    protected int getNoNotMaskedBuckets()
    {
        if (!_sortedMode) return -1;
        if (_mask == null) return _sortedValues.length;
        int r = 0;
        for (Coords c : _sortedCoords) if (!isMasked(c._x, c._y, c._z)) r++;
        return r;
    }

    /**
     * Calculates the number of buckets that are masked and are (in order) strictly before the left index bound for
     * value filtering (works only in the sorted mode). In the case the raw data is used, the method returns -1.
     * If the left index bound or the mask is not set, the method returns 0.
     *
     * @return the number of buckets that are not masked,
     */
    protected int getNoMaskedBucketsPriorToLeftIndexBound()
    {
        if (!_sortedMode) return -1;
        if (_mask == null) return 0;
        if (_filteredIndices == null) return 0;
        int r = 0;
        for (int i = 0; i < _filteredIndices[0]; i++)
            if (isMasked(_sortedCoords[i]._x, _sortedCoords[i]._y, _sortedCoords[i]._z)) r++;
        return r;
    }

    /**
     * Calculates the number of buckets that are masked and are (in order) inclusive to the
     * indices bounds (works only in the sorted mode). In the case the raw data is used, the method returns -1.
     * If the left/right index bound or the mask is not set, the method returns 0.
     *
     * @return the number of buckets that are not masked,
     */
    protected int getNoMaskedBucketsBetweenIndexBounds()
    {
        if (!_sortedMode) return -1;
        if (_mask == null) return 0;
        if (_filteredIndices == null) return 0;
        int r = 0;
        for (int i = _filteredIndices[0]; i < _filteredIndices[1]; i++)
            if (isMasked(_sortedCoords[i]._x, _sortedCoords[i]._y, _sortedCoords[i]._z)) r++;
        return r;
    }

    /**
     * Setter for the mask.
     *
     * @param mask filter mask
     */
    public void setMask(boolean[][][] mask)
    {
        _mask = mask;
    }

    /**
     * Checks if the bucked is masked (and should not be displayed).
     *
     * @param x bucket x-coordinate
     * @param y bucket y-coordinate
     * @param z bucket z-coordinate
     * @return true = bucket is masked, false otherwise
     */
    protected boolean isMasked(int x, int y, int z)
    {
        // masking
        return (_mask != null) && (_mask.length > z) && (_mask[z] != null) &&
                (_mask[z].length > y) && (_mask[z][y] != null) &&
                (_mask[z][y].length > x) && (_mask[z][y][x]);
    }


    /**
     * Checks if the bucked is masked (and should not be displayed).
     *
     * @param x bucket x-coordinate
     * @param y bucket y-coordinate
     * @return true = bucket is masked, false otherwise
     */
    protected boolean isMasked(int x, int y)
    {
        // masking
        return (_mask != null) && (_mask.length > 0) && (_mask[0] != null) &&
                (_mask[0].length > y) && (_mask[0][y] != null) &&
                (_mask[0][y].length > x) && (_mask[0][y][x]);
    }

    /**
     * Checks if the input value is accepted by the value filter (yes, if the filter == null).
     *
     * @param value     value to be tested
     * @param inclusive if true, the closed value interval is used, open if false
     * @return true if the value filter is null or the value is in the filter's range; false otherwise
     */
    protected boolean isValueAccepted(double value, boolean inclusive)
    {
        if (_valueFilter == null) return true;
        return _valueFilter.isInRange(value, inclusive);
    }

    /**
     * Auxiliary method creating the heatmap display ranges manager
     *
     * @param heatmapDR   heatmap display ranges manager
     * @param normalizers can be used to provide a normalized used by {@link Heatmap2DLayer} to determine bucket coordinates in the normalized [0-1] space (x-coordinates, y-coordinate, z-coordinate) -- used construct non-uniform heatmaps, if the element is null, the default linear normalized is used, note that min/max values of the provided normalizer are always overwritten by 0/1.
     * @param dimensions  no. dimensions (2 for 2D and 3 for 3D).
     * @return instance of a display range manager
     */
    protected DisplayRangesManager createHeatmapDisplayRangesManager(DisplayRangesManager.DisplayRange heatmapDR,
                                                                     AbstractMinMaxNormalization[] normalizers,
                                                                     int dimensions)
    {
        DisplayRangesManager.Params pDRM = new DisplayRangesManager.Params();
        pDRM._DR = new DisplayRangesManager.DisplayRange[dimensions + 1];
        for (int i = 0; i < dimensions; i++)
        {
            pDRM._DR[i] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false, false);
            if ((normalizers != null) && (normalizers[i] != null))
            {
                pDRM._DR[i].setNormalizer(normalizers[i]);
                pDRM._DR[i].getNormalizer().setMinMax(0.0d, 1.0d);
            }
        }
        pDRM._DR[dimensions] = heatmapDR;
        if (pDRM._DR[dimensions] == null)
            pDRM._DR[dimensions] = new DisplayRangesManager.DisplayRange(null, true, false);
        pDRM._attIdx_to_drIdx = new Integer[dimensions + 1];
        for (int i = 0; i < dimensions + 1; i++) pDRM._attIdx_to_drIdx[i] = i;
        return new DisplayRangesManager(pDRM);
    }

    /**
     * Setter for the sorted mode.
     *
     * @param sortedMode sorted mode flag (true = sorted mode; false = raw mode).
     */
    protected void setSortedMode(boolean sortedMode)
    {
        _sortedMode = sortedMode;
    }

    /**
     * Can be called to check if the model is in sorted mode.
     *
     * @return true = the model is in the sorted mode; false otherwise
     */
    protected boolean isInSortedMode()
    {
        return _sortedMode;
    }


    /**
     * Setter for the heatmap raw data
     *
     * @param rawData heatmap raw data
     */
    protected void setRawData(double[][][] rawData)
    {
        _data = rawData;
    }


    /**
     * Getter for the heatmap raw data
     *
     * @return heatmap raw data
     */
    protected double[][][] getRawData()
    {
        return _data;
    }

    /**
     * Setter for the sorted buckets' values.
     *
     * @param sortedValues sorted buckets' values
     */
    protected void setSortedValues(double[] sortedValues)
    {
        _sortedValues = sortedValues;
    }


    /**
     * Getter for the sorted buckets' values.
     *
     * @return sorted buckets' values
     */
    protected double[] getSortedValues()
    {
        return _sortedValues;
    }

    /**
     * Setter for the sorted buckets' coordinates data
     *
     * @param sortedCoords buckets' coordinates data
     */
    protected void setSortedCoords(Coords[] sortedCoords)
    {
        _sortedCoords = sortedCoords;
    }

    /**
     * Getter for the sorted buckets' coordinates data
     *
     * @return buckets' coordinates data
     */
    protected Coords[] getSortedCoords()
    {
        return _sortedCoords;
    }

    /**
     * Getter for the heatmap gradient
     *
     * @return heatmap gradient
     */
    protected Gradient getHeatmapGradient()
    {
        return _gradient;
    }

    /**
     * Getter for the heatmap display ranges manager
     *
     * @return heatmap display ranges manager
     */
    protected DisplayRangesManager getHeatmapDRM()
    {
        return _heatmapDRM;
    }

    /**
     * Getter for the display range associated with the heatmap.
     *
     * @return heatmap display range
     */
    protected DisplayRangesManager.DisplayRange getHeatmapDisplayRange()
    {
        return _heatmapDRM.getDisplayRange(_dimensions);
    }

    /**
     * Getter for the number of divisions on axes (x-axis, y-axis, z-axis).
     *
     * @return the numbers of divisions on axes
     */
    protected int[] getDivisions()
    {
        return _divs;
    }

    /**
     * Getter for the ticks data getter used to determine buckets' x-coordinates.
     *
     * @return ticks data getter
     */
    protected ITicksDataGetter getXBucketCoordsTicksDataGetter()
    {
        if (_refTicks == null) return null;
        if (_refTicks.length < 1) return null;
        return _refTicks[0];
    }

    /**
     * Getter for the ticks data getter used to determine buckets' y-coordinates.
     *
     * @return ticks data getter
     */
    protected ITicksDataGetter getYBucketCoordsTicksDataGetter()
    {
        if (_refTicks == null) return null;
        if (_refTicks.length < 2) return null;
        return _refTicks[1];
    }

    /**
     * Getter for the ticks data getter used to determine buckets' z-coordinates.
     *
     * @return ticks data getter
     */
    protected ITicksDataGetter getZBucketCoordsTicksDataGetter()
    {
        if (_refTicks == null) return null;
        if (_refTicks.length < 2) return null;
        return _refTicks[2];
    }

    /**
     * Can be called to clear memory.
     */
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        _data = null;
        _sortedCoords = null;
        _sortedValues = null;
        _valueFilter = null;
        _filteredIndices = null;
    }
}
