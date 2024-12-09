package plotwrapper;

import container.Notification;
import plot.AbstractPlot;
import utils.Projection;

import java.awt.*;

/**
 * Implementation of {@link AbstractPlotWrapper}. It places the wrapped plot centrally and shrinks it using the provided factors.
 *
 * @author MTomczyk
 */
public class PlotShrinkWrapper extends AbstractPlotWrapper
{
    /**
     * Shrink (percent) value for width. Interpretation of the value: 0% = 0.0 = object disappears; 100% = 1.0 = original size.
     */
    private final float _sw;

    /**
     * Shrink (percent) value for height. Interpretation of the value: 0% = 0.0 = object disappears; 100% = 1.0 = original size.
     */
    private final float _sh;

    /**
     * Parameterized constructor.
     *
     * @param plot plot to be displayed.
     * @param sw   shrink (percent) value for width
     * @param sh   shrink (percent) value for height
     */
    public PlotShrinkWrapper(AbstractPlot plot, float sw, float sh)
    {
        super(new Params(plot));
        _sw = sw;
        _sh = sh;
        assert Float.compare(sw, 0.0f) >= 0;
        assert Float.compare(sh, 0.0f) >= 0;
    }

    /**
     * Instantiates the layout.
     *
     * @param p params container
     */
    @Override
    protected void instantiateLayout(Params p)
    {
        setLayout(null);
        add(_M._plot);
    }

    /**
     * Can be called to update the layout (typically called on the window resized event).
     */
    @Override
    public void updateLayout()
    {
        Notification.printNotification(_M._GC, null, "Plot shrink wrapper [id = " + _M.getPlotID() + "]: update layout method called");

        float w = getWidth() * _sw;
        float h = getHeight() * _sh;

        float x = getWidth() / 2.0f - w / 2;
        float y = getHeight() / 2.0f - h / 2;

        int ix = Projection.getP(x);
        int iy = Projection.getP(y);
        int iw = Projection.getP(w);
        int ih = Projection.getP(h);

        _M._plot.setBounds(ix, iy, iw, ih);
        _M._plot.setPrimaryDrawingArea(ix, iy, iw, ih);
        _M._plot.setPreferredSize(new Dimension(iw, ih));
    }
}
