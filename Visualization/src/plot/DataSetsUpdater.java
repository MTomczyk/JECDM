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
     * If true, he method updates data sets whose names match names of those provided; if a data set is in the input but
     * not in the currently maintained set, it is ignored; if a data set is not in the input but is in te currently
     * maintained set, it remains in the set but; if false, the method removes the currently maintained sets and uses
     * the input as the new ones/
     */
    private final boolean _updateMatchingOnly;

    /**
     * Parameterized constructor.
     *
     * @param PC                 reference to the plot container
     * @param dataSets           new data sets
     * @param updateLegend       if true, the legend is updated after the data is swapped
     * @param updateMatchingOnly if true, he method updates data sets whose names match names of those provided; if
     *                           a data set is in the input but not in the currently maintained set, it is ignored;
     *                           if a data set is not in the input but is in te currently maintained set, it remains
     *                           in the set but; if false, the method removes the currently maintained sets and uses
     *                           the input as the new ones
     */
    public DataSetsUpdater(PlotContainer PC, ArrayList<IDataSet> dataSets, boolean updateLegend, boolean updateMatchingOnly)
    {
        _PC = PC;
        _dataSets = dataSets;
        _updateLegend = updateLegend;
        _updateMatchingOnly = updateMatchingOnly;
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

        DSUpdaterData data = new DSUpdaterData(DS, _dataSets, _updateMatchingOnly);

        //dispose previously assigned data
        if (data._dispose != null)
            for (int i = 0; i < data._dispose.length; i++)
                if (data._dispose[i]) DS.get(i).dispose();

        if (data._newDataSets != null)
            for (int i = 0; i < data._newDataSets.size(); i++)
                if (data._newDataSets.get(i) != null)
                {
                    if (data._skipIDSUpdate != null)
                        data._newDataSets.get(i).setSkipIDSUpdates(data._skipIDSUpdate[i]);
                    data._newDataSets.get(i).setContainers(_PC.getPlot()._M._GC, _PC.getPlot()._M._PC);
                }

        _PC.getPlot().getModel().setDataSetsReference(data._newDataSets, _updateMatchingOnly);

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
