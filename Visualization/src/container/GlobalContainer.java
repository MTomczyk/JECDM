package container;

import frame.Frame;
import plotswrapper.AbstractPlotsWrapper;
import swing.swingworkerqueue.ExecutionBlock;

/**
 * Container-like object shared among different GUI objects.
 * Provides references and various functionalities.
 *
 * @author MTomczyk
 */
public class GlobalContainer extends AbstractVisualizationContainer
{
    /**
     * Reference to the main frame.
     */
    private final Frame _frame;

    /**
     * Parameterized constructor.
     *
     * @param frame     reference to the main frame
     * @param debugMode if true -> e.g., some notification can be printed to te console
     */
    public GlobalContainer(Frame frame, boolean debugMode)
    {
        super(debugMode);
        _frame = frame;
    }

    /**
     * Call to notify that the window has been opened or becomes visible.
     */
    public void notifyWindowVisible()
    {
        _frame.getModel().notifyWindowDisplayed();
    }

    /**
     * Call to notify that the window has been hidden (i.e., minimized).
     */
    public void notifyWindowHidden()
    {
        _frame.getModel().notifyWindowHidden();
    }

    /**
     * Getter for the main frame.
     *
     * @return the main frame
     */
    public Frame getFrame()
    {
        return _frame;
    }

    /**
     * Getter for plots wrapper.
     *
     * @return plots wrapper
     */
    public AbstractPlotsWrapper getPlotsWrapper()
    {
        return _frame.getModel().getPlotsWrapper();
    }

    /**
     * Registers workers (encapsulated in a block) for execution to the queue, which ensures that the background tasks
     * do not overlap, and they are executed in the order in which they were registered.
     *
     * @param executionBlock block of workers to be executed
     */
    public void registerWorkers(ExecutionBlock<Void, Void> executionBlock)
    {
        getPlotsWrapper().getController().registerWorkers(executionBlock);
    }

}
