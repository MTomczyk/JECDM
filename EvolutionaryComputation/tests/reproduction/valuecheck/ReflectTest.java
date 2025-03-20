package reproduction.valuecheck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for {@link reproduction.valuecheck.Reflect} class.
 *
 * @author MTomczyk
 */
class ReflectTest
{
    /**
     * Test 1.
     */
    @Test
    void checkAndCorrect1()
    {
        IValueCheck VC = new Reflect();
        assertEquals(0.5d, VC.checkAndCorrect(-0.5d, 0.0d, 1.0d));
        assertEquals(0.5d, VC.checkAndCorrect(1.5d, 0.0d, 1.0d));
        assertEquals(0.5d, VC.checkAndCorrect(0.5d, 0.0d, 1.0d));
        assertEquals(0.0d, VC.checkAndCorrect(0.0d, 0.0d, 1.0d));
        assertEquals(1.0d, VC.checkAndCorrect(1.0d, 0.0d, 1.0d));
        assertEquals(1.0d, VC.checkAndCorrect(5.0d, 0.0d, 2.0d));
        assertEquals(1.0d, VC.checkAndCorrect(-3.0d, 0.0d, 2.0d));
        assertEquals(4.0d, VC.checkAndCorrect(7.0d, 2.0d, 5.0d));
        assertEquals(2.0d, VC.checkAndCorrect(-1.0d, 2.0d, 5.0d));

        {
            double [] v = new double[]{-0.5d, 1.5d, 0.5d, 0.0d, 1.0d};
            assertEquals(v, VC.checkAndCorrect(v, 0.0d, 1.0d));
            assertEquals(0.5d, v[0]);
            assertEquals(0.5d, v[1]);
            assertEquals(0.5d, v[2]);
            assertEquals(0.0d, v[3]);
            assertEquals(1.0d, v[4]);
        }
        {
            double [] v = new double[]{5.0d, -3.0d};
            assertEquals(v, VC.checkAndCorrect(v, 0.0d, 2.0d));
            assertEquals(1.0d, v[0]);
            assertEquals(1.0d, v[1]);
        }
        {
            double [] v = new double[]{7.0d, -1.0d};
            assertEquals(v, VC.checkAndCorrect(v, 2.0d, 5.0d));
            assertEquals(4.0d, v[0]);
            assertEquals(2.0d, v[1]);
        }
    }

    /**
     * Test 2.
     */
    @Test
    void checkAndCorrect2()
    {
        IValueCheck VC = new Reflect();
        assertEquals(0, VC.checkAndCorrect(0, 0, 1));
        assertEquals(1, VC.checkAndCorrect(1, 0, 1));
        assertEquals(2, VC.checkAndCorrect(2, 1, 3));
        assertEquals(1, VC.checkAndCorrect(5, 0, 2));
        assertEquals(1, VC.checkAndCorrect(-3, 0, 2));
        assertEquals(4, VC.checkAndCorrect(7, 2, 5));
        assertEquals(2, VC.checkAndCorrect(-1, 2, 5));

        {
            int [] v = new int[]{0, 1};
            assertEquals(v, VC.checkAndCorrect(v, 0, 1));
            assertEquals(0, v[0]);
            assertEquals(1, v[1]);
        }
        {
            int [] v = new int[]{2};
            assertEquals(v, VC.checkAndCorrect(v, 1, 3));
            assertEquals(2, v[0]);
        }
        {
            int [] v = new int[]{5, -3};
            assertEquals(v, VC.checkAndCorrect(v, 0, 2));
            assertEquals(1, v[0]);
            assertEquals(1, v[1]);
        }
        {
            int [] v = new int[]{7, -1};
            assertEquals(v, VC.checkAndCorrect(v, 2, 5));
            assertEquals(4, v[0]);
            assertEquals(2, v[1]);
        }
    }
}