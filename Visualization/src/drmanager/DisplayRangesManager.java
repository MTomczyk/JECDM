package drmanager;

import component.axis.swing.AbstractAxis;
import dataset.Data;
import space.Range;
import space.normalization.minmax.AbstractMinMaxNormalization;
import space.normalization.minmax.Linear;

/**
 * Display ranges manager is responsible for maintaining all data linked to display bounds imposed for rendering
 * (min/max values per each dimension to be portrayed). Used for properly projecting (normalizing) data points
 * to be displayed.
 *
 * @author MTomczyk
 */
public class DisplayRangesManager
{

    /**
     * Container-like class representing a display range linked to some dimension/attribute/objective/etc.
     */
    public static class DisplayRange
    {
        /**
         * Display range (main field). Can be null. If null, the data points are not projected but are displayed as they
         * are (normalization is ignored), given the associated dimension.
         */
        private Range _R;

        /**
         * If true, display range is dynamically updated when a new data set is examined (given the linked dimension).
         */
        private final boolean _updateDynamically;

        /**
         * If true and the {@link DisplayRange#_updateDynamically} flag is true, the display range is updated also
         * considering the past data. If false, the display range is recalculated from scratch.
         */
        private final boolean _updateFromScratch;

        /**
         * Auxiliary object for normalizing the rendering/objective space.
         * It is used for transforming input data points into normalized coordinates and
         * executing reversed transformation (e.g., when determining axes tick labels).
         */
        private AbstractMinMaxNormalization _normalizer;

        /**
         * Parameterized constructor. Display ranges are as provided and fixed.
         *
         * @param R display range (main field); can be null. If null, the data points are not projected but are displayed as they are (normalization is ignored), given the associated dimension
         */
        public DisplayRange(Range R)
        {
            this(R, false, false, new Linear());
        }

        /**
         * Parameterized constructor (update from scratch flag is set to false).
         *
         * @param R                 display range (main field); can be null. If null, the data points are not projected but are displayed as they are (normalization is ignored), given the associated dimension
         * @param updateDynamically if true, display range is dynamically updated when a new data set is examined (given the linked dimension)
         */
        public DisplayRange(Range R, boolean updateDynamically)
        {
            this(R, updateDynamically, false);
        }

        /**
         * Parameterized constructor.
         *
         * @param R                 display range (main field); can be null. If null, the data points are not projected but are displayed as they are (normalization is ignored), given the associated dimension
         * @param updateDynamically if true, display range is dynamically updated when a new data set is examined (given the linked dimension)
         * @param updateFromScratch if true and the {@link DisplayRange#_updateDynamically} flag is true, the display range is updated considering also the past data. If false, the display range is recalculated from scratch
         */
        public DisplayRange(Range R, boolean updateDynamically, boolean updateFromScratch)
        {
            this(R, updateDynamically, updateFromScratch, new Linear());
        }

        /**
         * Parameterized constructor.
         *
         * @param R                 display range (main field); can be null. If null, the data points are not projected but are displayed as they are (normalization is ignored), given the associated dimension
         * @param updateDynamically if true, display range is dynamically updated when a new data set is examined (given the linked dimension)
         * @param updateFromScratch if true and the {@link DisplayRange#_updateDynamically} flag is true, the display range is updated considering also the past data. If false, the display range is recalculated from scratch
         * @param normalizer        auxiliary object for normalizing the rendering/objective space; it is used for transforming input data points into normalized coordinates and executing reversed transformation (e.g., when determining axes tick labels)
         */
        public DisplayRange(Range R, boolean updateDynamically, boolean updateFromScratch, AbstractMinMaxNormalization normalizer)
        {
            _R = R;
            _updateDynamically = updateDynamically;
            _updateFromScratch = updateFromScratch;
            _normalizer = normalizer;
            if (_R != null) _normalizer.setMinMax(R.getLeft(), R.getRight());
        }

        /**
         * Getter for the display range.
         *
         * @return display range
         */
        public Range getR()
        {
            return _R;
        }

        /**
         * Setter for the display range.
         *
         * @param R display range
         */
        public void setR(Range R)
        {
            _R = R;
        }

        /**
         * Getter for the normalizer.
         *
         * @return normalizer
         */
        public AbstractMinMaxNormalization getNormalizer()
        {
            return _normalizer;
        }

        /**
         * Setter for the normalizer.
         *
         * @param normalizer normalizer object
         */
        public void setNormalizer(AbstractMinMaxNormalization normalizer)
        {
            _normalizer = normalizer;
        }

        /**
         * Setter for the normalizer. Copies min/max values from the previously stored normalizer (if not null).
         *
         * @param normalizer normalizer object
         */
        public void setNormalizerAndPreserveMinMax(AbstractMinMaxNormalization normalizer)
        {
            if (_normalizer != null) normalizer.setMinMax(_normalizer.getMin(), _normalizer.getMax());
            _normalizer = normalizer;
        }

        /**
         * Constructs a deep copy.
         *
         * @return copy
         */
        public DisplayRange getClone()
        {
            Range r = null;
            AbstractMinMaxNormalization normalizer = null;
            if (_R != null) r = _R.getClone();
            if (_normalizer != null) normalizer = _normalizer.getClone();
            return new DisplayRange(r, _updateDynamically, _updateFromScratch, normalizer);
        }
    }

    /**
     * Auxiliary report-like class summarizing the changes in the display ranges after executing the {@link DisplayRangesManager#updateDisplayRanges(Data, boolean[])} method.
     */
    public static class Report
    {
        /**
         * If true, at least one display range has changed.
         */
        public boolean _displayRangesChanged = false;

        /**
         * Specifies which display ranges have changed.
         */
        public boolean[] _displayRangeChanged;

        /**
         * Stored info on the previous display ranges (deep copies).
         */
        public Range[] _previousRanges;

        /**
         * Parameterized constructor.
         *
         * @param dim no. dimensions.
         */
        public Report(int dim)
        {
            _displayRangeChanged = new boolean[dim];
            _previousRanges = new Range[dim];
        }

        /**
         * Merges the results of two reports (joint/or conclusions). The results are stored in the current object.
         * Note that {@link Report#_previousRanges} are assumed to be the same for both objects.
         *
         * @param r other report
         */
        public void mergeWith(Report r)
        {
            _displayRangesChanged = _displayRangesChanged || r._displayRangesChanged;

            if (_displayRangeChanged == null)
            {
                if (r._displayRangeChanged != null) _displayRangeChanged = r._displayRangeChanged.clone();
            }
            else
            {
                if (r._displayRangeChanged != null)
                    for (int i = 0; i < Math.min(_displayRangeChanged.length, r._displayRangeChanged.length); i++)
                        _displayRangeChanged[i] = _displayRangeChanged[i] || r._displayRangeChanged[i];
            }

            // previous ranges are assumed to be the same
        }
    }

    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Container-like class representing a display range linked to some dimension/attribute/objective/etc.
         */
        public DisplayRange[] _DR;

        /**
         * Additional field for mapping the data point index to display range index (usually 1:1 mapping).
         * Can be null if the attribute does not affect a display range.
         */
        public Integer[] _attIdx_to_drIdx;

        /**
         * If true, the att->dr mapping is explicitly copied into dr->att and dr->att_flat mapping.
         */
        protected boolean _explicitlyCopyMappingFromAttToDr = false;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor (sets the display ranges as provided).
         *
         * @param range         ranges to be set
         * @param dynamicUpdate dynamic update flag set (incumbents will be used if true)
         */
        public Params(Range[] range, boolean dynamicUpdate)
        {
            _DR = new DisplayRange[range.length];
            for (int i = 0; i < range.length; i++)
                _DR[i] = new DisplayRange(range[i], dynamicUpdate, false);
        }

        /**
         * Params getter. Sets display ranges to null and allows them to be updated dynamically.
         *
         * @return params container
         */
        public static Params getFor2D()
        {
            return getFor2D(null, true, false, null, true, false);
        }

        /**
         * Params getter. Sets display ranges as provided and prohibits their update.
         *
         * @param xDisplayRange initial display range for X-axis
         * @param yDisplayRange initial display range for Y-axis
         * @return params container
         */
        public static Params getFor2D(Range xDisplayRange, Range yDisplayRange)
        {
            return getFor2D(xDisplayRange, false, false, yDisplayRange, false, false);
        }

        /**
         * Params getter. Sets display ranges as provided.
         *
         * @param xDisplayRange      initial display range for X-axis
         * @param updateDynamicallyX flag indicating whether X-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateXFromScratch flag indicating whether X-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @param yDisplayRange      initial display range for Y-axis
         * @param updateDynamicallyY flag indicating whether Y-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateYFromScratch flag indicating whether Y-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @return params container
         */
        public static Params getFor2D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                      Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch)
        {
            Params p = new Params();
            p._DR = new DisplayRange[2];
            p._DR[0] = getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
            p._DR[1] = getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
            p._attIdx_to_drIdx = new Integer[]{0, 1};
            return p;
        }


        /**
         * Params getter. Sets display ranges to null and allows them to be updated dynamically.
         *
         * @return params container
         */
        public static Params getFor3D()
        {
            return getFor3D(null, true, false, null,
                    true, false, null, true, false);
        }

        /**
         * Params getter. Sets display ranges as provided and prohibits their update.
         *
         * @param xDisplayRange initial display range for X-axis
         * @param yDisplayRange initial display range for Y-axis
         * @param zDisplayRange initial display range for Z-axis
         * @return params container
         */
        public static Params getFor3D(Range xDisplayRange, Range yDisplayRange, Range zDisplayRange)
        {
            return getFor3D(xDisplayRange, false, false,
                    yDisplayRange, false, false,
                    zDisplayRange, false, false);
        }

        /**
         * Params getter. Sets display ranges as provided.
         *
         * @param xDisplayRange      initial display range for X-axis
         * @param updateDynamicallyX flag indicating whether X-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateXFromScratch flag indicating whether X-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @param yDisplayRange      initial display range for Y-axis
         * @param updateDynamicallyY flag indicating whether Y-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateYFromScratch flag indicating whether Y-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @param zDisplayRange      initial display range for Z-axis
         * @param updateDynamicallyZ flag indicating whether Z-axis display rage should be updated dynamically (1:1 mapping with Z-axes)
         * @param updateZFromScratch flag indicating whether Z-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Z-axes)
         * @return params container
         */
        public static Params getFor3D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                      Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch,
                                      Range zDisplayRange, boolean updateDynamicallyZ, boolean updateZFromScratch)
        {
            Params p = new Params();
            p._DR = new DisplayRange[3];
            p._DR[0] = getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
            p._DR[1] = getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
            p._DR[2] = getParameterizedDisplayRange(zDisplayRange, updateDynamicallyZ, updateZFromScratch);
            p._attIdx_to_drIdx = new Integer[]{0, 1, 2};
            return p;
        }


        /**
         * Params getter. Sets display ranges as provided and prohibits their update (4 dimension = extra dimension).
         *
         * @param xDisplayRange initial display range for X-axis
         * @param yDisplayRange initial display range for Y-axis
         * @param zDisplayRange initial display range for Z-axis
         * @param aDisplayRange initial display range for the extra dimensions
         * @return params container
         */
        public static Params getFor4D(Range xDisplayRange, Range yDisplayRange, Range zDisplayRange, Range aDisplayRange)
        {
            return getFor4D(xDisplayRange, false, false,
                    yDisplayRange, false, false,
                    zDisplayRange, false, false,
                    aDisplayRange, false, false);
        }

        /**
         * Params getter. Sets display ranges as provided (4 dimension = extra dimension).
         *
         * @param xDisplayRange      initial display range for X-axis
         * @param updateDynamicallyX flag indicating whether X-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateXFromScratch flag indicating whether X-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @param yDisplayRange      initial display range for Y-axis
         * @param updateDynamicallyY flag indicating whether Y-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateYFromScratch flag indicating whether Y-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @param zDisplayRange      initial display range for Z-axis
         * @param updateDynamicallyZ flag indicating whether Z-axis display rage should be updated dynamically (1:1 mapping with Z-axes)
         * @param updateZFromScratch flag indicating whether Z-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Z-axes)
         * @param aDisplayRange      initial display range for the extra dimension
         * @param updateDynamicallyA flag indicating whether extra dimension's display rage should be updated dynamically
         * @param updateAFromScratch flag indicating whether extra dimension's display rage should be updated from scratch when updated dynamically
         * @return params container
         */
        public static Params getFor4D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                      Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch,
                                      Range zDisplayRange, boolean updateDynamicallyZ, boolean updateZFromScratch,
                                      Range aDisplayRange, boolean updateDynamicallyA, boolean updateAFromScratch)
        {
            Params p = new Params();
            p._DR = new DisplayRange[4];
            p._DR[0] = getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
            p._DR[1] = getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
            p._DR[2] = getParameterizedDisplayRange(zDisplayRange, updateDynamicallyZ, updateZFromScratch);
            p._DR[3] = getParameterizedDisplayRange(aDisplayRange, updateDynamicallyA, updateAFromScratch);
            p._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};
            return p;
        }

        /**
         * Params getter for the convergence plot.
         * Sets display ranges to null and allows them to be updated dynamically.
         * The third (upper bound) and the fourth (lower bound) attribute are mapped on the second display range that
         * should be linked to Y-axis.
         *
         * @return params container
         */
        public static Params getForConvergencePlot2D()
        {
            return getForConvergencePlot2D(null, true, false, null, true, false);
        }

        /**
         * Params getter for the converge plot.
         * Sets display ranges as provided and prohibits their update.
         * The third (upper bound) and the fourth (lowerr bound) attribute are mapped on the second display range that
         * should be linked to Y-axis.
         *
         * @param xDisplayRange display range for X-axis
         * @param yDisplayRange display range for Y-axis
         * @return params container
         */
        public static Params getForConvergencePlot2D(Range xDisplayRange, Range yDisplayRange)
        {
            return getForConvergencePlot2D(xDisplayRange, false, false, yDisplayRange, false, false);
        }

        /**
         * Params getter for the convergence plot.
         * Sets display ranges as provided.
         * The first display ranges are devoted to Y-axes (parallel coordinates).
         * The last display range is fixed and auxiliary, and is linked to X-axis ([0, 1] range).
         *
         * @param xDisplayRange      initial display range for X-axis
         * @param yDisplayRange      initial display range for Y-axis
         * @param updateDynamicallyY flag indicating whether Y-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateYFromScratch flag indicating whether Y-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @param updateDynamicallyX flag indicating whether X-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateXFromScratch flag indicating whether X-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @return params container
         */
        public static Params getForConvergencePlot2D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                                     Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch)
        {
            Params p = new Params();
            p._DR = new DisplayRange[2];
            p._DR[0] = getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
            p._DR[1] = getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
            p._attIdx_to_drIdx = new Integer[]{0, 1, 1, 1};
            p._explicitlyCopyMappingFromAttToDr = true;
            return p;
        }

        /**
         * Params getter for the parallel coordinate plot.
         * The ranges are unknown but are allowed to be updated dynamically.
         * The first display ranges are devoted to Y-axes (parallel coordinates).
         * The last display range is fixed and auxiliary, and is linked to X-axis ([0, 1] range).
         *
         * @param dimensions number of dimension  (np. parallel coordinate lines); should be at least 1
         * @return params container
         */
        public static Params getForParallelCoordinatePlot2D(int dimensions)
        {
            return getForParallelCoordinatePlot2D(dimensions, null, true, false);
        }

        /**
         * Params getter for the parallel coordinate plot (sets ``update from scratch'' flags to false).
         * Sets display ranges as provided. The first display ranges are devoted to Y-axes (parallel coordinates).
         * The last display range is fixed and auxiliary, and is linked to X-axis ([0, 1] range).
         *
         * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
         * @param yDisplayRange     initial display range (applied to each dimension)
         * @param updateDynamically flag indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes); applied to each dimension
         * @return params container
         */
        public static Params getForParallelCoordinatePlot2D(int dimensions, Range yDisplayRange, boolean updateDynamically)
        {
            return getForParallelCoordinatePlot2D(dimensions, yDisplayRange, updateDynamically, false);
        }

        /**
         * Params getter for the parallel coordinate plot.
         * Sets display ranges as provided. The first display ranges are devoted to Y-axes (parallel coordinates).
         * The last display range is fixed and auxiliary, and is linked to X-axis ([0, 1] range).
         *
         * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
         * @param yDisplayRange     initial display range (applied to each dimension)
         * @param updateDynamically flag indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes); applied to each dimension
         * @param updateFromScratch flag indicating whether a particular display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes); applied to each dimension
         * @return params container
         */
        public static Params getForParallelCoordinatePlot2D(int dimensions, Range yDisplayRange, boolean updateDynamically, boolean updateFromScratch)
        {
            Range[] r = new Range[dimensions];
            boolean[] ud = new boolean[dimensions];
            boolean[] us = new boolean[dimensions];
            for (int i = 0; i < dimensions; i++)
            {
                if (yDisplayRange != null) r[i] = yDisplayRange.getClone();
                else r[i] = null;
                ud[i] = updateDynamically;
                us[i] = updateFromScratch;
            }
            return getForParallelCoordinatePlot2D(dimensions, r, ud, us);
        }

        /**
         * Params getter for the parallel coordinate plot (sets ``update from scratch'' flags to false).
         * Sets display ranges as provided. The first display ranges are devoted to Y-axes (parallel coordinates).
         * The last display range is fixed and auxiliary, and is linked to X-axis ([0, 1] range).
         *
         * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
         * @param yDisplayRanges    initial display ranges per dimension
         * @param updateDynamically flags indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @return params container
         */
        public static Params getForParallelCoordinatePlot2D(int dimensions, Range[] yDisplayRanges, boolean[] updateDynamically)
        {
            return getForParallelCoordinatePlot2D(dimensions, yDisplayRanges, updateDynamically, new boolean[updateDynamically.length]);
        }

        /**
         * Params getter for the parallel coordinate plot.
         * Sets display ranges as provided. The first display ranges are devoted to Y-axes (parallel coordinates).
         * The last display range is fixed and auxiliary, and is linked to X-axis ([0, 1] range).
         *
         * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
         * @param yDisplayRanges    initial display ranges per dimension
         * @param updateDynamically flags indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes)
         * @param updateFromScratch flags indicating whether a particular display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
         * @return params container
         */
        public static Params getForParallelCoordinatePlot2D(int dimensions, Range[] yDisplayRanges, boolean[] updateDynamically, boolean[] updateFromScratch)
        {
            Params p = new Params();
            p._DR = new DisplayRange[dimensions + 1];
            p._attIdx_to_drIdx = new Integer[dimensions];
            p._explicitlyCopyMappingFromAttToDr = true;
            for (int i = 0; i < dimensions; i++)
            {
                p._DR[i] = new DisplayRange(yDisplayRanges[i], updateDynamically[i], updateFromScratch[i]);
                p._attIdx_to_drIdx[i] = i;
            }
            p._DR[dimensions] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false, false);
            return p;
        }

        /**
         * Display range constructor.
         *
         * @param R                 range for the display range (can be null if the display range is to be updated dynamically)
         * @param updateDynamically if true, the display range is to be updated dynamically, false = otherwise
         * @param updateFromScratch if true, the display range is rebuilt from scratch if updated dynamically (false = current range is used as a base for the update)
         * @return parameterized display range
         */
        private static DisplayRange getParameterizedDisplayRange(Range R, boolean updateDynamically, boolean updateFromScratch)
        {
            DisplayRange dr = new DisplayRange(R, updateDynamically, updateFromScratch);
            if ((R != null)) dr._normalizer.setMinMax(R.getLeft(), R.getRight());
            return dr;
        }
    }

    /**
     * Stores data on display ranges.
     */
    protected DisplayRange[] _DR;

    /**
     * Additional field for mapping the data point index to display range index (usually 1:1 mapping). Can be null if the attribute does not affect a display range.
     */
    protected Integer[] _attIdx_to_drIdx;

    /**
     * Additional field for mapping the display range index to the data point index (usually 1:1 mapping).
     */
    protected int[] _drIdx_to_attIdx;

    /**
     * Additional field for mapping the display range index to the data point index (usually 1:1 mapping).
     * Flat = the mapping is considered as if '_attIdx_to_drIdx' had no null and all entries were shifted to
     * the left.
     */
    protected int[] _drIdx_to_flatAttIdx;

    /**
     * Returns the number of attributes that are mapped into display range (equivalent to the dimensionality of the projection space).
     *
     * @return dimensionality of the projection space
     */
    public int getNoAttributesMappedToDRs()
    {
        int r = 0;
        for (Integer i : _attIdx_to_drIdx) if (i != null) r++;
        return r;
    }

    /**
     * The method overwrites field values with values stored in the other object.
     * Important note: the method preserves the references! The method is used only
     * when working asynchronously on an object's copy. After some processes are done, they may wish
     * to overwrite the updated values. Hence, it is also assumed that this object's structure is
     * similar to the input manager's structure (e.g., the number of display ranges is the same, and the possible nulls
     * occur on the same indexes).
     *
     * @param drm source for new values
     */
    public void overwriteWithValuesStoredIn(DisplayRangesManager drm)
    {
        if (_attIdx_to_drIdx != null)
            System.arraycopy(drm._attIdx_to_drIdx, 0, _attIdx_to_drIdx, 0, _attIdx_to_drIdx.length);
        if (_drIdx_to_attIdx != null)
            System.arraycopy(drm._drIdx_to_attIdx, 0, _drIdx_to_attIdx, 0, _drIdx_to_attIdx.length);

        if (_DR != null)
        {
            for (int i = 0; i < _DR.length; i++)
            {
                if (_DR[i] != null)
                {
                    if (_DR[i]._R != null) _DR[i]._R.setValues(drm._DR[i]._R.getLeft(), drm._DR[i]._R.getRight());
                    else _DR[i]._R = drm._DR[i]._R;

                    if (_DR[i]._normalizer == null) _DR[i]._normalizer = drm._DR[i]._normalizer;
                    else _DR[i]._normalizer.parameterizeAsIn(drm._DR[i]._normalizer);
                }
            }
        }
    }

    /**
     * Can be used to create a series of display ranges with specified properties.
     *
     * @param n                 the number of display ranges to generate
     * @param R                 ranges (the same for all dimensions, the object will be cloned; can also be null when the range is to be updated dynamically)
     * @param updateDynamically if true, display range is dynamically updated when anew data set is examined (given the linked dimension)
     * @param updateFromScratch if true and the {@link DisplayRange#_updateDynamically} flag is true, the display range is updated considering also the past data. If false, the display range is recalculated from scratch.=
     * @return n display ranges
     */
    public static DisplayRange[] getDisplayRanges(int n, Range R, boolean updateDynamically, boolean updateFromScratch)
    {
        DisplayRange[] drs = new DisplayRange[n];
        for (int i = 0; i < n; i++)
        {
            Range r = null;
            if (R != null) r = R.getClone();
            drs[i] = new DisplayRange(r, updateDynamically, updateFromScratch);
        }
        return drs;
    }

    /**
     * Constructs a deep copy.
     *
     * @return deep copy
     */
    public DisplayRangesManager getClone()
    {
        DisplayRangesManager DRM = new DisplayRangesManager();
        DRM._DR = new DisplayRange[_DR.length];
        for (int i = 0; i < DRM._DR.length; i++)
            if (_DR[i] != null) DRM._DR[i] = _DR[i].getClone();
        DRM._attIdx_to_drIdx = _attIdx_to_drIdx.clone();
        DRM._drIdx_to_attIdx = _drIdx_to_attIdx.clone();
        DRM._drIdx_to_flatAttIdx = _drIdx_to_flatAttIdx.clone();
        return DRM;
    }

    /**
     * Private default constructor.
     */
    private DisplayRangesManager()
    {

    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public DisplayRangesManager(Params p)
    {
        _DR = p._DR;
        _attIdx_to_drIdx = p._attIdx_to_drIdx;
        if (_attIdx_to_drIdx == null) // default 1:1 mapping
        {
            _attIdx_to_drIdx = new Integer[_DR.length];
            for (int i = 0; i < _attIdx_to_drIdx.length; i++) _attIdx_to_drIdx[i] = i;
        }

        if (p._explicitlyCopyMappingFromAttToDr)
        {
            _drIdx_to_attIdx = new int[_attIdx_to_drIdx.length];
            _drIdx_to_flatAttIdx = new int[_attIdx_to_drIdx.length];
            for (int i = 0; i < _attIdx_to_drIdx.length; i++)
            {
                _drIdx_to_attIdx[i] = _attIdx_to_drIdx[i];
                _drIdx_to_flatAttIdx[i] = _attIdx_to_drIdx[i];
            }
        }
        else
        {
            // fill dr_att and dr_flat_att mapping
            int notNullAtts = 0;
            for (Integer i : _attIdx_to_drIdx) if (i != null) notNullAtts++;

            // check validity of attIdx mapping
            boolean[] test = new boolean[notNullAtts];
            for (Integer i : _attIdx_to_drIdx)
            {
                if ((i != null) && (i >= 0) && (i < notNullAtts)) test[i] = true;
            }


            boolean passed = true;
            for (boolean b : test)
                if (!b)
                {
                    passed = false;
                    break;
                }

            assert passed;

            _drIdx_to_attIdx = new int[notNullAtts];
            _drIdx_to_flatAttIdx = new int[notNullAtts];

            int nullsSkipped = 0;
            for (int i = 0; i < _attIdx_to_drIdx.length; i++)
            {
                if (_attIdx_to_drIdx[i] == null)
                {
                    nullsSkipped++;
                    continue;
                }
                _drIdx_to_attIdx[_attIdx_to_drIdx[i]] = i;
                _drIdx_to_flatAttIdx[_attIdx_to_drIdx[i]] = i - nullsSkipped;
            }
        }
    }

    /**
     * Auxiliary method data allows updating a single display range given a data set represented as a 2D matrix of double.
     * No report on the update is prepared. The method takes into account, e.g., whether the display range can be
     * dynamically updated.
     *
     * @param data                 data for updating a display range
     * @param drID                 display range ID (in-array index)
     * @param skipNegativeInfinity if true, entries that are equal to Double.NEGATIVE_INFINITY are skipped in the analysis
     * @return report on the update (all data is stored as first objects in array-like fields (instead of as imposed by the provided drID))
     */
    public Report updateSingleDisplayRange(double[][] data, int drID, boolean skipNegativeInfinity)
    {
        Report rep = new Report(1);
        rep._displayRangeChanged = new boolean[]{false};
        rep._displayRangesChanged = false;

        if (_DR == null) return rep;
        if (_DR.length <= drID) return rep;
        if (_DR[drID] == null) return rep;

        DisplayRange DR = _DR[drID];
        // copy previous
        if (DR._R != null) rep._previousRanges[0] = DR._R.getClone();

        if (!DR._updateDynamically) return rep;

        // init
        if ((DR._R == null) || (DR._updateFromScratch))
        {
            DR._R = new Range(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, true); // remember to check if invalid at the end
            DR._normalizer.setMinMax(DR._R.getLeft(), DR._R.getRight());
        }

        // update
        for (double[] V : data)
        {
            if (V == null) continue;
            for (double v : V)
            {
                if ((skipNegativeInfinity) && (Double.compare(Double.NEGATIVE_INFINITY, v) == 0)) continue;
                if (Double.compare(v, DR._R.getRight()) > 0) DR._R.setRight(v);
                if (Double.compare(v, DR._R.getLeft()) < 0) DR._R.setLeft(v);
            }
        }

        // optional repair
        if (DR._updateFromScratch && DR._R.isInvalid())
            DR._R = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        else if (DR._R.isInvalid()) DR._R = null;

        if (DR._R != null) DR._normalizer.setMinMax(DR._R.getLeft(), DR._R.getRight());

        // comparison
        if (((DR._R != null) && (rep._previousRanges[0] == null)) ||
                ((DR._R != null) && (!_DR[drID]._R.isEqual(rep._previousRanges[0])))
        )
        {
            rep._displayRangesChanged = true;
            rep._displayRangeChanged[0] = true;
            DR._normalizer.setMinMax(DR._R.getLeft(), DR._R.getRight());
        }

        return rep;
    }

    /**
     * Auxiliary method that checks whether to skip updating display range due to ``skipDisplayRangeUpdateMask''.
     *
     * @param index                      display range index
     * @param skipDisplayRangeUpdateMask auxiliary mask (can be null) that tell whether the update of the i-th display
     *                                   range should be skipped; this mask is supplied along with a data set
     *                                   (data set can individually request not to update some display ranges)
     * @return true = skip updating selected display range; false otherwise
     */
    private boolean skipDueToSkipMask(int index, boolean[] skipDisplayRangeUpdateMask)
    {
        if (skipDisplayRangeUpdateMask == null) return false;
        if (skipDisplayRangeUpdateMask.length <= index) return false;
        return skipDisplayRangeUpdateMask[index];
    }

    /**
     * Can be called to update data on display ranges.
     *
     * @param data                       container-like object storing data points to be inspected
     * @param skipDisplayRangeUpdateMask auxiliary mask (can be null) that tell whether the update of the i-th display
     *                                   range should be skipped; this mask is supplied along with a data set
     *                                   (data set can individually request not to update some display ranges)
     * @return report on the update process
     */
    public Report updateDisplayRanges(Data data, boolean[] skipDisplayRangeUpdateMask)
    {
        Report rep = new Report(_DR.length);

        // create copies
        rep._previousRanges = new Range[_DR.length];
        for (int i = 0; i < _DR.length; i++)
        {
            if (_DR[i] == null) continue;
            if (_DR[i]._R == null) rep._previousRanges[i] = null;
            else rep._previousRanges[i] = _DR[i]._R.getClone();
        }

        // check pre-mature termination (return immediately)
        {
            boolean all = true;
            for (int i = 0; i < _DR.length; i++)
            {
                if (skipDueToSkipMask(i, skipDisplayRangeUpdateMask)) continue;
                if ((_DR[i] != null) && (_DR[i]._updateDynamically))
                {
                    all = false;
                    break;
                }
            }
            if (all) return rep;
        }

        // ``optionally set initial range value''
        for (int i = 0; i < _DR.length; i++)
        {
            if (_DR[i] == null) continue;
            if (skipDueToSkipMask(i, skipDisplayRangeUpdateMask)) continue;
            if (_DR[i]._updateDynamically)
            {
                if ((_DR[i]._updateFromScratch) || (_DR[i]._R == null))
                    _DR[i]._R = new Range(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, true);
                // remember to check if invalid at the end
            }
        }

        int drID;
        double val;

        // iterate over all points (arrays)
        if (data != null)
            for (double[][] dArr : data.getData())
            {
                if (dArr == null) continue;

                // iterate over all input points
                for (double[] d : dArr)
                {
                    if (d == null) continue;

                    // inspect each dimension
                    for (int attID = 0; attID < d.length; attID++)
                    {
                        if (attID >= _attIdx_to_drIdx.length) continue;
                        if (_attIdx_to_drIdx[attID] == null) continue;
                        drID = _attIdx_to_drIdx[attID];

                        if (!_DR[drID]._updateDynamically) continue;
                        if (skipDueToSkipMask(drID, skipDisplayRangeUpdateMask)) continue;

                        // can be checked
                        val = d[attID];
                        if (Double.compare(val, _DR[drID]._R.getRight()) > 0) _DR[drID]._R.setRight(val);
                        if (Double.compare(val, _DR[drID]._R.getLeft()) < 0) _DR[drID]._R.setLeft(val);
                    }
                }
            }

        // optional repair
        for (int i = 0; i < _DR.length; i++)
        {
            if (_DR[i] == null) continue;
            if (skipDueToSkipMask(i, skipDisplayRangeUpdateMask)) continue;

            if ((_DR[i]._updateDynamically) && (_DR[i]._updateFromScratch) && (_DR[i]._R.isInvalid()))
            {
                _DR[i]._R = new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
                _DR[i]._normalizer.setMinMax(_DR[i]._R.getLeft(), _DR[i]._R.getRight());
            }
            else if (_DR[i]._R.isInvalid())
            {
                _DR[i]._R = null;
                _DR[i]._normalizer.setMinMax(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); // default
            }
        }

        // compare with previous ranges
        for (int i = 0; i < _DR.length; i++)
        {
            if (_DR[i] == null) continue;
            if (skipDueToSkipMask(i, skipDisplayRangeUpdateMask)) continue;

            if (((_DR[i]._R != null) && (rep._previousRanges[i] == null)) ||
                    ((_DR[i]._R != null) && (rep._previousRanges[i] != null) && (!_DR[i]._R.isEqual(rep._previousRanges[i])))
            )
            {
                rep._displayRangesChanged = true;
                rep._displayRangeChanged[i] = true;
                _DR[i]._normalizer.setMinMax(_DR[i]._R.getLeft(), _DR[i]._R.getRight());
            }
        }

        return rep;
    }

    /**
     * Auxiliary method returning current display range for x-axis.
     *
     * @return display range for the x-axis
     */
    public DisplayRange getDisplayRangeForXAxis()
    {
        return getDisplayRangeForAxis(AbstractAxis.Type.X);
    }

    /**
     * Auxiliary method returning current display range for y-axis.
     *
     * @return display range for the y-axis
     */
    public DisplayRange getDisplayRangeForYAxis()
    {
        return getDisplayRangeForAxis(AbstractAxis.Type.Y);
    }

    /**
     * Auxiliary method returning current display range for z-axis.
     *
     * @return display range for the z-axis
     */
    public DisplayRange getDisplayRangeForZAxis()
    {
        return getDisplayRangeForAxis(AbstractAxis.Type.Z);
    }


    /**
     * Auxiliary method returning current display range for a specified axis.
     *
     * @param type axis type (id)
     * @return display range
     */
    public DisplayRange getDisplayRangeForAxis(AbstractAxis.Type type)
    {
        if (_attIdx_to_drIdx == null) return null;
        Integer attID = AbstractAxis.getDefaultIDFromType(type);
        if (attID == null) return null;
        return getDisplayRangeForAttribute(attID);
    }

    /**
     * Auxiliary method returning current display range given the input attribute index.
     *
     * @param attID attribute ID
     * @return display range
     */
    public DisplayRange getDisplayRangeForAttribute(int attID)
    {
        if (attID >= _attIdx_to_drIdx.length) return null;
        if (_attIdx_to_drIdx[attID] == null) return null;
        int drID = _attIdx_to_drIdx[attID];
        if (_DR == null) return null;
        if (_DR.length <= drID) return null;
        return _DR[drID];
    }

    /**
     * Auxiliary method returning current display range given the input display range index.
     *
     * @param drID display range ID
     * @return display range
     */
    public DisplayRange getDisplayRange(int drID)
    {
        if (_DR == null) return null;
        if (drID >= _DR.length) return null;
        return _DR[drID];
    }


    /**
     * Auxiliary method returning the last display range stored.
     *
     * @return display range (null if not possible to retrieve)
     */
    public DisplayRange getLastDisplayRange()
    {
        if (_DR == null) return null;
        if (_DR.length == 0) return null;
        return _DR[_DR.length - 1];
    }


    /**
     * Getter for the additional field for mapping the data point index to display range index (usually 1:1 mapping). Can be null if the attribute does not affect a display range.
     *
     * @return mapping
     */
    public Integer[] get_attIdx_to_drIdx()
    {
        return _attIdx_to_drIdx;
    }

    /**
     * Getter for the additional field for mapping the display range index to the data point index (usually 1:1 mapping).
     *
     * @return mapping
     */
    public int[] get_drIdx_to_attIdx()
    {
        return _drIdx_to_attIdx;
    }

    /**
     * Getter for the additional field for mapping the display range index to the data point index (usually 1:1 mapping).
     * Flat = the mapping is considered as if '_attIdx_to_drIdx' had no null and all entries were shifted to
     * the left.
     *
     * @return mapping
     */
    public int[] get_drIdx_to_flatAttIdx()
    {
        return _drIdx_to_flatAttIdx;
    }

}
