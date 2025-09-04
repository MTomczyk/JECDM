package y2025.SoftwareX_JECDM;

import container.Containers;
import executor.ExperimentPerformer;
import summary.Summary;

/**
 * Experiment executor.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class ExecuteExperiment
{
    /**
     * Runs the experiment.
     *
     * @param args arguments that can be passed via the console.
     */
    public static void main(String[] args)
    {
        try
        {
            Containers containers = ContainersGetter.getContainers();
            ExperimentPerformer.Params pE = new ExperimentPerformer.Params(containers);
            ExperimentPerformer EP = new ExperimentPerformer(pE);

            Summary s = EP.execute(null);
            System.out.println(s.getStringRepresentation(false));

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
