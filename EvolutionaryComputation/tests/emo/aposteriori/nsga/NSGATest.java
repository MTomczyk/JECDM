package emo.aposteriori.nsga;

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
 * Provides various tests for {@link NSGA}.
 *
 * @author MTomczyk
 */
class NSGATest
{
    /**
     * Tests the {@link NSGA#adjustPopulationSize(int)} method.
     */
    @Test
    void adjustPopulationSize()
    {
        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3, 10);
        int ps = 100;
        int gens = 100;
        NSGA nsga = NSGA.getNSGA(0, false, 0.01d, ps, R, problemBundle);
        IRunner runner = new Runner(new Runner.Params(nsga));

        String msg = null;
        try
        {
            int newPS = 1 + R.nextInt(100);
            nsga.adjustPopulationSize(newPS);
            runner.init();
            assertEquals(newPS, nsga.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsga.getPopulationSize());
            assertEquals(newPS, nsga.getOffspringSize());

            for (int g = 1; g < gens - 1; g++)
            {
                runner.executeSingleGeneration(g, null);
                assertEquals(newPS, nsga.getSpecimensContainer().getPopulation().size());
                assertEquals(newPS, nsga.getPopulationSize());
                assertEquals(newPS, nsga.getOffspringSize());

                newPS = 1 + R.nextInt(100);
                nsga.adjustPopulationSize(newPS);
            }
            runner.executeSingleGeneration(gens - 1, null);

            assertEquals(newPS, nsga.getSpecimensContainer().getPopulation().size());
            assertEquals(newPS, nsga.getPopulationSize());
            assertEquals(newPS, nsga.getOffspringSize());

        } catch (RunnerException e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }
}