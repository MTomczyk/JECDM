package t11_20.t15_reproduction_strategy;

import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaii.NSGAIIBuilder;
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
import selection.Tournament;

/**
 * This tutorial showcases how to adjust the reproduction pattern. The example instantiates the NSGA-II algorithm set to
 * produce two offspring solutions from each pair of parent solutions selected in a tournament.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        NSGAIIBuilder b = new NSGAIIBuilder(new L32_X64_MIX(0));
        DTLZBundle dtlzBundle = DTLZBundle.getBundle(
                Problem.DTLZ2, 2, 10);
        b.setCriteria(dtlzBundle._criteria);
        b.setPopulationSize(100);
        b.setParentsSelector(new Tournament(2));
        b.setInitialPopulationConstructor(dtlzBundle._construct);
        b.setSpecimensEvaluator(dtlzBundle._evaluate);

        // This example uses a standard reproducer that is expected to construct two offspring solutions from each
        // pair of parents determined by the tournament selection. The StandardDoubleTOReproducer is used, and it can be
        // supplied with an implementation of ICrossoverTO (TO = two offspring) introduced to allow for
        // (two parents->two offspring solutions) mapping. The SBXTO operator is a standard SBX crossover operator that
        // complementarily constructs the offspring using two parents (see the source for details).
        b.setParentsReproducer(new StandardDoubleTOReproducer( // overwrites the previous setter
                new SBXTO(1.0d, 20.0d), // SBX-TO crossover
                new PM(1.0d / (10.0d + 1.0d), 20.0d), // PM mutation
                // parameters specifying the decision-variable bounds and a variable repairing procedure:
                new Reflect(), 0.0d, 1.0d
        ));

        b.setDynamicOSBoundsLearningPolicy();

        // By default, the reproduction strategy instantiated in the developed EMOAs assumes (two parents ->
        // one offspring mapping). By adjusting the strategy, one can define custom mappings. The adjustment can be done
        // by an auxiliary "adjuster" object. The "adjusters", when provided, are triggered after performing the default
        // parameterization of the associated component. The line below couples the EA with a reproduction strategy
        // requesting the production of two offspring from each parent object determined by the selection procedure.
        // Note that the selection procedures implemented so far in the framework respect the constraints imposed
        // by the assumed reproduction strategy. In the case of not using the below command, an exception with the
        // "The number of offspring to construct = 1 for the Parents object being processed, but no dedicated reproducer
        // is provided" message can be expected.
        b.setEAParamsAdjuster(p -> p._reproductionStrategy = new ReproductionStrategy(2));

        try
        {
            NSGAII nsgaii = b.getInstance();
            Runner runner = new Runner(nsgaii);
            runner.executeEvolution(500);
            for (Specimen s : nsgaii.getSpecimensContainer().getPopulation())
                PrintUtils.printVectorOfDoubles(s.getPerformanceVector(), 5);
            // Additionally, one can verify the number of performed function evaluations in the following way (expecting
            // 50000 = 100 (population size; initialization) + 499 (remaining generations) * 100 (total offspring size):
            System.out.println(nsgaii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
