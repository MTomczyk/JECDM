package dataset;

import container.GlobalContainer;
import container.Notification;
import container.PlotContainer;
import dataset.painter.IDS;
import dataset.painter.IPainter;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import drmanager.DisplayRangesManager;
import listeners.FrameListener;
import plot.AbstractPlot;
import space.Dimension;
import thread.swingworker.EventTypes;

import java.awt.event.ComponentEvent;

/**
 * Abstract class representation of a data set.
 *
 * @author MTomczyk
 */
public abstract class AbstractDataSet implements IDataSet
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Data to be visualized.
         */
        public Data _data;

        /**
         * Painter used to depict the data.
         */
        protected IPainter _painter;

        /**
         * Data set name.
         */
        public String _name;

        /**
         * Data set legend label (if null, the regular name is used).
         */
        public String _legendLabel = null;

        /**
         * If true, the data set is displayable on legend, false otherwise.
         */
        public volatile boolean _displayableOnLegend = true;

        /**
         * If true, the data set rendering is skipped
         */
        public volatile boolean _skipRendering = false;

        /**
         * Auxiliary mask that tell whether the update of the i-th display range should be skipped.
         * Can return null (not used). Obviously, the mask has no effect when updating display ranges is globally disabled.
         */
        public boolean[] _skipDisplayRangeUpdateMask = null;

        /**
         * Flag indicating if the IDS updates should be skipped.
         */
        public boolean _skipIDSUpdates = false;

        /**
         * Parameterized constructor.
         *
         * @param name    data set name
         * @param data    data to be visualized
         * @param painter painter used to depict the data
         */
        public Params(String name, Data data, IPainter painter)
        {
            _data = data;
            _painter = painter;
            _name = name;
        }
    }

    /**
     * Data set name.
     */
    protected String _name;

    /**
     * Data set legend label (if null, the name is used).
     */
    protected String _legendLabel;

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
    protected volatile boolean _displayableOnLegend;

    /**
     * If true, the data set rendering is skipped
     */
    protected volatile boolean _skipRendering;

    /**
     * Auxiliary mask that tell whether the update of the i-th display range should be skipped.
     * Can return null (not used). Obviously, the mask has no effect when updating display ranges is globally disabled.
     */
    protected boolean[] _skipDisplayRangeUpdateMask;

    /**
     * Flag indicating if the IDS updates should be skipped.
     */
    protected boolean _skipIDSUpdates;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractDataSet(Params p)
    {
        _data = p._data;
        _painter = p._painter;
        _name = p._name;
        _legendLabel = p._legendLabel;
        _skipDisplayRangeUpdateMask = p._skipDisplayRangeUpdateMask;
        _skipIDSUpdates = p._skipIDSUpdates;
        _displayableOnLegend = p._displayableOnLegend;
        _skipRendering = p._skipRendering;

        _painter.setData(_data);
        _painter.setDataSet(this);
        _painter.setName("Painter of (" + _name + ")");
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
     * Method called to indicate that the data processing began. It locks references, etc.
     *
     * @param fromFirstLevel if true, the processing is executed from the beginning
     */
    @Override
    public void beginDataProcessing(boolean fromFirstLevel)
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': begin data processing method called");
        if (_skipIDSUpdates) return;
        _painter.beginDataProcessing(fromFirstLevel);
    }

    /**
     * Method that should be called after processing IDS (releases some locked references, etc.)
     */
    @Override
    public void finishDataProcessing()
    {
        Notification.printNotification(_GC, _PC, "Data set '" + _name + "': finish data processing method called");
        if (_skipIDSUpdates) return;
        _painter.finishDataProcessing();
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
        if (_skipIDSUpdates) return;
        _painter.updateFirstLevelIDS(DRM, eventType);
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
        if (_skipIDSUpdates) return;
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
        if (_skipIDSUpdates) return;
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
     * Returns arrow styles.
     *
     * @return arrow styles
     */
    @Override
    public ArrowStyles getArrowStyles()
    {
        return _painter.getArrowStyles();
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
     * Getter for data set name.
     *
     * @return data set name
     */
    @Override
    public String getLegendLabel()
    {
        return _legendLabel;
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
     * Setter for the flag indicating if the IDS updates should be skipped.
     *
     * @param skipIDSUpdates flag
     */
    public void setSkipIDSUpdates(boolean skipIDSUpdates)
    {
        _skipIDSUpdates = skipIDSUpdates;
    }

    /**
     * Getter for the flag indicating if the IDS updates should be skipped.
     *
     * @return flag
     */
    public boolean areIDSUpdatesSkipped()
    {
        return _skipIDSUpdates;
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
