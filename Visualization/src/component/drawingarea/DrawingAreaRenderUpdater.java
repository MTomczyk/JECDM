package component.drawingarea;

import component.drawingarea.renderingdata.AbstractRenderingData;
import component.drawingarea.renderingdata.Layer;
import container.PlotContainer;
import dataset.IDataSet;
import statistics.movingaverage.MovingAverageLong;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for updating the final render.
 *
 * @author MTomczyk
 */
public class DrawingAreaRenderUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    protected final PlotContainer _PC;

    /**
     * If true, render generation times are measured and stored.
     */
    protected boolean _measureRenderGenerationTimes;

    /**
     * Object storing render generation times measured (moving average).
     */
    protected MovingAverageLong _renderGenerationTimes;

    /**
     * Timestamp used when calculating processing time.
     */
    protected long pTime = -1;


    /**
     * Parameterized constructor.
     *
     * @param PC                           reference to the plot container
     * @param measureRenderGenerationTimes if true, render generation times are measured and stored
     * @param renderGenerationTimes        reference to the object storing render generation times measured (moving average)
     */
    public DrawingAreaRenderUpdater(PlotContainer PC,
                                    boolean measureRenderGenerationTimes,
                                    MovingAverageLong renderGenerationTimes)
    {
        _PC = PC;
        _measureRenderGenerationTimes = measureRenderGenerationTimes;
        _renderGenerationTimes = renderGenerationTimes;
    }


    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        if (_measureRenderGenerationTimes)
            pTime = System.nanoTime();

        AbstractRenderingData RD = _PC.getRenderingData();
        RD.setRenderToFlush(RD.getRender());

        // RENDER PHASE ======================================
        Layer currentRender = RD.createLayer(_PC.getPlot().getWidth(), _PC.getPlot().getHeight());
        ArrayList<IDataSet> dataSets = _PC.getDataSets();

        Graphics2D renderGraphics = (Graphics2D) currentRender.getGraphics();

        if (renderGraphics != null)
        {
            renderGraphics.setComposite(AlphaComposite.SrcOver);
            renderGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            _PC.getDrawingArea().paintAuxElements(renderGraphics);
            if (dataSets != null) for (IDataSet ds : dataSets) ds.draw(renderGraphics);
            renderGraphics.dispose();
            RD.setRender(currentRender);
        }
        else RD.setRender(null);

        if (_measureRenderGenerationTimes)
            _renderGenerationTimes.addData(System.nanoTime() - pTime);

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
