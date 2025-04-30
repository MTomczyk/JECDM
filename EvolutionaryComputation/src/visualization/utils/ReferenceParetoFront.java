package visualization.utils;

import color.gradient.Color;
import dataset.DSFactory2D;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import space.Vector;
import space.simplex.DasDennis;

/**
 * Provides means of creating simple Pareto front visualizations (data sets {@link dataset.IDataSet}).
 *
 * @author MTomczyk
 */
public class ReferenceParetoFront
{
    /**
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line
     * (line color is black, the sphere radius is 1.0). Sets the data sets' name to "Pareto front".
     *
     * @param lw line width
     * @return data set
     */
    public static IDataSet getConcaveSpherical2DPF(float lw)
    {
        return getConcaveSpherical2DPF(1.0d, lw, Color.BLACK);
    }

    /**
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line.
     * Sets the data sets' name to "Pareto front".
     *
     * @param rad   radius of the sphere
     * @param lw    line width
     * @param color line color
     * @return data set
     */
    public static IDataSet getConcaveSpherical2DPF(double rad, float lw, Color color)
    {
        return getConcaveSpherical2DPF(rad, lw, color, 2, 0, 1);
    }

    /**
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line.
     * Sets the data sets' name to "Pareto front".
     *
     * @param rad radius of the sphere
     * @param ls  line style
     * @return data set
     */
    public static IDataSet getConcaveSpherical2DPF(double rad, LineStyle ls)
    {
        return getConcaveSpherical2DPF("Pareto front", rad, ls, 2, 0, 1);
    }


    /**
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices. Sets the data sets' name to "Pareto front".
     *
     * @param rad   radius of the sphere
     * @param lw    line width
     * @param color line color
     * @param M     data point dimensionality (at least 2)
     * @param xIdx  index in the data point array where the PF's x-coordinate values will be stored
     * @param yIdx  index in the data point array where the PF's y-coordinate values will be stored
     * @return data set
     */
    public static IDataSet getConcaveSpherical2DPF(double rad, float lw, Color color, int M, int xIdx, int yIdx)
    {
        return getConcaveSpherical2DPF("Pareto front", rad, lw, color, M, xIdx, yIdx);
    }

    /**
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices.
     *
     * @param name  Pareto front name
     * @param rad   radius of the sphere
     * @param lw    line width
     * @param color line color
     * @param M     data point dimensionality (at least 2)
     * @param xIdx  index in the data point array where the PF's x-coordinate values will be stored
     * @param yIdx  index in the data point array where the PF's y-coordinate values will be stored
     * @return data set
     */
    public static IDataSet getConcaveSpherical2DPF(String name, double rad, float lw, Color color, int M, int xIdx, int yIdx)
    {
        return getConcaveSpherical2DPF(name, rad, new LineStyle(lw, color), M, xIdx, yIdx);
    }


    /**
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices.
     *
     * @param name Pareto front name
     * @param rad  radius of the sphere
     * @param ls   line style
     * @param M    data point dimensionality (at least 2)
     * @param xIdx index in the data point array where the PF's x-coordinate values will be stored
     * @param yIdx index in the data point array where the PF's y-coordinate values will be stored
     * @return data set
     */
    public static IDataSet getConcaveSpherical2DPF(String name, double rad, LineStyle ls, int M, int xIdx, int yIdx)
    {
        int points = 1000;
        double p2 = Math.PI / 2.0d;
        double da = 1.0d / (points - 1);
        if (M < 2) M = 2;
        if ((xIdx < 0) || (xIdx >= M)) xIdx = 0;
        if ((yIdx < 0) || (yIdx >= M)) yIdx = 0;
        double[][] data = new double[points][M];
        for (int i = 0; i < points; i++)
        {
            double a = da * i;
            data[i][xIdx] = Math.cos(a * p2) * rad;
            data[i][yIdx] = Math.sin(a * p2) * rad;
        }
        return DSFactory2D.getDS(name, data, ls);
    }

    /**
     * Returns concave and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length. Sets the data sets' name to "Pareto front".
     *
     * @param p  the number of cuts for the Das and Dennis' method
     * @param ms marker style
     * @return data set
     */
    public static IDataSet getConcaveSpherical3DPF(int p, MarkerStyle ms)
    {
        return getConcaveSpherical3DPF(1.0d, p, ms);
    }

    /**
     * Returns concave and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length. Sets the data sets' name to "Pareto front".
     *
     * @param rad radius of the sphere
     * @param p   the number of cuts for the Das and Dennis' method
     * @param ms  marker style
     * @return data set
     */
    public static IDataSet getConcaveSpherical3DPF(double rad, int p, MarkerStyle ms)
    {
        return getConcaveSpherical3DPF("Pareto front", rad, p, ms);
    }

    /**
     * Returns concave and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length.
     *
     * @param name data set name
     * @param rad  radius of the sphere
     * @param p    the number of cuts for the Das and Dennis' method
     * @param ms   marker style
     * @return data set
     */
    public static IDataSet getConcaveSpherical3DPF(String name, double rad, int p, MarkerStyle ms)
    {
        if (p < 1) p = 1;
        double[][] w = DasDennis.getWeightVectorsAsPrimitive(3, p);
        if (w == null) return null;
        double[][] data = new double[w.length][3];

        for (int i = 0; i < w.length; i++)
        {
            data[i] = w[i];
            Vector.normalize(data[i]);
            Vector.multiply(data[i], rad);
        }

        return DSFactory3D.getDS(name, data, ms);
    }

    /**
     * Returns convex and spherical 2D Pareto front (data set) illustrated as a line
     * (line color is black, the sphere radius is 1.0). Sets the data sets' name to "Pareto front".
     *
     * @param lw line width
     * @return data set
     */
    public static IDataSet getConvexSpherical2DPF(float lw)
    {
        return getConvexSpherical2DPF(1.0d, lw, Color.BLACK);
    }

    /**
     * Returns convex and spherical 2D Pareto front (data set) illustrated as a line.
     * Sets the data sets' name to "Pareto front".
     *
     * @param rad   radius of the sphere
     * @param lw    line width
     * @param color line color
     * @return data set
     */
    public static IDataSet getConvexSpherical2DPF(double rad, float lw, Color color)
    {
        return getConvexSpherical2DPF(rad, lw, color, 2, 0, 1);
    }

    /**
     * Returns convex and spherical 2D Pareto front (data set) illustrated as a line.
     * Sets the data sets' name to "Pareto front".
     *
     * @param rad radius of the sphere
     * @param ls  line style
     * @return data set
     */
    public static IDataSet getConvexSpherical2DPF(double rad, LineStyle ls)
    {
        return getConvexSpherical2DPF("Pareto front", rad, ls, 2, 0, 1);
    }


    /**
     * Returns convex and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices. Sets the data sets' name to "Pareto front".
     *
     * @param rad   radius of the sphere
     * @param lw    line width
     * @param color line color
     * @param M     data point dimensionality (at least 2)
     * @param xIdx  index in the data point array where the PF's x-coordinate values will be stored
     * @param yIdx  index in the data point array where the PF's y-coordinate values will be stored
     * @return data set
     */
    public static IDataSet getConvexSpherical2DPF(double rad, float lw, Color color, int M, int xIdx, int yIdx)
    {
        return getConvexSpherical2DPF("Pareto front", rad, lw, color, M, xIdx, yIdx);
    }

    /**
     * Returns convex and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices.
     *
     * @param name  Pareto front name
     * @param rad   radius of the sphere
     * @param lw    line width
     * @param color line color
     * @param M     data point dimensionality (at least 2)
     * @param xIdx  index in the data point array where the PF's x-coordinate values will be stored
     * @param yIdx  index in the data point array where the PF's y-coordinate values will be stored
     * @return data set
     */
    public static IDataSet getConvexSpherical2DPF(String name, double rad, float lw, Color color, int M, int xIdx, int yIdx)
    {
        return getConvexSpherical2DPF(name, rad, new LineStyle(lw, color), M, xIdx, yIdx);
    }

    /**
     * Returns convex and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices.
     *
     * @param name Pareto front name
     * @param rad  radius of the sphere
     * @param ls   line style
     * @param M    data point dimensionality (at least 2)
     * @param xIdx index in the data point array where the PF's x-coordinate values will be stored
     * @param yIdx index in the data point array where the PF's y-coordinate values will be stored
     * @return data set
     */
    public static IDataSet getConvexSpherical2DPF(String name, double rad, LineStyle ls, int M, int xIdx, int yIdx)
    {
        IDataSet ds = getConcaveSpherical2DPF(name, rad, ls, M, xIdx, yIdx);
        double[][] data = ds.getData().getData().getFirst();
        for (int i = 0; i < data.length; i++)
        {
            data[i][0] = 1.0d - data[i][0];
            data[i][1] = 1.0d - data[i][1];
        }
        return DSFactory2D.getDS(name, data, ls);
    }

    /**
     * Returns convex and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length. Sets the data sets' name to "Pareto front".
     *
     * @param p  the number of cuts for the Das and Dennis' method
     * @param ms marker style
     * @return data set
     */
    public static IDataSet getConvexSpherical3DPF(int p, MarkerStyle ms)
    {
        return getConvexSpherical3DPF(1.0d, p, ms);
    }

    /**
     * Returns convex and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length. Sets the data sets' name to "Pareto front".
     *
     * @param rad radius of the sphere
     * @param p   the number of cuts for the Das and Dennis' method
     * @param ms  marker style
     * @return data set
     */
    public static IDataSet getConvexSpherical3DPF(double rad, int p, MarkerStyle ms)
    {
        return getConvexSpherical3DPF("Pareto front", rad, p, ms);
    }

    /**
     * Returns convex and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length.
     *
     * @param name data set name
     * @param rad  radius of the sphere
     * @param p    the number of cuts for the Das and Dennis' method
     * @param ms   marker style
     * @return data set
     */
    public static IDataSet getConvexSpherical3DPF(String name, double rad, int p, MarkerStyle ms)
    {
        IDataSet ds = getConcaveSpherical3DPF(name, rad, p, ms);
        if (ds == null) return null;
        double[][] data = ds.getData().getData().getFirst();
        for (int i = 0; i < data.length; i++)
        {
            data[i][0] = 1.0d - data[i][0];
            data[i][1] = 1.0d - data[i][1];
            data[i][2] = 1.0d - data[i][2];
        }

        return DSFactory3D.getDS(name, data, ms);
    }


    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a line.
     * The points are normalized, i.e., sum to 1. Sets the data sets' name to "Pareto front".
     *
     * @param lw    line width
     * @param color line color
     * @return data set
     */
    public static IDataSet getFlat2DPF(float lw, Color color)
    {
        return getFlat2DPF(1.0d, lw, color);
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a line.
     * The points are normalized, i.e., sum to 1. Sets the data sets' name to "Pareto front"..
     *
     * @param scale rescales the normalized Pareto front (normalized points coordinates * scale; default = 1)
     * @param lw    line width
     * @param color line color
     * @return data set
     */
    public static IDataSet getFlat2DPF(double scale, float lw, Color color)
    {
        return getFlat2DPF(scale, new LineStyle(lw, color));
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a line.
     * The points are normalized, i.e., sum to 1. Sets the data sets' name to "Pareto front".
     *
     * @param ls line style
     * @return data set
     */
    public static IDataSet getFlat2DPF(LineStyle ls)
    {
        return getFlat2DPF(1.0d, ls);
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a line.
     * The points are normalized, i.e., sum to 1. Sets the data sets' name to "Pareto front".
     *
     * @param scale rescales the normalized Pareto front (normalized points coordinates * scale; default = 1)
     * @param ls    line style
     * @return data set
     */
    public static IDataSet getFlat2DPF(double scale, LineStyle ls)
    {
        return getFlat2DPF("Pareto front", scale, ls);
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a line.
     * The points are normalized, i.e., sum to 1.
     *
     * @param name  data set name
     * @param scale rescales the normalized Pareto front (normalized points coordinates * scale; default = 1)
     * @param ls    line style
     * @return data set
     */
    public static IDataSet getFlat2DPF(String name, double scale, LineStyle ls)
    {
        return DSFactory2D.getDS(name, new double[][]{{scale, 0.0d}, {0.0d, scale}}, ls);
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a series of markers. The reference points are calculated
     * using Das and Dennis' method. The points are normalized, i.e., sum to 1. Sets the data sets' name to "Pareto front".
     *
     * @param p  the number of cuts for the Das and Dennis' method
     * @param ms marker style
     * @return data set
     */
    public static IDataSet getFlat3DPF(int p, MarkerStyle ms)
    {
        return getFlat3DPF(1.0d, p, ms);
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a series of markers. The reference points are calculated
     * using Das and Dennis' method. The points are normalized, i.e., sum to 1. Sets the data sets' name to "Pareto front".
     *
     * @param scale rescales the normalized Pareto front (normalized points coordinates * scale; default = 1)
     * @param p     the number of cuts for the Das and Dennis' method
     * @param ms    marker style
     * @return data set
     */
    public static IDataSet getFlat3DPF(double scale, int p, MarkerStyle ms)
    {
        return getFlat3DPF("Pareto front", scale, p, ms);
    }

    /**
     * Returns a flat 2D Pareto front (data set) illustrated as a series of markers. The reference points are calculated
     * using Das and Dennis' method. The points are normalized, i.e., sum to 1.
     *
     * @param name  data set name
     * @param scale rescales the normalized Pareto front (normalized points coordinates * scale; default = 1)
     * @param p     the number of cuts for the Das and Dennis' method
     * @param ms    marker style
     * @return data set
     */
    public static IDataSet getFlat3DPF(String name, double scale, int p, MarkerStyle ms)
    {
        if (p < 1) p = 1;
        double[][] w = DasDennis.getWeightVectorsAsPrimitive(3, p);
        if (w == null) return null;
        for (int i = 0; i < w.length; i++) for (int j = 0; j < 3; j++) w[i][j] *= scale;
        return DSFactory3D.getDS(name, w, ms);
    }
}
