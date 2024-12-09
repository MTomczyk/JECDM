package plot.heatmap;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import component.drawingarea.DrawingArea3D;
import component.drawingarea.DrawingArea3DGLEventListener;
import container.PlotContainer;

/**
 * GL event listener linked to {@link DrawingArea3D}.
 *
 * @author MTomczyk
 */
public class Heatmap3DDrawingAreaGLEventListener extends DrawingArea3DGLEventListener implements GLEventListener
{

    /**
     * Reference to the parent drawing area 3d.
     */
    private final Heatmap3DDrawingArea _heatmapDrawingArea3D;

    /**
     * Parameterized constructor.
     *
     * @param noBuffers            the   number of offscreen buffers
     * @param heatmapDrawingArea3D reference to the parent drawing area 3d (heatmap)
     * @param PC                   plot container
     */
    public Heatmap3DDrawingAreaGLEventListener(int noBuffers, Heatmap3DDrawingArea heatmapDrawingArea3D, PlotContainer PC)
    {
        super(noBuffers, heatmapDrawingArea3D, PC);
        _heatmapDrawingArea3D = heatmapDrawingArea3D;
    }

    /**
     * Releases GL resources.
     *
     * @param glAutoDrawable offscreen auto drawable
     */
    @Override
    public void dispose(GLAutoDrawable glAutoDrawable)
    {
        super.dispose(glAutoDrawable);
        GL2 gl = glAutoDrawable.getGL().getGL2();
        _heatmapDrawingArea3D._heatmapLayer.dispose(gl);
    }
}
