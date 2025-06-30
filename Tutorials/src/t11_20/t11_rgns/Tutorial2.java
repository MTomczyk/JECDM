package t11_20.t11_rgns;

import container.global.GlobalDataContainer;
import container.global.initializers.DefaultRandomNumberGeneratorInitializer;
import container.global.initializers.FromStreamsInitializer;
import container.global.initializers.IRandomNumberGeneratorInitializer;
import exception.GlobalException;
import exception.ScenarioException;
import random.IRandom;
import random.L32_X64_MIX;
import random.MersenneTwister64;
import scenario.Scenario;

/**
 * This tutorial showcases new methods for configuring the creation of random number generators in the experiment executor.
 *
 * @author MTomczyk
 */
public class Tutorial2
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create GDC via params container, as usual:
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();

        // [...] Here comes some configuration

        // Configure Random Number Generator Initializer (RNGI):
        {
            // Example 1: This initializer instantiates a specified RNG given an input seed for the trial data container
            // being currently instantiated. The seed is set by the experiment executor at the global level, and it is
            // defined as: current scenario ID (i.e., 0, 1, 2, ... ) * no.trials + current trial ID (i.e., 0, 1, 2, ... ),
            // producing thus non-colliding, sequential seeds.
            //pGDC._RNGI = new DefaultRandomNumberGeneratorInitializer(seed -> new MersenneTwister64(seed));
            pGDC._RNGI = new DefaultRandomNumberGeneratorInitializer(MersenneTwister64::new); // or using the method reference
        }
        {
            // Example 2: This initializer constructs streams of RNGs using IRandom.createSplitInstances(...), or
            // IRandom.createInstancesViaJumps(...) methods, depending on the selected mode
            // (FromStreamsInitializer.Mode.SPLITTABLE/JUMPABLE flag). The streams are created either globally
            // (all required instances are created at once; see the FromStreamsInitializer.InitializationStage.GLOBAL
            // flag) or at the scenario level (only scenario-related batched are created, determined by the number of
            // trials specified;  see the FromStreamsInitializer.InitializationStage.SCENARIO flag). The below
            // constructor must also be supplied with the reference RNG instance from which the streams will be created.
            pGDC._RNGI = new FromStreamsInitializer(
                    FromStreamsInitializer.Mode.SPLITTABLE,
                    FromStreamsInitializer.InitializationStage.GLOBAL,
                    () -> new L32_X64_MIX(0));
        }
        {
            // Example 3: Alternatively, one can create own RGNs provided by implementing the interface:
            pGDC._RNGI = new IRandomNumberGeneratorInitializer()
            {
                /**
                 * This method can be implemented to create streams during GDC initialization.
                 * @param noScenarios total number of scenarios (should include even disabled ones)
                 * @param noTrials    total number of trials per scenario
                 * @throws GlobalException the exception can be thrown and can be handled by the executor
                 */
                @Override
                public void requestStreamsCreationDuringGDCInit(int noScenarios, int noTrials) throws GlobalException
                {

                }

                /**
                 * This method can be implemented to create streams during SDC initialization.
                 * @param scenario scenario being processed
                 * @param noTrials total number of trials per scenario
                 * @throws ScenarioException the exception can be thrown and can be handled by the executor
                 */
                @Override
                public void requestStreamsCreationDuringSDCInit(Scenario scenario, int noTrials) throws ScenarioException
                {

                }

                /**
                 * This method must be implemented to return a dedicated, i.e., trial-specific, RNG.
                 * @param scenario trial's scenario requesting the random number generator
                 * @param trialID  ID of a trial requesting the random number generator
                 * @param noTrials total number of trials per scenario
                 * @return a dedicated RNG instance
                 * @throws ScenarioException the exception can be thrown and can be handled by the executor
                 */
                @Override
                public IRandom getRNG(Scenario scenario, int trialID, int noTrials) throws ScenarioException
                {
                    return null;
                }
            };
        }
    }
}
