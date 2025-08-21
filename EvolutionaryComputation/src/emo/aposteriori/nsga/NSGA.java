package emo.aposteriori.nsga;

import criterion.Criteria;
import ea.EA;
import ea.IEA;
import os.ObjectiveSpaceManager;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import phase.IConstruct;
import phase.IEvaluate;
import population.SpecimensContainer;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.MOOProblemBundle;
import random.IRandom;
import reproduction.DoubleReproduce;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;
import space.distance.Euclidean;

/**
 * Provides means for creating an instance of NSGA.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NSGA extends EA implements IEA
{
    /**
     * NSGA bundle
     */
    protected NSGABundle _bundle;

    /**
     * Parameterized constructor (private).
     *
     * @param p params container
     */
    private NSGA(Params p)
    {
        super(p);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize, R, problem);
    }

    /**
     * Creates the NSGA algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem)
    {
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, null);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize, R, problem, osAdjuster);
    }

    /**
     * Creates the NSGA algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, osAdjuster);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param threshold      threshold for distances when calculating niche count values
     * @param populationSize population size
     * @param R              the RGN
     * @param criteria       criteria
     * @param select         parents selector
     * @param construct      specimens constructor (creates decision double-vectors)
     * @param evaluate       specimens evaluator (evaluates decision double-vectors)
     * @param reproduce      specimens reproducer (creates offspring decision double-vectors)
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(double threshold,
                               int populationSize,
                               IRandom R,
                               Criteria criteria,
                               ISelect select,
                               DoubleConstruct.IConstruct construct,
                               DoubleEvaluate.IEvaluate evaluate,
                               DoubleReproduce.IReproduce reproduce)
    {
        return getNSGA(0, true, threshold, populationSize, R, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce));
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param threshold      threshold for distances when calculating niche count values
     * @param populationSize population size
     * @param R              the RGN
     * @param criteria       criteria array
     * @param select         parents selector
     * @param construct      specimens constructor
     * @param evaluate       specimens evaluator
     * @param reproduce      specimens reproduce
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(double threshold,
                               int populationSize,
                               IRandom R,
                               Criteria criteria,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce)
    {
        return getNSGA(threshold, populationSize, R, MOOProblemBundle.getProblemBundle(criteria), select, construct, evaluate, reproduce);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param threshold      threshold for distances when calculating niche count values
     * @param populationSize population size
     * @param R              the RGN
     * @param problem        problem bundle (provides criteria)
     * @param select         parents selector
     * @param construct      specimens constructor
     * @param evaluate       specimens evaluator
     * @param reproduce      specimens reproduce
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce)
    {
        return getNSGA(0, true, threshold, populationSize, R, problem, select, construct,
                evaluate, reproduce, null);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproduce
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce)
    {
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select, construct,
                evaluate, reproduce, null);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @param reproduce           specimens reproducer
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize, R, problem, select, construct, evaluate, reproduce, osAdjuster);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @param reproduce           specimens reproducer
     * @return NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        // Instantiate the bundle:
        NSGABundle.Params pB = new NSGABundle.Params(problem._criteria);
        // Select the distance function:
        pB._distance = new Euclidean();
        // Distance threshold:
        pB._th = threshold;
        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically)
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = problem._criteria;
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = false;
            pOS._updateNadirUsingIncumbent = false;
            if (osAdjuster != null) osAdjuster.adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(problem);
            pB._distance.setNormalizations(problem._normalizations);
        }

        // Specify the selection method
        pB._select = select;

        // Specify problem-related fields:
        pB._construct = construct;
        pB._reproduce = reproduce;
        pB._evaluate = evaluate;

        pB._name = "NSGA";

        // Instantiate the bundle:
        NSGABundle nsgaBundle = new NSGABundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, nsgaBundle);
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._R = R;
        pEA._id = id;

        NSGA nsga = new NSGA(pEA);
        nsga._bundle = nsgaBundle;
        return nsga;
    }

    /**
     * Auxiliary method for adjusting the population size. It also suitably alters the offspring size (should equal
     * population size). Use with caution. It should not be invoked when executing an initialization or a generation
     * but between these steps. The method does not explicitly extend the population array in
     * {@link SpecimensContainer#getPopulation()} nor truncate it. However, the default implementation of phases allows
     * for automatically adapting to new population sizes during evolution.
     *
     * @param populationSize new population size (set to 1 if the input is lesser)
     */
    public void adjustPopulationSize(int populationSize)
    {
        setPopulationSize(populationSize);
        setOffspringSize(populationSize);
    }

    /**
     * Auxiliary method for adjusting niche count threshold. It should not be invoked when executing an initialization
     * or a generation but between these steps.
     *
     * @param th new niche count threshold
     */
    public void adjustNicheCountThreshold(double th)
    {
        _bundle._nsgaSort.setNicheCountThreshold(th);
    }

    /**
     * Getter for the current niche count threshold.
     *
     * @return current niche count threshold
     */
    public double getNicheCountThreshold()
    {
        return _bundle._nsgaSort.getNicheCountThreshold();
    }
}
