package reproduction.operators.crossover;

import random.IRandom;

/**
 * Implementation of the SBX operator. The operator constructs the offspring's vector elements using the corresponding
 * elements of the parents' vectors. The resulting value will be closer to one of the parents' values (random decision
 * drawn from the uniform distribution).
 *
 * @author MTomczyk
 */


public class SBX extends AbstractSBX
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
     * Parameterized constructor.
     *
     * @param p params container
     */
    public SBX(Params p)
    {
        super(p);
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
    public double[] crossover(double[] p1, double[] p2, IRandom R)
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

            o[i] = applyDoubleCorrection(o[i], i);
        }
        return o;
    }
}
