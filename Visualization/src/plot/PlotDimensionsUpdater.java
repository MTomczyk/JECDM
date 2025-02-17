package plot;

import container.PlotContainer;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.awt.*;
import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for manually setting plot dimensions.
 *
 * @author MTomczyk
 */
class PlotDimensionsUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * New plot X-coordinate.
     */
    private final int _x;

    /**
     * New plot Y-coordinate.
     */
    private final int _y;

    /**
     * New plot width.
     */
    private final int _w;

    /**
     * New plot height.
     */
    private final int _h;


    /**
     * Parameterized constructor.
     *
     * @param PC reference to the plot container
     * @param x  new plot x-coordinate
     * @param y  new plot y-coordinate
     * @param w  new plot width
     * @param h  new plot height
     */
    public PlotDimensionsUpdater(PlotContainer PC, int x, int y, int w, int h)
    {
        _PC = PC;
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        _PC.getPlot().setBounds(_x, _y, _w, _h);
        _PC.getPlot().setPreferredSize(new Dimension(_w, _h));
        _PC.getPlot().updateLayout();
        _PC.getPlot().updateLegend();
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
