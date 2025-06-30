package interaction.feedbackprovider.dm;

import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import interaction.reference.ReferenceSets;
import system.dm.DM;


/**
 * Interface for classes responsible for constructing feedback ({@link preference.IPreferenceInformation}) using the
 * constructed reference sets (each object is supposed to be linked with a single decision maker).
 *
 * @author MTomczyk
 */
public interface IDMFeedbackProvider
{
    /**
     * Auxiliary method for registering a DM (should be called before {@link IDMFeedbackProvider#getFeedback(ReferenceSets)}).
     *
     * @param dm the decision maker to be registered
     * @throws FeedbackProviderException the exception can be thrown 
     */
    void registerDM(DM dm) throws FeedbackProviderException;

    /**
     * Auxiliary method for unregistering the DM.
     */
    void unregisterDM();

    /**
     * Auxiliary method for registering the current decision-making context (should be called before {@link IDMFeedbackProvider#getFeedback(ReferenceSets)}).
     *
     * @param dmContext current decision-making context
     * @throws FeedbackProviderException the exception can be thrown 
     */
    void registerDecisionMakingContext(DMContext dmContext) throws FeedbackProviderException;

    /**
     * Auxiliary method for unregistering the current decision-making context.
     */
    void unregisterDecisionMakingContext();

    /**
     * Auxiliary validation method.
     *
     * @throws FeedbackProviderException the exception can be thrown 
     */
    void validate() throws FeedbackProviderException;

    /**
     * The main method for providing the feedback (wrapped via {@link DMResult}).
     *
     * @param referenceSets reference sets that are the base for evaluation
     * @return feedback (wrapped via {@link DMResult}).
     * @throws FeedbackProviderException the exception can be thrown 
     */
    DMResult getFeedback(ReferenceSets referenceSets) throws FeedbackProviderException;
}
