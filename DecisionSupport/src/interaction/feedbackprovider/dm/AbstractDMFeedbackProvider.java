package interaction.feedbackprovider.dm;

import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import interaction.reference.ReferenceSets;
import system.dm.DM;

/**
 * Abstract implementation of {@link IDMFeedbackProvider}. Provides common fields, functionalities.
 *
 * @author MTomczyk
 */


public abstract class AbstractDMFeedbackProvider implements IDMFeedbackProvider
{
    /**
     * Registered decision maker's identifier.
     */
    protected DM _dm;

    /**
     * Registered decision-making context.
     */
    protected DMContext _dmContext;

    /**
     * Auxiliary method for registering a DM (should be called before {@link IDMFeedbackProvider#getFeedback(ReferenceSets)}).
     *
     * @param dm the decision maker to be registered
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public void registerDM(DM dm) throws FeedbackProviderException
    {
        if (dm == null) throw new FeedbackProviderException("The input decision maker is null", this.getClass());
        _dm = dm;
    }

    /**
     * Auxiliary validation method.
     *
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public void validate() throws FeedbackProviderException
    {

    }

    /**
     * Auxiliary method for unregistering the DM.
     *
     */
    @Override
    public void unregisterDM()
    {
        _dm = null;
    }

    /**
     * Auxiliary method for unregistering the current decision-making context.
     */
    @Override
    public void unregisterDecisionMakingContext()
    {
        _dmContext = null;
    }


    /**
     * Auxiliary method for registering the current decision-making context (should be called before {@link IDMFeedbackProvider#getFeedback(ReferenceSets)}).
     *
     * @param dmContext current decision-making context
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws FeedbackProviderException
    {
        if (dmContext == null) throw new FeedbackProviderException("The input decision-making context is null", this.getClass());
        _dmContext = dmContext;
    }

    /**
     * The main method for providing the feedback (wrapped via {@link DMResult}).
     *
     * @param referenceSets reference sets that are the base for evaluation
     * @return feedback (wrapped via {@link DMResult}).
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public DMResult getFeedback(ReferenceSets referenceSets) throws FeedbackProviderException
    {
        return null;
    }
}
