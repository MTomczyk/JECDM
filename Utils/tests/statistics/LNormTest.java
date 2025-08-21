package statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link LNorm}.
 *
 * @author MTomczyk
 */
class LNormTest
{
    /**
     * Tests {@link LNorm#calculate(double[])}.
     */
    @Test
    void calculateAlpha1()
    {
        LNorm lNorm1 = new LNorm(0);
        LNorm lNorm2 = new LNorm(0.99d);
        LNorm lNorm3 = new LNorm(0.99d);

        assertEquals(0.0d, lNorm1.calculate(new double[0]));
        assertEquals(0.0d, lNorm2.calculate(new double[0]));
        assertEquals(0.0d, lNorm3.calculate(new double[0]));

        assertEquals(2.0d, lNorm1.calculate(new double[]{2.0d}));
        assertEquals(2.0d, lNorm2.calculate(new double[]{2.0d}));
        assertEquals(2.0d, lNorm3.calculate(new double[]{2.0d}));

        assertEquals(3.0d, lNorm1.calculate(new double[]{2.0d, 1.0d}));
        assertEquals(3.0d, lNorm2.calculate(new double[]{2.0d, 1.0d}));
        assertEquals(3.0d, lNorm3.calculate(new double[]{2.0d, 1.0d}));
    }

    /**
     * Tests {@link LNorm#calculate(double[])}.
     */
    @Test
    void calculateAlpha2()
    {
        LNorm lNorm = new LNorm(2.0d);
        assertEquals(0.0d, lNorm.calculate(new double[0]));
        assertEquals(2.0d, lNorm.calculate(new double[]{2.0d}), 1.0E-6);
        assertEquals(Math.sqrt(Math.pow(2.0d, 2.0d) + Math.pow(3.0d, 2.0d)),
                lNorm.calculate(new double[]{2.0d, 3.0d}), 1.0E-6);
    }

    /**
     * Tests {@link LNorm#calculate(double[])}.
     */
    @Test
    void calculateAlphaInfty()
    {
        LNorm lNorm = new LNorm(Double.POSITIVE_INFINITY);
        assertEquals(0.0d, lNorm.calculate(new double[0]));
        assertEquals(2.0d, lNorm.calculate(new double[]{2.0d}), 1.0E-6);
        assertEquals(10.0d, lNorm.calculate(new double[]{2.0d, -5.0d, 10.0d, 3.0d}), 1.0E-6);
    }
}