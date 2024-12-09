package emo.utils.decomposition.similarity;

import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.LNorm;

/**
 * Provides default implementation for the Euclidean similarity.
 *
 * @author MTomczyk
 */
public abstract class AbstractEuclidean implements ISimilarity
{
    /**
     * Auxiliary object for calculating the Euclidean distance (no weights and normalizations are used),
     * it is assumed that the input weight vectors for the comparison are already on the normalized simplex hyperplane.
     */
    protected final space.scalarfunction.LNorm _LN = new space.scalarfunction.LNorm(null, 2.0d, null);

    /**
     * Quantifies a similarity between two L-norms as a Euclidean distance between their two weight vectors (the less, the better).
     * It is assumed that the input goals are L-norms ({@link LNorm}) and their vectors are on a simplex hyperplane.
     *
     * @param A the first L-norm
     * @param B the second L-norm
     * @return Euclidean distance between weight vectors of two L-norms
     */
    @Override
    public double calculateSimilarity(IGoal A, IGoal B)
    {
        double[] w1 = A.getParams()[0];
        double[] w2 = B.getParams()[0];
        double[] dw = new double[w1.length]; // calculate the delta vector.
        for (int i = 0; i < w1.length; i++) dw[i] = w1[i] - w2[i];
        return _LN.evaluate(dw); // calculate the Euclidean norm
    }

    /**
     * Used to determine preference direction.
     * @return true -> smaller values are preferred; false otherwise.
     */
    @Override
    public boolean isLessPreferred()
    {
        return true;
    }
}
