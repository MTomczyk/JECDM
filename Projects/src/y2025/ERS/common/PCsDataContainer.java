package y2025.ERS.common;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Simple class loading and storing pairwise comparisons data prepared by {@link y2025.ERS.e1_auxiliary.GeneratePCsData}.
 *
 * @author MTomczyk
 */
public class PCsDataContainer
{
    /**
     * Class wrapping PCs data associated with a trial run.
     */
    public static class TrialPCs
    {
        /**
         * Reference alternatives (evaluations) to be compared by the DM (no PCs x 2 x dimensions).
         */
        public final double[][][] _referenceEvaluations;

        /**
         * DM's model weights.
         */
        public final double[] _dmW;

        /**
         * DM's model alpha value.
         */
        public final double _dmA;

        /**
         * Parameterized constructor.
         *
         * @param referenceEvaluations reference alternatives (evaluations) to be compared by the DM (no PCs x 2 x dimensions)
         * @param dmW                  DM's model weights
         * @param dmA                  DM's model alpha value
         */
        public TrialPCs(double[][][] referenceEvaluations, double[] dmW, double dmA)
        {
            _referenceEvaluations = referenceEvaluations;
            _dmW = dmW;
            _dmA = dmA;
        }
    }

    /**
     * Class wrapping PCs data associated with a scenario.
     */
    public static class ScenarioPCs
    {
        /**
         * Pairwise comparisons data (each for each trial).
         */
        public final TrialPCs[] _trialPCs;

        /**
         * Parameterized constructor.
         *
         * @param trialPCs pairwise comparisons data (each for each trial)
         */
        public ScenarioPCs(TrialPCs[] trialPCs)
        {
            _trialPCs = trialPCs;
        }
    }

    /**
     * Pairwise comparisons data: no. DMs configurations x no. objectives configurations.
     */
    public final ScenarioPCs[][] _PCs;

    /**
     * Parameterized constructor
     *
     * @param path   path to the pcs.txt file
     * @param Ms     no. objectives configurations
     * @param DMs    no. DMs configurations
     * @param trials no. trials
     * @param nPCs   no. PCs
     * @throws Exception the exception can be thrown 
     */
    public PCsDataContainer(String path, int Ms, int DMs, int trials, int nPCs) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        _PCs = new ScenarioPCs[DMs][Ms];

        for (int dmC = 0; dmC < DMs; dmC++) // iterate over DM's configurations
        {
            String sAlpha = br.readLine();
            double alpha = Double.parseDouble(sAlpha);

            for (int mC = 0; mC < Ms; mC++)
            {
                String sM = br.readLine();
                int M = Integer.parseInt(sM);

                TrialPCs[] trialPCs = new TrialPCs[trials];

                _PCs[dmC][mC] = new ScenarioPCs(trialPCs);
                for (int t = 0; t < trials; t++)
                {
                    String line = br.readLine(); // pcs;
                    String[] l = line.split(" ");

                    double[] w = new double[M]; // get weights
                    for (int m = 0; m < M; m++) w[m] = Double.parseDouble(l[m]);

                    double[][][] evaluations = new double[nPCs][2][M];

                    int index = M * 2; // skip the DM's weights and the incumbent
                    for (int pc = 0; pc < nPCs; pc++) // iterate over pcs
                    {
                        for (int s = 0; s < 2; s++) // iterate over solution
                            for (int c = 0; c < M; c++) // iterate over dimensions
                                evaluations[pc][s][c] = Double.parseDouble(l[index++]);
                    }

                    trialPCs[t] = new TrialPCs(evaluations, w.clone(), alpha);
                }
            }
        }
    }
}
