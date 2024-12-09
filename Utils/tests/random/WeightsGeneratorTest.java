package random;

import org.apache.commons.math4.legacy.stat.StatUtils;
import org.junit.jupiter.api.Test;
import print.PrintUtils;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for {@link WeightsGenerator} class.
 *
 * @author MTomczyk
 */
class WeightsGeneratorTest
{

    /**
     * Test #1.
     */
    @Test
    void getNormalizedWeightVector1()
    {
        IRandom R = new MersenneTwister64(0);
        int T = 1000000;
        for (int t = 0; t < T; t++)
        {
            double[] w = WeightsGenerator.getNormalizedWeightVector(1, R);
            assertEquals(1, w.length);
            assertEquals(1.0d, w[0], 0.001d);
        }
    }

    /**
     * Test #2.
     */
    @Test
    void getNormalizedWeightVector2()
    {
        IRandom R = new MersenneTwister64(0);
        int T = 1000000;
        int disc = 10;
        BucketCoordsTransform transform = new BucketCoordsTransform(2, disc, Range.getNormalRange(), new LinearlyThresholded());
        double[][] H = new double[disc][disc];
        for (int t = 0; t < T; t++)
        {
            double[] w = WeightsGenerator.getNormalizedWeightVector(2, R);
            int[] c = transform.getBucketCoords(w);
            if (c == null) continue;
            H[c[0]][c[1]]++;
        }
        for (int m1 = 0; m1 < disc; m1++)
            for (int m2 = 0; m2 < disc; m2++) H[m1][m2] /= T;
        PrintUtils.print2dDoubles(H, 3);

        double expected = 1.0d / disc;
        for (int i = 0; i < disc; i++)
            for (int j = 0; j < disc; j++)
            {
                if (i + j == disc - 1) assertEquals(expected, H[i][j], 0.001d);
                else assertEquals(0.0d, H[i][j], 0.001d);
            }
    }


    /**
     * Test #3.
     */
    @Test
    void getNormalizedWeightVector3()
    {
        IRandom R = new MersenneTwister64(0);
        int T = 10000000;
        int disc = 10;
        BucketCoordsTransform transform = new BucketCoordsTransform(3, disc, Range.getNormalRange(), new LinearlyThresholded());
        double[][][] H = new double[disc][disc][disc];
        for (int t = 0; t < T; t++)
        {
            double[] w = WeightsGenerator.getNormalizedWeightVector(3, R);
            int[] c = transform.getBucketCoords(w);
            if (c == null) continue;
            H[c[0]][c[1]][c[2]]++;
        }
        for (int m1 = 0; m1 < disc; m1++)
            for (int m2 = 0; m2 < disc; m2++)
                for (int m3 = 0; m3 < disc; m3++) H[m1][m2][m3] /= T;


        double expected = (1.0d / disc) * (1.0d / disc);
        for (int i = 0; i < disc; i++)
            for (int j = 0; j < disc; j++)
                for (int k = 0; k < disc; k++)
                {
                    if ((i + j + k == disc - 1) || (i + j + k == disc - 2)) assertEquals(expected, H[i][j][k], 0.001d);
                    else assertEquals(0.0d, H[i][j][k], 0.001d);
                }
    }

    /**
     * Test #4.
     */
    @Test
    void getNormalizedWeightVector4()
    {
        IRandom R = new MersenneTwister64(0);
        int T = 10000000;

        for (int m = 1; m < 10; m++)
        {
            for (int t = 0; t < T; t++)
            {
                double [] w = WeightsGenerator.getNormalizedWeightVector(m, R);
                double sum = StatUtils.sum(w);
                assertEquals(1.0d, sum, 0.00000000001d);
            }
        }
    }
}