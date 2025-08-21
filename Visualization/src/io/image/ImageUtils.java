package io.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides various auxiliary functionalities.
 *
 * @author MTomczyk
 */
public class ImageUtils
{
    /**
     * This method merges two input buffered images. It is required that their heights and types match (must be either
     * TYPE_INT_RGB or TYPE_INT_ARGB). If not, the method returns null.
     *
     * @param L image to be placed left
     * @param R image to be placed right
     * @return two images combined horizontally into one
     */
    public static BufferedImage mergeHorizontally(BufferedImage L, BufferedImage R)
    {
        if (L == null) return null;
        if (L.getWidth() < 1) return null;
        if (L.getHeight() < 1) return null;

        if (R == null) return null;
        if (R.getWidth() < 1) return null;
        if (R.getHeight() < 1) return null;

        if (R.getHeight() != L.getHeight()) return null;

        int type = L.getType();
        if (type != R.getType()) return null;
        if ((type != BufferedImage.TYPE_INT_RGB) && (type != BufferedImage.TYPE_INT_ARGB)) return null;

        BufferedImage image = new BufferedImage(L.getWidth() + R.getWidth(), L.getHeight(), R.getType());
        Graphics2D g = image.createGraphics();
        g.drawImage(L, 0, 0, null);
        g.drawImage(R, L.getWidth(), 0, null);
        g.dispose();
        return image;
    }
}
