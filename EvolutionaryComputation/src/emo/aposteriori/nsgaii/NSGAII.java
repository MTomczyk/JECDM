package emo.aposteriori.nsgaii;

import criterion.Criteria;
import ea.EA;
import ea.IEA;
import emo.aposteriori.nsga.NSGABundle;
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
 * Provides means for creating an instance of NSGA-II.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class NSGAII extends EA implements IEA
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
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem)
    {
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem, (NSGAIIBundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem, bundleAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm. The algorithm is coupled with a random selection.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem)
    {
        return getNSGAII(id, updateOSDynamically, populationSize, R,
                problem, null, null);
    }

    /**
     * Creates the NSGA-II algorithm. The algorithm is coupled with a random selection.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(id, updateOSDynamically, populationSize,
                R, problem, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGAII(0, updateOSDynamically, populationSize,
                R, problem, osAdjuster, null);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(0, updateOSDynamically, populationSize,
                R, problem, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem,
                select, problem._construct, problem._evaluate, problem._reproduce,
                osAdjuster, null);
    }

    /**
     * Creates the NSGA-II algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int id,
                                   boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem,
                select, problem._construct, problem._evaluate,
                problem._reproduce, osAdjuster, bundleAdjuster);
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
     * @return the NSGA-II algorithm
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
                select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                (NSGAIIBundle.IParamsAdjuster) null);
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
     * @param bundleAdjuster if provided, it is used to adjust the {@link NSGABundle.Params} instance being created by
     *                       this method to instantiate the NSGA-II algorithm; adjustment is done after the default
     *                       initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   Criteria criteria,
                                   ISelect select,
                                   DoubleConstruct.IConstruct construct,
                                   DoubleEvaluate.IEvaluate evaluate,
                                   DoubleReproduce.IReproduce reproduce,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(0, true, populationSize, R, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                bundleAdjuster);
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
     * @return the NSGA-II algorithm
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
                select, construct, evaluate, reproduce, (NSGAIIBundle.IParamsAdjuster) null);
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
     * @param bundleAdjuster if provided, it is used to adjust the {@link NSGABundle.Params} instance being created by
     *                       this method to instantiate the NSGA-II algorithm; adjustment is done after the default
     *                       initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   Criteria criteria,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(0, true, populationSize, R, MOOProblemBundle.getProblemBundle(criteria),
                select, construct, evaluate, reproduce, bundleAdjuster);
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
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce)
    {
        return getNSGAII(0, true, populationSize, R,
                problem, select, construct, evaluate, reproduce,
                (NSGAIIBundle.IParamsAdjuster) null);
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
     * @param bundleAdjuster if provided, it is used to adjust the {@link NSGABundle.Params} instance being created by
     *                       this method to instantiate the NSGA-II algorithm; adjustment is done after the default
     *                       initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(0, true, populationSize, R, problem,
                select, construct, evaluate, reproduce, bundleAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @return the NSGA-II algorithm
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
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem,
                select, construct, evaluate, reproduce, null, null);
    }


    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
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
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem,
                select, construct, evaluate, reproduce, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-II algorithm
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
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem,
                select, construct, evaluate, reproduce, osAdjuster, null);
    }

    /**
     * Creates the NSGA-II algorithm. Sets id to 0.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(boolean updateOSDynamically,
                                   int populationSize,
                                   IRandom R,
                                   AbstractMOOProblemBundle problem,
                                   ISelect select,
                                   IConstruct construct,
                                   IEvaluate evaluate,
                                   IReproduce reproduce,
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(0, updateOSDynamically, populationSize, R, problem,
                select, construct, evaluate, reproduce, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA-II algorithm
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
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem, select, construct,
                evaluate, reproduce, osAdjuster, null);
    }

    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @return the NSGA-II algorithm
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
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGAII(id, updateOSDynamically, populationSize, R, problem, select, construct, evaluate, reproduce,
                osAdjuster, bundleAdjuster, null);
    }

    /**
     * Creates the NSGA-II algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA-II algorithm; adjustment is done after the
     *                            default initialization
     * @param eaParamsAdjuster    if provided, it is used to adjust the {@link EA.Params} instance being created by this
     *                            method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA-II algorithm
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
                                   ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                                   NSGAIIBundle.IParamsAdjuster bundleAdjuster,
                                   EA.IParamsAdjuster eaParamsAdjuster)
    {
        NSGAIIBuilder nsgaiiBuilder = new NSGAIIBuilder(R);
        nsgaiiBuilder.setID(id);
        if (updateOSDynamically)
        {
            nsgaiiBuilder.setDynamicOSBoundsLearningPolicy();
            nsgaiiBuilder.setOSMParamsAdjuster(osAdjuster);
        }
        else nsgaiiBuilder.setFixedOSBoundsLearningPolicy(problem);
        nsgaiiBuilder.setCriteria(problem);
        nsgaiiBuilder.setParentsSelector(select);
        nsgaiiBuilder.setProblemImplementations(construct, evaluate, reproduce);
        nsgaiiBuilder.setName("NSGA-II");
        nsgaiiBuilder.setNSGAIIParamsAdjuster(bundleAdjuster);
        nsgaiiBuilder.setPopulationSize(populationSize);
        nsgaiiBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getNSGAII(nsgaiiBuilder);
    }

    /**
     * Creates the NSGA-II algorithm using {@link NSGAIIBuilder}.
     *
     * @param nsgaiiBuilder NSGA-II builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                      {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects
     *                      are initialized as imposed by the specified  configuration; also note that the adjusters
     *                      give greater access to the data being instantiated and, thus, the validity of custom
     *                      adjustments is typically unchecked and may lead to errors
     * @return the NSGA-II algorithm
     */
    public static NSGAII getNSGAII(NSGAIIBuilder nsgaiiBuilder)
    {
        // Instantiate the bundle:
        NSGAIIBundle.Params pB = new NSGAIIBundle.Params(nsgaiiBuilder.getCriteria());

        // Parameterize depending on the ``update OS dynamically'' flag.
        if (nsgaiiBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = nsgaiiBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = nsgaiiBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = nsgaiiBuilder.shouldUseNadirIncumbent();
            if ((nsgaiiBuilder.getUtopia() != null) && (nsgaiiBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(nsgaiiBuilder.getUtopia(), nsgaiiBuilder.getNadir());
            if (nsgaiiBuilder.getOSMParamsAdjuster() != null) nsgaiiBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = nsgaiiBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(nsgaiiBuilder.getUtopia(), nsgaiiBuilder.getNadir());
        }

        pB._select = nsgaiiBuilder.getParentsSelector();
        pB._construct = nsgaiiBuilder.getInitialPopulationConstructor();
        pB._reproduce = nsgaiiBuilder.getParentsReproducer();
        pB._evaluate = nsgaiiBuilder.getSpecimensEvaluator();
        pB._name = nsgaiiBuilder.getName();

        // Instantiate the bundle:
        if (nsgaiiBuilder.getNSGAIIParamsAdjuster() != null) nsgaiiBuilder.getNSGAIIParamsAdjuster().adjust(pB);
        NSGAIIBundle nsgaiiBundle = new NSGAIIBundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(nsgaiiBuilder.getCriteria(), nsgaiiBundle);
        pEA._populationSize = nsgaiiBuilder.getPopulationSize();
        pEA._offspringSize = nsgaiiBuilder.getPopulationSize();
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = nsgaiiBuilder.getR();
        pEA._id = nsgaiiBuilder.getID();
        if (nsgaiiBuilder.getEAParamsAdjuster() != null)
            nsgaiiBuilder.getEAParamsAdjuster().adjust(pEA);
        return new NSGAII(pEA);
    }

    /**
     * Auxiliary method for adjusting the population size. It also suitably alters the offspring size (should equal
     * population size). Use with caution. It should not be invoked when executing an initialization or a generation but
     * between these steps. The method does not explicitly extend the population array in
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
