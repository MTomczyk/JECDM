package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common;

import criterion.Criteria;
import ea.AbstractEABundle;
import phase.SortByAuxValue;
import random.IRandom;

/**
 * The "bundle" implementation for the evolutionary algorithm for solving the 2-variable continuous problem.
 *
 * @author MTomczyk
 */


public class ContEABundle extends AbstractEABundle
{
    /**
     * Params container.
     */
    public static class Params extends AbstractEABundle.Params
    {
        /**
         * Kernels used.
         */
        protected final Kernel[] _kernels;

        /**
         * Parameterized constructor.
         *
         * @param kernels kernels used
         */
        public Params(Kernel[] kernels)
        {
            super("EA", Criteria.constructCriteria(new String[]{"Value", "Size"},
                    new boolean[]{true, false}));
            _kernels = kernels;
        }

        /**
         * Auxiliary method called by the constructor at the beginning to instantiate default params values (optional).
         * This implementation instantiates phases-related objects.
         */
        @Override
        protected void instantiateDefaultValues()
        {
            if (_construct == null) _construct = new ContConstructor();
            if (_evaluate == null) _evaluate = new ContEvaluator(_kernels);
            if (_reproduce == null) _reproduce = new ContReproducer();
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
    public ContEABundle(Params p)
    {
        super(p);
    }
}
