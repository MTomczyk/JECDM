package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import criterion.Criteria;
import ea.AbstractEABundle;
import phase.SortByAuxValue;
import random.IRandom;

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
         * Knapsack capacity.
         */
        protected final double _capacity;

        /**
         * If true, infeasible solutions will be immediately repaired by removing items from the knapsack in the increasing
         * order of their value/size ratios until the feasibility is restored. If false, the algorithm works in "penalty mode".
         */
        protected final boolean _repairMode;

        /**
         * Random number generator.
         */
        protected final IRandom _R;

        /**
         * Parameterized constructor.
         *
         * @param data       knapsack data
         * @param repairMode if true, infeasible solutions will be immediately repaired by removing items from the knapsack
         *                   in the increasing order of their value/size ratios until the feasibility is restored;
         *                   if false, the algorithm works in "penalty mode"
         * @param capacity   knapsack capacity
         * @param R          random number generator
         */
        public Params(Data data, double capacity, boolean repairMode, IRandom R)
        {
            super("EA (repair = " + repairMode + ")", Criteria.constructCriteria(new String[]{"Value", "Size"},
                    new boolean[]{true, false}));
            _data = data;
            _capacity = capacity;
            _R = R;
            _repairMode = repairMode;
        }

        /**
         * Auxiliary method called by the constructor at the beginning to instantiate default params values (optional).
         * This implementation instantiates phases-related objects.
         */
        @Override
        protected void instantiateDefaultValues()
        {
            if (_construct == null) _construct = new KnapsackConstructor(_data);
            if (_evaluate == null) _evaluate = new KnapsackEvaluator(_data, _capacity, _repairMode);
            if (_reproduce == null) _reproduce = new KnapsackReproducer(_data);
        }
    }

    /**
     * The method is overwritten to instantiate the {@link SortByAuxValue} phase.
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
