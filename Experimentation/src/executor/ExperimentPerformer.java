package executor;

import condition.ScenarioDisablingConditions;
import container.Containers;
import container.global.GlobalDataContainer;
import container.scenario.AbstractScenarioDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.scenario.Validator;
import container.trial.TrialDataContainerFactory;
import exception.AbstractExperimentationException;
import exception.GlobalException;
import exception.ScenarioException;
import parser.Parser;
import scenario.Scenario;
import summary.ScenarioSummary;
import summary.Summary;
import utils.Level;
import utils.Log;

import java.io.File;
import java.time.LocalDateTime;

/**
 * The main executor class (responsible for conducting the experiments).
 *
 * @author MTomczyk
 */
public class ExperimentPerformer extends AbstractExecutor
{
    /**
     * Params container
     */
    public static class Params
    {
        /**
         * Global data container (contains the top-level data).
         */
        public GlobalDataContainer _GDC = null;

        /**
         * Scenario data container factory (creates the object handling the scenario-level data).
         */
        public ScenarioDataContainerFactory _SDCF = null;

        /**
         * Trial data container factory (creates the object handling the trial-level data).
         */
        public TrialDataContainerFactory _TDCF = null;

        /**
         * If true, the notifications are expected to be printed to the console.
         */
        public boolean _notify = true;

        /**
         * If true, the notifications related to the top-level (global) processing are expected to be printed to the console.
         * Note that the general setting _notify = false can surpass this flag and thus neglect its indications.
         */
        public boolean _notifyGlobalLevel = true;

        /**
         * If true, the notifications related to the middle-level (crossed-scenarios) processing are expected to be printed to the console.
         * Note that the general setting _notify = false can surpass this flag and thus neglect its indications.
         */
        public boolean _notifyCrossedScenariosLevel = true;

        /**
         * If true, the notifications related to the middle-level (scenario) processing are expected to be printed to the console.
         * Note that the general setting _notify = false can surpass this flag and thus neglect its indications.
         */
        public boolean _notifyScenarioLevel = true;

        /**
         * If true, the notifications related to the bottom-level (trial) processing are expected to be printed to the console.
         * Note that the general setting _notify = false can surpass this flag and thus neglect its indications.
         */
        public boolean _notifyTrialLevel = true;

        /**
         * If true, a timestamp accompanies the notifications printed into the console.
         */
        public boolean _addTimestamp = true;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor. Instantiates the containers/factories as imposed by the input {@link Containers} object.
         *
         * @param containers provides containers/factories
         */
        public Params(Containers containers)
        {
            _GDC = containers._GDC;
            _SDCF = containers._SDCF;
            _TDCF = containers._TDCF;
        }
    }

    /**
     * Global data container (contains the top-level data).
     */
    protected final GlobalDataContainer _GDC;

    /**
     * Scenario data container factory (creates the object handling the scenario-level data).
     */
    protected final ScenarioDataContainerFactory _SDCF;

    /**
     * Trial data container factory (creates the object handling the trial-level data).
     */
    protected final TrialDataContainerFactory _TDCF;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public ExperimentPerformer(Params p)
    {
        super(new Log(p._notify, p._notifyGlobalLevel, p._notifyCrossedScenariosLevel,
                p._notifyScenarioLevel, p._notifyTrialLevel, p._addTimestamp), 0);
        _GDC = p._GDC;
        _SDCF = p._SDCF;
        _TDCF = p._TDCF;
    }

    /**
     * The primary method that starts executing the experiments.
     *
     * @return execution summary
     */
    public Summary execute()
    {
        return execute(null);
    }

    /**
     * The primary method that starts executing the experiments.
     *
     * @param args additional arguments that can be passed via, e.g., the command lines
     * @return execution summary
     */
    public Summary execute(String[] args)
    {
        Summary summary = instantiateSummary(); // creates the main summary object
        summary.setStartTimestamp(LocalDateTime.now()); // set starting date
        doInitLog();

        Parser parser = new Parser();
        Parser.Result r = null;
        if (args != null) r = parser.parse(args);

        try
        {
            validateGDC();
            validateSDCF();
            validateTDCF();
            instantiateGDC(r);
            createMainFolder();
            processScenarios(summary);

        } catch (AbstractExperimentationException e)
        {
            terminate(summary, _log.printTerminationMessage(e));
            return summary;
        }

        finalize(summary);
        return summary;
    }

    /**
     * Auxiliary method for instantiating GDC
     *
     * @param r parser results
     * @throws GlobalException the global-level exception can be thrown
     */
    protected void instantiateGDC(Parser.Result r) throws GlobalException
    {
        _log.log("Instantiates the global data container", Level.Global, _indent);
        _GDC.instantiateData(r);
    }

    /**
     * Auxiliary method for contracting the main summary object.
     *
     * @return summary object
     */
    protected Summary instantiateSummary()
    {
        return new Summary();
    }

    /**
     * The main method for processing individual scenarios.
     *
     * @param summary summary object to be filled throughout the execution
     * @throws GlobalException global exception can be thrown 
     */
    protected void processScenarios(Summary summary) throws GlobalException
    {
        _log.log("Beginning processing the scenarios", Level.Global, _indent);

        int completedScenarios = 0;
        int terminatedScenarios = 0;
        int skippedScenarios = 0;
        int completedTrials = 0;
        int terminatedTrials = 0;
        int skippedTrials = 0;

        instantiateScenarioSummaries(summary);

        for (int sc = 0; sc < _GDC.getScenarios().getScenarios().length; sc++)
        {
            Scenario scenario = _GDC.getScenarios().getScenarios()[sc];
            ScenarioSummary sSummary = summary.getScenariosSummaries()[sc];

            if ((scenario.isDisabled()) || (checkScenarioDisablingConditions(scenario)))
            {
                skippedScenarios++;
                sSummary.setStartTimestamp(LocalDateTime.now());
                sSummary.setSkipped(true);
                sSummary.setTerminatedTrials(0);
                sSummary.setCompletedTrials(0);
                sSummary.setTerminationDueToException(false);
                sSummary.setStopTimestamp(LocalDateTime.now());
                _log.log("Skipping the scenario (is disabled) = " + scenario, Level.Global, _indent);
                continue;
            }

            Status status = processScenario(scenario, sSummary);
            if (status == Status.COMPLETED) completedScenarios++;
            else if (status == Status.TERMINATED) terminatedScenarios++;

            completedTrials += sSummary.getCompletedTrials();
            terminatedTrials += sSummary.getTerminatedTrials();
            skippedTrials += sSummary.getSkippedTrials();
        }

        summary.setCompletedScenarios(completedScenarios);
        summary.setTerminatedScenarios(terminatedScenarios);
        summary.setSkippedScenarios(skippedScenarios);
        summary.setCompletedTrials(completedTrials);
        summary.setTerminatedTrials(terminatedTrials);
        summary.setSkippedTrials(skippedTrials);
    }

    /**
     * Auxiliary method for checking whether the scenario should be disabled given the scenario disabling conditions
     * (see {@link ScenarioDisablingConditions}). If at least one scenario disabling conditions object
     * is in favor of scenario exclusion, the scenario will be excluded from processing.
     *
     * @param scenario scenario to be examined
     * @return true = scenario should be skipped; false otherwise
     */
    protected boolean checkScenarioDisablingConditions(Scenario scenario)
    {
        ScenarioDisablingConditions[] SDCs = _GDC.getScenarioDisablingConditions();
        if (SDCs == null) return false;
        for (ScenarioDisablingConditions sdc : SDCs)
            if (sdc.shouldBeDisabled(scenario)) return true;
        return false;
    }

    /**
     * Auxiliary method for instantiating scenario summary objects.
     *
     * @param summary the global summary object
     */
    protected void instantiateScenarioSummaries(Summary summary)
    {
        summary.setScenariosSummaries(new ScenarioSummary[_GDC.getScenarios().getScenarios().length]);
        for (int sc = 0; sc < _GDC.getScenarios().getScenarios().length; sc++)
            summary.getScenariosSummaries()[sc] = new ScenarioSummary(_GDC.getScenarios().getScenarios()[sc]);
    }

    /**
     * The main method for processing a single scenario.
     * This method is split into three ordered stages: post, main, and pre.
     *
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled throughout the execution
     * @return processing status
     */
    protected Status processScenario(Scenario scenario, ScenarioSummary scenarioSummary)
    {
        Status status = preProcessScenario(scenario, scenarioSummary);
        status = mainProcessScenario(status, scenario, scenarioSummary);
        status = postProcessScenario(status, scenario, scenarioSummary);
        return status;
    }


    /**
     * The main method for processing a single scenario (pre-stage).
     *
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled throughout the execution
     * @return processing status
     */
    protected Status preProcessScenario(Scenario scenario, ScenarioSummary scenarioSummary)
    {
        scenarioSummary.setStartTimestamp(LocalDateTime.now());
        scenarioSummary.setSkipped(false);
        scenarioSummary.setTerminationDueToException(false);
        return Status.CONTINUED;
    }

    /**
     * The main method for processing a single scenario (main-stage).
     *
     * @param status          current processing status
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled throughout the execution
     * @return processing status
     */
    protected Status mainProcessScenario(Status status, Scenario scenario, ScenarioSummary scenarioSummary)
    {
        AbstractScenarioDataContainer SDC;
        ScenarioExecutor SE = null;

        try
        {
            SDC = _SDCF.getInstance(_GDC, scenario, new Validator(scenario, false));
            SE = instantiateScenarioExecutor(SDC, _TDCF, scenarioSummary);
            SE.execute();
            status = Status.COMPLETED;

        } catch (ScenarioException e)
        {
            scenarioSummary.setExceptionMessage(_log.printTerminationMessage(e));
            scenarioSummary.setTerminationDueToException(true);
            status = Status.TERMINATED;
        }

        scenarioSummary.setStopTimestamp(LocalDateTime.now());

        try
        {
            if (SE != null)
            {
                SE.doExecutionSummary();
                SE.dispose();
            }
        } catch (ScenarioException e)
        {
            _log.log("Exception occurred during the disposal of scenario = " + scenario + " " + e.getDetailedReasonMessage(), Level.Global, _indent);
        }

        return status;
    }

    /**
     * The main method for processing a single scenario (post-stage).
     *
     * @param status          current processing status
     * @param scenario        scenario being currently processed
     * @param scenarioSummary scenario summary object to be filled throughout the execution
     * @return processing status
     */
    protected Status postProcessScenario(Status status, Scenario scenario, ScenarioSummary scenarioSummary)
    {
        //System.out.println();
        return status;
    }

    /**
     * Auxiliary method for validating the GDC (whether is provided)
     *
     * @throws GlobalException global exception is thrown when the GDC is not provided
     */
    protected void validateGDC() throws GlobalException
    {
        if (_GDC == null) throw new GlobalException("Global Data Container is not provided", null, this.getClass());
    }

    /**
     * Auxiliary method for validating the SDCF (scenario data container factory)
     *
     * @throws GlobalException global exception is thrown when the SDCF is not provided
     */
    protected void validateSDCF() throws GlobalException
    {
        if (_SDCF == null)
            throw new GlobalException("Scenario Data Container Factory is not provided", null, this.getClass());
    }

    /**
     * Auxiliary method for validating the SDCF (scenario data container factory)
     *
     * @throws GlobalException global exception is thrown when the SDCF is not provided
     */
    protected void validateTDCF() throws GlobalException
    {
        if (_TDCF == null)
            throw new GlobalException("Trial Data Container Factory is not provided", null, this.getClass());
    }


    /**
     * Auxiliary method for printing the initial log.
     */
    protected void doInitLog()
    {
        _log.log("Experiment execution starts", Level.Global, _indent);
    }

    /**
     * Auxiliary method that creates the main folder.
     *
     * @throws GlobalException the exception will be thrown if the main folder cannot be created
     */
    protected void createMainFolder() throws GlobalException
    {
        File file = new File(_GDC.getMainPath());
        if (!file.exists())
        {
            boolean created = file.mkdir();
            if (!created)
            {
                created = file.mkdirs();
                if (!created) throw new GlobalException("Could not create the main folder (" + _GDC.getMainPath() + ")", null, this.getClass());
            }
        }

        _log.log("The main folder is created (" + _GDC.getMainPath() + ")", Level.Global, _indent);
    }

    /**
     * An auxiliary method that cleans up in the case of premature termination.
     *
     * @param summary summary object that will be filled by the method and returned
     * @param msg     message generated when notifying about the exception
     */
    private void terminate(Summary summary, String[] msg)
    {
        _log.log("Executing the termination method", Level.Global, _indent);
        summary.setTerminationDueToException(true);
        summary.setExceptionMessage(msg);
        finalize(summary);
    }

    /**
     * Auxiliary method that creates the scenario executor object.
     *
     * @param SDC      scenario data container that is to be linked with the executor
     * @param TDCF     trial data container factory
     * @param sSummary scenario summary object to be filled
     * @return scenario executor object
     */
    protected ScenarioExecutor instantiateScenarioExecutor(AbstractScenarioDataContainer SDC, TrialDataContainerFactory TDCF, ScenarioSummary sSummary)
    {
        return new ScenarioExecutor(SDC, TDCF, sSummary, _log);
    }

    /**
     * Executes final operations.
     *
     * @param summary summary object
     */
    protected void finalize(Summary summary)
    {
        _log.log("Executing the finalization method", Level.Global, _indent);

        // clear the data
        if (_GDC != null)
        {
            _log.log("Disposing the GDC data", Level.Global, _indent);
            _GDC.dispose();
        }

        if (_SDCF != null) _SDCF.dispose();
        if (_TDCF != null) _TDCF.dispose();

        summary.setStopTimestamp(LocalDateTime.now());
        _log.log("Experiment execution stops", Level.Global, _indent);
    }
}

