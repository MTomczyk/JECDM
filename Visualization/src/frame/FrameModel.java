package frame;


import container.GlobalContainer;
import container.Notification;
import plotswrapper.AbstractPlotsWrapper;

/**
 * Model for the {@link Frame class}.
 *
 * @author MTomczyk
 */
public class FrameModel
{
    /**
     * Global container (shared object; stores references, provides various functionalities).
     */
    protected final GlobalContainer _GC;

    /**
     * Reference to frame.
     */
    protected final Frame _frame;

    /**
     * Reference to frame controller.
     */
    protected FrameController _C;

    /**
     * Flag indicating whether the window is displayed (visible) or not (volatile).
     */
    protected volatile boolean _windowDisplayed = false;

    /**
     * Wrapper for the plots to be displayed.
     */
    protected AbstractPlotsWrapper _plotsWrapper;

    /**
     * Parameterized constructor.
     *
     * @param frame        reference to frame
     * @param plotsWrapper plots to be displayed
     * @param debugMode    if true -> e.g., some notification can be printed to the console
     */
    public FrameModel(Frame frame, AbstractPlotsWrapper plotsWrapper, boolean debugMode)
    {
        _frame = frame;
        _plotsWrapper = plotsWrapper;

        _GC = new GlobalContainer(frame, debugMode);
        Notification.printNotification(_GC, null, "Frame model: global container is set");
    }

    /**
     * Setter for the frame controller.
     *
     * @param frameController frame controller
     */
    public void setFrameController(FrameController frameController)
    {
        _C = frameController;
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_GC, null, "Frame model: dispose method called");
        _C = null;
        _plotsWrapper.dispose();
    }

    /**
     * Change the object's state to "window opened" (or visible) (boolean flag).
     */
    public void notifyWindowDisplayed()
    {
        _windowDisplayed = true;
    }

    /**
     * Change the object's state to "window hidden" (not visible, i.e., minimized) (boolean flag).
     */
    public void notifyWindowHidden()
    {
        _windowDisplayed = false;
    }

    /**
     * Getter for plots wrapper.
     *
     * @return plots wrapper
     */
    public AbstractPlotsWrapper getPlotsWrapper()
    {
        return _plotsWrapper;
    }

    /**
     * Getter for the global container.
     *
     * @return global container
     */
    public GlobalContainer getGlobalContainer()
    {
        return _GC;
    }


}
