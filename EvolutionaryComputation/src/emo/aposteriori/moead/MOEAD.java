package emo.aposteriori.moead;

import ea.EA;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import emo.utils.decomposition.similarity.ISimilarity;
import os.ObjectiveSpaceManager;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;

/**
 * Provides means for creating an instance of MOEA/D.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class MOEAD extends EA
{
    /**
     * MOEA/D's goal manager.
     */
    private MOEADGoalsManager _goalsManager;

    /**
     * Parameterized constructor (private).
     *
     * @param p params container
     */
    private MOEAD(EA.Params p)
    {
        super(p);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with a random selection.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent   if true, nadir incumbent will be used when updating OS
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D.
     * @return MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        // IMPORTANT: create just 1 offspring (1 offspring in one steady-state repeat)
        ISelect select = new Random(2);
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize);
    }

    /**
     * Creates the MOEA/D algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent   if true, nadir incumbent will be used when updating OS
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D.
     * @return MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 IConstruct construct,
                                 IEvaluate evaluate,
                                 IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        MOEADGoalsManager.Params pManager = new MOEADGoalsManager.Params(goals, similarity, neighborhoodSize);
        pManager._alloc = new Uniform(); // Create random (uniform) allocation
        MOEADGoalsManager manager = new MOEADGoalsManager(pManager);

        // Instantiate the bundle:
        MOEADBundle.Params pB = new MOEADBundle.Params(problem._criteria, manager);

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (updateOSDynamically)
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = problem._criteria;
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = true;
            pOS._updateNadirUsingIncumbent = useNadirIncumbent;
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations (will be delivered to the object responsible for calculating crowding distances):
            pB._initialNormalizations = problem._normalizations;
            pB._osManager = null; // no os manager needed
            manager.updateNormalizations(pB._initialNormalizations); // update normalizations
        }

        pB._select = select;
        pB._construct = construct;
        pB._reproduce = reproduce;
        pB._evaluate = evaluate;
        pB._name = "MOEA/D";

        // Instantiate the bundle:
        MOEADBundle moeadBundle = new MOEADBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, moeadBundle);
        pEA._populationSize = goals.length;
        pEA._offspringSize = 1; // Important: offspring size = 1
        pEA._R = R;
        pEA._id = id;
        MOEAD moead = new MOEAD(pEA);
        moead._goalsManager = pB._goalsManager;
        return moead;
    }

    /**
     * Auxiliary method for adjusting the optimization goals (thus, population size and other relevant fields).
     * Use with caution. It should not be invoked when executing an initialization or a generation but between
     * these steps (not even between steady-state repeats). The method does not explicitly extend the population array in
     * {@link population.SpecimensContainer#getPopulation()} nor truncate it. However, the default implementation of
     * phases allows for automatically adapting to new population sizes during evolution.
     *
     * @param goals      new optimization goals (the method terminates if null or empty)
     * @param similarity similarity measure used to build the neighborhood
     */
    public void adjustOptimizationGoals(IGoal[] goals, ISimilarity similarity)
    {
        if ((goals == null) || (goals.length == 0)) return;
        if (similarity == null) return;
        setPopulationSize(goals.length);
        setOffspringSize(1);
        _goalsManager.restructureAndMakeBestAssignments(new IGoal[][]{goals}, new ISimilarity[]{similarity},
                getSpecimensContainer());
    }
}
