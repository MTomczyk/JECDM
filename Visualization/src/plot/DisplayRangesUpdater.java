package plot;

import container.PlotContainer;
import dataset.IDataSet;
import drmanager.DisplayRangesManager;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for updating display ranges in the case when data sets a set in the model {@link PlotModel}.
 *
 * @author MTomczyk
 */
class DisplayRangesUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * Parameterized constructor.
     *
     * @param PC reference to the plot container
     */
    public DisplayRangesUpdater(PlotContainer PC)
    {
        _PC = PC;
    }


    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        ArrayList<IDataSet> DS = _PC.getDataSets();
        if (DS != null)
        {
            // update display ranges
            DisplayRangesManager.Report report = null;
            // work on a copy for safety
            DisplayRangesManager _processedDRM = _PC.getDisplayRangesManager().getClone();
            for (IDataSet ds : DS)
            {
                DisplayRangesManager.Report nr = _processedDRM.updateDisplayRanges(ds.getData(), ds.getSkipDisplayRangesUpdateMasks());
                if (report == null) report = nr;
                else report.mergeWith(nr); // report, could be used somewhere
            }

            // overwrite
            _PC.getDisplayRangesManager().overwriteWithValuesStoredIn(_processedDRM);
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
