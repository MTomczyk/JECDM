package reproduction.operators.mutation;

import random.IRandom;

/**
 * Flip mutation operator. Integers/doubles are flipped according to 1 by default.
 *
 * @author MTomczyk
 */

public class Flip extends AbstractMutation implements IMutate
{
    /**
     * Params container.
     */
    public static class Params extends AbstractMutation.Params
    {
        /**
         * Used for flipping integers
         */
        public int _upperIntThreshold = 1;

        /**
         * Used for flipping doubles.
         */
        public double _upperDoubleThreshold = 1.0d;

        /**
         * Parameterized constructor.
         *
         * @param probability probability of executing the flip
         */
        public Params(double probability)
        {
            super(probability);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability    probability of mutation
         * @param upperThreshold used for flipping integers
         */
        public Params(double probability, int upperThreshold)
        {
            super(probability);
            _upperIntThreshold = upperThreshold;
        }

        /**
         * Parameterized constructor.
         *
         * @param probability    probability of mutation
         * @param upperThreshold used for flipping doubles
         */
        public Params(double probability, double upperThreshold)
        {
            super(probability);
            _upperDoubleThreshold = upperThreshold;
        }
    }

    /**
     * Used for flipping integers.
     */
    private final int _upperIntThreshold;

    /**
     * Used for flipping doubles.
     */
    private final double _upperDoubleThreshold;

    /**
     * Parameterized constructor. Accepts params container.
     *
     * @param params params container.
     */
    public Flip(Params params)
    {
        super(params);
        _upperIntThreshold = params._upperIntThreshold;
        _upperDoubleThreshold = params._upperDoubleThreshold;
    }


    /**
     * Flips integers.
     *
     * @param o decision vector to be mutated
     */
    @Override
    public void mutate(int[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability) o[i] = _upperIntThreshold - o[i];
    }

    /**
     * Flips doubles.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(double[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability) o[i] = _upperDoubleThreshold - o[i];
    }

    /**
     * Flips booleans.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(boolean[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability) o[i] = !o[i];
    }
}