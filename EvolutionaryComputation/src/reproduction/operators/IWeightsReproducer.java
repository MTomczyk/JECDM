package reproduction.operators;


import random.IRandom;
import reproduction.operators.crossover.ICrossover;

/**
 * Auxiliary interface for classes responsible for producing a weight vector from two input objects.
 *
 * @author MTomczyk
 */
public interface IWeightsReproducer<T>
{
    /**
     * The main method.
     *
     * @param A the first object
     * @param B the second object
     * @param R random number generator
     * @return weight vector
     */
    ICrossover.DoubleResult getWeights(T A, T B, IRandom R);
}
