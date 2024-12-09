package plot;

import component.AbstractSwingComponent;
import component.axis.swing.AbstractAxis;
import component.axis.swing.XAxis;
import component.axis.swing.YAxis;
import component.axis.ticksupdater.FromDisplayRange;
import component.drawingarea.AbstractDrawingArea;
import component.drawingarea.DrawingArea2D;
import container.Notification;

import java.text.DecimalFormat;

/**
 * Default implementation of a plot for 2D visualization.
 *
 * @author MTomczyk
 */

public class Plot2D extends AbstractPlot
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPlot.Params
    {
        /**
         * Default constructor.
         */
        public Params()
        {
            super();
        }

    }

    /**
     * Parametrized constructor.
     *
     * @param p params container
     */
    public Plot2D(Params p)
    {
        super(p);
        _name = "Plot 2D";
    }

    /**
     * Fills the drawing area params used to construct the drawing area object.
     *
     * @param pD drawing area params
     * @param p plot params container
     */
    @Override
    protected void fillDrawingAreaParams(AbstractSwingComponent.Params pD, AbstractPlot.Params p)
    {
        pD._PC = _PC;
        pD._drawMainGridlines = p._drawMainGridlines;
        pD._drawAuxGridlines = p._drawAuxGridlines;
        pD._clipDrawingArea = p._clipDrawingArea;
        pD._measureOffscreenGenerationTimes = p._measureOffscreenGenerationTimes;
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
        DrawingArea2D.Params pD = new DrawingArea2D.Params(_PC);
        fillDrawingAreaParams(pD, p);
        return new DrawingArea2D(pD);
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
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: create axes method call");

        int cnt = 0;
        if (p._drawXAxis) cnt++;
        if (p._drawYAxis) cnt++;

        AbstractAxis[] axes = new AbstractAxis[cnt];
        cnt = 0;

        if (p._drawXAxis)
        {
            XAxis.Params pX = new XAxis.Params(_PC);
            pX._title = p._xAxisTitle;
            pX._ticksDataGetter = new FromDisplayRange(_PC.getDisplayRangesManager().getDisplayRange(0), 5,
                    new DecimalFormat("0.##E0"));
            axes[cnt++] = new XAxis(pX);
        }

        if (p._drawYAxis)
        {
            YAxis.Params pY = new YAxis.Params(_PC);
            pY._title = p._yAxisTitle;
            pY._ticksDataGetter = new FromDisplayRange(_PC.getDisplayRangesManager().getDisplayRange(1), 5,
                    new DecimalFormat("0.##E0"));
            axes[cnt] = new YAxis(pY);
        }

        return axes;
    }

}
