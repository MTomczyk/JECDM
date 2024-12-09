package model;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import dmcontext.DMContext;
import exeption.PreferenceModelException;
import model.evaluator.EvaluationResult;
import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;

import java.util.ArrayList;

/**
 * The top-level interface for preference models (objects responsible for mathematically modeling the decision maker's
 * preferences). The interface is generic and has to be parameterized with a type representing the internal model
 * (e.g., L-norm, value function, etc.) that has to extend {@link AbstractInternalModel}.
 *
 * @author MTomczyk
 */
public interface IPreferenceModel<T extends IInternalModel>
{
    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    void registerDecisionMakingContext(DMContext dmContext) throws PreferenceModelException;

    /**
     * The main method for evaluating an alternative. Note that it is preferred to use {@link IPreferenceModel#evaluateAlternatives(AbstractAlternatives)}
     * as it may speed up calculations, may be the only implemented method, or can provide additional evaluation results
     * (implementation dependent).
     *
     * @param alternative the alternative to be evaluated
     * @return attained score
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    double evaluate(Alternative alternative) throws PreferenceModelException;

    /**
     * The main method for evaluating alternatives.
     *
     * @param alternatives alternatives to be evaluated
     * @return evaluation result object
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    EvaluationResult evaluateAlternatives(AbstractAlternatives<?> alternatives) throws PreferenceModelException;

    /**
     * Auxiliary method that allows determining the model preference direction (dedicated to value models; default
     * implementation = the flag is retrieved from the first internal model).
     *
     * @return true if smaller values are preferred, false otherwise
     */
    boolean isLessPreferred();

    /**
     * Auxiliary method for setting the internal models. Multiple internal models can be used within one instance of a
     * top-level preference model. This way, various possible formulations of the decision maker's preference can be wrapped.
     *
     * @param models internal models to be set
     */
    void setInternalModels(ArrayList<T> models);

    /**
     * Setter for the internal model. The method should overwrite all previously stored models.
     *
     * @param model internal model to be set
     */
    void setInternalModel(T model);

    /**
     * Getter for internal models.
     *
     * @return internal models
     */
    ArrayList<T> getInternalModels();

    /**
     * Getter for the internal model. In the case of storing multiple of these, the first one is expected to be returned.
     *
     * @return internal model
     */
    T getInternalModel();

    /**
     * Unregisters the decision-making context
     *
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    void unregisterDecisionMakingContext() throws PreferenceModelException;

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    String toString();


    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    String getName();
}
