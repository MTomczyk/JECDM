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
     * @param R  random number generator
     * @return new decision vector
     */
    @Override
    public IntResult crossover(int[] p1, int[] p2, IRandom R)
    {
        return null;
    }

    /**
     * Default implementation.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector
     */
    @Override
    public DoubleResult crossover(double[] p1, double[] p2, IRandom R)
    {
        return null;
    }

    /**
     * Default implementation of the method.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector
     */
    @Override
    public BoolResult crossover(boolean[] p1, boolean[] p2, IRandom R)
    {
        return null;
    }

    /**
     * Auxiliary method for checking if two parents should be swapped. The method first checks the internal
     * parameterization (e.g., _swapParentsRandomly flag). Then it returns a random boolean.
     *
     * @param R random number generator
     * @return false, if two parents should be swapped; true otherwise
     */
    protected boolean shouldSwap(IRandom R)
    {
        if (!_swapParentsRandomly) return false;
        return R.nextBoolean();
    }

    /**
     * Returns randomly either a [0, 1] or [1, 0] vector (the decision is based on the result of
     * {@link AbstractCrossover#shouldSwap(IRandom)}). The arrays' elements are indices supposed to be pointing to
     * parents' decision vectors.
     *
     * @param R Random number generator.
     * @return [0, 1] or [1, 0] vector
     */
    protected int[] getSwappedIndices(IRandom R)
    {
        if (shouldSwap(R)) return new int[]{1, 0};
        else return new int[]{0, 1};
    }

    /**
     * Supportive method for swapping parents' decision vectors (randomly; the decision is based on the result of
     * {@link AbstractCrossover#shouldSwap(IRandom)})).
     *
     * @param p1 first parent
     * @param p2 second parent
     * @param R  random number generator
     * @return array of decision vectors (random order)
     */
    protected int[][] doSwap(int[] p1, int[] p2, IRandom R)
    {
        if (shouldSwap(R)) return new int[][]{p2, p1};
        else return new int[][]{p1, p2};
    }

    /**
     * Supportive method for swapping parents' decision vectors (randomly; the decision is based on the result of
     * {@link AbstractCrossover#shouldSwap(IRandom)})).
     *
     * @param p1 first parent
     * @param p2 second parent
     * @param R  random number generator
     * @return array of decision vectors (random order)
     */
    protected double[][] doSwap(double[] p1, double[] p2, IRandom R)
    {
        if (shouldSwap(R)) return new double[][]{p2, p1};
        else return new double[][]{p1, p2};
    }

    /**
     * Supportive method for swapping parents' decision vectors (randomly; the decision is based on the result of
     * {@link AbstractCrossover#shouldSwap(IRandom)})).
     *
     * @param p1 first parent
     * @param p2 second parent
     * @param R  random number generator
     * @return array of decision vectors (random order)
     */
    protected boolean[][] doSwap(boolean[] p1, boolean[] p2, IRandom R)
    {
        if (shouldSwap(R)) return new boolean[][]{p2, p1};
        else return new boolean[][]{p1, p2};
    }

}
