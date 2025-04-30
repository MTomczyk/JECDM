package y2025.ERS.e1_auxiliary;

import alternative.Alternative;
import exeption.PreferenceModelException;
import io.FileUtils;
import model.IPreferenceModel;
import model.internals.value.scalarizing.LNorm;
import print.PrintUtils;
import problem.moo.ReferencePointsFactory;
import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This runnable prepares dummy pairs of solutions to be compared in the experiments.
 *
 * @author MTomczyk
 */
public class GeneratePCsData
{
    /**
     * Simple wrapper for reference points.
     */
    public static class RP
    {
        /**
         * Reference point.
         */
        public final double[] _rp;

        /**
         * Reference point evaluation.
         */
        public double _e;

        /**
         * Parameterized constructor.
         *
         * @param rp reference point
         */
        public RP(double[] rp)
        {
            _rp = rp;
        }
    }


    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0); // RNG
        int noSamples = 100000; // no samples per scenario and pcs
        int pcs = 10; // no pcs
        int trials = 100; // no trials
        double[] alphas = new double[]{1.0d, 5.0d, Double.POSITIVE_INFINITY}; // DMs' compensation levels

        try
        {
            Path path = FileUtils.getPathRelatedToClass(GeneratePCsData.class, "Projects", "src", File.separatorChar);
            String fp = path.toString() + File.separatorChar + "pcs.txt";
            FileWriter writer = new FileWriter(fp);

            // Generate random points:
            double[][][][] rps = new double[4][pcs][][];
            ArrayList<ArrayList<ArrayList<RP>>> RPs = new ArrayList<>(pcs);

            for (int M = 2; M < 6; M++)
            {
                ArrayList<ArrayList<RP>> perM = new ArrayList<>(4);
                for (int pc = 0; pc < pcs; pc++)
                {
                    double rM = 1.5d - 0.5d * (pc / (double) (pcs - 1));
                    rps[M - 2][pc] = ReferencePointsFactory.getUniformRandomRPsOnConvexSphere(noSamples, M, R);
                    assert rps[pc] != null;
                    ArrayList<RP> arp = new ArrayList<>(noSamples);
                    for (double[] rp : rps[M - 2][pc])
                    {
                        for (int m = 0; m < M; m++) rp[m] *= rM;
                        arp.add(new RP(rp));
                    }
                    perM.add(arp);
                }
                RPs.add(perM);
            }

            // Generate indices:
            int[] idxAt = new int[pcs * 2];
            double gamma = 2.0d;
            for (int i = 0; i < pcs * 2; i++)
            {
                double v = Math.pow((pcs * 2 - i - 1) / (double) (pcs * 2), gamma);
                idxAt[i] = (int) (v * noSamples + 0.5d);
            }
            PrintUtils.printVectorOfIntegers(idxAt);

            // Iterate over alphas
            for (double alpha : alphas)
            {
                writer.write(alpha + System.lineSeparator());

                System.out.println("Processing alpha = " + alpha);
                // Iterate over objectives
                for (int M = 2; M < 6; M++)
                {
                    writer.write(M + System.lineSeparator());

                    System.out.println("Processing M = " + M);

                    // Iterate over trials
                    for (int t = 0; t < trials; t++)
                    {
                        System.out.println("Processing t = " + t);
                        StringBuilder line = new StringBuilder();

                        // Generate random DM:
                        double[] dmW = WeightsGenerator.getNormalizedWeightVector(M, R);
                        IPreferenceModel<LNorm> model = new model.definitions.LNorm(new LNorm(dmW, alpha));
                        for (int m = 0; m < M; m++) line.append(dmW[m]).append(" ");

                        // Apply scores:
                        for (ArrayList<RP> arp : RPs.get(M - 2))
                        {
                            for (RP rp : arp)
                                rp._e = model.evaluate(new Alternative("A", rp._rp));
                            arp.sort(Comparator.comparingDouble(o -> o._e));
                        }

                        // Save best performer:
                        for (int m = 0; m < M; m++) line.append(RPs.get(M - 2).get(0).get(0)._rp[m]).append(" ");

                        // Generate solution pairs:
                        for (int pc = 0; pc < pcs; pc++)
                        {
                            int i1 = idxAt[pc * 2];
                            int i2 = idxAt[pc * 2 + 1];
                            double[] v1 = RPs.get(M - 2).get(pc).get(i1)._rp;
                            double[] v2 = RPs.get(M - 2).get(pc).get(i2)._rp;

                            for (int m = 0; m < M; m++) line.append(v1[m]).append(" ");

                            for (int m = 0; m < M; m++)
                            {
                                line.append(v2[m]);
                                if ((pc == pcs - 1) && (m == M - 1)) line.append(System.lineSeparator());
                                else line.append(" ");
                            }
                        }

                        writer.write(line.toString());
                    }

                    System.gc();
                }
            }

            writer.close();

        } catch (PreferenceModelException |
                 IOException e)

        {
            throw new RuntimeException(e);
        }
    }
}
