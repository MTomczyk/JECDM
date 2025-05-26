package model.similarity.lnorm;

import model.similarity.AbstractCosSimilarity;
import model.similarity.ISimilarity;
import model.internals.value.scalarizing.LNorm;

/**
 * Calculates a similarity between two L-norms a Euclidean distance between their weight vectors (on a normalized
 * simplex hyperplane).
 *
 * @author MTomczyk
 */
public class Cos extends AbstractCosSimilarity<LNorm> implements ISimilarity<LNorm>
{
    /**
     * Quantifies similarity between two models.
     *
     * @param A the first model
     * @param B the second model.
     * @return similarity between the models
     */
    @Override
    public double calculateSimilarity(LNorm A, LNorm B)
    {
        double[] w1 = A.getWeights();
        double[] w2 = B.getWeights();
        return _distance.getDistance(w1, w2);
    }
}
