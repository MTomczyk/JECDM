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
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms)
    {
        return getDS(name, (double[][]) null, ms, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls)
    {
        return getDS(name, (double[][]) null, null, ls);
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
    public static DataSet getReferenceDS(String name, MarkerStyle ms, LineStyle ls)
    {
        return getDS(name, (double[][]) null, ms, ls);
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
    public static DataSet getReferenceDS(String name, LineStyle ls, ArrowStyles as)
    {
        return getReferenceDS(name, ls, as, false);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, LineStyle ls, ArrowStyles as, boolean treatContiguousLinesAsBroken)
    {
        return getReferenceDS(name, null, ls, as, treatContiguousLinesAsBroken);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name                         data set name
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
     * @param treatContiguousLinesAsBroken if true, the default interpretation of raw data is changed. Instead of treating
     *                                     each double [][] data segment as one contiguous line (when using a line style),
     *                                     the data is considered to be a series of independent lines whose coordinates
     *                                     occupy each subsequent pair of double [] vectors in the data segment
     * @return parameterized data set object
     */
    public static DataSet getReferenceDS(String name, MarkerStyle ms, LineStyle ls, ArrowStyles as, boolean treatContiguousLinesAsBroken)
    {
        return getDS(name, (double[][]) null, ms, ls, as, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms)
    {
        return getDS(name, data, ms, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, MarkerStyle ms)
    {
        return getDS(name, data, ms, null);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, LineStyle ls)
    {
        return getDS(name, data, null, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, LinkedList<double[][]> data, LineStyle ls)
    {
        return getDS(name, data, null, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @param ls   line style (either ms or ls can be null)
     * @param as   arrow styles (used only when ls is provided)
     * @return parameterized data set object
     */
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms, LineStyle ls, ArrowStyles as)
    {
        return getDS(name, data, ms, ls, as, false, 0.005f);
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
    public static DataSet getDS(String name, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        return getDS(name, data, ms, ls, false);
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
    public static DataSet getDS(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        return getDS(name, data, ms, ls, false);
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
        return getDS(name, data, ms, ls, null, treatContiguousLinesAsBroken, 0.005f);
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
        return getDS(name, data, ms, ls, null, false, gradientLineMinSegmentLength);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
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
        return getDS(name, data, null, ls, as, treatContiguousLinesAsBroken, 0.005f);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
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
        return new DataSet(name, data, new Painter2D(ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength));
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
        return getDS(name, data, ms, ls, null, treatContiguousLinesAsBroken, 0.005f);
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
        return getDS(name, data, ms, ls, null, false, gradientLineMinSegmentLength);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
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
        return getDS(name, data, null, ls, as, treatContiguousLinesAsBroken, 0.005f);
    }


    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name                         data set name
     * @param data                         input data point
     * @param ms                           marker style (either ms or ls can be null)
     * @param ls                           line style (either ms or ls can be null)
     * @param as                           arrow styles (used only when ls is provided)
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
        return new DataSet(name, data, new Painter2D(ms, ls, as, treatContiguousLinesAsBroken, gradientLineMinSegmentLength));
    }


    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or [x-coordinate,
     * y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This constructor does
     * not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may be treated as a
     * reference for {@link updater.DataUpdater}.
     *
     * @param name          data set name
     * @param ms            marker style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name, MarkerStyle ms, Color envelopeColor)
    {
        return getReferenceDSForConvergencePlot(name, ms, null, envelopeColor);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot (no envelope used).
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name, LineStyle ls)
    {
        return getReferenceDSForConvergencePlot(name, null, ls, null);
    }

    /**
     * Builder for a data set that should be represented as a convergence plot.
     * It is assumed that input data points are two or four-element tuples: [x-coordinate, y-coordinate] or
     * [x-coordinate, y-coordinate, y + deviation; y - deviation]. The deviation can represent, e.g., some error. This
     * constructor does not allow providing data to be depicted. Hence, it creates an ''empty'' data set object that may
     * be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name          data set name
     * @param ls            line style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForConvergencePlot(String name, LineStyle ls, Color envelopeColor)
    {
        return getReferenceDSForConvergencePlot(name, null, ls, envelopeColor);
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
    public static DataSet getReferenceDSForConvergencePlot(String name, MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        return new DataSet(name, (double[][]) null, new ConvergencePlotPainter2D(ms, ls, envelopeColor));
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
     * @param ls            line style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name, double[][] data, LineStyle ls, Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, null, ls, envelopeColor);
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
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name, double[][] data, MarkerStyle ms, Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ms, null, envelopeColor);
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
    public static DataSet getDSForConvergencePlot(String name, double[][] data, MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        return new DataSet(name, data, new ConvergencePlotPainter2D(ms, ls, envelopeColor));
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
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name, LinkedList<double[][]> data, MarkerStyle ms, Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, ms, null, envelopeColor);
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
     * @param ls            line style (either ms or ls can be null)
     * @param envelopeColor envelope color (transparency is supported; not used when the input consists of [X,Y] pairs)
     * @return parameterized data set object
     */
    public static DataSet getDSForConvergencePlot(String name, LinkedList<double[][]> data, LineStyle ls, Color envelopeColor)
    {
        return getDSForConvergencePlot(name, data, null, ls, envelopeColor);
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
    public static DataSet getDSForConvergencePlot(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        return new DataSet(name, data, new ConvergencePlotPainter2D(ms, ls, envelopeColor));
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
     * @param dimensions the number of dimensions (parallel y-axes)
     * @param ms         marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name, int dimensions, MarkerStyle ms)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, ms, null);
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
     * @param dimensions the number of dimensions (parallel y-axes)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name, int dimensions, LineStyle ls)
    {
        return getReferenceDSForParallelCoordinatePlot(name, dimensions, null, ls);
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
     * @param dimensions the number of dimensions (parallel y-axes)
     * @param ms         marker style (either ms or ls can be null)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getReferenceDSForParallelCoordinatePlot(String name, int dimensions, MarkerStyle ms, LineStyle ls)
    {
        return new DataSet(name, (double[][]) null, new PCPPainter2D(ms, ls, dimensions));
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel y-axes)
     * @param data       input data point
     * @param ms         marker style (either ms or ls can be null)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDSForParallelCoordinatePlot(String name, int dimensions, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        return new DataSet(name, data, new PCPPainter2D(ms, ls, dimensions));
    }

    /**
     * Builder for a data set that should be represented as a parallel coordinate plot.
     * It is assumed that input data points are M-element tuples, where M is the number of dimensions/attributes/etc.
     * considered. Exception: the PCP allows using custom display ranges, but they must be positioned explicitly between
     * the first M display ranges associated with the parallel coordinate lines and the last display range that is
     * reserved for the X-axis. Consequently, the M-element tuple can be extended by additional (custom) elements.
     *
     * @param name       data set name
     * @param dimensions the number of dimensions (parallel y-axes)
     * @param data       input data point
     * @param ms         marker style (either ms or ls can be null)
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getDSForParallelCoordinatePlot(String name, int dimensions, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        return new DataSet(name, data, new PCPPainter2D(ms, ls, dimensions));
    }

}
