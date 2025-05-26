package random;

import sort.InsertionSortDouble;

/**
 * Auxiliary class aiding in generating random weight vectors.
 */
public class WeightsGenerator
{
    /**
     * Generates normalized weight vectors (components are from [0, 1] bound and sum to 1.0). The vectors are drawn
     * randomly from a uniform distribution. The procedure generates n - 1 auxiliary numbers from [0, 1] intervals
     * randomly, denoted by x_i, and sorts them along with 0 and 1 numbers. Then, the weights w_i are determines as
     * x_i - x_(i + 1). The complexity of this procedure is O(m log m).
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

    /**
     * Generates normalized weight vectors (components are from [0, 1] bound and sum to 1.0). The vectors are drawn
     * randomly from a uniform distribution. This method is based on sampling from the Dirichlet distribution.
     * For larger m, it exhibits better performance than {@link WeightsGenerator#getNormalizedWeightVector(int, IRandom)}.
     *
     * @param m vector dimensionality
     * @param R random number generator
     * @return normalized weight vectors
     */
    public static double[] getNormalizedWeightVectorFromDirichlet(int m, IRandom R)
    {
        double sum = 0.0d;
        double[] d = new double[m];
        for (int i = 0; i < m; i++)
        {
            d[i] = -Math.log(R.nextDouble());
            sum += d[i];
        }
        for (int i = 0; i < m; i++)
            d[i] = d[i] / sum;
        return d;
    }
}
