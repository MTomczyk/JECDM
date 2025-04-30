package statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link StandardDeviationIgnoreNegatives}.
 *
 * @author MTomczyk
 */
class StandardDeviationIgnoreNegativesTest
{
    /**
     * Tests {@link StandardDeviationIgnoreNegatives#calculate(double[])}
     */
    @Test
    void calculate()
    {
        StandardDeviationIgnoreNegatives mIN = new StandardDeviationIgnoreNegatives();
        assertEquals(0.0d, mIN.calculate(new double[0]));
        assertEquals(0.0d, mIN.calculate(new double[1]));
        assertEquals(0.0d, mIN.calculate(new double[]{-2.0d, -3.0d}));
        assertEquals(0.0d, mIN.calculate(new double[]{3.0d, -3.0d}));
        assertEquals(0.5d, mIN.calculate(new double[]{3.0d, 2.0d}), 1.0E-6);
    }
}