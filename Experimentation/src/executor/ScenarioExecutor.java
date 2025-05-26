package executor;

import container.scenario.AbstractScenarioDataContainer;
import container.trial.TrialDataContainerFactory;
import exception.ScenarioException;
import random.IRandom;
import summary.ScenarioSummary;
import summary.TrialSummary;
import utils.Level;
import utils.Log;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * The scenario-level executor class (responsible for conducting the concrete scenario).
 *
 * @author MTomczyk
 */


public class ScenarioExecutor extends AbstractExecutor
{
    /**
     * Reference to the scenario data container.
     */
    protected AbstractScenarioDataContainer _SDC;

    /**
     * Summary object (to be filled throughout the execution).
     */
    protected ScenarioSummary _sSummary;

    /**
     * Object responsible for executing threads (each thread is responsible for handling one test run for the current scenario)
     */
    protected TrialPoolExecutor _trialPoolExecutor;

    /**
     * Trial data container factory (creates the object handling the trial-level data).
     */
    protected final TrialDataContainerFactory _TDCF;

    /**
     * Monitor thread (object responsible for observing the current processing state).
     */
    protected Monitor _monitor;

    /**
     * Auxiliary object reponsible for dispatching the monitor thread.
     */
    protected ExecutorService _monitorExecutor;

    /**
     * Auxiliary container-like object storing data on the monitor processing.
     */
    protected Future<MonitorReport> _monitorReport;

    /**
     * Count-down latch object used to synchronize scenario processing, i.e., this executor (instance) will wait
     * with termination until all trial executors (and optionally the monitor thread) finish processing.
     */
    protected CountDownLatch _latch;

    /**
     * Auxiliary field storing the trial executors to be processed.
     */
    protected ArrayList<TrialExecutor> _trialExecutors;


    /**
     * Parameterized constructor.
     *
     * @param SDC             scenario data container
     * @param TDCF            trial data container factory (creates the object handling the trial-level data)
     * @param scenarioSummary summary object (filled throughout the execution)
     * @param log             provides means for logging (mainly printing messages to the console)
     */
    public ScenarioExecutor(AbstractScenarioDataContainer SDC,
                            TrialDataContainerFactory TDCF,
                            ScenarioSummary scenarioSummary,
                            Log log)
    {
        super(log, 2);
        _SDC = SDC;
        _TDCF = TDCF;
        _sSummary = scenarioSummary;
    }

    /**
     * The primary method that starts executing the scenario.
     *
     * @throws ScenarioException the scenario exception can be thrown (e.g., when the scenario-related data cannot be instantiated)
     */
    protected void execute() throws ScenarioException
    {
        _log.log("Experimental scenario = " + _SDC.toString() + " begins processing", Level.Scenario, _indent);
        createScenarioFolder();

        _log.log("Creates the count down latch (barrier)", Level.Scenario, _indent);
        _latch = instantiateCountDownLatch();

        _log.log("Creates trial executors", Level.Scenario, _indent);
        _trialExecutors = constructTrialExecutors(_latch);

        _log.log("Creates a trial (thread) pool", Level.Scenario, _indent);
        _trialPoolExecutor = new TrialPoolExecutor(_SDC.getGDC().getNoThreads(), _trialExecutors, _SDC.getScenario());

        _log.log("Attempting to instantiate the monitor thread", Level.Scenario, _indent);
        _monitor = instantiateMonitorThread(_latch, _trialExecutors);

        _log.log("Attempting to instantiate the monitor thread executor", Level.Scenario, _indent);
        _monitorExecutor = instantiateMonitorExecutor();

        if (_monitorExecutor != null)
        {
            _log.log("Executes the monitor thread", Level.Scenario, _indent);
            _monitorReport = _monitorExecutor.submit(_monitor);
        }

        _log.log("Executes the threads", Level.Scenario, _indent);
        _trialPoolExecutor.execute();

        try
        {
            _latch.await();
        } catch (InterruptedException e)
        {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, _SDC.getScenario());
        }

        _log.log("All threads terminated/completed processing", Level.Scenario, _indent);
        _log.log("Experimental scenario = " + _SDC.toString() + " ends processing", Level.Scenario, _indent);
    }

    /**
     * Auxiliary method for performing and logging an execution summary. Called by {@link ExperimentPerformer}.
     * The method also fills the scenario summary object {@link ScenarioExecutor#_sSummary}.
     */
    protected void doExecutionSummary()
    {
        _log.log("Scenario = " + _SDC.getScenario() + " processing time = " + StringUtils.getDeltaTime(_sSummary.getStartTimestamp(),
                _sSummary.getStopTimestamp()), Level.Global, _indent);

        int total = _SDC.getGDC().getNoEnabledTrials();
        _sSummary.setNoTrials(total);
        int completed = 0;
        int terminated = 0;
        int skipped = _SDC.getGDC().getNoTrials();
        LinkedList<String[]> trialExceptionMessages = new LinkedList<>();

        for (int t = 0; t < _trialExecutors.size(); t++)
        {
            Future<TrialSummary> f = _trialPoolExecutor.getFuture().get(t);

            try
            {
                f.get();
                completed++;

            } catch (InterruptedException | ExecutionException e)
            {
                terminated++;
                trialExceptionMessages.add(_trialExecutors.get(t).getSummary().getExceptionMessage()); // explicitly get the summary
            }

            skipped--;
        }

        _sSummary.setCompletedTrials(completed);
        _sSummary.setTerminatedTrials(terminated);
        _sSummary.setSkippedTrials(skipped);
        _sSummary.setTrialsExceptionMessages(trialExceptionMessages);

        _log.log("Total number of trials = " + total, Level.Scenario, _indent);
        _log.log("Successfully completed trials = " + completed, Level.Scenario, _indent);
        _log.log("Terminated trials (due to exception) = " + terminated, Level.Scenario, _indent);

        if (terminated > 0)
        {
            _log.log("Printing captured error messages", Level.Scenario, _indent);
            for (String[] e : trialExceptionMessages)
                for (String s : e) _log.log(s, Level.Scenario, _indent + 2);
        }
    }


    /**
     * This method instantiates the count-down latch used as a synchronization barrier.
     *
     * @return count-down latch instance
     */
    protected CountDownLatch instantiateCountDownLatch()
    {
        int count = _SDC.getGDC().getNoEnabledTrials();
        if (_SDC.getGDC().useMonitorThread()) count++;
        return new CountDownLatch(count);
    }

    /**
     * This method creates the monitor thread (object responsible for observing the current processing state).
     *
     * @param latch          reference to the count-down latch that will be reduced by the monitor thread after all executors finish their processing
     * @param trialExecutors reference to all trial executors that are to be processed on separate threads
     * @return the collection of trial executors (runnables) to be processed via a thread pool executor
     */
    protected Monitor instantiateMonitorThread(CountDownLatch latch, ArrayList<TrialExecutor> trialExecutors)
    {
        if (_SDC.getGDC().useMonitorThread())
        {
            int delay = _SDC.getGDC().getMonitorReportingInterval();
            _log.log("Instantiate the monitor thread with reporting interval = " + delay, Level.Scenario, _indent);
            Monitor.Params pM = new Monitor.Params();
            pM._startTimestamp = _sSummary.getStartTimestamp();
            pM._SDC = _SDC;
            pM._log = _log;
            pM._trialExecutors = trialExecutors;
            pM._latch = latch;
            pM._delay = delay;
            return new Monitor(pM);
        }
        else
        {
            _log.log("Skipping creation of the monitor thread", Level.Scenario, _indent);
            return null;
        }
    }

    /**
     * This method creates the monitor thread executor (instantiated only when the monitor is used).
     *
     * @return the monitor thread executor
     */
    protected ExecutorService instantiateMonitorExecutor()
    {
        if (_SDC.getGDC().useMonitorThread()) return Executors.newSingleThreadExecutor();
        else return null;
    }

    /**
     * This method creates trial executors (each executes one test run of the scenario).
     *
     * @param latch reference to the count-down latch that will be reduced by the monitor thread after all executors
     *              finish their processing
     * @return the collection of trial executors (runnables) to be processed via a thread pool executor
     * @throws ScenarioException scenario exception can be thrown and propagated higher
     */
    protected ArrayList<TrialExecutor> constructTrialExecutors(CountDownLatch latch) throws ScenarioException
    {
        ArrayList<TrialExecutor> executors = new ArrayList<>(_SDC.getGDC().getNoEnabledTrials());
        for (int id : _SDC.getGDC().getTrialIDs())
        {
            IRandom R = _SDC.getGDC().requestRandomNumberGenerator(_SDC.getScenario(), id); // request RNG (synchronous)
            executors.add(new TrialExecutor(_SDC, _TDCF, id, R, latch, _log));
        }
        return executors;
    }

    /**
     * This method creates the scenario-related folder that will store the results.
     * Note that a folder will be created only if it did not exist prior to method execution.
     * The method does not clear the already existing folder.
     *
     * @throws ScenarioException I/O-related exceptions can be thrown
     */
    protected void createScenarioFolder() throws ScenarioException
    {
        _log.log("Creates a scenario-related folder", Level.Scenario, _indent);
        String path = _SDC.getMainPath();
        File file = new File(path);
        if (!file.exists())
        {
            boolean created = file.mkdirs();
            if (!created)
                throw new ScenarioException("The scenario-related folder was not created", null, this.getClass(), _SDC.getScenario());
        }
    }

    /**
     * Auxiliary method for clearing the data.
     *
     * @throws ScenarioException scenario exception can be thrown and propagated to the global executor
     */
    protected void dispose() throws ScenarioException
    {
        try
        {
            _log.log("Disposes the thread pool", Level.Scenario, _indent);
            _trialPoolExecutor.dispose();
            _trialPoolExecutor = null;
            if (_monitorExecutor != null) _monitorExecutor.shutdown();

            _log.log("Disposes the scenario data container", Level.Scenario, _indent);
            _SDC.dispose();
        } catch (Exception e)
        {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, _SDC.getScenario());
        }

    }
}