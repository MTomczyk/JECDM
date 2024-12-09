package component.drawingarea;

import container.PlotContainer;
import dataset.IDataSet;
import drmanager.DisplayRangesManager;
import swing.swingworkerqueue.QueuedSwingWorker;
import thread.swingworker.EventTypes;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for updating IDS structures.
 *
 * @author MTomczyk
 */
class DrawingAreaIDSUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * Event type that triggered the worker.
     */
    private final EventTypes _eventType;

    /**
     * Parameterized constructor.
     *
     * @param PC         reference to the plot container
     * @param eventType  event type that triggered the worker
     */
    public DrawingAreaIDSUpdater(PlotContainer PC, EventTypes eventType)
    {
        _PC = PC;
        _eventType = eventType;
    }


    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        ArrayList<IDataSet> dataSets = _PC.getDataSets();
        if (dataSets != null)
        {
            DisplayRangesManager DRM = _PC.getDisplayRangesManager().getClone();
            space.Dimension[] projectionBounds = _PC.getDrawingArea()._renderingData.getCopyOfProjectionBounds();

            for (IDataSet ds : dataSets)
            {
                if ((ds != null) && (ds.getData() != null))
                {
                    if ((_eventType.equals(EventTypes.ON_DATA_CHANGED)) || (_eventType.equals(EventTypes.ON_DEMAND)))
                    {
                        ds.beginDataProcessing(true);
                        ds.updateFirstLevelIDS(DRM, _eventType);
                    }
                    else ds.beginDataProcessing(false);

                    ds.updateSecondLevelIDS(projectionBounds, _eventType);
                    ds.updateThirdLevelIDS(_eventType);
                    ds.finishDataProcessing();
                }
            }
        }

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
