package combinatorics;

import org.junit.jupiter.api.Test;
import print.PrintUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for the {@link Possibilities} class.
 *
 * @author MTomczyk
 */
class PossibilitiesTest
{
    /**
     * Compares the expected matrix with the outcome.
     *
     * @param exp expected matrix
     * @param out outcome
     */
    private static void compare(int[][] exp, int[][] out)
    {
        assertEquals(exp.length, out.length);
        for (int i = 0; i < exp.length; i++)
        {
            assertEquals(exp[i].length, out[i].length);
            for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], out[i][j]);
        }
    }

    /**
     * Test 1.
     */
    @Test
    void generateCartesianProduct1()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{3, 1, 2});
            PrintUtils.print2dIntegers(cp);
            int[][] exp = new int[][]{{0, 0, 0}, {1, 0, 0}, {2, 0, 0}, {0, 0, 1}, {1, 0, 1}, {2, 0, 1}};
            compare(exp, cp);
        }
    }

    /**
     * Test 2.
     */
    @Test
    void generateCartesianProduct2()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{3, 1, 2}, true);
            PrintUtils.print2dIntegers(cp);
            int[][] exp = new int[][]{{0, 0, 0}, {0, 0, 1}, {1, 0, 0}, {1, 0, 1}, {2, 0, 0}, {2, 0, 1}};
            compare(exp, cp);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void generateCartesianProduct3()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{3});
            PrintUtils.print2dIntegers(cp);
            int[][] exp = new int[][]{{0}, {1}, {2}};
            compare(exp, cp);
        }
    }

    /**
     * Test 4.
     */
    @Test
    void generateCartesianProduct4()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{3}, true);
            PrintUtils.print2dIntegers(cp);
            int[][] exp = new int[][]{{0}, {1}, {2}};
            compare(exp, cp);
        }
    }

    /**
     * Test 5.
     */
    @Test
    void generateCartesianProduct5()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{1, 1, 1, 1, 1});
            PrintUtils.print2dIntegers(cp);
            int[][] exp = new int[][]{{0, 0, 0, 0, 0}};
            compare(exp, cp);
        }
    }

    /**
     * Test 6.
     */
    @Test
    void generateCartesianProduct6()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{1, 1, 1, 1, 1}, true);
            PrintUtils.print2dIntegers(cp);
            int[][] exp = new int[][]{{0, 0, 0, 0, 0}};
            compare(exp, cp);
        }
    }

    /**
     * Test 7.
     */
    @Test
    void generateCartesianProduct7()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{3, 0, 3});
            PrintUtils.print2dIntegers(cp);
            assertEquals(0, cp.length);
        }
    }

    /**
     * Test 8.
     */
    @Test
    void generateCartesianProduct8()
    {
        {
            int[][] cp = Possibilities.generateCartesianProduct(new int[]{3, 0, 3}, true);
            PrintUtils.print2dIntegers(cp);
            assertEquals(0, cp.length);
        }
    }
}