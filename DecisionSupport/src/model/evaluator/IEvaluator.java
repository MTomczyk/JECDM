package model.evaluator;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import exeption.PreferenceModelException;
import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;

import java.util.ArrayList;

/**
 * Interfaces for auxiliary objects responsible for evaluating alternatives given the provided internal models
 * (extensions of {@link AbstractInternalModel}).
 *
 * @author MTomczyk
 */
public interface IEvaluator<T extends IInternalModel>
{
    /**
     * Auxiliary method that can be used to register alternatives for evaluation. Note that it is supposed that the
     * alternatives registered via this method constitute a superset (or at least equal set) to the alternatives
     * evaluated via {@link IEvaluator#evaluateAlternatives(AbstractAlternatives, ArrayList)}.
     *
     * @param alternatives alternatives to be evaluated
     * @throws PreferenceModelException the exception can be thrown 
     */
    void registerAlternativesSuperset(AbstractAlternatives<?> alternatives) throws PreferenceModelException;


    /**
     * Auxiliary method that can be used to unregister the alternatives' superset.
     */
    void unregisterAlternativesSuperset();

    /**
     * The main method for evaluating an alternative. Note that it is preferred to use {@link IEvaluator#evaluateAlternatives(AbstractAlternatives, ArrayList)}}
     * as it may speed up calculations, may be the only implemented method, or can provide additional evaluation results
     * (implementation dependent).
     *
     * @param alternative the alternative to be evaluated
     * @param models      internal models used for evaluation
     * @return attained score
     * @throws PreferenceModelException the exception can be thrown 
     */
    double evaluate(Alternative alternative, ArrayList<T> models) throws PreferenceModelException;

    /**
     * The main method for evaluating alternatives.
     *
     * @param alternatives alternatives to be evaluated
     * @param models       internal models used for evaluation
     * @return attained scores (each element corresponds to a different alternative, 1:1 connection) wrapped via {@link EvaluationResult}
     * @throws PreferenceModelException the exception can be thrown 
     */
    EvaluationResult evaluateAlternatives(AbstractAlternatives<?> alternatives, ArrayList<T> models) throws PreferenceModelException;
}
