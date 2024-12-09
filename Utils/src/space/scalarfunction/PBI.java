package space.scalarfunction;

import space.Vector;
import space.normalization.INormalization;

/**
 * Implementation of the PBI scalarizing function.
 *
 * @author MTomczyk
 */

public class PBI extends AbstractScalarizingFunction implements IScalarizingFunction
{
    /**
     * Parameterized constructor (normalizations are not used).
     *
     * @param r     reference point (normalized, i.e., components sum to 1)
     * @param theta weight associated with the distance perpendicular to the reference line
     *              (i.e., the greater the theta, the greater the pressure towards the distribution, instead of convergence)
     */
    public PBI(double[] r, double theta)
    {
        this(r, theta, null);
    }


    /**
     * Parameterized constructor.
     *
     * @param r              reference point (normalized, i.e., components sum to 1)
     * @param theta          weight associated with the distance perpendicular to the reference line
     *                       (i.e., the greater the theta, the greater the pressure towards the distribution, instead of convergence)
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public PBI(double[] r, double theta, INormalization[] normalizations)
    {
        super(r, theta, normalizations);
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
        double [] ne = new double[e.length];
        for (int i = 0; i < ne.length; i++) ne[i] = getNormalized(e, i);

        double [] p  = Vector.getPointLineOrthogonalProjection(ne, _w); // projection point
        double d1 = Vector.getLength(p); // distance to origin
        for (int i = 0; i < ne.length; i++) ne[i] = p[i] - ne[i];
        double d2 = Vector.getLength(ne); // distance between the evaluation and the projection
        return d1 + _a * d2;
    }


    /**
     * Params setter. It is assumed that the first element of the 2D input array is the new reference point, while the second
     * element is the new theta (i.e., [reference point, [theta]]).
     *
     * @param p params to be set
     */
    @Override
    public void setParams(double[][] p)
    {
        _w = p[0];
        _a = p[1][0];
    }

    /**
     * Params getter. It returns the reference point and the theta value stored in a 2D matrix
     * of the following form [reference point, [theta]]
     */
    @Override
    public double[][] getParams()
    {
        return new double[][]{_w.clone(), {_a}};
    }

    /**
     * Returns the string representation
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return  "PBI with theta = " + getAuxParam();
    }
}
