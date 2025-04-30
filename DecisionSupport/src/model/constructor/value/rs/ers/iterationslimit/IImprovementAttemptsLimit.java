package model.constructor.value.rs.ers.iterationslimit;

/**
 * Simple interface for dynamically determining the limit for the number of iterations (improvement attempts) for
 * {@link model.constructor.value.rs.ers.ERS}.
 *
 * @author MTomczyk
 */
public interface IImprovementAttemptsLimit
{
    /**
     * Determines the limit for the number of iterations.
     *
     * @param N  the number of feasible models to sample
     * @param M  considered space dimensionality
     * @param Hs history size
     * @return the number of iterations (should be at least 0)
     */
    int getIterations(int N, int M, int Hs);
}
