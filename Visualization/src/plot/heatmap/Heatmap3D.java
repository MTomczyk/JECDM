package plot.heatmap;

import color.gradient.Gradient;
import component.AbstractSwingComponent;
import component.axis.gl.Axis3D;
import component.axis.ticksupdater.ITicksDataGetter;
import component.drawingarea.AbstractDrawingArea;
import component.pane.Pane;
import container.PlotContainer;
import dataset.painter.style.BucketStyle;
import drmanager.DisplayRangesManager;
import plot.AbstractPlot;
import plot.Plot3D;
import plot.Plot3DController;
import plot.PlotModel;
import space.normalization.minmax.AbstractMinMaxNormalization;

/**
 * Heatmap implementation of a plot for 2D visualization.
 *
 * @author MTomczyk
 */


public class Heatmap3D extends Plot3D
{
    /**
     * Params container.
     */
    public static class Params extends Plot3D.Params
    {
        /**
         * The number of divisions on the x-axis.
         */
        public int _xDiv = 1;

        /**
         * The number of divisions on the y-axis.
         */
        public int _yDiv = 1;

        /**
         * The number of divisions on the z-axis.
         */
        public int _zDiv = 1;

        /**
         * Gradient used when drawing the heatmap.
         */
        public Gradient _gradient = null;

        /**
         * Display range object for handling data on the display range/normalizations/etc.
         * If not provided, it is instantiated with nulled range and flags for dynamic range update set to true.
         */
        public DisplayRangesManager.DisplayRange _heatmapDisplayRange;

        /**
         * If true, the x-axis is customized so that every bucket has a tick (i.e., the number of ticks equals the number of divisions + ticks are centered).
         */
        public boolean _xAxisWithBoxTicks = false;

        /**
         * If true, the y-axis is customized so that every bucket has a tick (i.e., the number of ticks equals the number of divisions + ticks are centered).
         */
        public boolean _yAxisWithBoxTicks = false;

        /**
         * If true, the z-axis is customized so that every bucket has a tick (i.e., the number of ticks equals the number of divisions + ticks are centered).
         */
        public boolean _zAxisWithBoxTicks = false;

        /**
         * If true, the number of main vertical grid lines is set to match the number of buckets.
         */
        public boolean _verticalGridLinesWithBoxTicks = true;

        /**
         * If true, the number of main horizontal grid lines is set to match the number of buckets.
         */
        public boolean _horizontalGridLinesWithBoxTicks = true;


        /**
         * If true, the number of main depth grid lines (along z-axis) is set to match the number of buckets.
         */
        public boolean _depthGridLinesWithBoxTicks = true;

        /**
         * Can be used to provide a normalized used by {@link Heatmap3DLayer} to determine buckets coordinates in the normalized [0-1]
         * space (x-coordinates) -- used construct non-uniform heatmaps. If null, the default linear normalized is used.
         * Note that min/max values of the provided normalizer are always overwritten by 0/1.
         */
        public AbstractMinMaxNormalization _xBucketCoordsNormalizer = null;

        /**
         * Can be used to provide a normalized used by {@link Heatmap3DLayer} to determine buckets coordinates in the normalized [0-1]
         * space (y-coordinates) -- used construct non-uniform heatmaps. If null, the default linear normalized is used.
         * Note that min/max values of the provided normalizer are always overwritten by 0/1.
         */
        public AbstractMinMaxNormalization _yBucketCoordsNormalizer = null;

        /**
         * Can be used to provide a normalized used by {@link Heatmap3DLayer} to determine buckets coordinates in the normalized [0-1]
         * space (y-coordinates) -- used construct non-uniform heatmaps. If null, the default linear normalized is used.
         * Note that min/max values of the provided normalizer are always overwritten by 0/1.
         */
        public AbstractMinMaxNormalization _zBucketCoordsNormalizer = null;

        /**
         * Style used when rendering the buckets.
         */
        public BucketStyle _bucketStyle;

        /**
         * Default constructor. Properly adjust some fields that are not relevant to the heatmap.
         * Instantiates the heatmap display range as null with 'dynamic update' flag set to true.
         *
         */
        public Params()
        {
            this(new BucketStyle(), null);
        }

        /**
         * Default constructor. Properly adjust some fields that are not relevant to the heatmap.
         *
         * @param heatmapDisplayRange Display range object for handling data on the display range/normalizations/etc.; if not provided (null), it is instantiated with nulled range and flags for dynamic range update set to true.
         */
        public Params(DisplayRangesManager.DisplayRange heatmapDisplayRange)
        {
            this(new BucketStyle(), heatmapDisplayRange);
        }

        /**
         * Parameterized constructor. Properly adjust some fields that are not relevant to the heatmap.
         *
         * @param bucketStyle bucket style
         * @param heatmapDisplayRange Display range object for handling data on the display range/normalizations/etc.; if not provided (null), it is instantiated with nulled range and flags for dynamic range update set to true.
         *
         */
        public Params(BucketStyle bucketStyle, DisplayRangesManager.DisplayRange heatmapDisplayRange)
        {
            _drawMainGridlines = false;
            _drawAuxGridlines = false;
            _bucketStyle = bucketStyle;
            _heatmapDisplayRange = heatmapDisplayRange;
            if (_heatmapDisplayRange == null) _heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true, false);
        }
    }

    /**
     * Reference to the heatmap model extending {@link PlotModel}.
     */
    private Heatmap3DModel _HM;

    /**
     * Parametrized constructor.
     *
     * @param p params container
     */
    public Heatmap3D(Params p)
    {
        super(p);
        _name = "Heatmap 3D";
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
        Heatmap3D.Params pp = (Params) p;
        _C3D = new Plot3DController(this);
        _C = _C3D;
        _HM = new Heatmap3DModel(this, PC, new Heatmap3DLayer(pp, _PC));
        _HM._heatmap3DLayer._HM.setSortedMode(true);
        _M3D = _HM;
        _M = _HM;
        doBasicParameterizationOfPlotAndController(p);
    }

    /**
     * Fills the drawing area params used to construct the drawing area object.
     *
     * @param pD drawing area params
     * @param p  plot params container
     */
    protected void fillDrawingAreaParams(AbstractSwingComponent.Params pD, AbstractPlot.Params p)
    {
        super.fillDrawingAreaParams(pD, p);
        Heatmap3DDrawingArea.Params pD3D = (Heatmap3DDrawingArea.Params) pD;
        pD3D._heatmapLayer = _HM._heatmap3DLayer;
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
        Heatmap3DDrawingArea.Params pD = new Heatmap3DDrawingArea.Params(_PC);
        fillDrawingAreaParams(pD, p);
        Heatmap3DDrawingArea d3d = new Heatmap3DDrawingArea(pD);

        _M3D.getGLInitComponentsContainer().getComponents().add(d3d);
        _M3D.getGLInitComponentsContainer().setDrawingArea(d3d);

        Heatmap3D.Params pp = (Params) p;
        if ((pp._verticalGridLinesWithBoxTicks) || (pp._horizontalGridLinesWithBoxTicks) || (pp._depthGridLinesWithBoxTicks))
        {
            if (d3d.getPanes() != null)
            {
                for (Pane pane : d3d.getPanes())
                {
                    if (pane != null)
                    {
                        if ((pp._horizontalGridLinesWithBoxTicks) && (pane.getXTicksDataGetter() != null))
                        {
                            pane.getXTicksDataGetter().setNoTicks(pp._xDiv + 1);
                            if ((pp._xAxisWithBoxTicks) && (pp._xBucketCoordsNormalizer != null))
                                pane.getXTicksDataGetter().createForcedUnnormalizedLocationsUsingNormalizer(pp._xBucketCoordsNormalizer);
                        }
                        if ((pp._verticalGridLinesWithBoxTicks) && (pane.getYTicksDataGetter() != null))
                        {
                            pane.getYTicksDataGetter().setNoTicks(pp._yDiv + 1);
                            if ((pp._yAxisWithBoxTicks) && (pp._yBucketCoordsNormalizer != null))
                                pane.getYTicksDataGetter().createForcedUnnormalizedLocationsUsingNormalizer(pp._yBucketCoordsNormalizer);
                        }
                        if ((pp._depthGridLinesWithBoxTicks) && (pane.getZTicksDataGetter() != null))
                        {
                            pane.getZTicksDataGetter().setNoTicks(pp._zDiv + 1);
                            if ((pp._zAxisWithBoxTicks) && (pp._zBucketCoordsNormalizer != null))
                                pane.getZTicksDataGetter().createForcedUnnormalizedLocationsUsingNormalizer(pp._zBucketCoordsNormalizer);
                        }
                    }
                }
            }
        }

        if ((pp._xAxisWithBoxTicks) || (pp._yAxisWithBoxTicks) || (pp._zAxisWithBoxTicks))
        {
            if (d3d.getAxes() != null)
            {
                for (Axis3D a : d3d.getAxes())
                {
                    if (a != null)
                    {
                        ITicksDataGetter tdg = a.getTicksDataGetter();
                        float[] fn = null;
                        if ((pp._xAxisWithBoxTicks) && (a.getAssociatedDisplayRangeID() == 0))
                        {
                            fn = Heatmap2D.getForcedLocators(pp._xDiv, pp._xBucketCoordsNormalizer);
                            tdg.setNoTicks(pp._xDiv);
                        }
                        else if ((pp._yAxisWithBoxTicks) && (a.getAssociatedDisplayRangeID() == 1))
                        {
                            fn = Heatmap2D.getForcedLocators(pp._yDiv, pp._yBucketCoordsNormalizer);
                            tdg.setNoTicks(pp._yDiv);
                        }
                        else if ((pp._zAxisWithBoxTicks) && (a.getAssociatedDisplayRangeID() == 2))
                        {
                            fn = Heatmap2D.getForcedLocators(pp._zDiv, pp._zBucketCoordsNormalizer);
                            tdg.setNoTicks(pp._zDiv);
                        }
                        tdg.setForcedNormalizedLocations(fn);
                    }
                }
            }
        }

        return d3d;
    }


    /**
     * Getter for the heatmap model.
     *
     * @return heatmap model.
     */
    @Override
    public Heatmap3DModel getModel()
    {
        return _HM;
    }
}
