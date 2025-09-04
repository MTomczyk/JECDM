package space.normalization;

import org.junit.jupiter.api.Test;
import space.normalization.minmax.Linear;
import space.normalization.minmax.LinearWithFlip;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link INormalization}.
 *
 * @author MTomczyk
 */
class INormalizationTest
{
    /**
     * Tests the cloning methods.
     */
    @Test
    void getClone()
    {
        {
            INormalization normalization1 = new LinearWithFlip(1.0d, 2.0d, 3.0d);
            INormalization normalization2 = normalization1.getClone();
            assertInstanceOf(LinearWithFlip.class, normalization2);
            assertNotEquals(normalization1, normalization2);
            LinearWithFlip lwf = (LinearWithFlip) normalization2;
            assertEquals(1.0d, lwf.getMin(), 1.0E-6);
            assertEquals(2.0d, lwf.getMax(), 1.0E-6);
            assertEquals(3.0d, lwf.getFlipThreshold(), 1.0E-6);
        }
        {
            INormalization[] normalizations1 =
                    new INormalization[]{new Linear(1.0d, 2.0d),
                            new LinearWithFlip(3.0d, 4.0d, 5.0d)};

            INormalization[] normalizations2 = INormalization.getCloned(normalizations1);
            assertNotNull(normalizations2);
            assertEquals(2, normalizations2.length);

            assertInstanceOf(Linear.class, normalizations2[0]);
            assertNotEquals(normalizations1[0], normalizations2[0]);
            Linear l = (Linear) normalizations2[0];
            assertEquals(1.0d, l.getMin(), 1.0E-6);
            assertEquals(2.0d, l.getMax(), 1.0E-6);

            assertInstanceOf(LinearWithFlip.class, normalizations2[1]);
            assertNotEquals(normalizations1[1], normalizations2[1]);
            LinearWithFlip lwf = (LinearWithFlip) normalizations2[1];
            assertEquals(3.0d, lwf.getMin(), 1.0E-6);
            assertEquals(4.0d, lwf.getMax(), 1.0E-6);
            assertEquals(5.0d, lwf.getFlipThreshold(), 1.0E-6);
        }
    }

}