package visualization;

/**
 * Interface for classes responsible for visualizing evolutionary processes.
 *
 * @author MTomczyk
 */

public interface IVisualization
{
    /**
     * Triggers data update. The new data is constructed, processed, and accordingly sent to plots,
     * as imposed by {@link updater.DataUpdater}.
     */
    void updateData();

    /**
     * Can be used to check if the window (JFrame) is opened (visible and drawn).
     *
     * @return true = the window (JFrame) is opened (visible and drawn), false otherwise
     */
    boolean isWindowDisplayed();

    /**
     * Supportive method for initialization.
     */
    void init();

    /**
     * Supportive method that displays the main frame.
     */
    void display();

    /**
     * Starts (resumes) awaiting background processes.
     */
    void startBackgroundThreads();

    /**
     * Pauses ongoing background processes.
     */
    void stopBackgroundThreads();

    /**
     * Supportive method that terminates visualization.
     */
    void dispose();
}
