package reproduction.operators.mutation;

import random.IRandom;

/**
 * Polynomial Mutation operator implemented for doubles (domain is [0, 1] by default).
 *
 * @author MTomczyk
 */
public class Gaussian extends AbstractMutation implements IMutate
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
         * @param std         standard deviation
         */
        public Params(double probability, double std)
        {
            super(probability);
            _std = std;
        }

        /**
         * Standard deviation.
         */
        public double _std;
    }

    /**
     * Distribution index of the PM operator.
     */
    private final double _std;


    /**
     * Parameterized constructor. Accepts params container.
     *
     * @param p params container.
     */
    public Gaussian(Params p)
    {
        super(p);
        _std = p._std;
    }


    /**
     * Executes Gaussian mutation (doubles only).
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    @Override
    public void mutate(double[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability)
            {
                double delta = R.nextGaussian() * _std;
                o[i] = applyDoubleCorrection(o[i] + delta, i);
            }
    }

}
