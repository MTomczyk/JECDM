package reproduction.operators.crossover;

import random.IRandom;

/**
 * Abstract implementation of the SBX operator.
 *
 * @author MTomczyk
 */
public class AbstractSBX extends AbstractCrossover implements ICrossover
{
    /**
     * Params container.
     */
    public static class Params extends AbstractCrossover.Params
    {
        /**
         * Distribution index.
         */
        public double _distributionIndex;

        /**
         * Parameterized constructor.
         *
         * @param probability       probability of executing the operator
         * @param distributionIndex distribution index for the SBX operator.
         */
        public Params(double probability, double distributionIndex)
        {
            super(probability);
            _distributionIndex = distributionIndex;
        }
    }

    /**
     * Distribution index.
     */
    protected final double _distributionIndex;

    /**
     * Auxiliary variable.
     */
    protected final double _power;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractSBX(Params p)
    {
        super(p);
        _distributionIndex = p._distributionIndex;
        _power = 1.0d / (_distributionIndex + 1.0d);
    }

    /**
     * Supportive method for calculating SBX value.
     *
     * @param p1 first parent's value
     * @param p2 second parent's value
     * @param R random number generator
     * @return calculated sbx value
     */
    protected double[] getValue(double p1, double p2, IRandom R)
    {
        if (Double.compare(p1, p2) == 0) return new double[]{p1, p1};

        boolean reversed = p2 < p1;

        double u = R.nextDouble();

        double dv = Math.abs(p2 - p1);
        double b = 1.0d + (2.0d * Math.min(p1, p2) / dv);
        double bq = getBQ(b, u);
        double o1 = 0.5d * ((p1 + p2) - bq * dv);

        b = 1.0d + (2.0d * (1.0d - Math.max(p1, p2)) / dv);
        bq = getBQ(b, u);
        double o2 = 0.5d * ((p1 + p2) + bq * dv);

        if (reversed) return new double[]{o2, o1};
        else return new double[]{o1, o2};
    }

    /**
     * Supportive method for calculating some technical parameter value.
     *
     * @param b b-parameter
     * @param u u-parameter
     * @return bq-parameter
     */
    protected double getBQ(double b, double u)
    {
        double a = 2.0d - Math.pow(b, -(_distributionIndex + 1.0d));
        if (u < 0.5d) return Math.pow(u * a, _power);
        else return Math.pow(1.0d / (2.0d - u * a), _power);
    }
}
