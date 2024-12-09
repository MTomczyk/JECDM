package space.simplex;

import java.util.ArrayList;

/**
 * Auxiliary class for constructing weight vectors using the Das and Dennis' method.
 *
 * @author MTomczyk
 */
public class DasDennis
{
    /**
     * Returns normalized weight vectors constructed weight vectors using the Das and Dennis' method.
     *
     * @param M space dimensionality
     * @param p the number of cuts
     * @return normalized weight vectors
     */
    public static ArrayList<double[]> getWeightVectors(int M, int p)
    {
        ArrayList<double[]> result = new ArrayList<>();

        for (int i = 0; i <= p; i++)
        {
            int[] v = new int[M];
            v[0] = i;
            permute(result, v, i, M, p, 1);
        }

        return result;
    }

    /**
     * Auxiliary recursive method permuting indices.
     * @param result all weight vectors generated
     * @param v weight vector being built
     * @param sum sum of indices
     * @param M dimensionality
     * @param p number of cuts
     * @param m dimension-id propagated during recursion
     */
    private static void permute(ArrayList<double[]> result, int[] v, int sum, int M, int p, int m)
    {
        if (m == M - 1)
        {
            v[M - 1] = p - sum;
            double[] r = new double[M];
            for (int i = 0; i < M; i++)
                r[i] = (double) v[i] / (double) p;
            result.add(r);
        }
        else
        {
            for (int i = 0; i <= p - sum; i++)
            {
                int[] v2 = v.clone();
                v2[m] = i;
                permute(result, v2, sum + i, M, p, m + 1);
            }
        }
    }
}
