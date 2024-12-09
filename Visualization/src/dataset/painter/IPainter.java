package dataset.painter;

import container.GlobalContainer;
import container.PlotContainer;
import dataset.Data;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import drmanager.DisplayRangesManager;
import listeners.FrameListener;
import plot.AbstractPlot;
import space.Dimension;
import thread.swingworker.EventTypes;

import java.awt.event.ComponentEvent;

/**
 * Interfaces for objects responsible for performing data set rendering.
 *
 * @author MTomczyk
 */
public interface IPainter
{
    /**
     * The method clones the painter but ignores the rendering data.
     * The method supports data reaplcing.
     *
     * @return cloned painter (except for rendering data).
     */
    IPainter getEmptyClone();

    /**
     * Supportive method for setting the painter's name.
     *
     * @param name painter's name
     */
    void setName(String name);

    /**
     * Setter for the input raw (unprocessed) data to be displayed.
     *
     * @param data data
     */
    void setData(Data data);

    /**
     * Setter for the parent data set possessing the painter.
     *
     * @param ds parent data set possessing the painter
     */
    void setDataSet(IDataSet ds);

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
     * @param fromFirstLevel if true, processing is executed from the beginning, otherwise the process starts from the IDS second level
     */
    void beginDataProcessing(boolean fromFirstLevel);

    /**
     * Method that should be called after processing IDS (releases some locked references, etc.)
     */
    void finishDataProcessing();

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
    void updateFirstLevelIDS(DisplayRangesManager DRM, EventTypes eventType);

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Data ({@link Data}) is suitably processed and transformed into projection data {@link IDS} that is
     * easy-to-be-rendered. Second level IDS corresponds to normalization of data points as imposed by the drawing area coordinates.
     * The method should be called when the drawing area changes (see the top-level {@link FrameListener#componentResized(ComponentEvent)} ).
     *
     * @param dimensions drawing area dimensions (coordinates + sizes)
     * @param eventType  event type that triggered the method execution
     */
    void updateSecondLevelIDS(Dimension[] dimensions, EventTypes eventType);

    /**
     * IDS = Internal Data Set Structures = data structures optimized for rendering.
     * Can be called to construct third-level IDS (e.g., data for VBOs when rendering in 3D).
     *
     * @param eventType event type that triggered the method execution
     */
    void updateThirdLevelIDS(EventTypes eventType);

    /**
     * Setter for the renderer.
     *
     * @param g renderer object
     */
    void setRenderer(Object g);

    /**
     * Called at the end of the rendering to release the renderer (e.g., Java AWT Graphics dispose()).
     */
    void releaseRenderer();

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
     * Getter for the IDS recalculation times.
     *
     * @param phase phase (implementation-specific, by default 0 = 1st level, 1 = 2nd level, 2 = 3rd level).
     * @return Recalculation time (in milliseconds). If null -> no times were measured.
     */
    Long getRecalculationTime(int phase);

    /**
     * Can be called to clear the data.
     */
    void dispose();
}
