package reproduction.operators;

import random.IRandom;

/**
 * Generic interface for mutation operators.
 *
 * @author MTomczyk
 */
public interface IMutate<T>
{
    /**
     * The main method for mutating the input object.
     *
     * @param A the input object
     * @param R random number generator
     * @return mutated input (should be the same object).
     */
    T mutate(T A, IRandom R);
}
