package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.t2;

import ea.EA;
import exception.RunnerException;
import population.Specimen;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common.Utils;

/**
 * This tutorial focuses on creating an evolutionary method for solving the knapsack problem and running the evolution
 * using the {@link runner.IRunner} implementation.
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
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int populationSize = 10;
        int knapsackCapacity = 50;
        int generations = 20;
        boolean repair = true;

        EA knapsackEA = Utils.getKnapsackEA(populationSize, R, knapsackCapacity, repair);

        Runner.Params pR = new Runner.Params(knapsackEA);
        IRunner runner = new Runner(pR);

        int printLimit = 10;

        try
        {
            System.out.println("Initialization (generation = 0):");
            runner.init();

            //noinspection DataFlowIssue
            for (int i = 0; i < Math.min(printLimit, populationSize); i++)
            {
                Specimen s = knapsackEA.getSpecimensContainer().getPopulation().get(i);
                Utils.printSpecimenData(s, true);
            }

        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

        for (int g = 1; g < generations; g++)
        {
            System.out.println("Generation = " + g + ":");
            try
            {
                runner.executeSingleGeneration(g, null);

                //noinspection DataFlowIssue
                for (int i = 0; i < Math.min(printLimit, populationSize); i++)
                {
                    Specimen s = knapsackEA.getSpecimensContainer().getPopulation().get(i);
                    Utils.printSpecimenData(s, true);
                }

            } catch (RunnerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


}
