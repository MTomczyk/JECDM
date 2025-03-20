package drmanager;

import space.Range;

/**
 * Provides various means for quickly instantiating {@link DisplayRangesManager.Params} objects.
 *
 * @author MTomczyk
 */
public class DRMPFactory
{
    // =================================================================================================================
    // REGULAR PLOTS
    // =================================================================================================================

    /**
     * Params getter. Sets display ranges to null and allows them to be updated dynamically.
     *
     * @return params container
     */
    public static DisplayRangesManager.Params getFor2D()
    {
        return getFor2D(null, true, false, null, true, false);
    }


    /**
     * Params getter. Sets display ranges as fixed at [0, limits]
     *
     * @param xLimit limit for the X-axis
     * @param yLimit limit for the Y-axis
     * @return params container
     */
    public static DisplayRangesManager.Params getFor2D(double xLimit, double yLimit)
    {
        //noinspection SuspiciousNameCombination
        return getFor2D(Range.get0R(xLimit), false, false,
                Range.get0R(yLimit), false, false);
    }

    /**
     * Params getter. Sets display ranges as provided and prohibits their update.
     *
     * @param xDisplayRange initial display range for X-axis
     * @param yDisplayRange initial display range for Y-axis
     * @return params container
     */
    public static DisplayRangesManager.Params getFor2D(Range xDisplayRange, Range yDisplayRange)
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
    public static DisplayRangesManager.Params getFor2D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                                       Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch)
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[2];
        p._DR[0] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
        p._DR[1] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
        p._attIdx_to_drIdx = new Integer[]{0, 1};
        return p;
    }


    /**
     * Params getter. Sets display ranges to null and allows them to be updated dynamically.
     *
     * @return params container
     */
    public static DisplayRangesManager.Params getFor3D()
    {
        return getFor3D(null, true, false, null,
                true, false, null, true, false);
    }

    /**
     * Params getter. Sets display ranges as fixed at [0, limits]
     *
     * @param xLimit limit for the X-axis
     * @param yLimit limit for the Y-axis
     * @param zLimit limit for the Z-axis
     * @return params container
     */
    public static DisplayRangesManager.Params getFor3D(double xLimit, double yLimit, double zLimit)
    {
        //noinspection SuspiciousNameCombination
        return getFor3D(Range.get0R(xLimit), false, false,
                Range.get0R(yLimit), false, false,
                Range.get0R(zLimit), false, false);
    }


    /**
     * Params getter. Sets display ranges as provided and prohibits their update.
     *
     * @param xDisplayRange initial display range for X-axis
     * @param yDisplayRange initial display range for Y-axis
     * @param zDisplayRange initial display range for Z-axis
     * @return params container
     */
    public static DisplayRangesManager.Params getFor3D(Range xDisplayRange, Range yDisplayRange, Range zDisplayRange)
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
    public static DisplayRangesManager.Params getFor3D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                                       Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch,
                                                       Range zDisplayRange, boolean updateDynamicallyZ, boolean updateZFromScratch)
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[3];
        p._DR[0] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
        p._DR[1] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
        p._DR[2] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(zDisplayRange, updateDynamicallyZ, updateZFromScratch);
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
    public static DisplayRangesManager.Params getFor4D(Range xDisplayRange, Range yDisplayRange, Range zDisplayRange, Range aDisplayRange)
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
    public static DisplayRangesManager.Params getFor4D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                                       Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch,
                                                       Range zDisplayRange, boolean updateDynamicallyZ, boolean updateZFromScratch,
                                                       Range aDisplayRange, boolean updateDynamicallyA, boolean updateAFromScratch)
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[4];
        p._DR[0] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
        p._DR[1] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
        p._DR[2] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(zDisplayRange, updateDynamicallyZ, updateZFromScratch);
        p._DR[3] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(aDisplayRange, updateDynamicallyA, updateAFromScratch);
        p._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};
        return p;
    }

    // =================================================================================================================
    // CONVERGENCE PLOT
    // =================================================================================================================

    /**
     * Params getter for the convergence plot. Sets display ranges to null and allows them to be updated dynamically.
     * The third (upper bound) and the fourth (lower bound) attribute are mapped on the second display range that
     * should be linked to Y-axis.
     *
     * @return params container
     */
    public static DisplayRangesManager.Params getForConvergencePlot2D()
    {
        return getForConvergencePlot2D(null, true, false, null, true, false);
    }

    /**
     * Params getter for the converge plot. Sets display ranges as provided and prohibits their update. The third
     * (upper bound) and the fourth (lower bound) attribute are mapped on the second display range that should be
     * linked to Y-axis.
     *
     * @param xDisplayRange display range for X-axis
     * @param yDisplayRange display range for Y-axis
     * @return params container
     */
    public static DisplayRangesManager.Params getForConvergencePlot2D(Range xDisplayRange, Range yDisplayRange)
    {
        return getForConvergencePlot2D(xDisplayRange, false, false, yDisplayRange, false, false);
    }

    /**
     * Params getter for the converge plot. Sets display ranges as provided. The third (upper bound) and the fourth
     * (lower bound) attribute are mapped on the second display range that should be linked to Y-axis.
     *
     * @param xDisplayRange      initial display range for X-axis
     * @param yDisplayRange      initial display range for Y-axis
     * @param updateDynamicallyY flag indicating whether Y-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
     * @param updateYFromScratch flag indicating whether Y-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
     * @param updateDynamicallyX flag indicating whether X-axis display rage should be updated dynamically (1:1 mapping with Y-axes)
     * @param updateXFromScratch flag indicating whether X-axis display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
     * @return params container
     */
    public static DisplayRangesManager.Params getForConvergencePlot2D(Range xDisplayRange, boolean updateDynamicallyX, boolean updateXFromScratch,
                                                                      Range yDisplayRange, boolean updateDynamicallyY, boolean updateYFromScratch)
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        p._DR = new DisplayRangesManager.DisplayRange[2];
        p._DR[0] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(xDisplayRange, updateDynamicallyX, updateXFromScratch);
        p._DR[1] = DisplayRangesManager.DisplayRange.getParameterizedDisplayRange(yDisplayRange, updateDynamicallyY, updateYFromScratch);
        p._attIdx_to_drIdx = new Integer[]{0, 1, 1, 1};
        p._explicitlyCopyMappingFromAttToDr = true;
        return p;
    }

    // =================================================================================================================
    // PARALLEL COORDINATE PLOT
    // =================================================================================================================

    /**
     * Params getter for the parallel coordinate plot. The ranges are unknown but are allowed to be updated
     * dynamically. The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The
     * additional (last) display range is added automatically and is associated with the X-axis (fixed [0, 1] range).
     *
     * @param dimensions number of dimension  (np. parallel coordinate lines); should be at least 1
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions)
    {
        return getForParallelCoordinatePlot2D(dimensions, null, true, false);
    }

    /**
     * Params getter for the parallel coordinate plot (sets ``update from scratch'' flags to false). Sets display
     * ranges as provided. The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The
     * additional (last) display range is added automatically and is associated with the X-axis (fixed [0, 1] range).
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRange     initial display range (common to each dimension)
     * @param updateDynamically flag indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes); applied to each dimension
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range yDisplayRange, boolean updateDynamically)
    {
        return getForParallelCoordinatePlot2D(dimensions, yDisplayRange, updateDynamically, false);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided. The first "dimensions"
     * display ranges are devoted to Y-axes (parallel coordinates). The additional (last) display range is added
     * automatically and is associated with the X-axis (fixed [0, 1] range).
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRange     initial display range (common to each dimension)
     * @param updateDynamically flag indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes); applied to each dimension
     * @param updateFromScratch flag indicating whether a particular display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes); applied to each dimension
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range yDisplayRange, boolean updateDynamically, boolean updateFromScratch)
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
     * Sets display ranges as provided. The first "dimensions" display ranges are devoted to Y-axes (parallel
     * coordinates). The additional (last) display range is added automatically and is associated with the X-axis
     * (fixed [0, 1] range).
     *
     * @param yLimits limit for the Y-axes (sets the display ranges fixed at [0, yLimit]) intervals
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(double[] yLimits)
    {
        Range[] ranges = new Range[yLimits.length];
        for (int i = 0; i < yLimits.length; i++) ranges[i] = Range.get0R(yLimits[i]);
        return getForParallelCoordinatePlot2D(yLimits.length, ranges, new boolean[yLimits.length]);
    }

    /**
     * Params getter for the parallel coordinate plot (sets ``update from scratch'' flags to false).
     * Sets display ranges as provided. The first "dimensions" display ranges are devoted to Y-axes (parallel
     * coordinates). The additional (last) display range is added automatically and is associated with the X-axis (fixed [0, 1] range).
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRanges    initial display ranges per dimension
     * @param updateDynamically flags indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes)
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range[] yDisplayRanges, boolean[] updateDynamically)
    {
        return getForParallelCoordinatePlot2D(dimensions, yDisplayRanges, updateDynamically, new boolean[updateDynamically.length]);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided. The first "dimensions" display
     * ranges are devoted to Y-axes (parallel coordinates). The additional (last) display range is added automatically and is
     * associated with the X-axis (fixed [0, 1] range).
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRanges    initial display ranges per dimension
     * @param updateDynamically flags indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes)
     * @param updateFromScratch flags indicating whether a particular display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range[] yDisplayRanges,
                                                                             boolean[] updateDynamically, boolean[] updateFromScratch)
    {
        return getForParallelCoordinatePlot2D(dimensions, yDisplayRanges, (DisplayRangesManager.DisplayRange) null,
                null, updateDynamically, updateFromScratch);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided.
     * The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The additional (last)
     * display range is added automatically and is associated with the X-axis (fixed [0, 1] range). This method allows
     * for including one extra display range and attribute mapping (the extra DR is placed between the first "dimensions"
     * DRs and the last one). The "update dynamically" and "update from scratch" policies are set to false.
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRange     initial display range (common to all dimensions)
     * @param extraDR           optional (can be null) extra (custom) display range that will be positioned exactly between
     *                          the first "dimensions" DRs (linked to Y-axes) and the last DR (X-axis)
     * @param extraAttToExtraDR extra "attributes to DR" mapping that can be used along with "extra DR" (can be null);
     *                          will be appended to the regular mapping vector, if used
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions,
                                                                             Range yDisplayRange,
                                                                             DisplayRangesManager.DisplayRange extraDR,
                                                                             Integer extraAttToExtraDR)
    {
        return getForParallelCoordinatePlot2D(dimensions, yDisplayRange, extraDR, extraAttToExtraDR, false);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided.
     * The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The additional (last) display
     * range is automatically added and is associated with the X-axis (fixed [0, 1] range). This method allows for
     * including one extra display range and attribute mapping (the extra DR is placed between the first "dimensions"
     * DRs and the last one). The "update from scratch" policy is set to false.
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRange     initial display range (common to all dimensions)
     * @param extraDR           optional (can be null) extra (custom) display range that will be positioned exactly between
     *                          the first "dimensions" DRs (linked to Y-axes) and the last DR (X-axis)
     * @param extraAttToExtraDR extra "attributes to DR" mapping that can be used along with "extra DR" (can be null);
     *                          will be appended to the regular mapping vector, if used
     * @param updateDynamically flag indicating whether display rages should be updated dynamically (common to all regular DRs)
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range yDisplayRange,
                                                                             DisplayRangesManager.DisplayRange extraDR,
                                                                             Integer extraAttToExtraDR,
                                                                             boolean updateDynamically)
    {
        boolean[] ud = new boolean[dimensions];
        boolean[] ufs = new boolean[dimensions];
        for (int i = 0; i < dimensions; i++)
        {
            ud[i] = updateDynamically;
            ufs[i] = false;
        }
        return getForParallelCoordinatePlot2D(dimensions, yDisplayRange, extraDR, extraAttToExtraDR, ud, ufs);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided.
     * The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The additional (last) display
     * range is added automatically and is associated with the X-axis (fixed [0, 1] range).
     * This method allows for including one extra display range and attribute mapping (the extra DR is placed
     * between the first "dimensions" DRs and the last one).
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRange     initial display range (common to all dimensions)
     * @param extraDR           optional (can be null) extra (custom) display range that will be positioned exactly between
     *                          the first "dimensions" DRs (linked to Y-axes) and the last DR (X-axis)
     * @param extraAttToExtraDR extra "attributes to DR" mapping that can be used along with "extra DR" (can be null);
     *                          will be appended to the regular mapping vector, if used
     * @param updateDynamically flags indicating whether a particular display rage should be updated dynamically (1:1
     *                          mapping with Y-axes)
     * @param updateFromScratch flags indicating whether a particular display rage should be updated from scratch when
     *                          updated dynamically (1:1 mapping with Y-axes)
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range yDisplayRange,
                                                                             DisplayRangesManager.DisplayRange extraDR,
                                                                             Integer extraAttToExtraDR,
                                                                             boolean[] updateDynamically,
                                                                             boolean[] updateFromScratch)
    {
        Range[] ranges = new Range[dimensions];
        for (int i = 0; i < dimensions; i++) ranges[i] = yDisplayRange.getClone();
        return getForParallelCoordinatePlot2D(
                dimensions, ranges,
                extraDR != null ? new DisplayRangesManager.DisplayRange[]{extraDR} : null,
                extraAttToExtraDR != null ? new Integer[]{extraAttToExtraDR} : null,
                updateDynamically, updateFromScratch);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided.
     * The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The additional (last) display
     * range is added automatically and is associated with the X-axis (fixed [0, 1] range). This method allows for
     * including one extra display range and attribute mapping (the extra DR is placed between the first "dimensions"
     * DRs and the last one).
     *
     * @param dimensions        number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRanges    initial display ranges per dimension
     * @param extraDR           optional (can be null) extra (custom) display range that will be positioned exactly between
     *                          the first "dimensions" DRs (linked to Y-axes) and the last DR (X-axis)
     * @param extraAttToExtraDR extra "attributes to DR" mapping that can be used along with "extra DR" (can be null);
     *                          will be appended to the regular mapping vector, if used
     * @param updateDynamically flags indicating whether a particular display rage should be updated dynamically (1:1
     *                          mapping with Y-axes)
     * @param updateFromScratch flags indicating whether a particular display rage should be updated from scratch when
     *                          updated dynamically (1:1 mapping with Y-axes)
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range[] yDisplayRanges,
                                                                             DisplayRangesManager.DisplayRange extraDR,
                                                                             Integer extraAttToExtraDR,
                                                                             boolean[] updateDynamically,
                                                                             boolean[] updateFromScratch)
    {
        return getForParallelCoordinatePlot2D(
                dimensions, yDisplayRanges,
                extraDR != null ? new DisplayRangesManager.DisplayRange[]{extraDR} : null,
                extraAttToExtraDR != null ? new Integer[]{extraAttToExtraDR} : null,
                updateDynamically, updateFromScratch);
    }

    /**
     * Params getter for the parallel coordinate plot. Sets display ranges as provided.
     * The first "dimensions" display ranges are devoted to Y-axes (parallel coordinates). The additional (last) display
     * range is added automatically and is associated with the X-axis (fixed [0, 1] range). This method allows for
     * including extra display ranges and attribute mappings (the extra DRs are placed between the first "dimensions"
     * DRs and the last one).
     *
     * @param dimensions         number of dimension  (np. parallel coordinate lines); should be at least 1
     * @param yDisplayRanges     initial display ranges per dimension
     * @param extraDRs           optional (array can be null) extra (custom) display ranges that will be positioned exactly
     *                           between the first "dimensions" DRs (linked to Y-axes) and the last DR (X-axis)
     * @param extraAttToExtraDRs extra "attributes to DRs" mapping that can be used along with "extra DRs" (array can be null); will be appended to the regular mapping vector, if used
     * @param updateDynamically  flags indicating whether a particular display rage should be updated dynamically (1:1 mapping with Y-axes)
     * @param updateFromScratch  flags indicating whether a particular display rage should be updated from scratch when updated dynamically (1:1 mapping with Y-axes)
     * @return params container
     */
    public static DisplayRangesManager.Params getForParallelCoordinatePlot2D(int dimensions, Range[] yDisplayRanges,
                                                                             DisplayRangesManager.DisplayRange[] extraDRs,
                                                                             Integer[] extraAttToExtraDRs,
                                                                             boolean[] updateDynamically,
                                                                             boolean[] updateFromScratch)
    {
        DisplayRangesManager.Params p = new DisplayRangesManager.Params();
        int noExtraDRs = extraDRs != null ? extraDRs.length : 0;
        int noExtraAttToExtraDRs = extraAttToExtraDRs != null ? extraAttToExtraDRs.length : 0;

        p._DR = new DisplayRangesManager.DisplayRange[dimensions + noExtraDRs + 1];
        p._attIdx_to_drIdx = new Integer[dimensions + noExtraAttToExtraDRs];
        p._explicitlyCopyMappingFromAttToDr = true;

        for (int i = 0; i < dimensions; i++)
        {
            p._DR[i] = new DisplayRangesManager.DisplayRange(yDisplayRanges[i], updateDynamically[i], updateFromScratch[i]);
            p._attIdx_to_drIdx[i] = i;
        }

        if (extraDRs != null)
            System.arraycopy(extraDRs, 0, p._DR, dimensions, extraDRs.length);
        if (extraAttToExtraDRs != null)
            System.arraycopy(extraAttToExtraDRs, 0, p._attIdx_to_drIdx, dimensions, extraAttToExtraDRs.length);

        p._DR[dimensions + noExtraDRs] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false, false);

        return p;
    }

}
