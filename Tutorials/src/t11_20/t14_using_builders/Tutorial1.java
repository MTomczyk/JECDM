package t11_20.t14_using_builders;

import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaii.NSGAIIBuilder;
import exception.EAException;
import exception.RunnerException;
import population.Specimen;
import print.PrintUtils;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import reproduction.StandardDoubleReproducer;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Reflect;
import runner.Runner;
import selection.IComparator;
import selection.Tournament;

/**
 * This tutorial showcases how to use a builder-like class to instantiate the NSGA-II algorithm.
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // First, instantiate the relevant builder object (the random number generator is final and must be provided
        // via the constructor):
        NSGAIIBuilder b = new NSGAIIBuilder(new L32_X64_MIX(0));

        // We will parameterize the method to solve a simple DTLZ2 problem with 2 objective functions.
        // For this purpose, we can create a bundle, which will facilitate the NSGA-construction process:
        DTLZBundle dtlzBundle = DTLZBundle.getBundle(
                Problem.DTLZ2, 2, 10); // params: problem, objectives, distance-related parameters

        b.setCriteria(dtlzBundle._criteria); // set criteria definitions
        b.setPopulationSize(100); // set population size
        b.setParentsSelector(new Tournament(2)); // use tournament selection
        // IMPORTANT note: the implementation of the tournament selection assumes comparing specimens by their first
        // auxiliary score values; the implemented sorting procedure of NSGA-II sets these values in a way that the
        // non-dominated + crowding distance indications are preserved (lower values are preferred; (ee the extract from
        // the builder's java doc). Nonetheless, one can customize the comparison rule in the tournament selection to
        // serve various needs in the following way:
        b.setParentsSelector(new Tournament(2, new IComparator()
        {
            /**
             * The main method for comparing two specimens.
             *
             * @param A the first specimen
             * @param B the second specimen
             * @return 1 if A is considered better, -1 if B is considered better, 0 in the case of a draw.
             */
            @Override
            public int compare(Specimen A, Specimen B)
            {
                // NOTE that the below is the default comparator used by the Tournament class (one does not need to
                // specify it).
                return Double.compare(A.getAlternative().getAuxScore(), B.getAlternative().getAuxScore());
            }
        }));

        b.setInitialPopulationConstructor(dtlzBundle._construct); // set the initial population constructor
        b.setSpecimensEvaluator(dtlzBundle._evaluate); // set specimens evaluator
        b.setParentsReproducer(dtlzBundle._reproduce); // set the default reproducer that is provided by the bundle...
        // or define own processing (assumption: the solutions use double representations and one offspring is always
        // constructed from two parents):
        b.setParentsReproducer(new StandardDoubleReproducer( // overwrites the previous setter
                new SBX(1.0d, 20.0d), // SBX crossover
                new PM(1.0d / (10.0d + 1.0d), 20.0d), // PM mutation
                // parameters specifying the decision-variable bounds and a variable repairing procedure:
                new Reflect(), 0.0d, 1.0d
        ));

        b.setDynamicOSBoundsLearningPolicy(); // NSGA-II will initially know nothing about the Pareto front bounds and
        // is set to learn them dynamically during evolution

        try
        {
            // Instantiating the method can be done in the following way. The method will throw a checked exception
            // if some parameterization means are missing:
            NSGAII nsgaii = b.getInstance();
            Runner runner = new Runner(nsgaii);
            runner.executeEvolution(500);
            // Print population:
            for (Specimen s : nsgaii.getSpecimensContainer().getPopulation())
                PrintUtils.printVectorOfDoubles(s.getPerformanceVector(), 5);
        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
