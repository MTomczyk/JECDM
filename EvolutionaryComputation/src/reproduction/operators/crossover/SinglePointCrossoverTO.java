package reproduction.operators.crossover;

import random.IRandom;

/**
 * Implementation of a single-point crossover. Point of cut x is selected randomly.
 * Then, offspring inherits [0, x) values from a random parent, while [x, number of decision variables)
 * elements are inherited from the other. This operator assumes constructing two offspring from two parents.
 *
 * @author MTomczyk
 */

public class SinglePointCrossoverTO extends AbstractCrossover implements ICrossoverTO
{
    /**
     * Params container.
     */
    public static class Params extends AbstractCrossover.Params
    {
        /**
         * Default constructor (sets ``swap parents randomly'' to true with the probability of 1.0)).
         */
        public Params()
        {
            this(1.0d, true);
        }

        /**
         * Parameterized constructor.
         *
         * @param p                   probability of swapping
         * @param swapParentsRandomly If true, parents' input decision vectors are swapped randomly
         */
        public Params(double p, boolean swapParentsRandomly)
        {
            super(p, swapParentsRandomly);
        }
    }

    /**
     * Parameterized constructor.
     */
    public SinglePointCrossoverTO()
    {
        this(new Params());
    }

    /**
     * Parameterized constructor. Accepts params container.
     *
     * @param params params container.
     */
    public SinglePointCrossoverTO(Params params)
    {
        super(params);
    }

    /**
     * Generic method declaration for reproducing (applying crossover) two parents' integer decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public IntResult crossover(int[] p1, int[] p2, IRandom R)
    {
        int[][] p = doSwap(p1, p2, R);
        int[] o1 = new int[p[0].length];
        int[] o2 = new int[p[1].length];

        int c = R.nextInt(p[0].length + 1);

        if (c >= 0)
        {
            System.arraycopy(p[0], 0, o1, 0, c);
            System.arraycopy(p[1], 0, o2, 0, c);
        }
        if ((c < p[1].length) && (p[1].length - c >= 0))
        {
            System.arraycopy(p[1], c, o1, c, p[1].length - c);
            System.arraycopy(p[0], c, o2, c, p[0].length - c);
        }
        return new IntResult(o1, o2); // no primary parent
    }

    /**
     * Generic method declaration for reproducing (applying crossover) two parents' double decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public DoubleResult crossover(double[] p1, double[] p2, IRandom R)
    {
        double[][] p = doSwap(p1, p2, R);
        double[] o1 = new double[p[0].length];
        double[] o2 = new double[p[1].length];

        int c = R.nextInt(p[0].length + 1);

        if (c >= 0)
        {
            System.arraycopy(p[0], 0, o1, 0, c);
            System.arraycopy(p[1], 0, o2, 0, c);
        }
        if ((c < p[1].length) && (p[1].length - c >= 0))
        {
            System.arraycopy(p[1], c, o1, c, p[1].length - c);
            System.arraycopy(p[0], c, o2, c, p[0].length - c);
        }
        return new DoubleResult(o1, o2); // no primary parent
    }

    /**
     * Generic method declaration for reproducing (applying crossover) two parents' boolean decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public BoolResult crossover(boolean[] p1, boolean[] p2, IRandom R)
    {
        boolean[][] p = doSwap(p1, p2, R);
        boolean[] o1 = new boolean[p[0].length];
        boolean[] o2 = new boolean[p[1].length];
        int c = R.nextInt(p[0].length + 1);
        System.arraycopy(p[0], 0, o1, 0, c);
        System.arraycopy(p[1], 0, o2, 0, c);
        System.arraycopy(p[1], c, o1, c, p[1].length - c);
        System.arraycopy(p[0], c, o2, c, p[0].length - c);
        return new BoolResult(o1, o2); // no primary parent
    }

}
