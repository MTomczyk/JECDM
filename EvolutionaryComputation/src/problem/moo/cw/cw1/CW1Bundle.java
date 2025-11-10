package problem.moo.cw.cw1;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.IMutate;
import reproduction.operators.mutation.PM;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

/**
 * Abstract bundle (container) for default implementations of problem-related phases (construct/reproduce/evaluate,
 * etc.) and OS-related data helpful when solving the Crash-worthiness problem (Liao, X., Li, Q., Yang, X. et al.
 * Multiobjective optimization for crash safety design of vehicles using stepwise regression model. Struct Multidisc
 * Optim 35, 561–569 (2008). <a href="https://doi.org/10.1007/s00158-007-0163-x">.LINK</a>). Note that the problem
 * assumes that there are 5 continuous decision variables in [1;3] bounds. This implementation assumes using normalized
 * variables in [0; 1] bounds. The linear rescaling is done when evaluating the solutions (see {@link Evaluate}).
 *
 * @author MTomczyk
 */
public class CW1Bundle extends AbstractMOOProblemBundle
{
    /**
     * Auxiliary interface for providing crossover operators in-line.
     */
    public interface ICrossoverConstructor
    {
        /**
         * The main method's signature.
         *
         * @return crossover operator
         */
        ICrossover getCrossover();
    }

    /**
     * Auxiliary interface for providing mutation operators in-line.
     */
    public interface IMutationConstructor
    {
        /**
         * The main method's signature.
         *
         * @return mutation operator
         */
        IMutate getMutation();
    }

    /**
     * Parameterized constructor.
     *
     * @param problem                problem id
     * @param construct              constructs the initial population
     * @param reproduce              creates offspring
     * @param evaluate               evaluates solutions
     * @param displayRanges          display ranges for a test problem used when performing visualization (they do not
     *                               match the true utopia/nadir points)
     * @param paretoFrontBounds      bounds for the Pareto front
     * @param normalizations         min-max normalizations for a test problem (min = true utopia (or nadir) point, max
     *                               = true nadir (or utopia) point)
     * @param utopia                 true utopia point for a test problem
     * @param nadir                  true nadir point for a test problem
     * @param optimizationDirections optimization direction flags (for each objective); true indicates that the
     *                               objective is to be maximized, false otherwise
     * @param criteria               contains reference criteria objects
     */
    public CW1Bundle(Problem problem,
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
        super(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds,
                normalizations, utopia, nadir, optimizationDirections, criteria);
    }

    /**
     * Getter for the default bundle for the Crash-worthiness problem.
     *
     * @return bundle
     */
    public static CW1Bundle getBundle()
    {
        return getBundle(null, null);
    }

    /**
     * Getter for the default bundle for the Crash-worthiness problem.
     *
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the
     *                             crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static CW1Bundle getBundle(ICrossoverConstructor crossoverConstructor,
                                      IMutationConstructor mutationConstructor)
    {
        IConstruct construct = new Construct();
        IEvaluate evaluate = new Evaluate();
        ICrossover crossover;
        if (crossoverConstructor != null) crossover = crossoverConstructor.getCrossover();
        else crossover = new SBX(new SBX.Params(1.0d, 10.0d));

        IMutate mutate;
        if (mutationConstructor != null) mutate = mutationConstructor.getMutation();
        else mutate = new PM(new PM.Params(1.0d / 5.0d, 10.0d));

        IReproduce reproduce = new Reproduce(crossover, mutate);
        Range[] displayRanges = getDisplayRanges();
        Range[] paretoFrontBounds = getParetoFrontBounds();
        INormalization[] normalizations = getNormalizations();
        double[] utopia = getUtopia();
        double[] nadir = getNadir();
        return new CW1Bundle(Problem.CW1, construct, reproduce, evaluate, displayRanges, paretoFrontBounds,
                normalizations, utopia, nadir, new boolean[3],
                Criteria.constructCriteria(new String[]{"Mass", "Ain", "Intrusion"}, new boolean[3]));
    }


    /**
     * Supportive method. Returns display ranges for the Crash-worthiness problem used when performing visualization
     * (they do not match the true utopia/nadir points).
     *
     * @return bounds for the Pareto front
     */
    public static Range[] getDisplayRanges()
    {
        return new Range[]{
                new Range(1661.707822500d, 1700.0d),
                new Range(6.142800000d, 11.0d),
                new Range(0.039400000d, 0.28000000d)
        };
    }

    /**
     * Returns the Pareto front bounds.
     *
     * @return bounds for the Pareto front
     */
    public static Range[] getParetoFrontBounds()
    {
        return new Range[]{
                new Range(1661.707822500d, 1695.200203500d),
                new Range(6.142800000d, 10.745400000d),
                new Range(0.039400000d, 0.264000000d)
        };
    }

    /**
     * Returns min-max normalizations for the Crash-worthiness problem (min = true utopia point, max = true nadir
     * point).
     *
     * @return min-max normalizations
     */
    public static INormalization[] getNormalizations()
    {
        return new INormalization[]{
                new Linear(1661.707822500d, 1695.200203500d),
                new Linear(6.142800000d, 10.745400000d),
                new Linear(0.039400000d, 0.264000000d)
        };
    }

    /**
     * Returns true utopia point for the Crash-worthiness problem.
     *
     * @return utopia point
     */
    public static double[] getUtopia()
    {
        return new double[]{1661.707822500d, 6.142800000d, 0.039400000};
    }

    /**
     * Returns true nadir point for the Crash-worthiness problem.
     *
     * @return nadir point
     */
    public static double[] getNadir()
    {
        return new double[]{1695.200203500d, 10.745400000d, 0.264000000};
    }
}
