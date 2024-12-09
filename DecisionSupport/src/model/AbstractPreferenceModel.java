package model;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import dmcontext.DMContext;
import exeption.PreferenceModelException;
import model.evaluator.EvaluationResult;
import model.evaluator.IEvaluator;
import model.evaluator.RepresentativeModel;
import model.internals.IInternalModel;

import java.util.ArrayList;

/**
 * Default, abstract implementation of {@link IPreferenceModel}. Contains common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractPreferenceModel<T extends IInternalModel> implements IPreferenceModel<T>
{
    /**
     * The name of the preference model.
     */
    protected final String _name;

    /**
     * This array stores the internal models (extending {@link IInternalModel}.
     */
    protected ArrayList<T> _models;

    /**
     * Auxiliary model for evaluating an alternative given the internal model (models) stored.
     */
    protected final IEvaluator<T> _evaluator;

    /**
     * Parameterized constructor. Uses {@link RepresentativeModel} when evaluating solutions.
     *
     * @param name the name of the preference model
     */
    public AbstractPreferenceModel(String name)
    {
        this(name, new RepresentativeModel<>());
    }

    /**
     * Parameterized constructor.
     *
     * @param name      the name of the preference model
     * @param evaluator auxiliary model for evaluating an alternative given the internal model (models) stored
     */
    public AbstractPreferenceModel(String name, IEvaluator<T> evaluator)
    {
        _name = name;
        _evaluator = evaluator;
    }


    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws PreferenceModelException
    {
        if (dmContext == null) throw new PreferenceModelException("The input decision-making context is null", this.getClass());
        if (_evaluator == null) throw new PreferenceModelException("No evaluator is provided", this.getClass());
        _evaluator.registerAlternativesSuperset(dmContext.getCurrentAlternativesSuperset());
    }

    /**
     * Unregisters the decision-making context
     *
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    @Override
    public void unregisterDecisionMakingContext() throws PreferenceModelException
    {
        _evaluator.unregisterAlternativesSuperset();
    }

    /**
     * The main method for evaluating an alternative. Note that it is preferred to use {@link IPreferenceModel#evaluateAlternatives(AbstractAlternatives)}
     * as it may speed up calculations, may be the only implemented method, or can provide additional evaluation results
     * (implementation dependent). The evaluation is delegated to {@link IEvaluator}.
     *
     * @param alternative the alternative to be evaluated
     * @return attained score
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    @Override
    public double evaluate(Alternative alternative) throws PreferenceModelException
    {
        if ((_models == null) || (_models.isEmpty()))
            throw new PreferenceModelException("No internal models are specified", this.getClass());
        if (_evaluator == null) throw new PreferenceModelException("No evaluator is provided", this.getClass());
        return _evaluator.evaluate(alternative, _models);
    }


    /**
     * The main method for evaluating alternatives. The evaluation is delegated to {@link IEvaluator}.
     *
     * @return attained scores (each element corresponds to a different alternative, 1:1 connection with registered alternatives)
     * @throws PreferenceModelException the exception can be thrown and propagated higher
     */
    @Override
    public EvaluationResult evaluateAlternatives(AbstractAlternatives<?> alternatives) throws PreferenceModelException
    {
        if ((_models == null) || (_models.isEmpty()))
            throw new PreferenceModelException("No internal models are specified", this.getClass());
        if (_evaluator == null) throw new PreferenceModelException("No evaluator is provided", this.getClass());
        return _evaluator.evaluateAlternatives(alternatives, _models);
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
        T im = getInternalModel();
        if (im != null) return im.isLessPreferred();
        return true;
    }

    /**
     * Auxiliary method for setting the internal models. Multiple internal models can be used within one instance of a
     * top-level preference model. This way, various possible formulations of the decision maker's preference can be wrapped.
     *
     * @param models internal models to be set
     */
    @Override
    public void setInternalModels(ArrayList<T> models)
    {
        _models = models;
    }

    /**
     * Setter for the internal model. The method overwrites all previously stored models.
     *
     * @param model internal model to be set
     */
    @Override
    public void setInternalModel(T model)
    {
        if (_models == null)
        {
            _models = new ArrayList<>(1);
            _models.add(model);
        }
        else if (_models.size() > 1)
        {
            for (IInternalModel m : _models) m.dispose();
            _models = new ArrayList<>(1);
            _models.add(model);
        }
        else _models.set(0, model);
    }

    /**
     * Getter for internal models.
     *
     * @return internal models
     */
    @Override
    public ArrayList<T> getInternalModels()
    {
        return _models;
    }

    /**
     * Getter for the internal model. In the case of storing multiple of these,
     * the first one is returned.
     *
     * @return internal model
     */
    @Override
    public T getInternalModel()
    {
        if (_models == null) return null;
        return _models.get(0);
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }


    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String getName()
    {
        return _name;
    }
}
