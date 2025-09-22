package t11_20.t14_using_builders;

import decisionsupport.operators.LNormOnSimplex;
import emo.interactive.StandardDSSBuilder;
import emo.interactive.iemod.IEMOD;
import emo.interactive.iemod.IEMODBuilder;
import emo.utils.decomposition.goal.GoalsFactory;
import exception.EAException;
import exception.RunnerException;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.ERS;
import model.constructor.value.rs.ers.evolutionary.EvolutionaryModelConstructor;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.iterationslimit.Constant;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
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
import selection.Random;

/**
 * This tutorial showcases how to use a builder-like class to instantiate the IEMO/D algorithm.
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
        // First, instantiate the relevant builder object (the random number generator is final and must be provided
        // via the constructor):
        IEMODBuilder<LNorm> b = new IEMODBuilder<>(new L32_X64_MIX(0));

        // We will parameterize the method to solve a simple DTLZ2 problem with 2 objective functions.
        // For this purpose, we can create a bundle, which will facilitate the IEMOD-construction process:
        DTLZBundle dtlzBundle = DTLZBundle.getBundle(
                Problem.DTLZ2, 2, 10); // params: problem, objectives, distance-related parameters

        b.setCriteria(dtlzBundle._criteria); // set criteria definitions
        // Set the optimization goals for IEMO/D:
        b.setGoals(GoalsFactory.getLNormsDND(2, 99, Double.POSITIVE_INFINITY, dtlzBundle._normalizations));
        int ps = b.getGoals().length; // derive the population size for the following parameterization
        b.setSimilarity(new emo.utils.decomposition.similarity.lnorm.Euclidean()); // define the similarity measure
        // between the goals
        b.setNeighborhoodSize(10); // set the neighborhood size
        b.setParentsSelector(new Random()); // set the random selection of parents (IEMO/D is founded on MOEA/D that
        // uses a restricted mating pool)

        b.setInitialPopulationConstructor(dtlzBundle._construct); // set the initial population constructor
        b.setSpecimensEvaluator(dtlzBundle._evaluate); // set specimens evaluator
        b.setParentsReproducer(new StandardDoubleReproducer( // define the reproducer:
                new SBX(1.0d, 20.0d), // SBX crossover
                new PM(1.0d / (10.0d + 1.0d), 20.0d), // PM mutation
                // parameters specifying the decision-variable bounds and a variable repairing procedure:
                new Reflect(), 0.0d, 1.0d
        ));

        // We will assume that the algorithm knows the Pareto front bounds from the beginning:
        b.setFixedOSBoundsLearningPolicy(dtlzBundle._normalizations, dtlzBundle._utopia, dtlzBundle._nadir);
        // Note that the dynamic adjustment of the known Pareto front bounds is a relatively complex concept
        // in preference-learning methods. It is somewhat covered in tutorial 15.

        // The preference-based IEMO/D requires the instantiation of a decision-support system. It is done via
        // a suitably parameterized builder:
        StandardDSSBuilder<LNorm> bD = new StandardDSSBuilder<>();
        bD.setPreferenceModel(new model.definitions.LNorm()); // define the assumed preference model (L-norm)
        bD.setInteractionRule(new IterationInterval(200, 20, 10)); // define the interaction
        // rule (starting from 200th generation, after every 20 generations, assume the limit of 10 interactions)
        bD.setReferenceSetConstructor(new RandomPairs(new RequiredSpread(1.0E-6))); // use a reference set
        // constructor that prepares a random pair of solutions to be compared by the DM
        //  Use the model of an artificial DM to compare the solutions. This example assumes that it is modelled using
        //  a weighted Chebyshev function with equal weights:
        bD.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY, dtlzBundle._normalizations))));

        // This example uses the Evolutionary Rejection Sampling method as the preference-learning procedure:
        ERS.Params<LNorm> pERS = new ERS.Params<>(
                new LNormGenerator(2, Double.POSITIVE_INFINITY, dtlzBundle._normalizations));
        pERS._similarity = new Euclidean();
        pERS._feasibleSamplesToGenerate = ps;
        pERS._inconsistencyThreshold = ps - 1;
        pERS._iterationsLimit = new Constant(100000);
        pERS._EMC = new EvolutionaryModelConstructor<>(
                new LNormOnSimplex(Double.POSITIVE_INFINITY, 0.2d, 0.2d),
                new Tournament<>(2));
        bD.setModelConstructor(new ERS<>(pERS));
        b.setStandardDSSBuilder(bD);

        try
        {
            // Instantiating the method can be done in the following way. The method will throw a checked exception
            // if some parameterization means are missing:
            IEMOD iemod = b.getInstance();
            Runner runner = new Runner(iemod, ps);
            // IMPORTANT NOTE: since IEMO/D is a preference-learning method, a more demanding approach than the regular
            // NSGA-II algorithm, the optimization process may take more time:
            runner.executeEvolution(500);
            // Print population.NOTE that the final specimens are expected to be located around the Pareto front's
            // middle solution, i.e., around the [0.7, 0.7] coordinate.
            for (Specimen s : iemod.getSpecimensContainer().getPopulation())
                PrintUtils.printVectorOfDoubles(s.getPerformanceVector(), 5);

        } catch (EAException | RunnerException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
