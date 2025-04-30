package y2025.ERS.e3_samplers;

import container.Containers;
import executor.ScenariosSummarizer;
import summary.Summary;

/**
 * Performs per-scenario summary.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class ExecuteScenarioSummarizer
{
    /**
     * Performs per-scenario summary.
     *
     * @param args arguments that can be passed via the console.
     */
    public static void main(String[] args)
    {
        try
        {
            Containers containers = ContainersGetter.getContainers();
            ScenariosSummarizer.Params pScS = new ScenariosSummarizer.Params(containers);
            ScenariosSummarizer scenariosSummarizer = new ScenariosSummarizer(pScS);

            Summary s = scenariosSummarizer.execute(null);
            System.out.println(s.getStringRepresentation(false));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}
