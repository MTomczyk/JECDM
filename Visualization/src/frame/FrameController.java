package frame;

import container.Notification;
import listeners.FrameListener;
import listeners.MousePressedLooseFocus;
import listeners.WindowListener;

/**
 * Controller for the {@link Frame class}.
 *
 * @author MTomczyk
 */
public class FrameController
{
    /**
     * Reference to frame.
     */
    protected final Frame _frame;

    /**
     * Reference to frame model.
     */
    protected FrameModel _M;

    /**
     * Window state listener.
     */
    protected WindowListener _windowListener;

    /**
     * Frame listener.
     */
    protected FrameListener _frameListener;

    /**
     * Mouse click -> loose focus to all plots (listener -> action).
     * The goal is to lose focus on all plots anytime something that is not a plot is clicked.
     */
    protected MousePressedLooseFocus _mousePressedLooseFocus;

    /**
     * Parameterized constructor.
     *
     * @param frame reference to frame
     */
    public FrameController(Frame frame)
    {
        _frame = frame;
    }

    /**
     * Instantiates background threads
     * (also calls the same method for descendants (plots wrapper -> wrappers -> plots)).
     */
    protected void instantiateBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null, "Frame controller: instantiate background threads method called");
        _M._plotsWrapper.getController().instantiateBackgroundThreads();
    }

    /**
     * Starts all plot-related background threads (also calls the same method for descendants (plots wrapper -> wrappers -> plots)).
     */
    public void startBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null,  "Frame controller: start background threads method called");
        _M._plotsWrapper.getController().startBackgroundThreads();
    }

    /**
     * Stops all plot-related background threads (also calls the same method for descendants (plots wrapper -> wrappers -> plots)).
     */
    public void stopBackgroundThreads()
    {
        Notification.printNotification(_M._GC, null,  "Frame controller: stop background threads method called");
        _M._plotsWrapper.getController().stopBackgroundThreads();
    }

    /**
     * Registers key bindings (also calls the same method for descendants (plots wrapper -> wrappers -> plots)).
     */
    public void registerKeyBindings()
    {
        Notification.printNotification(_M._GC, null,  "Frame controller: register key bindings method called");
        _M._plotsWrapper.getController().registerKeyBindings();
    }

    /**
     * Instantiates and registers listeners (also calls the same method for descendants (plots wrapper -> wrappers -> plots)).
     */
    protected void instantiateListeners()
    {
        Notification.printNotification(_M._GC, null,  "Frame controller: instantiate listeners method called");

        _windowListener = new WindowListener(_M._GC);
        _frame.addWindowStateListener(_windowListener);
        _frame.addWindowListener(_windowListener);

        _frameListener = new FrameListener(_M._GC);
        _frame.addComponentListener(_frameListener);

        _mousePressedLooseFocus = new MousePressedLooseFocus(_M._GC);
        _frame.addMouseListener(_mousePressedLooseFocus);

        _M._plotsWrapper.getController().instantiateListeners();
    }

    /**
     * Auxiliary method for enabling the top-level listeners.
     */
    public void enableListeners()
    {
        if (_windowListener != null) _windowListener.enable();
        if (_mousePressedLooseFocus != null) _mousePressedLooseFocus.enable();
        if (_frameListener != null) _frameListener.enable();
    }

    /**
     * Auxiliary method for disabling the top-level listeners.
     */
    public void disableListeners()
    {
        if (_windowListener != null) _windowListener.disable();
        if (_mousePressedLooseFocus != null) _mousePressedLooseFocus.disable();
        if (_frameListener != null) _frameListener.disable();
    }

    /**
     * Unregisters listeners (also calls the same method for descendants (plots wrapper -> wrappers -> plots)).
     */
    protected void unregisterListeners()
    {
        Notification.printNotification(_M._GC, null,  "Frame controller: unregister listeners method called");

        _frame.removeWindowStateListener(_windowListener);
        _frame.removeWindowListener(_windowListener);
        _frame.removeComponentListener(_frameListener);
        _frame.removeMouseListener(_mousePressedLooseFocus);

        _M._plotsWrapper.getController().unregisterListeners();
    }

    /**
     * Setter for the frame model.
     *
     * @param frameModel frame model
     */
    public void setFrameModel(FrameModel frameModel)
    {
        _M = frameModel;
    }

    /**
     * Can be called to dispose the object and its children.
     */
    public void dispose()
    {
        Notification.printNotification(_M._GC, null,  "Frame controller: dispose method called");
        disableListeners();
        unregisterListeners();
        _windowListener = null;
        _frameListener = null;
        _mousePressedLooseFocus = null;
        _M = null;
    }
}
