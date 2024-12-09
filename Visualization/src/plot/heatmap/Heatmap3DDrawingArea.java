package plot.heatmap;

import com.jogamp.opengl.GL2;
import component.drawingarea.DrawingArea3D;
import component.drawingarea.DrawingArea3DGLEventListener;
import container.PlotContainer;
import gl.GLInit;

/**
 * Class representing a plot drawing area for 3D visualization (OpenGL-based).
 *
 * @author MTomczyk
 */


public class Heatmap3DDrawingArea extends DrawingArea3D implements GLInit
{
    /**
     * Params container.
     */
    public static class Params extends DrawingArea3D.Params
    {
        /**
         * Heatmap 3D layer to be rendered.
         */
        public Heatmap3DLayer _heatmapLayer = null;

        /**
         * Parameterized constructor.
         *
         * @param PC plot container: allows accessing various plot components
         */
        public Params(PlotContainer PC)
        {
            super(PC);
            _name = "Heatmap drawing area 3d";
        }
    }

    /**
     * Heatmap 3D layer to be rendered.
     */
    protected Heatmap3DLayer _heatmapLayer;

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public Heatmap3DDrawingArea(Params p)
    {
        super(p);
        _heatmapLayer = p._heatmapLayer;
    }

    /**
     * Auxiliary method initializing VBO objects/buffers.
     *
     * @param gl open gl rendering context
     */
    @Override
    protected void initVBO(GL2 gl)
    {
        super.initVBO(gl);
        _componentsRequiringUpdate.add(_heatmapLayer);
    }

    /**
     * Constructs and returns new GL Event listener object (each one is linked to a different GL auto drawable).
     *
     * @param noBuffers no offscreen buffers
     * @return gl event listener
     */
    @Override
    protected DrawingArea3DGLEventListener getGLEventListener(int noBuffers)
    {
        return new Heatmap3DDrawingAreaGLEventListener(noBuffers, this, _PC);
    }

    /**
     * Draws objects.
     *
     * @param gl open gl rendering context
     */
    @Override
    protected void drawObjects(GL2 gl)
    {
        super.drawObjects(gl);
        _heatmapLayer.draw(gl);
    }

    /**
     * Can be called to clear memory.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _heatmapLayer.dispose();
    }
}
