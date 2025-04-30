package model.similarity;

import model.internals.AbstractInternalModel;
import space.distance.Euclidean;
import space.distance.IDistance;

/**
 * Abstract implementation of {@link ISimilarity} quantifying a similarity as a Euclidean distance between its parameters.
 *
 * @author MTomczyk
 */
public class AbstractEuclideanSimilarity <T extends AbstractInternalModel> implements ISimilarity<T>
{
    /**
     * Auxiliary object for calculating the Euclidean distance (no weights and normalizations are used),
     * it is assumed that the input weight vectors for the comparison are already on the normalized simplex hyperplane.
     */
    protected final IDistance _distance = new Euclidean();

    /**
     * Quantifies similarity between two models.
     *
     * @param A the first model
     * @param B the second model.
     * @return similarity between the models
     */
    @Override
    public double calculateSimilarity(T A, T B)
    {
        return 0;
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
