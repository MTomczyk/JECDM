package reproduction.operators.mutation;

import random.IRandom;

/**
 * Simple mutation that operates only on integer decision vectors. It modifies variables by adding a random integer
 * number drawn from specified bounds.
 *
 * @author MTomczyk
 */
public class RandomIntegerModifier extends AbstractMutation implements IMutate
{
    /**
     * Params container.
     */
    public static class Params extends AbstractMutation.Params
    {
        /**
         * Lower bound for the modifying number to be drawn (can be negative).
         */
        private final int _lb;

        /**
         * Upper bound for the modifying number to be drawn (equals at least the lower bound; if equal, the modifier will
         * be constant)
         */
        private final int _ub;

        /**
         * Parameterized constructor.
         *
         * @param probability probability of applying the mutation operator
         * @param lb          bound for the modifying number to be drawn (can be negative)
         * @param ub          upper bound for the modifying number to be drawn (equals at least the lower bound; if equal, the
         *                    modifier will be constant)
         */
        public Params(double probability, int lb, int ub)
        {
            super(probability);
            _lb = lb;
            if (ub < _lb) _ub = lb;
            else _ub = ub;
        }
    }

    /**
     * Lower bound for the modifying number to be drawn (can be negative).
     */
    private final int _lb;

    /**
     * Upper bound for the modifying number to be drawn (equals at least the lower bound; if equal, the modifier will
     * be constant)
     */
    private final int _ub;

    /**
     * Delta between the upper and the lowe bound.
     */
    private final int _d;

    /**
     * Parameterized constructor.
     *
     * @param probability probability of applying the mutation operator
     * @param lb          bound for the modifying number to be drawn (can be negative)
     * @param ub          upper bound for the modifying number to be drawn (equals at least the lower bound; if equal, the
     *                    modifier will be constant)
     */
    public RandomIntegerModifier(double probability, int lb, int ub)
    {
        this(new Params(probability, lb, ub));
    }

    /**
     * Parameterized constructor. Accepts params container.
     *
     * @param p params container.
     */
    public RandomIntegerModifier(Params p)
    {
        super(p);
        _lb = p._lb;
        _ub = p._ub;
        _d = _ub - _lb;
    }

    /**
     * Performs the mutation. Random integer drawn from the specified bounds is applied to each decision variable
     * with a specified probability.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    @Override
    public int[] mutate(int[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability)
            {
                if (_d == 0) o[i] += _lb;
                else o[i] += (_lb + R.nextInt(_d));
            }
        return o;
    }
}
