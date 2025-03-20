package t1_10.t9_facilitating_usage.t2_soo;

import criterion.Criteria;
import ea.EA;
import ea.EAFactory;
import exception.RunnerException;
import org.apache.commons.math4.legacy.stat.StatUtils;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import phase.Sort;
import phase.SortByPerformanceValue;
import population.Specimen;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.DoubleReproduce;
import reproduction.StandardDoubleReproducer;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Wrap;
import runner.IRunner;
import runner.Runner;
import selection.ISelect;
import selection.Random;

/**
 * This tutorial showcases how to concisely instantiate an evolutionary algorithm that employs arrays of doubles
 * to represent specimens (decision vectors).
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode", "ExtractMethodRecommender"})
public class Tutorial2aDouble
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int ps = 20; // population size
        int n = 10; // no. decision variables
        int gens = 50; // limit for the number of generations
        Criteria criteria = Criteria.constructCriterion("Minimize sum", false); // Criterion
        IRandom R = new MersenneTwister64(0); // RGN

        DoubleConstruct.IConstruct constructor = R1 -> R1.nextDoubles(n); // initial decision vectors: random arrays of doubles (size = n)
        DoubleEvaluate.IEvaluate evaluator = v -> new double[]{StatUtils.sum(v)}; // evaluation function: sum of decision variable values

        // Reproduce this way (option #1):
        //SBX sbx = new SBX(1.0d, 10.0d); // SBX used as the crossover operator
        //PM pm = new PM(1.0d / (double) n, 10.0d); // PM used as the crossover operator
        //IValueCheck vc = new Wrap(); // value check for the reproducer (checks if variables are in [0, 1] bounds and conditionally corrects them)
        //DoubleReproduce.IReproduce reproducer = (p1, p2, R1) -> // tells how an offspring decision vector is constructed from parents' vectors
        //        vc.checkAndCorrect(pm.mutate(sbx.crossover(p1, p2, R1), R1), 0.0d, 1.0d);

        // Option #2: Instantiates the standard reproduced (2 parents per offspring; decision vectors of the same length; of doubles)
        // 1. SBX used as the crossover operator
        // 2. PM used as the crossover operator
        // 3. value check for the reproducer (Wrap class; checks if variables are in [0, 1] bounds and conditionally corrects them)
        StandardDoubleReproducer sdr = new StandardDoubleReproducer(new SBX(1.0d, 10.0d),
                new PM(1.0d/(double) n, 10.0d), new Wrap(), 0.0d, 1.0d);
        // tells how an offspring decision vector is constructed from parents' vectors
        DoubleReproduce.IReproduce reproducer = sdr::reproduce;

        // Sort in the ascending order of the first objective value (first elements are preferred; the objective function is to minimize the sum)
        Sort sort = new SortByPerformanceValue(true); // sort by performance (first objective value); ascending order

        // Alternatively (provide own implementation of ISort):
        /*
        Sort sort = new Sort(new Sort.ISort()
        {
            @Override
            public void sort(ArrayList<Specimen> specimens)
            {

            }
        });
        */

        ISelect selector = new Random(); // use random parents selection (two parents per offspring)
        // Create the EA using the factory object:
        EA ea = EAFactory.getGenerationalEA("EA", ps, criteria, R, constructor, evaluator, reproducer, sort, selector);

        // Run the evolution and report populations after each generation:
        IRunner runner = new Runner(new Runner.Params(ea));

        try
        {
            runner.init();

            // Print data on population:
            System.out.println("Generation = 0");
            for (Specimen s : ea.getSpecimensContainer().getPopulation())
            {
                System.out.print(s.getID().toString() + ": ");
                System.out.print(s.getEvaluations()[0] + "; ");
                System.out.println(PrintUtils.getVectorOfDoubles(s.getDoubleDecisionVector(), 2));
            }


            for (int i = 1; i < gens; i++)
            {
                runner.executeSingleGeneration(i, null);

                System.out.println("Generation = " + i);
                for (Specimen s : ea.getSpecimensContainer().getPopulation())
                {
                    System.out.print(s.getID().toString() + ": ");
                    System.out.print(s.getEvaluations()[0] + "; ");
                    System.out.println(PrintUtils.getVectorOfDoubles(s.getDoubleDecisionVector(), 2));
                }
            }

        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

    }
}
