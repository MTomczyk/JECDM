package container.global.initializers;

import random.IRandom;
import random.MersenneTwister64;
import scenario.Scenario;

/**
 * Creates Mersenne Twister generator (64 bits). The RNG counter maintained and provided via {@link container.global.GlobalDataContainer}
 * is used as a non-colliding seed.
 *
 * @author MTomczyk
 */
public class DefaultRandomNumberGeneratorInitializer implements IRandomNumberGeneratorInitializer
{
    /**
     * Main method.
     *
     * @param scenario trial's scenario requesting the random number generator
     * @param trialID  ID of a trial requesting the random number generator
     * @param noTrials total number of trials per scenario
     * @return random number generator
     */
    public IRandom getRNG(Scenario scenario, int trialID, int noTrials)
    {
        int seed = scenario.getID() * noTrials + trialID;
        return new MersenneTwister64(seed);
    }
}
