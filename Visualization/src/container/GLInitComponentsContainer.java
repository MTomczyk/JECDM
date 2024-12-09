package container;

import component.drawingarea.DrawingArea3D;
import gl.GLInit;

import java.util.LinkedList;

/**
 * Simple container for components implementing {@link gl.GLInit}.
 *
 * @author MTomczyk
 */

public class GLInitComponentsContainer
{
    /**
     * List of all components.
     */
    private LinkedList<GLInit> _components;

    /**
     * Plot drawing area.
     */
    private DrawingArea3D _drawingArea;

    /**
     * Getter for the list of all components.
     *
     * @return list of all components
     */
    public LinkedList<GLInit> getComponents()
    {
        return _components;
    }


    /**
     * Setter for components.
     *
     * @param components components
     */
    public void setComponents(LinkedList<GLInit> components)
    {
        _components = components;
    }

    /**
     * Getter for the plot drawing area.
     *
     * @return plot drawing area
     */
    public DrawingArea3D getDrawingArea()
    {
        return _drawingArea;
    }

    /**
     * Setter for plot drawing area.
     *
     * @param drawingArea plot drawing area
     */
    public void setDrawingArea(DrawingArea3D drawingArea)
    {
        _drawingArea = drawingArea;
    }
}
