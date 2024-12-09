package container;

/**
 * Auxiliary class helping passing and printing messages through global/plot container.
 *
 * @author MTomczyk
 */


public class Notification
{
    /**
     * Prints the debug message (static method).
     * The method tries to deliver the message through the global container (printed if the debug flag is true),
     * or the plot container if the first is not available.
     * If the input global container is not available, the method exists.
     *
     * @param GC  global container
     * @param PC  plot container
     * @param msg message to be printed
     */
    public static void printNotification(GlobalContainer GC, PlotContainer PC, String msg)
    {
        printNotification(GC, PC, msg, false);
    }

    /**
     * Prints the debug message (static method).
     * The method tries to deliver the message through the global container (printed if the debug flag is true).
     * If the input global container is not available, the method exists.
     *
     * @param GC    global container
     * @param PC    plot container
     * @param msg   message to be printed
     * @param force if true, the message will be printed even if the debug flag in the container is false
     */
    public static void printNotification(GlobalContainer GC, PlotContainer PC, String msg, boolean force)
    {
        if (GC != null) GC.printNotification(msg, force);
        else if (PC != null) PC.printNotification(msg, force);
    }
}
