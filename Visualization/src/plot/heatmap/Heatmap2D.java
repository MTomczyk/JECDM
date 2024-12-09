package plot.heatmap;

import color.gradient.Gradient;
import component.axis.swing.AbstractAxis;
import component.axis.ticksupdater.ITicksDataGetter;
import component.drawingarea.AbstractDrawingArea;
import container.Notification;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.PlotController;
import plot.PlotModel;
import space.normalization.minmax.AbstractMinMaxNormalization;

/**
 * Heatmap implementation of a plot for 2D visualization.
 *
 * @author MTomczyk
 */


public class Heatmap2D extends Plot2D
{
    /**
     * Params container.
     */
    public static class Params extends Plot2D.Params
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
         * Gradient used when drawing the heatmap.
         */
        public Gradient _gradient = null;

        /**
         * Display range object for handling data on the display range/normalizations/etc.
         * If not provided, it is instantiated with nulled range and flags for dynamic range update set to true.
         */
        public DisplayRangesManager.DisplayRange _heatmapDisplayRange = null;

        /**
         * If true, the x-axis is customized so that every bucket has a tick (i.e., the number of ticks equals the number of divisions + ticks are centered).
         */
        public boolean _xAxisWithBoxTicks = false;

        /**
         * If true, the y-axis is customized so that every bucket has a tick (i.e., the number of ticks equals the number of divisions + ticks are centered).
         */
        public boolean _yAxisWithBoxTicks = false;

        /**
         * If true, the number of main vertical grid lines is set to match the number of buckets.
         */
        public boolean _verticalGridLinesWithBoxTicks = true;

        /**
         * If true, the number of main horizontal grid lines is set to match the number of buckets.
         */
        public boolean _horizontalGridLinesWithBoxTicks = true;

        /**
         * Can be used to provide a normalized used by {@link Heatmap2DLayer} to determine buckets coordinates in the normalized [0-1]
         * space (x-coordinates) -- used construct non-uniform heatmaps. If null, the default linear normalized is used.
         * Note that min/max values of the provided normalizer are always overwritten by 0/1.
         */
        public AbstractMinMaxNormalization _xBucketCoordsNormalizer = null;

        /**
         * Can be used to provide a normalized used by {@link Heatmap2DLayer} to determine buckets coordinates in the normalized [0-1]
         * space (y-coordinates) -- used construct non-uniform heatmaps. If null, the default linear normalized is used.
         * Note that min/max values of the provided normalizer are always overwritten by 0/1.
         */
        public AbstractMinMaxNormalization _yBucketCoordsNormalizer = null;


        /**
         * Default constructor. Properly adjust some fields that are not relevant to the heatmap.
         */
        public Params()
        {
            _drawMainGridlines = false;
            _drawAuxGridlines = false;
        }
    }

    /**
     * Reference to the heatmap 2D layer.
     */
    private Heatmap2DLayer _heatmap2DLayer;

    /**
     * Reference to the heatmap model extending {@link PlotModel}.
     */
    private Heatmap2DModel _HM;

    /**
     * Parametrized constructor.
     *
     * @param p params container
     */
    public Heatmap2D(Params p)
    {
        super(p);
        _name = "Heatmap 2D";
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
        Heatmap2D.Params pp = (Params) p;
        _C = new PlotController(this);
        _heatmap2DLayer = new Heatmap2DLayer(pp, _PC);
        _HM = new Heatmap2DModel(this, PC, _heatmap2DLayer);
        _M = _HM;
        doBasicParameterizationOfPlotAndController(p);
    }


    /**
     * Auxiliary method for instantiating plot drawing area (2D).
     *
     * @param p params container
     * @return drawing area object
     */
    protected AbstractDrawingArea createDrawingArea(AbstractPlot.Params p)
    {
        Heatmap2D.Params pp = (Params) p;

        Notification.printNotification(_GC, _PC, _name + " [id = " + _M.getPlotID() + "]: get drawing area method call");
        Heatmap2DDrawingArea.Params pD = new Heatmap2DDrawingArea.Params(_PC, _heatmap2DLayer);
        fillDrawingAreaParams(pD, p);
        Heatmap2DDrawingArea hd2d = new Heatmap2DDrawingArea(pD);

        // surpass gridlines
        if ((hd2d.getMainGrid() != null) && (pp._verticalGridLinesWithBoxTicks))
            hd2d.getMainGrid().getVerticalTicksDataGetter().setNoTicks(pp._xDiv + 1);
        if ((hd2d.getMainGrid() != null) && (pp._horizontalGridLinesWithBoxTicks))
            hd2d.getMainGrid().getHorizontalTicksDataGetter().setNoTicks(pp._yDiv + 1);

        if ((hd2d.getMainGrid() != null) && (pp._xBucketCoordsNormalizer != null))
            hd2d.getMainGrid().getVerticalTicksDataGetter().createForcedUnnormalizedLocationsUsingNormalizer(pp._xBucketCoordsNormalizer);
        if ((hd2d.getMainGrid() != null) && (pp._yBucketCoordsNormalizer != null))
            hd2d.getMainGrid().getHorizontalTicksDataGetter().createForcedUnnormalizedLocationsUsingNormalizer(pp._yBucketCoordsNormalizer);

        return hd2d;
    }

    /**
     * Auxiliary method for instantiating plot axes.
     *
     * @param p params container
     * @return drawing area object
     */
    protected AbstractAxis[] createAxes(AbstractPlot.Params p)
    {
        AbstractAxis[] axes = super.createAxes(p);

        Heatmap2D.Params pp = (Params) p;

        if (pp._xAxisWithBoxTicks)
        {
            ITicksDataGetter tdg = axes[0].getTicksDataGetter();
            float[] fn = Heatmap2D.getForcedLocators(pp._xDiv, pp._xBucketCoordsNormalizer);
            tdg.setNoTicks(pp._xDiv);
            tdg.setForcedNormalizedLocations(fn);
        }

        if (pp._yAxisWithBoxTicks)
        {
            ITicksDataGetter tdg = axes[1].getTicksDataGetter();
            float[] fn = Heatmap2D.getForcedLocators(pp._yDiv, pp._yBucketCoordsNormalizer);
            tdg.setNoTicks(pp._yDiv);
            tdg.setForcedNormalizedLocations(fn);
        }

        return axes;
    }

    /**
     * Auxiliary getter for force tick locations when they are expected to match heatmap boxes.
     *
     * @param div        number of dimension divisions
     * @param normalizer normalizer used to scale the tick locations
     * @return forced tick location (in normalized space)
     */
    public static float[] getForcedLocators(int div, AbstractMinMaxNormalization normalizer)
    {
        float[] fn = new float[div];
        float off = 1.0f / div;
        float half = (1.0f / div) / 2.0f;
        for (int i = 0; i < div; i++)
        {
            if (normalizer != null) fn[i] = (float) normalizer.getNormalized(off * i + half);
            else fn[i] = off * i + half;
        }
        return fn;
    }

    /**
     * Getter for the heatmap model.
     *
     * @return heatmap model.
     */
    @Override
    public Heatmap2DModel getModel()
    {
        return _HM;
    }
}
