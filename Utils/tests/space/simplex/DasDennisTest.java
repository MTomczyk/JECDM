package space.simplex;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}