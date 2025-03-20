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
     *
     * @return generated integer
     */
    int nextInt();

    /**
     * Generates array of random integer (unbounded).
     *
     * @param n array length
     * @return generated integer
     */
    int[] nextInts(int n);

    /**
     * Generates random integer from [0, b) bound.
     *
     * @param b right bound
     * @return generated integer
     */
    int nextInt(int b);

    /**
     * Generates array of random integers from [0, b) bound.
     *
     * @param n array length
     * @param b right bound
     * @return generated integer
     */
    int[] nextInts(int n, int b);

    /**
     * Generates random double from a Gaussian distribution (exp. = 0, std. = 1).
     *
     * @return generated number
     */
    double nextGaussian();

    /**
     * Generates array of random doubles derived from a Gaussian distribution (exp. = 0, std. = 1).
     *
     * @param n array length
     * @return array of generated numbers
     */
    double[] nextGaussians(int n);

    /**
     * Generates random double from [0, 1] bound.
     *
     * @return generated number
     */
    double nextDouble();

    /**
     * Generates array of random double from [0, 1] bound.
     *
     * @param n array length
     * @return generated number
     */
    double[] nextDoubles(int n);

    /**
     * Generates random float from [0, 1] bound.
     *
     * @return generated number
     */
    float nextFloat();

    /**
     * Generates array of random float from [0, 1] bound.
     *
     * @param n array length
     * @return array of generated floats
     */
    float[] nextFloats(int n);

    /**
     * Generates true/false flag randomly.
     *
     * @return generated flag
     */
    boolean nextBoolean();

    /**
     * Generates array of random true/false flags.
     *
     * @param n array length
     * @return array of generated flags
     */
    boolean[] nextBooleans(int n);

    /**
     * Selects one of the provided integers randomly with a given probability distribution.
     *
     * @param d array of integers
     * @param p probability distribution
     * @return selected integer
     */
    int getIntWithProbability(int[] d, double[] p);

    /**
     * Selects a random index with a given probability distribution (indexes are bounded from 0 to the length of the input array).
     *
     * @param p probability distribution
     * @return selected index
     */
    int getIdxWithProbability(double[] p);

    /**
     * Returns the seed used to initialize the object.
     *
     * @return the seed
     */
    long getSeed();
}
