package emo.aposteriori.nsgaii;

import ea.EA;
import os.ObjectiveSpaceManager;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Tournament;

/**
 * Provides means for creating an instance of NSGA-II.
 *
 * @author MTomczyk
 */
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
     * Creates the NSGA-II algorithm. The algorithm is coupled with a tournament selection of size 2.
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
        ISelect select = new Tournament(new Tournament.Params(2, populationSize, false));
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem, select, problem._construct, problem._evaluate, problem._reproduce);
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
            pOS._updateUtopiaUsingIncumbent = true;
            pOS._updateNadirUsingIncumbent = false;
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
}
