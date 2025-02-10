package space.simplex;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Various tests for {@link space.simplex.DasDennis} class.
 *
 * @author MTomczyk
 */
class DasDennisTest
{
    /**
     * Test 1.
     */
    @Test
    void getWeightVectors1()
    {
        ArrayList<double[]> w = DasDennis.getWeightVectors(2, 4);

        double[][] exp = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.25d, 0.75d},
                        {0.5d, 0.5d},
                        {0.75d, 0.25d},
                        {1.0d, 0.0d},
                };

        assertEquals(exp.length, w.size());
        for (int i = 0; i < 5; i++)
        {
            assertEquals(exp[i][0], w.get(i)[0], 0.0000001d);
            assertEquals(exp[i][1], w.get(i)[1], 0.0000001d);
        }
    }


    /**
     * Test 2.
     */
    @Test
    void getWeightVectors2()
    {
        ArrayList<double[]> w = DasDennis.getWeightVectors(3, 4);

        double[][] exp = new double[][]
                {
                        {0.0d, 0.0, 1.0d},
                        {0.0d, 0.25d, 0.75d},
                        {0.0d, 0.5d, 0.5d},
                        {0.0d, 0.75d, 0.25d},
                        {0.0d, 1.0d, 0.0d},
                        {0.25d, 0.0d, 0.75d},
                        {0.25d, 0.25d, 0.5d},
                        {0.25d, 0.5d, 0.25d},
                        {0.25d, 0.75d, 0.0d},
                        {0.5d, 0.0d, 0.5d},
                        {0.5d, 0.25d, 0.25d},
                        {0.5d, 0.5d, 0.0d},
                        {0.75d, 0.0d, 0.25d},
                        {0.75d, 0.25d, 0.0d},
                        {1.0d, 0.0d, 0.0d}
                };


        assertEquals(15, w.size());

        assertEquals(exp.length, w.size());

        for (int i = 0; i < 15; i++)
        {
            assertEquals(exp[i][0], w.get(i)[0], 0.0000001d);
            assertEquals(exp[i][1], w.get(i)[1], 0.0000001d);
            assertEquals(exp[i][2], w.get(i)[2], 0.0000001d);
        }
    }

    /**
     * Tests {@link DasDennis#getNoProblems(int, int)}.
     */
    @Test
    void testNoProblems()
    {
        for (int m = 2; m < 7; m++)
        {
            for (int p = 1; p < 7; p++)
            {
                int n = DasDennis.getNoProblems(m, p);
                ArrayList<double[]> w = DasDennis.getWeightVectors(m, p);
                assertNotNull(w);
                assertEquals(n, w.size());
            }
        }
    }
}