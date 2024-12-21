package space.distance;

import space.normalization.INormalization;

/**
 * Interface for distance functions.
 *
 * @author MTomczyk
 */

public interface IDistance
{
    /**
     * Can be called to calculate the distance between to vectors (of equal dimensionality).
     * It is assumed that dist(a,b) should equal dist(b,a) (metric space condition).
     *
     * @param a the first vector
     * @param b the second vector
     * @return distance between a and b
     */
    double getDistance(double[] a, double[] b);

    /**
     * Setter for the normalization functions (used to rescale dimensions)
     *
     * @param normalizations normalizers
     */
    void setNormalizations(INormalization[] normalizations);

    /**
     * High-abstraction (implementation-specific) params setter.
     *
     * @param p new params
     */
    void setParams(double[][] p);

    /**
     * High-abstraction (implementation-specific) params getter.
     *
     * @return params
     */
    double[][] getParams();

    /**
     * Implementation-specific weights setter.
     *
     * @param w new weights
     */
    void setWeights(double[] w);

    /**
     * Implementation-specific setter for some auxiliary parameter (e.g., compensation level in L-norms).
     *
     * @param a auxiliary parameter
     */
    void setAuxParam(double a);

    /**
     * Implementation-specific weights getter.
     *
     * @return weights
     */
    double[] getWeights();

    /**
     * Implementation-specific auxiliary param getter.
     *
     * @return auxiliary param value
     */
    double getAuxParam();

    /**
     * Can be used to check whether less/more means closer/further.
     *
     * @return true, if less means closer; false otherwise
     */
    boolean isLessMeaningCloser();
}
