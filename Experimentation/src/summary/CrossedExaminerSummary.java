package summary;

import executor.CrossSummarizer;

/**
 * An auxiliary class that summarizes the execution process.
 * It is an extension dedicated to {@link CrossSummarizer} executor.
 *
 * @author MTomczyk
 */


public class CrossedExaminerSummary extends Summary
{
    /**
     * Crossed scenarios summaries.
     */
    protected CrossedScenariosSummary[] _crossedScenarioSummaries = null;

    /**
     * Auxiliary method for appending a header to the summary (string representation).
     *
     * @param sb string builder being used by {@link Summary#toString()})
     * @param ls line separator
     */
    @Override
    protected void appendHeader(StringBuilder sb, String ls)
    {
        sb.append("THE SUMMARY OF THE CROSS-EXAMINATION PROCESS").append(ls);
    }

    /**
     * Auxiliary method for appending crossed scenarios summaries
     *
     * @param sb  string builder being used by {@link Summary#toString()})
     * @param ls  line separator
     * @param ind indent
     * @param detailed if true, scenario summaries will be included
     */
    @Override
    protected void appendScenarioSummaries(StringBuilder sb, String ls, String ind, boolean detailed)
    {
        if (_crossedScenarioSummaries != null)
        {
            sb.append(ind).append("Total number of crossed scenarios = ").append(_crossedScenarioSummaries.length).append(ls);
            sb.append(ind).append("Successfully completed crossed scenarios = ").append(_completedScenarios).append(ls);
            sb.append(ind).append("Terminated (due to exception) crossed scenarios = ").append(_terminatedScenarios).append(ls);

            if (detailed)
            {
                sb.append("CROSSED SCENARIOS SUMMARIES:").append(ls);
                for (CrossedScenariosSummary s : _crossedScenarioSummaries)
                {
                    if (s == null) continue;
                    sb.append(s);
                }
            }
        }
    }


    /**
     * Getter for the crossed scenario summaries.
     *
     * @return crossed scenario summaries
     */
    public CrossedScenariosSummary[] getCrossedScenariosSummaries()
    {
        return _crossedScenarioSummaries;
    }

    /**
     * Setter for the crossed scenario summaries.
     *
     * @param crossedScenariosSummaries crossed scenario summaries
     */
    public void setScenariosSummaries(CrossedScenariosSummary[] crossedScenariosSummaries)
    {
        _crossedScenarioSummaries = crossedScenariosSummaries;
    }

}
