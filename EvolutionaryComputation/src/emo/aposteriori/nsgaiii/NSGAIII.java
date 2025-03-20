package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import ea.EA;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.*;
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
 * Provides means for creating an instance of NSGA-III.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NSGAIII extends EA
{
    /**
     * Reference to the goals manager.
     */
    private NSGAIIIGoalsManager _goalsManager;

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
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(0, updateOSDynamically, false, R, goals, problem);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent   if true, nadir incumbent will be used when updating OS
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent   if true, nadir incumbent will be used when updating OS
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, osAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, new RandomAssignment(), new RandomSpecimen(), osAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent   if true, nadir incumbent will be used when updating OS
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, osAdjuster);
    }


    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent   if true, nadir incumbent will be used when updating OS
     * @param R                   the RGN
     * @param goals               optimization goals
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param osAdjuster          auxiliary object responsible for customizing objective space manager params container
     *                            built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, new RandomAssignment(), new RandomSpecimen(), osAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, assignmentResolveTie, specimenResolveTie);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, assignmentResolveTie, specimenResolveTie);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent    if true, nadir incumbent will be used when updating OS
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, assignmentResolveTie, specimenResolveTie);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
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
     * Creates the NSGA-III algorithm. Sets the id to 0 the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, assignmentResolveTie, specimenResolveTie, osAdjuster);
    }


    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, assignmentResolveTie, specimenResolveTie, osAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent    if true, nadir incumbent will be used when updating OS
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, assignmentResolveTie, specimenResolveTie, osAdjuster);
    }


    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent    if true, nadir incumbent will be used when updating OS
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor, evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, assignmentResolveTie, specimenResolveTie, osAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param criteria             criteria
     * @param select               parents selector
     * @param construct            specimens constructor (creates decision double-vectors)
     * @param evaluate             specimens evaluator (evaluates decision double-vectors)
     * @param reproduce            specimens reproducer (creates offspring decision double-vectors)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(IRandom R,
                                     IGoal[] goals,
                                     Criteria criteria,
                                     ISelect select,
                                     DoubleConstruct.IConstruct construct,
                                     DoubleEvaluate.IEvaluate evaluate,
                                     DoubleReproduce.IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(R, goals, criteria, select, construct, evaluate, reproduce, assignmentResolveTie, specimenResolveTie, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param criteria             criteria
     * @param select               parents selector
     * @param construct            specimens constructor (creates decision double-vectors)
     * @param evaluate             specimens evaluator (evaluates decision double-vectors)
     * @param reproduce            specimens reproducer (creates offspring decision double-vectors)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(IRandom R,
                                     IGoal[] goals,
                                     Criteria criteria,
                                     ISelect select,
                                     DoubleConstruct.IConstruct construct,
                                     DoubleEvaluate.IEvaluate evaluate,
                                     DoubleReproduce.IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(true, false, R, goals, criteria, select, construct,
                evaluate, reproduce, assignmentResolveTie, specimenResolveTie, osAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0.
     *
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param useNadirIncumbent    if true, nadir incumbent will be used when updating OS
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param criteria             criteria
     * @param select               parents selector
     * @param construct            specimens constructor (creates decision double-vectors)
     * @param evaluate             specimens evaluator (evaluates decision double-vectors)
     * @param reproduce            specimens reproducer (creates offspring decision double-vectors)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     Criteria criteria,
                                     ISelect select,
                                     DoubleConstruct.IConstruct construct,
                                     DoubleEvaluate.IEvaluate evaluate,
                                     DoubleReproduce.IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), assignmentResolveTie,
                specimenResolveTie, osAdjuster);
    }


    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param criteria             criteria
     * @param select               parents selector
     * @param construct            specimens constructor
     * @param evaluate             specimens evaluator
     * @param reproduce            specimens reproducer
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(IRandom R,
                                     IGoal[] goals,
                                     Criteria criteria,
                                     ISelect select,
                                     IConstruct construct,
                                     IEvaluate evaluate,
                                     IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(0, true, false, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                select, construct, evaluate, reproduce, assignmentResolveTie, specimenResolveTie);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria)
     * @param select               parents selector
     * @param construct            specimens constructor
     * @param evaluate             specimens evaluator
     * @param reproduce            specimens reproducer
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ISelect select,
                                     IConstruct construct,
                                     IEvaluate evaluate,
                                     IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(0, true, false, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie);
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
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie, null);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the OS will be updated dynamically; false = it will be fixed
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed))
     * @param select               parents selector
     * @param construct            specimens constructor
     * @param evaluate             specimens evaluator
     * @param reproduce            specimens reproducer
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ISelect select,
                                     IConstruct construct,
                                     IEvaluate evaluate,
                                     IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie, osAdjuster);
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
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return NSGA-III algorithm
     */
    protected static NSGAIII getNSGAIII(int id,
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
                                        ISpecimenResolveTie specimenResolveTie,
                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
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
            pOS._updateUtopiaUsingIncumbent = false;
            pOS._updateNadirUsingIncumbent = useNadirIncumbent;
            if (osAdjuster != null) osAdjuster.adjust(pOS);
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

        NSGAIII nsgaiii = new NSGAIII(pEA);
        nsgaiii._goalsManager = pB._goalsManager;
        return nsgaiii;
    }

    /**
     * Auxiliary method for adjusting the optimization goals (thus, population size and other relevant fields).
     * Use with caution. It should not be invoked when executing an initialization or a generation but between
     * these steps. The method does not explicitly extend the population array in
     * {@link population.SpecimensContainer#getPopulation()} nor truncate it. However, the default implementation of
     * phases allows for automatically adapting to new population sizes during evolution.
     *
     * @param goals new optimization goals (the method terminates if null or empty)
     */
    public void adjustOptimizationGoals(IGoal[] goals)
    {
        if ((goals == null) || (goals.length == 0)) return;
        setPopulationSize(goals.length);
        setOffspringSize(goals.length);
        _goalsManager.restructure(new IGoal[][]{goals});
    }
}
