package reproduction.operators.mutation;

import random.IRandom;

/**
 * Interface for the mutation operator.
 *
 * @author MTomczyk
 */
public interface IMutate
{
    /**
     * Method declaration for mutating integer decision vector.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    default int[] mutate(int[] o, IRandom R)
    {
        return o;
    }

    /**
     * Method declaration for mutating double decision vector.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    default double[] mutate(double[] o, IRandom R)
    {
        return o;
    }

    /**
     * Method declaration for mutating boolean decision vector.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     * @return returns input vector
     */
    default boolean[] mutate(boolean[] o, IRandom R)
    {
        return o;
    }
}
