package dataset;

import color.Color;
import dataset.painter.IPainter;
import dataset.painter.Painter2D;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;

import java.util.LinkedList;

/**
 * Basic implementation of {@link AbstractDataSet}.
 *
 * @author MTomczyk
 */
public class DataSet extends AbstractDataSet implements IDataSet
{
    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     * This constructor does not allow providing data to be depicted.
     * Hence, it creates an ''empty'' data set object that may be treated as a reference
     * for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style
     */
    protected DataSet(String name, MarkerStyle ms)
    {
        this(name, (double[][]) null, ms, null);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     * This constructor does not allow providing data to be depicted.
     * Hence, it creates an ''empty'' data set object that may be treated as a reference
     * for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style
     */
    protected DataSet(String name, LineStyle ls)
    {
        this(name, (double[][]) null, null, ls);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     * This constructor does not allow providing data to be depicted.
     * Hence, it creates an ''empty'' data set object that may be treated as a reference
     * for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style
     * @param ls   line style
     */
    protected DataSet(String name, MarkerStyle ms, LineStyle ls)
    {
        this(name, (double[][]) null, ms, ls);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     */
    protected DataSet(String name, double[][] data)
    {
        this(name, data, null, null);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     */
    protected DataSet(String name, LinkedList<double[][]> data)
    {
        this(name, data, null, null);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     */
    protected DataSet(String name, Data data)
    {
        this(name, data, null, null);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ms   marker style used when depicting the data
     */
    protected DataSet(String name, double[][] data, MarkerStyle ms)
    {
        this(name, data, ms, null);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ms   marker style used when depicting the data
     */
    protected DataSet(String name, LinkedList<double[][]> data, MarkerStyle ms)
    {
        this(name, data, ms, null);
    }


    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ms   marker style used when depicting the data
     */
    protected DataSet(String name, Data data, MarkerStyle ms)
    {
        this(name, data, ms, null);
    }


    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ls   line style used when depicting the data
     */
    protected DataSet(String name, double[][] data, LineStyle ls)
    {
        this(name, data, null, ls);
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ls   line style used when depicting the data
     */
    protected DataSet(String name, LinkedList<double[][]> data, LineStyle ls)
    {
        this(name, data, null, ls);
    }


    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ls   line style used when depicting the data
     */
    protected DataSet(String name, Data data, LineStyle ls)
    {
        this(name, data, null, ls);
    }


    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ms   marker style used when depicting the data (either ms or ls can be null)
     * @param ls   line style used when depicting the data (either ms or ls can be null)
     */
    protected DataSet(String name, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        this(name, data, new Painter2D(ms, ls));
    }

    /**
     * Parameterized constructor (for 2D plots; (protected access: it is recommended that painter object creation is handled by
     * the static builders)).
     *
     * @param name data set name
     * @param data data
     * @param ms   marker style used when depicting the data (either ms or ls can be null)
     * @param ls   line style used when depicting the data (either ms or ls can be null)
     */
    protected DataSet(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        this(name, data, new Painter2D(ms, ls));
    }

    /**
     * Parameterized constructor (for 2D plots; protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name data set name
     * @param data data
     * @param ms   marker style used when depicting the data (either ms or ls can be null)
     * @param ls   line style used when depicting the data (either ms or ls can be null)
     */
    protected DataSet(String name, Data data, MarkerStyle ms, LineStyle ls)
    {
        this(name, data, new Painter2D(ms, ls));
    }

    /**
     * Parameterized constructor (protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name    data set name
     * @param data    data
     * @param painter object responsible for rendering
     */
    protected DataSet(String name, double[][] data, IPainter painter)
    {
        super(new Data(data), painter);
        _name = name;
        _legendLabel = name;
        _painter.setData(_data);
        _painter.setDataSet(this);
        _painter.setName("Painter of (" + _name + ")");
    }

    /**
     * Parameterized constructor (protected access: it is recommended that painter object creation is handled by
     * the static builders).
     *
     * @param name    data set name
     * @param data    data
     * @param painter object responsible for rendering
     */
    protected DataSet(String name, LinkedList<double[][]> data, IPainter painter)
    {
        super(new Data(data), painter);
        _name = name;
        _legendLabel = name;
        _painter.setData(_data);
        _painter.setDataSet(this);
        _painter.setName("Painter of (" + _name + ")");
    }

    /**
     * Parameterized constructor (protected access: it is recommended that painter object creation is handled by
     * data set constructor).
     *
     * @param name    data set name
     * @param data    data
     * @param painter object responsible for rendering
     */
    protected DataSet(String name, Data data, IPainter painter)
    {
        super(data, painter);
        _name = name;
        _legendLabel = name;
        _painter.setData(_data);
        _painter.setDataSet(this);
        _painter.setName("Painter of (" + _name + ")");
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor2D(String name, MarkerStyle ms)
    {
        return DSFactory2D.getReferenceDS(name, ms);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor2D(String name, LineStyle ls)
    {
        return DSFactory2D.getReferenceDS(name, ls);
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
    public static DataSet getFor2D(String name, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory2D.getReferenceDS(name, ms, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor2D(String name, double[][] data, MarkerStyle ms)
    {
        return DSFactory2D.getDS(name, data, ms);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor2D(String name, LinkedList<double[][]> data, MarkerStyle ms)
    {
        return DSFactory2D.getDS(name, data, ms);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor2D(String name, double[][] data, LineStyle ls)
    {
        return DSFactory2D.getDS(name, data, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 2D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor2D(String name, LinkedList<double[][]> data, LineStyle ls)
    {
        return DSFactory2D.getDS(name, data, ls);
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
    public static DataSet getFor2D(String name, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory2D.getDS(name, data, ms, ls);
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
    public static DataSet getFor2D(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory2D.getDS(name, data, ms, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor3D(String name, MarkerStyle ms)
    {
        return DSFactory3D.getReferenceDS(name, ms);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot. This constructor does not allow providing data to
     * be depicted. Hence, it creates an ''empty'' data set object that may be treated as a reference for {@link updater.DataUpdater}.
     *
     * @param name data set name
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor3D(String name, LineStyle ls)
    {
        return DSFactory3D.getReferenceDS(name, ls);
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
    public static DataSet getFor3D(String name, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory3D.getReferenceDS(name, ms, ls);
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
    public static DataSet getFor3D(String name, MarkerStyle ms, boolean useAlpha)
    {
        return DSFactory3D.getReferenceDS(name, ms, useAlpha);
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
    public static DataSet getFor3D(String name, LineStyle ls, boolean useAlpha)
    {
        return DSFactory3D.getReferenceDS(name, ls, useAlpha);
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
    public static DataSet getFor3D(String name, MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        return DSFactory3D.getReferenceDS(name, ms, ls, useAlpha);
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
    public static DataSet getFor3D(String name, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory3D.getDS(name, data, ms, ls);
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
    public static DataSet getFor3D(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory3D.getDS(name, data, ms, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor3D(String name, double[][] data, MarkerStyle ms)
    {
        return DSFactory3D.getDS(name, data, ms);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ms   marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor3D(String name, LinkedList<double[][]> data, MarkerStyle ms)
    {
        return DSFactory3D.getDS(name, data, ms);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor3D(String name, double[][] data, LineStyle ls)
    {
        return DSFactory3D.getDS(name, data, ls);
    }

    /**
     * Builder for a data set that should be rendered using a 3D plot.
     *
     * @param name data set name
     * @param data input data point
     * @param ls   line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getFor3D(String name, LinkedList<double[][]> data, LineStyle ls)
    {
        return DSFactory3D.getDS(name, data, ls);
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
    public static DataSet getFor3D(String name, double[][] data, MarkerStyle ms, boolean useAlpha)
    {
        return DSFactory3D.getDS(name, data, ms, useAlpha);
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
    public static DataSet getFor3D(String name, LinkedList<double[][]> data, MarkerStyle ms, boolean useAlpha)
    {
        return DSFactory3D.getDS(name, data, ms, useAlpha);
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
    public static DataSet getFor3D(String name, double[][] data, LineStyle ls, boolean useAlpha)
    {
        return DSFactory3D.getDS(name, data, ls, useAlpha);
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
    public static DataSet getFor3D(String name, LinkedList<double[][]> data, LineStyle ls, boolean useAlpha)
    {
        return DSFactory3D.getDS(name, data, ls, useAlpha);
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
    public static DataSet getFor3D(String name, double[][] data, MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        return DSFactory3D.getDS(name, data, ms, ls, useAlpha);
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
    public static DataSet getFor3D(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls, boolean useAlpha)
    {
        return DSFactory3D.getDS(name, data, ms, ls, useAlpha);
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
    public static DataSet getForConvergencePlot2D(String name, MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        return DSFactory2D.getReferenceDSForConvergencePlot(name, ms, ls, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, MarkerStyle ms, Color envelopeColor)
    {
        return DSFactory2D.getReferenceDSForConvergencePlot(name, ms, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, LineStyle ls)
    {
        return DSFactory2D.getReferenceDSForConvergencePlot(name, ls);
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
    public static DataSet getForConvergencePlot2D(String name, LineStyle ls, Color envelopeColor)
    {
        return DSFactory2D.getReferenceDSForConvergencePlot(name, ls, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, double[][] data, LineStyle ls, Color envelopeColor)
    {
        return DSFactory2D.getDSForConvergencePlot(name, data, ls, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, double[][] data, MarkerStyle ms, Color envelopeColor)
    {
        return DSFactory2D.getDSForConvergencePlot(name, data, ms, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, double[][] data, MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        return DSFactory2D.getDSForConvergencePlot(name, data, ms, ls, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, LinkedList<double[][]> data, MarkerStyle ms, Color envelopeColor)
    {
        return DSFactory2D.getDSForConvergencePlot(name, data, ms, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, LinkedList<double[][]> data, LineStyle ls, Color envelopeColor)
    {
        return DSFactory2D.getDSForConvergencePlot(name, data, ls, envelopeColor);
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
    public static DataSet getForConvergencePlot2D(String name, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls, Color envelopeColor)
    {
        return DSFactory2D.getDSForConvergencePlot(name, data, ms, ls, envelopeColor);
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
    public static DataSet getForParallelCoordinatePlot2D(String name, int dimensions, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory2D.getReferenceDSForParallelCoordinatePlot(name, dimensions, ms, ls);
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
     * @param ms         marker style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getForParallelCoordinatePlot2D(String name, int dimensions, MarkerStyle ms)
    {
        return DSFactory2D.getReferenceDSForParallelCoordinatePlot(name, dimensions, ms);
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
     * @param ls         line style (either ms or ls can be null)
     * @return parameterized data set object
     */
    public static DataSet getForParallelCoordinatePlot2D(String name, int dimensions, LineStyle ls)
    {
        return DSFactory2D.getReferenceDSForParallelCoordinatePlot(name, dimensions, ls);
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
    public static DataSet getForParallelCoordinatePlot2D(String name, int dimensions, double[][] data, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory2D.getDSForParallelCoordinatePlot(name, dimensions, data, ms, ls);
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
    public static DataSet getForParallelCoordinatePlot2D(String name, int dimensions, LinkedList<double[][]> data, MarkerStyle ms, LineStyle ls)
    {
        return DSFactory2D.getDSForParallelCoordinatePlot(name, dimensions, data, ms, ls);
    }

}
