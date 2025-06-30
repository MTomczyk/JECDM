package statistics;

import org.junit.jupiter.api.Test;
import space.Range;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link StandardDeviationInRange}.
 *
 * @author MTomczyk
 */
class StandardDeviationInRangeTest
{
    /**
     * Tests {@link StandardDeviationInRange#calculate(double[])}
     */
    @Test
    void calculate()
    {
        {
            StandardDeviationInRange mIN = new StandardDeviationInRange(new Range(-6.0d, 6.0d));
            assertEquals(0.0d, mIN.calculate(new double[0]));
            assertEquals(0.0d, mIN.calculate(new double[1]));
            assertEquals(0.5d, mIN.calculate(new double[]{-2.0d, -3.0d}));
            assertEquals(3.0d, mIN.calculate(new double[]{3.0d, -3.0d}));
            assertEquals(0.5d, mIN.calculate(new double[]{3.0d, 2.0d}), 1.0E-6);
        }
        {
            StandardDeviationInRange mIN = new StandardDeviationInRange(new Range(0.0d, 6.0d));
            assertEquals(0.0d, mIN.calculate(new double[0]));
            assertEquals(0.0d, mIN.calculate(new double[1]));
            assertEquals(0.0d, mIN.calculate(new double[]{-2.0d, -3.0d}));
            assertEquals(0.0d, mIN.calculate(new double[]{3.0d, -3.0d}));
            assertEquals(0.5d, mIN.calculate(new double[]{3.0d, 2.0d}), 1.0E-6);
        }
    }
}