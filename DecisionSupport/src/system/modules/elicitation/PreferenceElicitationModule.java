package system.modules.elicitation;

import dmcontext.DMContext;
import exeption.*;
import interaction.Status;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.DMResult;
import interaction.reference.ReferenceSetsConstructor;
import interaction.refine.Refiner;
import interaction.refine.Result;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.Postpone;
import interaction.trigger.Reason;
import system.dm.DecisionMakerSystem;
import system.modules.AbstractModule;

import java.util.LinkedList;

/**
 * This class represents a module associated with preference elicitation.
 *
 * @author MTomczyk
 */
public class PreferenceElicitationModule extends AbstractModule
{
    /**
     * Params container
     */
    public static class Params extends AbstractModule.Params
    {
        /**
         * Interaction trigger.
         */
        public InteractionTrigger _interactionTrigger;

        /**
         * Refiner object.
         */
        public Refiner _refiner;

        /**
         * Reference sets constructor.
         */
        public ReferenceSetsConstructor _referenceSetsConstructor;

        /**
         * Object responsible for constructing a feedback given the reference sets.
         */
        public FeedbackProvider _feedbackProvider;

        /**
         * If true, the results of all called interaction trigger checks are stored in a list.
         */
        public boolean _collectInteractionTriggerResults = false;

        /**
         * If true, the results of all called refining processes are stored in a list.
         */
        public boolean _collectRefinerResults = false;

        /**
         * If true, the results of all called reference sets construction processes are stored in a list.
         */
        public boolean _collectReferenceSetsConstructionResults = false;

        /**
         * Default constructor.
         */
        public Params()
        {
            super("Preference elicitation module");
        }
    }

    /**
     * Interaction trigger.
     */
    private final InteractionTrigger _interactionTrigger;

    /**
     * Refiner object.
     */
    private final Refiner _refiner;

    /**
     * Reference sets constructor.
     */
    private final ReferenceSetsConstructor _referenceSetsConstructor;

    /**
     * Object responsible for constructing a feedback given the reference sets.
     */
    private final FeedbackProvider _feedbackProvider;

    /**
     * If true, the results of all called interaction trigger checks are stored in a list.
     */
    private final boolean _collectInteractionTriggerResults;

    /**
     * Interaction trigger results.
     */
    private final LinkedList<interaction.trigger.Result> _interactionTriggerResults;

    /**
     * If true, the results of all called refining processes are stored in a list.
     */
    private final boolean _collectRefinerResults;

    /**
     * Refining results.
     */
    private final LinkedList<Result> _refiningResults;

    /**
     * If true, the results of all called reference sets construction processes are stored in a list.
     */
    private final boolean _collectReferenceSetsConstructionResults;

    /**
     * Reference sets construction results.
     */
    private final LinkedList<interaction.reference.Result> _referenceSetsConstructionResults;

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public PreferenceElicitationModule(Params p)
    {
        super(p);
        _interactionTrigger = p._interactionTrigger;
        _refiner = p._refiner;
        _referenceSetsConstructor = p._referenceSetsConstructor;
        _feedbackProvider = p._feedbackProvider;

        _collectInteractionTriggerResults = p._collectInteractionTriggerResults;
        if (_collectInteractionTriggerResults) _interactionTriggerResults = new LinkedList<>();
        else _interactionTriggerResults = null;

        _collectRefinerResults = p._collectRefinerResults;
        if (_collectRefinerResults) _refiningResults = new LinkedList<>();
        else _refiningResults = null;

        _collectReferenceSetsConstructionResults = p._collectReferenceSetsConstructionResults;
        if (_collectReferenceSetsConstructionResults) _referenceSetsConstructionResults = new LinkedList<>();
        else _referenceSetsConstructionResults = null;
    }

    /**
     * Auxiliary method for performing data validation.
     *
     * @throws ModuleException the exception will be thrown if the validation fails
     */
    @Override
    public void validate() throws ModuleException
    {
        if (_interactionTrigger == null)
            throw new ModuleException("The interaction trigger is not provided", this.getClass());
        if (_refiner == null)
            throw new ModuleException("The refiner is not provided", this.getClass());
        if (_referenceSetsConstructor == null)
            throw new ModuleException("The reference sets constructor is not provided", this.getClass());
        if (_feedbackProvider == null)
            throw new ModuleException("The feedback provider is not provided", this.getClass());

        super.validate();

        try
        {
            _interactionTrigger.validate();
            _refiner.validate();
            _referenceSetsConstructor.validate(_DMs);
            _feedbackProvider.validate(_DMs);

        } catch (TriggerException | FeedbackProviderException | RefinerException | ReferenceSetsConstructorException e)
        {
            throw new ModuleException("The additional validation failed " + e.getDetailedReasonMessage(), this.getClass(), e.getCause());
        }
    }


    /**
     * The main method for executing the default preference elicitation process. It consists of the following steps.
     * First, the interaction trigger object ({@link InteractionTrigger}) is used to determine whether the interaction
     * should be performed. If not, the method terminates, suitably filling the report. Otherwise, the method proceeds
     * to the reference sets construction process. If the process fails (due to, e.g., the termination filter
     * (see {@link ReferenceSetsConstructor})), the method calls the 'postpone interactions' method and exists. If the
     * reference sets are constructed, the method continues to the  feedback-receiving step (see {@link FeedbackProvider}).
     * The received feedback is used to notify the decision maker systems {@link DecisionMakerSystem} about the most
     * recent preference information obtained, and subsequently, it exists successfully.
     *
     * @param dmContext current decision-making context
     * @return report on the process execution
     * @throws ModuleException the exception can be thrown 
     */
    public Report executeProcess(DMContext dmContext) throws ModuleException
    {
        // register current decision-making context
        registerDecisionMakingContext(dmContext);

        system.modules.elicitation.Report report = new system.modules.elicitation.Report(dmContext);
        long startTime = System.nanoTime();

        // should trigger the interactions?
        interaction.trigger.Result triggerInteractionsResult = shouldTriggerTheInteractions(dmContext);
        report._interactionTriggerResult = triggerInteractionsResult;

        if (!triggerInteractionsResult._shouldInteract)
        {
            unregisterDecisionMakingContext();
            report._processingTime = (double) (System.nanoTime() - startTime) / 1000000.0d;
            report._interactionStatus = Status.INTERACTION_WAS_NOT_TRIGGERED;
            return report;
        }

        notifyPreferenceElicitationBegins(dmContext);

        // construct reference sets
        interaction.reference.Result constructReferenceSetsResult = constructReferenceSets(dmContext);
        report._referenceSetsConstructionResult = constructReferenceSetsResult;
        Status st = constructReferenceSetsResult._status;

        //
        if (!constructReferenceSetsResult._status.equals(Status.PROCESS_ENDED_SUCCESSFULLY))
        {
            Reason reason = null;
            if ((st.equals(Status.TERMINATED_DUE_TO_TERMINATION_FILTER)) ||
                    st.equals(Status.TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES) ||
                    st.equals(Status.PROCESS_ENDED_BUT_NO_REFERENCE_SETS_WERE_CONSTRUCTED))
                reason = Reason.COULD_NOT_DETERMINE_REFERENCE_SETS;

            Postpone postpone = new Postpone(reason, dmContext.getCurrentIteration(), dmContext.getCurrentDateTime());
            postponeInteractions(postpone);

            notifyPreferenceElicitationFailed(dmContext);
            notifyPreferenceElicitationEnds(dmContext);
            unregisterDecisionMakingContext();

            report._processingTime = (double) (System.nanoTime() - startTime) / 1000000.0d;
            report._interactionStatus = st;
            return report;
        }

        interaction.feedbackprovider.Result feedbackProviderResult;
        try
        {
            feedbackProviderResult = _feedbackProvider.generateFeedback(dmContext, _DMs, constructReferenceSetsResult);
            report._feedbackProviderResult = feedbackProviderResult;

        } catch (FeedbackProviderException e)
        {
            throw new ModuleException("Feedback-receiving process failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        } catch (Exception e)
        {
            throw new ModuleException("(unexpected error) Feedback-receiving process failed (reason = " + e.getMessage() + ")", this.getClass(), e);
        }

        // register feedback
        notifyAboutTheMostRecentPreferenceInformation(feedbackProviderResult);

        // unregister current state og knowledge
        notifyPreferenceElicitationEnds(dmContext);
        unregisterDecisionMakingContext();

        report._processingTime = (double) (System.nanoTime() - startTime) / 1000000.0d;
        report._interactionStatus = Status.PROCESS_ENDED_SUCCESSFULLY;
        return report;
    }

    /**
     * Method for notifying that the preference elicitation begins.
     *
     * @param dmContext current decision-making context
     * @throws ModuleException the exception can be thrown 
     */
    public void notifyPreferenceElicitationBegins(DMContext dmContext) throws ModuleException
    {
        for (DecisionMakerSystem dms : _DMSs)
        {
            try
            {
                _interactionTrigger.notifyPreferenceElicitationBegins(dmContext);
                dms.notifyPreferenceElicitationBegins();
            } catch (DecisionMakerSystemException e)
            {
                throw new ModuleException("The exception occurred for the decision maker = " + dms.getDM().getName() +
                        "when notifying about the beginning of the elicitation process " + e.getDetailedReasonMessage(),
                        this.getClass(), e);
            }
        }
    }


    /**
     * Method for notifying that the preference elicitation failed.
     *
     * @param dmContext current decision-making context
     * @throws ModuleException the exception can be thrown 
     */
    public void notifyPreferenceElicitationFailed(DMContext dmContext) throws ModuleException
    {
        for (DecisionMakerSystem dms : _DMSs)
        {
            try
            {
                _interactionTrigger.notifyPreferenceElicitationFailed(dmContext);
                dms.notifyPreferenceElicitationFailed();
            } catch (DecisionMakerSystemException e)
            {
                throw new ModuleException("The exception occurred for the decision maker = " + dms.getDM().getName() +
                        "when notifying about the failure of the elicitation process " + e.getDetailedReasonMessage(),
                        this.getClass(), e);
            }
        }
    }

    /**
     * Method for notifying that the preference elicitation ends.
     *
     * @param dmContext current decision-making context
     * @throws ModuleException the exception can be thrown 
     */
    public void notifyPreferenceElicitationEnds(DMContext dmContext) throws ModuleException
    {
        for (DecisionMakerSystem dms : _DMSs)
        {
            try
            {
                _interactionTrigger.notifyPreferenceElicitationEnds(dmContext);
                dms.notifyPreferenceElicitationEnds();
            } catch (DecisionMakerSystemException e)
            {
                throw new ModuleException("The exception occurred for the decision maker = " + dms.getDM().getName() +
                        "when notifying about the ending of the elicitation process " + e.getDetailedReasonMessage(),
                        this.getClass(), e);
            }
        }
    }


    /**
     * Method for notifying about the most recent preference information.
     *
     * @param feedback container for the received feedback
     * @throws ModuleException the exception can be thrown 
     */
    public void notifyAboutTheMostRecentPreferenceInformation(interaction.feedbackprovider.Result feedback) throws ModuleException
    {
        for (DecisionMakerSystem dms : _DMSs)
        {
            DMResult dmResult = feedback._feedback.get(dms.getDM());
            if (!checkIfPreferenceInformationWasDerived(dmResult)) continue;

            try
            {
                dms.notifyAboutTheMostRecentPreferenceInformation(dmResult._feedback);
            } catch (DecisionMakerSystemException e)
            {
                throw new ModuleException("The exception occurred for the decision maker = " + dms.getDM().getName() +
                        "when notifying about the most recent feedback " + e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }
    }

    /**
     * Auxiliary method that checks whether new preference information was derived.
     *
     * @param dmResult DM-related result
     * @return true = was derived; false otherwise
     */
    private boolean checkIfPreferenceInformationWasDerived(DMResult dmResult)
    {
        if (dmResult == null) return false;
        if (dmResult._feedback == null) return false;
        return !dmResult._feedback.isEmpty();
    }

    /**
     * The main method for checking whether the interactions should be triggered.
     *
     * @param dmContext current decision-making context
     * @return the indication whether to interact stored in {@link interaction.trigger.Result}
     * @throws ModuleException the exception can be thrown 
     */
    public interaction.trigger.Result shouldTriggerTheInteractions(DMContext dmContext) throws ModuleException
    {
        interaction.trigger.Result result;
        try
        {
            result = _interactionTrigger.shouldTriggerTheInteractions(dmContext);
        } catch (TriggerException e)
        {
            throw new ModuleException("Interaction trigger check process failed " + e.getDetailedReasonMessage(), this.getClass(), e);

        }
        if ((_collectInteractionTriggerResults) && (_interactionTriggerResults != null))
            _interactionTriggerResults.add(result);
        return result;
    }

    /**
     * The main method for deriving reference sets (wrapped using {@link interaction.reference.Result}).
     *
     * @param dmContext current decision-making context
     * @return obtained reference sets (wrapped using {@link interaction.reference.Result})
     * @throws ModuleException the exception can be thrown 
     */
    public interaction.reference.Result constructReferenceSets(DMContext dmContext) throws ModuleException
    {
        Result refiningResults;
        try
        {
            refiningResults = _refiner.refine(dmContext);
        } catch (RefinerException e)
        {
            throw new ModuleException("Refining process failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }

        if ((_collectRefinerResults) && (_refiningResults != null)) _refiningResults.add(refiningResults);
        if (refiningResults._status.equals(Status.TERMINATED_DUE_TO_TERMINATION_FILTER))
            return getTerminatingResult(dmContext, refiningResults, refiningResults._status);

        interaction.reference.Result referenceResult;
        try
        {
            referenceResult = _referenceSetsConstructor.constructReferenceSets(dmContext, _DMs, refiningResults);
        } catch (ReferenceSetsConstructorException e)
        {
            throw new ModuleException("Constructing reference sets failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }

        if ((_collectReferenceSetsConstructionResults) && (_referenceSetsConstructionResults != null))
            _referenceSetsConstructionResults.add(referenceResult);
        if ((referenceResult._status.equals(Status.TERMINATED_DUE_TO_HAVING_NOT_ENOUGH_ALTERNATIVES))
                || (referenceResult._status.equals(Status.PROCESS_ENDED_BUT_NO_REFERENCE_SETS_WERE_CONSTRUCTED)))
            return getTerminatingResult(dmContext, refiningResults, referenceResult._status);

        return referenceResult;
    }

    /**
     * Auxiliary method for constructing a result (on constructing reference sets) associated with termination.
     *
     * @param dmContext      current decision-making context
     * @param refiningResult results of the refining process
     * @param status         status to be set
     * @return result
     */
    private interaction.reference.Result getTerminatingResult(DMContext dmContext, Result refiningResult, Status status)
    {
        interaction.reference.Result result = new interaction.reference.Result(dmContext, refiningResult);
        result._status = status;
        result._processingTime = 0.0d;
        result._terminatedDueToNotEnoughAlternatives = false;
        result._terminatedDueToNotEnoughAlternativesMessage = null;
        result._referenceSetsContainer = null;
        return result;
    }

    /**
     * The main method for notifying the interaction trigger about a will to postpone the interactions.
     *
     * @param postpone object providing reasons (and auxiliary data) for postponing
     */
    public void postponeInteractions(Postpone postpone)
    {
        _interactionTrigger.postpone(postpone);
    }

    /**
     * Getter for the interaction trigger check results.
     *
     * @return interaction trigger check results
     */
    public LinkedList<interaction.trigger.Result> getInteractionTriggerResults()
    {
        return _interactionTriggerResults;
    }

    /**
     * Getter for the refining results.
     *
     * @return refining results
     */
    public LinkedList<Result> getRefiningResults()
    {
        return _refiningResults;
    }

    /**
     * Getter for the reference sets construction results.
     *
     * @return reference sets construction results
     */
    public LinkedList<interaction.reference.Result> getReferenceSetsConstructionResults()
    {
        return _referenceSetsConstructionResults;
    }
}
