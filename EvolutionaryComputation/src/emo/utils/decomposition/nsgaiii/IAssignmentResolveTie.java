package emo.utils.decomposition.nsgaiii;

import emo.utils.decomposition.goal.Assignment;
import random.IRandom;

import java.util.LinkedList;

/**
 * Interface for resolving a tie when selecting an assignment with a minimal niche count.
 * (used, e.g., by {@link NSGAIIIGoalsManager}).
 *
 * @author MTomczyk
 */
public interface IAssignmentResolveTie
{
    /**
     * Main method for resolving a tie when selecting a candidate assignment with a minimal niche count.
     *
     * @param candidates candidate assignments with a minimal niche count
     * @param R random number generator
     * @return selected assignment
     */
    Assignment resolveTie(LinkedList<Assignment> candidates, IRandom R);
}
