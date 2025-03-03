package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import criterion.Criteria;
import phase.IConstruct;
import phase.IEvaluate;
import problem.moo.MOOProblemBundle;
import reproduction.IReproduce;
import space.Range;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

/**
 * Class representing this packages problem bundle. Important note: Although this bundle concerns single-objective
 * knapsack problem formulation (as imposed by this package's classes), it is viewed as a two-objective one since the
 * evaluator class instantiates and fills a two-element performance vector to each specimen. The vector's first element
 * is associated with the first objective -- total value, while the latter is with total size. This way, the bundle can
 * be easily coupled with 2D visualizations portraying solutions as (value; size) vectors.
 *
 * @author MTomczyk
 */
public class ProblemBundle extends MOOProblemBundle
{
    /**
     * Parameterized constructor.
     *
     * @param construct         constructs the initial population
     * @param reproduce         creates offspring
     * @param evaluate          Evaluates solutions
     * @param displayRanges     display ranges for a test problem used when performing visualization (they may not match the true utopia/nadir points)
     * @param paretoFrontBounds bounds for the Pareto front
     * @param normalizations    min-max normalizations for a test problem (min = true utopia point, max = true nadir point)
     * @param utopia            true utopia point for a test problem
     * @param nadir             true nadir point for a test problem
     * @param criteria          default criteria definitions
     */
    protected ProblemBundle(IConstruct construct,
                            IReproduce reproduce,
                            IEvaluate evaluate,
                            Range[] displayRanges,
                            Range[] paretoFrontBounds,
                            INormalization[] normalizations,
                            double[] utopia,
                            double[] nadir,
                            Criteria criteria)
    {
        super(null, construct, reproduce, evaluate, displayRanges, paretoFrontBounds, normalizations, utopia, nadir, criteria);
    }

    /**
     * Factory-like class for retrieving the bundle. It instantiates objective space-related data as though the problem
     * is two-objective (the first objective is maximizing total value, while the second is minimizing total size).
     *
     * @param data                           knapsack data
     * @param capacity                       capacity
     * @param repairMode                     if true, infeasible solutions will be immediately repaired by removing items
     *                                       from the knapsack in the increasing order of their value/size ratios until
     *                                       the feasibility is restored; if false, the algorithm works in "penalty mode"
     * @param attemptReinsertionDuringRepair if true (and the repair mode is on), the evaluator will attempt to reinsert
     *                                       items after the feasibility is restored via the repairing procedure; the items
     *                                       will be examined in descending order of their value/size ratios
     * @return problem bundle (problem is viewed as two-objective)
     */
    public static MOOProblemBundle getProblemBundle(Data data, double capacity, boolean repairMode, boolean attemptReinsertionDuringRepair)
    {
        IConstruct construct = new KnapsackConstructor(data);
        IReproduce reproduce = new KnapsackReproducer(data);
        IEvaluate evaluate = new KnapsackEvaluator(data, capacity, repairMode, attemptReinsertionDuringRepair);
        Criteria criteria = Criteria.constructCriteria(new String[]{"Value", "Size"}, new boolean[]{true, false});

        // value; size
        double[] nadir = new double[2];
        double[] utopia = new double[2];

        for (int i = 0; i < data._items; i++)
        {
            utopia[0] += data._values[i];
            nadir[1] += data._sizes[i];
        }

        Range[] paretoFrontBounds = new Range[]{
                new Range(nadir[0], utopia[0]),
                new Range(utopia[1], nadir[1])
        };

        Range[] displayRanges = new Range[]{
                new Range(nadir[0], utopia[0]),
                new Range(utopia[1], nadir[1])
        };

        INormalization[] normalizations = new INormalization[]{
                new Linear(paretoFrontBounds[0].getLeft(), paretoFrontBounds[0].getRight()),
                new Linear(paretoFrontBounds[1].getLeft(), paretoFrontBounds[1].getRight())
        };

        return new ProblemBundle(construct, reproduce, evaluate, displayRanges, paretoFrontBounds, normalizations, utopia, nadir, criteria);
    }
}
