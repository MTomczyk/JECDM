package model.similarity;

import model.internals.AbstractInternalModel;

/**
 * Interface for objects responsible for quantifying a similarity between two internal models
 * ({@link AbstractInternalModel}). Note that the interpretability is dependent on the flag yielded by
 * {@link ISimilarity#isLessMeaningCloser()}.
 *
 * @author MTomczyk
 */
public interface ISimilarity<T extends AbstractInternalModel>
{
    /**
     * Quantifies similarity between two models.
     *
     * @param A the first model
     * @param B the second model.
     * @return similarity between the models
     */
    double calculateSimilarity(T A, T B);

    /**
     * Used to determine is less/more means closer (more similar)/further.
     *
     * @return true, if smaller values mean closer (more similar); false otherwise.
     */
    boolean isLessMeaningCloser();
}
