package statistics;

import org.junit.jupiter.api.Test;
import space.Range;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link MinInRange}.
 *
 * @author MTomczyk
 */
class MinInRangeTest
{
    /**
     * Tests {@link MinInRange#calculate(double[])}
     */
    @Test
    void calculate()
    {
        MinInRange mIN = new MinInRange(new Range(1.0d, 2.0d));
        assertEquals(0.0d, mIN.calculate(new double[0]));
        assertEquals(0.0d, mIN.calculate(new double[1]));
        assertEquals(0.0d, mIN.calculate(new double[]{-2.0d, -3.0d}));
        assertEquals(1.0d, mIN.calculate(new double[]{3.0d, 1.0d, -3.0d}));
        assertEquals(1.0d, mIN.calculate(new double[]{3.0d, 1.0d, 2.0d}), 1.0E-6);
    }
}