package statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link NoTimesNonNegative}
 *
 * @author MTomczyk
 */
class NoTimesNonNegativeTest
{

    /**
     * Tests {@link NoTimesNonNegative#calculate(double[])}.
     */
    @Test
    void calculate()
    {
        NoTimesNonNegative NTNN = new NoTimesNonNegative();
        assertEquals(2.0d, NTNN.calculate(new double[]{-1.0E-5, 0.0d, 1.0d}), 1.0E-5);
        assertEquals(0.0d, NTNN.calculate(new double[]{-1.0E-5, -1.0d, -1.0d}), 1.0E-5);
        assertEquals(3.0d, NTNN.calculate(new double[]{10.0d, 0.0d, 1.0d}), 1.0E-5);
    }
}