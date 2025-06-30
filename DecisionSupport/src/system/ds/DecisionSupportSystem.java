package system.ds;

import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionSupportSystemException;
import exeption.ModuleException;
import interaction.Status;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.reference.ReferenceSetsConstructor;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import system.dm.DM;
import system.dm.DecisionMakerSystem;

import java.time.LocalDateTime;


/**
 * Top-level class representing a decision support system. It encapsulates two major modules: preference elicitation module
 * {@link system.modules.elicitation.PreferenceElicitationModule} and models updater module {@link system.modules.updater.ModelsUpdaterModule}.
 *
 * @author MTomczyk
 */


public class DecisionSupportSystem
{
    /**
     * Simple params adjuster (can be used in various implementations to adjust {@link Params} being parameterized).
     */
    public interface IParamsAdjuster
    {
        /**
         * The main method.
         *
         * @param p params container being parameterized
         */
        void adjust(Params p);
    }

    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Represents a consistent family of criteria.
         */
        public Criteria _criteria;

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
         * Decision maker's data bundles.
         */
        public DMBundle[] _dmBundles;

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
         * If true, the results of all called models updates are stored in a list.
         */
        public boolean _collectModelsUpdatesResults = false;

    }

    /**
     * DSS model.
     */
    private final DecisionSupportSystemModel _model;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    public DecisionSupportSystem(Params p) throws DecisionSupportSystemException
    {
        _model = new DecisionSupportSystemModel(p);
    }

    /**
     * Auxiliary method for notifying about system start. Must be called before calling
     * {@link DecisionSupportSystem#executeProcess(DMContext.Params)} (and other similar methods).
     *
     * @throws DecisionSupportSystemException the exception will be thrown if the method was already called
     */
    public void notifySystemStarts() throws DecisionSupportSystemException
    {
        if (_model._systemStarted)
            throw new DecisionSupportSystemException("The 'notify system starts' method was already called", this.getClass());
        _model._systemStartTimestamp = LocalDateTime.now();
        _model._systemStarted = true;
    }


    /**
     * This is the top-level method that aggregates functionalities offered by the two main modules:
     * {@link system.modules.elicitation.PreferenceElicitationModule} and {@link system.modules.updater.ModelsUpdaterModule}.
     * It first calls the preference elicitation module. If the resulting preference elicitation report indicates that
     * the additional feedback was provided by the decision maker(s), the method will call for models updates. The reports
     * from both processes are wrapped via {@link system.dm.Report} and returned by this method (the report on models update
     * will be null if the associated process was not triggered).
     *
     * @param pDMC current decision-making context (params)
     * @return final report on the DSS process
     * @throws DecisionSupportSystemException the exception can be thrown 
     */
    public Report executeProcess(DMContext.Params pDMC) throws DecisionSupportSystemException
    {
        if (!_model._systemStarted)
            throw new DecisionSupportSystemException("The 'notify system starts' method was not called", this.getClass());

        DMContext dmContext = _model.deriveDecisionMakingContext(pDMC);
        Report report = new Report(dmContext);
        long startTime = System.nanoTime();

        system.modules.updater.Report updateReport;
        system.modules.elicitation.Report elicitationReport = executeElicitationProcess(dmContext);
        report._elicitationReport = elicitationReport;

        if (elicitationReport._interactionStatus == Status.PROCESS_ENDED_SUCCESSFULLY)
        {
            updateReport = executeModelUpdateProcess(dmContext);
            report._updateReport = updateReport;
        }

        report._processingTime = (double) (System.nanoTime() - startTime) / 1000000.0d;

        return report;
    }

    /**
     * Supportive method (available, can be called but with extra caution) for performing the preference elicitation
     * process ({@link system.modules.elicitation.PreferenceElicitationModule#executeProcess(DMContext)}).
     *
     * @param pDMC current decision-making context (params)
     * @return report on the preference elicitation
     * @throws DecisionSupportSystemException the exception can be thrown 
     */
    public system.modules.elicitation.Report executeElicitationProcess(DMContext.Params pDMC) throws DecisionSupportSystemException
    {
        DMContext dmContext = _model.deriveDecisionMakingContext(pDMC);
        return executeElicitationProcess(dmContext);
    }

    /**
     * Supportive (not available) method for performing the preference elicitation process ({@link system.modules.elicitation.PreferenceElicitationModule#executeProcess(DMContext)}).
     *
     * @param dmContext current decision-making context
     * @return report on the preference elicitation
     * @throws DecisionSupportSystemException the exception can be thrown 
     */
    private system.modules.elicitation.Report executeElicitationProcess(DMContext dmContext) throws DecisionSupportSystemException
    {
        try
        {
            return _model._preferenceElicitationModule.executeProcess(dmContext);
        } catch (ModuleException e)
        {
            throw new DecisionSupportSystemException("Error occurred when executing the preference elicitation process " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

    /**
     * Supportive (available, can be called but with extra caution) method for performing the models update process
     * ({@link system.modules.elicitation.PreferenceElicitationModule#executeProcess(DMContext)}).
     *
     * @param pDMC current decision-making context (params)
     * @return report on the models update
     * @throws DecisionSupportSystemException the exception can be thrown 
     */
    public system.modules.updater.Report executeModelUpdateProcess(DMContext.Params pDMC) throws DecisionSupportSystemException
    {
        DMContext dmContext = _model.deriveDecisionMakingContext(pDMC);
        return executeModelUpdateProcess(dmContext);
    }

    /**
     * Supportive (not available) method for performing the models update process ({@link system.modules.elicitation.PreferenceElicitationModule#executeProcess(DMContext)}).
     *
     * @param dmContext current decision-making context
     * @return report on the models update
     * @throws DecisionSupportSystemException the exception can be thrown 
     */
    private system.modules.updater.Report executeModelUpdateProcess(DMContext dmContext) throws DecisionSupportSystemException
    {
        try
        {
            return _model._modelsUpdaterModule.executeProcess(dmContext);
        } catch (ModuleException e)
        {
            throw new DecisionSupportSystemException("Error occurred when executing the models updater module " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }


    /**
     * Getter for criteria.
     *
     * @return criteria
     */
    public Criteria getCriteria()
    {
        return _model._criteria;
    }

    /**
     * Getter for decision makers objects.
     *
     * @return criteria
     */
    public DM[] getDMs()
    {
        return _model._DMs;
    }

    /**
     * Getter for the decision makers' systems.
     *
     * @return decision makers' systems
     */
    public DecisionMakerSystem[] getDecisionMakersSystems()
    {
        return _model._DMSs;
    }


}
