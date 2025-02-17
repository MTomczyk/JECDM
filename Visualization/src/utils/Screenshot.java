package utils;

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
     * Barrier used for synchronization.
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
}
