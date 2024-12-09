package model.internals;

import alternative.Alternative;
import space.normalization.INormalization;

/**
 * Abstract (top-level) class providing common fields and functionalities for classes representing internal models.
 *
 * @author MTomczyk
 */
public abstract class AbstractInternalModel implements IInternalModel
{
    /**
     * The name of the model.
     */
    protected String _name;

    /**
     * Parameterized constructor.
     *
     * @param name internal model name
     */
    public AbstractInternalModel(String name)
    {
        _name = name;
    }

    /**
     * The main method for evaluating an alternative.
     *
     * @param alternative alternative to be evaluated
     * @return attained score
     */
    @Override
    public double evaluate(Alternative alternative)
    {
        return 0.0d;
    }


    /**
     * The main method for setting new normalizations (used to rescale alternative evaluations given the considered criteria).
     *
     * @param normalizations normalizations used to rescale the dimensions
     */
    @Override
    public void setNormalizations(INormalization[] normalizations)
    {

    }

    /**
     * Auxiliary method that allows determining the model preference direction (default implementation = the flag is retrieved from the
     * first internal model).
     *
     * @return true if smaller values are preferred, false otherwise
     */
    @Override
    public boolean isLessPreferred()
    {
        return true;
    }

    /**
     * Can be used to set parameterize the model (implementation dependent).
     *
     * @param params new params
     */
    @Override
    public void setParams(double[][] params)
    {

    }

    /**
     * Can be used to get model parameters (implementation dependent)
     *
     * @return model params
     */
    @Override
    public double[][] getParams()
    {
        return null;
    }

    /**
     * Can be used to get model weights (implementation dependent)
     *
     * @return model weights
     */
    @Override
    public double[] getWeights()
    {
        return null;
    }

    /**
     * Can be used to get model auxiliary parameter (e.g., alpha compensation level, implementation dependent)
     *
     * @return model auxiliary parameter
     */
    @Override
    public Double getAuxParam()
    {
        return null;
    }

    /**
     * Can be used to set model auxiliary parameter (e.g., alpha compensation level, implementation dependent)
     *
     * @param param auxiliary parameter
     */
    @Override
    public void setAuxParam(double param)
    {

    }

    /**
     * Auxiliary method that can be overwritten to dispose the data.
     */
    @Override
    public void dispose()
    {

    }
}
