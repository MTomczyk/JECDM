package model.similarity.lnorm;

import model.internals.value.scalarizing.LNorm;
import model.similarity.ISimilarity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provides various tests for {@link Euclidean}.
 *
 * @author MTomczyk
 */
class EuclideanTest
{
    /**
     * Test 1.
     */
    @Test
    void calculateSimilarity1()
    {
        ISimilarity<LNorm> similarity = new Euclidean();
        assertTrue(similarity.isLessMeaningCloser());
        {
            LNorm lnorm1 = new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY);
            LNorm lnorm2 = new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY);
            assertEquals(Math.sqrt(2.0d), similarity.calculateSimilarity(lnorm1, lnorm2), 1.0E-6);
        }
        {
            LNorm lnorm1 = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
            LNorm lnorm2 = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
            assertEquals(0.0d, similarity.calculateSimilarity(lnorm1, lnorm2), 1.0E-6);
        }
    }

}