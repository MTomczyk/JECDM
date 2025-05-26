package reproduction.operators;

import model.internals.value.scalarizing.LNorm;
import random.IRandom;
import reproduction.operators.crossover.OnSimplexCombination;
import reproduction.operators.mutation.OnSimplexSimplexMutation;

/**
 * Abstract implementation of {@link IWeightsReproducer}.
 *
 * @author MTomczyk
 */
public abstract class AbstractOnSimplexWeightsReproducer<T> implements IWeightsReproducer<T>
{
    /**
     * Crossover operator used.
     */
    protected final OnSimplexCombination _crossover;

    /**
     * Mutation operator used.
     */
    protected final OnSimplexSimplexMutation _mutation;

    /**
     * Parameterized constructor.
     *
     * @param crossover crossover operator
     * @param mutation  mutation operator
     */
    protected AbstractOnSimplexWeightsReproducer(OnSimplexCombination crossover, OnSimplexSimplexMutation mutation)
    {
        _crossover = crossover;
        _mutation = mutation;
    }

    /**
     * Auxiliary method for reproducing weight vectors.
     *
     * @param w1 the first weight vector
     * @param w2 the second weight vector
     * @param R  random number generator
     * @return offspring weight vector
     */
    protected double[] reproduce(double[] w1, double[] w2, IRandom R)
    {
        double[] o = _crossover.crossover(w1, w2, R);
        _mutation.mutate(o, R);
        return o;
    }

}
