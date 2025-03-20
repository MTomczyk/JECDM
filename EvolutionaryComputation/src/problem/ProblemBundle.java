package problem;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import reproduction.IReproduce;

/**
 * Default implementation of {@link AbstractProblemBundle}. Provides means for instantiating custom problem bundles.
 *
 * @author MTomczyk
 */
public class ProblemBundle extends AbstractProblemBundle
{
    /**
     * Parameterized constructor.
     *
     * @param problem                problem id
     * @param construct              constructs the initial population
     * @param reproduce              creates offspring
     * @param evaluate               Evaluates solutions
     * @param criteria               criteria array
     * @param utopia                 true utopia point for a test problem
     * @param optimizationDirections optimization direction flags (for each objective); true indicates that the objective
     *                               is to be maximized, false otherwise; if the array is null; the array is derived from the criteria object ({@link Criteria#getCriteriaTypes()})
     */
    private ProblemBundle(Problem problem,
                          IConstruct construct,
                          IReproduce reproduce,
                          IEvaluate evaluate,
                          Criteria criteria,
                          double[] utopia,
                          boolean[] optimizationDirections)
    {
        super(problem, construct, reproduce, evaluate, criteria, utopia, optimizationDirections);
    }

    /**
     * Construct and returns a problem bundle that does not involve default EA-related components.
     * Sets only the data that is supplied via this method.
     *
     * @param criteria criteria array
     * @return problem bundle
     */
    public static ProblemBundle getProblemBundle(Criteria criteria)
    {
        return getProblemBundle(criteria, null);
    }

    /**
     * Construct and returns a problem bundle that does not involve default EA-related components.
     * Sets only the data that is supplied via this method.
     *
     * @param criteria criteria array
     * @param utopia   true utopia point for a test problem
     * @return problem bundle
     */
    public static ProblemBundle getProblemBundle(Criteria criteria, double[] utopia)
    {
        return new ProblemBundle(null, null, null, null, criteria, utopia, null);
    }
}

