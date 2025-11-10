package plot;

import component.AbstractSwingComponent;
import component.axis.swing.AbstractAxis;
import component.colorbar.AbstractColorbar;
import component.drawingarea.AbstractDrawingArea;
import component.legend.AbstractLegend;
import component.legend.Legend;
import component.margins.AbstractMargins;
import component.margins.Margins;
import component.title.AbstractTitle;
import component.title.Title;
import container.ComponentsContainer;
import container.Notification;
import container.PlotContainer;
import drmanager.DisplayRangesManager;
import layoutmanager.BaseLayoutManager;
import layoutmanager.ILayoutManager;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import scheme.enums.ColorFields;
import scheme.enums.FlagFields;
import scheme.enums.SizeFields;
import utils.Projection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * Abstract class representing the plot.
 *
 * @author MTomczyk
 */
public class AbstractPlot extends AbstractSwingComponent
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Plot scheme (determines colors, sizes, alignments, etc).
         */
        public AbstractScheme _scheme = null;

        /**
         * If true, e.g., some notification can be printed to the console.
         */
        public boolean _debugMode = false;

        /**
         * Plot title. If not null, a component representing the plot title is rendered in the main plot area (can be null -> not displayed).
         */
        public String _title = null;

        /**
         * If true, main gridlines are drawn; false otherwise.
         */
        public boolean _drawMainGridlines = true;

        /**
         * If true, auxiliary gridlines are drawn; false otherwise.
         */
        public boolean _drawAuxGridlines = true;

        /**
         * If true, X-axis is drawn; false otherwise.
         */
        public boolean _drawXAxis = true;

        /**
         * If true, Y-axis is drawn; false otherwise.
         */
        public boolean _drawYAxis = true;

        /**
         * X-axis (axes) title. Can be null -> not rendered.
         */
        public String _xAxisTitle = null;

        /**
         * Y-axis (axes) title. Can be null -> not rendered.
         */
        public String _yAxisTitle = null;

        /**
         * If true: legend is drawn; false = otherwise.
         */
        public boolean _drawLegend = false;

        /**
         * Params container used to instantiate display ranges manager (object responsible for storing/updating data on display ranges, i.e., dimension bounds, used when projecting data points into rendering space).
         */
        public DisplayRangesManager.Params _pDisplayRangesManager = null;

        /**
         * Optional colorbar component (can be null -> not displayed).
         */
        public AbstractColorbar _colorbar = null;

        /**
         * If true, drawing area is clipped to its boundaries (rendering outside is ignored). False otherwise.
         */
        public boolean _clipDrawingArea = true;

        /**
         * If true, offscreen renders generation times are measured and stored.
         */
        protected boolean _measureOffscreenGenerationTimes = true;
    }

    /**
     * MVC model: plot controller.
     */
    protected PlotController _C;

    /**
     * MVC model: plot model.
     */
    protected PlotModel _M;

    /**
     * Object responsible for components organization.
     */
    protected ILayoutManager _layoutManager;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractPlot(Params p)
    {
        super("Main plot component", null); // do not need to provide PC here, it is set in the following lines

        DisplayRangesManager DRM;
        if (p._pDisplayRangesManager == null) DRM = constructDefaultDisplayRangesManager(p);
        else DRM = new DisplayRangesManager(p._pDisplayRangesManager);

        _PC = new PlotContainer(this, DRM, p._debugMode);

        instantiateModelAndController(p, _PC);

        _M._scheme = p._scheme;
        if (_M._scheme == null)
        {
            p._scheme = new WhiteScheme();
            _M._scheme = p._scheme;
        }
        applySchemeCustomModifications(p);

        setLayout(null); // layout is controlled by own manager

        instantiateComponents(p);
        instantiateLayoutManager(p);
        _M.instantiateDisplayRangesChangedListeners();
    }

    /**
     * Can be called by the extending classes to apply some custom modifications to the input scheme.
     *
     * @param p params container
     */
    protected void applySchemeCustomModifications(Params p)
    {

    }

    /**
     * Constructs a default display ranges manager automatically when the {@link drmanager.DisplayRangesManager.Params} were not provided.
     *
     * @param p params container
     * @return instance of {@link DisplayRangesManager}.
     */
    protected DisplayRangesManager constructDefaultDisplayRangesManager(Params p)
    {
        return new DisplayRangesManager(DisplayRangesManager.Params.getFor2D());
    }

    /**
     * Auxiliary method for instantiating plot components.
     *
     * @param p params container
     */
    protected void instantiateComponents(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: instantiate components method called");

        _M._CC = new ComponentsContainer();
        LinkedList<AbstractSwingComponent> components = new LinkedList<>();
        _M._CC.setComponents(components);

        AbstractMargins margins = createMargins(p);
        _M._CC.setMargins(margins);
        if (margins != null) components.add(margins);

        AbstractDrawingArea drawingArea = createDrawingArea(p);
        _M._CC.setDrawingArea(drawingArea);
        if (drawingArea != null) components.add(drawingArea);

        AbstractTitle title = createTitle(p);
        _M._CC.setTitle(title);
        if (title != null) components.add(title);

        AbstractAxis[] axes = createAxes(p);
        _M._CC.setAxes(axes);
        if (axes != null) components.addAll(Arrays.asList(_M._CC.getAxes()));

        AbstractLegend legend = createLegend(p);
        _M._CC.setLegend(legend);
        if (legend != null) components.add(legend);

        _M._CC.setColorbar(p._colorbar);
        if (p._colorbar != null)
        {
            _M._CC.getColorbar().setPlotContainer(_PC);
            components.add(_M._CC.getColorbar());
        }
    }

    /**
     * Auxiliary method for instantiating plot margins.
     *
     * @param p params container
     * @return margins object
     */
    protected AbstractMargins createMargins(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: create margins method called");
        return new Margins(_PC);
    }

    /**
     * Fills the drawing area params used to construct the drawing area object.
     *
     * @param pD drawing area params
     * @param p  plot params container
     */
    protected void fillDrawingAreaParams(AbstractSwingComponent.Params pD, AbstractPlot.Params p)
    {

    }

    /**
     * Auxiliary method for instantiating plot drawing area.
     *
     * @param p params container
     * @return drawing area object
     */
    protected AbstractDrawingArea createDrawingArea(Params p)
    {
        return null;
    }


    /**
     * Auxiliary method for instantiating plot drawing area.
     *
     * @param p params container
     * @return drawing area object
     */
    protected AbstractTitle createTitle(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: create title method called");
        if (p._title == null) return null;
        return new Title(p._title, _PC);
    }


    /**
     * Auxiliary method for instantiating the legend.
     *
     * @param p params container
     * @return legend object
     */
    protected AbstractLegend createLegend(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: create legend method called");
        if (!p._drawLegend) return null;
        return new Legend(_PC);
    }

    /**
     * Auxiliary method for instantiating plot axes.
     *
     * @param p params container
     * @return drawing area object
     */
    protected AbstractAxis[] createAxes(Params p)
    {
        return null;
    }

    /**
     * Auxiliary method for instantiating the layout manager.
     *
     * @param p params container
     */
    protected void instantiateLayoutManager(Params p)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: instantiate layout manager method called");
        _layoutManager = new BaseLayoutManager(_M._PC, _M._CC);
        _layoutManager.addElementsInCorrectOrder(this);
    }


    /**
     * Instantiates the model and the controller.
     *
     * @param p  params container
     * @param PC plot container
     */
    protected void instantiateModelAndController(Params p, PlotContainer PC)
    {
        _C = new PlotController(this);
        _M = new PlotModel(this, PC);
        doBasicParameterizationOfPlotAndController(p);
    }

    /**
     * Executes a basic parameterization of just created plot controller and model.
     *
     * @param p plot params container
     */
    protected void doBasicParameterizationOfPlotAndController(Params p)
    {
        _C.setPlotModel(_M);
        _M.setPlotController(_C);
    }

    /**
     * Called to update plot appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc).
     */
    @Override
    public void updateScheme(AbstractScheme scheme)
    {
        // Check for premature termination
        if (!_M._schemeUpdatesEnabled) return;

        super.updateScheme(scheme);

        if (scheme == null) scheme = _M._scheme;
        else _M._scheme = scheme;

        _backgroundColor = scheme.getColors(_surpassedColors, ColorFields.PLOT_BACKGROUND);

        _borderColor = scheme.getColors(_surpassedColors, ColorFields.PLOT_BORDER);
        _borderWidth.setFixedSize(scheme.getSizes(_surpassedSizes, SizeFields.PLOT_BORDER_WIDTH_FIXED));
        _borderWidth.setRelativeSizeMultiplier(scheme.getSizes(_surpassedSizes, SizeFields.PLOT_BORDER_WIDTH_RELATIVE_MULTIPLIER));
        _borderWidth.setUseRelativeSize(scheme.getFlags(_surpassedFlags, FlagFields.PLOT_BORDER_USE_RELATIVE_WIDTH));
        _borderStroke = getStroke(_borderWidth);

        setOpaque(scheme.getFlags(_surpassedFlags, FlagFields.PLOT_OPAQUE));

        for (AbstractSwingComponent c : _M._CC.getComponents())
        {
            if (c == null) continue;
            c.updateScheme(scheme);
        }

        if (_C._menu != null) _C._menu.updateScheme(scheme);

        _layoutManager.updateScheme(scheme);
    }

    /**
     * Can be called to update the layout (typically called on the window resized event).
     * Should not be invoked manually.
     */
    public void updateLayout()
    {
        Notification.printNotification(_GC, _PC, "Plot [id = " + _M.getPlotID() + "]: update layout method called (width = " + getWidth() + " ; height = " + getHeight() + ")");

        // Check for premature termination
        if (!_M._layoutUpdatesEnabled) return;

        setLocationAndSize(getX(), getY(), getWidth(), getHeight());
        setPrimaryDrawingArea(getX(), getY(), getWidth(), getHeight());

        Graphics g = null;
        if (getGraphics() != null) g = getGraphics().create();

        _borderStroke = getStroke(_borderWidth);
        _layoutManager.positionElements(
                g,
                Projection.getP(_translationVector[0]),
                Projection.getP(_translationVector[1]),
                _primaryDrawingArea.width,
                _primaryDrawingArea.height);

        if (_C._menu != null) _C._menu.updateRelativeFields();

        if (g != null) g.dispose();
    }

    /**
     * Updates legend (can be called after data sets swap).
     */
    protected void updateLegend()
    {
        if (_M == null) return;
        Notification.printNotification(_GC, _PC, "Plot [id = " + _M.getPlotID() + "]: update legend method called (width = " + getWidth() + " ; height = " + getHeight() + ")");
        Graphics g = null;
        if (getGraphics() != null) g = getGraphics().create();

        _borderStroke = getStroke(_borderWidth);
        _layoutManager.updateLegend(g,
                Projection.getP(_translationVector[0]),
                Projection.getP(_translationVector[1]),
                _primaryDrawingArea.width,
                _primaryDrawingArea.height);
        if (g != null) g.dispose();
    }


    /**
     * Method for drawing the element.
     *
     * @param g Java AWT Graphics context
     */
    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(null);
        g.fillRect(0, 0, getWidth(), getHeight()); // resets background
        drawBackground(g);
        drawBorder(g);
    }


    /**
     * Returns the model (MVC).
     *
     * @return the model
     */
    public PlotModel getModel()
    {
        return _M;
    }

    /**
     * Returns the controller (MVC).
     *
     * @return the controller
     */
    public PlotController getController()
    {
        return _C;
    }

    /**
     * Can be called to dispose the object and its children.
     */
    @Override
    public void dispose()
    {
        super.dispose();
        _layoutManager.dispose();
        _C.dispose();
        _M.dispose();
        _C = null;
        _M = null;
    }

    /**
     * Getter for components container.
     *
     * @return components container
     */
    public ComponentsContainer getComponentsContainer()
    {
        return _M._CC;
    }

    /**
     * Constructs and returns a screenshot of the plot. Data is stored as a buffered image.
     * Important note: the tasks are executed on the EDT (SwingUtilities.invokeAndWait(...)); thus, the method
     * is executed synchronously.
     *
     * @param transparentBackground if true, background is saved as transparent and the returned object is saved as ARGB; if false, the
     *                              displayed background color is saved (RGB mode in BufferedImage).
     * @return plot screenshot (buffered image); null in the case of an error
     */
    public BufferedImage getPlotScreenshot(boolean transparentBackground)
    {
        Notification.printNotification(_GC, _PC, _name + " [id = " + _M._id + "]: get plot screenshot method called");
        final BufferedImage screenshot;
        if (transparentBackground) screenshot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        else screenshot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        try
        {
            SwingUtilities.invokeAndWait(() ->
            {

                if (transparentBackground)
                {
                    Graphics2D g2 = screenshot.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // clear screenshot
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
                    g2.setColor(new Color(0, 0, 0, 0));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                    for (AbstractSwingComponent c : _M._CC.getComponents())
                    {
                        BufferedImage componentImage = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2c = componentImage.createGraphics();
                        g2c.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        c.paint(g2c);
                        g2.drawImage(componentImage, c.getX(), c.getY(), null);
                        g2c.dispose();
                    }
                    g2.dispose();
                } else
                {
                    Graphics2D g2 = screenshot.createGraphics();
                    paint(g2);
                    g2.dispose();
                }
            });
        } catch (InterruptedException | InvocationTargetException e)
        {
            System.out.println("Error occurred when creating a screenshot (message = " + e.getMessage() + ")");
            return null;
        }
        return screenshot;
    }
}
