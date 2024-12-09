package tools.ivemo.heatmap.visualization;

import plot.heatmap.Heatmap3D;
import plotwrapper.AbstractPlotWrapper;
import scheme.enums.ColorFields;
import swing.RangePanel;
import tools.ivemo.heatmap.io.params.PlotParams;



/**
 * Custom panel that contains and manages Heatmap 3D and its linked range panel.
 * It is also a wrapper for the heatmap 3D standard class.
 *
 * @author MTomczyk
 */

public class HeatmapPanel3D extends AbstractPlotWrapper
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
         * @param heatmap3D reference to the plot
         * @param PP        plot params (parser's wrapper)
         */
        public Params(Heatmap3D heatmap3D, PlotParams PP)
        {
            super(heatmap3D);
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
     * @param heatmap3D reference to the plot
     * @param PP   plot params (parser's wrapper)
     */
    public HeatmapPanel3D(Heatmap3D heatmap3D, PlotParams PP)
    {
        super(new Params(heatmap3D, PP));
    }

    /**
     * Can be overwritten to instantiate some additional GUI elements.
     *
     * @param p params container
     */
    @Override
    protected void instantiateGUI(AbstractPlotWrapper.Params p)
    {
        PlotParams PP = ((Params) p)._PP;
        RangePanelHeatmap3D rangePanel = new RangePanelHeatmap3D(
                (int) PP._heatmapDisplayRange.getLeft(),
                (int) (PP._heatmapDisplayRange.getRight() + 0.999999999d)); // wrap doubles
        rangePanel.setHeatmap3D((Heatmap3D) getModel().getPlot());
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
