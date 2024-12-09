package component.drawingarea;

import color.Color;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;
import component.axis.gl.Axis3D;
import component.pane.Pane;
import container.PlotContainer;
import gl.IVBOComponent;
import listeners.interact.AbstractInteractListener;

import java.awt.image.BufferedImage;

/**
 * GL event listener linked to {@link DrawingArea3D}.
 *
 * @author MTomczyk
 */
public class DrawingArea3DGLEventListener implements GLEventListener
{


    /**
     * Determines the number of offscreen buffers used for rendering on the GPU.
     * Recommended = 2. Note that the painting is asynchronous and, thus, if = 1,
     * the tearing may be substantial.
     */
    private final int _noBuffers;

    /**
     * Reference to the parent drawing area 3d.
     */
    private final DrawingArea3D _drawingArea3D;

    /**
     * Plot container.
     */
    private final PlotContainer _PC;

    /**
     * Simple boolean-based locked for controlling buffer swapping.
     */
    private volatile boolean _activeBufferIsLocked = false;

    /**
     * Current active buffer used for rendering on the GPU.
     */
    private int _activeBuffer;


    /**
     * Current render used when repainting the main canvas.
     */
    private Integer _currentRender;

    /**
     * Buffer util used to transfer GL renders into the buffered image (each per buffer).
     * The same object instance is kept as long as the component size remains the same.
     * In the case of the resize event, a new buffer util object is created (as it implies creating a new buffered image).
     */
    private final AWTGLReadBufferUtil[] _bufferUtil;

    /**
     * Buffer util that is supposed to replace the current one after the repainting
     * triggered by the on the resize event is done (each per buffer).
     */
    private final AWTGLReadBufferUtil[] _onResizeBufferUtil;

    /**
     * Generated renders.
     */
    private final BufferedImage[] _renders;


    /**
     * Parameterized constructor.
     *
     * @param noBuffers     no offscreen buffers (buffered images)
     * @param drawingArea3D reference to the parent drawing area 3d
     * @param PC            plot container
     */
    public DrawingArea3DGLEventListener(int noBuffers, DrawingArea3D drawingArea3D, PlotContainer PC)
    {
        _noBuffers = noBuffers;
        _drawingArea3D = drawingArea3D;
        _PC = PC;
        _activeBuffer = 0;
        _currentRender = 0;
        _bufferUtil = new AWTGLReadBufferUtil[noBuffers];
        _onResizeBufferUtil = new AWTGLReadBufferUtil[noBuffers];
        _renders = new BufferedImage[noBuffers];
    }


    /**
     * Performs GL-related initialization.
     *
     * @param glAutoDrawable offscreen auto drawable
     */
    @Override
    public void init(GLAutoDrawable glAutoDrawable)
    {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        if (_drawingArea3D._useAntiAliasing) gl.glEnable(GL2.GL_MULTISAMPLE);
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
        //gl.glEnable(GL2.GL_POINT_SIZE);
        gl.glEnable(GL2.GL_VERTEX_PROGRAM_POINT_SIZE);
        gl.glDepthFunc(GL2.GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glLoadIdentity();

        if (_drawingArea3D._useAlphaChannel)
        {
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL2.GL_BLEND);
        }

        // only if ID = 0 (only master inits VBOs)
        _drawingArea3D.initVBO(gl);
        gl.glFlush();
    }


    /**
     * Appropriately clear color and depth buffers.
     *
     * @param gl current gl context
     */
    protected void clearColors(GL2 gl)
    {
        float alpha = 1.0f;
        if (_drawingArea3D._useAlphaChannel) alpha = 0.0f;
        Color c = _drawingArea3D.getBackgroundColor();
        if (c != null) gl.glClearColor(c._r, c._g, c._b, alpha);
        else gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    }

    /**
     * Auxiliary method that increments the current buffer number and backs to 0 if the counter exceeds the number of buffers.
     */
    protected void incrementActiveBuffer()
    {
        // active buffer cannot hit the current render
        _activeBuffer++;
        if (_activeBuffer >= _noBuffers) _activeBuffer = 0;
    }

    /**
     * Auxiliary method that increments the current render number and backs to 0 if the counter exceeds the number of buffers
     * or stops if the number meets the active buffer number.
     */
    protected void incrementCurrentRender()
    {
        if (_currentRender == null) _currentRender = 0;
        if (_currentRender == _activeBuffer) return; // current render can hit the active buffer

        if ((_activeBufferIsLocked) && ((_currentRender + 1 == _activeBuffer)
            || ((_currentRender == _noBuffers - 1) && (_activeBuffer == 0)))) return; // do not shift when locked

        _currentRender++; // shift
        if (_currentRender >= _noBuffers) _currentRender = 0;
    }


    /**
     * Increments the current render index and returns the pointed render to be painted.
     *
     * @return render to be painted
     */
    protected BufferedImage incrementCurrentIndexAndGetRender()
    {
        if (_currentRender == null) return null;
        if (_currentRender < 0) return null;
        if (_currentRender >= _renders.length) return null;
        incrementCurrentRender();
        return _renders[_currentRender];
    }

    /**
     * Executes GL-based rendering (offscreen rendering)
     *
     * @param glAutoDrawable offscreen auto drawable
     */
    @Override
    public void display(GLAutoDrawable glAutoDrawable)
    {
        if (_drawingArea3D._onReshape)
        {
            _drawingArea3D._onReshape = false;
            return;
        }

        long pTime = 0;
        if (_drawingArea3D._measureRenderGenerationTimes)
            pTime = System.nanoTime();

        space.Dimension[] onDimensions = _drawingArea3D._renderingData.getCopyOfOnscreenLayerExpectedDimensions();

        // premature termination
        if ((Double.compare(onDimensions[0]._size, 0.0d) <= 0) || (Double.compare(onDimensions[1]._size, 0.0d) <= 0))
        {
            _currentRender = null;
            return;
        }

        _activeBufferIsLocked = true; // lock

        GL2 gl = glAutoDrawable.getGL().getGL2();

        // remove previous painters
        if (_drawingArea3D._painters3DForRemoval != null)
        {
            for (IVBOComponent c : _drawingArea3D._painters3DForRemoval) c.dispose(gl);
            _drawingArea3D._painters3DForRemoval = null;
        }

        // check for painters that may require first upload on GPU
        // create initial data transfer
        if (_drawingArea3D._painters3D != null)
        {
            for (IVBOComponent c : _drawingArea3D._painters3D)
            {
                if (c.isInitialUpdateRequested()) c.executeInitialDataTransfer(gl);
                else if (c.isUpdateRequested()) c.executeUpdate(gl);
            }
        }

        // check for components that may require update (even initial) on GPU
        for (IVBOComponent c : _drawingArea3D._componentsRequiringUpdate)
        {
            if (c.isInitialUpdateRequested()) c.executeInitialDataTransfer(gl);
            else if (c.isUpdateRequested()) c.executeUpdate(gl);
        }

        gl.glLoadIdentity();
        clearColors(gl);

        float[] camera = null;
        float[] object = null;
        float[] translation = null;

        AbstractInteractListener listener = null;
        if (_PC != null) listener = _PC.getInteractListener();
        if (listener != null)
        {
            if (listener.getCameraRotation() != null) camera = listener.getCameraRotation().clone();
            if (listener.getObjectRotation() != null) object = listener.getObjectRotation().clone();
            if (listener.getTranslation() != null) translation = listener.getTranslation().clone();
        }

        if (camera != null)
        {
            gl.glRotatef(camera[0], 1f, 0f, 0f);
            gl.glRotatef(camera[1], 0f, 1f, 0f);
        }

        if (translation != null) gl.glTranslatef(translation[0], translation[1], -translation[2]);

        if (object != null)
        {
            gl.glRotatef(object[0], 1f, 0f, 0f);
            gl.glRotatef(object[1], 0f, 1f, 0f);
        }

        _drawingArea3D.drawObjects(gl);

        gl.glFlush();
        gl.getContext().getGLDrawable().swapBuffers();

        incrementActiveBuffer();

        if (_onResizeBufferUtil[_activeBuffer] != null)
        {
            if (_bufferUtil[_activeBuffer] != null) _bufferUtil[_activeBuffer].dispose(gl);
            if (_renders[_activeBuffer] != null)
            {
                _renders[_activeBuffer].flush();
                _renders[_activeBuffer] = null;
            }
            _bufferUtil[_activeBuffer] = _onResizeBufferUtil[_activeBuffer];
            _onResizeBufferUtil[_activeBuffer] = null;
        }

        if (_bufferUtil[_activeBuffer] != null)
        {

            BufferedImage glImage = _bufferUtil[_activeBuffer].readPixelsToBufferedImage(gl, false); // does not create new object

            if (_renders[_activeBuffer] == null)
            {
                _renders[_activeBuffer] = glImage; // gl image is shared and instantiated only on resize
            }

        }

        _activeBufferIsLocked = false; // unlock

        if (_drawingArea3D._measureRenderGenerationTimes)
            _drawingArea3D._renderGenerationTimes.addData(System.nanoTime() - pTime);
    }


    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3)
    {
        if ((i2 > 0) && (i3 > 0))
        {
            _drawingArea3D._onReshape = true;
            GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glViewport(0, 0, i2, i3);
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            _drawingArea3D._glu.gluPerspective(60.0f, (float) i2 / i3, 0.01f, 10f);

            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glFlush();

            if (_currentRender == null)
            {
                _activeBuffer = 0;
                _currentRender = 0;
            }

            for (AWTGLReadBufferUtil u : _onResizeBufferUtil)
                if (u != null) u.dispose(gl);

            for (int b = 0; b < _noBuffers; b++)
                _onResizeBufferUtil[b] = new AWTGLReadBufferUtil(_drawingArea3D._profile, _drawingArea3D._useAlphaChannel);
        }
        else _currentRender = null;
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable)
    {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        if (_drawingArea3D._axes != null) for (Axis3D a : _drawingArea3D._axes) a.dispose(gl);
        if (_drawingArea3D._panes != null) for (Pane p : _drawingArea3D._panes) p.dispose(gl);
        if (_drawingArea3D._cube != null) _drawingArea3D._cube.dispose(gl);
        if (_drawingArea3D._painters3D != null) for (IVBOComponent p : _drawingArea3D._painters3D) p.dispose(gl);
        if (_drawingArea3D._componentsRequiringUpdate != null)
            for (IVBOComponent p : _drawingArea3D._componentsRequiringUpdate) p.dispose(gl);

        for (AWTGLReadBufferUtil u : _bufferUtil) if (u != null) u.dispose(gl);
        for (AWTGLReadBufferUtil u : _onResizeBufferUtil) if (u != null) u.dispose(gl);
    }


}
