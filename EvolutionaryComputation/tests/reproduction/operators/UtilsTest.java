package reproduction.operators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link ReproductionUtils}
 *
 * @author MTomczyk
 */
class UtilsTest
{
    @Test
    void countTrues()
    {
        assertEquals(0, ReproductionUtils.countTrues(null));
        assertEquals(0, ReproductionUtils.countTrues(new boolean[]{}));
        assertEquals(0, ReproductionUtils.countTrues(new boolean[]{false, false}));
        assertEquals(1, ReproductionUtils.countTrues(new boolean[]{true, false}));
        assertEquals(1, ReproductionUtils.countTrues(new boolean[]{false, true}));
        assertEquals(2, ReproductionUtils.countTrues(new boolean[]{true, true}));
    }
}