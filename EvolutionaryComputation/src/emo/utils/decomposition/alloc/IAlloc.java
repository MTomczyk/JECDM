package emo.utils.decomposition.alloc;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.GoalID;
import random.IRandom;

/**
 * Resource allocator. Determines the order in which the goals should be "updated" in steady-state repeats.
 * More computational resources can be given to goals by allowing them to be updated more than once in a single generation.
 *
 * @author MTomczyk
 */

public interface IAlloc
{
    /**
     * Constructs the allocations.
     *
     * @param families families maintained by the algorithm
     * @param R        random number generator
     * @return allocation
     */
    GoalID[] getAllocations(Family[] families, IRandom R);
}
