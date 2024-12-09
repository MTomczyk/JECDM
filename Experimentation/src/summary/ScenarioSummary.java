package summary;

import scenario.Scenario;
import utils.StringUtils;

import java.util.LinkedList;

/**
 * Each instance represents a summary of the scenario execution.
 *
 * @author MTomczyk
 */


public class ScenarioSummary extends AbstractSummary
{
    /**
     * Reference to the linked scenario.
     */
    protected final Scenario _scenario;

    /**
     * Flag indicating whether the scenario was skipped.
     */
    protected boolean _skipped = false;

    /**
     * Field storing data on the total number of trials.
     */
    protected int _noTrials;

    /**
     * Field storing data on the number of successfully completed test runs.
     */
    protected int _completedTrials;

    /**
     * Field storing data on the number of terminated test runs (due to exception).
     */
    protected int _terminatedTrials;

    /**
     * Field storing data on the number of skipped test runs (e.g., due to disabling conditions).
     */
    protected int _skippedTrials;

    /**
     * Field storing the thrown, by trial executors, error messages.
     */
    protected LinkedList<String[]> _trialsExceptionMessages;

    /**
     * Parameterized constructor.
     *
     * @param scenario scenario linked to the summary
     */
    public ScenarioSummary(Scenario scenario)
    {
        super(4);
        _scenario = scenario;
        _noTrials = 0;
        _completedTrials = 0;
        _terminatedTrials = 0;
        _skippedTrials = 0;
        _trialsExceptionMessages = new LinkedList<>();
    }

    /**
     * Method that generates the summary (string).
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        String ls = System.lineSeparator();
        sb.append("SUMMARY OF SCENARIO = ").append(_scenario.toString()).append(ls);
        applyBasicStatistics(sb, _indent);

        String ind = StringUtils.getIndent(_indent);
        sb.append(ind).append("Skipped due to being disabled = ").append(_skipped).append(ls);
        appendTrialStatistics(sb, ls, ind);

        //noinspection DuplicatedCode
        if ((_trialsExceptionMessages != null) && (!_trialsExceptionMessages.isEmpty()))
        {
            sb.append(ind).append("Printing captured error messages").append(ls);
            for (String[] e : _trialsExceptionMessages)
                for (String s : e) sb.append(ind).append(s);
        }

        return sb.toString();
    }

    /**
     * Auxiliary method for appending statistics related to trials.
     *
     * @param sb  string builder being used by {@link ScenarioSummary#toString()}
     * @param ls  line separator
     * @param ind indent
     */
    protected void appendTrialStatistics(StringBuilder sb, String ls, String ind)
    {
        sb.append(ind).append("Total number of trials = ").append(_noTrials).append(" (excludes disabled trials)").append(ls);
        sb.append(ind).append("Successfully completed trials = ").append(_completedTrials).append(ls);
        sb.append(ind).append("Terminated trials (due to exception) = ").append(_terminatedTrials).append(ls);
        sb.append(ind).append("Skipped trials (due to being disabled) = ").append(_skippedTrials).append(ls);
    }

    /**
     * Getter for the reference to the linked scenario.
     *
     * @return the linked scenario
     */
    public Scenario getScenario()
    {
        return _scenario;
    }

    /**
     * Getter for the flag indicating whether the scenario was skipped.
     *
     * @return the flag
     */
    public boolean isSkipped()
    {
        return _skipped;
    }

    /**
     * Setter for the flag indicating whether the scenario was skipped.
     *
     * @param skipped true = the scenario was skipped; false otherwise
     */
    public void setSkipped(boolean skipped)
    {
        _skipped = skipped;
    }

    /**
     * Getter for the total number of trials.
     *
     * @return the total number of trials
     */
    public int getNoTrials()
    {
        return _noTrials;
    }

    /**
     * Setter for the total number of trials
     *
     * @param noTrials the total number of trials
     */
    public void setNoTrials(int noTrials)
    {
        _noTrials = noTrials;
    }

    /**
     * Getter for the number of successfully completed test runs.
     *
     * @return the number of successfully completed test runs
     */
    public int getCompletedTrials()
    {
        return _completedTrials;
    }

    /**
     * Setter for the number of successfully completed test runs.
     *
     * @param completedTrials the number of successfully completed test runs
     */
    public void setCompletedTrials(int completedTrials)
    {
        _completedTrials = completedTrials;
    }

    /**
     * Getter for the number of terminated test runs (due to exception).
     *
     * @return the number of terminated test runs (due to exception)
     */
    public int getTerminatedTrials()
    {
        return _terminatedTrials;
    }

    /**
     * Setter for the number of terminated test runs (due to exception).
     *
     * @param terminatedTrials the number of terminated test runs (due to exception)
     */
    public void setTerminatedTrials(int terminatedTrials)
    {
        _terminatedTrials = terminatedTrials;
    }

    /**
     * Getter for the number of skipped test runs (e.g., due to disabling conditions).
     *
     * @return the number of skipped test runs (e.g., due to disabling conditions)
     */
    public int getSkippedTrials()
    {
        return _skippedTrials;
    }

    /**
     * Setter for the number of skipped test runs (e.g., due to disabling conditions).
     *
     * @param skippedTrials the number of skipped  test runs (e.g., due to disabling conditions)
     */
    public void setSkippedTrials(int skippedTrials)
    {
        _skippedTrials = skippedTrials;
    }


    /**
     * Getter for the collection of thrown, by trial executors, error messages.
     *
     * @return collection of thrown, by trial executors, error messages
     */
    public LinkedList<String[]> getTrialsExceptionMessages()
    {
        return _trialsExceptionMessages;
    }

    /**
     * Setter for the collection of thrown, by trial executors, error messages.
     *
     * @param trialsExceptionMessages collection of thrown, by trial executors, error messages
     */
    public void setTrialsExceptionMessages(LinkedList<String[]> trialsExceptionMessages)
    {
        _trialsExceptionMessages = trialsExceptionMessages;
    }


}
