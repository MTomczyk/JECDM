package executor.complex.case1;

import container.scenario.AbstractScenarioDataContainer;
import container.trial.TrialDataContainerFactory;
import executor.ScenarioExecutor;
import executor.TrialExecutor;
import random.IRandom;
import summary.ScenarioSummary;
import utils.Log;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Dummy extension of {@link ScenarioExecutor} (for testing).
 * Instantiates dummy trial executors.
 *
 * @author MTomczyk
 */
public class DummyScenarioExecutor extends ScenarioExecutor
{
    /**
     * Parameterized constructor.
     *
     * @param SDC             scenario data container
     * @param TDCF            trial data container factory (creates the object handling the trial-level data)
     * @param scenarioSummary summary object (filled throughout the execution)
     * @param log             provides means for logging (mainly printing messages to the console)
     * @param delay          delay between subsequent generations (in milliseconds)
     * @param throwException an array of flags indicating whether a trial should be forced to cast an exception (i-th object is assumed to be linked with a trial with id = i)
     */
    public DummyScenarioExecutor(AbstractScenarioDataContainer SDC,
                                 TrialDataContainerFactory TDCF,
                                 ScenarioSummary scenarioSummary,
                                 Log log,
                                 int delay,
                                 boolean [] throwException)
    {
        super(SDC, TDCF, scenarioSummary, log);
        _delay = delay;
        _throwException = throwException;
    }


    /**
     * Delay between subsequent generations (in milliseconds).
     */
    private final int _delay;

    /**
     * If true, the main running method will immediately throw an exception.
     */
    private final boolean [] _throwException;

    /**
     * This method creates trial executors (each executes one test run of the scenario).
     *
     * @param latch reference to the count-down latch that will be reduced by the monitor thread after all executors
     *              finish their processing
     * @return the collection of trial executors (runnables) to be processed via a thread pool executor
     */
    @Override
    protected ArrayList<TrialExecutor> constructTrialExecutors(CountDownLatch latch)
    {
        ArrayList<TrialExecutor> executors = new ArrayList<>(_SDC.getGDC().getNoEnabledTrials());
        for (int id : _SDC.getGDC().getTrialIDs())
        {
            IRandom R = _SDC.getGDC().requestRandomNumberGenerator(); // request RNG (synchronous)
            executors.add(new DummyTrialExecutor(_SDC, _TDCF, id, R, latch, _log, _delay, _throwException));
        }
        return executors;
    }


}
