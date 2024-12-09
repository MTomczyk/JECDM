package space.scalarfunction;

import space.Vector;
import space.normalization.INormalization;

/**
 * Implementation of the point-line distance function (orthogonal projection).
 *
 * @author MTomczyk
 */

public class PointLineProjection extends AbstractScalarizingFunction implements IScalarizingFunction
{
    /**
     * Parameterized constructor (normalizations are not used).
     *
     * @param r reference point (normalized, i.e., components sum to 1)
     */
    public PointLineProjection(double[] r)
    {
        this(r, null);
    }


    /**
     * Parameterized constructor.
     *
     * @param r              reference point (normalized, i.e., components sum to 1)
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public PointLineProjection(double[] r, INormalization[] normalizations)
    {
        super(r, 0.0d, normalizations);
    }

    /**
     * Can be called to get a scalarizing value from an evaluation vector.
     *
     * @param e evaluation vector
     * @return final scalarizing score
     */
    @Override
    public double evaluate(double[] e)
    {
        double[] ne = new double[e.length];
        for (int i = 0; i < ne.length; i++) ne[i] = getNormalized(e, i);
        double[] p = Vector.getPointLineOrthogonalProjection(ne, _w); // projection point
        for (int i = 0; i < ne.length; i++) ne[i] = p[i] - ne[i];
        return Vector.getLength(ne);
    }


    /**
     * Params setter. It is assumed that the only element of the 2D input array is the new reference point
     * (i.e., [reference point]).
     *
     * @param p params to be set
     */
    @Override
    public void setParams(double[][] p)
    {
        _w = p[0];
    }

    /**
     * Params getter. It returns the reference point wrapped by a vector to satisfy the interface requirements.
     */
    @Override
    public double[][] getParams()
    {
        return new double[][]{_w.clone()};
    }


    /**
     * Returns the string representation
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "PLP";
    }
}
