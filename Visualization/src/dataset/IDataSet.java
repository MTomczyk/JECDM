package dataset;

import container.GlobalContainer;
import container.PlotContainer;
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
 * Interface for data set object responsible for maintaining and rendering plot data sets.
 *
 * @author MTomczyk
 */
public interface IDataSet
{
    /**
     * This method creates a new data set object.
     * All the fields are cloned except for the data to be depicted.
     * This data is provided as the input. The method may be useful when, e.g., using plots to animate the data
     * (frequent calls for {@link plot.PlotModel#setDataSets(ArrayList, boolean)} would be required).
     *
     * @param data input data to be displayed.
     * @return data set object
     */
    IDataSet wrapAround(LinkedList<double[][]> data);

    /**
     * Getter for the input raw (unprocessed) data to be displayed.
     *
     * @return data
     */
    Data getData();

    /**
     * It can be implemented to return auxiliary mask that tell whether the update of the i-th display range should be skipped.
     * Can return null (not used). Obviously, the mask has no effect when updating display ranges is globally disabled.
     *
     * @return mask (i-th element = true -> the update of i-th display range is skipped.
     */
    boolean[] getSkipDisplayRangesUpdateMasks();

    /**
     * It can be implemented to set auxiliary mask that tell whether the update of the i-th display range should be skipped.
     * Can be null (not used).
     *
     * @param mask mask (i-th element = true -> the update of i-th display range is skipped.
     */
    void setSkipDisplayRangesUpdateMasks(boolean[] mask);

    /**
     * Setter for the containers.
     *
     * @param GC global container
     * @param PC plot container
     */
    void setContainers(GlobalContainer GC, PlotContainer PC);

    /**
     * Method called to indicate that the data processing began. It locks references, etc.
     *
     * @param fromFirstLevel if true, the processing is executed from the beginning
     */
    void beginDataProcessing(boolean fromFirstLevel);

    /**
     * Method that should be called after processing IDS (releases some locked references, etc.)
     */
    void finishDataProcessing();

    /**
     * IDS = Internal Data Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data that is
     * easy-to-be-rendered. First level IDS corresponds to normalization of data points as imposed by display ranges.
     * The method should be called on the init phase (when the display ranges are fixed) are called by {@link AbstractPlot}
     * when the display ranges changed after data update.
     *
     * @param DRM       display range manager maintained by the plot
     * @param eventType event type that triggered the method execution
     */
    void updateFirstLevelIDS(DisplayRangesManager DRM, EventTypes eventType);

    /**
     * IDS = Internal Data Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data that is
     * easy-to-be-rendered. Second level IDS corresponds to normalization of data points as imposed by the drawing area coordinates.
     * The method should be called when the drawing area changes (see the top-level {@link FrameListener#componentResized(ComponentEvent)} )
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @param eventType  event type that triggered the method execution
     */
    void updateSecondLevelIDS(Dimension[] dimensions, EventTypes eventType);

    /**
     * IDS = Internal Data Structures = data structures optimized for rendering.
     * Can be called to construct third-level IDS (e.g., data for VBOs when rendering in 3D)
     *
     * @param eventType event type that triggered the method execution
     */
    void updateThirdLevelIDS(EventTypes eventType);

    /**
     * Main method for drawing a data set.
     *
     * @param r rendering context
     */
    void draw(Object r);

    /**
     * Returns marker style.
     *
     * @return marker style
     */
    MarkerStyle getMarkerStyle();

    /**
     * Returns line style.
     *
     * @return line style
     */
    LineStyle getLineStyle();

    /**
     * Getter for data set name.
     *
     * @return data set name
     */
    String getName();

    /**
     * Getter for the "skip rendering" parameter. If true, the data set rendering is skipped (but the data is still processed).
     *
     * @return if true the data set rendering is skipped
     */
    boolean isRenderingSkipped();

    /**
     * Setter for the "skip rendering" parameter. If true, the data set rendering is skipped (but the data is still processed).
     * Can be used to efficiently manipulate which data is currently (not) displayed.
     *
     * @param skipRendering if true, the data set rendering is skipped
     */
    void setSkipRendering(boolean skipRendering);

    /**
     * If true, the data set is displayable on the legend.
     *
     * @return true if the data set is displayable on legend, false otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isDisplayableOnLegend();

    /**
     * Setter for the "displayable on legend" parameter.
     *
     * @param displayableOnLegend if true the data set is displayable on legend (will be rendered), false otherwise
     */
    void setDisplayableOnLegend(boolean displayableOnLegend);

    /**
     * Getter for the painter object.
     *
     * @return painter object
     */
    IPainter getPainter();

    /**
     * Can be called to clear the data.
     */
    void dispose();
}
