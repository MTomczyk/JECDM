package container;

import component.drawingarea.AbstractDrawingArea;
import component.drawingarea.renderingdata.AbstractRenderingData;
import dataset.IDataSet;
import drmanager.DisplayRangesManager;
import listeners.interact.AbstractInteractListener;
import plot.AbstractPlot;
import scheme.referencevalue.IReferenceValueGetter;
import scheme.referencevalue.MinPlotDimension;

import java.util.ArrayList;

/**
 * Container-like object shared among different GUI plot-related objects.
 * Provides references and various functionalities.
 *
 * @author MTomczyk
 */
public class PlotContainer extends AbstractVisualizationContainer
{
    /**
     * Reference to the associated plot.
     */
    protected AbstractPlot _plot;

    /**
     * Object responsible for providing reference value used when calculating relative locations/dimensions/etc.
     */
    protected IReferenceValueGetter _RVG;

    /**
     * Object responsible for storing/updating data on display ranges, i.e., dimension bounds, used when projecting data points into rendering space.
     */
    protected final DisplayRangesManager _DRM;


    /**
     * Parameterized constructor.
     *
     * @param plot      reference to the associated plot
     * @param drm       display ranges manager
     * @param debugMode if true -> e.g., some notification can be printed to the console
     */
    public PlotContainer(AbstractPlot plot, DisplayRangesManager drm, boolean debugMode)
    {
        super(debugMode);
        _plot = plot;
        _DRM = drm;
        _RVG = new MinPlotDimension(plot);
    }

    /**
     * Save method for returning plot ID.
     * Any failure while retrieving the ID will force the method to return -1.
     *
     * @param PC plot container
     * @return plot id, null if the plot id was not retrievable
     */
    public static Integer getID(PlotContainer PC)
    {
        if (PC == null) return null;
        if (PC.getPlot() == null) return null;
        if (PC.getPlot().getModel() == null) return null;
        return PC.getPlot().getModel().getPlotID();
    }


    /**
     * Getter for the plot.
     *
     * @return the plot
     */
    public AbstractPlot getPlot()
    {
        return _plot;
    }

    /**
     * Getter for plot id. Returns -1, if the id is not assigned (but should be).
     *
     * @return plot id
     */
    public int getPlotID()
    {
        Integer id = PlotContainer.getID(this);
        if (id == null) return -1;
        return id;
    }

    /**
     * Getter for the display ranges manager (object responsible for storing/updating data on display ranges, i.e., dimension bounds, used when projecting data points into rendering space).
     *
     * @return display rages manager
     */
    public DisplayRangesManager getDisplayRangesManager()
    {
        return _DRM;
    }

    /**
     * Getter for drawing area.
     *
     * @return drawing area
     */
    public AbstractDrawingArea getDrawingArea()
    {
        if (_plot.getComponentsContainer().getDrawingArea() == null) return null;
        return _plot.getComponentsContainer().getDrawingArea();
    }

    /**
     * Setter for the reference value getter  (object responsible for providing reference value used when calculating relative locations/dimensions/etc.).
     *
     * @param RVG reference value getter
     */
    public void setReferenceValueGetter(IReferenceValueGetter RVG)
    {
        _RVG = RVG;
    }

    /**
     * Getter for the reference value getter  (object responsible for providing reference value used when calculating relative locations/dimensions/etc.).
     *
     * @return reference value getter
     */
    public IReferenceValueGetter getReferenceValueGetter()
    {
        return _RVG;
    }

    /**
     * Data sets getter.
     *
     * @return data sets
     */
    public ArrayList<IDataSet> getDataSets()
    {
        return _plot.getModel().getDataSets();
    }

    /**
     * Rendering data getter.
     *
     * @return rendering data
     */
    public AbstractRenderingData getRenderingData()
    {
        return _plot.getComponentsContainer().getDrawingArea().getRenderingData();
    }

    /**
     * Getter for the plot's interact listener.
     *
     * @return interact listener
     */
    public AbstractInteractListener getInteractListener()
    {
        return _plot.getController().getInteractListener();
    }

}
