package emo.aposteriori.moead;

import criterion.Criteria;
import ea.EA;
import ea.IEA;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import emo.utils.decomposition.similarity.ISimilarity;
import exception.EAException;
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
 * Provides means for creating an instance of MOEA/D.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class MOEAD extends EA implements IEA
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
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        return getMOEAD(0, updateOSDynamically, false, R,
                goals, problem, similarity, neighborhoodSize, (MOEADBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, false, R, goals, problem,
                similarity, neighborhoodSize, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        return getMOEAD(id, updateOSDynamically, false,
                R, goals, problem, similarity, neighborhoodSize, null, null);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(id, updateOSDynamically, false,
                R, goals, problem, similarity, neighborhoodSize, null, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent, R, goals,
                problem, similarity, neighborhoodSize, (MOEADBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent, R,
                goals, problem, similarity, neighborhoodSize, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
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
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem,
                similarity, neighborhoodSize, null, null);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals,
                problem, similarity, neighborhoodSize, null, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, R, goals, problem, similarity,
                neighborhoodSize, osAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, R, goals, problem, similarity,
                neighborhoodSize, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getMOEAD(id, updateOSDynamically, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce,
                similarity, neighborhoodSize, osAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getMOEAD(id, updateOSDynamically, false, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity, neighborhoodSize,
                osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent,
                R, goals, problem, similarity, neighborhoodSize, osAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and the method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent,
                R, goals, problem, similarity, neighborhoodSize, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, similarity,
                neighborhoodSize, osAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm. The method is coupled with the random selection of parents.
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
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                problem._construct, problem._evaluate, problem._reproduce,
                similarity, neighborhoodSize, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0, couples the method with the random selection of parents, and
     * parameterizes it to update the OS dynamically.
     *
     * @param R                the RGN
     * @param goals            optimization goals
     * @param criteria         criteria
     * @param construct        specimens constructor
     * @param evaluate         specimens evaluator
     * @param reproduce        specimens reproducer
     * @param similarity       object used to quantify similarity between two optimization goals
     * @param neighborhoodSize neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(IRandom R,
                                 IGoal[] goals,
                                 Criteria criteria,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        return getMOEAD(0, true, false, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                new Random(), new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), similarity, neighborhoodSize, (MOEADBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0, couples the method with the random selection of parents, and
     * parameterizes it to update the OS dynamically.
     *
     * @param R                the RGN
     * @param goals            optimization goals
     * @param criteria         criteria
     * @param construct        specimens constructor
     * @param evaluate         specimens evaluator
     * @param reproduce        specimens reproducer
     * @param similarity       object used to quantify similarity between two optimization goals
     * @param neighborhoodSize neighborhood size for MOEA/D
     * @param bundleAdjuster   if provided, it is used to adjust the {@link MOEADBundle.Params} instance being created
     *                         by this method to instantiate the MOEA/D algorithm; adjustment is done after the default
     *                         initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(IRandom R,
                                 IGoal[] goals,
                                 Criteria criteria,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, true, false, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                new Random(), new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), similarity, neighborhoodSize, bundleAdjuster);
    }


    /**
     * Creates the MOEA/D algorithm. Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param R                the RGN
     * @param goals            optimization goals
     * @param criteria         criteria
     * @param select           parents selector
     * @param construct        specimens constructor
     * @param evaluate         specimens evaluator
     * @param reproduce        specimens reproducer
     * @param similarity       object used to quantify similarity between two optimization goals
     * @param neighborhoodSize neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(IRandom R,
                                 IGoal[] goals,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        return getMOEAD(0, true, false, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, (MOEADBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the MOEA/D algorithm.  Sets id to 0 and parameterizes the method to update the OS dynamically.
     *
     * @param R                the RGN
     * @param goals            optimization goals
     * @param criteria         criteria
     * @param select           parents selector
     * @param construct        specimens constructor
     * @param evaluate         specimens evaluator
     * @param reproduce        specimens reproducer
     * @param similarity       object used to quantify similarity between two optimization goals
     * @param neighborhoodSize neighborhood size for MOEA/D
     * @param bundleAdjuster   if provided, it is used to adjust the {@link MOEADBundle.Params} instance being created
     *                         by this method to instantiate the MOEA/D algorithm; adjustment is done after the default
     *                         initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(IRandom R,
                                 IGoal[] goals,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, true, false, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm.
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
     * @param criteria            criteria
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize)
    {
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, (MOEADBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the MOEA/D algorithm.
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
     * @param criteria            criteria
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(int id,
                                 boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 Criteria criteria,
                                 ISelect select,
                                 DoubleConstruct.IConstruct construct,
                                 DoubleEvaluate.IEvaluate evaluate,
                                 DoubleReproduce.IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                similarity, neighborhoodSize, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D.
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
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
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, similarity, neighborhoodSize, (MOEADBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    public static MOEAD getMOEAD(boolean updateOSDynamically,
                                 boolean useNadirIncumbent,
                                 IRandom R,
                                 IGoal[] goals,
                                 AbstractMOOProblemBundle problem,
                                 ISelect select,
                                 IConstruct construct,
                                 IEvaluate evaluate,
                                 IReproduce reproduce,
                                 ISimilarity similarity,
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct, evaluate,
                reproduce, similarity, neighborhoodSize, bundleAdjuster);
    }


    /**
     * Creates the MOEA/D algorithm.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @return the MOEA/D algorithm
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
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, null, null);
    }


    /**
     * Creates the MOEA/D algorithm.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
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
                                 int neighborhoodSize,
                                 MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, null, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the MOEA/D algorithm
     */
    protected static MOEAD getMOEAD(boolean updateOSDynamically,
                                    boolean useNadirIncumbent,
                                    IRandom R,
                                    IGoal[] goals,
                                    AbstractMOOProblemBundle problem,
                                    ISelect select,
                                    IConstruct construct,
                                    IEvaluate evaluate,
                                    IReproduce reproduce,
                                    ISimilarity similarity,
                                    int neighborhoodSize,
                                    ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                construct, evaluate, reproduce, similarity, neighborhoodSize, osAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm. Sets id to 0.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    protected static MOEAD getMOEAD(boolean updateOSDynamically,
                                    boolean useNadirIncumbent,
                                    IRandom R,
                                    IGoal[] goals,
                                    AbstractMOOProblemBundle problem,
                                    ISelect select,
                                    IConstruct construct,
                                    IEvaluate evaluate,
                                    IReproduce reproduce,
                                    ISimilarity similarity,
                                    int neighborhoodSize,
                                    ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                    MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(0, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the MOEA/D algorithm.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the MOEA/D algorithm
     */
    protected static MOEAD getMOEAD(int id,
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
                                    int neighborhoodSize,
                                    ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select,
                construct, evaluate, reproduce, similarity, neighborhoodSize, osAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @return the MOEA/D algorithm
     */
    protected static MOEAD getMOEAD(int id,
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
                                    int neighborhoodSize,
                                    ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                    MOEADBundle.IParamsAdjuster bundleAdjuster)
    {
        return getMOEAD(id, updateOSDynamically, useNadirIncumbent, R, goals, problem, select, construct,
                evaluate, reproduce, similarity, neighborhoodSize, osAdjuster, bundleAdjuster, null);
    }

    /**
     * Creates the MOEA/D algorithm.
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
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param similarity          object used to quantify similarity between two optimization goals
     * @param neighborhoodSize    neighborhood size for MOEA/D
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link MOEADBundle.Params} instance being
     *                            created by this method to instantiate the MOEA/D algorithm; adjustment is done after
     *                            the default initialization
     * @param eaParamsAdjuster    if provided, it is used to adjust the {@link EA.Params} instance being created by this
     *                            method to instantiate the MOEA/D algorithm; adjustment is done after the default
     *                            initialization
     * @return the MOEA/D algorithm
     */
    protected static MOEAD getMOEAD(int id,
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
                                    int neighborhoodSize,
                                    ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                    MOEADBundle.IParamsAdjuster bundleAdjuster,
                                    EA.IParamsAdjuster eaParamsAdjuster)
    {
        MOEADBuilder moeadBuilder = new MOEADBuilder(R);
        moeadBuilder.setGoals(goals);
        moeadBuilder.setSimilarity(similarity);
        moeadBuilder.setNeighborhoodSize(neighborhoodSize);
        moeadBuilder.setAlloc(new Uniform());
        moeadBuilder.setCriteria(problem._criteria);
        if (updateOSDynamically)
        {
            moeadBuilder.setDynamicOSBoundsLearningPolicy();
            moeadBuilder.setOSMParamsAdjuster(osAdjuster);
            moeadBuilder.setUseNadirIncumbent(useNadirIncumbent);
        }
        else moeadBuilder.setFixedOSBoundsLearningPolicy(problem);

        moeadBuilder.setParentsSelector(select);
        moeadBuilder.setInitialPopulationConstructor(construct);
        moeadBuilder.setParentsReproducer(reproduce);
        moeadBuilder.setSpecimensEvaluator(evaluate);
        moeadBuilder.setName("MOEA/D");
        moeadBuilder.setID(id);
        moeadBuilder.setMOEADParamsAdjuster(bundleAdjuster);
        moeadBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getMOEAD(moeadBuilder);
    }

    /**
     * Creates the MOEA/D algorithm using {@link MOEADBuilder}.
     *
     * @param moeadBuilder MOEA/D builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                     {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are
     *                     initialized as imposed by the specified  configuration; also note that the adjusters give
     *                     greater access to the data being instantiated and, thus, the validity of custom adjustments
     *                     is typically unchecked and may lead to errors
     * @return the MOEA/D algorithm
     */
    protected static MOEAD getMOEAD(MOEADBuilder moeadBuilder)
    {
        MOEADGoalsManager.Params pManager = new MOEADGoalsManager.Params(moeadBuilder.getGoals(),
                moeadBuilder.getSimilarity(), moeadBuilder.getNeighborhoodSize());
        pManager._alloc = moeadBuilder.getAlloc();
        MOEADGoalsManager manager = new MOEADGoalsManager(pManager);

        // Instantiate the bundle:
        MOEADBundle.Params pB = new MOEADBundle.Params(moeadBuilder.getCriteria(), manager);

        // Parameterize depending on the ``update OS dynamically'' flag.

        if (moeadBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = moeadBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = moeadBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = moeadBuilder.shouldUseNadirIncumbent();
            if ((moeadBuilder.getUtopia() != null) && (moeadBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(moeadBuilder.getUtopia(), moeadBuilder.getNadir());
            if (moeadBuilder.getOSMParamsAdjuster() != null) moeadBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = moeadBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(moeadBuilder.getUtopia(), moeadBuilder.getNadir());
            manager.updateNormalizations(pB._initialNormalizations); // update normalizations
        }

        pB._select = moeadBuilder.getParentsSelector();
        pB._construct = moeadBuilder.getInitialPopulationConstructor();
        pB._reproduce = moeadBuilder.getParentsReproducer();
        pB._evaluate = moeadBuilder.getSpecimensEvaluator();
        pB._name = moeadBuilder.getName();

        // Instantiate the bundle:
        if (moeadBuilder.getMOEADParamsAdjuster() != null) moeadBuilder.getMOEADParamsAdjuster().adjust(pB);
        MOEADBundle moeadBundle = new MOEADBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(moeadBuilder.getCriteria(), moeadBundle);
        pEA._populationSize = moeadBuilder.getGoals().length;
        pEA._offspringSize = 1; // Important: offspring size = 1
        pEA._expectedNumberOfSteadyStateRepeats = pEA._populationSize;
        pEA._R = moeadBuilder.getR();
        pEA._id = moeadBuilder.getID();
        if (moeadBuilder.getEAParamsAdjuster() != null)
            moeadBuilder.getEAParamsAdjuster().adjust(pEA);

        MOEAD moead = new MOEAD(pEA);
        moead._goalsManager = pB._goalsManager;
        return moead;
    }


    /**
     * Auxiliary method for adjusting the optimization goals (thus, population size and other relevant fields). Use with
     * caution. It should not be invoked when executing an initialization or a generation but between these steps (not
     * even between steady-state repeats). The method does not explicitly extend the population array in
     * {@link population.SpecimensContainer#getPopulation()} nor truncate it. However, the default implementation of
     * phases allows for automatically adapting to new population sizes during evolution.
     *
     * @param goals      new optimization goals (the method terminates if null or empty)
     * @param similarity similarity measure used to build the neighborhood
     * @throws EAException an exception can be thrown and propagated higher
     */
    public void adjustOptimizationGoals(IGoal[] goals, ISimilarity similarity) throws EAException
    {
        if ((goals == null) || (goals.length == 0)) return;
        if (similarity == null) return;
        setPopulationSize(goals.length);
        setOffspringSize(1);
        updateExpectedNumberOfSteadyStateRepeats(goals.length);
        _goalsManager.restructureAndMakeBestAssignments(new IGoal[][]{goals}, new ISimilarity[]{similarity},
                getSpecimensContainer());
        _goalsManager.determineUpdatesSequence(_R);
    }
}
