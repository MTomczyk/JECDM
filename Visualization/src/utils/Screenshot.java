package utils;

import color.Color;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

/**
 * Simple wrapper for a screenshot (buffered image).
 *
 * @author MTomczyk
 */
public class Screenshot
{
    /**
     * Screenshot.
     */
    public BufferedImage _image;

    /**
     * If true, alpha channel is added (supports transparency).
     */
    public final boolean _useAlphaChannel;

    /**
     * Barrier used for synchronization. Its .countDown() method must be called. Otherwise, the plot processing will
     * be frozen.
     */
    public final CountDownLatch _barrier;

    /**
     * Default constructor (uses no alpha channel).
     */
    public Screenshot()
    {
        this(false);
    }

    /**
     * Default constructor.
     *
     * @param useAlphaChannel if true, alpha channel is added (supports transparency)
     */
    public Screenshot(boolean useAlphaChannel)
    {
        _useAlphaChannel = useAlphaChannel;
        _barrier = new CountDownLatch(1);
    }

    /**
     * This method clips the created screenshot so that its depicted object occupies all
     * image; it is done by removing the first/last columns/rows whose all pixels match the given color (RGB channels
     * are compared and optionally A, if the image supports it) The method terminates immediately if
     * {@link Screenshot#_image} is null. This also will be triggered if the image is not in the TYPE_INT_RGB or
     * TYPE_INT_ARGB format. Clipping is not performed if the image is empty according to the input pattern color.
     *
     * @param color color for comparison
     * @return the method returns the clipped image (null, if the premature termination is triggered)
     */
    @SuppressWarnings("DuplicatedCode")
    public BufferedImage clipToFit(Color color)
    {
        if (_image == null) return null;
        if (_image.getWidth() < 1) return null;
        if (_image.getHeight() < 1) return null;
        if (color == null) return null;

        int type = _image.getType();
        if ((type != BufferedImage.TYPE_INT_RGB) && (type != BufferedImage.TYPE_INT_ARGB)) return null;

        int l = 0;
        int r = _image.getWidth() - 1;
        int t = 0;
        int b = _image.getHeight() - 1;

        boolean[] ts = new boolean[4];

        if ((r > 0) && (b > 0))
        {
            // left bound
            for (l = 0; l <= r; l++)
            {
                boolean passed = true;
                for (int row = 0; row < _image.getHeight(); row++)
                {
                    if (!compareColors(color, Utils.getColor(_image.getRGB(l, row)), type == BufferedImage.TYPE_INT_ARGB))
                    {
                        passed = false;
                        break;
                    }
                }
                if (!passed) break;
                if (l == r) ts[0] = true;
            }

            // right bound
            for (r = _image.getWidth() - 1; r >= l; r--)
            {
                boolean passed = true;
                for (int row = 0; row < _image.getHeight(); row++)
                {
                    if (!compareColors(color, Utils.getColor(_image.getRGB(r, row)), type == BufferedImage.TYPE_INT_ARGB))
                    {
                        passed = false;
                        break;
                    }
                }
                if (!passed) break;
                if (r == l) ts[1] = true;
            }
            // top bound
            for (t = 0; t <= b; t++)
            {
                boolean passed = true;
                for (int col = 0; col < _image.getWidth(); col++)
                {
                    if (!compareColors(color, Utils.getColor(_image.getRGB(col, t)), type == BufferedImage.TYPE_INT_ARGB))
                    {
                        passed = false;
                        break;
                    }
                }
                if (!passed) break;
                if (t == b) ts[2] = true;
            }
            // bottom bound
            for (b = _image.getHeight() - 1; b >= t; b--)
            {
                boolean passed = true;
                for (int col = 0; col < _image.getWidth(); col++)
                {
                    if (!compareColors(color, Utils.getColor(_image.getRGB(col, b)), type == BufferedImage.TYPE_INT_ARGB))
                    {
                        passed = false;
                        break;
                    }
                }
                if (!passed) break;
                if (b == t) ts[3] = true;
            }
        }

        boolean s = ts[0] && ts[1] && ts[2] && ts[3];
        if (s) return null;

        _image = _image.getSubimage(l, t, (r - l + 1), (b - t + 1));
        return _image;
    }


    /**
     * Compares the pattern color with the image color
     *
     * @param pattern       pattern color
     * @param imageColor    image color
     * @param considerAlpha if true, alpha channel is considered
     * @return true, if both colors are equal, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean compareColors(Color pattern, Color imageColor, boolean considerAlpha)
    {
        if (considerAlpha)
            return ((pattern.getRed() == imageColor.getRed()) && (pattern.getBlue() == imageColor.getBlue())
                    && (pattern.getGreen() == imageColor.getGreen()) && (pattern.getAlpha() == imageColor.getAlpha()));
        else
            return ((pattern.getRed() == imageColor.getRed()) && (pattern.getBlue() == imageColor.getBlue())
                    && (pattern.getGreen() == imageColor.getGreen()));
    }
}
