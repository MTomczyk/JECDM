package dataset;

import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import dataset.painter.IDS;
import dataset.painter.IPainter;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import drmanager.DisplayRangesManager;
import listeners.FrameListener;
import plot.AbstractPlot;
import space.Dimension;
import thread.swingworker.EventTypes;

import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Abstract class representation of a data set.
 *
 * @author MTomczyk
 */
public abstract class AbstractDataSet implements IDataSet
{
    /**
     * Data set name.
     */
    protected String _name = "";

    /**
     * Current data to be displayed.
     */
    protected final Data _data;

    /**
     * Reference to the global container.
     */
    protected GlobalContainer _GC;

    /**
     * Reference to the plot container.
     */
    protected PlotContainer _PC;

    /**
     * Data set painter.
     */
    protected IPainter _painter;

    /**
     * If true, the data set is displayable on legend, false otherwise.
     */
    protected volatile boolean _displayableOnLegend = true;

    /**
     * If true, the data set rendering is skipped
     */
    protected volatile boolean _skipRendering = false;

    /**
     * Auxiliary mask that tell whether the update of the i-th display range should be skipped.
     * Can return null (not used). Obviously, the mask has no effect when updating display ranges is globally disabled.
     */
    protected boolean[] _skipDisplayRangeUpdateMask;

    /**
     * This method creates a new data set object.
     * All the fields are cloned except for the data to be depicted.
     * This data is provided as the input. The method may be useful when, e.g., using plots to animate the data
     * (frequent calls for {@link plot.PlotModel#setDataSets(ArrayList, boolean)} would be required).
     *
     * @param data input data to be displayed.
     */
    public IDataSet wrapAround(LinkedList<double[][]> data)
    {
        IPainter painter = _painter.getEmptyClone();
        DataSet DS = new DataSet(_name, new Data(data), painter);
        DS._GC = _GC;
        DS._PC = _PC;
        DS._skipRendering = _skipRendering;
        DS._displayableOnLegend = _displayableOnLegend;
        if (_skipDisplayRangeUpdateMask == null) DS.setSkipDisplayRangesUpdateMasks(null);
        else DS.setSkipDisplayRangesUpdateMasks(_skipDisplayRangeUpdateMask.clone());
        return DS;
    }


    /**
     * Parameterized constructor.
     *
     * @param data    data to be rendered
     * @param painter object used for data rendering
     */
    public AbstractDataSet(Data data, IPainter painter)
    {
        _data = data;
        _painter = painter;
    }


    /**
     * Getter for the input raw (unprocessed) data to be displayed.
     *
     * @return data
     */
    @Override
    public Data getData()
    {
        return _data;
    }

    /**
     * Returns an auxiliary mask that tell whether the update of the i-th display range should be skipped.
     * Can return null (not used). Obviously, the mask has no effect when updating display ranges is globally disabled.
     *
     * @return mask (i-th element = true -> the update of i-th display range is skipped).
     */
    @Override
    public boolean[] getSkipDisplayRangesUpdateMasks()
    {
        return _skipDisplayRangeUpdateMask;
    }

    /**
     * It can be implemented to set auxiliary mask that tell whether the update of the i-th display range should be skipped.
     * Can be null (not used).
     *
     * @param mask mask (i-th element = true -> the update of i-th display range is skipped).
     */
    @Override
    public void setSkipDisplayRangesUpdateMasks(boolean[] mask)
    {
        _skipDisplayRangeUpdateMask = mask;
    }

    /**
     * Setter for the containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    @Override
    public void setContainers(GlobalContainer GC, PlotContainer PC)
    {
        _GC = GC;
        _PC = PC;
        _painter.setContainers(GC, PC);
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': containers set");
    }


    /**
     * IDS = Internal Data Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data {@link IDS} that is
     * easy-to-be-rendered. First level IDS corresponds to normalization of data points as imposed by display ranges.
     * The method should be called on the init phase (when the display ranges are fixed) are called by {@link AbstractPlot}
     * when the display ranges changed after the data update.
     *
     * @param DRM       display range manager maintained by the plot
     * @param eventType event type that triggered the method execution
     */
    @Override
    public void updateFirstLevelIDS(DisplayRangesManager DRM, EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': update first level IDS method called (event type = " + eventType.toString() + ")");
        _painter.updateFirstLevelIDS(DRM, eventType);
    }

    /**
     * Method called to indicate that the data processing began. It locks references, etc.
     *
     * @param fromFirstLevel if true, the processing is executed from the beginning
     */
    @Override
    public void beginDataProcessing(boolean fromFirstLevel)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': begin data processing method called");
        _painter.beginDataProcessing(fromFirstLevel);
    }

    /**
     * Method that should be called after processing IDS (releases some locked references, etc.)
     */
    @Override
    public void finishDataProcessing()
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': finish data processing method called");
        _painter.finishDataProcessing();
    }

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data {@link IDS} that is
     * easy-to-be-rendered. Second level IDS corresponds to normalization of data points as imposed by the drawing area coordinates.
     * The method should be called when the drawing area changes (see the top-level {@link FrameListener#componentResized(ComponentEvent)} )
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @param eventType  event type that triggered the method execution
     */
    @Override
    public void updateSecondLevelIDS(Dimension[] dimensions, EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': update second level IDS method called (event type = " + eventType.toString() + ")");
        _painter.updateSecondLevelIDS(dimensions, eventType);
    }

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Can be called to construct third-level IDS (e.g., data for VBOs when rendering in 3D)
     *
     * @param eventType event type that triggered the method execution
     */
    @Override
    public void updateThirdLevelIDS(EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': update third level IDS method called (event type = " + eventType.toString() + ")");
        _painter.updateThirdLevelIDS(eventType);
    }

    /**
     * Main method for drawing a data set.
     *
     * @param r rendering context
     */
    @Override
    public void draw(Object r)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': draw method called");
        if (!_skipRendering) _painter.draw(r);
    }


    /**
     * Returns marker style.
     *
     * @return marker style
     */
    @Override
    public MarkerStyle getMarkerStyle()
    {
        return _painter.getMarkerStyle();
    }

    /**
     * Returns line style.
     *
     * @return line style
     */
    @Override
    public LineStyle getLineStyle()
    {
        return _painter.getLineStyle();
    }

    /**
     * Getter for data set name.
     *
     * @return data set name
     */
    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * If true, the data set is displayable on the legend.
     *
     * @return true if the data set is displayable on legend, false otherwise
     */
    @Override
    public boolean isDisplayableOnLegend()
    {
        return _displayableOnLegend;
    }

    /**
     * Setter for the "displayable" parameter.
     *
     * @param displayableOnLegend if true the data set is displayable on legend (will be rendered), false otherwise
     */
    @Override
    public void setDisplayableOnLegend(boolean displayableOnLegend)
    {
        _displayableOnLegend = displayableOnLegend;
    }

    /**
     * Getter for the "skip rendering" parameter. If true, the data set rendering is skipped (but the data is still processed).
     *
     * @return if true the data set rendering is skipped
     */
    @Override
    public boolean isRenderingSkipped()
    {
        return _skipRendering;
    }

    /**
     * Setter for the "skip rendering" parameter. If true, the data set rendering is skipped (but the data is still processed).
     * Can be used to efficiently manipulate which data is currently (not) displayed.
     *
     * @param skipRendering if true the data set rendering is skipped
     */
    @Override
    public void setSkipRendering(boolean skipRendering)
    {
        _skipRendering = skipRendering;
    }

    /**
     * Getter for the painter object.
     *
     * @return painter object
     */
    public IPainter getPainter()
    {
        return _painter;
    }


    /**
     * Can be called to clear the data.
     */
    @Override
    public void dispose()
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': dispose method called");
        _painter.dispose();
    }

}
