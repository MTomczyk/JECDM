package executor;

import container.global.AbstractGlobalDataContainer;
import exception.ScenarioException;
import scenario.Scenario;
import summary.TrialSummary;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Wrapper for the {@link java.util.concurrent.ExecutorService} class.
 * Handles the trial executors responsible for running test runs and measuring the performance.
 * It is assumed that the thread pool is of a fixed size
 * (as provided via {@link AbstractGlobalDataContainer.Params#_noThreads}).
 *
 * @author MTomczyk
 */
public class TrialPoolExecutor
{
    /**
     * Executor service responsible for running/handling the trial executors {@link TrialExecutor}.
     */
    protected ExecutorService _executorService;

    /**
     * Trial executors to be processed.
     */
    protected ArrayList<Callable<TrialSummary>> _trialExecutors;

    /**
     * Future objects (each per one executor) that can be used, e.g., to handle the exception messages.
     */
    protected ArrayList<Future<TrialSummary>> _future;

    /**
     * Scenario being currently processed.
     */
    protected final Scenario _scenario;

    /**
     * Parameterized constructor.
     *
     * @param threads        the number of threads
     * @param trialExecutors trial executors (runnables responsible for executing the test runs on separate threads)
     * @param scenario       scenario being currently processed
     */
    public TrialPoolExecutor(int threads, ArrayList<TrialExecutor> trialExecutors, Scenario scenario)
    {
        // do the type casting (trial executor extends callable)
        ArrayList<Callable<TrialSummary>> executors = new ArrayList<>(trialExecutors.size());
        executors.addAll(trialExecutors);

        _executorService = Executors.newFixedThreadPool(threads);
        _trialExecutors = executors;
        _future = new ArrayList<>(_trialExecutors.size());
        _scenario = scenario;
    }

    /**
     * This method submits all the callables for execution.
     */
    public void execute()
    {
        for (Callable<TrialSummary> trialExecutor : _trialExecutors)
            _future.add(_executorService.submit(trialExecutor));
    }

    /**
     * Getter for the execution results (future objects).
     * Called by {@link ScenarioExecutor} after all trials are processed (ensured by a barrier).
     *
     * @return execution results
     */
    public ArrayList<Future<TrialSummary>> getFuture()
    {
        return _future;
    }

    /**
     * Clears the data.
     *
     * @throws ScenarioException scenario exception can be thrown and propagated to the global executor
     */
    public void dispose() throws ScenarioException
    {
        try
        {
            _executorService.shutdown();
            _executorService = null;
            _trialExecutors = null;
        } catch (Exception e)
        {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, _scenario);
        }

    }

}
