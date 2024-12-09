package summary;

import executor.ScenariosSummarizer;
import scenario.Scenario;

/**
 * Each instance represents a summary of the scenario execution.
 * This extension is used by {@link ScenariosSummarizer} executor.
 *
 * @author MTomczyk
 */


public class TrialAggregatorScenarioSummary extends ScenarioSummary
{

    /**
     * Parameterized constructor.
     *
     * @param scenario scenario linked to the summary
     */
    public TrialAggregatorScenarioSummary(Scenario scenario)
    {
        super(scenario);
    }

    /**
     * Auxiliary method for appending statistics related to trials (the extension skips this step).
     *
     * @param sb  string builder being used by {@link ScenarioSummary#toString()}
     * @param ls  line separator
     * @param ind indent
     */
    @Override
    protected void appendTrialStatistics(StringBuilder sb, String ls, String ind)
    {
    }
}
