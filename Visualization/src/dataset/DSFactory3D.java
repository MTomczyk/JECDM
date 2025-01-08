package dataset;

import dataset.painter.Painter3D;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;

import java.util.LinkedList;

/**
 * Provides various methods for instantiating data set instances used in 3D visualization ({@link DataSet}).
 *
 * @author MTomczyk
 */
public class DSFactory3D
{
    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms)
    {
        return getDS(name, (double[][]) null, ms, null, true);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls)
    {
        return getDS(name, (double[][]) null, null, ls, true);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms, LineStyle ls)
    {
        return getDS(name, (double[][]) null, ms, ls, true);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ms       marker style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms, boolean useAlpha)
    {
        return getDS(name, (double[][]) null, ms, null, useAlpha);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ls       line style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls, boolean useAlpha)
    {
        return getDS(name, (double[][]) null, null, ls, useAlpha);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ms       marker style (either ms or ls can be null)
     * @param ls       line style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        return getReferenceDS(name, ms, ls, null, useAlpha, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @param as   arrow styles (used only when ls is provided)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls, ArrowStyles as)
    {
        return getReferenceDS(name, ls, as, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ls       line style (either ms or ls can be null)
     * @param as       arrow styles (used only when ls is provided)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls, ArrowStyles as, boolean useAlpha)
    {
        return getReferenceDS(name, null, ls, as, useAlpha, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls, ArrowStyles as, boolean useAlpha, boolean treatContiguousLinesAsBroken)
    {
        return getReferenceDS(name, null, ls, as, useAlpha, treatContiguousLinesAsBroken);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms, LineStyle ls, ArrowStyles as, boolean useAlpha, boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, (double[][]) null, ms, ls, as, useAlpha, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        return getDS(name, data, ms, ls, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        return getDS(name, data, ms, ls, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms)
    {
        return getDS(name, data, ms, null, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, MarkerStyle ms)
    {
        return getDS(name, data, ms, null, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, LineStyle ls)
    {
        return getDS(name, data, null, ls, false);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, LineStyle ls)
    {
        return getDS(name, data, null, ls, false);
    }


    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ms       marker style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms, boolean useAlpha)
    {
        return new DataSet(name, data, new Painter3D(ms, null, useAlpha));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ms       marker style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, MarkerStyle ms, boolean useAlpha)
    {
        return new DataSet(name, data, new Painter3D(ms, null, useAlpha));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ls       line style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, LineStyle ls, boolean useAlpha)
    {
        return new DataSet(name, data, new Painter3D(null, ls, useAlpha));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ls       line style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, LineStyle ls, boolean useAlpha)
    {
        return new DataSet(name, data, new Painter3D(null, ls, useAlpha));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ms       marker style (either ms or ls can be null)
     * @param ls       line style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        return new DataSet(name, data, new Painter3D(ms, ls, useAlpha));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ms       marker style (either ms or ls can be null)
     * @param ls       line style (either ms or ls can be null)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        return new DataSet(name, data, new Painter3D(ms, ls, useAlpha));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, ms, ls, null, useAlpha, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean useAlpha,
                                float gradientLineMinSegmentLength)
    {
        return getDS(name, data, ms, ls, null, useAlpha, false, gradientLineMinSegmentLength);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles  (used only when ls is provided)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, null, ls, as, useAlpha, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength)
    {
        return new DataSet(name, data, new Painter3D(ms, ls, as, useAlpha, treatContiguousLinesAsBroken, gradientLineMinSegmentLength));
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, ms, ls, null, useAlpha, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean useAlpha,
                                float gradientLineMinSegmentLength)
    {
        return getDS(name, data, ms, ls, null, useAlpha, false, gradientLineMinSegmentLength);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, null, ls, as, useAlpha, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength)
    {
        return new DataSet(name, data, new Painter3D(ms, ls, as, useAlpha, treatContiguousLinesAsBroken, gradientLineMinSegmentLength));
    }
}
