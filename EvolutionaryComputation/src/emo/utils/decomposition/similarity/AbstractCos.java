package emo.utils.decomposition.similarity;

import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.LNorm;
import space.Vector;

/**
 * Provides default implementation for the cosine similarity.
 *
 * @author MTomczyk
 */
public abstract class AbstractCos implements ISimilarity
{
    /**
     * Quantifies a similarity between two L-norms as a cosine distance between their two weight vectors (the less, the better).
     * It is assumed that the input goals are L-norms ({@link LNorm}) and their vectors are on a simplex hyperplane.
     *
     * @param A the first L-norm
     * @param B the second L-norm
     * @return cosine distance between weight vectors of two L-norms
     */
    @Override
    public double calculateSimilarity(IGoal A, IGoal B)
    {
        double[] w1 = A.getParams()[0];
        double[] w2 = B.getParams()[0];
        return Vector.getCosineSimilarity(w1, w2);
    }

    /**
     * Used to determine preference direction (i.e., whether the smaller or bigger values are preferred).
     * @return true -> smaller values are preferred; false otherwise.
     */
    @Override
    public boolean isLessPreferred()
    {
        return false;
    }
}
