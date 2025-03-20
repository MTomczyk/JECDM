package emo.aposteriori.nsgaii;

import criterion.Criteria;
import ea.EA;
import os.ObjectiveSpaceManager;
import phase.DoubleConstruct;
import phase.DoubleEvaluate;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.MOOProblemBundle;
import random.IRandom;
import reproduction.DoubleReproduce;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;

/**
 * Provides means for creating an instance of NSGA-II.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NSGAII extends EA
{
    /**
     * Parameterized constructor (private).
     *
     * @param p params container
     */
    private NSGAII(EA.Params p)
    {
        super(p);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem)
    {
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem);
    }

    /**
     * Creates the NSGA-II algorithm. The algorithm is coupled with a random selection.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem)
    {
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem, null);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem, osAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem, select, problem._construct, problem._evaluate, problem._reproduce, osAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param populationSize population size
     * @param R              the RGN
     * @param criteria       criteria
     * @param select         parents selector
     * @param construct      specimens constructor (creates decision double-vectors)
     * @param evaluate       specimens evaluator (evaluates decision double-vectors)
     * @param reproduce      specimens reproducer (creates offspring decision double-vectors)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   Criteria criteria,
                                   ISelect select,
                                   DoubleConstruct.IConstruct construct,
                                   DoubleEvaluate.IEvaluate evaluate,
                                   DoubleReproduce.IReproduce reproduce)
    {
        return getNSGAII(0, true, populationSize, R, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce));
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param populationSize population size
     * @param R              the RGN
     * @param criteria       criteria
     * @param select         parents selector
     * @param construct      specimens constructor
     * @param evaluate       specimens evaluator
     * @param reproduce      specimens reproducer
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   Criteria criteria,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce)
    {
        return getNSGAII(0, true, populationSize, R, MOOProblemBundle.getProblemBundle(criteria),
                select, construct, evaluate, reproduce);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param populationSize population size
     * @param R              the RGN
     * @param problem        problem bundle (provides criteria)
     * @param select         parents selector
     * @param construct      specimens constructor
     * @param evaluate       specimens evaluator
     * @param reproduce      specimens reproducer
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce)
    {
        return getNSGAII(0, true, populationSize, R, problem, select, construct, evaluate, reproduce);
    }

    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce)
    {
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem, select, construct, evaluate, reproduce, null);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem, select, construct, evaluate, reproduce, osAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
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
        NSGAIIBundle.Params pB = new NSGAIIBundle.Params(problem._criteria);

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
            // Set the initial normalizations (will be delivered to the object responsible for calculating crowding distances):
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = null; // no os manager needed
        }

        pB._select = select;

        pB._construct = construct;
        pB._reproduce = reproduce;
        pB._evaluate = evaluate;

        pB._name = "NSGA-II";

        // Instantiate the bundle:
        NSGAIIBundle nsgaBundle = new NSGAIIBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, nsgaBundle);
        pEA._populationSize = populationSize;
        pEA._offspringSize = populationSize;
        pEA._R = R;
        pEA._id = id;
        return new NSGAII(pEA);
    }

    /**
     * Auxiliary method for adjusting the population size. It also suitably alters the offspring size (should equal
     * population size). Use with caution. It should not be invoked when executing an initialization or a generation
     * but between these steps. The method does not explicitly extend the population array in
     * {@link population.SpecimensContainer#getPopulation()} nor truncate it. However, the default implementation of
     * phases allows for automatically adapting to new population sizes during evolution.
     *
     * @param populationSize new population size (set to 1 if the input is lesser)
     */
    public void adjustPopulationSize(int populationSize)
    {
        setPopulationSize(populationSize);
        setOffspringSize(populationSize);
    }
}
