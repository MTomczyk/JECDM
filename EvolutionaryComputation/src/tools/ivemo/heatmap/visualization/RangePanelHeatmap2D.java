package tools.ivemo.heatmap.visualization;

import plot.heatmap.Heatmap2D;
import space.Range;
import swing.RangePanel;


/**
 * Customized range panel that allows filtering displayed 2D heatmap data.
 *
 * @author MTomczyk
 */

public class RangePanelHeatmap2D extends RangePanel
{
    /**
     * Reference to the heatmap 2D plot
     */
    private Heatmap2D _heatmap2D;

    /**
     * Parameterized constructor.
     *
     * @param minValue min value
     * @param maxValue max value
     */
    public RangePanelHeatmap2D(int minValue, int maxValue)
    {
        super(minValue, maxValue);
    }

    /**
     * Overwrites the default implementation -> updates GUI, logic, and finally sets a new display bound on the heatmap.
     *
     * @param type           caller type (MIN_TYPE/MAX_TYPE).
     * @param requestedValue newly set value
     */
    @Override
    protected void requestUpdate(int type, int requestedValue)
    {
        super.requestUpdate(type, requestedValue);
        //_heatmap2D.getModel().setValueFilterInTheNormalizedSpace(_minPanel.getCurrentValue(), _maxPanel.getCurrentValue());
        _heatmap2D.getModel().setValueFilter(new Range(_minPanel.getCurrentValue(), _maxPanel.getCurrentValue()));
    }

    /**
     * Setter for the heatmap 2D plot.
     *
     * @param heatmap2D heatmap 2D plot
     */
    void setHeatmap2D(Heatmap2D heatmap2D)
    {
        _heatmap2D = heatmap2D;
    }
}
