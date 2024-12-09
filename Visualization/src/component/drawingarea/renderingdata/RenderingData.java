package component.drawingarea.renderingdata;

import component.drawingarea.AbstractDrawingArea;

import java.awt.*;

/**
 * Auxiliary class supporting efficient rendering.
 * It wraps an offscreen buffered image that is updated only when the resulting plot is supposed to change.
 * In the case when the regular {@link AbstractDrawingArea#paintComponent(Graphics)} method is called,
 * the offscreen image is copied to the onscreen image and painted over the canvas.
 *
 * @author MTomczyk
 */
public class RenderingData extends AbstractRenderingData
{
    /**
     * Parameterized constructor.
     *
     * @param projectionBoundsDimensionality dimensionality of the rendering space
     */
    public RenderingData(int projectionBoundsDimensionality)
    {
        super(projectionBoundsDimensionality);
    }
}
