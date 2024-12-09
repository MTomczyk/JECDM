package plot.heatmap;

import component.drawingarea.DrawingArea2D;
import container.PlotContainer;

import java.awt.*;

/**
 * Class representing a plot drawing area for 2D visualization (AWT-based).
 *
 * @author MTomczyk
 */


public class Heatmap2DDrawingArea extends DrawingArea2D
{
    /**
     * Params container.
     */
    public static class Params extends DrawingArea2D.Params
    {
        /**
         * Object rendering the heatmap.
         */
        public Heatmap2DLayer _heatmap2DLayer;

        /**
         * Parameterized constructor.
         *
         * @param PC             plot container: allows accessing various plot components
         * @param heatmap2DLayer object rendering the heatmap
         */
        public Params(PlotContainer PC, Heatmap2DLayer heatmap2DLayer)
        {
            super(PC);
            _name = "Heatmap 2D drawing area";
            _heatmap2DLayer = heatmap2DLayer;
        }
    }

    /**
     * Object rendering the heatmap.
     */
    private final Heatmap2DLayer _heatmap2DLayer;

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public Heatmap2DDrawingArea(Params p)
    {
        super(p);
        _heatmap2DLayer = p._heatmap2DLayer;
    }


    /**
     * Executed before drawing data sets.
     * Paints components that do not use up(down) scaling.
     */
    @Override
    protected void paintAuxElements(Graphics g)
    {
        _heatmap2DLayer.paintComponent(g);
        super.paintAuxElements(g);
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
        _heatmap2DLayer.setLocationAndSize(x, y, w, h);
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
        _heatmap2DLayer.setPrimaryDrawingArea(x, y, w, h);
    }

    /**
     * Can be called to clear memory.
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        super.dispose();
        _heatmap2DLayer.dispose();
    }
}
