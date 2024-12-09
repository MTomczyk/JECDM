package space.normalization.minmax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various test for {@link Logarithmic}.
 *
 * @author MTomczyk
 */
class LogarithmicTest
{
    /**
     * Test 1
     */
    @Test
    void test1()
    {
        double [] v = new double[]{1.0d, 10.0d, 100.0d, 1000.0d, 10000.0d, 100000.0d};
        double [] e = new double[]{0.0d, 0.2d, 0.4d, 0.6d, 0.8d, 1.0d};
        Logarithmic LS = new Logarithmic(1.0d, 100000.0d, 10.0d);
        for (int i = 0; i < v.length; i++)
        {
            assertEquals(e[i], LS.getNormalized(v[i]), 0.0001d);
            assertEquals(v[i], LS.getUnnormalized(LS.getNormalized(v[i])), 0.0001d);
        }

        Logarithmic LS2 = (Logarithmic) LS.getClone();
        for (int i = 0; i < v.length; i++)
        {
            assertEquals(e[i], LS2.getNormalized(v[i]), 0.0001d);
            assertEquals(v[i], LS2.getUnnormalized(LS.getNormalized(v[i])), 0.0001d);
        }
    }
}