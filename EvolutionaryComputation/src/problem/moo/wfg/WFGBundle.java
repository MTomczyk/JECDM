package problem.moo.wfg;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.wfg.evaluate.*;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.IMutate;
import reproduction.operators.mutation.PM;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

/**
 * Abstract bundle (container) for default implementations of problem-related phases (construct/reproduce/evaluate, etc.)
 * and OS-related data helpful when solving WFG problems.
 *
 * @author MTomczyk
 */
public class WFGBundle extends AbstractMOOProblemBundle
{
    /**
     * Auxiliary interface for providing crossover operators in-line.
     */
    public interface ICrossoverConstructor
    {
        /**
         * The main method's signature.
         *
         * @param problem problem
         * @param M       the number of objectives
         * @param k       the number of position-related parameters (should be divisible by M - 1)
         * @param l       the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
         * @return crossover operator
         */
        ICrossover getCrossover(Problem problem, int M, int k, int l);
    }

    /**
     * Auxiliary interface for providing mutation operators in-line.
     */
    public interface IMutationConstructor
    {
        /**
         * The main method's signature.
         *
         * @param problem problem
         * @param M       the number of objectives
         * @param k       the number of position-related parameters (should be divisible by M - 1)
         * @param l       the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
         * @return crossover operator
         */
        IMutate getMutation(Problem problem, int M, int k, int l);
    }


    /**
     * Parameterized constructor.
     *
     * @param problem                problem id
     * @param construct              constructs the initial population
     * @param reproduce              creates offspring
     * @param evaluate               evaluates solutions
     * @param displayRanges          display ranges for a test problem used when performing visualization (they do not match the true utopia/nadir points)
     * @param paretoFrontBounds      bounds for the Pareto front
     * @param normalizations         min-max normalizations for a test problem (min = true utopia (or nadir) point, max = true nadir (or utopia) point)
     * @param utopia                 true utopia point for a test problem
     * @param nadir                  true nadir point for a test problem
     * @param optimizationDirections optimization direction flags (for each objective); true indicates that the objective is to be maximized, false otherwise
     * @param criteria               contains reference criteria
     */
    public WFGBundle(Problem problem,
                     IConstruct construct,
                     IReproduce reproduce,
                     IEvaluate evaluate,
                     Range[] displayRanges,
                     Range[] paretoFrontBounds,
                     INormalization[] normalizations,
                     double[] utopia,
                     double[] nadir,
                     boolean[] optimizationDirections,
                     Criteria criteria)
    {
        super(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds, normalizations,
                utopia, nadir, optimizationDirections, criteria);
    }

    /**
     * Getter for the default bundle for DTLZ problems. The number of position- and distance-related parameters
     * is set to default (as imposed by {@link WFGBundle#getRecommendedNOPositionRelatedParameters(Problem, int)} and
     * {@link WFGBundle#getRecommendedNODistanceRelatedParameters(Problem, int)})
     *
     * @param problem problem id
     * @param M       the number of considered objectives
     * @return bundle
     */
    public static WFGBundle getBundle(Problem problem, int M)
    {
        return getBundle(problem, M, getRecommendedNOPositionRelatedParameters(problem, M),
                getRecommendedNODistanceRelatedParameters(problem, M), null, null);
    }

    /**
     * Getter for the default bundle for DTLZ problems. The number of position- and distance-related parameters
     * is set to default (as imposed by {@link WFGBundle#getRecommendedNOPositionRelatedParameters(Problem, int)} and
     * {@link WFGBundle#getRecommendedNODistanceRelatedParameters(Problem, int)})
     *
     * @param problem              problem id
     * @param M                    the number of considered objectives
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static WFGBundle getBundle(Problem problem, int M,
                                      WFGBundle.ICrossoverConstructor crossoverConstructor,
                                      WFGBundle.IMutationConstructor mutationConstructor)
    {
        return getBundle(problem, M, getRecommendedNOPositionRelatedParameters(problem, M),
                getRecommendedNODistanceRelatedParameters(problem, M), crossoverConstructor, mutationConstructor);
    }


    /**
     * Getter for the default bundle for DTLZ problems.
     *
     * @param problem problem id
     * @param M       the number of considered objectives
     * @param k       the number of position-related parameters (should be divisible by M - 1)
     * @param l       the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     * @return bundle
     */
    public static WFGBundle getBundle(Problem problem, int M, int k, int l)
    {
        return getBundle(problem, M, k, l, null, null);
    }

    /**
     * Getter for the default bundle for DTLZ problems.
     *
     * @param problem              problem id
     * @param M                    the number of considered objectives
     * @param k                    the number of position-related parameters (should be divisible by M - 1)
     * @param l                    the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static WFGBundle getBundle(Problem problem, int M, int k, int l,
                                      WFGBundle.ICrossoverConstructor crossoverConstructor,
                                      WFGBundle.IMutationConstructor mutationConstructor)
    {
        WFGEvaluate evaluator;
        switch (problem)
        {
            case WFG1ALPHA02 -> evaluator = new WFG1(M, k, l, 0.2d);
            case WFG1ALPHA025 -> evaluator = new WFG1(M, k, l, 0.25d);
            case WFG1ALPHA05 -> evaluator = new WFG1(M, k, l, 0.5d);
            case WFG2 -> evaluator = new WFG2(M, k, l);
            case WFG3 -> evaluator = new WFG3(M, k, l, true);
            case WFG4 -> evaluator = new WFG4(M, k, l);
            case WFG5 -> evaluator = new WFG5(M, k, l);
            case WFG6 -> evaluator = new WFG6(M, k, l);
            case WFG7 -> evaluator = new WFG7(M, k, l);
            case WFG8 -> evaluator = new WFG8(M, k, l);
            case WFG9 -> evaluator = new WFG9(M, k, l);

            case WFG1EASY -> evaluator = new WFG1Easy(M);
            case WFG2EASY -> evaluator = new WFG2Easy(M);
            case WFG3EASY -> evaluator = new WFG3Easy(M, true);
            case WFG4EASY -> evaluator = new WFG4Easy(M);
            case WFG5EASY -> evaluator = new WFG5Easy(M);
            case WFG6EASY -> evaluator = new WFG6Easy(M);
            case WFG7EASY -> evaluator = new WFG7Easy(M);
            case WFG8EASY -> evaluator = new WFG8Easy(M);
            case WFG9EASY -> evaluator = new WFG9Easy(M);

            default -> evaluator = new WFG1(M, k, l, 0.02d);
        }

        return getBundle(problem, evaluator, M, k, l, crossoverConstructor, mutationConstructor);
    }


    /**
     * Getter for the default bundle for WFG problems.
     *
     * @param problem  problem id
     * @param evaluate evaluator object responsible for evaluating solutions in line with WFG test suite
     * @param M        the number of considered objectives
     * @param k        the number of position-related parameters (should be divisible by M - 1)
     * @param l        the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     * @return bundle
     */
    public static WFGBundle getBundle(Problem problem, WFGEvaluate evaluate, int M, int k, int l)
    {
        return getBundle(problem, evaluate, M, k, l, null, null);
    }

    /**
     * Getter for the default bundle for WFG problems.
     *
     * @param problem              problem id
     * @param evaluate             evaluator object responsible for evaluating solutions in line with WFG test suite
     * @param M                    the number of considered objectives
     * @param k                    the number of position-related parameters (should be divisible by M - 1)
     * @param l                    the number of distance-related parameters (any number, except for WFG3 and 4 for which must be divisible by 2)
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static WFGBundle getBundle(Problem problem, WFGEvaluate evaluate, int M, int k, int l,
                                      WFGBundle.ICrossoverConstructor crossoverConstructor,
                                      WFGBundle.IMutationConstructor mutationConstructor)
    {
        evaluate.instantiateEvaluator();
        IConstruct construct = new Construct(k, l);
        ICrossover crossover;
        if (crossoverConstructor != null) crossover = crossoverConstructor.getCrossover(problem, M, k, l);
        else crossover = new SBX(new SBX.Params(1.0d, 20.0d));
        IMutate mutate;
        if (mutationConstructor != null) mutate = mutationConstructor.getMutation(problem, M, k, l);
        else mutate = new PM(new PM.Params(1.0d / ((double) k + l), 20.0d));
        IReproduce reproduce = new Reproduce(M, k, l, crossover, mutate);
        Range[] displayRanges = getDisplayRanges(problem, M);
        Range[] paretoFrontBounds = getParetoFrontBounds(problem, M);
        INormalization[] normalizations = getNormalizations(problem, M);
        double[] utopia = getUtopia(problem, M);
        double[] nadir = getNadir(problem, M);
        return new WFGBundle(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds,
                normalizations, utopia, nadir, new boolean[M], Criteria.constructCriteria("C", M, false));
    }


    /**
     * Supportive method. Returns display ranges for WFG1-9 problems used when performing visualization (they do not
     * match the true utopia/nadir points).
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return bounds for the Pareto front
     */
    public static Range[] getDisplayRanges(Problem problem, int M)
    {
        Range[] ranges = new Range[M];
        if ((problem.equals(Problem.WFG3)) && (M > 2))
        {
            for (int i = 0; i < M - 1; i++) ranges[i] = new Range(0.0d, (i + 1) + 0.5d);
            ranges[M - 1] = new Range(0.0d, 2 * M + 0.5d);
        }
        else for (int i = 0; i < M; i++) ranges[i] = new Range(0.0d, 2 * (i + 1) + 0.5d);
        return ranges;
    }

    /**
     * Returns the Pareto front bounds.
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return bounds for the Pareto front
     */
    public static Range[] getParetoFrontBounds(Problem problem, int M)
    {
        Range[] ranges = new Range[M];
        if ((problem.equals(Problem.WFG3)) && (M > 2))
        {
            for (int i = 0; i < M - 1; i++) ranges[i] = new Range(0.0d, (i + 1));
            ranges[M - 1] = new Range(0.0d, 2 * M);
        }
        else for (int i = 0; i < M; i++) ranges[i] = new Range(0.0d, 2 * (i + 1));
        return ranges;
    }

    /**
     * Returns min-max normalizations for WFG1-9 problems (min = true utopia point, max = true nadir point).
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return min-max normalizations
     */
    public static INormalization[] getNormalizations(Problem problem, int M)
    {
        INormalization[] normalizations = new INormalization[M];
        if ((problem.equals(Problem.WFG3)) && (M > 2))
        {
            for (int i = 0; i < M - 1; i++) normalizations[i] = new Linear(0.0d, (i + 1));
            normalizations[M - 1] = new Linear(0.0d, 2 * M);
        }
        else for (int i = 0; i < M; i++) normalizations[i] = new Linear(0.0d, 2 * (i + 1));
        return normalizations;
    }

    /**
     * Returns true utopia point for WFG1-9 problems.
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return utopia point
     */
    public static double[] getUtopia(Problem problem, int M)
    {
        return new double[M];
    }

    /**
     * Returns true nadir point for WFG1-9 problems.
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return nadir point
     */
    public static double[] getNadir(Problem problem, int M)
    {
        double[] n = new double[M];
        if ((problem.equals(Problem.WFG3)) && (M > 2))
        {
            for (int i = 0; i < M - 1; i++) n[i] = (i + 1);
            n[M - 1] = 2 * M;
        }
        else for (int i = 0; i < M; i++) n[i] = 2 * (i + 1);
        return n;
    }

    /**
     * Auxiliary method that returns a dedicated number of position-related parameters for the problem based on the
     * number of objectives.
     *
     * @param problem problem ID
     * @param M       the number of objectives
     * @return the recommended number of position-related parameters
     */
    public static int getRecommendedNOPositionRelatedParameters(Problem problem, int M)
    {
        return 2 * (M - 1);
    }

    /**
     * Auxiliary method that returns a dedicated number of distance-related parameters for the problem based on the
     * number of objectives.
     *
     * @param problem problem ID
     * @param M       the number of objectives
     * @return the recommended number of distance-related parameters
     */
    public static int getRecommendedNODistanceRelatedParameters(Problem problem, int M)
    {
        return 20;
    }


}
