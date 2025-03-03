package problem;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import reproduction.IReproduce;

/**
 * Abstract bundle (container) for default implementations for solving various problems (construct/reproduce/evaluate, etc.).
 *
 * @author MTomczyk
 */
public abstract class AbstractProblemBundle
{
    /**
     * Problem ID.
     */
    public final Problem _problem;

    /**
     * Constructs initial population.
     */
    public final IConstruct _construct;

    /**
     * Creates offspring.
     */
    public final IReproduce _reproduce;

    /**
     * Evaluates solutions.
     */
    public final IEvaluate _evaluate;

    /**
     * Contains reference criteria objects.
     */
    public final Criteria _criteria;

    /**
     * True utopia point for test problems.
     */
    public final double[] _utopia;

    /**
     * Optimization direction flags (for each objective). True indicates that the objective is to be maximized, false otherwise.
     */
    public final boolean[] _optimizationDirections;

    /**
     * Parameterized constructor.
     *
     * @param problem   problem id
     * @param construct constructs the initial population
     * @param reproduce creates offspring
     * @param evaluate  Evaluates solutions
     * @param criteria  criteria array
     * @param utopia    true utopia point for a test problem
     */
    protected AbstractProblemBundle(Problem problem,
                                    IConstruct construct,
                                    IReproduce reproduce,
                                    IEvaluate evaluate,
                                    Criteria criteria,
                                    double[] utopia)
    {
        this(problem, construct, reproduce, evaluate, criteria, utopia, null);
    }


    /**
     * Parameterized constructor.
     *
     * @param problem                problem id
     * @param construct              constructs the initial population
     * @param reproduce              creates offspring
     * @param evaluate               Evaluates solutions
     * @param criteria               criteria array
     * @param utopia                 true utopia point for a test problem
     * @param optimizationDirections optimization direction flags (for each objective); true indicates that the objective is to be maximized, false otherwise;
     *                               if the array is null; the array is derived from the criteria object ({@link Criteria#getCriteriaTypes()})
     */
    protected AbstractProblemBundle(Problem problem,
                                    IConstruct construct,
                                    IReproduce reproduce,
                                    IEvaluate evaluate,
                                    Criteria criteria,
                                    double[] utopia,
                                    boolean[] optimizationDirections)
    {
        _problem = problem;
        _construct = construct;
        _reproduce = reproduce;
        _evaluate = evaluate;
        _criteria = criteria;
        _utopia = utopia;
        if (optimizationDirections != null) _optimizationDirections = optimizationDirections;
        else if (_criteria != null) _optimizationDirections = criteria.getCriteriaTypes();
        else _optimizationDirections = null;
    }

    /**
     * Checks if a problem is a DTLZ problem
     *
     * @param problem problem id
     * @return true = the problem is a DTLZ problem; false otherwise
     */
    public static boolean isDTLZ(Problem problem)
    {
        return (problem.equals(Problem.DTLZ1)) ||
                (problem.equals(Problem.DTLZ2)) ||
                (problem.equals(Problem.DTLZ3)) ||
                (problem.equals(Problem.DTLZ4)) ||
                (problem.equals(Problem.DTLZ5)) ||
                (problem.equals(Problem.DTLZ6)) ||
                (problem.equals(Problem.DTLZ7));
    }

    /**
     * Checks if a problem is a WFG problem
     *
     * @param problem problem id
     * @return true = the problem is a WFG problem; false otherwise
     */
    public static boolean isWFG(Problem problem)
    {
        return (problem.equals(Problem.WFG1)) ||
                (problem.equals(Problem.WFG1ALPHA02)) ||
                (problem.equals(Problem.WFG2)) ||
                (problem.equals(Problem.WFG3)) ||
                (problem.equals(Problem.WFG4)) ||
                (problem.equals(Problem.WFG5)) ||
                (problem.equals(Problem.WFG6)) ||
                (problem.equals(Problem.WFG7)) ||
                (problem.equals(Problem.WFG8)) ||
                (problem.equals(Problem.WFG9)) ||

                (problem.equals(Problem.WFG1EASY)) ||
                (problem.equals(Problem.WFG2EASY)) ||
                (problem.equals(Problem.WFG3EASY)) ||
                (problem.equals(Problem.WFG4EASY)) ||
                (problem.equals(Problem.WFG5EASY)) ||
                (problem.equals(Problem.WFG6EASY)) ||
                (problem.equals(Problem.WFG7EASY)) ||
                (problem.equals(Problem.WFG8EASY)) ||
                (problem.equals(Problem.WFG9EASY));
    }

    /**
     * Helps retrieving a problem ID (enum) from a string.
     *
     * @param id problem id (string)
     * @return problem id (enum); null if there is no matching problem
     */
    public static Problem getProblemFromString(String id)
    {
        return switch (id)
        {
            case "DTLZ1" -> Problem.DTLZ1;
            case "DTLZ2" -> Problem.DTLZ2;
            case "DTLZ3" -> Problem.DTLZ3;
            case "DTLZ4" -> Problem.DTLZ4;
            case "DTLZ5" -> Problem.DTLZ5;
            case "DTLZ6" -> Problem.DTLZ6;
            case "DTLZ7" -> Problem.DTLZ7;
            case "WFG1" -> Problem.WFG1;
            case "WFG1ALPHA02" -> Problem.WFG1ALPHA02;
            case "WFG1ALPHA025" -> Problem.WFG1ALPHA025;
            case "WFG1ALPHA05" -> Problem.WFG1ALPHA05;
            case "WFG2" -> Problem.WFG2;
            case "WFG3" -> Problem.WFG3;
            case "WFG4" -> Problem.WFG4;
            case "WFG5" -> Problem.WFG5;
            case "WFG6" -> Problem.WFG6;
            case "WFG7" -> Problem.WFG7;
            case "WFG8" -> Problem.WFG8;
            case "WFG9" -> Problem.WFG9;
            case "WFG1EASY" -> Problem.WFG1EASY;
            case "WFG2EASY" -> Problem.WFG2EASY;
            case "WFG3EASY" -> Problem.WFG3EASY;
            case "WFG4EASY" -> Problem.WFG4EASY;
            case "WFG5EASY" -> Problem.WFG5EASY;
            case "WFG6EASY" -> Problem.WFG6EASY;
            case "WFG7EASY" -> Problem.WFG7EASY;
            case "WFG8EASY" -> Problem.WFG8EASY;
            case "WFG9EASY" -> Problem.WFG9EASY;
            default -> null;
        };
    }

    /**
     * Can be called to check if the problem is multi-objective.
     *
     * @return true, if the problem is multi-objective; false otherwise
     */
    public boolean isMultiObjective()
    {
        if (_criteria == null) return false;
        return _criteria._no != 1;
    }

}
