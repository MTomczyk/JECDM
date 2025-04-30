package model.similarity.lnorm;

import model.internals.value.scalarizing.LNorm;
import model.similarity.ISimilarity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Provides various tests for {@link Cos}.
 *
 * @author MTomczyk
 */
class CosTest
{
    /**
     * Test 1.
     */
    @Test
    void calculateSimilarity()
    {
        ISimilarity<LNorm> similarity = new Cos();
        assertFalse(similarity.isLessMeaningCloser());
        {
            LNorm lnorm1 = new LNorm(new double[]{0.0d, 1.0d}, Double.POSITIVE_INFINITY);
            LNorm lnorm2 = new LNorm(new double[]{1.0d, 0.0d}, Double.POSITIVE_INFINITY);
            assertEquals(0.0d, similarity.calculateSimilarity(lnorm1, lnorm2), 1.0E-6);
        }
        {
            LNorm lnorm1 = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
            LNorm lnorm2 = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);
            assertEquals(1.0d, similarity.calculateSimilarity(lnorm1, lnorm2), 1.0E-6);
        }
    }
}