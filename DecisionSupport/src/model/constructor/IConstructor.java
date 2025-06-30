package model.constructor;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * General interface for classes responsible for constructing (internal) models given the decision maker's preference
 * information provided.
 *
 * @author MTomczyk
 */
public interface IConstructor<T extends AbstractInternalModel>
{
    /**
     * Auxiliary method that can be called to clear all stored models (can be called, e.g., when the inconsistency was detected).
     */
    void clearModels();

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws ConstructorException the exception can be thrown 
     */
    void registerDecisionMakingContext(DMContext dmContext) throws ConstructorException;

    /**
     * Method for notifying that the preference elicitation begins
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyPreferenceElicitationBegins() throws ConstructorException;

    /**
     * Method for notifying on the most recent preference information provided by the decision maker.
     *
     * @param preferenceInformation preference information (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyAboutMostRecentPreferenceInformation(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException;

    /**
     * Method for notifying that the preference elicitation failed (e.g., when there were no reasonable alternatives for comparison).
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyPreferenceElicitationFailed() throws ConstructorException;

    /**
     * Method for notifying that the preference elicitation ends.
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyPreferenceElicitationEnds() throws ConstructorException;

    /**
     * Method for notifying that the internal models construction begins.
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyModelsConstructionBegins() throws ConstructorException;

    /**
     * The main method for constructing internal models (array) given the decision maker's preference information.
     *
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return constructed models (bundle object that also provides additional data)
     * @throws ConstructorException the exception can be thrown 
     */
    Report<T> constructModels(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException;

    /**
     * Method for notifying that the internal models construction ends.
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyModelsConstructionEnds() throws ConstructorException;

    /**
     * Method for notifying that the consistency reintroduction begins.
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyConsistencyReintroductionBegins() throws ConstructorException;

    /**
     * Method for notifying about the beginning of an attempt to reintroduce consistency (one construction process is called in one attempt)
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyConsistencyReintroductionAttemptBegins() throws ConstructorException;

    /**
     * Method for notifying about preference examples removed from the initial set when attempting to reinstate consistency.
     *
     * @param removedPreferenceInformation removed preference examples (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyAboutRemovedPreferenceInformation(LinkedList<PreferenceInformationWrapper> removedPreferenceInformation) throws ConstructorException;

    /**
     * Method for notifying about preference examples added from the preference examples set when attempting to reinstate consistency.
     *
     * @param addedPreferenceInformation added preference examples (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyAboutAddedPreferenceInformation(LinkedList<PreferenceInformationWrapper> addedPreferenceInformation) throws ConstructorException;

    /**
     * Method for notifying about the ending of an attempt to reintroduce consistency (one construction process is called in one attempt)
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyConsistencyReintroductionAttemptEnds() throws ConstructorException;

    /**
     * Method for notifying that the consistency reintroduction ends.
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void notifyConsistencyReintroductionEnds() throws ConstructorException;

    /**
     * Unregisters the decision-making context
     *
     * @throws ConstructorException the exception can be thrown 
     */
    void unregisterDecisionMakingContext() throws ConstructorException;


    /**
     * Getter for compatibility analyzer.
     *
     * @return compatibility analyzer
     */
    CompatibilityAnalyzer getCompatibilityAnalyzer();
}
