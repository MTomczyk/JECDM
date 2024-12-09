package model.internals.value.scalarizing;

import alternative.Alternative;
import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;
import model.internals.value.AbstractValueInternalModel;
import space.normalization.INormalization;
import space.scalarfunction.IScalarizingFunction;

/**
 * Extension of {@link AbstractInternalModel} dedicated to scalarizing functions
 * {@link IScalarizingFunction}.
 *
 * @author MTomczyk
 */
public abstract class AbstractScalarizingFunctionInternalModel extends AbstractValueInternalModel implements IInternalModel
{
    /**
     * Scalar function.
     */
    protected final IScalarizingFunction _f;

    /**
     * Parameterized constructor.
     *
     * @param name model name
     * @param f    scalar function used
     */
    public AbstractScalarizingFunctionInternalModel(String name, IScalarizingFunction f)
    {
        super(name);
        _f = f;
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
        return _f.evaluate(alternative.getPerformanceVector());
    }

    /**
     * The main method for setting new normalizations (used to rescale alternative evaluations given the considered criteria).
     *
     * @param normalizations normalizations used to rescale the dimensions
     */
    @Override
    public void setNormalizations(INormalization[] normalizations)
    {
        _f.setNormalizations(normalizations);
    }

    /**
     * Can be used to set parameterize the model (implementation dependent).
     *
     * @param params new params
     */
    @Override
    public void setParams(double[][] params)
    {
        _f.setParams(params);
    }

    /**
     * Can be used to get model parameters (implementation dependent)
     *
     * @return model params
     */
    @Override
    public double[][] getParams()
    {
        return _f.getParams();
    }

    /**
     * Can be used to get model weights (implementation dependent)
     *
     * @return model weights
     */
    @Override
    public double[] getWeights()
    {
        return _f.getWeights();
    }

    /**
     * Can be used to get model auxiliary parameter (e.g., alpha compensation level, implementation dependent)
     *
     * @return model auxiliary parameter
     */
    @Override
    public Double getAuxParam()
    {
        return _f.getAuxParam();
    }

    /**
     * Can be used to set model auxiliary parameter (e.g., alpha compensation level, implementation dependent)
     *
     * @param param auxiliary parameter
     */
    @Override
    public void setAuxParam(double param)
    {
        _f.setAuxParam(param);
    }
}
