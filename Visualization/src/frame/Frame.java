package frame;

import color.Color;
import com.jogamp.opengl.GLProfile;
import container.Notification;
import plot.AbstractPlot;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.PlotsWrapper;
import scheme.AbstractScheme;
import swing.ComponentUtils;
import utils.Projection;

import javax.swing.*;
import java.awt.*;

/**
 * Frame that contains/displays/manages the plots {@link AbstractPlot} (abstract class).
 *
 * @author MTomczyk
 */
public class Frame extends JFrame
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Instantiates the params container (frame size is set to 50% of Min(screen.width, screen.height).
         *
         * @param plot main plot panel
         * @return frame params container
         */
        public static Params getParams(AbstractPlot plot)
        {
            return getParams(plot, 0.5f);
        }

        /**
         * Instantiates the params container  (frame size is set to 50% of Min(screen.width, screen.height).
         *
         * @param plotsWrapper main panel (wrapper) maintaining plots to be displayed
         * @return frame params container
         */
        public static Params getParams(AbstractPlotsWrapper plotsWrapper)
        {
            return getParams(plotsWrapper, 0.5f);
        }

        /**
         * Instantiates the params container (frame size is set to Min(widthRelative * screen width,  heightRelative * screen height).
         *
         * @param relativeSize frame width relative to screen width/height
         * @param plot         main plot panel
         * @return frame params container
         */
        public static Params getParams(AbstractPlot plot, float relativeSize)
        {
            int width = Projection.getP_mul(relativeSize, Toolkit.getDefaultToolkit().getScreenSize().width);
            int height = Projection.getP_mul(relativeSize, Toolkit.getDefaultToolkit().getScreenSize().height);
            int size = Math.min(width, height);
            return new Params(new PlotsWrapper(plot), size, size);
        }

        /**
         * Instantiates the params container (frame size is set to Min(widthRelative * screen width,  heightRelative * screen height).
         *
         * @param plotsWrapper main panel (wrapper) maintaining plots to be displayed
         * @param relativeSize frame width relative to screen width/height
         * @return frame params container
         */
        public static Params getParams(AbstractPlotsWrapper plotsWrapper, float relativeSize)
        {
            int width = Projection.getP_mul(relativeSize, Toolkit.getDefaultToolkit().getScreenSize().width);
            int height = Projection.getP_mul(relativeSize, Toolkit.getDefaultToolkit().getScreenSize().height);
            int size = Math.min(width, height);
            return new Params(plotsWrapper, size, size);
        }

        /**
         * Instantiates the params container (frame size is set to [widthRelative * screen width,  heightRelative * screen height]).
         *
         * @param plot           main plot panel
         * @param widthRelative  frame width relative to screen width
         * @param heightRelative frame height relative to screen height
         * @return frame params container
         */
        public static Params getParams(AbstractPlot plot, float widthRelative, float heightRelative)
        {
            int width = Projection.getP_mul(widthRelative, Toolkit.getDefaultToolkit().getScreenSize().width);
            int height = Projection.getP_mul(heightRelative, Toolkit.getDefaultToolkit().getScreenSize().height);
            return new Params(new PlotsWrapper(plot), width, height);
        }

        /**
         * Instantiates the params container (frame size is set to [widthRelative * screen width,  heightRelative * screen height]).
         *
         * @param plot   main plot panel
         * @param width  frame width
         * @param height frame height
         * @return frame params container
         */
        public static Params getParams(AbstractPlot plot, int width, int height)
        {
            return new Params(new PlotsWrapper(plot), width, height);
        }

        /**
         * Instantiates the params container (frame size is set to [widthRelative * screen width,  heightRelative * screen height]).
         *
         * @param plotsWrapper   main panel (wrapper) maintaining plots to be displayed
         * @param widthRelative  frame width relative to screen width
         * @param heightRelative frame height relative to screen height
         * @return frame params container
         */
        public static Params getParams(AbstractPlotsWrapper plotsWrapper, float widthRelative, float heightRelative)
        {
            int width = Projection.getP_mul(widthRelative, Toolkit.getDefaultToolkit().getScreenSize().width);
            int height = Projection.getP_mul(heightRelative, Toolkit.getDefaultToolkit().getScreenSize().height);
            return new Params(plotsWrapper, width, height);
        }

        /**
         * Instantiates the params container (frame size is set as provided).
         *
         * @param plotsWrapper main panel (wrapper) maintaining plots to be displayed
         * @param width        frame width
         * @param height       frame height
         * @return frame params container
         */
        public static Params getParams(AbstractPlotsWrapper plotsWrapper, int width, int height)
        {
            return new Params(plotsWrapper, width, height);
        }

        /**
         * Frame width.
         */
        public int _width;

        /**
         * Frame height.
         */
        public int _height;

        /**
         * Plots to be displayed.
         */
        public AbstractPlotsWrapper _plotsWrapper;

        /**
         * Frame title (can be null -> not used).
         */
        public String _title = null;

        /**
         * Background color (can be null -> not used).
         */
        public Color _backgroundColor = null;

        /**
         * If true -> e.g., some notification can be printed to the console.
         */
        public boolean _debugMode = false;

        /**
         * If true, the repaints events are called during resizing. False -> repaint is done after resizing is completed (OS dependent).
         */
        public boolean _dynamicLayout = true;

        /**
         * Parameterized constructor.
         *
         * @param plotsWrapper plots wrapper
         * @param width        frame width
         * @param height       frame height
         */
        protected Params(AbstractPlotsWrapper plotsWrapper, int width, int height)
        {
            _plotsWrapper = plotsWrapper;
            _width = width;
            _height = height;
        }
    }

    /**
     * MVC: frame controller.
     */
    protected FrameController _C;

    /**
     * MVC: frame model.
     */
    protected FrameModel _M;

    /**
     * Parameterized constructor (frame size is set to 50% of Min(screen.width, screen.height).
     *
     * @param plot main plot panel
     */
    public Frame(AbstractPlot plot)
    {
        this(plot, 0.5f);
    }

    /**
     * Parameterized constructor (frame size is set to 50% of Min(screen.width, screen.height).
     *
     * @param plotsWrapper main panel (wrapper) maintaining plots to be displayed
     */
    public Frame(AbstractPlotsWrapper plotsWrapper)
    {
        this(plotsWrapper, 0.5f);
    }

    /**
     * Parameterized constructor.
     *
     * @param width  frame width
     * @param height frame height
     * @param plot   main plot panel
     */
    public Frame(AbstractPlot plot, int width, int height)
    {
        this(new Params(new PlotsWrapper(plot), width, height));
    }

    /**
     * Parameterized constructor.
     *
     * @param plotsWrapper main panel (wrapper) maintaining plots to be displayed
     * @param width        frame width.
     * @param height       frame height
     */
    public Frame(AbstractPlotsWrapper plotsWrapper, int width, int height)
    {
        this(new Params(plotsWrapper, width, height));
    }

    /**
     * Parameterized constructor (frame size is set to Min(widthRelative * screen width,  heightRelative * screen height).
     *
     * @param relativeSize frame width relative to screen width/height
     * @param plot         main plot panel
     */
    public Frame(AbstractPlot plot, float relativeSize)
    {
        instantiate(Params.getParams(plot, relativeSize));
    }

    /**
     * Parameterized constructor (frame size is set to Min(widthRelative * screen width,  heightRelative * screen height).
     *
     * @param plotsWrapper main panel (wrapper) maintaining plots to be displayed
     * @param relativeSize frame width relative to screen width/height
     */
    public Frame(AbstractPlotsWrapper plotsWrapper, float relativeSize)
    {
        instantiate(Params.getParams(plotsWrapper, relativeSize));
    }

    /**
     * Parameterized constructor (frame size is set to [widthRelative * screen width,  heightRelative * screen height]).
     *
     * @param plot           main plot panel
     * @param widthRelative  frame width relative to screen width
     * @param heightRelative frame height relative to screen height
     */
    public Frame(AbstractPlot plot, float widthRelative, float heightRelative)
    {
        instantiate(Params.getParams(plot, widthRelative, heightRelative));
    }

    /**
     * Parameterized constructor (frame size is set to [widthRelative * screen width,  heightRelative * screen height]).
     *
     * @param plotsWrapper   main panel (wrapper) maintaining plots to be displayed
     * @param widthRelative  frame width relative to screen width
     * @param heightRelative frame height relative to screen height
     */
    public Frame(AbstractPlotsWrapper plotsWrapper, float widthRelative, float heightRelative)
    {
        instantiate(Params.getParams(plotsWrapper, widthRelative, heightRelative));
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Frame(Params p)
    {
        instantiate(p);
    }

    /**
     * Instantiates the frame.
     *
     * @param p params container
     */
    protected void instantiate(Params p)
    {
        Toolkit.getDefaultToolkit().setDynamicLayout(p._dynamicLayout);

        // Must be called here. It resolves the following bug: When called within a SwingWorker (suppose you want to do
        // so), retrieving the profile for the first time takes a lot of time. When calling during the frame
        // initialization (for the first time), the retrieval is fast. Hence, the blow line.
        GLProfile.get(GLProfile.GL2);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(p._width, p._height));
        setSize(new Dimension(p._width, p._height));
        setLocationRelativeTo(null);

        if (p._title != null) setTitle(p._title);

        _C = instantiateFrameController(p);
        _M = instantiateFrameModel(p);
        _C.setFrameModel(_M);
        _M.setFrameController(_C);

        // order matters
        Notification.printNotification(_M._GC, null, "Frame: assign plot IDs method called");
        _M._plotsWrapper.getModel().establishGlobalContainer(_M._GC);
        if (p._backgroundColor != null)
        {
            setBackground(p._backgroundColor);
            _M._plotsWrapper.setBackground(p._backgroundColor);
        }

        _C.instantiateBackgroundThreads();
        _C.instantiateListeners();
        _C.registerKeyBindings();
        _C.enableListeners();

        _M._plotsWrapper.getController().requestFocusOnTheFirstValidPlot();

        instantiateLayout(p);

        updateScheme(null);
        _M._plotsWrapper.updateLayout();

        try
        {
            ComponentUtils.initIcon(this);
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Auxiliary method for instantiating frame controller.
     *
     * @param p params container
     * @return frame controller
     */
    protected FrameController instantiateFrameController(Params p)
    {
        return new FrameController(this);
    }

    /**
     * Auxiliary method for instantiating frame model.
     *
     * @param p params container
     * @return frame controller
     */
    protected FrameModel instantiateFrameModel(Params p)
    {
        return new FrameModel(this, p._plotsWrapper, p._debugMode);
    }

    /**
     * Method that instantiates the layout.
     *
     * @param p params container
     */
    protected void instantiateLayout(Params p)
    {
        setLayout(new BorderLayout());
        add(_M._plotsWrapper, BorderLayout.CENTER);
    }

    /**
     * Can be called to update plot appearance.
     * Every plot updates its look based on its own scheme object instance.
     */
    public void updateScheme()
    {
        updateScheme(null);
    }

    /**
     * Can be called to update plot appearance.
     *
     * @param scheme scheme object (determines colors, sizes, alignments, etc.); each plot uses a deep copy object as a new scheme and updates its look; can also be null -> each plot uses its own scheme object provided when instantiated
     */
    public void updateScheme(AbstractScheme scheme)
    {
        Notification.printNotification(_M._GC, null, "Frame: update scheme method called");
        _M._plotsWrapper.updateScheme(scheme);
    }

    /**
     * Getter for the frame model.
     *
     * @return frame model
     */
    public FrameModel getModel()
    {
        return _M;
    }

    /**
     * Getter for the frame controller.
     *
     * @return frame controller
     */
    public FrameController getController()
    {
        return _C;
    }


    /**
     * Can be used to check if the window (JFrame) is opened/displayed (visible and drawn).
     * The kept flag (boolean) is defined as volatile.
     *
     * @return true = the window (JFrame) is opened (visible and drawn), false otherwise
     */
    public boolean isWindowDisplayed()
    {
        return _M._windowDisplayed;
    }

    /**
     * Can be called to update the layout (typically called on the window resized event).
     */
    public void updateLayout()
    {
        Notification.printNotification(_M._GC, null, "Frame: update layout method called");
        setPreferredSize(getSize());
        _M._plotsWrapper.updateLayout();
    }

    /**
     * Can be called to check if the termination began.
     *
     * @return true, if the termination began
     */
    public boolean isTerminating()
    {
        if (_M == null) return true;
        if (_M._plotsWrapper == null) return true;
        if (_M._plotsWrapper.getModel() == null) return true;
        return _M._plotsWrapper.getModel().isTerminating();
    }

    /**
     * Clears memory.
     */
    @Override
    public void dispose()
    {
        Notification.printNotification(_M._GC, null, "Frame: dispose method called");
        _M._plotsWrapper.getModel().notifyTerminating();
        _C.dispose();
        _M.dispose();
        _C = null;
        _M = null;

    }
}
