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
     *
     * @param criteria               criteria array
     * @param utopia                 true utopia point for a test problem
     * @return problem bundle
     */
    public static ProblemBundle getProblemBundle(Criteria criteria, double[] utopia)
    {
        return new ProblemBundle(null, null, null, null, criteria, utopia, null);
    }
}

