package tools.ivemo.heatmap.visualization;

import plot.heatmap.Heatmap3D;
import space.Range;
import swing.RangePanel;


/**
 * Customized range panel that allows filtering displayed 3D heatmap data.
 *
 * @author MTomczyk
 */

public class RangePanelHeatmap3D extends RangePanel
{
    /**
     * Reference to the heatmap 3D plot
     */
    private Heatmap3D _heatmap3D;

    /**
     * Parameterized constructor.
     *
     * @param minValue min value
     * @param maxValue max value
     */
    public RangePanelHeatmap3D(int minValue, int maxValue)
    {
        super(minValue, maxValue);
    }

    /**
     * Overwrites the default implementation -> updates GUI, logic, and finally sets a new display bound on the heatmap.
     *
     * @param type           caller type (MIN_TYPE/MAX_TYPE).
     * @param requestedValue newly set value
     */
    protected void requestUpdate(int type, int requestedValue)
    {
        super.requestUpdate(type, requestedValue);
        _heatmap3D.getModel().setValueFilter(new Range(_minPanel.getCurrentValue(), _maxPanel.getCurrentValue()));

    }

    /**
     * Setter for the heatmap 3D plot.
     *
     * @param heatmap3D heatmap 3D plot
     */
    void setHeatmap3D(Heatmap3D heatmap3D)
    {
        _heatmap3D = heatmap3D;
    }
}
