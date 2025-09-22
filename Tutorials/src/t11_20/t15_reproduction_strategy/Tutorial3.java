package t11_20.t15_reproduction_strategy;

import emo.aposteriori.moead.MOEAD;
import emo.aposteriori.moead.MOEADBuilder;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.EAException;
import exception.RunnerException;
import population.Specimen;
import print.PrintUtils;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.L32_X64_MIX;
import reproduction.ReproductionStrategy;
import reproduction.StandardDoubleTOReproducer;
import reproduction.operators.crossover.SBXTO;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Reflect;
import runner.Runner;
import selection.Random;

/**
 * This tutorial showcases how to adjust the reproduction pattern. The example instantiates the MOEA/D algorithm set to
 * produce two offspring solutions for each optimization goal update.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial3
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        MOEADBuilder b = new MOEADBuilder(new L32_X64_MIX(0));
        DTLZBundle dtlzBundle = DTLZBundle.getBundle(
                Problem.DTLZ2, 2, 10);
        b.setCriteria(dtlzBundle._criteria);
        // Use 100 weighted Chebyshev functions with weights generated using the Das and Dennis' systematic approach:
        IGoal[] goals = GoalsFactory.getLNormsDND(2, 99, Double.POSITIVE_INFINITY, null);
        b.setGoals(goals); // set the goals
        b.setSimilarity(new Euclidean()); // quantify the similarity based on the Euclidean distance between the weight vectors
        b.setNeighborhoodSize(10); // set the neighborhood size to 10
        b.setParentsSelector(new Random()); // use a regular random selection due to using a restricted mating pool
        b.setInitialPopulationConstructor(dtlzBundle._construct);
        b.setSpecimensEvaluator(dtlzBundle._evaluate);

        // Construct two offspring solutions from two parents:
        b.setParentsReproducer(new StandardDoubleTOReproducer( // overwrites the previous setter
                new SBXTO(1.0d, 20.0d), // SBX-TO crossover
                new PM(1.0d / (10.0d + 1.0d), 20.0d), // PM mutation
                // parameters specifying the decision-variable bounds and a variable repairing procedure:
                new Reflect(), 0.0d, 1.0d
        ));

        b.setDynamicOSBoundsLearningPolicy();
        b.setEAParamsAdjuster(p -> {
            p._offspringSize = 2; // this field indicates how many offspring solutions are to be generated in
            // one iteration (steady-state repeat)
            p._reproductionStrategy = new ReproductionStrategy(2); // use a constant
            // reproduction strategy specifying a construction of two offspring solutions from each Parents
            // object selected during the selection phase

            // So far, it is expected to produce in total 2 * 100 offspring solutions in one generation (offspring
            // size x the number of optimization goals; thus, the number of steady-state repeats). If one wants to
            // cap the total number of offspring to produce in one generation so that it matches the population size
            // (the number of optimization goals), one may set the field below (uncomment):
            //p._offspringLimitPerGeneration = goals.length;
        });

        try
        {
            MOEAD moead = b.getInstance();
            Runner runner = new Runner(moead, goals.length); // method and the number of steady-state repeats
            runner.executeEvolution(500);
            for (Specimen s : moead.getSpecimensContainer().getPopulation())
                PrintUtils.printVectorOfDoubles(s.getPerformanceVector(), 5);
            // Additionally, one can verify the number of performed function evaluations in the following way (expecting
            // 99900 = 100 (population size; initialization) + 499 (remaining generations) * 200 (steady state repeats
            // * offspring size):
            System.out.println(moead.getSpecimensContainer().getNoPerformedFunctionEvaluations());
            // If, however, the "p._offspringLimitInGeneration = goals.length;" line is uncommented, the expected
            // total number of function evaluations should equal 50000 (100 (population size; initialization)
            // + 499 (remaining generations) * 100 (total number of offspring solutions allowed to be produced):
        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
