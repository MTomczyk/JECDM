package reproduction.operators.crossover;

import random.IRandom;

/**
 * Interface for crossover operators designed to construct two offspring solutions from two parent solutions.
 *
 * @author MTomczyk
 */
public interface ICrossoverTO
{
    /**
     * Result of the processing (integers); two offspring solutions are assumed to be constructed from parents.
     */
    class IntResult
    {
        /**
         * Constructed first offspring vector.
         */
        public final int[] _o1;

        /**
         * Constructed second offspring vector.
         */
        public final int[] _o2;

        /**
         * Parameterized constructor.
         *
         * @param o1 constructed first offspring vector
         * @param o2 constructed second offspring vector
         */
        public IntResult(int[] o1, int[] o2)
        {
            _o1 = o1;
            _o2 = o2;
        }
    }

    /**
     * Result of the processing (doubles); two offspring solutions are assumed to be constructed from parents.
     */
    class DoubleResult
    {
        /**
         * Constructed first offspring vector.
         */
        public final double[] _o1;

        /**
         * Constructed second offspring vector.
         */
        public final double[] _o2;

        /**
         * Parameterized constructor.
         *
         * @param o1 constructed first offspring vector
         * @param o2 constructed second offspring vector
         */
        public DoubleResult(double[] o1, double[] o2)
        {
            _o1 = o1;
            _o2 = o2;
        }
    }

    /**
     * Result of the processing (booleans); two offspring solutions are assumed to be constructed from parents.
     */
    class BoolResult
    {
        /**
         * Constructed first offspring vector.
         */
        public final boolean[] _o1;

        /**
         * Constructed second offspring vector.
         */
        public final boolean[] _o2;

        /**
         * Parameterized constructor.
         *
         * @param o1 constructed first offspring vector
         * @param o2 constructed second offspring vector
         */
        public BoolResult(boolean[] o1, boolean[] o2)
        {
            _o1 = o1;
            _o2 = o2;
        }
    }

    /**
     * Method declaration for reproducing (applying crossover) two parents' integer decision vectors. This
     * implementation should return one offspring solution.
     *
     * @param p1 decision vector of the first parent (reference; do not modify it)
     * @param p2 decision vector of the second parent (reference; do not modify it)
     * @param R  random number generator
     * @return two new decision vectors (wrapped via IntResult class)
     */
    default IntResult crossover(int[] p1, int[] p2, IRandom R)
    {
        return null;
    }

    /**
     * Method declaration for reproducing (applying crossover) two parents' double decision vectors.
     *
     * @param p1 decision vector of the first parent (reference; do not modify it)
     * @param p2 decision vector of the second parent (reference; do not modify it)
     * @param R  random number generator
     * @return two new decision vectors (wrapped via DoubleResult class)
     */
    default DoubleResult crossover(double[] p1, double[] p2, IRandom R)
    {
        return null;
    }

    /**
     * Method declaration for reproducing (applying crossover) two parents' boolean decision vectors.
     *
     * @param p1 decision vector of the first parent (reference; do not modify it)
     * @param p2 decision vector of the second parent (reference; do not modify it)
     * @param R  random number generator
     * @return two new decision vectors  (wrapped via BoolResult class)
     */
    default BoolResult crossover(boolean[] p1, boolean[] p2, IRandom R)
    {
        return null;
    }
}
