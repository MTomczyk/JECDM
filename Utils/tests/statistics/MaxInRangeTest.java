package statistics;

import org.junit.jupiter.api.Test;
import space.Range;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link MaxInRange}.
 *
 * @author MTomczyk
 */
class MaxInRangeTest
{
    /**
     * Tests {@link MaxInRange#calculate(double[])}
     */
    @Test
    void calculate()
    {
        MaxInRange mIN = new MaxInRange(new Range(1.0d, 2.0d));
        assertEquals(0.0d, mIN.calculate(new double[0]));
        assertEquals(0.0d, mIN.calculate(new double[1]));
        assertEquals(0.0d, mIN.calculate(new double[]{-2.0d, -3.0d}));
        assertEquals(1.0d, mIN.calculate(new double[]{3.0d, 1.0d, -3.0d}));
        assertEquals(2.0d, mIN.calculate(new double[]{3.0d, 1.0d, 2.0d}), 1.0E-6);
    }
}