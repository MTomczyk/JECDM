package random;

import java.util.stream.Stream;

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
     * Method for indicating whether the concrete implementation supports performing jumps; for parallel computations
     * (see {@link IRandom#createInstanceViaJump()}).
     *
     * @return true, if object is jumble; false otherwise
     */
    boolean isJumpable();

    /**
     * The method is supposed create object instance with advanced state. The method should return null
     * if {@link IRandom#isJumpable()} returns false.
     *
     * @return object clone with advanced state (null, if the object is not jumpable)
     */
    IRandom createInstanceViaJump();

    /**
     * The method is supposed create object instance with advanced state. The method should return null
     * if {@link IRandom#isJumpable()} returns false.
     *
     * @param streamSize stream size (the number of clones to construct)
     * @return object clones with advanced state (null, if the object is not jumpable)
     */
    Stream<IRandom> createInstancesViaJumps(int streamSize);

    /**
     * Method for indicating whether the concrete implementation supports performing the split of the random number
     * generator; for parallel computations (see {@link IRandom#createSplitInstance()}).
     *
     * @return true, if object is splittable; false otherwise
     */
    boolean isSplittable();

    /**
     * The method is supposed create split instance of the object. The method should return null
     * if {@link IRandom#isSplittable()} returns false.
     *
     * @return split object (null, if the object is not splittable)
     */
    IRandom createSplitInstance();

    /**
     * The method is supposed create split instance of the object. The method should return null
     * if {@link IRandom#isSplittable()} returns false.
     *
     * @param streamSize stream size (the number of clones to construct)
     * @return split object (null, if the object is not splittable)
     */
    Stream<IRandom> createSplitInstances(int streamSize);

    /**
     * Returns string representation of the random number generator.
     * @return string representation of the random number generator
     */
    @Override
    String toString();
}
