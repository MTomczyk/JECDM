package space.distance;

import space.Vector;
import space.normalization.INormalization;

/**
 * Implementation of the Cos distance function (wraps {@link Vector#getCosineSimilarity(double[], double[])}).
 * If weight vector = null; weights are not used.
 *
 * @author MTomczyk
 */
public class Cos extends AbstractDistanceFunction implements IDistance
{
    /**
     * Parameterized constructor (normalizations and weights are not used).
     */
    public Cos()
    {
        this(null, null);
    }

    /**
     * Parameterized constructor (normalizations are not used).
     *
     * @param w weight vector
     */
    public Cos(double[] w)
    {
        this(w, null);
    }

    /**
     * Parameterized constructor (weights are not used).
     *
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public Cos(INormalization[] normalizations)
    {
        this(null, normalizations);
    }

    /**
     * Parameterized constructor.
     *
     * @param w              weight vector
     * @param normalizations normalizations that can be used to rescale processed points (can be null).
     */
    public Cos(double[] w, INormalization[] normalizations)
    {
        super(null, normalizations);
    }


    /**
     * Can be called to calculate the distance between two vectors (of equal dimensionality).
     * It is assumed that dist(a,b) should equal dist(b,a) (metric space condition).
     *
     * @param a the first vector
     * @param b the second vector
     * @return distance between a and b
     */
    @Override
    public double getDistance(double[] a, double[] b)
    {
        double[] d1 = new double[a.length];
        double[] d2 = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            double na = a[i];
            double nb = b[i];
            if (_normalizations != null)
            {
                na = _normalizations[i].getNormalized(na);
                nb = _normalizations[i].getNormalized(nb);
            }
            d1[i] = na;
            d2[i] = nb;
        }
        return Vector.getCosineSimilarity(d1, d2);
    }
}
