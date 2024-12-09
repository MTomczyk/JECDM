package model.constructor;

import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * Abstract implementation of {@link AbstractConstructor}. This extension serves as a wrapper for another object instance
 * that implements {@link IConstructor}. It overwrites all the "notify" methods so that its super implementation and
 * the wrapped constructor's implementations are called. This abstract class is supposed to be extended, and its
 * {@link AbstractWrappedConstructor#mainConstructModels(Report, LinkedList)} method to be suitably overwritten.
 *
 * @author MTomczyk
 */
public abstract class AbstractWrappedConstructor<T extends AbstractInternalModel> extends AbstractConstructor<T> implements IConstructor<T>
{
    /**
     * Wrapped constructor.
     */
    protected final IConstructor<T> _wrappedConstructor;

    /**
     * Parameterized constructor.
     *
     * @param name constructor's name
     * @param wrappedConstructor constructor object to be wrapped
     */
    protected AbstractWrappedConstructor(String name, IConstructor<T> wrappedConstructor)
    {
        super(name, null);
        _wrappedConstructor = wrappedConstructor;
    }

    /**
     * Auxiliary method that can be called to clear all stored models (can be called, e.g., when the inconsistency was detected).
     */
    @Override
    public void clearModels()
    {
        super.clearModels();
        _wrappedConstructor.clearModels();
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws ConstructorException
    {
        super.registerDecisionMakingContext(dmContext);
        _wrappedConstructor.registerDecisionMakingContext(dmContext);
    }

    /**
     * Method for notifying that the preference elicitation begins.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyPreferenceElicitationBegins() throws ConstructorException
    {
        super.notifyPreferenceElicitationBegins();
        _wrappedConstructor.notifyPreferenceElicitationBegins();
    }

    /**
     * Method for notifying that the preference elicitation failed (e.g., when there were no reasonable alternatives for comparison).
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationFailed() throws ConstructorException
    {
        super.notifyPreferenceElicitationFailed();
        _wrappedConstructor.notifyPreferenceElicitationFailed();
    }

    /**
     * Method for notifying on the most recent preference information provided by the decision maker.
     *
     * @param preferenceInformation preference information (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyAboutMostRecentPreferenceInformation(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        super.notifyAboutMostRecentPreferenceInformation(preferenceInformation);
        _wrappedConstructor.notifyAboutMostRecentPreferenceInformation(preferenceInformation);
    }

    /**
     * Method for notifying that the preference elicitation ends.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyPreferenceElicitationEnds() throws ConstructorException
    {
        super.notifyPreferenceElicitationEnds();
        _wrappedConstructor.notifyPreferenceElicitationEnds();
    }

    /**
     * Method for notifying that the internal models construction begins.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyModelsConstructionBegins() throws ConstructorException
    {
        super.notifyModelsConstructionBegins();
        _wrappedConstructor.notifyModelsConstructionBegins();
    }


    /**
     * Method for notifying that the internal models construction ends.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyModelsConstructionEnds() throws ConstructorException
    {
        super.notifyModelsConstructionEnds();
        _wrappedConstructor.notifyModelsConstructionEnds();
    }

    /**
     * Method for notifying that the consistency reintroduction begins.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionBegins() throws ConstructorException
    {
        super.notifyConsistencyReintroductionBegins();
        _wrappedConstructor.notifyPreferenceElicitationBegins();
    }

    /**
     * Method for notifying about the beginning of an attempt to reintroduce consistency (one construction process is called in one attempt)
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionAttemptBegins() throws ConstructorException
    {
        super.notifyConsistencyReintroductionAttemptBegins();
        _wrappedConstructor.notifyConsistencyReintroductionAttemptBegins();
    }


    /**
     * Method for notifying about preference examples removed from the initial set when attempting to reinstate consistency.
     *
     * @param removedPreferenceInformation removed preference examples (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyAboutRemovedPreferenceInformation(LinkedList<PreferenceInformationWrapper> removedPreferenceInformation) throws ConstructorException
    {
        super.notifyAboutRemovedPreferenceInformation(removedPreferenceInformation);
        _wrappedConstructor.notifyAboutRemovedPreferenceInformation(removedPreferenceInformation);
    }


    /**
     * Method for notifying about preference examples added from the preference examples set when attempting to reinstate consistency.
     *
     * @param addedPreferenceInformation added preference examples (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyAboutAddedPreferenceInformation(LinkedList<PreferenceInformationWrapper> addedPreferenceInformation) throws ConstructorException
    {
        super.notifyAboutAddedPreferenceInformation(addedPreferenceInformation);
        _wrappedConstructor.notifyAboutAddedPreferenceInformation(addedPreferenceInformation);
    }

    /**
     * Method for notifying about the ending of an attempt to reintroduce consistency (one construction process is called in one attempt)
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionAttemptEnds() throws ConstructorException
    {
        super.notifyConsistencyReintroductionAttemptEnds();
        _wrappedConstructor.notifyConsistencyReintroductionAttemptEnds();
    }

    /**
     * Method for notifying that the consistency reintroduction ends.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionEnds() throws ConstructorException
    {
        super.notifyConsistencyReintroductionEnds();
        _wrappedConstructor.notifyConsistencyReintroductionEnds();
    }


    /**
     * Unregisters the decision-making context
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void unregisterDecisionMakingContext() throws ConstructorException
    {
        super.unregisterDecisionMakingContext();
        _wrappedConstructor.unregisterDecisionMakingContext();
    }
}
