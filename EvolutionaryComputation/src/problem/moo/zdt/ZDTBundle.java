package problem.moo.zdt;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.zdt.evaluate.*;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.crossover.SBX;
import reproduction.operators.crossover.SinglePointCrossover;
import reproduction.operators.mutation.Flip;
import reproduction.operators.mutation.IMutate;
import reproduction.operators.mutation.PM;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

/**
 * Abstract bundle (container) for default implementations of problem-related phases (construct/reproduce/evaluate,
 * etc.) and OS-related data helpful when solving ZDT problems.
 *
 * @author MTomczyk
 */
public class ZDTBundle extends AbstractMOOProblemBundle
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
         * @return crossover operator
         */
        ICrossover getCrossover(Problem problem);
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
         * @return mutation operator
         */
        IMutate getMutation(Problem problem);
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
     * @param criteria               contains reference criteria
     */
    public ZDTBundle(Problem problem,
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
     * Getter for the default bundle for ZDT problems.
     *
     * @param problem problem id
     * @return bundle
     */
    public static ZDTBundle getBundle(Problem problem)
    {
        return getBundle(problem, null, null);
    }

    /**
     * Getter for the default bundle for ZDT problems.
     *
     * @param problem              problem id
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the
     *                             crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static ZDTBundle getBundle(Problem problem,
                                      ZDTBundle.ICrossoverConstructor crossoverConstructor,
                                      ZDTBundle.IMutationConstructor mutationConstructor)
    {
        AbstractZDTEvaluate evaluator;
        switch (problem)
        {
            case ZDT2 -> evaluator = new ZDT2();
            case ZDT3 -> evaluator = new ZDT3();
            case ZDT4 -> evaluator = new ZDT4();
            case ZDT5 -> evaluator = new ZDT5();
            case ZDT6 -> evaluator = new ZDT6();
            default -> evaluator = new ZDT1();
        }

        return getBundle(problem, evaluator, crossoverConstructor, mutationConstructor);
    }

    /**
     * Getter for the default bundle for ZDT problems.
     *
     * @param problem  problem id
     * @param evaluate evaluator object responsible for evaluating solutions in line with ZDT test suite
     * @return bundle
     */
    public static ZDTBundle getBundle(Problem problem, AbstractZDTEvaluate evaluate)
    {
        return getBundle(problem, evaluate, null, null);
    }

    /**
     * Getter for the default bundle for ZDT problems.
     *
     * @param problem              problem id
     * @param evaluate             evaluator object responsible for evaluating solutions in line with ZDT test suite
     * @param crossoverConstructor auxiliary object that, when provided (can be null), is used to construct the
     *                             crossover
     *                             operator (otherwise the default operators are used; see the code)
     * @param mutationConstructor  auxiliary object that, when provided (can be null), is used to construct the mutation
     *                             operator (otherwise the default operators are used; see the code)
     * @return bundle
     */
    public static ZDTBundle getBundle(Problem problem,
                                      AbstractZDTEvaluate evaluate,
                                      ZDTBundle.ICrossoverConstructor crossoverConstructor,
                                      ZDTBundle.IMutationConstructor mutationConstructor)
    {
        IConstruct construct;
        switch (problem)
        {
            case ZDT4, ZDT6 -> construct = new Construct46();
            case ZDT5 -> construct = new Construct5();
            default -> construct = new Construct123();
        }

        ICrossover crossover;
        if (crossoverConstructor != null) crossover = crossoverConstructor.getCrossover(problem);
        else
        {
            if (problem.equals(Problem.ZDT5)) crossover = new SinglePointCrossover();
            else crossover = new SBX(new SBX.Params(1.0d, 20.0d));
        }

        IMutate mutate1;
        IMutate mutate2 = null;

        if (mutationConstructor != null) mutate1 = mutationConstructor.getMutation(problem);
        else
        {
            double prob = 1.0d / 30.0d;
            if ((problem.equals(Problem.ZDT4)) || (problem.equals(Problem.ZDT6))) prob = 1.0d / 10.0d;
            if (problem.equals(Problem.ZDT5))
            {
                mutate1 = new Flip(1.0d/5.0d);
                mutate2 = new Flip(1.0d/30.0d);
            }
            else mutate1 = new PM(new PM.Params(prob, 20.0d));
        }

        IReproduce reproduce;
        if (problem == Problem.ZDT5) reproduce = new Reproduce5(crossover, mutate1, mutate2);
        else reproduce = new Reproduce12346(crossover, mutate1);

        Range[] displayRanges = getDisplayRanges(problem);
        Range[] paretoFrontBounds = getParetoFrontBounds(problem);
        INormalization[] normalizations = getNormalizations(problem);
        double[] utopia = getUtopia(problem);
        double[] nadir = getNadir(problem);
        return new ZDTBundle(problem, construct, reproduce, evaluate, displayRanges, paretoFrontBounds,
                normalizations, utopia, nadir, new boolean[2],
                Criteria.constructCriteria("C", 2, false));
    }

    /**
     * Returns true utopia point for ZDT1-6 problems.
     *
     * @param problem problem id
     * @return utopia point
     */
    public static double[] getUtopia(Problem problem)
    {
        double[] u;
        switch (problem)
        {
            case ZDT3 -> u = new double[]{0.0d, -0.773369012326640d};
            case ZDT5 -> u = new double[]{1.0d, 0.322580645161290d};
            case ZDT6 -> u = new double[]{0.280775318815370d, 0.0d};
            default -> u = new double[]{0.0d, 0.0d};
        }
        return u;
    }


    /**
     * Returns true nadir point for ZD1-6 problems.
     *
     * @param problem problem id
     * @return nadir point
     */
    public static double[] getNadir(Problem problem)
    {
        double[] n;
        switch (problem)
        {
            case ZDT3 -> n = new double[]{0.851832865436414d, 1.0d};
            case ZDT5 -> n = new double[]{31.0d, 10.0d};
            case ZDT6 -> n = new double[]{1.0d, 0.921165220344127d};
            default -> n = new double[]{1.0d, 1.0d};
        }
        return n;
    }

    /**
     * Returns min-max normalizations for ZDT1-6 problems (min = true utopia point, max = true nadir point).
     *
     * @param problem problem id
     * @return min-max normalizations
     */
    public static INormalization[] getNormalizations(Problem problem)
    {
        INormalization[] n;
        switch (problem)
        {
            case ZDT3 -> n = new INormalization[]{
                    new Linear(0.0d, 0.851832865436414d),
                    new Linear(-0.773369012326640d, 1.0d)
            };
            case ZDT5 -> n = new INormalization[]{
                    new Linear(1.0d, 31.0d),
                    new Linear(0.322580645161290, 10.0d)
            };
            case ZDT6 -> n = new INormalization[]{
                    new Linear(0.280775318815370d, 1.0d),
                    new Linear(0.0d, 0.921165220344127d)
            };
            default -> n = new INormalization[]{
                    new Linear(0.0d, 1.0d),
                    new Linear(0.0d, 1.0d)
            };
        }
        return n;
    }

    /**
     * Supportive method. Returns display ranges for ZDT1-6 problems used when performing visualization (they do not
     * match the true utopia/nadir points).
     *
     * @param problem problem id
     * @return bounds for the Pareto front
     */
    public static Range[] getDisplayRanges(Problem problem)
    {
        Range[] r = getParetoFrontBounds(problem);
        return new Range[]{r[0], new Range(r[1].getLeft(), r[1].getRight() + r[1].getInterval())};
    }

    /**
     * Returns the Pareto front bounds.
     *
     * @param problem problem id
     * @return bounds for the Pareto front
     */
    public static Range[] getParetoFrontBounds(Problem problem)
    {
        Range[] r;
        switch (problem)
        {
            case ZDT3 -> r = new Range[]{
                    new Range(0.0d, 0.851832865436414d),
                    new Range(-0.773369012326640d, 1.0d)
            };
            case ZDT5 -> r = new Range[]{
                    new Range(1.0d, 31.0d),
                    new Range(0.322580645161290, 10.0d)
            };
            case ZDT6 -> r = new Range[]{
                    new Range(0.280775318815370d, 1.0d),
                    new Range(0.0d, 0.921165220344127d)
            };
            default -> r = new Range[]{
                    new Range(0.0d, 1.0d),
                    new Range(0.0d, 1.0d)
            };
        }
        return r;
    }

}
