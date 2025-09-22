package reproduction.operators.crossover;

import random.IRandom;
import reproduction.valuecheck.IValueCheck;

/**
 * Implementation of the SBX operator designed to produce two offspring from two parents.
 *
 * @author MTomczyk
 */
public class SBXTO extends AbstractSBX implements ICrossoverTO
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
     * Parameterized constructor. The wrap procedure {@link reproduction.valuecheck.Wrap} is used to constrain variable
     * bounds to [0, 1].
     *
     * @param p params container
     */
    public SBXTO(Params p)
    {
        super(p);
    }

    /**
     * Parameterized constructor. The wrap procedure {@link reproduction.valuecheck.Wrap} is used to constrain variable
     * bounds to [0, 1].
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     */
    public SBXTO(double probability, double distributionIndex)
    {
        this(new SBXTO.Params(probability, distributionIndex));
    }

    /**
     * Returns an object instance that does not perform value check (the variable values are not bounded)
     *
     * @param probability       probability of executing the flip
     * @param distributionIndex distribution index
     * @return SBX operator for unconstrained variables
     */
    public static SBXTO getUnconstrained(double probability, double distributionIndex)
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
    public static SBXTO getConstrained(double probability, double distributionIndex, IValueCheck valueCheck)
    {
        Params p = new Params(probability, distributionIndex);
        p._valueCheck = valueCheck;
        return new SBXTO(p);
    }

    /**
     * Crossover implementation.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector
     */
    @Override
    public DoubleResult crossover(double[] p1, double[] p2, IRandom R)
    {
        double[][] p = doSwap(p1, p2, R);
        double[] o1 = new double[p[0].length];
        double[] o2 = new double[p[1].length];

        for (int i = 0; i < p[0].length; i++)
        {
            if (Double.compare(R.nextDouble(), _probability) < 0)
            {
                double[] os = getValue(p[0][i], p[1][i], R);
                if (R.nextBoolean())
                {
                    o1[i] = os[0];
                    o2[i] = os[1];
                } else
                {
                    o1[i] = os[1];
                    o2[i] = os[0];
                }
            } else
            {
                if (R.nextBoolean())
                {
                    o1[i] = p[0][i];
                    o2[i] = p[1][i];
                } else
                {
                    o1[i] = p[1][i];
                    o2[i] = p[0][i];
                }
            }

            o1[i] = applyDoubleBoundCorrection(o1[i], i);
            o2[i] = applyDoubleBoundCorrection(o2[i], i);
        }
        return new DoubleResult(o1, o2); // no primary parent
    }
}
