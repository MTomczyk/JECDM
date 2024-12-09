package problem.moo;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.AbstractProblemBundle;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import problem.moo.wfg.WFGBundle;
import reproduction.IReproduce;
import space.Range;
import space.normalization.INormalization;

/**
 * Abstract bundle (container) for default implementations of problem-related phases (construct/reproduce/evaluate, etc.)
 * and OS-related data helpful when solving various MOO problems.
 *
 * @author MTomczyk
 */
public abstract class AbstractMOOProblemBundle extends AbstractProblemBundle
{
    /**
     * Display ranges for test problems used when performing visualization (they may not match the true utopia/nadir points).
     */
    public final Range[] _displayRanges;

    /**
     * Bounds for the Pareto front.
     */
    public final Range[] _paretoFrontBounds;

    /**
     * Min-max normalizations for test problems (min = true utopia point, max = true nadir point).
     */
    public final INormalization[] _normalizations;

    /**
     * True utopia point for test problems.
     */
    public final double[] _utopia;

    /**
     * True nadir point for test problems.
     */
    public final double[] _nadir;

    /**
     * Optimization direction flags (for each objective). True indicates that the objective is to be maximized, false otherwise.
     */
    public final boolean[] _optimizationDirections;

    /**
     * Contains reference criteria objects.
     */
    public final Criteria _criteria;

    /**
     * Parameterized constructor.
     *
     * @param problem                problem id
     * @param construct              constructs the initial population
     * @param reproduce              creates offspring
     * @param evaluate               Evaluates solutions
     * @param displayRanges          display ranges for a test problem used when performing visualization (they may not match the true utopia/nadir points)
     * @param paretoFrontBounds      bounds for the Pareto front
     * @param normalizations         min-max normalizations for a test problem (min = true utopia point, max = true nadir point)
     * @param utopia                 true utopia point for a test problem
     * @param nadir                  true nadir point for a test problem
     * @param optimizationDirections optimization direction flags (for each objective); true indicates that the objective is to be maximized, false otherwise
     * @param criteria               default criteria definitions
     */
    protected AbstractMOOProblemBundle(Problem problem,
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
        super(problem, construct, reproduce, evaluate);
        _displayRanges = displayRanges;
        _paretoFrontBounds = paretoFrontBounds;
        _normalizations = normalizations;
        _utopia = utopia;
        _nadir = nadir;
        _optimizationDirections = optimizationDirections;
        _criteria = criteria;
    }

    /**
     * Returns problem bundle (if the input identifier is unknown, the method returns null).
     * The no. distance/position related parameters are set to default values.
     *
     * @param problem problem id (MOO)
     * @param M       number of objectives
     * @return min-max normalizations
     */
    public static AbstractMOOProblemBundle getBundle(Problem problem, int M)
    {
        if (AbstractMOOProblemBundle.isDTLZ(problem)) return DTLZBundle.getBundle(problem, M,
                DTLZBundle.getRecommendedNODistanceRelatedParameters(problem, M));
        else if (AbstractMOOProblemBundle.isWFG(problem)) return WFGBundle.getBundle(problem, M,
                WFGBundle.getRecommendedNOPositionRelatedParameters(problem, M),
                WFGBundle.getRecommendedNODistanceRelatedParameters(problem, M));
        return null;
    }

    /**
     * Returns min-max normalizations for a MOO problem (if the input identifier is unknown, the method returns null).
     *
     * @param problem problem id (MOO)
     * @param M       number of objectives
     * @return min-max normalizations
     */
    public static INormalization[] getNormalizations(Problem problem, int M)
    {
        if (AbstractMOOProblemBundle.isDTLZ(problem)) return DTLZBundle.getNormalizations(problem, M);
        else if (AbstractMOOProblemBundle.isWFG(problem)) return WFGBundle.getNormalizations(problem, M);
        return null;
    }
}
