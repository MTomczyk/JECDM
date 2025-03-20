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
     * (line color is black, the sphere radius is 1.0).
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
     * Returns concave and spherical 2D Pareto front (data set) illustrated as a line. This method constructs M-element
     * data points initialized with 0-values. The PF's x and y coordinate values are stored at the specified data points'
     * indices.
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
        return DSFactory2D.getDS("Pareto front", data, new LineStyle(lw, color));
    }

    /**
     * Returns concave and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using the parametric equation of a sphere. The two angles used for these equations are obtained
     * via discretization of [0;90] angles. Sets the radius of the sphere to 1.
     *
     * @param disc discretization level per dimension (total number of points equals disc squared)
     * @param ms   marker style
     * @return data set
     */
    public static IDataSet getConcaveSpherical3DPF(int disc, MarkerStyle ms)
    {
        return getConcaveSpherical3DPF(1.0d, disc, ms);
    }

    /**
     * Returns concave and spherical 3D Pareto front (data set) illustrated as a series of markers. The reference points
     * are calculated using Das and Dennis' method to get 3D weight vectors first. Then, these points (vectors) are
     * recalled to match the sphere length.
     *
     * @param rad radius of the sphere
     * @param p   the number of cuts for the Das and Dennis' method
     * @param ms  marker style
     * @return data set
     */
    public static IDataSet getConcaveSpherical3DPF(double rad, int p, MarkerStyle ms)
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

        return DSFactory3D.getDS("Pareto front", data, ms);
    }
}
