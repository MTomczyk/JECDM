package container.global.initializers;

import container.global.GlobalDataContainer;
import random.IRandom;
import scenario.Scenario;

/**
 * Interfaces for objects providing random number generators (called by {@link GlobalDataContainer}).
 *
 * @author MTomczyk
 */
public interface IRandomNumberGeneratorInitializer
{
    /**
     * Main method. Called implicitly by {@link executor.ScenarioExecutor} when creating trial data containers.
     *
     * @param scenario trial's scenario requesting the random number generator
     * @param trialID  ID of a trial requesting the random number generator
     * @param noTrials total number of trials per scenario
     * @return random number generator
     */
    IRandom getRNG(Scenario scenario, int trialID, int noTrials);
}
