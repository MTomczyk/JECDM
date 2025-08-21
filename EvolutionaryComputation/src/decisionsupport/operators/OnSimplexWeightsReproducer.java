package decisionsupport.operators;

import model.internals.value.scalarizing.LNorm;
import random.IRandom;
import reproduction.operators.AbstractOnSimplexWeightsReproducer;
import reproduction.operators.IWeightsReproducer;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.crossover.OnSimplexCombination;
import reproduction.operators.mutation.OnSimplexSimplexMutation;

/**
 * Auxiliary class responsible for generating a weight vector from two input L-norms.
 * It employs {@link OnSimplexCombination} and {@link OnSimplexSimplexMutation} operators.
 *
 * @author MTomczyk
 */
public class OnSimplexWeightsReproducer extends AbstractOnSimplexWeightsReproducer<LNorm> implements IWeightsReproducer<LNorm>
{
    /**
     * Parameterized constructor.
     *
     * @param cStd   standard deviation for the Gaussian operator employed by default in {@link OnSimplexCombination}
     * @param mScale scaling factor utilized by {@link OnSimplexSimplexMutation}
     */
    public OnSimplexWeightsReproducer(double cStd, double mScale)
    {
        super(new OnSimplexCombination(cStd), new OnSimplexSimplexMutation(mScale));
    }

    /**
     * The main method.
     *
     * @param A the first object
     * @param B the second object
     * @param R random number generator
     * @return constructed offspring weight vector
     */
    @Override
    public ICrossover.DoubleResult getWeights(LNorm A, LNorm B, IRandom R)
    {
        return reproduce(A.getWeights(), B.getWeights(), R);
    }
}
