package emo.utils.decomposition.similarity;

import emo.utils.decomposition.goal.IGoal;
import space.Vector;

/**
 * Provides default implementation for the cosine similarity.
 *
 * @author MTomczyk
 */
public abstract class AbstractCos implements ISimilarity
{
    /**
     * Quantifies a similarity between two goals as a cosine distance between their two weight vectors (the less, the closer).
     * It is assumed that the input vectors are on a normalized simplex hyperplane.
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
     * Used to determine is less/more means closer/further.
     *
     * @return true, if smaller values mean closer; false otherwise.
     */
    @Override
    public boolean isLessMeaningCloser()
    {
        return false;
    }
}
