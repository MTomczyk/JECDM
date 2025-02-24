package emo.aposteriori.nsgaiii;

import ea.EA;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.IAssignmentResolveTie;
import emo.utils.decomposition.nsgaiii.ISpecimenResolveTie;
import emo.utils.decomposition.nsgaiii.NSGAIIIGoalsManager;
import os.ObjectiveSpaceManager;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import reproduction.IReproduce;
import selection.ISelect;
import selection.Random;

/**
 * Provides means for creating an instance of NSGA-III.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NSGAIII extends EA
{
    /**
     * Parameterized constructor (private).
     *
     * @param p params container
     */
    private NSGAIII(EA.Params p)
    {
        super(p);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with a random selection.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent    if true, nadir incumbent will be used when updating OS
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, assignmentResolveTie, specimenResolveTie);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent    if true, nadir incumbent will be used when updating OS
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed))
     * @param select               parents selector
     * @param construct            specimens constructor
     * @param evaluate             specimens evaluator
     * @param reproduce            specimens reproducer
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ISelect select,
                                     IConstruct construct,
                                     IEvaluate evaluate,
                                     IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        NSGAIIIGoalsManager.Params pManager = new NSGAIIIGoalsManager.Params(goals);
        NSGAIIIGoalsManager manager = new NSGAIIIGoalsManager(pManager);

        // Instantiate the bundle:
        NSGAIIIBundle.Params pB = new NSGAIIIBundle.Params(problem._criteria, manager, assignmentResolveTie, specimenResolveTie);

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

        pB._name = "NSGA-III";

        // Instantiate the bundle:
        NSGAIIIBundle nsgaBundle = new NSGAIIIBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(problem._criteria, nsgaBundle);
        pEA._populationSize = goals.length;
        pEA._offspringSize = goals.length;
        pEA._R = R;
        pEA._id = id;
        return new NSGAIII(pEA);
    }
}
