package interaction.feedbackprovider.common;

import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.ReferenceSets;
import interaction.reference.ReferenceSetsContainer;
import system.dm.DM;


/**
 * Interface for classes responsible for constructing feedback ({@link preference.IPreferenceInformation}) using the
 * constructed reference sets (the used object is supposed to handle all decision makers).
 *
 * @author MTomczyk
 */
public interface ICommonFeedbackProvider
{
    /**
     * Auxiliary method for registering the decision makers (should be called before {@link ICommonFeedbackProvider#getFeedback(ReferenceSetsContainer)}).
     *
     * @param DMs the decision maker to be registered
     * @throws FeedbackProviderException the exception can be thrown 
     */
    void registerDMs(DM [] DMs) throws FeedbackProviderException;

    /**
     * Auxiliary method for unregistering the DMs.
     */
    void unregisterDMs();

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
     * The main method for providing the feedback (wrapped via {@link CommonResult}).
     *
     * @param referenceSetsContainer reference sets container that provides all reference sets
     * @return feedback (wrapped via {@link CommonResult}).
     * @throws FeedbackProviderException the exception can be thrown 
     */
    CommonResult getFeedback(ReferenceSetsContainer referenceSetsContainer) throws FeedbackProviderException;
}
