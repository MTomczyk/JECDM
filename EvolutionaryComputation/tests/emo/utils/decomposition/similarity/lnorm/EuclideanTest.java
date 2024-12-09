package emo.utils.decomposition.similarity.lnorm;

import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.ISimilarity;
import org.junit.jupiter.api.Test;
import space.scalarfunction.LNorm;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several for the {@link Euclidean} class.
 *
 * @author MTomczyk
 */
class EuclideanTest
{
    /**
     * Test 1
     */
    @Test
    void calculateSimilarity()
    {
        ISimilarity S = new Euclidean();

        for (int i = 0; i < 101; i++)
        {
            double [] wA = {(double) i / 100.0d, 1.0d - (double) i / 100.0d};
            double [] wB = {1.0d - (double) i / 100.0d, (double) i / 100.0d};
            LNorm lnA = new LNorm(wA, 2.0d, null);
            LNorm lnB = new LNorm(wB, 2.0d, null);
            IGoal GA = new emo.utils.decomposition.goal.definitions.LNorm(lnA);
            IGoal GB = new emo.utils.decomposition.goal.definitions.LNorm(lnB);

            double [] dw = new double[]{wA[0] - wB[0], wA[1] - wB[1]};
            double expected = Math.sqrt(dw[0] * dw[0] + dw[1] * dw[1]);

            assertEquals(expected, S.calculateSimilarity(GA, GB));
        }
    }
}