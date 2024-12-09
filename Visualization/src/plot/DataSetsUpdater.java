package plot;

import container.PlotContainer;
import dataset.IDataSet;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Swing worker object responsible for setting new data sets.
 * Since only one updater can be executed at a time, concurrency is avoided.
 *
 * @author MTomczyk
 */
class DataSetsUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Reference to the plot container.
     */
    private final PlotContainer _PC;

    /**
     * New data sets.
     */
    private final ArrayList<IDataSet> _dataSets;

    /**
     * if true, the legend is updated after the data is swapped
     */
    private final boolean _updateLegend;

    /**
     * Parameterized constructor.
     *
     * @param PC           reference to the plot container
     * @param dataSets     new data sets
     * @param updateLegend if true, the legend is updated after the data is swapped
     */
    public DataSetsUpdater(PlotContainer PC, ArrayList<IDataSet> dataSets, boolean updateLegend)
    {
        _PC = PC;
        _dataSets = dataSets;
        _updateLegend = updateLegend;
    }


    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        //dispose previously assigned data
        ArrayList<IDataSet> DS = _PC.getDataSets();
        if ((DS != null) && (!DS.equals(_dataSets))) // if not the same (accidentally)
            for (IDataSet ds : DS) if (ds != null) ds.dispose();

        // set data sets
        _PC.getPlot().getModel().setDataSetsReference(_dataSets);
        if (_dataSets != null)
            for (IDataSet ds : _dataSets)
                if (ds != null) ds.setContainers(_PC.getPlot()._M._GC, _PC.getPlot()._M._PC);

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
        if (_updateLegend) _PC.getPlot().updateLegend();

        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }


}
