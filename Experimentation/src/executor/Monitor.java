package executor;

import container.scenario.AbstractScenarioDataContainer;
import exception.ScenarioException;
import utils.Level;
import utils.Log;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * The monitor thread is run along the regular trial threads ({@link TrialExecutor}.
 * The monitor will observe the current processing state (e.g., how many test runs are completed and what the progress
 * of the rest is) and print the reports periodically to the console.
 *
 * @author MTomczyk
 */
public class Monitor implements Callable<MonitorReport>
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Scenario data container.
         */
        public AbstractScenarioDataContainer _SDC = null;

        /**
         * Starting data (indicates when the processing began).
         */
        public LocalDateTime _startTimestamp;

        /**
         * Provides means for logging (mainly printing messages to the console).
         */
        public Log _log = null;

        /**
         * Reference to all trial executors that are to be processed on separate threads.
         */
        public ArrayList<TrialExecutor> _trialExecutors = null;

        /**
         * Reference to the count-down latch that will be reduced by the monitor thread after all executors finish their processing.
         */
        public CountDownLatch _latch = null;

        /**
         * Delay between constructing subsequent reports (in milliseconds).
         */
        public int _delay = 0;
    }

    /**
     * Indent used when printing the reports.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int _indent = 6;

    /**
     * Scenario data container.
     */
    private final AbstractScenarioDataContainer _SDC;

    /**
     * Starting data (indicates when the processing began).
     */
    private final LocalDateTime _startDate;

    /**
     * Provides means for logging (mainly printing messages to the console).
     */
    private final Log _log;

    /**
     * Reference to all trial executors that are to be processed on separate threads.
     */
    private final ArrayList<TrialExecutor> _trialExecutors;

    /**
     * Reference to the count-down latch that will be reduced by the monitor thread after all executors finish their processing.
     */
    private final CountDownLatch _latch;

    /**
     * Delay between constructing subsequent reports (in nanoseconds).
     */
    private final long _delay;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Monitor(Params p)
    {
        _SDC = p._SDC;
        _startDate = p._startTimestamp;
        _log = p._log;
        _trialExecutors = p._trialExecutors;
        _latch = p._latch;
        _delay = p._delay * 1000000L; // conversion
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public MonitorReport call() throws Exception
    {
        MonitorReport report = new MonitorReport();

        try {
            long startTime = System.nanoTime();
            while (areSomeUnfinishedTrials()) {
                if (System.nanoTime() - startTime > _delay) {
                    printReport();
                    startTime = System.nanoTime();
                }
            }

            _latch.countDown();

            //_log.log("Monitor thread terminates", Level.Scenario, _indent);

        } catch (Exception e) {
            throw new ScenarioException(e.getMessage(), this.getClass(), e, _SDC.getScenario());
        }

        return report;
    }

    /**
     * The method for printing the report.
     */
    protected void printReport()
    {
        int total = _trialExecutors.size();
        int awaiting = 0;
        int processed = 0;
        int finished = 0;
        int exception = 0;

        LinkedList<Integer> currentTrialIDs = new LinkedList<>();
        LinkedList<Integer> currentGenerations = new LinkedList<>();
        LinkedList<LocalDateTime> startTimestamps = new LinkedList<>();

        for (TrialExecutor te : _trialExecutors) {
            if (te.isAwaiting()) awaiting++;
            if (te.isProcessed()) {
                processed++;
                currentTrialIDs.add(te.getTrialID());
                currentGenerations.add(te.getCurrentGeneration());
                startTimestamps.add(te.getSummary().getStartTimestamp());

            }
            if (te.isFinished()) finished++;
            if (te.wasExceptionCaught()) exception++;
        }

        LocalDateTime now = LocalDateTime.now();
        //System.out.println();
        _log.log("Monitor log ======================================================================", Level.Scenario, _indent);
        _log.log("Scenario = " + _SDC.toString(), Level.Scenario, _indent);
        String delta = StringUtils.getDeltaTime(_startDate, now);
        _log.log("Processing for = " + delta, Level.Scenario, _indent);
        _log.log("The total number of trials = " + total, Level.Scenario, _indent);
        _log.log("The number of awaiting trials = " + awaiting, Level.Scenario, _indent);
        _log.log("The number of trials being processed = " + processed, Level.Scenario, _indent);
        _log.log("The number of finished trials = " + finished, Level.Scenario, _indent);
        _log.log("The number of trials terminated due to exception = " + exception, Level.Scenario, _indent);
        _log.log("The states of trials being currently processed: ", Level.Scenario, _indent);

        ListIterator<Integer> iID = currentTrialIDs.listIterator();
        ListIterator<Integer> iG = currentGenerations.listIterator();
        ListIterator<LocalDateTime> iD = startTimestamps.listIterator();
        while ((iID.hasNext()) && (iG.hasNext()) && (iD.hasNext())) {
            int trial = iID.next();
            int generation = iG.next();
            LocalDateTime date = iD.next();
            String s1 = "Trial = " + trial;
            String s2 = "Generation = [" + generation + "/" + _SDC.getGenerations() + "]";
            String s3 = "Processing started = " + StringUtils.getTimestamp(date);
            String s4 = "Processing for = " + StringUtils.getDeltaTime(date, now);
            _log.log(s1 + " " + s2 + " " + s3 + " " + s4, Level.Scenario, _indent);
        }
    }

    /**
     * Auxiliary method that checks whether there are still some trials that are not completed.
     *
     * @return true is there are some unfinished trials, false otherwise (are finished or terminated due to the exception).
     */
    private boolean areSomeUnfinishedTrials()
    {
        for (TrialExecutor te : _trialExecutors)
            if (!te.isFinished()) return true;
        return false;
    }


}
