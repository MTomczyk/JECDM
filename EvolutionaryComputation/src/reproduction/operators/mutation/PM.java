package reproduction.operators.mutation;

import random.IRandom;

/**
 * Polynomial Mutation operator implemented for doubles (domain is [0, 1] by default).
 *
 * @author MTomczyk
 */

public class PM extends AbstractMutation implements IMutate
{
    /**
     * Params container.
     */
    public static class Params extends AbstractMutation.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param probability probability of executing the flip
         * @param distributionIndex distribution index
         */
        public Params(double probability, double distributionIndex)
        {
            super(probability);
            _distributionIndex = distributionIndex;
        }

        /**
         * Distribution index of the PM operator.
         */
        public double _distributionIndex;
    }

    /**
     * Distribution index of the PM operator.
     */
    private final double _distributionIndex;


    /**
     * Parameterized constructor. Accepts params container.
     *
     * @param p params container.
     */
    public PM(Params p)
    {
        super(p);
        _distributionIndex = p._distributionIndex;
    }

    /**
     * Execute PM (doubles only).
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(double[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability)
            {
                double v = R.nextDouble();
                double delta;

                if (v < 0.5d)
                {
                    double b = 2.0d * v + (1.0d - 2.0d * v) * (Math.pow(1.0d - o[i], (_distributionIndex + 1.0d)));
                    delta = Math.pow(b, (1.0 / (_distributionIndex + 1.0d))) - 1.0d;
                } else
                {
                    double b = 2.0d * (1.0d - v) + 2.0d * (v - 0.5) * (Math.pow(o[i], (_distributionIndex + 1.0d)));
                    delta = 1.0 - Math.pow(b, (1.0 / (_distributionIndex + 1.0d)));
                }

                o[i] = applyDoubleCorrection(o[i] + delta, i);
            }
    }

}
