package executor;

import container.scenario.AbstractScenarioDataContainer;
import container.trial.AbstractTrialDataContainer;
import container.trial.TrialDataContainerFactory;
import ea.EA;
import exception.AbstractExperimentationException;
import exception.TrialException;
import indicator.IIndicator;
import random.IRandom;
import runner.IRunner;
import summary.TrialSummary;
import utils.Log;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * The executor object is responsible for processing one test run of a scenario.
 *
 * @author MTomczyk
 */
public class TrialExecutor extends AbstractExecutor implements Callable<TrialSummary>
{
    /**
     * Reference to the parent (i.e., scenario-related) scenario data container.
     */
    protected final AbstractScenarioDataContainer _SDC;

    /**
     * Unique ID of the test run to be performed.
     */
    protected final TrialDataContainerFactory _TDCF;

    /**
     * Unique ID associated with the test run to be executed.
     */
    protected final int _trialID;

    /**
     * Reference to the count-down latch that will be reduced by the monitor thread after all executors finish their processing.
     */
    protected final CountDownLatch _latch;

    /**
     * Trial data container that will be instantiated by the callable.
     */
    protected AbstractTrialDataContainer _TDC;

    /**
     * Field reporting the current generation (can be accessed via the monitor object; volatile).
     */
    protected volatile int _currentGeneration;

    /**
     * Field reporting whether the exception was caught throughout the execution (can be accessed via the monitor object; volatile).
     */
    protected volatile boolean _exceptionCaught;

    /**
     * A flag indicating whether the trial is in the state of waiting for being processed.
     */
    protected volatile boolean _awaiting;

    /**
     * A flag indicating whether the trial execution is being processed.
     */
    protected volatile boolean _processed;

    /**
     * A flag indicating whether the trial execution is finished (false if it awaits execution or is being executed but the processing is not completed).
     */
    protected volatile boolean _finished;


    /**
     * Trial summary (lazy init; when run).
     */
    private TrialSummary _trialSummary = null;

    /**
     * Random number generator associated with this trial executor.
     */
    private final IRandom _R;


    /**
     * Parameterized constructor.
     *
     * @param SDC     to the parent (i.e., scenario-related) scenario data container
     * @param TDCF    factory object that allows to create new instances of {@link AbstractTrialDataContainer}
     * @param trialID Unique ID associated with the test run to be executed
     * @param R       random number generator
     * @param latch   reference to the count-down latch that will be reduced by the monitor thread after all executors finish their processing
     * @param log     provides means for logging (mainly printing messages to the console)
     */
    public TrialExecutor(AbstractScenarioDataContainer SDC,
                         TrialDataContainerFactory TDCF,
                         int trialID,
                         IRandom R,
                         CountDownLatch latch,
                         Log log)
    {
        super(log, 6);
        _R = R;
        _SDC = SDC;
        _TDCF = TDCF;
        _trialID = trialID;
        _latch = latch;
        _awaiting = true;
        _processed = false;
        _finished = false;
        _exceptionCaught = false;
        _currentGeneration = 0;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public TrialSummary call() throws Exception
    {
        _awaiting = false;
        _processed = true;
        _trialSummary = new TrialSummary(_trialID);
        _trialSummary.setStartTimestamp(LocalDateTime.now());

        try {
            _TDC = _TDCF.getInstance(_SDC, _trialID, _R);
            run();

        } catch (TrialException e) {
            terminate(e);
            throw e;
        }

        // regular finalization
        _trialSummary.setStopTimestamp(LocalDateTime.now());
        _finished = true;
        _processed = false;
        _latch.countDown();

        return _trialSummary;
    }

    /**
     * Auxiliary method for termination.
     *
     * @param e exception that caused the termination
     */
    private void terminate(AbstractExperimentationException e)
    {
        _trialSummary.setExceptionMessage(_log.getTerminationMessage(e));
        _trialSummary.setTerminationDueToException(true);
        _trialSummary.setStopTimestamp(LocalDateTime.now());
        _exceptionCaught = true;
        _finished = true;
        _processed = false;
        _awaiting = false;
        _latch.countDown();
    }

    /**
     * The main method for performing the evolution, making measurements, and string data.
     *
     * @throws TrialException the trial exception can be thrown and propagated higher
     */
    protected void run() throws TrialException
    {
        try {
            _TDC.createResultsFiles(); // create results files

            IRunner runner = _TDC.getRunner();
            EA ea = _TDC.getEA();

            IIndicator[] indicators = _TDC.getPerformanceIndicators();
            double[][] results = _TDC.getReferenceToResults(); // change values by reference
            int columnsCap = results[0].length; // generation cap
            int column = 0; // currently processed column

            runner.init();
            updateResults(ea, results, indicators, column);

            if (column + 1 >= columnsCap) {
                _TDC.pushResults(results, 0, columnsCap);
                _TDC.clearResults();
            }
            else column++;


            for (int g = 1; g < _SDC.getGenerations(); g++) // the init counts as the first generation
            {
                _currentGeneration = g;

                runner.executeSingleGeneration(g, null);
                updateResults(ea, results, indicators, column);

                if (column + 1 >= columnsCap) {
                    column = 0;
                    _TDC.pushResults(results, 0, columnsCap);
                    _TDC.clearResults();
                }
                else {
                    column++;
                }
            }

            if (column != 0) {
                _TDC.pushResults(results, 0, column);
                _TDC.clearResults();
            }

            _TDC.closeResultsFiles();
        } catch (Exception e) {
            throw new TrialException(e.getMessage(), this.getClass(), e, _SDC.getScenario(), _trialID);
        }
    }

    /**
     * Auxiliary method for updating the results matrix
     *
     * @param ea         instance of the evolutionary algorithm
     * @param results    the results matrix
     * @param indicators performance indicators used
     * @param column     index to the column in the results matrix being currently updated
     * @throws TrialException the trial exception can be thrown and propagated higher
     */
    private void updateResults(EA ea, double[][] results, IIndicator[] indicators, int column) throws TrialException
    {
        for (int i = 0; i < indicators.length; i++) {
            double score = indicators[i].evaluate(ea);
            results[i][column] = score;
        }
    }


    /**
     * Getter for the trial summary.
     *
     * @return trial summary
     */
    public TrialSummary getSummary()
    {
        return _trialSummary;
    }

    /**
     * Getter for the trial ID.
     *
     * @return trial ID
     */
    public int getTrialID()
    {
        return _trialID;
    }

    /**
     * Getter for the field reporting the current generation (can be accessed via the monitor object; volatile).
     *
     * @return current generation
     */
    public int getCurrentGeneration()
    {
        return _currentGeneration;
    }

    /**
     * Can be called to check whether the executor finished the processing.
     *
     * @return true = the processing is finished; false otherwise
     */
    public boolean isFinished()
    {
        return _finished;
    }

    /**
     * Can be called to check whether the executor is being processed.
     *
     * @return true = the processing is being processed; false otherwise
     */
    public boolean isProcessed()
    {
        return _processed;
    }

    /**
     * Can be called to check whether the trial is currently awaiting the start of processed.
     *
     * @return true = the trial is being processed; false otherwise
     */
    public boolean isAwaiting()
    {
        return _awaiting;
    }

    /**
     * Getter for the field reporting whether the exception was caught throughout the execution (can be accessed via the monitor object; volatile).
     *
     * @return true = exception was caught; false otherwise
     */
    public boolean wasExceptionCaught()
    {
        return _exceptionCaught;
    }

}
