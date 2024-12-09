package emo.utils.decomposition.goal.definitions;

import emo.utils.decomposition.goal.IGoal;
import population.Specimen;
import space.normalization.INormalization;
import space.scalarfunction.IScalarizingFunction;

/**
 * General, abstract class implementing {@link IGoal} interface.
 * It can be considered a wrapper for {@link IScalarizingFunction} object.
 *
 * @author MTomczyk
 */

public abstract class AbstractScalarGoal extends AbstractGoal implements IGoal
{
    /**
     * Scalar function.
     */
    protected final IScalarizingFunction _f;

    /**
     * Parameterized constructor.
     *
     * @param scalarFunction scalar function used to assess specimens.
     */
    protected AbstractScalarGoal(IScalarizingFunction scalarFunction)
    {
        _f = scalarFunction;
    }

    /**
     * Can be used to evaluate a specimen.
     *
     * @param specimen specimen object
     * @return specimen score
     */
    @Override
    public double evaluate(Specimen specimen)
    {
        return _f.evaluate(specimen.getEvaluations());
    }

    /**
     * Can be called to update normalizations used to rescale (normalize) specimen evaluations
     *
     * @param normalizations normalization functions (one per objective)
     */
    @Override
    public void updateNormalizations(INormalization[] normalizations)
    {
        super.updateNormalizations(normalizations);
        _f.setNormalizations(normalizations);
    }

    /**
     * Implementation specific getter for data that can be used, e.g., to quantify the similarity between goals.
     *
     * @return data
     */
    @Override
    public double[][] getParams()
    {
        return _f.getParams();
    }

}
