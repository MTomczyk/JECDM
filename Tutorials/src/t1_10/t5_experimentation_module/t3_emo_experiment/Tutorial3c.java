package t1_10.t5_experimentation_module.t3_emo_experiment;

import executor.CrossSummarizer;
import executor.ScenariosSummarizer;
import summary.Summary;

/**
 * This tutorial showcases how to design a simple experiment for comparing methods for evolutionary multi-objective
 * optimization.
 *
 * @author MTomczyk
 */
public class Tutorial3c
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        CrossSummarizer.Params pCS = new CrossSummarizer.Params(Utils.getContainers());
        CrossSummarizer CS = new CrossSummarizer(pCS);

        Summary summary = CS.execute();
        System.out.println(summary.getStringRepresentation(false));
    }
}
