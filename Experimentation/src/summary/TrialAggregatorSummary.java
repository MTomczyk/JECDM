package summary;

import executor.ScenariosSummarizer;

/**
 * An auxiliary class that summarizes the execution process.
 * It is an extension dedicated to {@link ScenariosSummarizer} executor.
 *
 * @author MTomczyk
 */


public class TrialAggregatorSummary extends Summary
{
    /**
     * Auxiliary method for appending a header to the summary (string representation).
     *
     * @param sb string builder being used by {@link Summary#toString()})
     * @param ls line separator
     */
    @Override
    protected void appendHeader(StringBuilder sb, String ls)
    {
        sb.append("THE SUMMARY OF THE TRIAL AGGREGATION PROCESS").append(ls);
    }

}
