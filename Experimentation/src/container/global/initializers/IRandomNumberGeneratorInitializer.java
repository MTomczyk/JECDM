package container.global.initializers;

import container.global.GlobalDataContainer;
import exception.GlobalException;
import exception.ScenarioException;
import random.IRandom;
import scenario.Scenario;
import scenario.Scenarios;

/**
 * Interfaces for objects providing random number generators (called by {@link GlobalDataContainer}).
 *
 * @author MTomczyk
 */
public interface IRandomNumberGeneratorInitializer
{
    /**
     * Auxiliary method. It is called by {@link executor.ExperimentPerformer} when instantiating GDC to request
     * RNG streams creation. An implementation may then construct such streams and return them
     * when {@link IRandomNumberGeneratorInitializer#getRNG(Scenario, int, int)} is called.
     *
     * @param noScenarios total number of scenarios (should include even disabled ones)
     * @param noTrials    total number of trials per scenario
     * @throws GlobalException the exception should be thrown if the streams cannot be instantiated
     */
    void requestStreamsCreationDuringGDCInit(int noScenarios, int noTrials) throws GlobalException;

    /**
     * Auxiliary method. It is called by {@link executor.ExperimentPerformer} when instantiating SDC to request RNG
     * streams creation. An implementation may then construct such streams and return them
     * when {@link IRandomNumberGeneratorInitializer#getRNG(Scenario, int, int)} is called.
     *
     * @param scenario scenario being processed
     * @param noTrials total number of trials per scenario
     * @throws ScenarioException the exception should be thrown if the streams cannot be instantiated
     */
    void requestStreamsCreationDuringSDCInit(Scenario scenario, int noTrials) throws ScenarioException;

    /**
     * Main method. Called implicitly by {@link executor.ScenarioExecutor} when creating trial data containers.
     *
     * @param scenario trial's scenario requesting the random number generator
     * @param trialID  ID of a trial requesting the random number generator
     * @param noTrials total number of trials per scenario
     * @return random number generator
     * @throws ScenarioException the exception should be thrown if the random number generator cannot be derived
     */
    IRandom getRNG(Scenario scenario, int trialID, int noTrials) throws ScenarioException;
}
