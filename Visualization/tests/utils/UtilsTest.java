package utils;

import color.Color;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Utils} class.
 *
 * @author MTomczyk
 */
class UtilsTest
{
    /**
     * Tests {@link Utils#getColor(int)} and {@link Utils#getColor(Color)} methods.
     */
    @Test
    void getColorTest()
    {
        IRandom R = new MersenneTwister64(0);
        for (int t = 0; t < 1000; t++)
        {
            Color color = new Color(R.nextInt(255), R.nextInt(255), R.nextInt(255), R.nextInt(255));
            int c = Utils.getColor(color);
            Color color1 = Utils.getColor(c);
            assertEquals(color1, color);
        }

        for (int t = 0; t < 1000; t++)
        {
            int c = R.nextInt(Integer.MAX_VALUE);
            Color color = Utils.getColor(c);
            int c1 = Utils.getColor(color);
            assertEquals(c, c1);
        }
    }
}