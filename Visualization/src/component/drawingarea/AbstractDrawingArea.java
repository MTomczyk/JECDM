package component.drawingarea;

import component.AbstractSwingComponent;
import component.drawingarea.renderingdata.AbstractRenderingData;
import component.drawingarea.renderingdata.RenderingData;
import container.Notification;
import container.PlotContainer;
import scheme.AbstractScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import scheme.enums.FlagFields;
import scheme.enums.SizeFields;
import statistics.movingaverage.MovingAverageLong;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.EventTypes;

import java.awt.*;

/**
 * Class representing a plot drawing area.
 *
 * @author MTomczyk
 */


public abstract class AbstractDrawingArea extends AbstractSwingComponent
{

    /**
     * Params container.
     */
    public static class Params extends AbstractSwingComponent.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name component name
         * @param PC   plot container: allows accessing various plot components
         */
        public Params(String name, PlotContainer PC)
        {
            super(name, PC);
        }

        /**
         * If true, render generation times are measured and stored.
         */
        public boolean _measureRenderGenerationTimes = true;
    }

    /**
     * Auxiliary class supporting efficient rendering. It wraps an offscreen buffered image that is updated only when the resulting plot is supposed to change.
     */
    protected volatile AbstractRenderingData _renderingData;

    /**
     * If true, drawing area is clipped to its boundaries (rendering outside is ignored). False otherwise.
     */
    protected boolean _clipDrawingArea;

    /**
     * If true, render generation times are measured and stored.
     */
    protected boolean _measureRenderGenerationTimes;

    /**
     * Object storing render generation times measured (moving average).
     */
    protected MovingAverageLong _renderGenerationTimes;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractDrawingArea(Params p)
    {
        super(p._name, p._PC);
        instantiateGrids(p);
        instantiateRenderingData(p);
        _clipDrawingArea = p._clipDrawingArea;
        _measureRenderGenerationTimes = p._measureRenderGenerationTimes;
        if (p._measureRenderGenerationTimes) _renderGenerationTimes = new MovingAverageLong(10);
    }

    /**
     * Instantiates the rendering data.
     *
     * @param p params container
     */
    protected void instantiateRenderingData(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: instantiate rendering data method called");
        _renderingData = new RenderingData(2);
    }


    /**
     * Auxiliary method for instantiating gridlines.
     *
     * @param p params container
     */
    protected void instantiateGrids(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: instantiate grids method called");
    }

    /**
     * This method can be called to create a swing worker responsible for reconstructing IDS (internal data structures)
     * Using the swing worker aids in executing potentially heavy operations out of the EDT.
     *
     * @param eventType       type of the even triggered the reconstruction of IDSS
     * @return swing worker instance
     */
    public QueuedSwingWorker<Void, Void> createIDSUpdater(EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: update IDS and reconstruct offscreen");
        DrawingAreaIDSUpdater idsUpdater = null;

        if ((eventType.equals(EventTypes.ON_RESIZE)) || (eventType.equals(EventTypes.ON_DATA_CHANGED)) ||
                (eventType.equals(EventTypes.ON_DEMAND)))
            idsUpdater = new DrawingAreaIDSUpdater(_PC, eventType);
        return idsUpdater;
    }

    /**
     * This method constructs a swing worker that updates the render image that is to be painted over the drawing area.
     * The use of the swing worker aids executing potentially heavy operations out of the EDT.
     *
     * @param eventType       type of the even triggered the reconstruction of the render
     * @return swing worker instance
     */
    public QueuedSwingWorker<Void, Void> createRenderUpdater(EventTypes eventType)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: create render updater method called");
        return null;
    }

    /**
     * Auxiliary method that establishes layer expected dimensions.
     */
    protected void calculateLayerExpectedDimensions()
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: calculate layer expected dimensions method called");
    }


    /**
     * Executed before drawing data sets.
     * Paints some auxiliary components.
     * @param g Java AWT Graphics context
     */
    protected void paintAuxElements(Graphics g)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + PlotContainer.getID(_PC) + "]: paint aux elements method called");
    }

    /**
     * Called to update the component appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        super.updateScheme(scheme);
        _align = Align.CENTER_CENTER;
        _backgroundColor = scheme.getColors(_surpassedColors, ColorFields.DRAWING_AREA_BACKGROUND);

        _borderColor = scheme.getColors(_surpassedColors, ColorFields.DRAWING_AREA_BORDER);
        _borderWidth.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.DRAWING_AREA_BORDER_WIDTH_FIXED));
        _borderWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.DRAWING_AREA_BORDER_WIDTH_RELATIVE_MULTIPLIER));
        _borderWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.DRAWING_AREA_USE_BORDER_RELATIVE_WIDTH));
        _borderStroke = getStroke(_borderWidth);

        setOpaque(scheme.getFlags(_surpassedFlags, FlagFields.DRAWING_AREA_OPAQUE));
    }

    /**
     * Getter for the rendering data.
     *
     * @return rendering data
     */
    public AbstractRenderingData getRenderingData()
    {
        return _renderingData;
    }

    /**
     * Getter for the moving average that stores the current (average) render generation time.
     *
     * @return render generation time (average)
     */
    public MovingAverageLong getRenderGenerationTimes()
    {
        return _renderGenerationTimes;
    }

    /**
     * Can be called to clear memory.
     */
    @Override
    @SuppressWarnings("DuplicatedCode")
    public void dispose()
    {
        super.dispose();
    }
}
