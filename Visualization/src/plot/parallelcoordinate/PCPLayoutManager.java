package plot.parallelcoordinate;

import component.axis.swing.AbstractAxis;
import container.ComponentsContainer;
import container.PlotContainer;
import layoutmanager.BaseLayoutManager;
import layoutmanager.ILayoutManager;
import utils.Projection;

/**
 * Organizes the layout as {@link BaseLayoutManager}.
 * However, it organizes all vertical axes along the X-axis.
 *
 * @author MTomczyk
 */


public class PCPLayoutManager extends BaseLayoutManager implements ILayoutManager
{
    /**
     * Parameterized constructor.
     *
     * @param PC plot container: allows easy access to various plot-related components/functionalities
     * @param CC container for various plot components
     */
    public PCPLayoutManager(PlotContainer PC, ComponentsContainer CC)
    {
        super(PC, CC);
    }


    /**
     * Auxiliary method executing post-positioning (overwrites previous positioning results).
     * @param x plot x-coordinate
     * @param y plot y-coordinate
     * @param w plot width
     * @param h plot height
     * @param sx segments x-coordinates (3 columns)
     * @param sy segments y-coordinates (3 rows)
     * @param sw segments widths (3 columns)
     * @param sh segments heights (3 rows)
     */
    @Override
    protected void doPostPositioning(int x, int y, int w, int h, int [] sx, int [] sy, int [] sw, int [] sh)
    {
        super.doPostPositioning(x, y, w, h, sx, sy, sw, sh);

        AbstractAxis XA = _CC.getAxes()[0];
        float [] tl = XA.getTicksDataGetter().getTicksLocations().clone();

        assert tl.length == _CC.getAxes().length - 1;

        //--- iterate over all vertical axes
        for (int i = 1; i < _CC.getAxes().length; i++)
        {
            AbstractAxis axis = _CC.getAxes()[i];
            float ax = tl[i - 1] * (sw[1] - 1.0f);
            axis.setLocationAndSize(sx[0], sy[0], w, h);
            axis.setPrimaryDrawingArea(sx[1], sy[1], Projection.getP(ax) + 1, sh[1]);
        }
    }
}
