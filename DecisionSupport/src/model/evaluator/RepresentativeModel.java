package model.evaluator;

import alternative.Alternative;
import exeption.PreferenceModelException;
import model.internals.IInternalModel;

import java.util.ArrayList;

/**
 * This implementation evaluates alternatives using only the first stored internal model (assumed to be a representative model).
 *
 * @author MTomczyk
 */
public class RepresentativeModel<T extends IInternalModel> extends AbstractEvaluator<T> implements IEvaluator<T>
{
    /**
     * The main method for evaluating an alternative. It uses the first internal model stored (representative model).
     *
     * @param alternative the alternative to be evaluated
     * @param models      internal models used for evaluation
     * @return attained score
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    @Override
    public double evaluate(Alternative alternative, ArrayList<T> models) throws PreferenceModelException
    {
        return models.get(0).evaluate(alternative);
    }
}
