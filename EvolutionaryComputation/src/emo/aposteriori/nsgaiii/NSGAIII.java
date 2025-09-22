package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import ea.EA;
import ea.IEA;
import emo.aposteriori.nsga.NSGABundle;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.*;
import os.ObjectiveSpace;
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
public class NSGAIII extends EA implements IEA
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
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(0, updateOSDynamically, false, R, goals, problem, (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically,
                false, R, goals, problem, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(id, updateOSDynamically, false, R,
                goals, problem, (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem)
    {
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, null,
                (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent,
                R, goals, problem, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-III algorithm
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
                problem._construct, problem._evaluate, problem._reproduce,
                new RandomAssignment(), new RandomSpecimen(), osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce,
                new RandomAssignment(), new RandomSpecimen(), osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals,
                problem, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals, problem,
                osAdjuster, bundleAdjuster);
    }


    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-III algorithm
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
                problem._construct, problem._evaluate, problem._reproduce,
                new RandomAssignment(), new RandomSpecimen(), osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param useNadirIncumbent   if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param R                   the RGN
     * @param goals               optimization goals used to steer the evolution; they also determine the population
     *                            size
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-III algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, new RandomAssignment(),
                new RandomSpecimen(), osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, assignmentResolveTie, specimenResolveTie,
                (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, assignmentResolveTie, specimenResolveTie,
                bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem,
                assignmentResolveTie, specimenResolveTie, (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem,
                assignmentResolveTie, specimenResolveTie, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals,
                problem, assignmentResolveTie, specimenResolveTie, (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals,
                problem, assignmentResolveTie, specimenResolveTie, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return the NSGA-III algorithm
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
                problem._construct, problem._evaluate, problem._reproduce,
                assignmentResolveTie, specimenResolveTie, (NSGAIIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce,
                assignmentResolveTie, specimenResolveTie, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets the id to 0 the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, assignmentResolveTie,
                specimenResolveTie, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets the id to 0 the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, R, goals, problem, assignmentResolveTie,
                specimenResolveTie, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return the NSGA-III algorithm
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
        return getNSGAIII(id, updateOSDynamically, false, R, goals,
                problem, assignmentResolveTie, specimenResolveTie, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem,
                assignmentResolveTie, specimenResolveTie, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return the NSGA-III algorithm
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
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals,
                problem, assignmentResolveTie, specimenResolveTie, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically,
                useNadirIncumbent, R, goals, problem,
                assignmentResolveTie, specimenResolveTie,
                osAdjuster, bundleAdjuster);
    }


    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @return the NSGA-III algorithm
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
                problem._construct, problem._evaluate,
                problem._reproduce, assignmentResolveTie,
                specimenResolveTie, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                             evaluator, and reproducer)
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param osAdjuster           auxiliary object responsible for customizing objective space manager params container
     *                             built when is set to updateOSDynamically (can be null; not used)
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(int id,
                                     boolean updateOSDynamically,
                                     boolean useNadirIncumbent,
                                     IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate,
                problem._reproduce, assignmentResolveTie,
                specimenResolveTie, osAdjuster, bundleAdjuster);
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
     * @return the NSGA-III algorithm
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
        return getNSGAIII(R, goals, criteria, select, construct,
                evaluate, reproduce, assignmentResolveTie,
                specimenResolveTie, null, null);
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
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
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(R, goals, criteria, select, construct,
                evaluate, reproduce, assignmentResolveTie,
                specimenResolveTie, null, bundleAdjuster);
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
     * @return the NSGA-III algorithm
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
                evaluate, reproduce, assignmentResolveTie,
                specimenResolveTie, osAdjuster, null);
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
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
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(true, false, R, goals, criteria, select, construct,
                evaluate, reproduce, assignmentResolveTie,
                specimenResolveTie, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @return the NSGA-III algorithm
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
                specimenResolveTie, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm. Sets id to 0.
     *
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
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
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, updateOSDynamically, useNadirIncumbent, R, goals,
                MOOProblemBundle.getProblemBundle(criteria), select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce), assignmentResolveTie,
                specimenResolveTie, osAdjuster, bundleAdjuster);
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
     * @return the NSGA-III algorithm
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
                select, construct, evaluate, reproduce, assignmentResolveTie,
                specimenResolveTie, (NSGAIIIBundle.IParamsAdjuster) null);
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(IRandom R,
                                     IGoal[] goals,
                                     Criteria criteria,
                                     ISelect select,
                                     IConstruct construct,
                                     IEvaluate evaluate,
                                     IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, true, false, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                select, construct, evaluate, reproduce,
                assignmentResolveTie, specimenResolveTie, bundleAdjuster);
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
     * @return the NSGA-III algorithm
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
                reproduce, assignmentResolveTie, specimenResolveTie, (NSGAIIIBundle.IParamsAdjuster) null);
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
     */
    public static NSGAIII getNSGAIII(IRandom R,
                                     IGoal[] goals,
                                     AbstractMOOProblemBundle problem,
                                     ISelect select,
                                     IConstruct construct,
                                     IEvaluate evaluate,
                                     IReproduce reproduce,
                                     IAssignmentResolveTie assignmentResolveTie,
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(0, true, false, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed))
     * @param select               parents selector
     * @param construct            specimens constructor
     * @param evaluate             specimens evaluator
     * @param reproduce            specimens reproducer
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @return the NSGA-III algorithm
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
                reproduce, assignmentResolveTie, specimenResolveTie, null, null);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param R                    the RGN
     * @param goals                optimization goals
     * @param problem              problem bundle (provides criteria, normalizations (when fixed))
     * @param select               parents selector
     * @param construct            specimens constructor
     * @param evaluate             specimens evaluator
     * @param reproduce            specimens reproducer
     * @param assignmentResolveTie object resolving the assignment selection ties
     * @param specimenResolveTie   object resolving the specimen selection ties
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
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
                                     ISpecimenResolveTie specimenResolveTie,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie,
                specimenResolveTie, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @return the NSGA-III algorithm
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
                reproduce, assignmentResolveTie,
                specimenResolveTie, osAdjuster, null);
    }


    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
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
                                     ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                     NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, false, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie, osAdjuster,
                bundleAdjuster);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @return the NSGA-III algorithm
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
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie, osAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @return the NSGA-III algorithm
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
                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                        NSGAIIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAIII(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, assignmentResolveTie, specimenResolveTie, osAdjuster, bundleAdjuster, null);
    }

    /**
     * Creates the NSGA-III algorithm.
     *
     * @param id                   algorithm id
     * @param updateOSDynamically  if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
     * @param useNadirIncumbent    if true, the data on the known Pareto front bounds will be updated dynamically;
     *                             false: the data is assumed fixed (suitable normalization functions must be provided
     *                             when instantiating the EA); if fixed, the objective space manager will not be
     *                             instantiated by default, and the normalizations will be directly passed to interested
     *                             components
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
     * @param bundleAdjuster       if provided, it is used to adjust the {@link NSGABundle.Params} instance being
     *                             created by this method to instantiate the NSGA-III algorithm; adjustment is done
     *                             after the default initialization
     * @param eaParamsAdjuster     if provided, it is used to adjust the {@link EA.Params} instance being created by
     *                             this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                             initialization
     * @return the NSGA-III algorithm
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
                                        ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                        NSGAIIIBundle.IParamsAdjuster bundleAdjuster,
                                        EA.IParamsAdjuster eaParamsAdjuster)
    {
        NSGAIIIBuilder nsgaiiiBuilder = new NSGAIIIBuilder(R);
        nsgaiiiBuilder.setGoals(goals);
        nsgaiiiBuilder.setCriteria(problem._criteria);
        nsgaiiiBuilder.setAssigmentResolveTie(assignmentResolveTie);
        nsgaiiiBuilder.setSpecimenResolveTie(specimenResolveTie);
        if (updateOSDynamically)
        {
            nsgaiiiBuilder.setDynamicOSBoundsLearningPolicy();
            nsgaiiiBuilder.setOSMParamsAdjuster(osAdjuster);
            nsgaiiiBuilder.setUseNadirIncumbent(useNadirIncumbent);
        }
        else nsgaiiiBuilder.setFixedOSBoundsLearningPolicy(problem);

        nsgaiiiBuilder.setParentsSelector(select);
        nsgaiiiBuilder.setProblemImplementations(construct, evaluate, reproduce);
        nsgaiiiBuilder.setName("NSGA-III");
        nsgaiiiBuilder.setID(id);
        nsgaiiiBuilder.setPopulationSize(goals.length);
        nsgaiiiBuilder.setNSGAIIIParamsAdjuster(bundleAdjuster);
        nsgaiiiBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getNSGAIII(nsgaiiiBuilder);
    }

    /**
     * Creates the NSGA-III algorithm using {@link NSGAIIIBuilder}.
     *
     * @param nsgaiiiBuilder NSGA-III builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                       {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects
     *                       are initialized as imposed by the specified  configuration; also note that the adjusters
     *                       give greater access to the data being instantiated and, thus, the validity of custom
     *                       adjustments is typically unchecked and may lead to errors
     * @return the NSGA-III algorithm
     */
    protected static NSGAIII getNSGAIII(NSGAIIIBuilder nsgaiiiBuilder)
    {
        NSGAIIIGoalsManager.Params pManager = new NSGAIIIGoalsManager.Params(nsgaiiiBuilder.getGoals());
        NSGAIIIGoalsManager manager = new NSGAIIIGoalsManager(pManager);

        // Instantiate the bundle:
        NSGAIIIBundle.Params pB = new NSGAIIIBundle.Params(nsgaiiiBuilder.getCriteria(),
                manager, nsgaiiiBuilder.getAssignmentResolveTie(), nsgaiiiBuilder.getSpecimenResolveTie());

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (nsgaiiiBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = nsgaiiiBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = nsgaiiiBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = nsgaiiiBuilder.shouldUseNadirIncumbent();
            if ((nsgaiiiBuilder.getUtopia() != null) && (nsgaiiiBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(nsgaiiiBuilder.getUtopia(), nsgaiiiBuilder.getNadir());
            if (nsgaiiiBuilder.getOSMParamsAdjuster() != null) nsgaiiiBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = nsgaiiiBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(nsgaiiiBuilder.getUtopia(), nsgaiiiBuilder.getNadir());
            manager.updateNormalizations(pB._initialNormalizations); // update normalizations
        }

        pB._select = nsgaiiiBuilder.getParentsSelector();
        pB._construct = nsgaiiiBuilder.getInitialPopulationConstructor();
        pB._reproduce = nsgaiiiBuilder.getParentsReproducer();
        pB._evaluate = nsgaiiiBuilder.getSpecimensEvaluator();
        pB._name = nsgaiiiBuilder.getName();

        // Instantiate the bundle:
        if (nsgaiiiBuilder.getNSGAIIIParamsAdjuster() != null) nsgaiiiBuilder.getNSGAIIIParamsAdjuster().adjust(pB);
        NSGAIIIBundle nsgaiiiBundle = new NSGAIIIBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(nsgaiiiBuilder.getCriteria(), nsgaiiiBundle);
        pEA._populationSize = nsgaiiiBuilder.getGoals().length;
        pEA._offspringSize = nsgaiiiBuilder.getGoals().length;
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = nsgaiiiBuilder.getR();
        pEA._id = nsgaiiiBuilder.getID();
        if (nsgaiiiBuilder.getEAParamsAdjuster() != null)
            nsgaiiiBuilder.getEAParamsAdjuster().adjust(pEA);

        NSGAIII nsgaiii = new NSGAIII(pEA);
        nsgaiii._goalsManager = pB._goalsManager;
        return nsgaiii;
    }

    /**
     * Auxiliary method for adjusting the optimization goals (thus, population size and other relevant fields). Use with
     * caution. It should not be invoked when executing an initialization or a generation but between these steps. The
     * method does not explicitly extend the population array in {@link population.SpecimensContainer#getPopulation()}
     * nor truncate it. However, the default implementation of phases allows for automatically adapting to new
     * population sizes during evolution.
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


    /**
     * Setter for the offspring size (protected).
     *
     * @param offspringSize offspring size
     */
    protected void adjustOffspringSize(int offspringSize)
    {
        super.setOffspringSize(offspringSize);
    }
}
