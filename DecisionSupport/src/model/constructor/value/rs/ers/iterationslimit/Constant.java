package model.constructor.value.rs.ers.iterationslimit;

/**
 * Default implementation of {@link IImprovementAttemptsLimit} that returns a constant limit.
 *
 * @author MTomczyk
 */
public class Constant implements IImprovementAttemptsLimit
{
    /**
     * Limit for the number of iterations.
     */
    public final int _limit;

    /**
     * Parameterized constructor.
     *
     * @param limit limit for the number of iterations
     */
    public Constant(int limit)
    {
        _limit = limit;
    }

    /**
     * Determines the limit for the number of iterations.
     *
     * @param N  the number of feasible models to sample
     * @param M  considered space dimensionality
     * @param Hs history size
     * @return the number of iterations (should be at least 0)
     */
    @Override
    public int getIterations(int N, int M, int Hs)
    {
        return _limit;
    }
}
