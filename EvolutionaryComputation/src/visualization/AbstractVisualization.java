package visualization;

import frame.Frame;
import updater.DataUpdater;


/**
 * Default implementation of {@link IVisualization} for visualizing progression of EAs.
 *
 * @author MTomczyk
 */

public abstract class AbstractVisualization implements IVisualization
{
    /**
     * Main frame wrapping the plots
     */
    protected final Frame _frame;

    /**
     * Data updater (creates/processes/and sends data to be visualized to plots).
     */
    protected final DataUpdater _dataUpdater;

    /**
     * Parameterized constructor.
     *
     * @param frame       wrapped frame
     * @param dataUpdater data updater
     */
    public AbstractVisualization(Frame frame, DataUpdater dataUpdater)
    {
        _frame = frame;
        _dataUpdater = dataUpdater;
    }

    /**
     * Triggers data update. The new data is constructed, processed, and accordingly sent to plots,
     * as imposed by {@link updater.DataUpdater}.
     */
    @Override
    public void updateData()
    {
        if (_dataUpdater != null)
        {
            if (_frame.isTerminating()) return;
            _dataUpdater.update();
        }
    }

    /**
     * Can be used to check if the window (JFrame) is opened (visible and drawn).
     *
     * @return true = the window (JFrame) is opened (visible and drawn), false otherwise
     */
    @Override
    public boolean isWindowDisplayed()
    {
        if (_frame == null) return false;
        return (_frame.isWindowDisplayed());
    }

    /**
     * Supportive method for initialization.
     */
    @Override
    public void init()
    {

    }


    /**
     * Supportive method that displays the main frame.
     */
    @Override
    public void display()
    {
        if (_frame != null)
        {
            _frame.setVisible(true);
        }
    }


    /**
     * Starts (resumes) awaiting background processes.
     */
    @Override
    public void startBackgroundThreads()
    {
        if (_frame != null)
        {
            _frame.getController().startBackgroundThreads();
        }
    }

    /**
     * Pauses ongoing background processes.
     */
    @Override
    public void stopBackgroundThreads()
    {
        if (_frame != null)
        {
            _frame.getController().stopBackgroundThreads();
        }
    }

    /**
     * Supportive method that terminates visualization.
     */
    @Override
    public void dispose()
    {
        if (_frame != null)
        {
            _frame.setVisible(false);
            _frame.dispose();
        }
        if (_dataUpdater != null)
        {
            _dataUpdater.dispose();
        }
    }
}
