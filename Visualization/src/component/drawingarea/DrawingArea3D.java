package component.drawingarea;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import component.axis.gl.Axis3D;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.FromFixedInterval;
import component.axis.ticksupdater.ITicksDataGetter;
import component.cube.Cube;
import component.drawingarea.renderingdata.RenderingData;
import component.pane.Pane;
import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import dataset.painter.Painter3D;
import drmanager.DisplayRangesManager;
import gl.GLInit;
import gl.IVBOComponent;
import scheme.AbstractScheme;
import scheme.enums.Align;
import space.Dimension;
import space.Range;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.EventTypes;
import utils.Projection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class representing a plot drawing area for 3D visualization (OpenGL-based).
 *
 * @author MTomczyk
 */


public class DrawingArea3D extends AbstractDrawingArea implements GLInit
{
    /**
     * Params container.
     */
    public static class Params extends AbstractDrawingArea.Params
    {
        /**
         * Determines the number of offscreen buffers used for rendering on the GPU.
         * Recommended = 2. Note that the painting is asynchronous and, thus, if = 1,
         * the tearing may be substantial.
         */
        public int _noBuffers = 1;

        /**
         * If true, the rendered area size fits the drawing area. Also, if true, it surpasses the aspect ratio param.
         */
        public boolean _fitToDrawingArea = false;

        /**
         * Ratio between the gl render width and height.
         * The render size is determined in a way that it is the greatest rectangle (square) fitting the plot bounds.
         */
        public float _aspectRatio = 1.0f;

        /**
         * If true, the main plot area is encapsulated with a cuboid.
         */
        public boolean _drawCube = true;

        /**
         * Panes to be drawn (expressed as a list of their alignments; can be null).
         */
        public Align[] _paneAlignments = null;

        /**
         * axes to be drawn (expressed as a list of their alignments; can be null).
         */
        public Align[] _axesAlignments = null;

        /**
         * Z-axis (axes) title. Can be null -> not rendered.
         */
        public String _zAxisTitle = null;

        /**
         * If true, antialiasing will be used. False = no antialiasing.
         */
        public boolean _useAntiAliasing = true;

        /**
         * No. samples for antialiasing.
         */
        public int _usedAASamples = 4;

        /**
         * If true, alpha channel is used. False -> memory can be saved and the computational efficiency can be improved.
         */
        public boolean _useAlphaChannel = false;

        /**
         * Projection bound for the x-dimension (if null, default values are used, i.e., [position = -0.5; size = 1.0].
         */
        public Dimension _xProjectionBound = null;

        /**
         * Projection bound for the y-dimension (if null, default values are used, i.e., [position = -0.5; size = 1.0].
         */
        public Dimension _yProjectionBound = null;

        /**
         * Projection bound for the z-dimension (if null, default values are used, i.e., [position = -0.5; size = 1.0].
         */
        public Dimension _zProjectionBound = null;

        /**
         * Parameterized constructor.
         *
         * @param PC plot container: allows accessing various plot components
         */
        public Params(PlotContainer PC)
        {
            super("Drawing Area 3D", PC);
        }
    }


    /**
     * Determines the number of offscreen buffers used for rendering on the GPU.
     * Recommended = 2. Note that the painting is asynchronous and, thus, if = 1,
     * the tearing may be substantial.
     */
    private int _noBuffers;

    /**
     * GL auto drawable (used for rendering the scene).
     * The number of drawables equals the number of buffers.
     * The drawable's resolution is dynamic and changes as a consequence of the plot resize event.
     */
    private GLOffscreenAutoDrawable _gl = null;

    /**
     * GL event listeners (each per buffer).
     */
    private DrawingArea3DGLEventListener _glEventListener;

    /**
     * Auxiliary GLU object (utility).
     */
    protected final GLUgl2 _glu = new GLUgl2();

    /**
     * GL profile.
     */
    protected GLProfile _profile;

    /**
     * If true, the rendered area size fits the drawing area. Also, if true, it surpasses the aspect ratio param.
     */
    private final boolean _fitToDrawingArea;

    /**
     * Ratio between the gl render width and height.
     * The render size is determined in a way that it is the greatest rectangle (square) fitting the plot bounds.
     */
    private float _aspectRatio;

    /**
     * If true, the main plot area is encapsulated with a cuboid.
     */
    protected final boolean _drawCube;

    /**
     * If true, antialiasing will be used. False = no antialiasing.
     */
    protected final boolean _useAntiAliasing;

    /**
     * No. samples for antialiasing.
     */
    private final int _usedAASamples;

    /**
     * If true, alpha channel is used. False -> memory can be saved and the computational efficiency can be improved.
     */
    protected final boolean _useAlphaChannel;

    /**
     * Cube 3D object.
     */
    protected Cube _cube = null;

    /**
     * Optional panes (can be null).
     */
    protected Pane[] _panes = null;

    /**
     * Panes to be drawn (expressed as a list of their alignments; can be null).
     */
    private final Align[] _paneAlignments;

    /**
     * Axes 3D.
     */
    protected Axis3D[] _axes;

    /**
     * Axes to be drawn (expressed as a list of their alignments; can be null).
     */
    private final Align[] _axesAlignments;

    /**
     * Contains a list of vbo-based components (not data-set related) that can potentially require data update on the GPU.
     */
    protected LinkedList<IVBOComponent> _componentsRequiringUpdate;

    /**
     * List of data sets' painters that are implementing {@link IVBOComponent}.
     * Only such painters are considered valid for painting.
     * They are stored and viewed as this interface so that various gl-based operations (e.g., rendering) can be executed.
     */
    protected LinkedList<IVBOComponent> _painters3D = null;

    /**
     * List of data sets' painters that are implementing {@link IVBOComponent} that are to be removed
     * during the next display call.
     */
    protected LinkedList<IVBOComponent> _painters3DForRemoval = new LinkedList<>();

    /**
     * Additional flag indicating whether the display method was triggered by the reshape event (to avoid).
     */
    protected volatile boolean _onReshape = false;

    /**
     * Parameterized constructor
     *
     * @param p params container
     */
    public DrawingArea3D(Params p)
    {
        super(p);
        _fitToDrawingArea = p._fitToDrawingArea;
        _aspectRatio = p._aspectRatio;
        if (Float.compare(_aspectRatio, 0.0f) < 0) _aspectRatio = 1.0f;
        _drawCube = p._drawCube;
        _useAntiAliasing = p._useAntiAliasing;
        _usedAASamples = p._usedAASamples;
        _useAlphaChannel = p._useAlphaChannel;
        _paneAlignments = p._paneAlignments;
        _axesAlignments = p._axesAlignments;
        _noBuffers = p._noBuffers;
        createGLObjects(p);
        setLayout(null);
    }

    /**
     * Instantiates the rendering data.
     *
     * @param p params container
     */
    @Override
    protected void instantiateRenderingData(AbstractDrawingArea.Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: instantiate rendering data method called");
        Params pp = (Params) p;
        _renderingData = new RenderingData(3);

        if (pp._xProjectionBound == null) _renderingData.setProjectionBound(-0.5f, 1.0f, 0);
        else
            _renderingData.setProjectionBound((float) pp._xProjectionBound._position, (float) pp._xProjectionBound._size, 0);
        if (pp._yProjectionBound == null) _renderingData.setProjectionBound(-0.5f, 1.0f, 1);
        else
            _renderingData.setProjectionBound((float) pp._yProjectionBound._position, (float) pp._yProjectionBound._size, 1);
        if (pp._zProjectionBound == null) _renderingData.setProjectionBound(-0.5f, 1.0f, 2);
        else
            _renderingData.setProjectionBound((float) pp._zProjectionBound._position, (float) pp._zProjectionBound._size, 2);
    }

    /**
     * Init OpenGL-related functionalities.
     */
    @Override
    public void init()
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: init (gl) called");
        _profile = GLProfile.get(GLProfile.GL2);
        GLDrawableFactory factory = GLDrawableFactory.getFactory(_profile);
        GLCapabilities capabilities = getGlCapabilities();

        if (_noBuffers < 1) _noBuffers = 1;

        _glEventListener = getGLEventListener(_noBuffers);
        _gl = factory.createOffscreenAutoDrawable(factory.getDefaultDevice(), capabilities, null, 1, 1);
        _gl.addGLEventListener(_glEventListener);
        _gl.setAutoSwapBufferMode(true);
        _gl.display();
    }

    /**
     * Constructs and returns new GL Event listener object (each one is linked to a different GL auto drawable).
     *
     * @param noBuffers no offscreen buffers
     * @return gl event listener
     */
    protected DrawingArea3DGLEventListener getGLEventListener(int noBuffers)
    {
        return new DrawingArea3DGLEventListener(noBuffers, this, _PC);
    }

    /**
     * Method for creating auxiliary gl objects.
     *
     * @param p params container
     */
    protected void createGLObjects(Params p)
    {
        if (p._drawCube) _cube = new Cube(_PC);

        if (_paneAlignments != null)
        {
            int num = 0;
            for (Align a : _paneAlignments) if (a != null) num++;
            if (num > 0)
            {
                _panes = new Pane[num];
                num = 0;
                for (Align a : _paneAlignments) _panes[num++] = new Pane("PANE [" + a + "]", _PC, a);
            }
            else _panes = null;
        }
        else _panes = null;

        if (_axesAlignments != null)
        {
            int num = 0;
            for (Align a : _axesAlignments) if (a != null) num++;
            if (num > 0)
            {
                _axes = new Axis3D[num];
                num = 0;
                for (Align a : _axesAlignments)
                {
                    Axis3D.Params pA = new Axis3D.Params("axis 3D " + a, _PC);
                    pA._align = a;
                    pA._ticksDataGetter = null;
                    int displayRangeID = Axis3D.getAssociatedDisplayRangeID(a);
                    if (displayRangeID == 0) pA._title = p._xAxisTitle;
                    else if (displayRangeID == 1) pA._title = p._yAxisTitle;
                    else if (displayRangeID == 2) pA._title = p._zAxisTitle;
                    DisplayRangesManager.DisplayRange DR = _PC.getDisplayRangesManager().getDisplayRange(displayRangeID);
                    if (DR != null) pA._ticksDataGetter = new FromDisplayRange(DR, 5);
                    else pA._ticksDataGetter = new FromFixedInterval(Range.getNormalRange(), 5);
                    _axes[num++] = new Axis3D(pA);
                }
            }
            else _axes = null;
        }
        else _axes = null;
    }

    /**
     * Auxiliary method initializing VBO objects/buffers.
     *
     * @param gl open gl rendering context
     */
    protected void initVBO(GL2 gl)
    {
        _componentsRequiringUpdate = new LinkedList<>();

        if (_drawCube)
        {
            _cube.createBuffers();
            _cube.executeInitialDataTransfer(gl);
        }

        if (_panes != null)
        {
            for (Pane p : _panes)
            {
                p.createBuffers();
                p.executeInitialDataTransfer(gl);
                _componentsRequiringUpdate.add(p);
            }
        }

        if (_axes != null)
        {
            for (Axis3D a : _axes)
            {
                a.createBuffers();
                a.executeInitialDataTransfer(gl);
                _componentsRequiringUpdate.add(a);
            }
        }
    }


    /**
     * Getter for GL capabilities.
     *
     * @return GL capabilities
     */
    private GLCapabilities getGlCapabilities()
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: get gl capabilities method called");

        GLCapabilities capabilities = new GLCapabilities(_profile);
        capabilities.setDoubleBuffered(true);
        capabilities.setHardwareAccelerated(true);
        capabilities.setOnscreen(false);
        capabilities.setFBO(true);
        capabilities.setPBuffer(true);
        capabilities.setAlphaBits(8);
        capabilities.setRedBits(8);
        capabilities.setBlueBits(8);
        capabilities.setGreenBits(8);

        if (_useAntiAliasing)
        {
            capabilities.setSampleBuffers(true);
            capabilities.setNumSamples(_usedAASamples);
        }
        return capabilities;
    }

    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics g2 = g.create();
        BufferedImage render = _glEventListener.incrementCurrentIndexAndGetRender();
        if (render != null)
        {
            Dimension[] d = _renderingData.getCopyOfOnscreenLayerExpectedDimensions();
            int dlx = Projection.getP((float) d[0]._position);
            int dly = Projection.getP((float) d[1]._position);
            int drx = Projection.getP((float) d[0].getRightPosition());
            int dry = Projection.getP((float) d[1].getRightPosition());
            int slx = 0;
            int sly = 0;
            int srx = Projection.getP((float) d[0]._size);
            int sry = Projection.getP((float) d[1]._size);
            Graphics2D g2d = (Graphics2D) g2;
            g2d.drawImage(render, dlx, dly, drx, dry, slx, sry, srx, sly, null);
        }
        g2.dispose();
    }

    /**
     * Draws objects.
     *
     * @param gl open gl rendering context
     */
    protected void drawObjects(GL2 gl)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: draw (gl) objects method called");
        if (_axes != null) for (Axis3D a : _axes) a.draw(gl);
        if (_panes != null) for (Pane p : _panes) p.draw(gl);
        if (_cube != null) _cube.draw(gl);
        if (_painters3D != null) for (IVBOComponent c : _painters3D) c.draw(gl);
    }

    /**
     * This method calls the display method on the GLEventListener. The method prepares the final
     * render to be displayed on this component
     *
     * @param eventType event type
     * @return returns render updater
     */
    @Override
    public QueuedSwingWorker<Void, Void> createRenderUpdater(EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: create render updater method called");
        if ((eventType.equals(EventTypes.ON_DATA_CHANGED)) || (eventType.equals(EventTypes.ON_RESIZE))
                || (eventType.equals(EventTypes.ON_DEMAND)) || (eventType.equals(EventTypes.ON_INTERACTION))
                || (eventType.equals(EventTypes.ON_HEATMAP_FILTER_CHANGED)) || (eventType.equals(EventTypes.ON_HEATMAP_DATA_CHANGED)))
        {
            return new DrawingArea3DRenderUpdater(_gl);
        }
        return null;
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
        Dimension[] d = _renderingData.getCopyOfOnscreenLayerExpectedDimensions();
        if ((w > 1) && (h > 1))
        {
            for (int i = 0; i < _noBuffers; i++)
            {
                if ((_gl != null) && (d != null))
                {
                    _gl.setSurfaceSize(Projection.getP((float) d[0]._size), Projection.getP((float) d[1]._size));
                }
            }
        }
    }

    /**
     * Auxiliary method that establishes the layer expected dimensions.
     */
    @Override
    protected void calculateLayerExpectedDimensions()
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: calculate layer expected dimensions method called");

        if ((getWidth() == 0) || (getHeight() == 0))
        {
            _renderingData.setOnscreenLayerExpectedDimensions(0, 0, 0);
            _renderingData.setOnscreenLayerExpectedDimensions(0, 0, 1);
            _renderingData.setOffscreenLayerExpectedDimensions(0, 0, 0);
            _renderingData.setOffscreenLayerExpectedDimensions(0, 0, 1);
        }
        else
        {
            if (_fitToDrawingArea)
            {
                _renderingData.setOnscreenLayerExpectedDimensions(Projection.getP(_translationVector[0]), _primaryDrawingArea.width, 0);
                _renderingData.setOnscreenLayerExpectedDimensions(Projection.getP(_translationVector[1]), _primaryDrawingArea.height, 1);
                _renderingData.setOffscreenLayerExpectedDimensions(Projection.getP(_translationVector[0]), _primaryDrawingArea.width, 0);
                _renderingData.setOffscreenLayerExpectedDimensions(Projection.getP(_translationVector[1]), _primaryDrawingArea.height, 1);
            }
            else
            {
                float cR = (float) getWidth() / getHeight();

                if (Float.compare(cR, _aspectRatio) > 0) // fit to height
                {
                    float newWidth = _primaryDrawingArea.height * _aspectRatio;
                    float newPosition = _translationVector[0] + (_primaryDrawingArea.width - newWidth) / 2.0f;
                    _renderingData.setOnscreenLayerExpectedDimensions(newPosition, newWidth, 0);
                    _renderingData.setOnscreenLayerExpectedDimensions(Projection.getP(_translationVector[1]),
                            _primaryDrawingArea.height, 1);
                    _renderingData.setOffscreenLayerExpectedDimensions(newPosition, newWidth, 0);
                    _renderingData.setOffscreenLayerExpectedDimensions(Projection.getP(_translationVector[1]),
                            _primaryDrawingArea.height, 1);
                }
                else // fit to width
                {
                    float newHeight = _primaryDrawingArea.width / _aspectRatio;
                    float newPosition = _translationVector[1] + (_primaryDrawingArea.height - newHeight) / 2.0f;
                    _renderingData.setOnscreenLayerExpectedDimensions(Projection.getP(_translationVector[0]),
                            _primaryDrawingArea.width, 0);
                    _renderingData.setOnscreenLayerExpectedDimensions(newPosition, newHeight, 1);
                    _renderingData.setOffscreenLayerExpectedDimensions(Projection.getP(_translationVector[0]),
                            _primaryDrawingArea.width, 0);
                    _renderingData.setOffscreenLayerExpectedDimensions(newPosition, newHeight, 1);
                }
            }
        }
    }

    /**
     * Sets the painters 3d associated with data sets for 3D visualization.
     *
     * @param painters 3D painters
     */
    public void setPainters3D(ArrayList<Painter3D> painters)
    {
        LinkedList<IVBOComponent> c = new LinkedList<>();
        for (Painter3D p : painters) if (p != null) c.add(p);
        _painters3D = c;
    }

    /**
     * Sets the painters 3d for removal
     *
     * @param paintersForRemoval 3D painters for removal
     */
    public void setPainters3DForRemoval(LinkedList<IVBOComponent> paintersForRemoval)
    {
        _painters3DForRemoval = paintersForRemoval;
    }

    /**
     * Getter for painters 3D.
     *
     * @return painters 3D
     */
    public LinkedList<IVBOComponent> getPainters3D()
    {
        return _painters3D;
    }


    /**
     * Getter for panes.
     *
     * @return panes
     */
    public Pane[] getPanes()
    {
        return _panes;
    }


    /**
     * Getter for axes.
     *
     * @return panes
     */
    public Axis3D[] getAxes()
    {
        return _axes;
    }


    /**
     * Can be used to update ticks data getter of gridlines (members of {@link Pane}).
     * The method iterates over all available panes and assigns them new ticks data getters - only relevant to them,
     * i.e., if pane represents XY coordinates, Z ticks data getter is not assigned. Nulled inputs are skipped.
     * At the end, the method triggers {@link IVBOComponent#updateBuffers()} method and marks the used VBOs as
     * the one that should be updated on the GPU next.
     *
     * @param xDimension new ticks data getter for the x-dimension (can be null -> skipped)
     * @param yDimension new ticks data getter for the y-dimension (can be null -> skipped)
     * @param zDimension new ticks data getter for the z-dimension (can be null -> skipped)
     */
    public void setGridlinesTicksDataGetters(ITicksDataGetter xDimension, ITicksDataGetter yDimension, ITicksDataGetter zDimension)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set gridlines ticks data getter method called");
        if (_panes == null) return;
        for (Pane p : _panes)
        {
            if (xDimension != null) p.setXTicksDataGetter(xDimension);
            if (yDimension != null) p.setYTicksDataGetter(yDimension);
            if (zDimension != null) p.setZTicksDataGetter(zDimension);
        }
    }

    /**
     * Assigns a new ticks data getter to those axes that are associated with the X-dimension.
     *
     * @param ticksDataGetter new ticks data getter
     */
    public void setTicksDataGetterForXAxes(ITicksDataGetter ticksDataGetter)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set ticks data getter for X-axes method called");
        setTicksDataGetterAxes(ticksDataGetter, 0);
    }

    /**
     * Assigns a new ticks data getter to those axes that are associated with the Y-dimension.
     *
     * @param ticksDataGetter new ticks data getter
     */
    public void setTicksDataGetterForYAxes(ITicksDataGetter ticksDataGetter)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set ticks data getter for Y-axes method called");
        setTicksDataGetterAxes(ticksDataGetter, 1);
    }

    /**
     * Assigns a new ticks data getter to those axes that are associated with the Z-dimension.
     *
     * @param ticksDataGetter new ticks data getter
     */
    public void setTicksDataGetterForZAxes(ITicksDataGetter ticksDataGetter)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set ticks data getter for Z-axes method called");
        setTicksDataGetterAxes(ticksDataGetter, 2);
    }

    /**
     * Assigns a new ticks data getter to those axes that are associated with the specified dimension.
     *
     * @param dimensionId     dimension ID (0 = X, 1 = Y, 2 = Z)
     * @param ticksDataGetter new ticks data getter
     */
    private void setTicksDataGetterAxes(ITicksDataGetter ticksDataGetter, int dimensionId)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set ticks data getter for axes method called");
        if (_axes == null) return;
        for (Axis3D a : _axes)
        {
            if (a == null) continue;
            if (a.getAssociatedDisplayRangeID() == dimensionId)
                a.setTicksDataGetter(ticksDataGetter);
        }
    }

    /**
     * Assigns new labels to those axes that are associated with the X-axis.
     *
     * @param labels new labels
     */
    public void setLabelsForXAxes(String[] labels)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set labels for X-axes method called");
        setLabelsForAxes(labels, 0);
    }

    /**
     * Assigns new labels to those axes that are associated with the Y-axis.
     *
     * @param labels new labels
     */
    public void setLabelsForYAxes(String[] labels)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set labels for Y-axes method called");
        setLabelsForAxes(labels, 1);
    }

    /**
     * Assigns new labels to those axes that are associated with the Z-axis.
     *
     * @param labels new labels
     */
    public void setLabelsForZAxes(String[] labels)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set labels for Z-axes method called");
        setLabelsForAxes(labels, 2);
    }

    /**
     * Assigns new labels to those axes that are associated with the specified dimension.
     *
     * @param labels      new labels
     * @param dimensionId dimension ID (0 = X, 1 = Y, 2 = Z)
     */
    private void setLabelsForAxes(String[] labels, int dimensionId)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: set labels for axes method called");
        if (_axes == null) return;
        for (Axis3D a : _axes)
        {
            if (a == null) continue;
            if (a.getAssociatedDisplayRangeID() == dimensionId)
                a.getTicksDataGetter().setPredefinedTickLabels(labels);
        }
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
        if (_cube != null) _cube.establishGlobalContainer(GC);
        if (_panes != null) for (Pane p : _panes) p.establishGlobalContainer(GC);
        if (_axes != null) for (Axis3D a : _axes) a.establishGlobalContainer(GC);
    }

    /**
     * Called to update the component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);
        if (_axes != null) for (Axis3D a : _axes) a.updateScheme(scheme);
        if (_panes != null) for (Pane p : _panes) p.updateScheme(scheme);
        if (_cube != null) _cube.updateScheme(scheme);
    }

    /**
     * Can be called to clear memory.
     */
    @Override
    public void dispose()
    {
        for (int i = 0; i < _noBuffers; i++)
        {
            _gl.destroy();
            _gl.removeGLEventListener(_glEventListener);
        }
        _glEventListener = null;
        _gl = null;

        super.dispose();
        _renderingData.dispose();
        _renderingData = null;

        _painters3D = null;
        _componentsRequiringUpdate = null;

        if (_cube != null)
        {
            _cube.dispose();
            _cube = null;
        }

        if (_panes != null) for (Pane p : _panes) p.dispose();
        _panes = null;
        if (_axes != null) for (Axis3D a : _axes) a.dispose();
        _axes = null;
    }
}
