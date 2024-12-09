package container;

/**
 * Abstract container object providing some basic and common functionalities.
 *
 * @author MTomczyk
 */


public abstract class AbstractVisualizationContainer
{
    /**
     * If true -> e.g., some notification can be printed to the console.
     */
    protected boolean _debugMode;

    /**
     * Parameterized constructor.
     *
     * @param debugMode if true -> e.g., some notification can be printed to the console
     */
    public AbstractVisualizationContainer(boolean debugMode)
    {
        _debugMode = debugMode;
    }

    /**
     * Can be called to print some messages. Messages are printed only if debug mode is on.
     *
     * @param msg message to be printed
     */
    public void printNotification(String msg)
    {
        printNotification(msg, false);
    }

    /**
     * Can be called to print some messages. Messages are printed only if debug mode is on.
     *
     * @param msg        message to be printed
     * @param forcePrint if true, message is printed without inspecting the debug flag.
     */
    public void printNotification(String msg, boolean forcePrint)
    {
        if ((forcePrint) || (_debugMode))
            System.out.println(msg);
    }
}
