package plot.parallelcoordinate;

import component.axis.swing.AbstractAxis;
import component.axis.swing.XAxis;
import component.axis.swing.YAxis;
import component.axis.ticksupdater.FromDisplayRange;
import container.Notification;
import drmanager.DisplayRangesManager;
import plot.AbstractPlot;
import plot.Plot2D;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.FlagFields;
import scheme.enums.SizeFields;
import space.Range;

import java.text.DecimalFormat;

/**
 * Parallel coordinate plot implementation of a plot for 2D visualization.
 * Several modifications are applied to the original {@link Plot2D} to adapt it to display
 * parallel coordinate lines:
 * 1. Dimensionality: consider M-dimensional space to be displayed. The {@link drmanager.DisplayRangesManager} handles
 * the input data points as usual (these should be M-dimensional double vectors). However, the display ranges manager
 * also contains an additional M + 1 display range that is fixed at the [0, 1] interval. It is used to determine X-coordinates
 * of points being drawn. IMPORTANT NOTE: the PCP allows using custom display ranges, but they must
 * be positioned explicitly between the first M display ranges associated with the parallel coordinate lines and the last
 * display range reserved for the X-axis.
 * 2. AXES: In contrast to original 2D plot that has two axes: X, Y, this class construct M + 1 axes objects
 * as follows: [X, A0, A1, ..., AM]. The first axis is horizontal, while the last M ones are vertical axes linked to
 * each dimension.
 * 3. The layout manager is adapted to properly display all M + 1 axes (see {@link }).
 * 3. Proper data set painter ({@link dataset.painter.AbstractPainter}) should be used to illustrate parallel
 * coordinate lines.
 *
 * @author MTomczyk
 */


public class ParallelCoordinatePlot2D extends Plot2D
{
    /**
     * Params container.
     */
    public static class Params extends Plot2D.Params
    {
        /**
         * The original space dimensionality (at least one).
         */
        protected final int _dimensions;

        /**
         * Vertical axes titles can be provided here. Null values are accepted -> such titles are skipped.
         * The number of elements is supposed to equal the dimensionality of the input data.
         */
        public String[] _axesTitles = null;

        /**
         * If true, axis overlapping ticks are disabled (overlapping ticks = all but first vertical axis -> not extreme ticks).
         */
        public boolean _disableOverlappingTicks = false;

        /**
         * Parameterized constructor.
         *
         * @param dimensions the number of parallel coordinate lines (i.e., dimensions)
         */
        public Params(int dimensions)
        {
            super();
            _dimensions = dimensions;
            _drawAuxGridlines = false;
            _drawMainGridlines = false;
        }
    }

    /**
     * Parametrized constructor.
     *
     * @param p params container
     */
    public ParallelCoordinatePlot2D(Plot2D.Params p)
    {
        super(p);
        _name = "Parallel Coordinate Plot 2D";
    }

    /**
     * Constructs a default display ranges manager automatically
     * when the {@link drmanager.DisplayRangesManager.Params} were not provided.
     *
     * @param p params container
     * @return instance of {@link DisplayRangesManager}.
     */
    @Override
    protected DisplayRangesManager constructDefaultDisplayRangesManager(AbstractPlot.Params p)
    {
        Params pp = (Params) p;
        DisplayRangesManager.Params pDRM = new DisplayRangesManager.Params();
        pDRM._DR = new DisplayRangesManager.DisplayRange[pp._dimensions + 1];
        pDRM._attIdx_to_drIdx = new Integer[pp._dimensions];
        for (int i = 0; i < pp._dimensions; i++)
        {
            pDRM._DR[i] = new DisplayRangesManager.DisplayRange(null, true, false);
            pDRM._attIdx_to_drIdx[i] = i;
        }

        pDRM._DR[pp._dimensions] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false, false);

        return new DisplayRangesManager(pDRM);
    }

    /**
     * Can be called by the extending classes to apply some custom modifications to the input scheme.
     *
     * @param p params container
     */
    @Override
    protected void applySchemeCustomModifications(AbstractPlot.Params p)
    {
        //p._scheme._color.put(ColorFields.DRAWING_AREA_BORDER, null);
    }


    /**
     * Auxiliary method for instantiating the layout manager.
     */
    @Override
    protected void instantiateLayoutManager(AbstractPlot.Params p)
    {
        _layoutManager = new PCPLayoutManager(_PC, getComponentsContainer());
        _layoutManager.addElementsInCorrectOrder(this);
    }

    /**
     * Auxiliary method for instantiating plot axes.
     *
     * @param p params container
     * @return drawing area object
     */
    @Override
    protected AbstractAxis[] createAxes(AbstractPlot.Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M.getPlotID() + "]: get axes method call");

        Params pp = (Params) p;
        assert pp._dimensions > 0;

        int cnt = 0;
        if (p._drawXAxis) cnt++;
        if (p._drawYAxis) cnt += pp._dimensions;

        AbstractAxis[] axes = new AbstractAxis[cnt];

        cnt = 0;
        if (p._drawXAxis)
        {
            XAxis.Params pX = new XAxis.Params(_PC);
            pX._title = p._xAxisTitle;
            pX._ticksDataGetter = new FromDisplayRange(_PC.getDisplayRangesManager().getLastDisplayRange(), pp._axesTitles, pp._dimensions);
            XAxis axis = new XAxis(pX);
            axes[cnt++] = axis;
        }

        if (p._drawYAxis)
        {
            for (int i = 0; i < pp._dimensions; i++)
            {
                YAxis.Params pY = new YAxis.Params(_PC);
                pY._title = null;
                pY._ticksDataGetter = new FromDisplayRange(_PC.getDisplayRangesManager().getDisplayRange(i), 5,
                        new DecimalFormat("0.##E0"));
                YAxis axis = new YAxis(pY);
                axis.getSurpassedAlignments().put(AlignFields.AXIS_Y, Align.LEFT);
                axis.getSurpassedSizes().put(SizeFields.AXIS_Y_MAIN_LINE_WIDTH_FIXED, 1.0f);
                axis.getSurpassedFlags().put(FlagFields.AXIS_Y_MAIN_LINE_USE_RELATIVE_WIDTH, false);

                if ((pp._disableOverlappingTicks) && (i > 0)) pY._ticksDataGetter.setNoTicks(2);

                axes[cnt++] = axis;
            }
        }

        return axes;
    }

    /**
     * Creates axes' titles that follows the "prefix + number" format (e.g., f1, f2, f3, etc.).
     *
     * @param prefix prefix used
     * @param M      the number of titles to construct
     * @return titles
     */
    public static String[] getAxesTitlesAsSequence(String prefix, int M)
    {
        String[] t = new String[M];
        for (int i = 0; i < M; i++) t[i] = prefix + (i + 1);
        return t;
    }
}
