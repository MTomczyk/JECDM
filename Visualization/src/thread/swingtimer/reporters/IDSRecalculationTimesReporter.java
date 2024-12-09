package thread.swingtimer.reporters;

import container.GlobalContainer;
import container.Notification;
import dataset.IDataSet;
import plot.AbstractPlot;
import plotwrapper.AbstractPlotWrapper;

import java.util.ArrayList;

/**
 * Prints reports on plots IDS (internal data structures supporting efficient rendering) recalculation times.
 *
 * @author MTomczyk
 */


public class IDSRecalculationTimesReporter extends AbstractPlotsWrapperReporter
{
    /**
     * Parameterized constructor (reports every one second).
     *
     * @param GC global container (shared object; stores references, provides various functionalities)
     */
    public IDSRecalculationTimesReporter(GlobalContainer GC)
    {
        super(GC, 1);
    }

    /**
     * Parameterized constructor.
     *
     * @param GC  global container (shared object; stores references, provides various functionalities)
     * @param fps frames per second (action execution frequency)
     */
    public IDSRecalculationTimesReporter(GlobalContainer GC, int fps)
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
            System.out.println("IDS recalculation times reporter: reference to global container is not provided");
        else if (_GC.getPlotsWrapper() == null)
            Notification.printNotification(_GC, null, "IDS recalculation times reporter: reference to plots wrapper is not provided", true);
        else if (_GC.getPlotsWrapper().getModel()._wrappers == null)
            Notification.printNotification(_GC, null, "IDS recalculation times reporter: wrappers are not available", true);
        else
        {
            for (AbstractPlotWrapper w : _GC.getPlotsWrapper().getModel()._wrappers)
            {
                if (w == null) continue;
                AbstractPlot plot = w.getModel().getPlot();
                if (plot == null)
                    Notification.printNotification(_GC, null, "IDS recalculation times reporter: plot for one of the wrappers is not available", true);
                else
                {
                    ArrayList<IDataSet> dss = plot.getModel().getDataSets();
                    if (dss == null)
                    {
                        Notification.printNotification(_GC, null, "   " + plot.getName() + " [id = " + plot.getModel().getPlotID() + "]: plot has no data sets", true);
                        continue;
                    }

                    for (IDataSet dataSet : dss)
                    {
                        Notification.printNotification(_GC, null, "      times for " + dataSet.getName() + ":", true);
                        for (int i = 0; i < 3; i++)
                        {
                            Long time = dataSet.getPainter().getRecalculationTime(i);
                            Double ms = null;
                            Double fps = null;
                            if ((time != null) && (time >= 0))
                            {
                                ms = time / 1000000.0d;
                                fps = 1000000000.0d / (time);
                            }
                            Notification.printNotification(_GC, null, "         IDS level = " + (i + 1) + ": recalculation time = " + String.format("%.2f", ms) + " [ms]; (equivalent to " + String.format("%.2f", fps) + " [FPS])", true);

                        }
                    }
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
