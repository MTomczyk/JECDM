package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.t1;

import ea.EA;
import ea.EATimestamp;
import exception.EAException;
import population.Specimen;
import random.IRandom;
import random.MersenneTwister64;
import t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common.Utils;

/**
 * This tutorial focuses on creating an evolutionary method for solving the knapsack problem and manually executing
 * the evolutionary process.
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Set basic variables:
        IRandom R = new MersenneTwister64(0);
        int populationSize = 10;
        int knapsackCapacity = 50;
        int generations = 20;
        boolean repair = true;

        // Retrieve the parameterized instance of the EA
        EA knapsackEA = Utils.getKnapsackEA(populationSize, R, knapsackCapacity, repair);

        // Sets the upper limit for the specimens to be printed per each generation
        int printLimit = 5;

        try
        {
            System.out.println("Initialization (generation = 0):");
            // Call the init method:
            knapsackEA.init();

            // Print specimen data:
            //noinspection DataFlowIssue
            for (int i = 0; i < Math.min(printLimit, populationSize); i++)
            {
                Specimen s = knapsackEA.getSpecimensContainer().getPopulation().get(i);
                Utils.printSpecimenData(s, true);
            }

        } catch (EAException e)
        {
            throw new RuntimeException(e);
        }

        // Perform a desired number of generations:
        for (int g = 1; g < generations; g++)
        {
            System.out.println("Generation = " + g + ":");
            try
            {
                // Execute the step method:
                knapsackEA.step(new EATimestamp(g, 0));

                // Print specimen data:
                //noinspection DataFlowIssue
                for (int i = 0; i < Math.min(printLimit, populationSize); i++)
                {
                    Specimen s = knapsackEA.getSpecimensContainer().getPopulation().get(i);
                    Utils.printSpecimenData(s, true);
                }

            } catch (EAException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


}
