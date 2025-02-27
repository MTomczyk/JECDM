package emo.aposteriori.nsgaii;

import exception.RunnerException;
import org.junit.jupiter.api.Test;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link NSGAII}.
 *
 * @author MTomczyk
 */
class NSGAIITest
{
    /**
     * Tests the {@link NSGAII#adjustPopulationSize(int)} method.
     */
    @Test
    void adjustPopulationSize()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int ps = 100;
        int gens = 100;
        NSGAII nsgaii = NSGAII.getNSGAII(0, false, ps, R, problemBundle);
        IRunner runner = new Runner(new Runner.Params(nsgaii));

        String msg = null;
        try
        {
            int newPS = 1 + R.nextInt(100);
            nsgaii.adjustPopulationSize(newPS);
            runner.init();
            assertEquals(newPS, nsgaii.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsgaii.getPopulationSize());
            assertEquals(newPS, nsgaii.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(newPS, nsgaii.getSpecimensContainer().getPopulation().size());
                assertEquals(newPS, nsgaii.getPopulationSize());
                assertEquals(newPS, nsgaii.getOffspringSize());

                newPS = 1 + R.nextInt(100);
                nsgaii.adjustPopulationSize(newPS);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(newPS, nsgaii.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsgaii.getPopulationSize());
            assertEquals(newPS, nsgaii.getOffspringSize());

        } catch (RunnerException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}