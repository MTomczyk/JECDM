package t1_10.t9_facilitating_usage.t2_soo;

import criterion.Criteria;
import ea.EA;
import ea.EAFactory;
import exception.RunnerException;
import phase.ChromosomeConstruct;
import phase.ChromosomeEvaluate;
import phase.Sort;
import phase.SortByPerformanceValue;
import population.Chromosome;
import population.Gene;
import population.Specimen;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.ChromosomeReproduce;
import reproduction.StandardBoolReproducer;
import reproduction.StandardDoubleReproducer;
import reproduction.StandardIntReproducer;
import reproduction.operators.crossover.SBX;
import reproduction.operators.crossover.SinglePointCrossover;
import reproduction.operators.mutation.Flip;
import reproduction.operators.mutation.PM;
import reproduction.operators.mutation.RandomIntegerModifier;
import reproduction.valuecheck.Wrap;
import runner.IRunner;
import runner.Runner;
import selection.ISelect;
import selection.Random;
import utils.MathUtils;

/**
 * This tutorial showcases how to concisely instantiate an evolutionary algorithm that employs mixed specimen
 * representation.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode", "ExtractMethodRecommender"})
public class Tutorial2dChromosome
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int ps = 100; // population size
        int n = 10; // no. decision variables
        int intLim = 10; // upper limit for integers
        int gens = 100; // limit for the number of generations

        Criteria criteria = Criteria.constructCriterion("Minimize sum", false); // Criterion
        IRandom R = new MersenneTwister64(0); // RGN

        // Initial chromosome: three decision vectors: random arrays of doubles (size = n), random arrays of ints (size = n),
        //  random arrays of booleans (size = n).
        ChromosomeConstruct.IConstruct constructor = R1 -> new Chromosome(new Gene[]{
                new Gene(R1.nextDoubles(n)),
                new Gene(R1.nextInts(n, intLim)),
                new Gene(R1.nextBooleans(n))
        });

        ChromosomeEvaluate.IEvaluate evaluator = chromosome -> new double[]{MathUtils.getSum(chromosome._genes[0]._dv) +
                MathUtils.getSum(chromosome._genes[1]._iv) +
                MathUtils.getSum(chromosome._genes[2]._bv)};

        StandardDoubleReproducer sdr = new StandardDoubleReproducer(new SBX(1.0d, 10.0d),
                new PM(1.0d/(double) n, 10.0d), new Wrap(), 0.0d, 1.0d);
        StandardIntReproducer sir = new StandardIntReproducer(new SinglePointCrossover(),
                new RandomIntegerModifier(1.0d / (double) n, -2, 2), new Wrap(), 0, intLim);
        StandardBoolReproducer sbr = new StandardBoolReproducer(new SinglePointCrossover(), new Flip(1.0d / (double) n));

        ChromosomeReproduce.IReproduce reproducer = (c1, c2, R12) ->
                new Chromosome(new Gene[]{new Gene(sdr.reproduce(c1.getDoubles(0), c2.getDoubles(0), R12)),
                new Gene(sir.reproduce(c1.getIntegers(1), c2.getIntegers(1), R12)),
                new Gene(sbr.reproduce(c1.getBooleans(2), c2.getBooleans(2), R12))});

        // Sort in the ascending order of the first objective value (first elements are preferred; the objective function is to minimize the sum)
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
                System.out.println(s.getEvaluations()[0]);
                System.out.println(PrintUtils.getVectorOfDoubles(s.getDoubleDecisionVector(0), 2));
                System.out.println(PrintUtils.getVectorOfIntegers(s.getIntDecisionVector(1)));
                System.out.println(PrintUtils.getVectorOfBooleans(s.getBooleanDecisionVector(2)));
            }


            for (int i = 1; i < gens; i++)
            {
                runner.executeSingleGeneration(i, null);

                System.out.println("Generation = " + i);
                for (Specimen s : ea.getSpecimensContainer().getPopulation())
                {
                    System.out.print(s.getID().toString() + ": ");
                    System.out.println(s.getEvaluations()[0]);
                    System.out.println(PrintUtils.getVectorOfDoubles(s.getDoubleDecisionVector(0), 2));
                    System.out.println(PrintUtils.getVectorOfIntegers(s.getIntDecisionVector(1)));
                    System.out.println(PrintUtils.getVectorOfBooleans(s.getBooleanDecisionVector(2)));
                }
            }

        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

    }
}
