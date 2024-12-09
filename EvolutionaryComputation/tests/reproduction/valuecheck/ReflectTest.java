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
    }
}