package container;

import component.AbstractSwingComponent;
import component.axis.swing.AbstractAxis;
import component.colorbar.AbstractColorbar;
import component.drawingarea.AbstractDrawingArea;
import component.legend.AbstractLegend;
import component.margins.AbstractMargins;
import component.title.AbstractTitle;

import java.util.LinkedList;

/**
 * Simple container for various plot components.
 *
 * @author MTomczyk
 */


public class ComponentsContainer
{
    /**
     * List of all components.
     */
    private LinkedList<AbstractSwingComponent> _components;

    /**
     * Plot margins.
     */
    private AbstractMargins _margins;

    /**
     * Plot drawing area.
     */
    private AbstractDrawingArea _drawingArea;

    /**
     * Plot title component.
     */
    private AbstractTitle _title;

    /**
     * Plot axes.
     */
    private AbstractAxis[] _axes;

    /**
     * Legend.
     */
    private AbstractLegend _legend;

    /**
     * Colorbar.
     */
    private AbstractColorbar _colorbar;

    /**
     * Getter for the list of all components.
     *
     * @return list of all components
     */
    public LinkedList<AbstractSwingComponent> getComponents()
    {
        return _components;
    }

    /**
     * Getter for plot margins.
     *
     * @return plot margins
     */
    public AbstractMargins getMargins()
    {
        return _margins;
    }

    /**
     * Getter for the plot drawing area.
     *
     * @return plot drawing area
     */
    public AbstractDrawingArea getDrawingArea()
    {
        return _drawingArea;
    }

    /**
     * Getter for plot title component.
     *
     * @return plot title component
     */
    public AbstractTitle getTitle()
    {
        return _title;
    }

    /**
     * Getter for plot axes.
     *
     * @return plot axes
     */
    public AbstractAxis[] getAxes()
    {
        return _axes;
    }

    /**
     * Getter for plot legend.
     *
     * @return plot legend
     */
    public AbstractLegend getLegend()
    {
        return _legend;
    }

    /**
     * Getter for colorbar.
     *
     * @return plot colorbar
     */
    public AbstractColorbar getColorbar()
    {
        return _colorbar;
    }

    /**
     * Setter for components.
     *
     * @param components components
     */
    public void setComponents(LinkedList<AbstractSwingComponent> components)
    {
        _components = components;
    }

    /**
     * Setter for plot margins.
     *
     * @param margins plot margins
     */
    public void setMargins(AbstractMargins margins)
    {
        _margins = margins;
    }

    /**
     * Setter for plot drawing area.
     *
     * @param drawingArea plot drawing area
     */
    public void setDrawingArea(AbstractDrawingArea drawingArea)
    {
        _drawingArea = drawingArea;
    }

    /**
     * Setter for plot title.
     *
     * @param title plot title
     */
    public void setTitle(AbstractTitle title)
    {
        _title = title;
    }

    /**
     * Setter for axes.
     *
     * @param axes axes
     */
    public void setAxes(AbstractAxis[] axes)
    {
        _axes = axes;
    }

    /**
     * Setter for the legend.
     *
     * @param legend legend
     */
    public void setLegend(AbstractLegend legend)
    {
        _legend = legend;
    }

    /**
     * Setter for the colorbar.
     *
     * @param colorbar colorbar
     */
    public void setColorbar(AbstractColorbar colorbar)
    {
        _colorbar = colorbar;
    }
}
