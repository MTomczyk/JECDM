package reproduction.operators.crossover;

import random.IRandom;

/**
 * Interface for crossover operators designed to construct an arbitrary number, but constant and pre-defined, of
 * offspring solutions from two parent solutions.
 *
 * @author MTomczyk
 */
public interface ICrossoverMO
{
    /**
     * Result of the processing (integers).
     */
    class IntResult
    {
        /**
         * Constructed offspring vectors packet into a matrix (offspring are packed as rows).
         */
        public final int[][] _o;

        /**
         * Parameterized constructor.
         *
         * @param o constructed offspring vectors packet into a matrix (offspring are packed as rows)
         */
        public IntResult(int[][] o)
        {
            _o = o;
        }
    }

    /**
     * Result of the processing (doubles).
     */
    class DoubleResult
    {
        /**
         * Constructed offspring vectors packet into a matrix (offspring are packed as rows).
         */
        public final double[][] _o;

        /**
         * Parameterized constructor.
         *
         * @param o constructed offspring vectors packet into a matrix (offspring are packed as rows)
         */
        public DoubleResult(double[][] o)
        {
            _o = o;
        }
    }

    /**
     * Result of the processing (booleans).
     */
    class BoolResult
    {
        /**
         * Constructed offspring vectors packet into a matrix (offspring are packed as rows).
         */
        public final boolean[][] _o;

        /**
         * Parameterized constructor.
         *
         * @param o constructed offspring vectors packet into a matrix (offspring are packed as rows)
         */
        public BoolResult(boolean[][] o)
        {
            _o = o;
        }
    }

    /**
     * Method declaration for reproducing (applying crossover) two parents' integer decision vectors. This
     * implementation should return one offspring solution.
     *
     * @param p1 decision vector of the first parent (reference; do not modify it)
     * @param p2 decision vector of the second parent (reference; do not modify it)
     * @param R  random number generator
     * @return an arbitrary (but constant and pre-defined) number of decision vectors (wrapped via IntResult class)
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
     * @return an arbitrary (but constant and pre-defined) number of decision vectors(wrapped via DoubleResult class)
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
     * @return an arbitrary (but constant and pre-defined) number of decision vectors (wrapped via BoolResult class)
     */
    default BoolResult crossover(boolean[] p1, boolean[] p2, IRandom R)
    {
        return null;
    }

    /**
     * This method should inform about the number of offspring solutions the reproducer will construct using two
     * parents. The number should be constant.
     *
     * @return the number of offspring solutions the reproducer will construct using two parents
     */
    int getNoOffspring();
}
