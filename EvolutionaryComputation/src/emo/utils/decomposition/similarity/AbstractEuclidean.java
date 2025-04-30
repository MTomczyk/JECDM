package emo.utils.decomposition.similarity;

import emo.utils.decomposition.goal.IGoal;
import space.distance.Euclidean;
import space.distance.IDistance;

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
    protected final IDistance _distance = new Euclidean();

    /**
     * Quantifies a similarity between two goals as a Euclidean distance between their two weight vectors (the less, the closer).
     * It is assumed that the input vectors are on a normalized simplex hyperplane.
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
        return _distance.getDistance(w1, w2);
    }

    /**
     * Used to determine is less/more means closer (more similar)/further.
     *
     * @return true, if smaller values mean closer (more similar); false otherwise.
     */
    @Override
    public boolean isLessMeaningCloser()
    {
        return true;
    }
}
