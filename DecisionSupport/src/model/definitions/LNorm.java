package model.definitions;

import model.IPreferenceModel;
import model.evaluator.IEvaluator;
import model.evaluator.RepresentativeModel;

/**
 * Implementation of {@link IPreferenceModel} that uses L-norms to represent the decision maker's preferences.
 *
 * @author MTomczyk
 */
public class LNorm extends ScalarizingFunction<model.internals.value.scalarizing.LNorm>
        implements IPreferenceModel<model.internals.value.scalarizing.LNorm>
{
    /**
     * Default constructor. Uses {@link RepresentativeModel} when evaluating solutions.
     */
    public LNorm()
    {
        super("L-norm", null, new RepresentativeModel<>());
    }

    /**
     * Parameterized constructor. Uses {@link RepresentativeModel} when evaluating solutions.
     *
     * @param internalModel internal model used
     */
    public LNorm(model.internals.value.scalarizing.LNorm internalModel)
    {
        this("L-norm", internalModel, new RepresentativeModel<>());
    }


    /**
     * Parameterized constructor.
     *
     * @param evaluator auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    public LNorm(IEvaluator<model.internals.value.scalarizing.LNorm> evaluator)
    {
        this("L-norm", null, evaluator);
    }

    /**
     * Parameterized constructor.
     *
     * @param internalModel internal model used
     * @param evaluator     auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    public LNorm(model.internals.value.scalarizing.LNorm internalModel, IEvaluator<model.internals.value.scalarizing.LNorm> evaluator)
    {
        this("L-norm", internalModel, evaluator);
    }

    /**
     * Parameterized constructor.
     *
     * @param name          model's name
     * @param internalModel internal model used
     * @param evaluator     auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    protected LNorm(String name, model.internals.value.scalarizing.LNorm internalModel, IEvaluator<model.internals.value.scalarizing.LNorm> evaluator)
    {
        super(name, internalModel, evaluator);
    }
}
