package reproduction.operators.crossover;

import random.IRandom;
import reproduction.valuecheck.IValueCheck;

/**
 * Implementation of the SBX operator. The operator constructs the offspring's vector elements using the corresponding
 * elements of the parents' vectors. The resulting value will be closer to one of the parents' values (random decision
 * drawn from the uniform distribution).
 *
 * @author MTomczyk
 */
public class SBX extends AbstractSBX implements ICrossover
{
    /**
     * Params container.
     */
    public static class Params extends AbstractSBX.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param probability       probability of executing the operator
         * @param distributionIndex distribution index for the SBX operator.
         */
        public Params(double probability, double distributionIndex)
        {
            super(probability, distributionIndex);
        }
    }

    /**
     * Parameterized constructor. The wrap procedure {@link reproduction.valuecheck.Wrap} is used to constrain variable bounds to [0, 1].
     *
     * @param p params container
     */
    public SBX(Params p)
    {
        super(p);
    }

    /**
     * Parameterized constructor. The wrap procedure {@link reproduction.valuecheck.Wrap} is used to constrain variable bounds to [0, 1].
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     */
    public SBX(double probability, double distributionIndex)
    {
        this(new SBX.Params(probability, distributionIndex));
    }

    /**
     * Returns an object instance that does not perform value check (the variable values are not bounded)
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     * @return SBX operator for unconstrained variables
     */
    public static SBX getUnconstrained(double probability, double distributionIndex)
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
    public static SBX getConstrained(double probability, double distributionIndex, IValueCheck valueCheck)
    {
        Params p = new Params(probability, distributionIndex);
        p._valueCheck = valueCheck;
        return new SBX(p);
    }

    /**
     * Crossover implementation.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    @Override
    public DoubleResult crossover(double[] p1, double[] p2, IRandom R)
    {
        double[][] p = doSwap(p1, p2, R);
        double[] o = new double[p[0].length];

        for (int i = 0; i < p[0].length; i++)
        {
            if (R.nextDouble() < _probability)
            {
                double[] os = getValue(p[0][i], p[1][i], R);
                if (R.nextBoolean()) o[i] = os[0];
                else o[i] = os[1];
            }
            else
            {
                if (R.nextBoolean()) o[i] = p[0][i];
                else o[i] = p[1][i];
            }

            o[i] = applyDoubleBoundCorrection(o[i], i);
        }
        return new DoubleResult(-1, o); // no primary parent
    }
}
