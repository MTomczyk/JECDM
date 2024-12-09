package t1_10.t5_experimentation_module.t3_emo_experiment;

import executor.ScenariosSummarizer;
import summary.Summary;

/**
 * This tutorial showcases how to design a simple experiment for comparing methods for evolutionary multi-objective
 * optimization.
 *
 * @author MTomczyk
 */
public class Tutorial3b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        ScenariosSummarizer.Params pScS = new ScenariosSummarizer.Params(Utils.getContainers());
        ScenariosSummarizer ScS = new ScenariosSummarizer(pScS);

        Summary summary = ScS.execute();
        System.out.println(summary.getStringRepresentation(false));
    }
}
