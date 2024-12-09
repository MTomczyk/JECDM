package model.internals;

import alternative.Alternative;
import space.normalization.INormalization;

/**
 * The top-level interface for internal models.
 *
 * @author MTomczyk
 */
public interface IInternalModel
{
    /**
     * The main method for evaluating an alternative.
     *
     * @param alternative alternative to be evaluated
     * @return attained score
     */
    double evaluate(Alternative alternative);


    /**
     * The main method for setting new normalizations (used to rescale alternative evaluations given the considered criteria).
     *
     * @param normalizations normalizations used to rescale the dimensions
     */
    void setNormalizations(INormalization[] normalizations);

    /**
     * Auxiliary method that allows determining the model preference direction (default implementation = the flag is retrieved from the
     * first internal model).
     *
     * @return true if smaller values are preferred, false otherwise
     */
    boolean isLessPreferred();

    /**
     * Can be used to set parameterize the model (implementation dependent).
     *
     * @param params new params
     */
    void setParams(double[][] params);


    /**
     * Can be used to get model parameters (implementation dependent)
     *
     * @return model params
     */
    double[][] getParams();

    /**
     * Can be used to get model weights (implementation dependent)
     *
     * @return model weights
     */
    double[] getWeights();

    /**
     * Can be used to get model auxiliary parameter (e.g., alpha compensation level, implementation dependent)
     *
     * @return model auxiliary parameter
     */
    Double getAuxParam();

    /**
     * Can be used to set model auxiliary parameter (e.g., alpha compensation level, implementation dependent)
     *
     * @param param auxiliary parameter
     */
    void setAuxParam(double param);


    /**
     * Auxiliary methods that can be overwritten to dispose the data.
     */
    void dispose();

}
