package emo.aposteriori.nsga;

import ea.EA;
import os.ObjectiveSpaceManager;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Tournament;
import space.distance.Euclidean;

/**
 * Provides means for creating an instance of NSGA.
 *
 * @author MTomczyk
 */
public class NSGA extends EA
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
     * Creates the NSGA algorithm. The algorithm is coupled with a tournament selection of size 2.
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
        ISelect select = new Tournament(new Tournament.Params(2, false));
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select, problem._construct, problem._evaluate, problem._reproduce);
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
                               IReproduce reproduce)
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
            pOS._updateUtopiaUsingIncumbent = true;
            pOS._updateNadirUsingIncumbent = false;
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations (will be delivered to the object responsible for calculating niche counts):
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = null; // no os manager needed
            // Adjust the distance function:
            pB._distance.setNormalizations(problem._normalizations);
        }

        // Specify the selection method
        pB._select = select;

        // Specify problem-related fields (obtained from the problem bundle):
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
     * but between these steps.
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
