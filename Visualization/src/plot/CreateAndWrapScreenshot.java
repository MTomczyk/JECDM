package plot;

import container.PlotContainer;
import swing.swingworkerqueue.QueuedSwingWorker;
import utils.Screenshot;

import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for creating the screenshot and passing it to a wrapper.
 *
 * @author MTomczyk
 */
class CreateAndWrapScreenshot extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * Reference to the screenshot wrapper.
     */
    private final Screenshot _screenshot;

    /**
     * Parameterized constructor.
     *
     * @param PC         reference to the plot container
     * @param screenshot reference to the screenshot wrapper
     */
    public CreateAndWrapScreenshot(PlotContainer PC, Screenshot screenshot)
    {
        _PC = PC;
        _screenshot = screenshot;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _screenshot._image = _PC.getPlot().getPlotScreenshot(_screenshot._useAlphaChannel);

        notifyTermination();
        return null;
    }

    /**
     * Finalizes data set update.
     */
    @Override
    protected void done()
    {
        if (isCancelled()) return;
        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}
