package combinatorics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link Utils}.
 *
 * @author MTomczyk
 */
class UtilsTest
{
    /**
     * Tests {@link Utils#calculateBinomialCoefficient(int, int)}.
     */
    @Test
    void testCalculateBinomialCoefficient()
    {
        assertEquals(0, Utils.calculateBinomialCoefficient(-1, -1));
        assertEquals(0, Utils.calculateBinomialCoefficient(2, -1));
        assertEquals(0, Utils.calculateBinomialCoefficient(-1, 2));
        assertEquals(0, Utils.calculateBinomialCoefficient(2, 3));
        assertEquals(1, Utils.calculateBinomialCoefficient(1, 1));
        assertEquals(1, Utils.calculateBinomialCoefficient(2, 2));

        int[][] exp = new int[][]
                {
                        {1, 0, 0, 0, 0},
                        {1, 1, 0, 0, 0},
                        {1, 2, 1, 0, 0},
                        {1, 3, 3, 1, 0},
                        {1, 4, 6, 4, 1}
                };
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
            {
                System.out.println(i + " " + j);
                assertEquals(exp[i][j], Utils.calculateBinomialCoefficient(i, j));
            }
    }
}