package random;

import sort.InsertionSortDouble;

/**
 * Auxiliary class aiding in generating random weight vectors.
 */
public class WeightsGenerator
{
    /**
     * Generates normalized weight vectors (components sum to 1.0).
     * The vectors are drawn randomly from a uniform distribution.
     *
     * @param m vector dimensionality
     * @param R random number generator
     * @return normalized weight vectors
     */
    public static double[] getNormalizedWeightVector(int m, IRandom R)
    {
        double[] w = new double[m];
        InsertionSortDouble sort = new InsertionSortDouble();
        sort.init(m + 1, true);
        sort.add(0.0d);
        for (int i = 0; i < m - 1; i++) sort.add(R.nextDouble());
        sort.add(1.0d);
        double[] tmp = sort._data;
        double sum = 0.0d;
        for (int i = 1; i < tmp.length - 1; i++)
        {
            w[i - 1] = tmp[i] - tmp[i - 1];
            sum += w[i - 1];
        }
        w[m - 1] = 1.0d - sum; // to minimize the rounding errors.
        return w;
    }
}
