package emo.aposteriori.moead;

import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.ISimilarity;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
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
 * Provides various tests for {@link MOEAD}.
 *
 * @author MTomczyk
 */
class MOEADTest
{
    /**
     * Tests the {@link MOEAD#adjustOptimizationGoals(IGoal[], ISimilarity)} method.
     */
    @Test
    void adjustOptimizationGoals()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int cuts = 50;
        int gens = 100;
        ISimilarity similarity = new Euclidean();
        IGoal[] goals = GoalsFactory.getLNormsDND(3, cuts, Double.POSITIVE_INFINITY, problemBundle._normalizations);

        MOEAD moead = MOEAD.getMOEAD(0, false, false, R, goals, problemBundle, similarity, 10);
        IRunner runner = new Runner(new Runner.Params(moead));
        runner.setSteadyStateRepeatsFor(goals.length, 0);

        String msg = null;
        try
        {
            int newCuts = 5 + R.nextInt(15);
            goals = GoalsFactory.getLNormsDND(3, newCuts, Double.POSITIVE_INFINITY, problemBundle._normalizations);
            moead.adjustOptimizationGoals(goals, similarity);
            runner.setSteadyStateRepeatsFor(goals.length, 0);

            runner.init();

            assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, moead.getPopulationSize());
            assertEquals(1, moead.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
                assertEquals(goals.length, moead.getPopulationSize());
                assertEquals(1, moead.getOffspringSize());

                newCuts = 5 + R.nextInt(15);
                goals = GoalsFactory.getLNormsDND(3, newCuts, Double.POSITIVE_INFINITY, problemBundle._normalizations);

                moead.adjustOptimizationGoals(goals, similarity);
                runner.setSteadyStateRepeatsFor(goals.length, 0);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(goals.length, moead.getSpecimensContainer().getPopulation().size());
            assertEquals(goals.length, moead.getPopulationSize());
            assertEquals(1, moead.getOffspringSize());

        } catch (RunnerException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}