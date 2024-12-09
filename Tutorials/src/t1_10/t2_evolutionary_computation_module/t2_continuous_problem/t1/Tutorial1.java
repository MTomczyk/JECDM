package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.t1;

import ea.EA;
import ea.EATimestamp;
import exception.EAException;
import population.Specimen;
import random.IRandom;
import random.MersenneTwister64;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Kernel;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Utils;

/**
 * This tutorial focuses on creating an evolutionary method for solving the 2-variable continuous problem.
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
        Kernel [] kernels = Utils.getDefaultKernels();
        IRandom R = new MersenneTwister64(0);
        int populationSize = 100;
        int generations = 20;

        EA contEA = Utils.getContEA(populationSize, kernels, R);

        int printLimit = 5;

        try
        {
            System.out.println("Initialization (generation = 0):");
            contEA.init();

            //noinspection DataFlowIssue
            for (int i = 0; i < Math.min(printLimit, populationSize); i++)
            {
                Specimen s = contEA.getSpecimensContainer().getPopulation().get(i);
                Utils.printSpecimenData(s, true);
            }

        } catch (EAException e)
        {
            throw new RuntimeException(e);
        }

        for (int g = 1; g < generations; g++)
        {
            System.out.println("Generation = " + g + ":");
            try
            {
                contEA.step(new EATimestamp(g, 0));

                //noinspection DataFlowIssue
                for (int i = 0; i < Math.min(printLimit, populationSize); i++)
                {
                    Specimen s = contEA.getSpecimensContainer().getPopulation().get(i);
                    Utils.printSpecimenData(s, true);
                }

            } catch (EAException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


}
