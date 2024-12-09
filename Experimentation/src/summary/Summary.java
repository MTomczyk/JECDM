package summary;


import utils.StringUtils;

/**
 * An auxiliary class that summarizes the execution process.
 *
 * @author MTomczyk
 */


public class Summary extends AbstractSummary
{
    /**
     * Default constructor.
     */
    public Summary()
    {
        super(2);
    }

    /**
     * Scenario summaries.
     */
    protected ScenarioSummary[] _scenariosSummaries = null;

    /**
     * Field representing the number of scenarios that were completed successfully.
     */
    protected int _completedScenarios = 0;

    /**
     * Field representing the number of scenarios that were completed successfully.
     */
    protected int _terminatedScenarios = 0;

    /**
     * Field representing the number of scenarios that were skipped (e.g., due to the disabled flag).
     */
    protected int _skippedScenarios = 0;

    /**
     * Field representing the number of trials that were completed successfully (in total).
     */
    protected int _completedTrials = 0;

    /**
     * Field representing the number of trials that were completed successfully (in total).
     */
    protected int _terminatedTrials = 0;

    /**
     * Field representing the number of trials that were skipped (e.g., due to the disabled flag; in total).
     */
    protected int _skippedTrials = 0;

    /**
     * Generates string representation.
     *
     * @param detailed if true, scenario summaries will be included
     * @return string representation
     */
    public String getStringRepresentation(boolean detailed)
    {
        String ls = System.lineSeparator();
        String ind = StringUtils.getIndent(_indent);
        StringBuilder sb = new StringBuilder();
        appendHeader(sb, ls);
        applyBasicStatistics(sb, _indent);
        appendScenarioSummaries(sb, ls, ind, detailed);
        return sb.toString();
    }

    /**
     * Method that generates the summary (the scenario summaries are not included).
     */
    @Override
    public String toString()
    {
        return getStringRepresentation(false);
    }


    /**
     * Auxiliary method for appending a header to the summary (string representation).
     *
     * @param sb string builder being used by {@link Summary#toString()})
     * @param ls line separator
     */
    protected void appendHeader(StringBuilder sb, String ls)
    {
        sb.append("THE SUMMARY OF THE EXECUTION PROCESS").append(ls);
    }

    /**
     * Auxiliary method for appending scenario summaries
     *
     * @param sb       string builder being used by {@link Summary#toString()})
     * @param ls       line separator
     * @param ind      indent
     * @param detailed if true, scenario summaries will be included
     */
    protected void appendScenarioSummaries(StringBuilder sb, String ls, String ind, boolean detailed)
    {
        if (_scenariosSummaries != null)
        {
            sb.append(ind).append("Total number of scenarios = ").append(_scenariosSummaries.length).append(ls);
            sb.append(ind).append("Successfully completed scenarios = ").append(_completedScenarios).append(" (may include cases where not all trials were completed)").append(ls);
            sb.append(ind).append("Terminated scenarios (due to exception) = ").append(_terminatedScenarios).append(ls);
            sb.append(ind).append("Skipped scenarios (due to being disabled) = ").append(_skippedScenarios).append(ls);
            sb.append(ind).append("Successfully completed trials (in total) = ").append(_completedTrials).append(ls);
            sb.append(ind).append("Terminated trials (due to exception; in total) = ").append(_terminatedTrials).append(ls);
            sb.append(ind).append("Skipped trials (due to being disabled; in total) = ").append(_skippedTrials).append(ls);

            if (detailed)
            {
                sb.append("SCENARIOS SUMMARIES:").append(ls);
                for (ScenarioSummary s : _scenariosSummaries)
                {
                    if (s == null) continue;
                    sb.append(s);
                }
            }
        }
    }

    /**
     * Getter for the scenario summaries.
     *
     * @return scenario summaries
     */
    public ScenarioSummary[] getScenariosSummaries()
    {
        return _scenariosSummaries;
    }

    /**
     * Setter for the scenario summaries.
     *
     * @param scenariosSummaries scenario summaries
     */
    public void setScenariosSummaries(ScenarioSummary[] scenariosSummaries)
    {
        _scenariosSummaries = scenariosSummaries;
    }

    /**
     * Getter for the number of scenarios that were completed successfully.
     *
     * @return the number of scenarios that were completed successfully
     */
    public int getCompletedScenarios()
    {
        return _completedScenarios;
    }

    /**
     * Setter for the number of scenarios that were completed successfully.
     *
     * @param completedScenarios the number of scenarios that were completed successfully
     */
    public void setCompletedScenarios(int completedScenarios)
    {
        _completedScenarios = completedScenarios;
    }

    /**
     * Getter for the number of scenarios that were completed successfully.
     *
     * @return the number of scenarios that were completed successfully
     */
    public int getTerminatedScenarios()
    {
        return _terminatedScenarios;
    }

    /**
     * Setter for the number of scenarios that were completed successfully.
     *
     * @param terminatedScenarios the number of scenarios that were completed successfully
     */
    public void setTerminatedScenarios(int terminatedScenarios)
    {
        _terminatedScenarios = terminatedScenarios;
    }

    /**
     * Getter for the number of scenarios that were skipped (due to the disabled flag).
     *
     * @return the number of scenarios that were skipped (due to the disabled flag)
     */
    public int getSkippedScenarios()
    {
        return _skippedScenarios;
    }

    /**
     * Setter for the number of scenarios that were skipped (due to the disabled flag).
     *
     * @param skippedScenarios the number of scenarios that were skipped (due to the disabled flag
     */
    public void setSkippedScenarios(int skippedScenarios)
    {
        _skippedScenarios = skippedScenarios;
    }

    /**
     * Getter for the number of trials that were completed successfully (in total).
     *
     * @return the number of trials that were completed successfully (in total)
     */
    public int getCompletedTrials()
    {
        return _completedTrials;
    }

    /**
     * Setter for the number of trials that were completed successfully (in total).
     *
     * @param completedTrials the number of trials that were completed successfully (in total)
     */
    public void setCompletedTrials(int completedTrials)
    {
        _completedTrials = completedTrials;
    }

    /**
     * Getter for the number of trials that were completed successfully (in total).
     *
     * @return the number of trials that were completed successfully (in total)
     */
    public int getTerminatedTrials()
    {
        return _terminatedTrials;
    }

    /**
     * Setter for the number of trials that were completed successfully (in total).
     *
     * @param terminatedTrials the number of trials that were completed successfully (in total)
     */
    public void setTerminatedTrials(int terminatedTrials)
    {
        _terminatedTrials = terminatedTrials;
    }

    /**
     * Getter for the number of trials that were skipped (due to the disabled flag; in total).
     *
     * @return the number of trials that were skipped (due to the disabled flag; in total)
     */
    public int getSkippedTrials()
    {
        return _skippedTrials;
    }

    /**
     * Setter for the number of trials that were skipped (due to the disabled flag; in total).
     *
     * @param skippedTrials the number of trials that were skipped (due to the disabled flag
     */
    public void setSkippedTrials(int skippedTrials)
    {
        _skippedTrials = skippedTrials;
    }
}
