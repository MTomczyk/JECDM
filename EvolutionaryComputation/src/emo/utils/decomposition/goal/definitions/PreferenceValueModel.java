package emo.utils.decomposition.goal.definitions;

import emo.utils.decomposition.goal.IGoal;
import model.internals.value.AbstractValueInternalModel;
import population.Specimen;
import space.normalization.INormalization;

/**
 * Wrapper for the value preference model ({@link AbstractValueInternalModel}).
 *
 * @author MTomczyk
 */
public class PreferenceValueModel extends AbstractGoal implements IGoal
{
    /**
     * Wrapped value model.
     */
    private final AbstractValueInternalModel _model;

    /**
     * Parameterized constructor.
     *
     * @param model wrapped preference model.
     */
    public PreferenceValueModel(AbstractValueInternalModel model)
    {
        _model = model;
    }

    /**
     * Can be used to evaluate a specimen. It's wrapped alternative is passed for evaluation.
     *
     * @param specimen specimen object
     * @return specimen score
     */
    @Override
    public double evaluate(Specimen specimen)
    {
        return _model.evaluate(specimen.getAlternative());
    }

    /**
     * Can be called to update normalizations used to rescale specimen evaluations.
     *
     * @param normalizations normalization functions (one per objective)
     */
    @Override
    public void updateNormalizations(INormalization[] normalizations)
    {
        super.updateNormalizations(normalizations);
        _model.setNormalizations(normalizations);
    }

    /**
     * Implementation specific getter for data that can be used, e.g., to quantify the similarity between goals.
     *
     * @return data
     */
    @Override
    public double[][] getParams()
    {
        return _model.getParams();
    }

    /**
     * Used to determine preference direction (i.e., whether the smaller or bigger values are preferred).
     *
     * @return true -> smaller values are preferred; false otherwise.
     */
    @Override
    public boolean isLessPreferred()
    {
        return _model.isLessPreferred();
    }

    /**
     * Getter for the wrapped model.
     *
     * @return wrapped model
     */
    public AbstractValueInternalModel getModel()
    {
        return _model;
    }
}
