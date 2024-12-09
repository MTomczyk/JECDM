package component.drawingarea.renderingdata;


import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Layer to be rendered over drawing area.
 * It wraps the buffered image class.
 *
 * @author MTomczyk
 */
public class Layer
{
    /**
     * Image data to be rendered over the drawing area.
     */
    private final BufferedImage _image;

    /**
     * Established graphics context.
     */
    private final Graphics _g;

    /**
     * Parameterized constructor
     *
     * @param image image data
     * @param g     established graphics context
     */
    public Layer(BufferedImage image, Graphics g)
    {
        _image = image;
        _g = g;
    }


    /**
     * Getter for the render.
     *
     * @return image data
     */
    public BufferedImage getImage()
    {
        return _image;
    }

    /**
     * Returns the graphics context established with the image.
     *
     * @return graphics context
     */
    public Graphics getGraphics()
    {
        return _g;
    }
}
