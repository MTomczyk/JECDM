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
     * Used to determine preference direction (i.e., whether the smaller or bigger values are preferred).
     * @return true -> smaller values are preferred; false otherwise.
     */
    boolean isLessPreferred();
}
