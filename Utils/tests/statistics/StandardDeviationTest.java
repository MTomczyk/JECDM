package statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link StandardDeviation}
 *
 * @author MTomczyk
 */
class StandardDeviationTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        IStatistic var = new StandardDeviation(true);
        assertEquals(0.0d, var.calculate(null));
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        IStatistic var = new StandardDeviation(false);
        assertEquals(0.0d, var.calculate(null));
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        IStatistic var = new StandardDeviation(true);
        assertEquals(0.0d, var.calculate(new double[0]));
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        IStatistic var = new StandardDeviation(false);
        assertEquals(0.0d, var.calculate(new double[0]));
    }

    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        IStatistic var = new StandardDeviation(true);
        assertEquals(0.0d, var.calculate(new double[]{2.0d}));
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        IStatistic var = new StandardDeviation(false);
        assertEquals(0.0d, var.calculate(new double[]{2.0d}));
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        double[] data = new double[]
                {
                        0.690107874,
                        0.432681465,
                        0.370629029,
                        0.300377258,
                        0.596447557,
                        0.116681157,
                        0.55045846,
                        0.051289614,
                        0.273405706,
                        0.38966863
                };

        IStatistic var = new StandardDeviation(true);
        assertEquals(0.202769379217, var.calculate(data), 0.0000001d);
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        double[] data = new double[]
                {
                        0.690107874,
                        0.432681465,
                        0.370629029,
                        0.300377258,
                        0.596447557,
                        0.116681157,
                        0.55045846,
                        0.051289614,
                        0.273405706,
                        0.38966863
                };

        IStatistic var = new StandardDeviation(false);
        assertEquals(0.192363923420, var.calculate(data), 0.0000001d);
    }
}