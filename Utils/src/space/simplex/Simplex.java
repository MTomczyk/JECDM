package space.simplex;

import org.apache.commons.math4.legacy.stat.StatUtils;
import space.Vector;

/**
 * This class provides various functionalities related to a normalized simplex plane (vectors whose components
 * are in [0, 1] bounds and sum up to 1.0)
 *
 * @author MTomczyk
 */
public class Simplex
{
    /**
     * Checks if the input vector consists of [0, 1] values that sum up to 1.0.
     *
     * @param v            input vector
     * @param sumTolerance tolerance used when comparing the sum of the components with 1.0 (the abs(sum-1.0) should be smaller equal the tolerance)
     * @return true, if the input vector consists of [0, 1] values that sum up to 1.0; false otherwise (returns false if v is null or is empty)
     */
    public static boolean isOnSimplex(double[] v, double sumTolerance)
    {
        if (v == null) return false;
        if (v.length == 0) return false;
        double sum = 0.0d;
        for (double value : v)
        {
            if (Double.compare(value, 0.0d) < 0) return false;
            if (Double.compare(value, 1.0d) > 0) return false;
            sum += value;
        }
        return Double.compare(Math.abs(sum - 1.0d), sumTolerance) <= 0;
    }

    /**
     * This method returns a min and max weight value that can be used to construct a combination of two vectors:
     * a + (b-a)*weights that preserve the feasibility of the constructed vector (components should be in [0, 1] bounds).
     *
     * @param a a-vector (assumed to be on normalized hyperplane)
     * @param b b-vector (assumed to be on normalized hyperplane)
     * @return [min weight; max weight] (returns null if the input is invalid, or a and b are equal)
     */
    public static double[] getMinMaxCombinationWeights(double[] a, double[] b)
    {

        if (a == null) return null;
        if (b == null) return null;
        if (a.length == 0) return null;
        if (b.length == 0) return null;
        if (a.length != b.length) return null;
        if (Vector.areVectorsEqual(a, b)) return null;
        double[] r = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};

        double[] dv = Vector.getDifference(b, a);
        for (int i = 0; i < a.length; i++)
        {
            if (Double.compare(dv[i], 0.0d) == 0) continue;
            double cmin = -a[i] / dv[i];
            double cmax = (1.0d - a[i]) / dv[i];
            if (Double.compare(dv[i], 0.0d) > 0)
            {
                if (Double.compare(cmin, r[0]) > 0) r[0] = cmin;
                if (Double.compare(cmax, r[1]) < 0) r[1] = cmax;
            }
            else
            {
                if (Double.compare(cmax, r[0]) > 0) r[0] = cmax;
                if (Double.compare(cmin, r[1]) < 0) r[1] = cmin;
            }
        }
        return r;
    }


    /**
     * This method normalizes the input vector by dividing its components by their sum (biases the distribution towards
     * a vector of equal components).
     *
     * @param a input vector
     */
    public static void normalize(double[] a)
    {
        if (a == null) return;
        if (a.length == 0) return;
        double sum = StatUtils.sum(a);
        if (Double.compare(sum, 0.0d) == 0) return;
        for (int i = 0; i < a.length; i++) a[i] /= sum;
    }
}
