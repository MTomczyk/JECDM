package reproduction.operators.crossover;

import random.IRandom;
import reproduction.operators.AbstractOperator;
import reproduction.valuecheck.IValueCheck;
import reproduction.valuecheck.Wrap;
import space.IntRange;
import space.Range;

/**
 * Abstract class implementing the {@link reproduction.IReproduce} interface.
 *
 * @author MTomczyk
 */


public abstract class AbstractCrossover extends AbstractOperator implements ICrossover
{
    /**
     * Params container.
     */
    public static class Params extends AbstractOperator.Params
    {

        /**
         * If true, parents' input decision vectors are swapped randomly.
         */
        public boolean _swapParentsRandomly;

        /**
         * Parameterized constructor (sets the Wrap object to check values).
         *
         * @param probability the probability of triggering the operation
         */
        public Params(double probability)
        {
            this(probability, true);
        }


        /**
         * Parameterized constructor (sets the Wrap object to check values).
         *
         * @param probability         the probability of triggering the operation
         * @param swapParentsRandomly if true, parents' input decision vectors are swapped randomly
         */
        public Params(double probability, boolean swapParentsRandomly)
        {
            this(probability, new Wrap(), swapParentsRandomly);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability the probability of triggering the operation
         * @param valueCheck  procedure used for correcting invalid gene values
         */
        public Params(double probability, IValueCheck valueCheck)
        {
            this(probability, valueCheck, true);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability         the probability of triggering the operation
         * @param valueCheck          procedure used for correcting invalid gene values
         * @param swapParentsRandomly if true, parents' input decision vectors are swapped randomly
         */
        public Params(double probability, IValueCheck valueCheck, boolean swapParentsRandomly)
        {
            this(probability, valueCheck, (Range[]) null, swapParentsRandomly);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability  the probability of triggering the operation
         * @param valueCheck   procedure used for correcting invalid gene values
         * @param doubleBounds feasible bounds for doubles
         */
        public Params(double probability, IValueCheck valueCheck, Range[] doubleBounds)
        {
            this(probability, valueCheck, doubleBounds, true);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability         the probability of triggering the operation
         * @param valueCheck          procedure used for correcting invalid gene values
         * @param doubleBounds        feasible bounds for doubles
         * @param swapParentsRandomly if true, parents' input decision vectors are swapped randomly
         */
        public Params(double probability, IValueCheck valueCheck, Range[] doubleBounds, boolean swapParentsRandomly)
        {
            super(probability, valueCheck, doubleBounds);
            _swapParentsRandomly = swapParentsRandomly;
        }

        /**
         * Parameterized constructor.
         *
         * @param probability the probability of triggering the operation
         * @param valueCheck  procedure used for correcting invalid gene values
         * @param intBounds   feasible bounds for integers
         */
        public Params(double probability, IValueCheck valueCheck, IntRange[] intBounds)
        {
            this(probability, valueCheck, intBounds, true);
        }

        /**
         * Parameterized constructor.
         *
         * @param probability         the probability of triggering the operation
         * @param valueCheck          procedure used for correcting invalid gene values
         * @param intBounds           feasible bounds for integers
         * @param swapParentsRandomly if true, parents' input decision vectors are swapped randomly
         */
        public Params(double probability, IValueCheck valueCheck, IntRange[] intBounds, boolean swapParentsRandomly)
        {
            super(probability, valueCheck, intBounds);
            _swapParentsRandomly = swapParentsRandomly;

        }
    }


    /**
     * Parameterized constructor. Parameters are passed via params container.
     *
     * @param p params container
     */
    public AbstractCrossover(Params p)
    {
        super(p);
        _swapParentsRandomly = p._swapParentsRandomly;
    }

    /**
     * If true, parents' input decision vectors are swapped randomly.
     */
    protected final boolean _swapParentsRandomly;

    /**
     * Default implementation of the method.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    @Override
    public int[] crossover(int[] p1, int[] p2, IRandom R)
    {
        return null;
    }

    /**
     * Default implementation.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    @Override
    public double[] crossover(double[] p1, double[] p2, IRandom R)
    {
        return null;
    }


    /**
     * Default implementation of the method.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    @Override
    public boolean[] crossover(boolean[] p1, boolean[] p2, IRandom R)
    {
        return null;
    }


    /**
     * Supportive method for swapping parents' decision vectors.
     *
     * @param p1 first parent
     * @param p2 second parent
     * @param R random number generator
     * @return array of decision vectors (random order)
     */
    protected int[][] doSwap(int[] p1, int[] p2, IRandom R)
    {
        if (!_swapParentsRandomly) return new int[][]{p1, p2};
        if (R.nextBoolean()) return new int[][]{p1, p2};
        else return new int[][]{p2, p1};
    }

    /**
     * Supportive method for swapping parents' decision vectors.
     *
     * @param p1 first parent
     * @param p2 second parent
     * @param R random number generator
     * @return array of decision vectors (random order)
     */
    protected double[][] doSwap(double[] p1, double[] p2, IRandom R)
    {
        if (!_swapParentsRandomly) return new double[][]{p1, p2};
        if (R.nextBoolean()) return new double[][]{p1, p2};
        else return new double[][]{p2, p1};

    }

    /**
     * Supportive method for swapping parents' decision vectors.
     *
     * @param p1 first parent
     * @param p2 second parent
     * @param R random number generator
     * @return array of decision vectors (random order)
     */
    protected boolean[][] doSwap(boolean[] p1, boolean[] p2, IRandom R)
    {
        if (!_swapParentsRandomly) return new boolean[][]{p1, p2};
        if (R.nextBoolean()) return new boolean[][]{p1, p2};
        else return new boolean[][]{p2, p1};
    }

}
