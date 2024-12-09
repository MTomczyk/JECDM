package emo.utils.decomposition.alloc;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalWrapper;
import random.IRandom;
import random.Shuffle;

/**
 * Uniform allocation (each goal is to be updated once) with a shuffle (uniform distribution).
 *
 * @author MTomczyk
 */

public class Uniform implements IAlloc
{
    /**
     * Used for shuffling arrays.
     */
    private Shuffle<GoalID> _shuffle = null;

    /**
     * Constructs the allocations.
     *
     * @param families families maintained by the algorithm
     * @param R        random number generator
     * @return allocation
     */
    @Override
    public GoalID[] getAllocations(Family[] families, IRandom R)
    {
        if (_shuffle == null) _shuffle = new Shuffle<>(R); // lazy init

        int ts = 0;
        for (Family f : families) ts += f.getSize();

        GoalID[] gl = new GoalID[ts];

        int pnt = 0;
        for (Family f : families)
            for (GoalWrapper g : f.getGoals())
                gl[pnt++] = g.getID();

        _shuffle.shuffle(gl);
        return gl;
    }
}
