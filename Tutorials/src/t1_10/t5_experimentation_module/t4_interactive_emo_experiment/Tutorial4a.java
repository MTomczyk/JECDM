package t1_10.t5_experimentation_module.t4_interactive_emo_experiment;

import executor.ExperimentPerformer;
import summary.Summary;

/**
 * This tutorial showcases how to design a simple experiment for comparing methods for evolutionary multi-objective
 * optimization.
 *
 * @author MTomczyk
 */
public class Tutorial4a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // The container/factories are constructed using Utils.getContainers()
        ExperimentPerformer.Params pEE = new ExperimentPerformer.Params(Utils.getContainers());
        ExperimentPerformer EE = new ExperimentPerformer(pEE);

        Summary summary = EE.execute();
        System.out.println(summary.getStringRepresentation(false));
    }
}
