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
     * Method declaration for reproducing (applying crossover) two parents' integer decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    int[] crossover(int[] p1, int[] p2, IRandom R);

    /**
     * Method declaration for reproducing (applying crossover) two parents' double decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    double[] crossover(double[] p1, double[] p2, IRandom R);

    /**
     * Method declaration for reproducing (applying crossover) two parents' boolean decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R random number generator
     * @return new decision vector
     */
    boolean[] crossover(boolean[] p1, boolean[] p2, IRandom R);


}
