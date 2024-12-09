package random;

/**
 * Wrapper for random number generators.
 *
 * @author MTomczyk
 */
public interface IRandom
{
    /**
     * Generates random integer (unbounded).
     * @return generated integer
     */
    int nextInt();

    /**
     * Generates random integer from [0, n) bound.
     * @param n right boundary
     * @return generated integer
     */
    int nextInt(int n);

    /**
     * Generates random double from a Gaussian distribution (exp. = 0, std. = 1).
     * @return generated number
     */
    double nextGaussian();

    /**
     * Generates random double from [0, 1] bound.
     * @return generated number
     */
    double nextDouble();

    /**
     * Generates random float from [0, 1] bound.
     *
     * @return generated number
     */
    float nextFloat();

    /**
     * Generates true/false flag randomly.
     * @return generated flag
     */
    boolean nextBoolean();

    /**
     * Selects one of the provided integers randomly with a given probability distribution.
     * @param d array of integers
     * @param p probability distribution
     * @return selected integer
     */
    int getIntWithProbability(int[] d, double [] p);

    /**
     * Selects a random index with a given probability distribution (indexes are bounded from 0 to the length of the input array).
     * @param p probability distribution
     * @return selected index
     */
    int getIdxWithProbability(double [] p);
}
