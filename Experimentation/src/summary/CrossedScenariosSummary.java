package summary;

import scenario.CrossedScenarios;
import utils.StringUtils;

import java.util.LinkedList;

/**
 * Each instance represents a summary of the execution of a cross-scenario.
 *
 * @author MTomczyk
 */


public class CrossedScenariosSummary extends AbstractSummary
{
    /**
     * Reference to the linked scenario.
     */
    protected final CrossedScenarios _crossedScenarios;

    /**
     * Stores the number of successfully processed scenarios.
     */
    private int _completedScenarios;

    /**
     * Stores the number of terminated (due to exception) scenarios.
     */
    private int _terminatedScenarios;

    /**
     * Stores the number of skipped (due to being disabled) scenarios.
     */
    private int _skippedScenarios;

    /**
     * Field storing the thrown, by trial executors, error messages.
     */
    private LinkedList<String[]> _crossedScenariosExceptionMessages;


    /**
     * This field stores (default) names of cross-savers that were skipped when processing crossed scenarios.
     */
    private LinkedList<String> _skippedSavers;

    /**
     * Parameterized constructor.
     *
     * @param crossedScenarios crossed scenarios linked to the summary
     */
    public CrossedScenariosSummary(CrossedScenarios crossedScenarios)
    {
        super(4);
        _crossedScenarios = crossedScenarios;
    }

    /**
     * Method that generates the summary (string).
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        String ls = System.lineSeparator();
        sb.append("SUMMARY OF CROSSED SCENARIOS = ").append(_crossedScenarios.toString()).append(ls);
        applyBasicStatistics(sb, _indent);
        String ind = StringUtils.getIndent(_indent);
        sb.append(ind).append("Total number of scenarios = ").append(_crossedScenarios.getReferenceScenariosSorted().length).append(ls);
        sb.append(ind).append("Successfully completed scenarios = ").append(_completedScenarios).append(ls);
        sb.append(ind).append("Terminated (due to exception) scenarios = ").append(_terminatedScenarios).append(ls);
        sb.append(ind).append("Skipped scenarios (due to being disabled) = ").append(_skippedScenarios).append(ls);

        if ((_skippedSavers == null) || (_skippedSavers.isEmpty()))
            sb.append(ind).append("Skipped savers = None").append(ls);
        else
        {
            sb.append(ind).append("Skipped savers = [");
            int idx = -1;
            for (String s : _skippedSavers)
            {
                idx++;
                if (s == null) continue;
                sb.append(s);
                if (idx < _skippedSavers.size() - 1) sb.append(", ");
            }
            sb.append("]").append(ls);
        }

        //noinspection DuplicatedCode
        if ((_crossedScenariosExceptionMessages != null) && (!_crossedScenariosExceptionMessages.isEmpty()))
        {
            sb.append(ind).append("Printing captured error messages").append(ls);
            for (String[] e : _crossedScenariosExceptionMessages)
                for (String s : e) sb.append(ind).append(s);
        }

        return sb.toString();
    }


    /**
     * Getter for the reference to the linked crossed scenarios object.
     *
     * @return crossed scenarios object
     */
    public CrossedScenarios getCrossedScenarios()
    {
        return _crossedScenarios;
    }

    /**
     * Getter for the number of successfully processed scenarios.
     *
     * @return the number of successfully processed scenarios
     */
    public int getCompletedScenarios()
    {
        return _completedScenarios;
    }

    /**
     * Setter for the number of successfully processed scenarios.
     *
     * @param completedScenarios the number of successfully processed scenarios
     */
    public void setCompletedScenarios(int completedScenarios)
    {
        _completedScenarios = completedScenarios;
    }

    /**
     * Getter for the number of terminated (due to exception) scenarios.
     *
     * @return the number of terminated (due to exception) scenarios
     */
    public int getTerminatedScenarios()
    {
        return _terminatedScenarios;
    }

    /**
     * Setter for the number of terminated (due to exception) scenarios.
     *
     * @param terminatedScenarios the number of terminated (due to exception) scenarios
     */
    public void setTerminatedScenarios(int terminatedScenarios)
    {
        _terminatedScenarios = terminatedScenarios;
    }

    /**
     * Getter for the number of skipped (due to being disabled) scenarios.
     *
     * @return the number of skipped (due to being disabled) scenarios
     */
    public int getSkippedScenarios()
    {
        return _skippedScenarios;
    }

    /**
     * Setter for the number of skipped (due to being disabled) scenarios.
     *
     * @param skippedScenarios the number of skipped (due to being disabled) scenarios
     */
    public void setSkippedScenarios(int skippedScenarios)
    {
        _skippedScenarios = skippedScenarios;
    }

    /**
     * Getter for exception messages thrown when processing scenarios.
     *
     * @return exception messages thrown when processing scenarios
     */
    public LinkedList<String[]> getCrossedScenariosExceptionMessages()
    {
        return _crossedScenariosExceptionMessages;
    }

    /**
     * Setter for exception messages thrown when processing scenarios.
     *
     * @param scenariosExceptionMessages exception messages thrown when processing scenarios
     */
    public void setCrossedScenariosExceptionMessages(LinkedList<String[]> scenariosExceptionMessages)
    {
        _crossedScenariosExceptionMessages = scenariosExceptionMessages;
    }

    /**
     * Getter for the field that stores (default) names of cross-savers that were skipped when processing crossed scenarios.
     *
     * @return skipped savers
     */
    public LinkedList<String> getSkippedSavers()
    {
        return _skippedSavers;
    }

    /**
     * Setter for the field that stores (default) names of cross-savers that were skipped when processing crossed scenarios.
     *
     * @param skippedSavers skipped savers
     */
    public void setSkippedSavers(LinkedList<String> skippedSavers)
    {
        this._skippedSavers = skippedSavers;
    }

}
