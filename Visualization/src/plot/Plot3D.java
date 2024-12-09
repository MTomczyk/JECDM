package plot;

import component.AbstractSwingComponent;
import component.drawingarea.AbstractDrawingArea;
import component.drawingarea.DrawingArea3D;
import container.Notification;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import gl.GLInit;
import scheme.enums.Align;
import space.Dimension;


/**
 * Default implementation of a plot for 3D visualization (OpenGL-based rendering).
 *
 * @author MTomczyk
 */


public class Plot3D extends AbstractPlot
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPlot.Params
    {
        /**
         * Panes to be drawn (expressed as a list of their alignments; default = LEFT, BACK, BOTTOM).
         */
        public Align[] _paneAlignments = new Align[]{Align.BOTTOM, Align.RIGHT, Align.BACK};

        /**
         * axes to be drawn (expressed as a list of their alignments; can be null).
         */
        public Align[] _axesAlignments = new Align[]{
                Align.FRONT_BOTTOM, Align.BACK_RIGHT, Align.LEFT_TOP
        };


        /**
         * Determines the number of offscreen buffers used for rendering on the GPU.
         * Recommended = 2. Note that the painting is asynchronous and, thus, if = 1,
         * the tearing may be substantial.
         */
        public int _noBuffers = 3;

        /**
         * If true, the main plot area will be outlined with a cuboid.
         */
        public boolean _drawCube = true;

        /**
         * If true, the rendered area size fits the drawing area. Also, if true, it surpasses the aspect ratio param.
         */
        public boolean _fitToDrawingArea = true;

        /**
         * Ratio between the gl render width and height.
         * The render size is determined in a way that it is the greatest rectangle (square) fitting the plot bounds.
         */
        public float _aspectRatio = 1.0f;

        /**
         * If true, z-axis is drawn; false otherwise.
         */
        public boolean _drawZAxis = true;

        /**
         * Z-axis (axes) title. Can be null -> not rendered.
         */
        public String _zAxisTitle = null;

        /**
         * If true, antialiasing wille used. False = no antialiasing.
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
    }

    /**
     * Reference to the plot model (viewed as 3D plot model).
     */
    protected Plot3DModel _M3D;

    /**
     * Reference to the plot controller (viewed as 3D controller).
     */
    protected Plot3DController _C3D;

    /**
     * Parametrized constructor.
     *
     * @param p params container
     */
    public Plot3D(Params p)
    {
        super(p);
        _name = "Plot 3D";
        doGLInit();
    }

    /**
     * Execute open gl initialization for all parent objects and stores them in the container.
     */
    protected void doGLInit()
    {
        for (GLInit glI : _M3D.getGLInitComponentsContainer().getComponents()) glI.init();
    }


    /**
     * Constructs a default display ranges manager automatically when the {@link drmanager.DisplayRangesManager.Params} were not provided.
     *
     * @param p params container
     * @return instance of {@link DisplayRangesManager}.
     */
    @Override
    protected DisplayRangesManager constructDefaultDisplayRangesManager(AbstractPlot.Params p)
    {
        return new DisplayRangesManager(DisplayRangesManager.Params.getFor3D());
    }

    /**
     * Instantiates the model and the controller.
     *
     * @param p  params container
     * @param PC plot container
     */
    @Override
    protected void instantiateModelAndController(AbstractPlot.Params p, PlotContainer PC)
    {
        _C3D = new Plot3DController(this);
        _C = _C3D;
        _M3D = new Plot3DModel(this, PC);
        _M = _M3D;
        doBasicParameterizationOfPlotAndController(p);
    }

    /**
     * Customizes plot 3D.
     *
     * @param p params container
     */
    @Override
    protected void applySchemeCustomModifications(AbstractPlot.Params p)
    {

    }

    /**
     * Fills the drawing area params used to construct the drawing area object.
     *
     * @param pD drawing area params
     * @param p  plot params container
     */
    protected void fillDrawingAreaParams(AbstractSwingComponent.Params pD, AbstractPlot.Params p)
    {
        DrawingArea3D.Params pD3D = (DrawingArea3D.Params) pD;
        Plot3D.Params pp = (Params) p;
        pD3D._noBuffers = pp._noBuffers;
        pD3D._xAxisTitle = pp._xAxisTitle;
        pD3D._yAxisTitle = pp._yAxisTitle;
        pD3D._zAxisTitle = pp._zAxisTitle;
        pD3D._PC = _PC;
        pD3D._pDisplayRangesManager = pp._pDisplayRangesManager;
        pD3D._drawCube = pp._drawCube;
        pD3D._fitToDrawingArea = pp._fitToDrawingArea;
        pD3D._aspectRatio = pp._aspectRatio;
        pD3D._drawMainGridlines = pp._drawMainGridlines;
        pD3D._drawAuxGridlines = pp._drawAuxGridlines;
        pD3D._clipDrawingArea = pp._clipDrawingArea;
        pD3D._useAlphaChannel = pp._useAlphaChannel;
        pD3D._useAntiAliasing = pp._useAntiAliasing;
        pD3D._usedAASamples = pp._usedAASamples;
        pD3D._paneAlignments = pp._paneAlignments;
        pD3D._axesAlignments = pp._axesAlignments;
        pD3D._xProjectionBound = pp._xProjectionBound;
        pD3D._yProjectionBound = pp._yProjectionBound;
        pD3D._zProjectionBound = pp._zProjectionBound;
    }

    /**
     * Auxiliary method for instantiating plot drawing area (2D).
     *
     * @param p params container
     * @return drawing area object
     */
    @Override
    protected AbstractDrawingArea createDrawingArea(AbstractPlot.Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: get drawing area method call");

        DrawingArea3D.Params pD = new DrawingArea3D.Params(_PC);
        fillDrawingAreaParams(pD, p);
        DrawingArea3D drawingArea3D = new DrawingArea3D(pD);

        _M3D.getGLInitComponentsContainer().getComponents().add(drawingArea3D);
        _M3D.getGLInitComponentsContainer().setDrawingArea(drawingArea3D);
        return drawingArea3D;
    }


}
