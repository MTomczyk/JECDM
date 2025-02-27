package emo.aposteriori.nsgaiii;

import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import exception.RunnerException;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Provides various tests for {@link NSGAIII}.
 *
 * @author MTomczyk
 */
class NSGAIIITest
{
    /**
     * Tests the {@link NSGAIII#adjustOptimizationGoals(IGoal[])} method.
     */
    @Test
    void adjustOptimizationGoals()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int cuts = 100;
        int gens = 100;
        IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(3, cuts, problemBundle._normalizations);

        NSGAIII nsgaiii = NSGAIII.getNSGAIII(0, false, false, R, goals, problemBundle);
        IRunner runner = new Runner(new Runner.Params(nsgaiii));

        String msg = null;
        try
        {
            int newCuts = 5 + R.nextInt(15);
            goals = GoalsFactory.getPointLineProjectionsDND(3, newCuts, problemBundle._normalizations);
            nsgaiii.adjustOptimizationGoals(goals);

            runner.init();
            assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, nsgaiii.getPopulationSize());
            assertEquals(goals.length, nsgaiii.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
                assertEquals(goals.length, nsgaiii.getPopulationSize());
                assertEquals(goals.length, nsgaiii.getOffspringSize());

                newCuts = 5 + R.nextInt(15);
                goals = GoalsFactory.getPointLineProjectionsDND(3, newCuts, problemBundle._normalizations);

                nsgaiii.adjustOptimizationGoals(goals);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(goals.length, nsgaiii.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, nsgaiii.getPopulationSize());
            assertEquals(goals.length, nsgaiii.getOffspringSize());

        } catch (RunnerException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}