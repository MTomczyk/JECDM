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
     * @param ms   marker style
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms)
    {
        return getReferenceDS(name, ms, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
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
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ms, null, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
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
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
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
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, null, ls, false, dsParamsAdjuster, pParamsAdjuster);
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
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls)
    {
        return getReferenceDS(name, ms, ls, null, null);
    }


    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
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
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ms, ls, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ms       marker style
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         boolean useAlpha)
    {
        return getReferenceDS(name, ms, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ms               marker style
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         boolean useAlpha,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ms, null, useAlpha, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ls       line style
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         boolean useAlpha)
    {
        return getReferenceDS(name, ls, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         boolean useAlpha,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, null, ls, useAlpha, dsParamsAdjuster, pParamsAdjuster);
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
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls,
                                         boolean useAlpha)
    {
        return getReferenceDS(name, ms, ls, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         MarkerStyle ms,
                                         LineStyle ls,
                                         boolean useAlpha,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ms, ls, null, useAlpha, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style
     * @param as   arrow styles (used only when ls is provided; can be null, if not used)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as)
    {
        return getReferenceDS(name, ls, as, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param as               arrow styles (used only when ls is provided; can be null, if not used)
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
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, ls, as, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name     data set name
     * @param ls       line style
     * @param as       arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean useAlpha)
    {
        return getReferenceDS(name, ls, as, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name             data set name
     * @param ls               line style
     * @param as               arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean useAlpha,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, null, ls, as, useAlpha, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name,
                                         LineStyle ls,
                                         ArrowStyles as,
                                         boolean useAlpha,
                                         boolean treatContiguousLinesAsBroken)
    {
        return getReferenceDS(name, ls, as, useAlpha, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
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
                                         boolean useAlpha,
                                         boolean treatContiguousLinesAsBroken,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getReferenceDS(name, null, ls, as, useAlpha, treatContiguousLinesAsBroken, dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
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
                                         boolean useAlpha,
                                         boolean treatContiguousLinesAsBroken)
    {
        return getReferenceDS(name, ms, ls, as, useAlpha, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
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
                                         boolean useAlpha,
                                         boolean treatContiguousLinesAsBroken,
                                         DataSet.IParamsAdjuster dsParamsAdjuster,
                                         Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, (double[][]) null, ms, ls, as, useAlpha, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
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
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls)
    {
        return getDS(name, data, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, false, dsParamsAdjuster, pParamsAdjuster);
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
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls)
    {
        return getDS(name, data, ms, ls, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
        return getDS(name, data, ms, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
     * Builder for a data set that should be rendered using a 3D plot.
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
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
     * Builder for a data set that should be rendered using a 3D plot.
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
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ls, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
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
     * Builder for a data set that should be rendered using a 3D plot.
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
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ls, false, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ms       marker style
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                boolean useAlpha)
    {
        return getDS(name, data, ms, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                boolean useAlpha,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, null, useAlpha, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ms       marker style
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                boolean useAlpha)
    {
        return getDS(name, data, ms, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                boolean useAlpha,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, null, useAlpha, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ls       line style
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                LineStyle ls,
                                boolean useAlpha)
    {
        return getDS(name, data, ls, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ls               line style
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                double[][] data,
                                LineStyle ls,
                                boolean useAlpha,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, useAlpha, dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name     data set name
     * @param data     input data point
     * @param ls       line style
     * @param useAlpha if true, the fourth (alpha) channel is used
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                LineStyle ls,
                                boolean useAlpha)
    {
        return getDS(name, data, ls, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ls               line style
     * @param useAlpha         if true, the fourth (alpha) channel is used
     * @param dsParamsAdjuster auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the data set object being built
     * @param pParamsAdjuster  auxiliary adjuster that, when provided (not null), is used adjusts params of
     *                         the painter object being built
     * @return parameterized data set object
     */
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                LineStyle ls,
                                boolean useAlpha,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, useAlpha, dsParamsAdjuster, pParamsAdjuster);
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
    public static DataSet getDS(String name,
                                double[][] data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean useAlpha)
    {
        return getDS(name, data, ms, ls, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param useAlpha         if true, the fourth (alpha) channel is used
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
                                boolean useAlpha,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(data);
        return getDS(name, d, ms, ls, null, useAlpha, false, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
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
    public static DataSet getDS(String name,
                                LinkedList<double[][]> data,
                                MarkerStyle ms,
                                LineStyle ls,
                                boolean useAlpha)
    {
        return getDS(name, data, ms, ls, useAlpha, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name             data set name
     * @param data             input data point
     * @param ms               marker style (either ms or ls can be null)
     * @param ls               line style (either ms or ls can be null)
     * @param useAlpha         if true, the fourth (alpha) channel is used
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
                                boolean useAlpha,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, useAlpha, false,
                0.005f, dsParamsAdjuster, pParamsAdjuster);
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
        return getDS(name, data, ms, ls, useAlpha, treatContiguousLinesAsBroken, null, null);
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
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, useAlpha, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
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
        return getDS(name, data, ms, ls, useAlpha, gradientLineMinSegmentLength, null, null);
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
                                boolean useAlpha,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, useAlpha, false, gradientLineMinSegmentLength,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
        return getDS(name, data, ls, as, useAlpha, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
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
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, as, useAlpha, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
        return getDS(name, data, ms, ls, as, useAlpha, treatContiguousLinesAsBroken, gradientLineMinSegmentLength,
                null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        LinkedList<double[][]> d = new LinkedList<>();
        d.add(data);
        return getDS(name, d, ms, ls, as, useAlpha, treatContiguousLinesAsBroken, gradientLineMinSegmentLength,
                dsParamsAdjuster, pParamsAdjuster);
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
        return getDS(name, data, ms, ls, useAlpha, treatContiguousLinesAsBroken, null, null);
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
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, useAlpha, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
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
        return getDS(name, data, ms, ls, useAlpha, gradientLineMinSegmentLength, null, null);
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
                                boolean useAlpha,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, ms, ls, null, useAlpha, false, gradientLineMinSegmentLength,
                dsParamsAdjuster, pParamsAdjuster);
    }


    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
        return getDS(name, data, ls, as, useAlpha, treatContiguousLinesAsBroken, null, null);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
     * @param useAlpha                     if true, the fourth (alpha) channel is used
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
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        return getDS(name, data, null, ls, as, useAlpha, treatContiguousLinesAsBroken, 0.005f,
                dsParamsAdjuster, pParamsAdjuster);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
        return getDS(name, data, ms, ls, as, useAlpha, treatContiguousLinesAsBroken, gradientLineMinSegmentLength, null, null);
    }


    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided; can be null, if not used)
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
                                boolean useAlpha,
                                boolean treatContiguousLinesAsBroken,
                                float gradientLineMinSegmentLength,
                                DataSet.IParamsAdjuster dsParamsAdjuster,
                                Painter3D.IParamsAdjuster pParamsAdjuster)
    {
        Painter3D.Params pP = new Painter3D.Params(ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength, useAlpha);
        if (pParamsAdjuster != null) pParamsAdjuster.adjust(pP);
        DataSet.Params pDS = new DataSet.Params(name, new Data(data), new Painter3D(pP));
        if (dsParamsAdjuster != null) dsParamsAdjuster.adjust(pDS);
        return new DataSet(pDS);
    }
}
