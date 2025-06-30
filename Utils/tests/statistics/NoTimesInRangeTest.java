package statistics;

import org.junit.jupiter.api.Test;
import space.Range;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link NoTimesInRange}.
 *
 * @author MTomczyk
 */
class NoTimesInRangeTest
{
    /**
     * Tests {@link NoTimesInRange#calculate(double[])}.
     */
    @Test
    void calculate()
    {
        NoTimesInRange NTIR = new NoTimesInRange(new Range(1.0d, 2.0d));
        assertEquals(1.0d, NTIR.calculate(new double[]{-1.0E-5, 3.0d, 1.0d}), 1.0E-5);
        assertEquals(0.0d, NTIR.calculate(new double[]{-1.0E-5, 4.0d, -1.0d}), 1.0E-5);
        assertEquals(2.0d, NTIR.calculate(new double[]{10.0d, 1.0d, 2.0d}), 1.0E-5);
    }
}