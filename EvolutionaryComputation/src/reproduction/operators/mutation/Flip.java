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
         * Parameterized constructor (uses a default flip threshold of 1).
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
     * Parameterized constructor (uses a default flip threshold of 1).
     *
     * @param probability probability of mutation
     */
    public Flip(double probability)
    {
        this(new Params(probability));
    }

    /**
     * Parameterized constructor.
     *
     * @param probability    probability of mutation
     * @param upperThreshold used for flipping doubles
     */
    public Flip(double probability, double upperThreshold)
    {
        this(new Params(probability, upperThreshold));
    }

    /**
     * Parameterized constructor.
     *
     * @param probability    probability of mutation
     * @param upperThreshold used for flipping integers
     */
    public Flip(double probability, int upperThreshold)
    {
        this(new Params(probability, upperThreshold));
    }

    /**
     * Flips integers.
     *
     * @param o decision vector to be mutated
     * @return returns input vector
     */
    @Override
    public int[] mutate(int[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability) o[i] = _upperIntThreshold - o[i];
        return o;
    }

    /**
     * Flips doubles.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    @Override
    public double[] mutate(double[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability) o[i] = _upperDoubleThreshold - o[i];
        return o;
    }

    /**
     * Flips booleans.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    @Override
    public boolean[] mutate(boolean[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability) o[i] = !o[i];
        return o;
    }
}
