package executor.complex.case1;

import container.scenario.AbstractScenarioDataContainer;
import container.trial.AbstractTrialDataContainer;
import container.trial.TrialDataContainerFactory;
import exception.TrialException;
import executor.TrialExecutor;
import random.IRandom;
import summary.TrialSummary;
import utils.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * A dummy extension of {@link }. This extension iterates over the requested number of generations, delaying each
 * iteration as requested. Additionally, the main running procedure may trigger an exception.
 *
 * @author MTomczyk
 */
public class DummyTrialExecutor extends TrialExecutor implements Callable<TrialSummary>
{
    /**
     * Parameterized constructor.
     *
     * @param SDC            to the parent (i.e., scenario-related) scenario data container
     * @param TDCF           factory object that allows to create new instances of {@link AbstractTrialDataContainer}
     * @param trialID        Unique ID associated with the test run to be executed
     * @param R              random number generator
     * @param latch          reference to the count-down latch that will be reduced by the monitor thread after all executors finish their processing
     * @param log            provides means for logging (mainly printing messages to the console)
     * @param delay          delay between subsequent generations (in milliseconds)
     * @param throwException an array of flags indicating whether a trial should be forced to cast an exception (i-th object is assumed to be linked with a trial with id = i)
     */
    public DummyTrialExecutor(AbstractScenarioDataContainer SDC,
                              TrialDataContainerFactory TDCF,
                              int trialID,
                              IRandom R,
                              CountDownLatch latch,
                              Log log,
                              int delay,
                              boolean[] throwException)
    {
        super(SDC, TDCF, trialID, R, latch, log);
        _delay = delay;
        _throwException = throwException;
    }

    /**
     * Delay between subsequent generations (in milliseconds).
     */
    private final int _delay;

    /**
     * An array of flags indicating whether a trial should be forced to cast an exception (i-th object is assumed to be linked with a trial with id = i).
     */
    private final boolean[] _throwException;

    /**
     * The main method for performing the evolution, making measurements, and string data.
     */
    @Override
    protected void run() throws TrialException
    {
        if ((_throwException != null) && (_throwException.length > _trialID) && (_throwException[_trialID]))
            throw new TrialException("Dummy exception is thrown", this.getClass(), _SDC.getScenario(), _trialID);

        int generations = _SDC.getGenerations();

        for (int g = 0; g < generations; g++)
        {
            try
            {
                Thread.sleep(_delay);
            } catch (InterruptedException e)
            {
                throw new TrialException(e.getMessage(), this.getClass(), _SDC.getScenario(), _trialID);
            }
            _currentGeneration = g;
        }

    }
}
