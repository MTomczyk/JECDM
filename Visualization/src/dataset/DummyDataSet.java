package dataset;

import container.Notification;
import dataset.painter.Painter2D;
import drmanager.DisplayRangesManager;
import space.Dimension;
import thread.swingworker.EventTypes;

import java.util.LinkedList;

/**
 * Dummy data set representation (for debugging).
 *
 * @author MTomczyk
 */
public class DummyDataSet extends AbstractDataSet implements IDataSet
{
    /**
     * Parameterized constructor.
     *
     * @param name data set name
     * @param data data
     */
    public DummyDataSet(String name, double[][] data)
    {
        super(new DataSet.Params("Dummy", new Data(data), new Painter2D(new Painter2D.Params(null, null, null))));
        _name = name;
    }

    /**
     * Not implemented.
     *
     * @param data null
     */
    @Override
    public IDataSet wrapAround(LinkedList<double[][]> data)
    {
        return null;
    }

    /**
     * The method does nothing.
     *
     * @param DRM       display range manager maintained by the plot
     * @param eventType event type that triggered the method execution
     */
    @Override
    public void updateFirstLevelIDS(DisplayRangesManager DRM, EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': update first level IDS method called (event type = " + eventType.toString() + ")");
    }

    /**
     * The method does nothing.
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @param eventType  event type that triggered the method execution
     */
    @Override
    public void updateSecondLevelIDS(Dimension[] dimensions, EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': update second level IDS method called (event type = " + eventType.toString() + ")");
    }

    /**
     * The method does nothing.
     *
     * @param r rendering context
     */
    @Override
    public void draw(Object r)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': draw method called");
    }


}
