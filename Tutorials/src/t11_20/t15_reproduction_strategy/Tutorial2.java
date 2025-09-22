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
import reproduction.StandardDoubleMOReproducer;
import reproduction.StandardDoubleReproducer;
import reproduction.StandardDoubleTOReproducer;
import reproduction.operators.crossover.DoubleWrappingCrossover;
import reproduction.operators.crossover.SBX;
import reproduction.operators.crossover.SBXTO;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Reflect;
import runner.Runner;
import selection.Tournament;

/**
 * This tutorial showcases how to adjust the reproduction pattern. The example instantiates the NSGA-II algorithm set to
 * produce randomly one, two, or three offspring solutions from each pair of parent solutions selected in a tournament.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2
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

        // This example provides various reproducers. Each is associated with producing a different number of offspring
        // solutions. A suitable reproducer is picked during the reproduction phase based on the expected number of
        // offspring to be generated associated with the Parents during the selection phase.
        b.setParentsReproducer(
                // reproducer that maps 2 parents into 1 offspring:
                new StandardDoubleReproducer(
                        new SBX(1.0d, 20.0d),
                        new PM(1.0d, 1.0d / (10.0d + 1.0d)),
                        new Reflect(), 0.0d, 1.0d),
                // reproducer that maps 2 parents into 2 offspring:
                new StandardDoubleTOReproducer(  // reproducer that maps two parents into two offspring
                        new SBXTO(1.0d, 20.0d),
                        new PM(1.0d, 1.0d / (10.0d + 1.0d)),
                        new Reflect(), 0.0d, 1.0d),
                // reproducer that maps 2 parents into 3 (parameter) offspring (MO = multiple offspring);
                // the StandardDoubleMOReproducer is a simple wrapper for the standard crossover operator mapping two
                // parents into one offspring; it is executed a required number of times to produce a desired number
                // of offspring:
                new StandardDoubleMOReproducer(new DoubleWrappingCrossover(3,
                        new SBX(1.0d, 20.0d)),
                        new PM(1.0d, 1.0d / (10.0d + 1.0d)),
                        new Reflect(), 0.0d, 1.0d)
        );

        b.setDynamicOSBoundsLearningPolicy();

        // This example uses a reproduction strategy that associates with each selected pair of parents a number of 1,
        // 2, or 3, which indicates the expected number of offspring solutions to be produced from the parents.
        ReproductionStrategy.Params pRS = new ReproductionStrategy.Params();
        // The contextual generator is responsible for determining the expected number of offspring solutions to produce
        // on demand. This implementation returns a random integer from [1, 2, 3].
        pRS._noOffspringFromParentsGenerator = (ea, counter, noExpectedOffspringGenerated) -> 1 + ea.getR().nextInt(4);
        pRS._isReproductionStrategyConstant = false; // auxiliary flag that indicates that the above procedure
        // is not constant
        pRS._enableOffspringThresholding = true; // It is recommended to keep this flag true in the case when the total
        // expected number of offspring solutions to generate may exceed the total offspring size; if this happens and
        // the flag is true, the last indication will be suitably clipped.
        ReproductionStrategy RS = new ReproductionStrategy(pRS);

        // The above is an equivalent of:
        //ReproductionStrategy RS = ReproductionStrategy.getDynamicStrategy((ea, counter, noExpectedOffspringGenerated)
        // -> 1 + ea.getR().nextInt(3));

        // Set the new reproduction strategy:
        b.setEAParamsAdjuster(p -> p._reproductionStrategy = RS);

        try
        {
            NSGAII nsgaii = b.getInstance();
            Runner runner = new Runner(nsgaii);
            runner.executeEvolution(500);
            for (Specimen s : nsgaii.getSpecimensContainer().getPopulation())
                PrintUtils.printVectorOfDoubles(s.getPerformanceVector(), 5);
            // Additionally, one can verify the number of performed function evaluations in the following way (expecting
            // 50000 = 100 (population size; initialization) + 499 (remaining generations) * 100 (population size):
            System.out.println(nsgaii.getSpecimensContainer().getNoPerformedFunctionEvaluations());
        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
