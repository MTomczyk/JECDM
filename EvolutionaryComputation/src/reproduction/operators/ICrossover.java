package reproduction.operators;

import random.IRandom;

/**
 * Generic interface for crossover operators based on reproducing two parents.
 *
 * @author MTomczyk
 */
public interface ICrossover<T>
{
    /**
     * The crossover method.
     *
     * @param A the first parent
     * @param B the second parent
     * @param R random number generator
     * @return offspring object
     */
    T crossover(T A, T B, IRandom R);
}
