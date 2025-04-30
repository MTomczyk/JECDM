package utils;

import color.Color;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Screenshot} class.
 *
 * @author MTomczyk
 */
class ScreenshotTest
{
    /**
     * Tests {@link Screenshot#clipToFit(Color)} method.
     */
    @Test
    void clipToFit()
    {
        {
            BufferedImage bufferedImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
            Screenshot screenshot = new Screenshot(false);
            screenshot._image = bufferedImage;
            assertNull(screenshot.clipToFit(null));
        }
        {
            BufferedImage bufferedImage = new BufferedImage(5, 5, BufferedImage.TYPE_BYTE_GRAY);
            Screenshot screenshot = new Screenshot(false);
            screenshot._image = bufferedImage;
            assertNull(screenshot.clipToFit(null));
        }
        {
            BufferedImage bufferedImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
            Screenshot screenshot = new Screenshot(false);
            screenshot._image = bufferedImage;

            bufferedImage.setRGB(2, 2, Utils.getColor(new Color(255, 0, 0, 0)));
            BufferedImage image = screenshot.clipToFit(new Color(0, 0, 0));
            assertNotNull(image);
            assertEquals(screenshot._image, image);
            assertEquals(1, screenshot._image.getWidth());
            assertEquals(1, screenshot._image.getHeight());
            assertEquals(screenshot._image.getType(), BufferedImage.TYPE_INT_RGB);
            // alpha is added when converting
            int color = screenshot._image.getRGB(0, 0);
            assertEquals(color, Utils.getColor(new Color(255, 0, 0, 255)));
        }

       {
            BufferedImage bufferedImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
            Screenshot screenshot = new Screenshot(false);
            screenshot._image = bufferedImage;

            bufferedImage.setRGB(2, 2, Utils.getColor(new Color(255, 0, 0, 0)));
            BufferedImage image = screenshot.clipToFit(new Color(0, 0, 0, 0));
            assertNotNull(image);
            assertEquals(screenshot._image, image);
            assertEquals(1, screenshot._image.getWidth());
            assertEquals(1, screenshot._image.getHeight());
            assertEquals(screenshot._image.getType(), BufferedImage.TYPE_INT_ARGB);
            // alpha is added
            int color = screenshot._image.getRGB(0, 0);
            assertEquals(color, Utils.getColor(new Color(255, 0, 0, 0)));
        }
    }
}