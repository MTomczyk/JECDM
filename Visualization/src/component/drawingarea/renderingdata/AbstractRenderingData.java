package component.drawingarea.renderingdata;

import component.drawingarea.AbstractDrawingArea;
import space.Dimension;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Auxiliary class supporting efficient rendering.
 * It wraps an offscreen buffered image that is updated only when the resulting plot is supposed to change.
 * In the case when the regular {@link AbstractDrawingArea#paintComponent(Graphics)} method is called,
 * the offscreen image is copied to the onscreen image and painted over the canvas.
 *
 * @author MTomczyk
 */
public class AbstractRenderingData
{
    /**
     * The most recent plot render.
     */
    private volatile Layer _render;

    /**
     * Reference to the renderer to be flushed during the next repaint called.
     * It corresponds to the previous rendering result. The render is supposed to be flushed on EDT to
     * avoid collisions during rendering.
     */
    private volatile Layer _renderToFlush = null;

    /**
     * Projection data used when recalculating IDS (second level).
     */
    private final space.Dimension[] _projectionBounds;

    /**
     * Offscreen layer expected dimensions.
     */
    private final space.Dimension[] _offscreenLayerExpectedDimensions;

    /**
     * Auxiliary layer expected dimensions.
     */
    private final space.Dimension[] _auxiliaryLayerExpectedDimensions;

    /**
     * Onscreen layer expected dimensions.
     */
    private final space.Dimension[] _onscreenLayerExpectedDimensions;

    /**
     * Parameterized constructor.
     *
     * @param projectionBoundsDimensionality dimensionality of the rendering space
     */
    public AbstractRenderingData(int projectionBoundsDimensionality)
    {
        _render = null;

        _projectionBounds = new Dimension[projectionBoundsDimensionality];
        for (int i = 0; i < projectionBoundsDimensionality; i++)
            _projectionBounds[i] = new Dimension(0.0d, 1.0d);

        _offscreenLayerExpectedDimensions = new Dimension[2];
        _offscreenLayerExpectedDimensions[0] = new Dimension(0.0d, 1.0d);
        _offscreenLayerExpectedDimensions[1] = new Dimension(0.0d, 1.0d);

        _auxiliaryLayerExpectedDimensions = new Dimension[2];
        _auxiliaryLayerExpectedDimensions[0] = new Dimension(0.0d, 1.0d);
        _auxiliaryLayerExpectedDimensions[1] = new Dimension(0.0d, 1.0d);

        _onscreenLayerExpectedDimensions = new Dimension[2];
        _onscreenLayerExpectedDimensions[0] = new Dimension(0.0d, 1.0d);
        _onscreenLayerExpectedDimensions[1] = new Dimension(0.0d, 1.0d);
    }


    /**
     * Instantiates a layer with an image of a given size.
     * The layer is instantiated with a transparent background and returned.
     *
     * @param width  width of the offscreen buffered image
     * @param height height of the offscreen buffered image
     * @return instantiated layer
     */
    public Layer createLayer(int width, int height)
    {
        BufferedImage im = null;
        Graphics g = null;
        if ((width > 0) && (height > 0))
        {
            im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D upG = im.createGraphics();
            upG.setComposite(AlphaComposite.Clear);
            upG.fillRect(0, 0, width, height);
            upG.setComposite(AlphaComposite.SrcOver);
            upG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g = upG;
        }

        return new Layer(im, g);
    }

    /**
     * Returns the ratio between the offscreen layer width and the onscreen layer width.
     * If the ratio goes to infinite, the method returns null.
     *
     * @return ratio
     */
    public Float getRescallingFactorX()
    {
        return getRescallingFactor(0);
    }

    /**
     * Returns the ratio between the offscreen layer height and the onscreen layer height.
     * If the ratio goes to infinite, the method returns null.
     *
     * @return ratio
     */
    public Float getRescallingFactorY()
    {
        return getRescallingFactor(1);
    }

    /**
     * Returns the ratio between the offscreen layer and the onscreen layer specified dimension.
     * If the ratio goes to infinite, the method returns null.
     *
     * @param dimension dimension id (0 = width, 1 = height)
     * @return ratio
     */
    protected Float getRescallingFactor(int dimension)
    {
        if (_onscreenLayerExpectedDimensions[dimension]._size <= 0) return null;
        if (_offscreenLayerExpectedDimensions[dimension]._size <= 0) return null;
        return (float) (_offscreenLayerExpectedDimensions[dimension]._size / _onscreenLayerExpectedDimensions[dimension]._size);
    }


    /**
     * Getter for the current render.
     *
     * @return plot render
     */
    public Layer getRender()
    {
        return _render;
    }


    /**
     * Setter for the current plot render
     *
     * @param render plot render
     */
    public void setRender(Layer render)
    {
        _render = render;
    }


    /**
     * Setter for the projection bound data used when recalculating IDS (second level).
     *
     * @param position new position
     * @param size     new size
     * @param id       projection bound in (in-array index)
     */
    public void setProjectionBound(float position, float size, int id)
    {
        setDimension(_projectionBounds, position, size, id);
    }

    /**
     * Setter for the onscreen layer expected dimensions.
     *
     * @param position new position
     * @param size     new size
     * @param id       projection bound in (in-array index)
     */
    public void setOnscreenLayerExpectedDimensions(float position, float size, int id)
    {
        setDimension(_onscreenLayerExpectedDimensions, position, size, id);
    }

    /**
     * Setter for the offscreen layer expected dimensions.
     *
     * @param position new position
     * @param size     new size
     * @param id       projection bound in (in-array index)
     */
    public void setOffscreenLayerExpectedDimensions(float position, float size, int id)
    {
        setDimension(_offscreenLayerExpectedDimensions, position, size, id);
    }

    /**
     * Setter for the auxiliary layer expected dimensions.
     *
     * @param position new position
     * @param size     new size
     * @param id       projection bound in (in-array index)
     */
    public void setAuxiliaryLayerExpectedDimensions(float position, float size, int id)
    {
        setDimension(_auxiliaryLayerExpectedDimensions, position, size, id);
    }

    /**
     * Setter for the dimension.
     *
     * @param target   target dimensions array
     * @param position new position
     * @param size     new size
     * @param id       projection bound in (in-array index)
     */
    protected void setDimension(space.Dimension[] target, float position, float size, int id)
    {
        if (target.length <= id) return;
        target[id]._position = position;
        target[id]._size = size;
    }


    /**
     * Returns a deep copy of projection bounds (can be used in asynchronous processing).
     *
     * @return projection bounds copy
     */
    public space.Dimension[] getCopyOfProjectionBounds()
    {
        return getCopyOfDimensions(_projectionBounds);
    }

    /**
     * Returns a deep copy of the onscreen layer expected dimensions (can be used in asynchronous processing).
     *
     * @return expected onscreen layer dimensions copy
     */
    public space.Dimension[] getCopyOfOnscreenLayerExpectedDimensions()
    {
        return getCopyOfDimensions(_onscreenLayerExpectedDimensions);
    }

    /**
     * Returns a deep copy of offscreen layer expected dimensions (can be used in asynchronous processing).
     *
     * @return expected offscreen layer dimensions copy
     */
    public space.Dimension[] getCopyOfOffscreenLayerExpectedDimensions()
    {
        return getCopyOfDimensions(_offscreenLayerExpectedDimensions);
    }

    /**
     * Returns a deep copy of auxiliary layer expected dimensions (can be used in asynchronous processing).
     *
     * @return expected auxiliary layer dimensions copy
     */
    public space.Dimension[] getCopyOfAuxiliaryLayerExpectedDimensions()
    {
        return getCopyOfDimensions(_auxiliaryLayerExpectedDimensions);
    }


    /**
     * Returns a deep copy of projection bounds (can be used in asynchronous processing).
     *
     * @param source source dimensions
     * @return projection bounds copy
     */
    protected space.Dimension[] getCopyOfDimensions(space.Dimension[] source)
    {
        space.Dimension[] copy = new Dimension[source.length];
        for (int i = 0; i < source.length; i++)
            copy[i] = new Dimension(source[i]._position, source[i]._size);
        return copy;
    }


    /**
     * Disposes buffers.
     */
    public void dispose()
    {
        if ((_render != null) && (_render.getImage() != null)) _render.getImage().flush();
        if ((_renderToFlush != null) && (_renderToFlush.getImage() != null)) _renderToFlush.getImage().flush();
        _render = null;
        _renderToFlush = null;
    }

    /**
     * Reference to the renderer to be flushed during the next repaint called.
     * It corresponds to the previous rendering result. The render is supposed to be flushed on EDT to
     * avoid collisions during rendering.
     *
     * @return layer to be flushed
     */
    public Layer getRenderToFlush()
    {
        return _renderToFlush;
    }

    /**
     * Setter for the layer to be flushed.
     *
     * @param renderToFlush layer to be flushed
     */
    public void setRenderToFlush(Layer renderToFlush)
    {
        _renderToFlush = renderToFlush;
    }
}
