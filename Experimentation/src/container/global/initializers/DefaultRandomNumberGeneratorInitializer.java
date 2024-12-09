package container.global.initializers;

import random.IRandom;
import random.MersenneTwister64;

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
     * @param currentCounterValue GDC increments the RNG counter each time a new instance is created; this input is
     *                            a non-colliding integer value that can be used as a seed when instantiating the RNG
     * @return random number generator instance
     */
    @Override
    public IRandom getRNG(int currentCounterValue)
    {
        return new MersenneTwister64(currentCounterValue);
    }
}
