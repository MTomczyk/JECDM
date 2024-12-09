package reproduction.valuecheck;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several for {@link reproduction.valuecheck.Absorb} class.
 *
 * @author MTomczyk
 */
class AbsorbTest
{
    /**
     * Test 1.
     */
    @Test
    void checkAndCorrect1()
    {
        IValueCheck VC = new Absorb();
        assertEquals(0.0d, VC.checkAndCorrect(-1.0d, 0.0d, 1.0d));
        assertEquals(1.0d, VC.checkAndCorrect(2.0d, 0.0d, 1.0d));
        assertEquals(1.0d, VC.checkAndCorrect(-1.0d, 1.0d, 3.0d));
        assertEquals(4.0d, VC.checkAndCorrect(5.0d, 1.0d, 4.0d));
    }

    /**
     * Test 2.
     */
    @Test
    void checkAndCorrect2()
    {
        IValueCheck VC = new Absorb();
        assertEquals(0, VC.checkAndCorrect(-1, 0, 1));
        assertEquals(1, VC.checkAndCorrect(2, 0, 1));
        assertEquals(1, VC.checkAndCorrect(-1, 1, 3));
        assertEquals(4, VC.checkAndCorrect(5, 1, 4));
    }
}