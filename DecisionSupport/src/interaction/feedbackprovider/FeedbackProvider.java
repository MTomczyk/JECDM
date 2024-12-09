package interaction.feedbackprovider;

import dmcontext.DMContext;
import exeption.FeedbackProviderException;
import exeption.ReferenceSetsConstructorException;
import interaction.feedbackprovider.common.CommonResult;
import interaction.feedbackprovider.common.ICommonFeedbackProvider;
import interaction.feedbackprovider.dm.DMResult;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.ReferenceSets;
import system.dm.DM;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Higher-level class that wraps all feedback providers ({@link IDMFeedbackProvider}).
 *
 * @author MTomczyk
 */
public class FeedbackProvider
{
    /**
     * Params container.
     */
    protected static class Params
    {
        /**
         * Feedback provider that is common to all decision makers (either common or per-dm providers should be used, not both).
         */
        private ICommonFeedbackProvider _commonFeedbackProvider;

        /**
         * Feedback providers that are unique to each decision maker (accessed via decision maker's string representation,
         * when linked to a decision maker, either common or per-dm providers should be used, not both).
         */
        private HashMap<String, IDMFeedbackProvider> _dmFeedbackProviders;

        /**
         * Default constructor.
         */
        protected Params()
        {
            this(null, null);
        }

        /**
         * Parameterized constructor.
         *
         * @param commonFeedbackProvider feedback provider that are common to all decision makers
         * @param dmFeedbackProviders    feedback providers that are unique to each decision maker (accessed via decision maker's string representation, either common or per-dm providers should be used, not both)
         */
        private Params(ICommonFeedbackProvider commonFeedbackProvider, HashMap<String, IDMFeedbackProvider> dmFeedbackProviders)
        {
            _commonFeedbackProvider = commonFeedbackProvider;
            _dmFeedbackProviders = dmFeedbackProviders;
        }

        /**
         * Creates default object instance (default parameterization) that uses a single feedback provider associated
         * with a single decision maker.
         *
         * @param dm       decision maker's string representation
         * @param provider common feedback provider
         * @return default object instance
         */
        public static Params getForSingleDM(String dm, IDMFeedbackProvider provider)
        {
            Params p = new Params();
            p._commonFeedbackProvider = null;
            p._dmFeedbackProviders = new HashMap<>();
            p._dmFeedbackProviders.put(dm, provider);
            return p;
        }

        /**
         * Creates an object instance that uses a two feedback providers associated
         * with two (different) decision makers.
         *
         * @param dm1       first decision maker's string representation
         * @param provider1 feedback provider associated with the first decision maker
         * @param dm2       second decision maker's string representation
         * @param provider2 feedback provider associated with the second decision maker
         * @return default object instance
         */
        public static Params getForTwoDMs(String dm1, IDMFeedbackProvider provider1, String dm2, IDMFeedbackProvider provider2)
        {
            Params p = new Params();
            p._commonFeedbackProvider = null;
            p._dmFeedbackProviders = new HashMap<>();
            p._dmFeedbackProviders.put(dm1, provider1);
            p._dmFeedbackProviders.put(dm2, provider2);
            return p;
        }
    }

    /**
     * Feedback provider that is common to all decision makers.
     */
    private final ICommonFeedbackProvider _commonFeedbackProvider;

    /**
     * Feedback providers that are unique to each decision maker (accessed via decision maker's string representation,
     * either common or per-dm providers should be used, not both).
     */
    private final HashMap<String, IDMFeedbackProvider> _dmFeedbackProviders;

    /**
     * If true, common provider is used, false otherwise.
     */
    private final boolean _commonMode;

    /**
     * Creates default object instance (default parameterization) that uses a single feedback provider (stored as common).
     *
     * @param dm       decision maker's string representation
     * @param provider common feedback provider
     * @return default object instance
     */
    public static FeedbackProvider getForSingleDM(String dm, IDMFeedbackProvider provider)
    {
        return new FeedbackProvider(Params.getForSingleDM(dm, provider));
    }

    /**
     * Creates an object instance that uses a two feedback providers associated
     * with two (different) decision makers.
     *
     * @param dm1       first decision maker's string representation
     * @param provider1 feedback provider associated with the first decision maker
     * @param dm2       second decision maker's string representation
     * @param provider2 feedback provider associated with the second decision maker
     * @return default object instance
     */
    public static FeedbackProvider getForTwoDMs(String dm1, IDMFeedbackProvider provider1, String dm2, IDMFeedbackProvider provider2)
    {
        return new FeedbackProvider(Params.getForTwoDMs(dm1, provider1, dm2, provider2));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected FeedbackProvider(Params p)
    {
        _commonFeedbackProvider = p._commonFeedbackProvider;
        _dmFeedbackProviders = p._dmFeedbackProviders;
        _commonMode = _commonFeedbackProvider != null;
    }

    /**
     * Auxiliary method for performing simple validation.
     *
     * @param DMs all decision makers registered in the system
     * @throws FeedbackProviderException the exception will be thrown and propagated higher if the validation fails
     */
    public void validate(DM[] DMs) throws FeedbackProviderException
    {
        if (_commonFeedbackProvider == null && _dmFeedbackProviders == null)
            throw new FeedbackProviderException("Neither common nor per-DMs feedback provider is provided (the objects are null)", this.getClass());

        if (_commonMode)
        {
            if (_commonFeedbackProvider == null)
                throw new FeedbackProviderException("The common mode is used but the common feedback provider is null", this.getClass());
            _commonFeedbackProvider.validate();
        }
        else
        {
            if (_dmFeedbackProviders == null)
                throw new FeedbackProviderException("The decision makers' feedback providers are not provided (the array is null)", this.getClass());
            if (_dmFeedbackProviders.isEmpty())
                throw new FeedbackProviderException("The decision makers' feedback providers are not provided (the array is empty)", this.getClass());

            if (DMs == null)
                throw new FeedbackProviderException("The decision makers are not provided (the array is null)", this.getClass());
            for (DM dm : DMs)
                if (!_dmFeedbackProviders.containsKey(dm.getName()))
                    throw new FeedbackProviderException("No feedback provider is associated with a decision maker = " + dm.getName(), this.getClass());

            Set<String> dms = new HashSet<>();
            for (DM dm : DMs) dms.add(dm.getName());
            for (String name : _dmFeedbackProviders.keySet())
                if (!dms.contains(name))
                    throw new FeedbackProviderException("The decision maker's identifier for the name = " + name + " is not provided", this.getClass());

            for (DM dm : DMs)
                _dmFeedbackProviders.get(dm.getName()).validate();
        }
    }

    /**
     * The main method for retrieving a feedback.
     *
     * @param dmContext                 current decision-making context
     * @param DMs                 decision makers' identifiers
     * @param referenceSetsResult constructed reference sets (wrapped via {@link interaction.reference.Result})
     * @return feedback (wrapped via {@link Result})
     * @throws FeedbackProviderException the exception can be thrown and propagated higher
     */
    public Result generateFeedback(DMContext dmContext, DM [] DMs, interaction.reference.Result referenceSetsResult) throws FeedbackProviderException
    {
        if (dmContext == null)
            throw new FeedbackProviderException("The current decision-making context is not provided", this.getClass());
        if ((referenceSetsResult == null) || (referenceSetsResult._referenceSetsContainer == null))
            throw new FeedbackProviderException("The reference sets are not provided (the input is null)", this.getClass());

        Result result = new Result(dmContext, DMs);
        long startTime = System.nanoTime();

        if (_commonMode)
        {
            _commonFeedbackProvider.registerDecisionMakingContext(dmContext);
            _commonFeedbackProvider.registerDMs(DMs);
            CommonResult commonResult = _commonFeedbackProvider.getFeedback(referenceSetsResult._referenceSetsContainer);
            result._feedback = commonResult._feedback;
            _commonFeedbackProvider.unregisterDMs();
            _commonFeedbackProvider.unregisterDecisionMakingContext();
        }
        else
        {
            result._feedback = new HashMap<>(DMs.length);
            for (DM dm : DMs)
            {
                ReferenceSets jointSet;
                try
                {
                    HashMap<DM, ReferenceSets> dmReferenceSets  = referenceSetsResult._referenceSetsContainer.getDMReferenceSets();
                    if (dmReferenceSets != null) jointSet = ReferenceSets.createJointSet(referenceSetsResult._referenceSetsContainer.getCommonReferenceSets(),
                            referenceSetsResult._referenceSetsContainer.getDMReferenceSets().get(dm));
                    else jointSet = ReferenceSets.createJointSet(referenceSetsResult._referenceSetsContainer.getCommonReferenceSets(), null);
                } catch (ReferenceSetsConstructorException e)
                {
                    throw new FeedbackProviderException("Could not create a joint reference sets for a decision maker = " + dm.getName() + " " + e.getDetailedReasonMessage(), this.getClass(), e);
                }

                IDMFeedbackProvider provider = _dmFeedbackProviders.get(dm.getName());
                provider.registerDecisionMakingContext(dmContext);
                provider.registerDM(dm);
                DMResult dmResult = provider.getFeedback(jointSet);
                result._feedback.put(dm, dmResult);
                provider.unregisterDM();
                provider.unregisterDecisionMakingContext();
            }
        }

        result._processingTime = (System.nanoTime() - startTime) / 1000000;
        return result;
    }
}
