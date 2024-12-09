package container.global.initializers;

import container.global.GlobalDataContainer;
import random.IRandom;

/**
 * Interfaces for objects providing random number generators (called by {@link GlobalDataContainer}).
 *
 * @author MTomczyk
 */
public interface IRandomNumberGeneratorInitializer
{
    /**
     * Main method.
     *
     * @param currentCounterValue GDC increments the RNG counter each time a new instance is created; this input is
     *                            a non-colliding integer value that can be used as a seed when instantiating the RNG
     * @return random number generator instance
     */
    IRandom getRNG(int currentCounterValue);
}
