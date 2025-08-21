package reproduction.operators.crossover;

import random.IRandom;

/**
 * Interface for the crossover operator.
 *
 * @author MTomczyk
 */
public interface ICrossover
{
    /**
     * Result of the processing.
     */
    abstract class Result
    {
        /**
         * Index of the primary parent used in the reproduction (implementation specific; e.g., 0 can mean that
         * the exploitation was done around the first parent, while 1 that around the second).
         */
        public final int _primaryParent;

        /**
         * Parameterized constructor.
         *
         * @param primaryParent index of the primary parent used in the reproduction (implementation specific; e.g.,
         *                      0 can mean that the exploitation was done around the first parent, while 1 that around
         *                      the second)
         */
        public Result(int primaryParent)
        {
            _primaryParent = primaryParent;
        }
    }

    /**
     * Result of the processing (integers).
     */
    class IntResult extends Result
    {
        /**
         * Constructed offspring vector.
         */
        public final int[] _o;

        /**
         * Parameterized constructor.
         *
         * @param primaryParent index of the primary parent used in the reproduction (implementation specific; e.g.,
         *                      0 can mean that the exploitation was done around the first parent, while 1 that around
         *                      the second)
         * @param o             constructed offspring vector
         */
        public IntResult(int primaryParent, int[] o)
        {
            super(primaryParent);
            _o = o;
        }
    }

    /**
     * Result of the processing (doubles).
     */
    class DoubleResult extends Result
    {
        /**
         * Constructed offspring vector.
         */
        public final double[] _o;

        /**
         * Parameterized constructor.
         *
         * @param primaryParent index of the primary parent used in the reproduction (implementation specific; e.g.,
         *                      0 can mean that the exploitation was done around the first parent, while 1 that around
         *                      the second)
         * @param o             constructed offspring vector
         */
        public DoubleResult(int primaryParent, double[] o)
        {
            super(primaryParent);
            _o = o;
        }
    }

    /**
     * Result of the processing (booleans).
     */
    class BoolResult extends Result
    {
        /**
         * Constructed offspring vector.
         */
        public final boolean[] _o;

        /**
         * Parameterized constructor.
         *
         * @param primaryParent index of the primary parent used in the reproduction (implementation specific; e.g.,
         *                      0 can mean that the exploitation was done around the first parent, while 1 that around
         *                      the second)
         * @param o             constructed offspring vector
         */
        public BoolResult(int primaryParent, boolean[] o)
        {
            super(primaryParent);
            _o = o;
        }
    }

    /**
     * Method declaration for reproducing (applying crossover) two parents' integer decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector (wrapped via IntResult class)
     */
    IntResult crossover(int[] p1, int[] p2, IRandom R);

    /**
     * Method declaration for reproducing (applying crossover) two parents' double decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector (wrapped via DoubleResult class)
     */
    DoubleResult crossover(double[] p1, double[] p2, IRandom R);

    /**
     * Method declaration for reproducing (applying crossover) two parents' boolean decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return new decision vector  (wrapped via BoolResult class)
     */
    BoolResult crossover(boolean[] p1, boolean[] p2, IRandom R);
}
