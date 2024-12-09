package system.model;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import dmcontext.DMContext;
import exeption.*;
import history.History;
import history.PreferenceInformationWrapper;
import inconsistency.IInconsistencyHandler;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.evaluator.EvaluationResult;
import model.internals.AbstractInternalModel;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * A class representing a model-level processing (for one decision maker). It is centrally focused on updating the
 * assumed preference model, given the preference examples provided by the decision maker, and handling potential inconsistencies.
 *
 * @author MTomczyk
 */
public class ModelSystem<T extends AbstractInternalModel>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractInternalModel>
    {
        /**
         * Preference model used to represent the decision maker's preferences.
         */
        public IPreferenceModel<T> _preferenceModel;

        /**
         * Preference model constructor.
         */
        public IConstructor<T> _modelConstructor;

        /**
         * Object responsible for handling inconsistency that can occur when building the model(s) being in line with the
         * decision maker's aspirations.
         */
        public IInconsistencyHandler<T> _inconsistencyHandler;
    }

    /**
     * Preference model used to represent the decision maker's preferences.
     */
    private final IPreferenceModel<T> _preferenceModel;

    /**
     * Preference model constructor.
     */
    private final IConstructor<T> _modelConstructor;

    /**
     * Object responsible for handling inconsistency that can occur when building the model(s) being in line with the
     * decision maker's aspirations.
     */
    private final IInconsistencyHandler<T> _inconsistencyHandler;

    /**
     * Current decision-making context.
     */
    private DMContext _dmContext;

    /**
     * History of preference elicitation associated with the model.
     */
    private final History _history;

    /**
     * Counter that represents how many attempts (to construct internal models) initially failed.
     */
    protected int _noFailedAttemptsDueToInconsistency = 0;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public ModelSystem(Params<T> p) throws ModelSystemException
    {
        if (p._preferenceModel == null)
            throw new ModelSystemException("The preference model is not provided", this.getClass());
        if (p._modelConstructor == null)
            throw new ModelSystemException("The model constructor is not provided", this.getClass());
        if (p._inconsistencyHandler == null)
            throw new ModelSystemException("The inconsistency handler is not provided", this.getClass());

        _preferenceModel = p._preferenceModel;
        _modelConstructor = p._modelConstructor;
        _inconsistencyHandler = p._inconsistencyHandler;
        _history = new History("History for " + _preferenceModel);
        _noFailedAttemptsDueToInconsistency = 0;
    }

    /**
     * Parameterized constructor.
     *
     * @param modelBundle model bundle used to parameterize the system
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public ModelSystem(system.ds.ModelBundle<T> modelBundle) throws ModelSystemException
    {
        if (modelBundle._preferenceModel == null)
            throw new ModelSystemException("The preference model is not provided", this.getClass());
        if (modelBundle._modelConstructor == null)
            throw new ModelSystemException("The model constructor is not provided", this.getClass());
        if (modelBundle._inconsistencyHandler == null)
            throw new ModelSystemException("The inconsistency handler is not provided", this.getClass());

        _preferenceModel = modelBundle._preferenceModel;
        _modelConstructor = modelBundle._modelConstructor;
        _inconsistencyHandler = modelBundle._inconsistencyHandler;
        _history = new History("History of " + _preferenceModel);

    }


    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext current decision-making context
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public void registerDecisionMakingContext(DMContext dmContext) throws ModelSystemException
    {
        try
        {
            _dmContext = dmContext;
            _preferenceModel.registerDecisionMakingContext(dmContext);
            _modelConstructor.registerDecisionMakingContext(dmContext);
            _inconsistencyHandler.registerDecisionMakingContext(dmContext);
        } catch (PreferenceModelException | ConstructorException | InconsistencyHandlerException e)
        {
            throw new ModelSystemException("Error occurred when registering decision-making context " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Auxiliary method that can be used to unregister the current decision-making context {@link DMContext}.
     *
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public void unregisterDecisionMakingContext() throws ModelSystemException
    {
        try
        {
            _dmContext = null;
            _preferenceModel.unregisterDecisionMakingContext();
            _modelConstructor.unregisterDecisionMakingContext();
            _inconsistencyHandler.unregisterDecisionMakingContext();
        } catch (PreferenceModelException | ConstructorException e)
        {
            throw new ModelSystemException("Error occurred when unregistering decision-making context " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying that the preference elicitation begins.
     *
     * @throws ModelSystemException the exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationBegins() throws ModelSystemException
    {
        try
        {
            _modelConstructor.notifyPreferenceElicitationBegins();
        } catch (ConstructorException e)
        {
            throw new ModelSystemException("Notification about the beginning of the preference elicitation failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying about the most recent preference information provided by the decision maker.
     *
     * @param preferenceInformation preference information (provided via wrapper)
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public void notifyAboutMostRecentPreferenceInformation(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ModelSystemException
    {
        try
        {
            _history.registerPreferenceInformation(preferenceInformation);
            _modelConstructor.notifyAboutMostRecentPreferenceInformation(preferenceInformation);
        } catch (ConstructorException e)
        {
            throw new ModelSystemException("Notification about the most recent preference information failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        } catch (HistoryException e)
        {
            throw new ModelSystemException("Could not register preference information in model history " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying that the preference elicitation failed (e.g., when there were no reasonable alternatives for comparison).
     *
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationFailed() throws ModelSystemException
    {
        try
        {
            _modelConstructor.notifyPreferenceElicitationFailed();
        } catch (ConstructorException e)
        {
            throw new ModelSystemException("Notification about a failure during preference elicitation failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Method for notifying that the preference elicitation ends.
     *
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationEnds() throws ModelSystemException
    {
        try
        {
            _modelConstructor.notifyPreferenceElicitationEnds();
        } catch (ConstructorException e)
        {
            throw new ModelSystemException("Notification about an end of the preference elicitation step failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * The main method for updating the model given the history of preference elicitation.
     *
     * @return report on the model update
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public system.model.Report<T> updateModel() throws ModelSystemException
    {
        system.model.Report<T> report = new system.model.Report<>(_dmContext, _preferenceModel.getName());
        long startTime = System.nanoTime();

        try
        {
            _modelConstructor.notifyModelsConstructionBegins();
            Report<T> constructReport = _modelConstructor.constructModels(_history.getPreferenceInformationCopy());
            report._report = constructReport;

            if (constructReport._inconsistencyDetected)
            {
                _noFailedAttemptsDueToInconsistency++;
                report._inconsistencyOccurred = true;
                _modelConstructor.notifyConsistencyReintroductionBegins();
                _modelConstructor.clearModels();

                inconsistency.Report<T> iReport = _inconsistencyHandler.reintroduceConsistency(constructReport, _modelConstructor, _history.getPreferenceInformationCopy());

                if (iReport._consistentState == null)
                    throw new InconsistencyHandlerException("No consistent state was provided", this.getClass());
                if (iReport._consistentState._report == null)
                    throw new InconsistencyHandlerException("No consistent model bundle was constructed", this.getClass());
                if (iReport._consistentState._report._models == null)
                    throw new InconsistencyHandlerException("No consistent models were constructed (the array is null)", this.getClass());
                if (iReport._consistentState._report._models.isEmpty())
                    throw new InconsistencyHandlerException("No consistent models were constructed (the array is empty)", this.getClass());

                report._reportOnInconsistencyHandling = iReport;
                report._report = iReport._consistentState._report;
                _history.updateHistoryWithASubset(iReport._consistentState._preferenceInformation, _dmContext.getCurrentIteration(), LocalDateTime.now());

                _preferenceModel.setInternalModels(iReport._consistentState._report._models);
                _modelConstructor.notifyConsistencyReintroductionEnds();
            }
            else
            {
                _preferenceModel.setInternalModels(constructReport._models);
            }
            _modelConstructor.notifyModelsConstructionEnds();

        } catch (ConstructorException | HistoryException | InconsistencyHandlerException e)
        {
            throw new ModelSystemException("Could not update the preference model " + e.getDetailedReasonMessage(), this.getClass(), e);
        }

        report._processingTime = (System.nanoTime() - startTime) / 1000000;
        return report;
    }


    /**
     * The main method for evaluating an alternative. Note that it is preferred to use {@link ModelSystem#evaluateAlternatives(AbstractAlternatives)}
     * as it may speed up calculations, may be the only implemented method, or can provide additional evaluation results
     * (implementation dependent).
     *
     * @param alternative the alternative to be evaluated
     * @return attained score
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public double evaluate(Alternative alternative) throws ModelSystemException
    {
        if (_preferenceModel == null) throw new ModelSystemException("The preference model is null", this.getClass());
        try
        {
            return _preferenceModel.evaluate(alternative);
        } catch (PreferenceModelException e)
        {
            throw new ModelSystemException("Error occurred when evaluating an alternative " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * The main method for evaluating alternatives.
     *
     * @param alternatives alternatives to be evaluated
     * @return attained scores (each element corresponds to a different alternative, 1:1 connection with registered alternatives)
     * @throws ModelSystemException model system exception can be thrown and propagated higher
     */
    public EvaluationResult evaluateAlternatives(AbstractAlternatives<?> alternatives) throws ModelSystemException
    {
        if (_preferenceModel == null) throw new ModelSystemException("The preference model is null", this.getClass());
        try
        {
            return _preferenceModel.evaluateAlternatives(alternatives);
        } catch (PreferenceModelException e)
        {
            throw new ModelSystemException("Error occurred when evaluating alternatives " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }


    /**
     * Getter for the preference elicitation history (associated with the model).
     *
     * @return preference elicitation history (associated with the model)
     */
    public History getHistory()
    {
        return _history;
    }

    /**
     * Getter for the preference model.
     *
     * @return preference model
     */
    public IPreferenceModel<T> getPreferenceModel()
    {
        return _preferenceModel;
    }

    /**
     * Getter for the model constructor.
     *
     * @return preference model
     */
    public IConstructor<T> getModelConstructor()
    {
        return _modelConstructor;
    }

    /**
     * Returns a counter that represents how many attempts (to construct internal models) initially failed.
     *
     * @return counter that represents how many attempts (to construct internal models) initially failed
     */
    public int getNoFailedAttemptsDueToInconsistency()
    {
        return _noFailedAttemptsDueToInconsistency;
    }
}
