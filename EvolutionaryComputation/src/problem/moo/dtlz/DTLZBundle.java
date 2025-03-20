package problem.moo.dtlz;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.dtlz.evaluate.*;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.IMutate;
import reproduction.operators.mutation.PM;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.Arrays;

/**
 * Abstract bundle (container) for default implementations of problem-related phases (construct/reproduce/evaluate, etc.)
 * and OS-related data helpful when solving DTLZ problems.
 *
 * @author MTomczyk
 */
public class DTLZBundle extends AbstractMOOProblemBundle
{
    /**
     * DTLZ7: auxiliary root used when determining the lower bound for the last objective function.
     * (minimizes h function and maximizes fs)
     */
    private static final double _DTLZ7_const = 0.859400856644724;

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
         * @param D       the number of distance-related parameters
         * @return crossover operator
         */
        ICrossover getCrossover(Problem problem, int M, int D);
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
         * @param D       the number of distance-related parameters
         * @return crossover operator
         */
        IMutate getMutation(Problem problem, int M, int D);
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
     * @param criteria               contains reference criteria objects
     */
    public DTLZBundle(Problem problem,
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
        super(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds, normalizations, utopia, nadir, optimizationDirections, criteria);
    }

    /**
     * Getter for the default bundle for DTLZ problems. Sets the number of distance-related attributes to default
     * (as imposed by {@link DTLZBundle#getRecommendedNODistanceRelatedParameters(Problem, int)}).
     *
     * @param problem problem id
     * @param M       the number of considered objectives
     * @return bundle
     */
    public static DTLZBundle getBundle(Problem problem, int M)
    {
        return getBundle(problem, M, getRecommendedNODistanceRelatedParameters(problem, M), null, null);
    }

    /**
     * Getter for the default bundle for DTLZ problems.
     *
     * @param problem problem id
     * @param M       the number of considered objectives
     * @param D       the number of distance-related variables
     * @return bundle
     */
    public static DTLZBundle getBundle(Problem problem, int M, int D)
    {
        return getBundle(problem, M, D, null, null);
    }

    /**
     * Getter for the default bundle for DTLZ problems. Sets the number of distance-related attributes to default
     * (as imposed by {@link DTLZBundle#getRecommendedNODistanceRelatedParameters(Problem, int)}).
     *
     * @param problem              problem id
     * @param M                    the number of considered objectives
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static DTLZBundle getBundle(Problem problem, int M,
                                       ICrossoverConstructor crossoverConstructor,
                                       IMutationConstructor mutationConstructor)
    {
        return getBundle(problem, M, getRecommendedNODistanceRelatedParameters(problem, M), crossoverConstructor, mutationConstructor);
    }

    /**
     * Getter for the default bundle for DTLZ problems.
     *
     * @param problem              problem id
     * @param M                    the number of considered objectives
     * @param D                    the number of distance-related variable
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static DTLZBundle getBundle(Problem problem, int M, int D,
                                       ICrossoverConstructor crossoverConstructor,
                                       IMutationConstructor mutationConstructor)
    {
        IConstruct construct = new Construct(M, D);
        IEvaluate evaluate;
        switch (problem)
        {
            case DTLZ2 -> evaluate = new DTLZ2(M, D);
            case DTLZ3 -> evaluate = new DTLZ3(M, D);
            case DTLZ4 -> evaluate = new DTLZ4(M, D);
            case DTLZ5 -> evaluate = new DTLZ5(M, D);
            case DTLZ6 -> evaluate = new DTLZ6(M, D);
            case DTLZ7 -> evaluate = new DTLZ7(M, D);

            default -> evaluate = new DTLZ1(M, D);
        }
        ICrossover crossover;
        if (crossoverConstructor != null) crossover = crossoverConstructor.getCrossover(problem, M, D);
        else crossover = new SBX(new SBX.Params(1.0d, 20.0d));

        IMutate mutate;
        if (mutationConstructor != null) mutate = mutationConstructor.getMutation(problem, M, D);
        else mutate = new PM(new PM.Params(1.0d / ((double) D + M - 1), 20.0d));
        IReproduce reproduce = new Reproduce(M, D, crossover, mutate);
        Range[] displayRanges = getDisplayRanges(problem, M);
        Range[] paretoFrontBounds = getParetoFrontBounds(problem, M);
        INormalization[] normalizations = getNormalizations(problem, M);
        double[] utopia = getUtopia(problem, M);
        double[] nadir = getNadir(problem, M);
        return new DTLZBundle(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds,
                normalizations, utopia, nadir, new boolean[M], Criteria.constructCriteria("C", M, false));
    }


    /**
     * Supportive method. Returns display ranges for DTLZ1-7 problems used when performing visualization (they do not
     * match the true utopia/nadir points).
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return bounds for the Pareto front
     */
    public static Range[] getDisplayRanges(Problem problem, int M)
    {
        Range[] ranges = new Range[M];
        if (problem == Problem.DTLZ7)
        {
            for (int i = 0; i < M - 1; i++) ranges[i] = new Range(0.0d, _DTLZ7_const);
            ranges[M - 1] = new Range(getDTLZ7LowerBoundForLastObjective(M), 2.0d * M);
        }
        else if ((problem == Problem.DTLZ5) || (problem == Problem.DTLZ6))
        {
            if (M > 2)
            {
                double base = Math.cos(Math.PI / 4.0d);
                ranges[0] = new Range(0.0d, Math.pow(base, M - 2) + 0.5d);
                ranges[1] = new Range(0.0d, Math.pow(base, M - 2) + 0.5d);
                for (int i = 2; i < M - 1; i++) ranges[i] = new Range(0.0d, Math.pow(base, M - i - 1) + 0.5d);
                ranges[M - 1] = new Range(0.0d, 1.5d);
            }
            else for (int i = 0; i < M; i++) ranges[i] = Range.getNormalRange();

        }
        else if (problem == Problem.DTLZ1) for (int i = 0; i < M; i++) ranges[i] = Range.get0R(2.0d);
        else for (int i = 0; i < M; i++) ranges[i] = new Range(0.0d, 1.5d);
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
        if (problem == Problem.DTLZ7)
        {
            for (int i = 0; i < M - 1; i++) ranges[i] = new Range(0.0d, _DTLZ7_const);
            ranges[M - 1] = new Range(getDTLZ7LowerBoundForLastObjective(M), 2.0d * M);
        }
        else if ((problem == Problem.DTLZ5) || (problem == Problem.DTLZ6))
        {
            if (M > 2)
            {
                double base = Math.cos(Math.PI / 4.0d);
                ranges[0] = new Range(0.0d, Math.pow(base, M - 2));
                ranges[1] = new Range(0.0d, Math.pow(base, M - 2));
                for (int i = 2; i < M - 1; i++) ranges[i] = new Range(0.0d, Math.pow(base, M - i - 1));
                ranges[M - 1] = new Range(0.0d, 1.0d);
            }
            else for (int i = 0; i < M; i++) ranges[i] = Range.getNormalRange();
        }
        else if (problem == Problem.DTLZ1) for (int i = 0; i < M; i++) ranges[i] = new Range(0.0d, 0.5d);
        else for (int i = 0; i < M; i++) ranges[i] = new Range(0.0d, 1.0d);
        return ranges;
    }

    /**
     * Calculates lower bound for the last objective of DTLZ7.
     *
     * @param M the number of objectives.
     * @return lower bound
     */
    private static double getDTLZ7LowerBoundForLastObjective(int M)
    {
        double lower = (1.0d + Math.sin(3.0d * Math.PI * _DTLZ7_const)) * (M - 1);
        lower = 2.0d * (M - _DTLZ7_const / 2.0d * lower);
        return lower;
    }

    /**
     * Returns min-max normalizations for DTLZ1-7 problems (min = true utopia point, max = true nadir point).
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return min-max normalizations
     */
    public static INormalization[] getNormalizations(Problem problem, int M)
    {
        INormalization[] normalizations = new INormalization[M];

        if (problem == Problem.DTLZ7)
        {
            for (int i = 0; i < M - 1; i++) normalizations[i] = new Linear(0.0d, _DTLZ7_const);
            normalizations[M - 1] = new Linear(getDTLZ7LowerBoundForLastObjective(M), 2.0d * M);
        }
        else if ((problem == Problem.DTLZ5) || (problem == Problem.DTLZ6))
        {
            if (M > 2)
            {
                double base = Math.cos(Math.PI / 4.0d);
                normalizations[0] = new Linear(0.0d, Math.pow(base, M - 2));
                normalizations[1] = new Linear(0.0d, Math.pow(base, M - 2));
                for (int i = 2; i < M - 1; i++) normalizations[i] = new Linear(0.0d, Math.pow(base, M - i - 1));
                normalizations[M - 1] = new Linear(0.0d, 1.0d);
            }
            else for (int i = 0; i < M; i++) normalizations[i] = new Linear(0.0d, 1.0d);
        }
        else if (problem == Problem.DTLZ1) for (int c = 0; c < M; c++) normalizations[c] = new Linear(0.0d, 0.5d);
        else for (int i = 0; i < M; i++) normalizations[i] = new Linear(0.0d, 1.0d);
        return normalizations;
    }

    /**
     * Returns true utopia point for DTLZ1-7 problems.
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return utopia point
     */
    public static double[] getUtopia(Problem problem, int M)
    {
        double[] u = new double[M];
        if (problem == Problem.DTLZ7)
            u[M - 1] = getDTLZ7LowerBoundForLastObjective(M);
        return u;
    }

    /**
     * Returns true nadir point for DTLZ1-7 problems.
     *
     * @param problem problem id
     * @param M       number of objectives
     * @return nadir point
     */
    public static double[] getNadir(Problem problem, int M)
    {
        double[] n = new double[M];
        if (problem == Problem.DTLZ7)
        {
            for (int i = 0; i < M - 1; i++) n[i] = _DTLZ7_const;
            n[M - 1] = 2.0d * M;
        }
        else if ((problem == Problem.DTLZ5) || (problem == Problem.DTLZ6))
        {
            if (M > 2)
            {
                double base = Math.cos(Math.PI / 4.0d);
                n[0] = Math.pow(base, M - 2);
                n[1] = Math.pow(base, M - 2);
                for (int i = 2; i < M - 1; i++) n[i] = Math.pow(base, M - i - 1);
                n[M - 1] = 1.0d;

                double right = Math.sin(Math.PI / 4.0d);
                for (int i = 0; i < M - 1; i++) n[i] = right;
                n[M - 1] = 1.0d;
            }
            else Arrays.fill(n, 1.0d);

        }
        else if (problem == Problem.DTLZ1) Arrays.fill(n, 0.5d);
        else Arrays.fill(n, 1.0d);
        return n;
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
        if (problem.equals(Problem.DTLZ1)) return M + 5 - 1;
        else if (problem.equals(Problem.DTLZ7)) return M + 20 - 1;
        else return M + 10 - 1;
    }
}
