package model.definitions;

import model.AbstractPreferenceModel;
import model.IPreferenceModel;
import model.evaluator.IEvaluator;
import model.internals.value.scalarizing.AbstractScalarizingFunctionInternalModel;

/**
 * General definition for scalarizing functions ({@link AbstractScalarizingFunctionInternalModel}).
 *
 * @author MTomczyk
 */
public class ScalarizingFunction<T extends AbstractScalarizingFunctionInternalModel> extends AbstractPreferenceModel<T>
        implements IPreferenceModel<T>
{
    /**
     * Parameterized constructor.
     *
     * @param evaluator auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    public ScalarizingFunction(IEvaluator<T> evaluator)
    {
        this("Preference model (scalarizing functions)", null, evaluator);
    }

    /**
     * Parameterized constructor.
     *
     * @param internalModel internal model used
     * @param evaluator     auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    public ScalarizingFunction(T internalModel, IEvaluator<T> evaluator)
    {
        this("Preference model (scalarizing functions)", internalModel, evaluator);
    }

    /**
     * Parameterized constructor.
     *
     * @param name          model's name
     * @param internalModel internal model used
     * @param evaluator     auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    protected ScalarizingFunction(String name, T internalModel, IEvaluator<T> evaluator)
    {
        super("Preference model (" + name + ")", evaluator);
        if (internalModel != null) setInternalModel(internalModel);
    }

}
