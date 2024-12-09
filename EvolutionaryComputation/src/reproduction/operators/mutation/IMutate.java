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
     */
    void mutate(int[] o, IRandom R);

    /**
     * Method declaration for mutating double decision vector.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    void mutate(double[] o, IRandom R);

    /**
     * Method declaration for mutating boolean decision vector.
     *
     * @param o decision vector to be mutated
     * @param R random number generator
     */
    void mutate(boolean[] o, IRandom R);
}
