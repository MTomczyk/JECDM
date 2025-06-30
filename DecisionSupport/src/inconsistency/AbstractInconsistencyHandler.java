package inconsistency;

import dmcontext.DMContext;
import exeption.ConstructorException;
import exeption.InconsistencyHandlerException;
import history.History;
import history.PreferenceInformationWrapper;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * Abstract implementation of {@link IInconsistencyHandler}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractInconsistencyHandler<T extends AbstractInternalModel> implements IInconsistencyHandler<T>
{
    /**
     * Handler's name (string representation).
     */
    protected final String _name;

    /**
     * If true, all the model bundles generated during consistency reintroduction are collected and returned in a report.
     */
    protected final boolean _storeAllStates;

    /**
     * Current decision-making context.
     */
    protected DMContext _dmContext;

    /**
     * Parameterized constructor.
     *
     * @param name                          handler's name
     * @param storeAllModelBundlesGenerated if true, all the model bundles generated during consistency reintroduction are collected and returned in a report
     */
    public AbstractInconsistencyHandler(String name, boolean storeAllModelBundlesGenerated)
    {
        _name = name;
        _storeAllStates = storeAllModelBundlesGenerated;
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext current decision-making context
     * @throws InconsistencyHandlerException the exception can be thrown 
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws InconsistencyHandlerException
    {
        _dmContext = dmContext;
    }

    /**
     * Performs basic data validation.
     *
     * @param modelBundle           bundle result of the attempt to construct models (see {@link Report}),
     *                              it is assumed that this bundle indicates inconsistency ({@link Report#_inconsistencyDetected}).
     * @param preferenceInformation current preference information (copied from the model system) that can be modified to reintroduce consistency (altered set should be stored in the report);
     *                              the list is derived via {@link History#getPreferenceInformationCopy()}, thus it is valid (e.g., preference statements are ordered from the oldest to the newest)
     * @throws InconsistencyHandlerException exception will be thrown if the data is invalid
     */
    protected void doBasicValidation(Report<T> modelBundle,
                                     LinkedList<PreferenceInformationWrapper> preferenceInformation)
            throws InconsistencyHandlerException
    {
        if (!modelBundle._inconsistencyDetected)
            throw new InconsistencyHandlerException("Called for inconsistency reintroduction, but the input model bundle reports consistency", this.getClass());

        if (preferenceInformation.isEmpty())
            throw new InconsistencyHandlerException("The history of preference elicitation is empty (no reason to reintroduce consistency)", this.getClass());
    }

    /**
     * Auxiliary method for notifying about the beginning of the attempt to reintroduce consistency.
     *
     * @param constructor constructor object
     * @throws InconsistencyHandlerException exception can be thrown 
     */
    protected void notifyAboutAttemptBeginning(IConstructor<T> constructor) throws InconsistencyHandlerException
    {
        try
        {
            constructor.notifyConsistencyReintroductionAttemptBegins();
        } catch (ConstructorException e)
        {
            throw new InconsistencyHandlerException("Notification on the beginning of the attempt to reintroduce consistency failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Supportive method that constructs candidate model bundle.
     *
     * @param constructor          constructor object
     * @param candidateHistoryCopy current state's preference information
     * @param report               report to be filled
     * @param currentAttempt       current attempt number
     * @return candidate model
     * @throws InconsistencyHandlerException exception can be thrown 
     */
    protected Report<T> getCandidateBundle(IConstructor<T> constructor,
                                           LinkedList<PreferenceInformationWrapper> candidateHistoryCopy,
                                           inconsistency.Report<T> report,
                                           int currentAttempt) throws InconsistencyHandlerException
    {
        Report<T> candidateState;
        try
        {
            candidateState = constructor.constructModels(candidateHistoryCopy);
        } catch (ConstructorException e)
        {
            throw new InconsistencyHandlerException("Failed to construct a new model bundle " + e.getDetailedReasonMessage(), this.getClass(), e);
        }

        if (_storeAllStates)
            report._states.add(new State<>(candidateState, new LinkedList<>(candidateHistoryCopy), currentAttempt));

        return candidateState;
    }

    /**
     * Auxiliary method for notifying about the ending of the attempt to reintroduce consistency.
     *
     * @param constructor constructor object
     * @throws InconsistencyHandlerException exception can be thrown 
     */
    protected void notifyAboutAttemptEnding(IConstructor<T> constructor) throws InconsistencyHandlerException
    {
        try
        {
            constructor.notifyConsistencyReintroductionAttemptBegins();
        } catch (ConstructorException e)
        {
            throw new InconsistencyHandlerException("Notification on the ending of the attempt to reintroduce consistency failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Auxiliary method that can be used to unregister the current decision-making context {@link DMContext}.
     */
    @Override
    public void unregisterDecisionMakingContext()
    {
        _dmContext = null;
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

}
