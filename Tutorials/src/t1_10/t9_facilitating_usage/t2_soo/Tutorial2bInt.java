package t1_10.t9_facilitating_usage.t2_soo;

import criterion.Criteria;
import ea.EA;
import ea.EAFactory;
import exception.RunnerException;
import phase.IntConstruct;
import phase.IntEvaluate;
import phase.Sort;
import phase.SortByPerformanceValue;
import population.Specimen;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.IntReproduce;
import reproduction.StandardIntReproducer;
import reproduction.operators.crossover.SinglePointCrossover;
import reproduction.operators.mutation.RandomIntegerModifier;
import reproduction.valuecheck.Wrap;
import runner.IRunner;
import runner.Runner;
import selection.ISelect;
import selection.Random;
import utils.MathUtils;

/**
 * This tutorial showcases how to concisely instantiate an evolutionary algorithm that employs arrays of integers
 * to represent specimens (decision vectors).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2bInt
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
        int intLim = 10; // upper limit for integers
        int gens = 50; // limit for the number of generations
        Criteria criteria = Criteria.constructCriterion("Minimize sum", false); // Criterion
        IRandom R = new MersenneTwister64(0); // RGN


        IntConstruct.IConstruct constructor = R1 -> R1.nextInts(n, intLim); // initial decision vectors: random arrays of integers (size = n)
        IntEvaluate.IEvaluate evaluator = v -> new double[]{MathUtils.getSum(v)}; // evaluation function: sum of decision variable values
        // Create the reproducer (single point crossover, random integer modifier, check bounds):
        StandardIntReproducer sir = new StandardIntReproducer(new SinglePointCrossover(),
                new RandomIntegerModifier(1.0d / (double) n, -2, 2), new Wrap(), 0, intLim);
        IntReproduce.IReproduce reproducer = sir::reproduce;
        Sort sort = new SortByPerformanceValue(true); // sort by performance (first objective value); ascending order
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
                System.out.println(PrintUtils.getVectorOfIntegers(s.getIntDecisionVector()));
            }


            for (int i = 1; i < gens; i++)
            {
                runner.executeSingleGeneration(i, null);

                System.out.println("Generation = " + i);
                for (Specimen s : ea.getSpecimensContainer().getPopulation())
                {
                    System.out.print(s.getID().toString() + ": ");
                    System.out.print(s.getEvaluations()[0] + "; ");
                    System.out.println(PrintUtils.getVectorOfIntegers(s.getIntDecisionVector()));
                }
            }

        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

    }
}
