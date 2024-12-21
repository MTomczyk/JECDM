package space.distance;

import org.junit.jupiter.api.Test;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provides various tests for {@link Cos}.
 *
 * @author MTomczyk
 */
class CosTest
{
    /**
     * Test 1 (without normalizations).
     */
    @Test
    void getDistance1()
    {
        {
            Cos cos = new Cos();
            assertTrue(cos.isLessMeaningCloser());

            {
                double[] a = new double[]{1.0d, 0.0d};
                double[] b = new double[]{0.0d, 0.5d};
                assertEquals(0.0d, cos.getDistance(a, b), 0.0001d);
            }
            {
                double[] a = new double[]{0.0d, 1.0d};
                double[] b = new double[]{0.0d, 0.5d};
                assertEquals(1.0d, cos.getDistance(a, b), 0.0001d);
            }
            {
                double[] a = new double[]{1.0d, 0.0d};
                double[] b = new double[]{1.0d, 1.0d};
                assertEquals(Math.sqrt(2.0d) / 2.0d, cos.getDistance(a, b), 0.0001d);
            }

            {
                double[] w1 = new double[]{1.0d, 0.0d};
                for (int i = 0; i < 101; i++)
                {
                    double h = (float) i / 100.0d;
                    double[] w2 = new double[]{1.0d, h};
                    double l = (float) Math.sqrt(1 + h * h);
                    double expected = 1.0d / l;
                    double computed = cos.getDistance(w1, w2);
                    assertEquals(expected, computed, 0.0001d);
                }
            }
        }
    }

    /**
     * Test 2 (with normalizations).
     */
    @Test
    void getDistance2()
    {
        {
            Cos cos = new Cos(new INormalization[]{new Linear(0.0d, 2.0d), new Linear()});
            {
                double[] a = new double[]{2.0d, 0.0d};
                double[] b = new double[]{0.0d, 0.5d};
                assertEquals(0.0d, cos.getDistance(a, b), 0.0001d);
            }
            {
                double[] a = new double[]{0.0d, 1.0d};
                double[] b = new double[]{0.0d, 0.5d};
                assertEquals(1.0d, cos.getDistance(a, b), 0.0001d);
            }
            {
                double[] a = new double[]{2.0d, 0.0d};
                double[] b = new double[]{2.0d, 1.0d};
                assertEquals(Math.sqrt(2.0d) / 2.0d, cos.getDistance(a, b), 0.0001d);
            }

            {
                double[] w1 = new double[]{2.0d, 0.0d};
                for (int i = 0; i < 101; i++)
                {
                    double h = (float) i / 100.0d;
                    double[] w2 = new double[]{2.0d, h};
                    double l = (float) Math.sqrt(1 + h * h);
                    double expected = 1.0d / l;
                    double computed = cos.getDistance(w1, w2);
                    assertEquals(expected, computed, 0.0001d);
                }
            }
        }
    }
}