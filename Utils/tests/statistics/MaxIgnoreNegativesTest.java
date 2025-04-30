package statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link MaxIgnoreNegatives}.
 *
 * @author MTomczyk
 */
class MaxIgnoreNegativesTest
{
    /**
     * Tests {@link MaxIgnoreNegatives#calculate(double[])}
     */
    @Test
    void calculate()
    {
        MaxIgnoreNegatives mIN = new MaxIgnoreNegatives();
        assertEquals(0.0d, mIN.calculate(new double[0]));
        assertEquals(0.0d, mIN.calculate(new double[1]));
        assertEquals(0.0d, mIN.calculate(new double[]{-2.0d, -3.0d}));
        assertEquals(3.0d, mIN.calculate(new double[]{3.0d, -3.0d}));
        assertEquals(4.0d, mIN.calculate(new double[]{3.0d, 4.0d}));
    }
}