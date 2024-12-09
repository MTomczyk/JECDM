package component.drawingarea;

import component.drawingarea.renderingdata.Layer;
import component.grid.Grid;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import scheme.AbstractScheme;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.EventTypes;
import utils.Projection;

import java.awt.*;

/**
 * Class representing a plot drawing area for 2D visualization (AWT-based).
 *
 * @author MTomczyk
 */


public class DrawingArea2D extends AbstractDrawingArea
{
    /**
     * Params container.
     */
    public static class Params extends AbstractDrawingArea.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param PC plot container: allows accessing various plot components
         */
        public Params(PlotContainer PC)
        {
            super("Drawing Area 2D", PC);
        }
    }

    /**
     * Main grid lines (can be null -> grid lines are not drawn).
     */
    protected Grid _mainGrid;

    /**
     * Aux grid lines (can be null -> grid lines are not drawn).
     */
    protected Grid _auxGrid;


    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public DrawingArea2D(Params p)
    {
        super(p);
        if (p._drawMainGridlines) _mainGrid = Grid.getMainGrid(p._PC);
        if (p._drawAuxGridlines) _auxGrid = Grid.getAuxGrid(p._PC);
    }

    /**
     * Updates bounds of the panel.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    @Override
    public void setLocationAndSize(int x, int y, int w, int h)
    {
        super.setLocationAndSize(x, y, w, h);
        if (_mainGrid != null) _mainGrid.setLocationAndSize(x, y, w, h);
        if (_auxGrid != null) _auxGrid.setLocationAndSize(x, y, w, h);
    }

    /**
     * Updates bounds of the primary drawing area (should be enclosed within the panel bounds).
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     */
    @Override
    public void setPrimaryDrawingArea(int x, int y, int w, int h)
    {
        super.setPrimaryDrawingArea(x, y, w, h);

        calculateLayerExpectedDimensions();

        updateRelativeFields();
        if (_mainGrid != null) _mainGrid.setPrimaryDrawingArea(x, y, w, h);
        if (_auxGrid != null) _auxGrid.setPrimaryDrawingArea(x, y, w, h);
    }

    /**
     * Auxiliary method that establishes the layer expected dimensions.
     */
    @Override
    protected void calculateLayerExpectedDimensions()
    {
        _renderingData.setProjectionBound(_translationVector[0], _primaryDrawingArea.width, 0);
        _renderingData.setProjectionBound(_translationVector[1], _primaryDrawingArea.height, 1);
        _renderingData.setOffscreenLayerExpectedDimensions(0, getWidth(), 0);
        _renderingData.setOffscreenLayerExpectedDimensions(0, getHeight(), 1);
        _renderingData.setOnscreenLayerExpectedDimensions(0, getWidth(), 0);
        _renderingData.setOnscreenLayerExpectedDimensions(0, getHeight(), 1);
    }

    /**
     * This method constructs a swing worker that updates the render image that is to be painted over the drawing area.
     * Using swing worker aids in executing potentially heavy operations out of the EDT.
     *
     * @param eventType type of the even triggered the reconstruction of the render
     * @return swing worker instance
     */
    @Override
    public QueuedSwingWorker<Void, Void> createRenderUpdater(EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: create renders method called");
        DrawingAreaRenderUpdater updater = null;

        if ((eventType.equals(EventTypes.ON_RESIZE)) || (eventType.equals(EventTypes.ON_DATA_CHANGED)) ||
                (eventType.equals(EventTypes.ON_HEATMAP_DATA_CHANGED)) || (eventType.equals(EventTypes.ON_DEMAND))
                || (eventType.equals(EventTypes.ON_HEATMAP_FILTER_CHANGED)))
            updater = new DrawingAreaRenderUpdater(_PC, _measureRenderGenerationTimes, _renderGenerationTimes);

        return updater;
    }

    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        if (_renderingData.getRenderToFlush() != null)
        {
            _renderingData.getRenderToFlush().getImage().flush();
            _renderingData.setRenderToFlush(null); // clear previous render
        }

        Graphics g2 = g.create();

        if (_clipDrawingArea)
        {
            g2.clipRect(Projection.getP(_translationVector[0]),
                    Projection.getP(_translationVector[1]),
                    Projection.getP(_primaryDrawingArea.width),
                    Projection.getP(_primaryDrawingArea.height));
        }

        // super here to account for clip area
        super.paintComponent(g2);

        Layer render = _renderingData.getRender();
        if (render != null) g2.drawImage(render.getImage(), 0, 0, null);
        g2.dispose();
    }

    /**
     * Executed before drawing data sets.
     * Paints some auxiliary components.
     */
    @Override
    protected void paintAuxElements(Graphics g)
    {
        if (_auxGrid != null) _auxGrid.paintComponent(g);
        if (_mainGrid != null) _mainGrid.paintComponent(g);
        drawBorder(g);
    }

    /**
     * Can be used to set a global container.
     *
     * @param GC global container: allows accessing various components of the main frame
     */
    @Override
    public void establishGlobalContainer(GlobalContainer GC)
    {
        super.establishGlobalContainer(GC);
        if (_auxGrid != null) _auxGrid.establishGlobalContainer(GC);
        if (_mainGrid != null) _mainGrid.establishGlobalContainer(GC);
    }

    /**
     * Method for updating relative fields values ({@link scheme.referencevalue.IReferenceValueGetter}).
     */
    @Override
    public void updateRelativeFields()
    {
        _borderStroke = getStroke(_borderWidth);
    }

    /**
     * Called to update component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);
        if (_mainGrid != null) _mainGrid.updateScheme(scheme);
        if (_auxGrid != null) _auxGrid.updateScheme(scheme);
    }

    /**
     * Can be called to clear memory.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _mainGrid = null;
        _auxGrid = null;
        _renderingData.dispose();
        _renderingData = null;
    }

    /**
     * Getter for the main grid.
     *
     * @return main grid
     */
    public Grid getMainGrid()
    {
        return _mainGrid;
    }

    /**
     * Getter for the aux grid.
     *
     * @return aux grid
     */
    public Grid getAuxGrid()
    {
        return _auxGrid;
    }
}
