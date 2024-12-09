package emo.utils.decomposition.nsgaiii;

import emo.utils.decomposition.goal.Assignment;
import random.IRandom;

import java.util.LinkedList;

/**
 * Implementation of {@link IAssignmentResolveTie} that resolves the tie randomly.
 *
 * @author MTomczyk
 */
public class RandomAssignment implements IAssignmentResolveTie
{
    /**
     * Main method for resolving a tie when selecting a candidate assignment with a minimal niche count.
     * It resolves the tie randomly.
     *
     * @param candidates candidate assignments with a minimal niche count
     * @param R          random number generator
     * @return selected assignment
     */
    @Override
    public Assignment resolveTie(LinkedList<Assignment> candidates, IRandom R)
    {
        return candidates.get(R.nextInt(candidates.size())); // get from a list is costly, but workaround would cost more
    }
}
