package problem.moo;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.Problem;
import reproduction.IReproduce;
import space.Range;
import space.normalization.INormalization;

/**
 * Abstract bundle (container) for default implementations of problem-related phases (construct/reproduce/evaluate, etc.)
 * and OS-related data helpful when solving various MOO problems.
 *
 * @author MTomczyk
 */
public class MOOProblemBundle extends AbstractMOOProblemBundle
{
    /**
     * Parameterized constructor.
     *
     * @param problem           problem id
     * @param construct         constructs the initial population
     * @param reproduce         creates offspring
     * @param evaluate          evaluates solutions
     * @param displayRanges     display ranges for a test problem used when performing visualization (they may not match the true utopia/nadir points)
     * @param paretoFrontBounds bounds for the Pareto front
     * @param normalizations    min-max normalizations for a test problem (min = true utopia point, max = true nadir point)
     * @param utopia            true utopia point for a test problem
     * @param nadir             true nadir point for a test problem
     * @param criteria          default criteria definitions
     */
    protected MOOProblemBundle(Problem problem,
                               IConstruct construct,
                               IReproduce reproduce,
                               IEvaluate evaluate,
                               Range[] displayRanges,
                               Range[] paretoFrontBounds,
                               INormalization[] normalizations,
                               double[] utopia,
                               double[] nadir,
                               Criteria criteria)
    {
        super(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds, normalizations, utopia, nadir, criteria);
    }

    /**
     * Construct and returns a problem bundle that does not involve default EA-related components.
     *
     * @param displayRanges     display ranges for a test problem used when performing visualization (they may not match the true utopia/nadir points)
     * @param paretoFrontBounds bounds for the Pareto front
     * @param normalizations    min-max normalizations for a test problem (min = true utopia point, max = true nadir point)
     * @param utopia            true utopia point for a test problem
     * @param nadir             true nadir point for a test problem
     * @param criteria          default criteria definitions
     * @return problem bundle
     */
    public static MOOProblemBundle getProblemBundle(Range[] displayRanges,
                                                    Range[] paretoFrontBounds,
                                                    INormalization[] normalizations,
                                                    double[] utopia,
                                                    double[] nadir,
                                                    Criteria criteria)
    {
        return new MOOProblemBundle(null, null, null, null, displayRanges, paretoFrontBounds, normalizations,
                utopia, nadir, criteria);
    }
}
