package emo.utils.decomposition;

import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.LNorm;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link GoalsManager}.
 *
 * @author MTomczyk
 */
class GoalsManagerTest
{
    /**
     * Test 1
     */
    @Test
    void test1()
    {
        {
            AbstractGoalsManager.Params p = new AbstractGoalsManager.Params((IGoal[]) null);
            GoalsManager GM = new GoalsManager(p);
            assertFalse(GM.validate());
        }
        {
            AbstractGoalsManager.Params p = new AbstractGoalsManager.Params(new IGoal[][]{});
            GoalsManager GM = new GoalsManager(p);
            assertTrue(GM.validate());
        }
        {
            IGoal[] goals = GoalsFactory.getLNormsDND(2, 5, Double.POSITIVE_INFINITY);
            AbstractGoalsManager.Params p = new AbstractGoalsManager.Params(new IGoal[][]{goals, null});
            GoalsManager GM = new GoalsManager(p);
            assertFalse(GM.validate());
        }
        {
            IGoal[] goals = GoalsFactory.getLNormsDND(2, 5, Double.POSITIVE_INFINITY);
            AbstractGoalsManager.Params p = new AbstractGoalsManager.Params(new IGoal[][]{goals, new IGoal[]{}});
            GoalsManager GM = new GoalsManager(p);
            assertTrue(GM.validate());
        }
        {
            IGoal[] goals = GoalsFactory.getLNormsDND(2, 5, Double.POSITIVE_INFINITY);
            AbstractGoalsManager.Params p = new AbstractGoalsManager.Params(goals);
            GoalsManager GM = new GoalsManager(p);
            assertTrue(GM.validate());
        }
        int trials = 1000;
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < trials; t++)
        {
            int total = 0;
            int fs = R.nextInt(5);
            IGoal[][] goals = new LNorm[fs][];
            for (int i = 0; i < fs; i++)
            {
                int gs = R.nextInt(10);
                total += gs;
                goals[i] = new LNorm[gs];
                for (int j = 0; j < gs; j++)
                    goals[i][j] = new LNorm(new space.scalarfunction.LNorm(Double.POSITIVE_INFINITY));
            }
            AbstractGoalsManager.Params p = new AbstractGoalsManager.Params(goals);
            GoalsManager GM = new GoalsManager(p);
            assertTrue(GM.validate());
            assertEquals(total, GM._totalNoGoals);
        }

    }
}