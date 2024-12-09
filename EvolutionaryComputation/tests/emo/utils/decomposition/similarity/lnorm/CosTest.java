package emo.utils.decomposition.similarity.lnorm;

import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.ISimilarity;
import org.junit.jupiter.api.Test;
import space.Vector;
import space.scalarfunction.LNorm;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for the {@link Cos} class.
 *
 * @author MTomczyk
 */

class CosTest
{
    /**
     * Test 1
     */
    @Test
    void calculateSimilarity()
    {
        ISimilarity S = new Cos();

        for (int i = 0; i < 101; i++)
        {
            double[] wA = {(double) i / 100.0d, 1.0d - (double) i / 100.0d};
            double[] wB = {1.0d - (double) i / 100.0d, (double) i / 100.0d};
            LNorm lnA = new LNorm(wA, 2.0d, null);
            LNorm lnB = new LNorm(wB, 2.0d, null);
            IGoal GA = new emo.utils.decomposition.goal.definitions.LNorm(lnA);
            IGoal GB = new emo.utils.decomposition.goal.definitions.LNorm(lnB);
            assertEquals(Vector.getCosineSimilarity(wA, wB), S.calculateSimilarity(GA, GB));
        }
    }
}