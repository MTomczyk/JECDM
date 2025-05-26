package dataset;

import color.Color;
import dataset.painter.Painter2D;
import dataset.painter.convergenceplot.ConvergencePlotPainter2D;
import dataset.painter.parallelcoordinateplot.PCPPainter2D;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;

import java.util.LinkedList;

/**
 * Provides various methods for instantiating data set instances used in 2D visualization ({@link DataSet}).
 *
 * @author MTomczyk
 */
public class DSFactory2D
{
    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms)
    {
        return getReferenceDS(name, ms, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ms               marker style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ms, null, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls)
    {
        return getReferenceDS(name, ls, null, null);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, null, ls, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls)
    {
        return getReferenceDS(name, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ms, ls, null, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @param as   arrow styles (used only when ls is provided)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as)
    {
        return getReferenceDS(name, ls, as, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param as               arrow styles (can be null, if not used)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ls, as, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ls                           line style
     * @param as                           arrow styles (can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean treatContiguousLinesAsBroken)
    {
        return getReferenceDS(name, ls, as, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ls                           line style
     * @param as                           arrow styles (can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean treatContiguousLinesAsBroken,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, null, ls, as, treatContiguousLinesAsBroken, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean treatContiguousLinesAsBroken)
    {
        return getReferenceDS(name, ms, ls, as, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean treatContiguousLinesAsBroken,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, (double[][]) null, ms, ls, as, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms)
    {
        return getDS(name, data, ms, (DataSet.IParamsAdjuster) null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, null, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms)
    {
        return getDS(name, data, ms, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, null, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                LineStyle ls)
    {
        return getDS(name, data, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ls               line style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                LineStyle ls,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                LineStyle ls)
    {
        return getDS(name, data, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ls               line style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                LineStyle ls,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @param as   arrow styles (used only when ls is provided; can be null, if not used)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                ArrowStyles as)
    {
        return getDS(name, data, ms, ls, as, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param as               arrow styles (used only when ls is provided; can be null if not used)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                ArrowStyles as,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, as, false, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls)
    {
        return getDS(name, data, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls)
    {
        return getDS(name, data, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
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
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, ms, ls, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
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
                                float gradientLineMinSegmentLength)
    {
        return getDS(name, data, ms, ls, gradientLineMinSegmentLength, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, false, gradientLineMinSegmentLength,
                dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, ls, as, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, as, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength)
    {
        return getDS(name, data, ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength,
                null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(data);
        return getDS(name, d, ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength,
                dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
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
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, ms, ls, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
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
                                float gradientLineMinSegmentLength)
    {
        return getDS(name, data, ms, ls, gradientLineMinSegmentLength, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, false, gradientLineMinSegmentLength,
                dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
                                boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, data, ls, as, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided, can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, as, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength)
    {
        return getDS(name, data, ms, ls, as, treatContiguousLinesAsBroken,
                gradientLineMinSegmentLength, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @param gradientLineMinSegmentLength Determines the minimal segment line used when constructing gradient line
     *                                     (discretization level, the lower the value, the greater the discretization
     *                                     but also computational resources used); the interpretation is
     *                                     implementation-dependent; default: percent value of an average screen
     *                                     dimension (in pixels)
     * @param dsParamsAdjuster             auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the data set object being built
     * @param pParamsAdjuster              auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                                     the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                ArrowStyles as,
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter2D.IParamsAdjuster pParamsAdjuster)
    {
        Painter2D.Params pP = new Painter2D.Params(ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength);
        if (pParamsAdjuster != null) pParamsAdjuster.adjust(pP);
        DataSet.Params pDS = new DataSet.Params(name, new Data(data), new Painter2D(pP));
        if (dsParamsAdjuster != null) dsParamsAdjuster.adjust(pDS);
        return new DataSet(pDS);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or [x-coordinate,
     * y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This constructor does
     * not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may be treated as a
     * reference for {@link updater.DataUpdater}.
     *
     * @param name          data set name
     * @param ms            marker style
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name,
                                                           MarkerStyle ms,
                                                           Color envelopeColor)
    {
        return getReferenceDSForConvergencePlot(name, ms, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or [x-coordinate,
     * y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This constructor does
     * not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may be treated as a
     * reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ms               marker style
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name,
                                                           MarkerStyle ms,
                                                           Color envelopeColor,
                                                           DataSet.IParamsAdjuster dsParamsAdjuster,
                                                           ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDSForConvergencePlot(name, ms, null, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot (no envelope used).
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name, LineStyle ls)
    {
        return getReferenceDSForConvergencePlot(name, ls, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot (no envelope used).
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name, LineStyle ls,
                                                           DataSet.IParamsAdjuster dsParamsAdjuster,
                                                           ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDSForConvergencePlot(name, null, ls, null, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name          data set name
     * @param ls            line style
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name,
                                                           LineStyle ls,
                                                           Color envelopeColor)
    {
        return getReferenceDSForConvergencePlot(name, ls, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name,
                                                           LineStyle ls,
                                                           Color envelopeColor,
                                                           DataSet.IParamsAdjuster dsParamsAdjuster,
                                                           ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDSForConvergencePlot(name, null, ls, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name          data set name
     * @param ms            marker style (either ms or ls can be null)
     * @param ls            line style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name,
                                                           MarkerStyle ms,
                                                           LineStyle ls,
                                                           Color envelopeColor)
    {
        return getReferenceDSForConvergencePlot(name, ms, ls, envelopeColor, null, null);
    }


    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name,
                                                           MarkerStyle ms,
                                                           LineStyle ls,
                                                           Color envelopeColor,
                                                           DataSet.IParamsAdjuster dsParamsAdjuster,
                                                           ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDSForConvergencePlot(name, (LinkedList<double[][]>) null, ms, ls, envelopeColor,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name          data set name
     * @param data          input data point
     * @param ls            line style
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  double[][] data,
                                                  LineStyle ls,
                                                  Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ls, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ls               line style
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  double[][] data,
                                                  LineStyle ls,
                                                  Color envelopeColor,
                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                  ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDSForConvergencePlot(name, data, null, ls, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name          data set name
     * @param data          input data point
     * @param ms            marker style
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  double[][] data,
                                                  MarkerStyle ms,
                                                  Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ms, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  double[][] data,
                                                  MarkerStyle ms,
                                                  Color envelopeColor,
                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                  ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDSForConvergencePlot(name, data, ms, null, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name          data set name
     * @param data          input data point
     * @param ms            marker style (either ms or ls can be null)
     * @param ls            line style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  double[][] data,
                                                  MarkerStyle ms,
                                                  LineStyle ls,
                                                  Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ms, ls, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  double[][] data,
                                                  MarkerStyle ms,
                                                  LineStyle ls,
                                                  Color envelopeColor,
                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                  ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(data);
        return getDSForConvergencePlot(name, d, ms, ls, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name          data set name
     * @param data          input data point
     * @param ms            marker style
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  LinkedList<double[][]> data,
                                                  MarkerStyle ms,
                                                  Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ms, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  LinkedList<double[][]> data,
                                                  MarkerStyle ms,
                                                  Color envelopeColor,
                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                  ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDSForConvergencePlot(name, data, ms, null, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name          data set name
     * @param data          input data point
     * @param ls            line style
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  LinkedList<double[][]> data,
                                                  LineStyle ls,
                                                  Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ls, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ls               line style
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  LinkedList<double[][]> data,
                                                  LineStyle ls,
                                                  Color envelopeColor,
                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                  ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDSForConvergencePlot(name, data, null, ls, envelopeColor, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name          data set name
     * @param data          input data point
     * @param ms            marker style (either ms or ls can be null)
     * @param ls            line style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  LinkedList<double[][]> data,
                                                  MarkerStyle ms,
                                                  LineStyle ls,
                                                  Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ms, ls, envelopeColor, null, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error.
     * Important note: the X-axis is considered a timeline. Therefore, the first attribute values of the input data
     * points should be monotonically increasing.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param envelopeColor    envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name,
                                                  LinkedList<double[][]> data,
                                                  MarkerStyle ms,
                                                  LineStyle ls,
                                                  Color envelopeColor,
                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                  ConvergencePlotPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        ConvergencePlotPainter2D.Params pP = new ConvergencePlotPainter2D.Params(ms, ls, envelopeColor);
        if (pParamsAdjuster != null) pParamsAdjuster.adjust(pP);
        DataSet.Params pDS = new DataSet.Params(name, new Data(data), new ConvergencePlotPainter2D(pP));
        if (dsParamsAdjuster != null) dsParamsAdjuster.adjust(pDS);
        return new DataSet(pDS);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set
     * object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel Y-axes)
     * @param ms         marker style
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name,
                                                                  int dimensions,
                                                                  MarkerStyle ms)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, ms, null, null, null);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set
     * object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param dimensions       the number of dimensions (parallel Y-axes)
     * @param ms               marker style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name,
                                                                  int dimensions,
                                                                  MarkerStyle ms,
                                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                                  PCPPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, ms, null, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set
     * object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel Y-axes)
     * @param ls         line style
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name, int dimensions, LineStyle ls)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, null, ls, null, null);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set
     * object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param dimensions       the number of dimensions (parallel Y-axes)
     * @param ls               line style
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name,
                                                                  int dimensions,
                                                                  LineStyle ls,
                                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                                  PCPPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, null, ls, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     * This constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set
     * object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel Y-axes)
     * @param ms         marker style (either ms or ls can be null)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name,
                                                                  int dimensions,
                                                                  MarkerStyle ms,
                                                                  LineStyle ls)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     * This constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set
     * object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param dimensions       the number of dimensions (parallel Y-axes)
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name,
                                                                  int dimensions,
                                                                  MarkerStyle ms,
                                                                  LineStyle ls,
                                                                  DataSet.IParamsAdjuster dsParamsAdjuster,
                                                                  PCPPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        return getDSForParallelCoordinatePlot(name, dimensions, (LinkedList<double[][]>) null, ms, ls,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel Y-axes)
     * @param data       input data point
     * @param ms         marker style (either ms or ls can be null)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDSForParallelCoordinatePlot(String name,
                                                         int dimensions,
                                                         double[][] data,
                                                         MarkerStyle ms,
                                                         LineStyle ls)
    {
        return getDSForParallelCoordinatePlot(name, dimensions, data, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     *
     * @param name             data set name
     * @param dimensions       the number of dimensions (parallel Y-axes)
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForParallelCoordinatePlot(String name,
                                                         int dimensions,
                                                         double[][] data,
                                                         MarkerStyle ms,
                                                         LineStyle ls,
                                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                                         PCPPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(data);
        return getDSForParallelCoordinatePlot(name, dimensions, d, ms, ls, dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel Y-axes)
     * @param data       input data point
     * @param ms         marker style (either ms or ls can be null)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDSForParallelCoordinatePlot(String name,
                                                         int dimensions,
                                                         LinkedList<double[][]> data,
                                                         MarkerStyle ms,
                                                         LineStyle ls)
    {
        return getDSForParallelCoordinatePlot(name, dimensions, data, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     *
     * @param name             data set name
     * @param dimensions       the number of dimensions (parallel Y-axes)
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDSForParallelCoordinatePlot(String name,
                                                         int dimensions,
                                                         LinkedList<double[][]> data,
                                                         MarkerStyle ms,
                                                         LineStyle ls,
                                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                                         PCPPainter2D.IParamsAdjuster pParamsAdjuster)
    {
        PCPPainter2D.Params pP = new PCPPainter2D.Params(ms, ls, dimensions);
        if (pParamsAdjuster != null) pParamsAdjuster.adjust(pP);
        DataSet.Params pDS = new DataSet.Params(name, new Data(data), new PCPPainter2D(pP));
        if (dsParamsAdjuster != null) dsParamsAdjuster.adjust(pDS);
        return new DataSet(pDS);
    }
}
