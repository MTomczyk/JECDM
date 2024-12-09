package space.scalarfunction;

import space.normalization.INormalization;

/**
 * Interface for scalarizing functions.
 *
 * @author MTomczyk
 */

public interface IScalarizingFunction
{
    /**
     * Can be called to get a scalarizing value from an evaluation vector.
     *
     * @param e evaluation vector
     * @return final scalarizing score
     */
    double evaluate(double[] e);

    /**
     * Setter for the normalization functions
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
     * @return  auxiliary param value
     */
    double getAuxParam();
}
