package plot;

import color.Color;
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
     * If not null, the created screenshot is clipped so that its depicted object occupies all image; it is done by
     * removing the first/last columns/rows whose all pixels match the given color (RGBA format is compared).
     */
    private final Color _clipToFilColor;

    /**
     * Parameterized constructor.
     *
     * @param PC             reference to the plot container
     * @param screenshot     reference to the screenshot wrapper
     * @param clipToFilColor if not null, the created screenshot is clipped so that its depicted object occupies all
     *                       image; it is done by removing the first/last columns/rows whose all pixels match the given
     *                       color (RGB channels are compared and optionally A, if the image supports it)
     */
    public CreateAndWrapScreenshot(PlotContainer PC, Screenshot screenshot, Color clipToFilColor)
    {
        _PC = PC;
        _screenshot = screenshot;
        _clipToFilColor = clipToFilColor;
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
        if (_clipToFilColor != null) _screenshot.clipToFit(_clipToFilColor);

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
