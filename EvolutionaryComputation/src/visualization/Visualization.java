package visualization;

import frame.Frame;
import plot.AbstractPlot;
import updater.DataUpdater;

/**
 * Default implementation of {@link IVisualization}.
 *
 * @author MTomczyk
 */
public class Visualization extends AbstractVisualization implements IVisualization
{

    /**
     * Parameterized constructor.
     *
     * @param plot plot to be wrapped
     * @param widthRelative  frame width relative to screen width
     * @param heightRelative frame height relative to screen height
     */
    public Visualization(AbstractPlot plot, float widthRelative, float heightRelative)
    {
        this(new Frame(plot, widthRelative, heightRelative), null);
    }

    /**
     * Parameterized constructor.
     *
     * @param frame wrapped frame
     */
    public Visualization(Frame frame)
    {
        this(frame, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param frame       wrapped frame
     * @param dataUpdater data updater
     */
    public Visualization(Frame frame, DataUpdater dataUpdater)
    {
        super(frame, dataUpdater);
    }
}
