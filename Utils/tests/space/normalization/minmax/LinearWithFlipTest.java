package space.normalization.minmax;

import org.junit.jupiter.api.Test;
import space.normalization.INormalization;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several of tests for {@link LinearWithFlip} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class LinearWithFlipTest
{

    /**
     * Test 1.
     */
    @Test
    void getNormalized1()
    {
        INormalization n = new LinearWithFlip(2.0d, 10.0d, 2.0d);
        double v = n.getNormalized(2.0d);
        assertEquals(2.0, v, 0.0001d);
        v = n.getNormalized(10.0d);
        assertEquals(1.0, v, 0.0001d);
        v = n.getNormalized(6.0d);
        assertEquals(1.5, v, 0.0001d);
        System.out.println(n);
    }

}