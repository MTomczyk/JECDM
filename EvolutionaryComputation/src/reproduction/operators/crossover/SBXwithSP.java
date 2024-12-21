package reproduction.operators.crossover;

import random.IRandom;

/**
 * Implementation of the SBX operator combined with the Single Point (SP) crossover.
 * Idea: First, determine the point of cut (SP). All pairs of matching genes are crossed using SBX crossover,
 * producing two offspring genes. As with the SP crossover, one parent's offspring genes are taken from the left side
 * of the point of cut, while the second are from the right side.
 *
 * @author MTomczyk
 */
public class SBXwithSP extends AbstractSBX implements ICrossover
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
    public SBXwithSP(Params p)
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

        int pnt = R.nextInt(p[0].length + 1);

        for (int i = 0; i < p[0].length; i++)
        {
            if (R.nextDouble() < _probability)
            {
                double[] os = getValue(p[0][i], p[1][i], R);
                if (i < pnt) o[i] = os[0];
                else o[i] = os[1];
            }
            else
            {
                if (i < pnt) o[i] = p[0][i];
                else o[i] = p[1][i];
            }

            o[i] = applyDoubleBoundCorrection(o[i], i);
        }
        return o;
    }

}
