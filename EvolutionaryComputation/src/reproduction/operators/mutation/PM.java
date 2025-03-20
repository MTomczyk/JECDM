package reproduction.operators.mutation;

import random.IRandom;
import reproduction.valuecheck.IValueCheck;

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
         * @param probability       probability of executing the flip
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
     * Parameterized constructor. The wrap procedure {@link reproduction.valuecheck.Wrap} is used to constrain variable bounds to [0, 1].
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     */
    public PM(double probability, double distributionIndex)
    {
        this(new Params(probability, distributionIndex));
    }

    /**
     * Returns an object instance that does not perform value check (the variable values are not bounded)
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     * @return PM operator for unconstrained variables
     */
    public static PM getUnconstrained(double probability, double distributionIndex)
    {
        return getConstrained(probability, distributionIndex, null);
    }

    /**
     * Returns an object instance that performs value check (the variable values are bounded to [0, 1])
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     * @param valueCheck        procedure used to check the values
     * @return PM operator for unconstrained variables
     */
    public static PM getConstrained(double probability, double distributionIndex, IValueCheck valueCheck)
    {
        Params p = new Params(probability, distributionIndex);
        p._valueCheck = valueCheck;
        return new PM(p);
    }

    /**
     * Execute PM (doubles only).
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    @Override
    public double[] mutate(double[] o, IRandom R)
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
                }
                else
                {
                    double b = 2.0d * (1.0d - v) + 2.0d * (v - 0.5) * (Math.pow(o[i], (_distributionIndex + 1.0d)));
                    delta = 1.0 - Math.pow(b, (1.0 / (_distributionIndex + 1.0d)));
                }

                o[i] = applyDoubleBoundCorrection(o[i] + delta, i);
            }
        return o;
    }

}
