package tools.ivemo.heatmap.visualization;

import plot.heatmap.Heatmap2D;
import plotwrapper.AbstractPlotWrapper;
import scheme.enums.ColorFields;
import swing.RangePanel;
import tools.ivemo.heatmap.io.params.PlotParams;


/**
 * Custom panel that contains and manages Heatmap 2D and its linked range panel.
 * It is also a wrapper for the heatmap 2D standard class.
 *
 * @author MTomczyk
 */

public class HeatmapPanel2D extends AbstractPlotWrapper
{
    /**
     * Params containers
     */
    protected static class Params extends AbstractPlotWrapper.Params
    {
        /**
         * Plot params (parser's wrapper).
         */
        public final PlotParams _PP;

        /**
         * Parameterized constructor.
         *
         * @param heatmap2D reference to the plot
         * @param PP        plot params (parser's wrapper)
         */
        public Params(Heatmap2D heatmap2D, PlotParams PP)
        {
            super(heatmap2D);
            _PP = PP;
        }
    }

    /**
     * Additional range panel.
     */
    private RangePanel _rangePanel;

    /**
     * Parameterized constructor.
     *
     * @param heatmap2D reference to the plot
     * @param PP   plot params (parser's wrapper)
     */
    public HeatmapPanel2D(Heatmap2D heatmap2D, PlotParams PP)
    {
        super(new Params(heatmap2D, PP));
    }

    /**
     * Can be overwritten to instantiate some additional GUI elements.
     *
     * @param p params container
     */
    @Override
    protected void instantiateGUI(AbstractPlotWrapper.Params p)
    {
        PlotParams PP = ((HeatmapPanel2D.Params) p)._PP;
        RangePanelHeatmap2D rangePanel = new RangePanelHeatmap2D(
                (int) PP._heatmapDisplayRange.getLeft(),
                (int) (PP._heatmapDisplayRange.getRight() + 0.999999999d)); // wrap doubles
        rangePanel.setHeatmap2D((Heatmap2D) getModel().getPlot());
        rangePanel.setColors(PP._scheme.getColors(null, ColorFields.PLOT_BACKGROUND),
                PP._scheme.getColors(null, ColorFields.TITLE_FONT));
        _rangePanel = rangePanel;
    }

    /**
     * Instantiates the layout.
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(AbstractPlotWrapper.Params p)
    {
        GUIUtils.establishLayout(this, getModel().getPlot(), _rangePanel);
    }

}
