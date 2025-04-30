package emo.utils.decomposition.similarity;

import emo.utils.decomposition.goal.IGoal;

/**
 * Interface for classes quantifying similarities between goals.
 *
 * @author MTomczyk
 */

public interface ISimilarity
{
    /**
     * Quantifies similarity between two optimization goals.
     * @param A the first optimization goal
     * @param B the second optimization goal.
     * @return similarity between the goals
     */
    double calculateSimilarity(IGoal A, IGoal B);

    /**
     * Used to determine is less/more means closer (more similar)/further.
     *
     * @return true, if smaller values mean closer (more similar); false otherwise.
     */
    boolean isLessMeaningCloser();
}
