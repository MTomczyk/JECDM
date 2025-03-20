package reproduction.operators.mutation;

import random.IRandom;
import reproduction.valuecheck.IValueCheck;

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
     * Parameterized constructor. The wrap procedure {@link reproduction.valuecheck.Wrap} is used to constrain variable bounds to [0, 1].
     *
     * @param probability probability of executing the flip
     * @param std         standard deviation
     */
    public Gaussian(double probability, double std)
    {
        this(new Params(probability, std));
    }

    /**
     * Returns an object instance that does not perform value check (the variable values are not bounded)
     *
     * @param probability probability of executing the flip
     * @param std         standard deviation
     * @return Gaussian operator for unconstrained variables
     */
    public static Gaussian getUnconstrained(double probability, double std)
    {
        return getConstrained(probability, std, null);
    }

    /**
     * Returns an object instance that performs value check (the variable values are bounded to [0, 1])
     *
     * @param probability probability of executing the flip
     * @param std         standard deviation
     * @param valueCheck  procedure used to check the values
     * @return Gaussian operator for unconstrained variables
     */
    public static Gaussian getConstrained(double probability, double std, IValueCheck valueCheck)
    {
        Params p = new Params(probability, std);
        p._valueCheck = valueCheck;
        return new Gaussian(p);
    }

    /**
     * Executes Gaussian mutation (doubles only).
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    @Override
    public double [] mutate(double[] o, IRandom R)
    {
        for (int i = 0; i < o.length; i++)
            if (R.nextDouble() < _probability)
            {
                double delta = R.nextGaussian() * _std;
                o[i] = applyDoubleBoundCorrection(o[i] + delta, i);
            }
        return o;
    }

}
