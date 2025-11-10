package search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link BiSection}.
 *
 * @author MTomczyk
 */
class BiSectionTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        double x = BiSection.run(1.0d, 3.0d,
                x1 -> -1.5d + x1,
                (iteration, evaluation) -> {
                    if (iteration > 1000000) return true;
                    return Double.compare(Math.abs(evaluation), 1.0E-6) < 0;
                }, new BiSection.IMidRule()
                {
                });
        System.out.println(x);
        assertEquals(1.5d, x, 1.0E-6);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        double x = BiSection.run(3.0d, 12.0d,
                x1 -> 3.0d - 0.5d * x1,
                (iteration, evaluation) -> {
                    if (iteration > 1000000) return true;
                    return Double.compare(Math.abs(evaluation), 1.0E-7) < 0;
                }, new BiSection.IMidRule()
                {
                });
        System.out.println(x);
        assertEquals(6.0d, x, 1.0E-6);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        double x = BiSection.run(3.01d, 12.0d,
                x1 -> x1 * x1 - 6.0d * x1 + 8.0d,
                (iteration, evaluation) -> {
                    if (iteration > 1000000) return true;
                    return Double.compare(Math.abs(evaluation), 1.0E-7) < 0;
                }, new BiSection.IMidRule()
                {
                });
        System.out.println(x);
        assertEquals(4.0d, x, 1.0E-6);
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        double x = BiSection.run(-5.0d, 2.4d,
                x1 -> x1 * x1 - 6.0d * x1 + 8.0d,
                (iteration, evaluation) -> {
                    if (iteration > 1000000) return true;
                    return Double.compare(Math.abs(evaluation), 1.0E-7) < 0;
                }, new BiSection.IMidRule()
                {
                });
        System.out.println(x);
        assertEquals(2.0d, x, 1.0E-6);
    }
}