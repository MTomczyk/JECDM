package thread.swingtimer.reporters;

import component.drawingarea.AbstractDrawingArea;
import container.GlobalContainer;
import container.Notification;
import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;
import statistics.movingaverage.MovingAverageLong;

/**
 * Prints reports on final render generation times (time needed to generate a final render for {@link component.drawingarea.AbstractDrawingArea}).
 *
 * @author MTomczyk
 */


public class RenderGenerationTimesReporter extends AbstractPlotsWrapperReporter
{
    /**
     * Parameterized constructor (reports every one second).
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     */
    public RenderGenerationTimesReporter(GlobalContainer GC)
    {
        super(GC, 1);
    }

    /**
     * Parameterized constructor.
     *
     * @param GC  global container (shared object; stores references, provides various functionalities)
     * @param fps frames per second (action execution frequency).
     */
    public RenderGenerationTimesReporter(GlobalContainer GC, int fps)
    {
        super(GC, fps);
    }

    /**
     * Reporter main action.
     */
    @Override
    protected void executeAction()
    {
        if (_GC == null)
            System.out.println("Render generation times reporter: reference to global container is not provided");
        else if (_GC.getPlotsWrapper() == null)
            Notification.printNotification(_GC, null, "Render generation times reporter: reference to plots wrapper is not provided", true);
        else if (_GC.getPlotsWrapper().getModel()._wrappers == null)
            Notification.printNotification(_GC, null, "Render generation times reporter: wrappers are not available", true);
        else
        {
            for (AbstractPlotWrapper w : _GC.getPlotsWrapper().getModel()._wrappers)
            {
                if (w == null) continue;
                AbstractPlot plot = w.getModel().getPlot();
                if (plot == null)
                    Notification.printNotification(_GC, null, "Render generation times reporter: plot for one of the wrappers is not available", true);
                else
                {
                    AbstractDrawingArea drawingArea = plot.getComponentsContainer().getDrawingArea();
                    MovingAverageLong at = drawingArea.getRenderGenerationTimes();

                    if (at == null)
                    {
                        Notification.printNotification(_GC, null, "   " + plot.getName() + " [id = " + plot.getModel().getPlotID() + "]: plot drawing area does not measure render generation times", true);
                        continue;
                    }

                    Long time = at.getLastEntry();
                    Double ms = null;
                    Double fps = null;
                    if ((time != null) && (time >= 0))
                    {
                        ms = time / 1000000.0d;
                        fps = 1000000000.0d / (time);
                    }
                    Notification.printNotification(_GC, null, "   " + plot.getName() + " [id = " + plot.getModel().getPlotID() + "]: render generation time = " + String.format("%.2f", ms) + " [ms]; (equivalent to " + String.format("%.2f", fps) + " [FPS])", true);

                }
            }
        }

    }

    /**
     * Can be called to dispose the data.
     */
    public void dispose()
    {
        super.dispose();
        Notification.printNotification(_GC, null, "IDS recalculation times: dispose method called");
    }
}
