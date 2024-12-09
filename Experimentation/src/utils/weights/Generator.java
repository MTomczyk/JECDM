package utils.weights;

import random.IRandom;
import random.MersenneTwister64;
import random.WeightsGenerator;

/**
 * Auxiliary method (main) for pre-generating vectors.
 *
 * @author MTomczyk
 */
public class Generator
{
    /**
     * Generates and prints vectors.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int M = 10;
        int n = 100;

        for (int i = 0; i < n; i++) {
            double[] w = WeightsGenerator.getNormalizedWeightVector(M, R);
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (int m = 0; m < M; m++) {
                String v = String.valueOf(w[m]);
                sb.append(v.replace(',', '.'));
                if (m < M - 1) sb.append(", ");
            }
            sb.append("}");
            if (i < n - 1) sb.append(",");
            System.out.println(sb);
        }
    }
}
