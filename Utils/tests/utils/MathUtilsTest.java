package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link MathUtils} class.
 *
 * @author MTomczyk
 */
class MathUtilsTest
{
    /**
     * Tests {@link MathUtils#getSum(int[])} method.
     */
    @Test
    void getSum1()
    {
        assertEquals(0, MathUtils.getSum((int[]) null));
        assertEquals(0, MathUtils.getSum(new int[0]));
        assertEquals(0, MathUtils.getSum(new int[]{-1, 1}));
        assertEquals(4, MathUtils.getSum(new int[]{1, 2, 3, -2}));

        assertEquals(0, MathUtils.getSum((int[]) null, 0, 1));
        assertEquals(0, MathUtils.getSum(new int[0], 0, 0));
        assertEquals(0, MathUtils.getSum(new int[]{-1, 1}, 0, 1));
        assertEquals(4, MathUtils.getSum(new int[]{1, 2, 3, -2}, 0, 3));

        assertEquals(-1, MathUtils.getSum(new int[]{-1, 1}, 0, 0));
        assertEquals(1, MathUtils.getSum(new int[]{-1, 1}, 1, 1));
        assertEquals(4, MathUtils.getSum(new int[]{1, 2, 3, -2}, 0, 3));
        assertEquals(5, MathUtils.getSum(new int[]{1, 2, 3, -2}, 1, 2));
        assertEquals(4, MathUtils.getSum(new int[]{1, 2, 3, -2}, -5, 5));
        assertEquals(0, MathUtils.getSum(new int[]{1, 2, 3, -2}, 5, -5));

    }

    /**
     * Tests {@link MathUtils#getSum(double[])} method.
     */
    @Test
    void getSum2()
    {
        assertEquals(0, MathUtils.getSum((double[]) null));
        assertEquals(0, MathUtils.getSum(new double[0]));
        assertEquals(0.0d, MathUtils.getSum(new double[]{-1, 1}), 1.0E-6);
        assertEquals(4.0d, MathUtils.getSum(new double[]{1, 2, 3, -2}), 1.0E-6);

        assertEquals(0, MathUtils.getSum((double[]) null, 0, 0));
        assertEquals(0, MathUtils.getSum(new double[0], 0, 0));
        assertEquals(0.0d, MathUtils.getSum(new double[]{-1, 1}, 0, 1), 1.0E-6);
        assertEquals(0.0d, MathUtils.getSum(new double[]{-1, 1}, -1, 2), 1.0E-6);
        assertEquals(-1.0d, MathUtils.getSum(new double[]{-1, 1}, 0, 0), 1.0E-6);
        assertEquals(1.0d, MathUtils.getSum(new double[]{-1, 1}, 1, 1), 1.0E-6);
        assertEquals(0.0d, MathUtils.getSum(new double[]{-1, 1}, 2, 1), 1.0E-6);
        assertEquals(4.0d, MathUtils.getSum(new double[]{1, 2, 3, -2}, 0, 3), 1.0E-6);
        assertEquals(5.0d, MathUtils.getSum(new double[]{1, 2, 3, -2}, 1, 2), 1.0E-6);
        assertEquals(4.0d, MathUtils.getSum(new double[]{1, 2, 3, -2}, -5, 7), 1.0E-6);
        assertEquals(0.0d, MathUtils.getSum(new double[]{1, 2, 3, -2}, 3, 2), 1.0E-6);
    }


    /**
     * Tests {@link MathUtils#getSum(boolean[])} method.
     */
    @Test
    void getSum3()
    {
        assertEquals(0, MathUtils.getSum((boolean[]) null));
        assertEquals(0, MathUtils.getSum(new boolean[0]));
        assertEquals(0, MathUtils.getSum(new boolean[]{false, false}));
        assertEquals(2, MathUtils.getSum(new boolean[]{true, false, true, false}));

        assertEquals(0, MathUtils.getSum((boolean[]) null, 0, 1));
        assertEquals(0, MathUtils.getSum(new boolean[0], 0, 1));
        assertEquals(0, MathUtils.getSum(new boolean[]{false, false}, 0, 1));
        assertEquals(2, MathUtils.getSum(new boolean[]{true, false, true, false}, 0, 3));
        assertEquals(2, MathUtils.getSum(new boolean[]{true, false, true, false}, -5, 5));
        assertEquals(0, MathUtils.getSum(new boolean[]{true, false, true, false}, 5, 2));
        assertEquals(1, MathUtils.getSum(new boolean[]{true, false, true, false}, 0, 1));
        assertEquals(1, MathUtils.getSum(new boolean[]{true, false, true, false}, 2, 2));

    }
}