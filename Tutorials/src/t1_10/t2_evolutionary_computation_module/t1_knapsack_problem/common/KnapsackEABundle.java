package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common;

import criterion.Criteria;
import ea.AbstractEABundle;
import phase.SortByAuxValue;

/**
 * The "bundle" implementation for the evolutionary algorithm for solving the knapsack problem.
 *
 * @author MTomczyk
 */


public class KnapsackEABundle extends AbstractEABundle
{
    /**
     * Params container.
     */
    public static class Params extends AbstractEABundle.Params
    {
        /**
         * Data container.
         */
        protected final Data _data;

        /**
         * If true, infeasible solutions will be immediately repaired by removing items from the knapsack in the increasing
         * order of their value/size ratios until the feasibility is restored. If false, the algorithm works in "penalty mode".
         */
        protected final boolean _repairMode;

        /**
         * Parameterized constructor.
         *
         * @param capacity   knapsack capacity
         * @param repairMode if true, infeasible solutions will be immediately repaired by removing items from the knapsack
         *                   in the increasing order of their value/size ratios until the feasibility is restored;
         *                   if false, the algorithm works in "penalty mode"
         */
        public Params(int capacity, boolean repairMode)
        {
            super("EA (repair = " + repairMode + ")", Criteria.constructCriteria(new String[]{"Value", "Size"},
                    new boolean[]{true, false}));
            _data = new Data(capacity);
            _repairMode = repairMode;
            _construct = new KnapsackConstructor(_data);
            _evaluate = new KnapsackEvaluator(_data, _repairMode);
            _reproduce = new KnapsackReproducer(_data);
        }
    }

    /**
     * The method is overwritten to instantiate the {@link phase.SortByAuxValue} phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateSortPhase(AbstractEABundle.Params p)
    {
        _phasesBundle._sort = new SortByAuxValue(false);
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public KnapsackEABundle(Params p)
    {
        super(p);
    }
}
