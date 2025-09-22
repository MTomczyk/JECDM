package emo.aposteriori.nsga;

import criterion.Criteria;
import ea.EA;
import ea.IEA;
import os.ObjectiveSpace;
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
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem)
    {
        return getNSGA(0, updateOSDynamically, threshold,
                populationSize, R, problem, (NSGABundle.IParamsAdjuster) null);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize,
                R, problem, bundleAdjuster);
    }

    /**
     * Creates the NSGA algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem)
    {
        return getNSGA(id, updateOSDynamically, threshold, populationSize,
                R, problem, null, null);
    }

    /**
     * Creates the NSGA algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(id, updateOSDynamically, threshold,
                populationSize, R, problem, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize,
                R, problem, osAdjuster, null);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0 and the algorithm is coupled with the random selection of parents.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize,
                R, problem, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @return the NSGA algorithm
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
                problem._construct, problem._evaluate, problem._reproduce, osAdjuster, null);
    }

    /**
     * Creates the NSGA algorithm. The algorithm is coupled with the random selection of parents.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria, normalizations (when fixed), specimen constructor,
     *                            evaluator, and reproducer)
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(int id,
                               boolean updateOSDynamically,
                               double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        ISelect select = new Random(2);
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select,
                problem._construct, problem._evaluate, problem._reproduce, osAdjuster, bundleAdjuster);
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
     * @return the NSGA algorithm
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
                select, new DoubleConstruct(construct),
                new DoubleEvaluate(evaluate), new DoubleReproduce(reproduce),
                (NSGABundle.IParamsAdjuster) null);
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
     * @param bundleAdjuster if provided, it is used to adjust the {@link NSGABundle.Params} instance being created by
     *                       this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                       initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(double threshold,
                               int populationSize,
                               IRandom R,
                               Criteria criteria,
                               ISelect select,
                               DoubleConstruct.IConstruct construct,
                               DoubleEvaluate.IEvaluate evaluate,
                               DoubleReproduce.IReproduce reproduce,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(0, true, threshold, populationSize, R, MOOProblemBundle.getProblemBundle(criteria),
                select, new DoubleConstruct(construct), new DoubleEvaluate(evaluate),
                new DoubleReproduce(reproduce), bundleAdjuster);
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
     * @return the NSGA algorithm
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
        return getNSGA(threshold, populationSize, R, MOOProblemBundle.getProblemBundle(criteria), select,
                construct, evaluate, reproduce, null);
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
     * @param bundleAdjuster if provided, it is used to adjust the {@link NSGABundle.Params} instance being created by
     *                       this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                       initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(double threshold,
                               int populationSize,
                               IRandom R,
                               Criteria criteria,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(threshold, populationSize, R, MOOProblemBundle.getProblemBundle(criteria), select,
                construct, evaluate, reproduce, bundleAdjuster);
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
     * @return the NSGA algorithm
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
                evaluate, reproduce, null, null);
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
     * @param bundleAdjuster if provided, it is used to adjust the {@link NSGABundle.Params} instance being created by
     *                       this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                       initialization
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(double threshold,
                               int populationSize,
                               IRandom R,
                               AbstractMOOProblemBundle problem,
                               ISelect select,
                               IConstruct construct,
                               IEvaluate evaluate,
                               IReproduce reproduce,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(0, true, threshold, populationSize, R, problem, select, construct,
                evaluate, reproduce, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproduce
     * @return the NSGA algorithm
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
                evaluate, reproduce, null, null);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproduce
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
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
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select, construct,
                evaluate, reproduce, null, bundleAdjuster);
    }

    /**
     * Creates the NSGA algorithm. Sets id to 0.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param reproduce           specimens reproducer
     * @return the NSGA algorithm
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
     * Creates the NSGA algorithm. Sets id to 0.
     *
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
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
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(0, updateOSDynamically, threshold, populationSize, R, problem, select, construct, evaluate,
                reproduce, osAdjuster, bundleAdjuster);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param reproduce           specimens reproducer
     * @return the NSGA algorithm
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
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select, construct,
                evaluate, reproduce, osAdjuster, null);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
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
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                               NSGABundle.IParamsAdjuster bundleAdjuster)
    {
        return getNSGA(id, updateOSDynamically, threshold, populationSize, R, problem, select, construct,
                evaluate, reproduce, osAdjuster, bundleAdjuster, null);
    }

    /**
     * Creates the NSGA algorithm.
     *
     * @param id                  algorithm id
     * @param updateOSDynamically if true, the data on the known Pareto front bounds will be updated dynamically; false:
     *                            the data is assumed fixed (suitable normalization functions must be provided when
     *                            instantiating the EA); if fixed, the objective space manager will not be instantiated
     *                            by default, and the normalizations will be directly passed to interested components
     * @param threshold           threshold for distances when calculating niche count values
     * @param populationSize      population size
     * @param R                   the RGN
     * @param problem             problem bundle (provides criteria and normalizations (when fixed))
     * @param select              parents selector
     * @param construct           specimens constructor
     * @param evaluate            specimens evaluator
     * @param reproduce           specimens reproducer
     * @param osAdjuster          auxiliary object (can be null) responsible for customizing objective space manager
     *                            params container built when the method is expected to update its known bounds on the
     *                            objective space dynamically (otherwise, it is possible that the manager will be null;
     *                            the adjuster is not used).
     * @param bundleAdjuster      if provided, it is used to adjust the {@link NSGABundle.Params} instance being created
     *                            by this method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @param eaParamsAdjuster    if provided, it is used to adjust the {@link EA.Params} instance being created by this
     *                            method to instantiate the NSGA algorithm; adjustment is done after the default
     *                            initialization
     * @return the NSGA algorithm
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
                               ObjectiveSpaceManager.IParamsAdjuster osAdjuster,
                               NSGABundle.IParamsAdjuster bundleAdjuster,
                               EA.IParamsAdjuster eaParamsAdjuster)
    {
        NSGABuilder nsgaBuilder = new NSGABuilder(R);
        nsgaBuilder.setID(id);
        nsgaBuilder.setThreshold(threshold);
        if (updateOSDynamically)
        {
            nsgaBuilder.setDynamicOSBoundsLearningPolicy();
            nsgaBuilder.setOSMParamsAdjuster(osAdjuster);
        }
        else nsgaBuilder.setFixedOSBoundsLearningPolicy(problem);
        nsgaBuilder.setCriteria(problem);
        nsgaBuilder.setParentsSelector(select);
        nsgaBuilder.setProblemImplementations(construct, evaluate, reproduce);
        nsgaBuilder.setName("NSGA");
        nsgaBuilder.setNSGAParamsAdjuster(bundleAdjuster);
        nsgaBuilder.setPopulationSize(populationSize);
        nsgaBuilder.setEAParamsAdjuster(eaParamsAdjuster);
        return getNSGA(nsgaBuilder);
    }

    /**
     * Creates the NSGA algorithm using {@link NSGABuilder}.
     *
     * @param nsgaBuilder NSGA builder to be used; note that the auxiliary adjuster objects (e.g.,
     *                    {@link os.ObjectiveSpaceManager.IParamsAdjuster}) are employed after the relevant objects are
     *                    initialized as imposed by the specified  configuration; also note that the adjusters give
     *                    greater access to the data being instantiated and, thus, the validity of custom adjustments is
     *                    typically unchecked and may lead to errors
     * @return the NSGA algorithm
     */
    public static NSGA getNSGA(NSGABuilder nsgaBuilder)
    {
        // Instantiate the bundle:
        NSGABundle.Params pB = new NSGABundle.Params(nsgaBuilder.getCriteria());
        // Select the distance function:
        pB._distance = new Euclidean();
        // Distance threshold:
        pB._th = nsgaBuilder.getThreshold();
        // Parameterize depending on the ``update OS dynamically'' flag.
        if (nsgaBuilder.shouldUpdateOSDynamically())
        {
            // No initial normalizations:
            pB._initialNormalizations = null;
            ObjectiveSpaceManager.Params pOS = new ObjectiveSpaceManager.Params();
            pOS._criteria = nsgaBuilder.getCriteria();
            // Default incumbent strategy:
            pOS._updateUtopiaUsingIncumbent = nsgaBuilder.shouldUseUtopiaIncumbent();
            pOS._updateNadirUsingIncumbent = nsgaBuilder.shouldUseNadirIncumbent();
            if ((nsgaBuilder.getUtopia() != null) && (nsgaBuilder.getNadir() != null))
                pOS._os = new ObjectiveSpace(nsgaBuilder.getUtopia(), nsgaBuilder.getNadir());
            if (nsgaBuilder.getOSMParamsAdjuster() != null) nsgaBuilder.getOSMParamsAdjuster().adjust(pOS);
            pB._osManager = new ObjectiveSpaceManager(pOS);
        }
        else
        {
            // Set the initial normalizations
            pB._initialNormalizations = nsgaBuilder.getInitialNormalizations();
            pB._osManager = ObjectiveSpaceManager.getFixedInstance(nsgaBuilder.getUtopia(), nsgaBuilder.getNadir());
            pB._distance.setNormalizations(nsgaBuilder.getInitialNormalizations());
        }

        pB._select = nsgaBuilder.getParentsSelector();
        pB._construct = nsgaBuilder.getInitialPopulationConstructor();
        pB._reproduce = nsgaBuilder.getParentsReproducer();
        pB._evaluate = nsgaBuilder.getSpecimensEvaluator();
        pB._name = nsgaBuilder.getName();

        // Instantiate the bundle:
        if (nsgaBuilder.getNSGAParamsAdjuster() != null) nsgaBuilder.getNSGAParamsAdjuster().adjust(pB);
        NSGABundle nsgaBundle = new NSGABundle(pB);

        // Create EA:
        EA.Params pEA = new EA.Params(nsgaBuilder.getCriteria(), nsgaBundle);
        pEA._populationSize = nsgaBuilder.getPopulationSize();
        pEA._offspringSize = nsgaBuilder.getPopulationSize();
        pEA._expectedNumberOfSteadyStateRepeats = 1;
        pEA._R = nsgaBuilder.getR();
        pEA._id = nsgaBuilder.getID();
        if (nsgaBuilder.getEAParamsAdjuster() != null)
            nsgaBuilder.getEAParamsAdjuster().adjust(pEA);

        NSGA nsga = new NSGA(pEA);
        nsga._bundle = nsgaBundle;
        return nsga;
    }

    /**
     * Auxiliary method for adjusting the population size. It also suitably alters the offspring size (should equal
     * population size). Use with caution. It should not be invoked when executing an initialization or a generation but
     * between these steps. The method does not explicitly extend the population array in
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
